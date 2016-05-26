package net.christopherliu.cacooapi;

import android.os.AsyncTask;
import android.util.Log;

import net.christopherliu.cacooapi.types.AccountInfo;
import net.christopherliu.system.AsyncTaskResult;

/**
 * Created by Christopher Liu on 5/23/2016.
 * This task retrieves account information from Cacoo.
 * The first parameter passed should be the API key.
 */
public class GetAccountInfoTask extends AsyncTask<String, Void, AsyncTaskResult<AccountInfo>> {
    //TODO callback method is potentially a thread leak
    public AccountInfoListener delegate = null;

    public GetAccountInfoTask(AccountInfoListener delegate) {
        this.delegate = delegate;
    }

    @Override
    protected AsyncTaskResult<AccountInfo> doInBackground(String... params) {
        CacooManager x = new CacooManager(params[0]);
        try {
            AccountInfo accountInfo = x.downloadAccountInfo();
            return new AsyncTaskResult<AccountInfo>(accountInfo);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return new AsyncTaskResult<AccountInfo>(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<AccountInfo> result) {
        delegate.handle(result);
    }
}