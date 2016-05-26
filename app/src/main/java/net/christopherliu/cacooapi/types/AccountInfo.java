package net.christopherliu.cacooapi.types;

import android.graphics.Bitmap;

import net.christopherliu.cacooapi.InvalidKeyException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Christopher Liu on 5/23/2016.
 * Data object
 */
public class AccountInfo {
    //TODO make these members private
    public JSONObject accountInfo;
    private JSONObject licenseInfo;
    public Bitmap accountImage;
    public String nickname;
    public String plan;

    public AccountInfo(JSONObject accountInfo, JSONObject licenseInfo) throws JSONException, InvalidKeyException {
        this.accountInfo = accountInfo;
        this.licenseInfo = licenseInfo;
        //TODO null return is not necessarily equivalent to invalid key
        if (accountInfo == null || licenseInfo == null) {
            throw new InvalidKeyException("The API key specified does not return a valid account from Cacoo.");
        }
        this.nickname = accountInfo.getString("nickname");
        this.plan = licenseInfo.getString("plan");
    }

    public String getFriendlyPlanText() {
        if (this.plan == "plus") {
            return "Plus Plan";
        }
        else {
            return "Free Plan";
        }
    }
}
