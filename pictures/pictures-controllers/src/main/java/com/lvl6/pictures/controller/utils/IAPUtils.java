package com.lvl6.pictures.controller.utils;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lvl6.gamesuite.common.properties.Globals;

@Component
public class IAPUtils implements InitializingBean{
    
    @Autowired
    Globals globals;

    public Globals getGlobals() {
	return globals;
    }

    public void setGlobals(Globals globals) {
	this.globals = globals;
    }
    

    /*APPLE'S VARIABLES*/
    public final String RECEIPT_DATA = "receipt-data";
    public final String STATUS = "status";
    public final String RECEIPT = "receipt";
    public final String TRANSACTION_ID = "transaction_id";
    public final String PRODUCT_ID = "product_id";
    public final String QUANTITY = "quantity";
    public final String BID = "bid";
    public final String BVRS = "bvrs";
    public final String APP_ITEM_ID = "app_item_id";
    public final String PURCHASE_DATE = "purchase_date";
    public final String PURCHASE_DATE_MS = "purchase_date_ms";

    //rubies
    public String PACKAGE1;
    public String PACKAGE2;
    public String PACKAGE3;
    public String PACKAGE4;
    public String PACKAGE5;

    //rubies on sale
    public String PACKAGE1SALE;
    public String PACKAGE2SALE;
    public String PACKAGE3SALE;
    public String PACKAGE4SALE;
    public String PACKAGE5SALE;

    //rubies for beginners
    public String PACKAGE1BSALE;
    public String PACKAGE2BSALE;
    public String PACKAGE3BSALE;
    public String PACKAGE4BSALE;
    public String PACKAGE5BSALE;

    //tokens, T1 = refill tokens
    public String PACKAGET1;

    //tokens on sale
    public String PACKAGET1SALE;

    //tokens for beginners
    public String PACKAGET1BSALE;

    public final String PACKAGE1IMG = "rubies1.png";
    public final String PACKAGE2IMG = "rubies2.png";
    public final String PACKAGE3IMG = "rubies3.png";
    public final String PACKAGE4IMG = "rubies4.png";
    public final String PACKAGE5IMG = "rubies5.png";
    public final String PACKAGET1IMG = "tokenrefill1.png";

    /*
     * 1- $1 for ? rubies
     * 2- $1 for  rubies
     * 3- $1 for  rubies
     * 4- $1 for  rubies
     * 5- $1 for  rubies
     */

    public final int PACKAGE_1_RUBIES = 50;
    public final int PACKAGE_2_RUBIES = 120;
    public final int PACKAGE_3_RUBIES = 250;
    public final int PACKAGE_4_RUBIES = 650;
    public final int PACKAGE_5_RUBIES = 1500;

    //T1 = refill tokens
    public final int PACKAGE_T1_TOKENS = 50000; //value doesn't matter

    public final double PACKAGE_1_PRICE = 0.99;
    public final double PACKAGE_2_PRICE = 1.99;
    public final double PACKAGE_3_PRICE = 4.99;
    public final double PACKAGE_4_PRICE = 9.99;
    public final double PACKAGE_5_PRICE = 19.99;

    public final double PACKAGE_1_SALE_PRICE = 3.99;
    public final double PACKAGE_2_SALE_PRICE = 7.99;
    public final double PACKAGE_3_SALE_PRICE = 15.99;
    public final double PACKAGE_4_SALE_PRICE = 39.99;
    public final double PACKAGE_5_SALE_PRICE = 74.99;

    public final double PACKAGE_1_BSALE_PRICE = 2.99;
    public final double PACKAGE_2_BSALE_PRICE = 4.99;
    public final double PACKAGE_3_BSALE_PRICE = 9.99;
    public final double PACKAGE_4_BSALE_PRICE = 24.99;
    public final double PACKAGE_5_BSALE_PRICE = 49.99;

    public final double PACKAGE_T1_PRICE = 0.99;

    public final double PACKAGE_T1_SALE_PRICE = 3.99;

    public final double PACKAGE_T1_BSALE_PRICE = 2.99;

    public final List<String> iapPackageNames = 
	    Arrays.asList(PACKAGE1, PACKAGE2, PACKAGE3, PACKAGE4, PACKAGE5);

    public final List<String> packageNames = 
	    Arrays.asList(PACKAGE1, PACKAGE2, PACKAGE3, PACKAGE4, PACKAGE5);
    public final List<Integer> packageGivenDiamonds = 
	    Arrays.asList(PACKAGE_1_RUBIES, PACKAGE_2_RUBIES, PACKAGE_3_RUBIES, 
		    PACKAGE_4_RUBIES, PACKAGE_5_RUBIES);

    public int getRubiesForPackageName(String packageName) {
	if (packageName.equals(PACKAGE1) || packageName.equals(PACKAGE1SALE) || packageName.equals(PACKAGE1BSALE)) {
	    return PACKAGE_1_RUBIES;
	}
	if (packageName.equals(PACKAGE2) || packageName.equals(PACKAGE2SALE) || packageName.equals(PACKAGE2BSALE)) {
	    return PACKAGE_2_RUBIES;
	}
	if (packageName.equals(PACKAGE3) || packageName.equals(PACKAGE3SALE) || packageName.equals(PACKAGE3BSALE)) {
	    return PACKAGE_3_RUBIES;
	}
	if (packageName.equals(PACKAGE4) || packageName.equals(PACKAGE4SALE) || packageName.equals(PACKAGE4BSALE)) {
	    return PACKAGE_4_RUBIES;
	}
	if (packageName.equals(PACKAGE5) || packageName.equals(PACKAGE5SALE) || packageName.equals(PACKAGE5BSALE)) {
	    return PACKAGE_5_RUBIES;
	}
	return 0;
    }
    
    public boolean isRefillTokensForPackageName(String packageName) {
	if (packageName.equals(PACKAGET1) || packageName.equals(PACKAGET1SALE) || packageName.equals(PACKAGET1BSALE)) {
	    return true;
	}
	return false;
    }

    public int getTokensForPackageName(String packageName) {
	if (packageName.equals(PACKAGET1) || packageName.equals(PACKAGET1SALE) || packageName.equals(PACKAGET1BSALE)) {
	    return PACKAGE_T1_TOKENS;
	}
	return 0;
    }

    public String getImageNameForPackageName(String packageName) {
	if (packageName.equals(PACKAGE1)) {
	    return PACKAGE1IMG;
	}
	if (packageName.equals(PACKAGE2)) {
	    return PACKAGE2IMG;
	}
	if (packageName.equals(PACKAGE3)) {
	    return PACKAGE3IMG;
	}
	if (packageName.equals(PACKAGE4)) {
	    return PACKAGE4IMG;
	}
	if (packageName.equals(PACKAGE5)) {
	    return PACKAGE5IMG;
	}
	if (packageName.equals(PACKAGET1)) {
	    return PACKAGET1IMG;
	}
	return null;
    }

    public double getCashSpentForPackageName(String packageName) {
	if (packageName.equals(PACKAGE1)) {
	    return PACKAGE_1_PRICE;
	}
	if (packageName.equals(PACKAGE2)) {
	    return PACKAGE_2_PRICE;
	}
	if (packageName.equals(PACKAGE3)) {
	    return PACKAGE_3_PRICE;
	}
	if (packageName.equals(PACKAGE4)) {
	    return PACKAGE_4_PRICE;
	}
	if (packageName.equals(PACKAGE5)) {
	    return PACKAGE_5_PRICE;
	}
	if (packageName.equals(PACKAGE1SALE)) {
	    return PACKAGE_1_SALE_PRICE;
	}
	if (packageName.equals(PACKAGE2SALE)) {
	    return PACKAGE_2_SALE_PRICE;
	}
	if (packageName.equals(PACKAGE3SALE)) {
	    return PACKAGE_3_SALE_PRICE;
	}
	if (packageName.equals(PACKAGE4SALE)) {
	    return PACKAGE_4_SALE_PRICE;
	}
	if (packageName.equals(PACKAGE5SALE)) {
	    return PACKAGE_5_SALE_PRICE;
	}
	if (packageName.equals(PACKAGE1BSALE)) {
	    return PACKAGE_1_BSALE_PRICE;
	}
	if (packageName.equals(PACKAGE2BSALE)) {
	    return PACKAGE_2_BSALE_PRICE;
	}
	if (packageName.equals(PACKAGE3BSALE)) {
	    return PACKAGE_3_BSALE_PRICE;
	}
	if (packageName.equals(PACKAGE4BSALE)) {
	    return PACKAGE_4_BSALE_PRICE;
	}
	if (packageName.equals(PACKAGE5BSALE)) {
	    return PACKAGE_5_BSALE_PRICE;
	}
	if (packageName.equals(PACKAGET1)) {
	    return PACKAGE_T1_PRICE;
	}
	return 0;
    }

    public boolean packageIsBeginnerSale(String packageName) {
	if (packageName.equals(PACKAGE1BSALE) || packageName.equals(PACKAGE2BSALE) || packageName.equals(PACKAGE3BSALE) || 
		packageName.equals(PACKAGE4BSALE) || packageName.equals(PACKAGE5BSALE)) {
	    return true;
	}
	return false;
    }

    //initializing here because globals isn't initialized
    //until this method executes
    @Override
    public void afterPropertiesSet() throws Exception {
	// TODO Auto-generated method stub
	//rubies
	    PACKAGE1 = globals.getAppleBundleId() + ".package1";
	    PACKAGE2 = globals.getAppleBundleId() + ".package2";
	    PACKAGE3 = globals.getAppleBundleId() + ".package3";
	    PACKAGE4 = globals.getAppleBundleId() + ".package4";
	    PACKAGE5 = globals.getAppleBundleId() + ".package5";

	    //rubies on sale
	    PACKAGE1SALE = globals.getAppleBundleId() + ".package1sale";
	    PACKAGE2SALE = globals.getAppleBundleId() + ".package2sale";
	    PACKAGE3SALE = globals.getAppleBundleId() + ".package3sale";
	    PACKAGE4SALE = globals.getAppleBundleId() + ".package4sale";
	    PACKAGE5SALE = globals.getAppleBundleId() + ".package5sale";

	    //rubies for beginners
	    PACKAGE1BSALE = globals.getAppleBundleId() + ".package1bsale";
	    PACKAGE2BSALE = globals.getAppleBundleId() + ".package2bsale";
	    PACKAGE3BSALE = globals.getAppleBundleId() + ".package3bsale";
	    PACKAGE4BSALE = globals.getAppleBundleId() + ".package4bsale";
	    PACKAGE5BSALE = globals.getAppleBundleId() + ".package5bsale";

	    //tokens, T1 = refill tokens
	    PACKAGET1 = globals.getAppleBundleId() + ".packageT1";

	    //tokens on sale
	    PACKAGET1SALE = globals.getAppleBundleId() + ".packageT1sale";

	    //tokens for beginners
	    PACKAGET1BSALE = globals.getAppleBundleId() + ".packageT1bsale";
    }
}
