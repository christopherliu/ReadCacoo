package net.christopherliu.cacooapi.types;

import android.graphics.Bitmap;

/**
 * Created by Christopher Liu on 5/23/2016.
 * Data object
 */
public class AccountInfo {
    // TODO consider private variables with accessors
    public Bitmap accountImage;
    public String nickname;
    public String plan;

    public AccountInfo(Bitmap accountImage, String nickname, String plan) {
        this.accountImage = accountImage;
        this.nickname = nickname;
        this.plan = plan;
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
