package net.christopherliu.cacooapi;

import net.christopherliu.cacooapi.types.AccountInfo;
import net.christopherliu.system.AsyncTaskResult;

/**
 * Created by Christopher Liu on 5/23/2016.
 */
public interface AccountInfoListener {
    /**
     * Responds to new incoming AccountInfo
     * @param accountInfo
     */
    void handle(AsyncTaskResult<AccountInfo> accountInfo);
}
