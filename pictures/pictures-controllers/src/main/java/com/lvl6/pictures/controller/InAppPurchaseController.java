package com.lvl6.pictures.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.controller.EventController;
import com.lvl6.gamesuite.common.events.RequestEvent;
import com.lvl6.gamesuite.common.noneventprotos.CommonEventProtocolProto.CommonEventProtocolRequest;
import com.lvl6.gamesuite.common.properties.Globals;
import com.lvl6.pictures.controller.utils.CreateNoneventProtoUtils;
import com.lvl6.pictures.controller.utils.IAPUtils;
import com.lvl6.pictures.eventprotos.InAppPurchaseEventProto.InAppPurchaseRequestProto;
import com.lvl6.pictures.eventprotos.InAppPurchaseEventProto.InAppPurchaseResponseProto;
import com.lvl6.pictures.eventprotos.InAppPurchaseEventProto.InAppPurchaseResponseProto.InAppPurchaseStatus;
import com.lvl6.pictures.eventprotos.UpdateClientProto.UpdateClientCurrencyResponseProto;
import com.lvl6.pictures.events.request.InAppPurchaseRequestEvent;
import com.lvl6.pictures.events.response.InAppPurchaseResponseEvent;
import com.lvl6.pictures.events.response.UpdateClientCurrencyResponseEvent;
import com.lvl6.pictures.noneventprotos.UserProto.BasicUserProto;
import com.lvl6.pictures.noneventprotos.UserProto.UserCurrencyProto;
import com.lvl6.pictures.po.Currency;
import com.lvl6.pictures.properties.PicturesPoConstants;
import com.lvl6.pictures.services.currency.CurrencyService;
import com.lvl6.pictures.services.inapppurchasehistory.InAppPurchaseHistoryService;


@Component
public class InAppPurchaseController extends EventController {

    private static Logger log = LoggerFactory.getLogger(new Object() {
    }.getClass().getEnclosingClass());

    private static final String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    private static final String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";

    public InAppPurchaseController() {
	numAllocatedThreads = 2;
    }

    @Autowired
    Globals globals;

    @Autowired
    protected CurrencyService currencyService;
    
    @Autowired
    protected IAPUtils iapUtils;
    
    @Autowired
    protected InAppPurchaseHistoryService inAppPurchaseHistoryService;
    
    @Autowired
    protected CreateNoneventProtoUtils createNoneventProtoUtils;

    @Override
    public RequestEvent createRequestEvent() {
	return new InAppPurchaseRequestEvent();
    }

    @Override
    public int getEventType() {
	return CommonEventProtocolRequest.C_IN_APP_PURCHASE_EVENT_VALUE;
    }

    /*
     * db stuff done before sending event to eventwriter/client because the
     * client's not waiting on it immediately anyways
     */
    // @SuppressWarnings("deprecation")
    @Override
    protected void processRequestEvent(RequestEvent event) throws Exception {
	InAppPurchaseRequestProto reqProto = ((InAppPurchaseRequestEvent) event)
		.getInAppPurchaseRequestProto();

	BasicUserProto senderProto = reqProto.getSender();
	String userId = senderProto.getUserId();
	String receipt = reqProto.getReceipt();

	InAppPurchaseResponseProto.Builder resBuilder = InAppPurchaseResponseProto.newBuilder();
	resBuilder.setSender(senderProto);
	resBuilder.setReceipt(reqProto.getReceipt());
	
	Date purchaseDate = null;

	try {
	    Currency c = getCurrencyService().getCurrencyForUser(userId);
	    int previousTokens = c.getTokens();
//	    int previousRubies = c.getRubies();

	    JSONObject response;

	    JSONObject jsonReceipt = new JSONObject();
	    jsonReceipt.put(getIapUtils().RECEIPT_DATA, receipt);
	    log.info("Processing purchase: " + jsonReceipt.toString(4));
	    // Send data
	    URL url = new URL(PRODUCTION_URL);

	    log.info("Sending purchase request to: " + url.toString());

	    URLConnection conn = url.openConnection();
	    conn.setDoOutput(true);
	    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	    wr.write(jsonReceipt.toString());
	    wr.flush();

	    // Get the response
	    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

	    String responseString = "";
	    String line;
	    while ((line = rd.readLine()) != null) {
		responseString += line;
	    }
	    log.info("Response: " + responseString);

	    response = new JSONObject(responseString);

	    int responseStatus = response.getInt(getIapUtils().STATUS); 
	    if (responseStatus == 21007 || responseStatus == 21008) {
		//in app purchase on development server
		wr.close();
		rd.close();
		url = new URL(SANDBOX_URL);
		conn = url.openConnection();
		conn.setDoOutput(true);
		wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(jsonReceipt.toString());
		wr.flush();
		rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		responseString = "";
		while ((line = rd.readLine()) != null) {
		    responseString += line;
		}
		response = new JSONObject(responseString);
	    }

	    JSONObject receiptFromApple = null;
	    if (response.getInt(getIapUtils().STATUS) == 0) {
		receiptFromApple = response.getJSONObject(getIapUtils().RECEIPT);
		
		long transactionId = receiptFromApple.getLong(getIapUtils().TRANSACTION_ID);
		boolean duplicateTransaction = getInAppPurchaseHistoryService()
			.checkIfDuplicateTransaction(transactionId);
		if (!duplicateTransaction) {
		    try {
			String packageName = receiptFromApple.getString(getIapUtils().PRODUCT_ID);
			int rubiesChange = getIapUtils().getRubiesForPackageName(packageName);
			int tokensChange = getIapUtils().getTokensForPackageName(packageName);
			boolean refillTokens = getIapUtils().isRefillTokensForPackageName(packageName);
			double cashCost = getIapUtils().getCashSpentForPackageName(packageName);
			//boolean isBeginnerSale = getIapUtils().packageIsBeginnerSale(packageName);
			purchaseDate = new Date(receiptFromApple.getLong(getIapUtils().PURCHASE_DATE_MS));

			if (rubiesChange > 0) {
			    resBuilder.setRubiesGained(rubiesChange);
			    getCurrencyService().relativelyUpdateRubiesForUser(c, rubiesChange);
			} else if (refillTokens){
			    tokensChange = PicturesPoConstants.CURRENCY__DEFAULT_MAX_TOKENS -
				    previousTokens;
			    resBuilder.setTokensGained(tokensChange);
			    int newTokens = previousTokens + tokensChange;
			    //since refilled tokens, change the time as well
			    getCurrencyService().updateTokensForUser(c, newTokens, purchaseDate);
			}

			//keep track that user purchased something
			getInAppPurchaseHistoryService().recordNewInAppPurchase(
				userId, transactionId, rubiesChange, tokensChange,
				refillTokens, purchaseDate);
			resBuilder.setStatus(InAppPurchaseStatus.SUCCESS);
			resBuilder.setPackageName(receiptFromApple.getString(getIapUtils().PRODUCT_ID));

			resBuilder.setPackagePrice(cashCost);
			log.info("successful in-app purchase from user " + c.getUserId() + " for package "
				+ receiptFromApple.getString(getIapUtils().PRODUCT_ID));

			//Timestamp date = new Timestamp((new Date()).getTime());
//			writeToUserCurrencyHistory(user, date, rubiesChange, tokensChange, previousTokens, previousRubies);
		    } catch (Exception e) {
			log.error("problem with in app purchase flow", e);
		    }
		} else {
		    resBuilder.setStatus(InAppPurchaseStatus.FAIL_DUPLICATE_RECEIPT);
		    log.error("duplicate receipt from user " + c.getUserId());
		}
	    } else {
		log.error("problem with in-app purchase that client sent, with receipt " + receipt);
	    }

	    wr.close();
	    rd.close();

	    if (!resBuilder.hasStatus()) {
		resBuilder.setStatus(InAppPurchaseStatus.FAIL_OTHER);
	    }

	    InAppPurchaseResponseProto resProto = resBuilder.build();

	    InAppPurchaseResponseEvent resEvent = new InAppPurchaseResponseEvent(userId);
	    resEvent.setTag(event.getTag());
	    resEvent.setInAppPurchaseResponseProto(resProto);
	    getEventWriter().handleEvent(resEvent);

//	    if (Globals.KABAM_ENABLED()) {
//		if (receiptFromApple != null && resBuilder.getStatus() == InAppPurchaseStatus.SUCCESS) {
//		    JSONObject logJson = getKabamJsonLogObject(reqProto, resBuilder, receiptFromApple);
//		    List<NameValuePair> queryParams = getKabamQueryParams(receipt, user, logJson);
//		    doKabamPost(queryParams, 0);
//		}
//	    }

	    if (null != purchaseDate) {
		UpdateClientCurrencyResponseEvent resEventUpdate =
			new UpdateClientCurrencyResponseEvent(userId);
		resEventUpdate.setTag(event.getTag());
		UpdateClientCurrencyResponseProto.Builder uccrpb = 
			UpdateClientCurrencyResponseProto.newBuilder();
		uccrpb.setBup(senderProto);
		UserCurrencyProto ucp = getCreateNoneventProtoUtils().createUserCurrencyProto(c);
		uccrpb.setUcp(ucp);
		uccrpb.setTimeOfUserUpdate(purchaseDate.getTime());
		resEventUpdate.setUpdateClientResponseProto(uccrpb.build());
		getEventWriter().handleEvent(resEventUpdate);
	    }

	} catch (Exception e) {
	    log.error("exception in InAppPurchaseController processEvent", e);
	    resBuilder.setStatus(InAppPurchaseStatus.FAIL_OTHER);
	    InAppPurchaseResponseProto resProto = resBuilder.build();
	    InAppPurchaseResponseEvent resEvent = new InAppPurchaseResponseEvent(userId);
	    resEvent.setTag(event.getTag());
	    resEvent.setInAppPurchaseResponseProto(resProto);

	} 
    }

//    private void doKabamPost(List<NameValuePair> queryParams, int numTries) {
//	log.info("Posting to Kabam");
//	String host = globals.getSandbox() ? KabamProperties.SANDBOX_PAYMENT_URL : KabamProperties.PRODUCTION_PAYMENT_URL;
//	HttpClient client = new DefaultHttpClient();
//	HttpPost post = new HttpPost(host);
//	try {
//	    log.info ("Sending post query: " + queryParams);
//	    post.setEntity(new UrlEncodedFormEntity(queryParams, Consts.UTF_8));
//	    HttpResponse response = client.execute(post);
//	    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//	    String responseString = "";
//	    String line;
//	    while ((line = rd.readLine()) != null) {
//		responseString += line;
//	    }
//	    log.info("Received response: " + responseString);
//
//	    JSONObject jsonResponse = new JSONObject(responseString);
//	    if (!jsonResponse.getBoolean("success")) {
//		log.error("Failed to log kabam payment with errorcode: "+jsonResponse.getInt("errorcode")+ " and errormessage: "+jsonResponse.getString("errormessage"));
//		if (numTries < 10) {
//		    doKabamPost(queryParams, numTries+1);
//		} else {
//		    log.error("Giving up..");
//		}
//	    }
//	} catch (Exception e) {
//	    log.error("Error doing Kabam post", e);
//	}
//    }

//    private List<NameValuePair> getKabamQueryParams(String receipt, User user, JSONObject logJson)throws NoSuchAlgorithmException {
//	log.info("Generating Post parameters");
//	int gameid = Globals.getSandbox() ? KabamProperties.SANDBOX_CLIENT_ID : KabamProperties.PRODUCTION_CLIENT_ID;
//	String secret = Globals.getSandbox() ? KabamProperties.SANDBOX_SECRET : KabamProperties.PRODUCTION_SECRET;
//	long time = new Date().getTime() / 1000;
//	List<NameValuePair> queryParams = new ArrayList<NameValuePair>();
//	queryParams.add(new BasicNameValuePair("gameid", ""+gameid));
//	queryParams.add(new BasicNameValuePair("log", logJson.toString()));
//	queryParams.add(new BasicNameValuePair("mobileid", user.getUdid()));
//	queryParams.add(new BasicNameValuePair("receipt", receipt));
//	queryParams.add(new BasicNameValuePair("timestamp", "" + time));
//	queryParams.add(new BasicNameValuePair("userid", "" + user.getKabamNaid()));
//	String str = "";
//	for (NameValuePair key : queryParams) {
//	    str += key.getName() + key.getValue();
//	}
//	str += secret;
//	queryParams.add(new BasicNameValuePair("sig", sha1(str)));
//	return queryParams;
//    }

//    private JSONObject getKabamJsonLogObject(InAppPurchaseRequestProto reqProto,
//	    InAppPurchaseResponseProto.Builder resBuilder, JSONObject receiptFromApple) throws JSONException {
//	Map<String, Object> logParams = new TreeMap<String, Object>();
//	logParams.put("serverid", "1");
//	logParams.put("localcents", reqProto.getLocalcents());
//	logParams.put("localcurrency", reqProto.getLocalcurrency());
//	logParams.put("igc", resBuilder.hasDiamondsGained() ? resBuilder.getDiamondsGained() : resBuilder.getCoinsGained());
//	logParams.put("igctype", resBuilder.hasDiamondsGained() ? "gold" : "silver");
//	logParams.put("transactionid", receiptFromApple.get(getIapUtils().TRANSACTION_ID));
//	logParams.put("platform", "itunes");
//	logParams.put("locale", reqProto.getLocale());
//	logParams.put("lang", "en");
//	logParams.put("ipaddr", reqProto.getIpaddr());
//	JSONObject logJson = new JSONObject(logParams);
//	return logJson;
//    }

//    private static String sha1(String input) throws NoSuchAlgorithmException {
//	MessageDigest mDigest = MessageDigest.getInstance("SHA1");
//	byte[] result = mDigest.digest(input.getBytes());
//	StringBuffer sb = new StringBuffer();
//	for (int i = 0; i < result.length; i++) {
//	    sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
//	}
//
//	return sb.toString();
//    }

//    private void writeToUserCurrencyHistory(User aUser, Timestamp date,
//	    int rubiesChange, int tokensChange, int previousTokens, int previousRubies) {
//	Map<String, Integer> previousRubiesSilver = new HashMap<String, Integer>();
//	Map<String, Integer> goldSilverChange = new HashMap<String, Integer>();
//	Map<String, String> reasonsForChanges = new HashMap<String, String>();
//	String gold = MiscMethods.gold;
//	String silver = MiscMethods.silver;
//	String reasonForChange = ControllerConstants.UCHRFC__IN_APP_PURCHASE;
//
//	if (0 < rubiesChange) {
//	    goldSilverChange.put(gold, rubiesChange);
//	    previousRubiesSilver.put(gold, previousRubies);
//	    reasonsForChanges.put(gold, reasonForChange + gold);
//	} 
//	if (0 < tokensChange) {
//	    goldSilverChange.put(silver, tokensChange);
//	    previousRubiesSilver.put(silver, previousTokens);
//	    reasonsForChanges.put(silver, reasonForChange + silver);
//	}
//
//	MiscMethods.writeToUserCurrencyOneUserGoldAndOrSilver(aUser, date,
//		goldSilverChange, previousRubiesSilver, reasonsForChanges);
//    }


    public Globals getGlobals() {
	return globals;
    }

    public void setGlobals(Globals globals) {
	this.globals = globals;
    }

    public CurrencyService getCurrencyService() {
        return currencyService;
    }

    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public IAPUtils getIapUtils() {
        return iapUtils;
    }

    public void setIapUtils(IAPUtils iapUtils) {
        this.iapUtils = iapUtils;
    }

    public InAppPurchaseHistoryService getInAppPurchaseHistoryService() {
        return inAppPurchaseHistoryService;
    }

    public void setInAppPurchaseHistoryService(
    	InAppPurchaseHistoryService inAppPurchaseHistoryService) {
        this.inAppPurchaseHistoryService = inAppPurchaseHistoryService;
    }

    public CreateNoneventProtoUtils getCreateNoneventProtoUtils() {
        return createNoneventProtoUtils;
    }

    public void setCreateNoneventProtoUtils(
    	CreateNoneventProtoUtils createNoneventProtoUtils) {
        this.createNoneventProtoUtils = createNoneventProtoUtils;
    }

}
