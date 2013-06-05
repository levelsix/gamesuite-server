package com.lvl6.gamesuite.common.properties;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;




public class Globals implements ApplicationContextAware {
	
	
	protected String appleBundleId;
	protected String appStoreUrl;
	protected String reviewPageUrl;
	protected boolean kabamEnabled = true;
	protected boolean sandbox = true;
	protected boolean iddictionOn = true;
	protected float versionNumber = 1.0f;
	protected int healthCheckTimeoutSeconds = 6;
	
	
	
	public boolean isKabamEnabled() {
		return kabamEnabled;
	}

	public void setKabamEnabled(boolean kabamEnabled) {
		this.kabamEnabled = kabamEnabled;
	}

	public String getAppleBundleId() {
		return appleBundleId;
	}

	public void setAppleBundleId(String appleBundleId) {
		this.appleBundleId = appleBundleId;
	}

	public String getAppStoreUrl() {
		return appStoreUrl;
	}

	public void setAppStoreUrl(String appStoreUrl) {
		this.appStoreUrl = appStoreUrl;
	}

	public String getReviewPageUrl() {
		return reviewPageUrl;
	}

	public void setReviewPageUrl(String reviewPageUrl) {
		this.reviewPageUrl = reviewPageUrl;
	}

    public int getHealthCheckTimeoutSeconds() {
		return healthCheckTimeoutSeconds;
	}

	public void setHealthCheckTimeoutSeconds(int healthCheckTimeoutSeconds) {
		this.healthCheckTimeoutSeconds = healthCheckTimeoutSeconds;
	}

	public float getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(float versionNumber) {
		this.versionNumber = versionNumber;
	}

	public boolean isIddictionOn() {
		return iddictionOn;
	}

	public void setIddictionOn(boolean iddictionOn) {
		this.iddictionOn = iddictionOn;
	}

	public boolean getSandbox() {
		return sandbox;
	}

	public void setSandbox(boolean isSandbox) {
		this.sandbox = isSandbox;
	}

	/** size of ByteBuffer for reading/writing from channels */

    public final int NET_BUFFER_SIZE=16384*64;

    /** maximum event size in bytes */
    public final int MAX_EVENT_SIZE=16384*64;

    /** interval to sleep between attempts to write to a channel. */
    public final long CHANNEL_WRITE_SLEEP = 10L;

    /** number of worker threads for EventWriter */
    public final int EVENT_WRITER_WORKERS = 5;
    
    /** number of worker threads for APNSWriter */
    public final int APNS_WRITER_WORKERS = 5;

    /** default number of workers for GameControllers */
    public final int DEFAULT_CONTROLLER_WORKERS = 2;

    public final boolean IS_SANDBOX(){return appContext.getBean(Globals.class).getSandbox();};
    
    public final boolean KABAM_ENABLED() {return appContext.getBean(Globals.class).isKabamEnabled();};

    public final int NUM_MINUTES_DIFFERENCE_LEEWAY_FOR_CLIENT_TIME = 10;
    
    public final String APP_STORE_URL() {return appContext.getBean(Globals.class).getAppStoreUrl();};//"itms-apps://itunes.com/apps/ageofchaos";
    public final String REVIEW_PAGE_URL() {return appContext.getBean(Globals.class).getReviewPageUrl();};//= "itms-apps://itunes.apple.com/WebObjects/MZStore.woa/wa/viewContentsUserReviews?id=548520325&pageNumber=0&sortOrdering=1&type=Purple+Software";
    public final String REVIEW_PAGE_CONFIRMATION_MESSAGE = "Awesome! Rate us 5 Stars in the App Store to keep the updates coming!";
    
    //public final Level LOG_LEVEL = Level.INFO;
    
    public final int NUM_SECONDS_FOR_CONTROLLER_PROCESS_EVENT_LONGTIME_LOG_WARNING = 1;
    
    public final String APPLE_BUNDLE_ID(){return appContext.getBean(Globals.class).getAppleBundleId();};// = "com.lvl6.kingdom";

    public final float VERSION_NUMBER(){return appContext.getBean(Globals.class).getVersionNumber();};

    public int HEALTH_CHECK_TIMEOUT() {return appContext.getBean(Globals.class).getHealthCheckTimeoutSeconds();};
    
    



    public final boolean IDDICTION_ON(){return appContext.getBean(Globals.class).isIddictionOn();}

    protected ApplicationContext appContext;
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		appContext = context;
	};

}