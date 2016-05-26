package net.christopherliu.cacooapi;

import android.os.AsyncTask;
import android.util.Log;

import net.christopherliu.cacooapi.types.Diagram;
import net.christopherliu.system.AsyncTaskResult;

/**
 * Created by Christopher Liu on 5/23/2016.
 * This task retrieves a list of diagrams from Cacoo.
 * The first parameter passed should be the API key.
 */
public class GetDiagramsTask extends AsyncTask<String, Void, AsyncTaskResult<Diagram[]>> {
    //TODO callback method is potentially a thread leak
    public DiagramsListener delegate = null;

    public GetDiagramsTask(DiagramsListener delegate) {
        this.delegate = delegate;
    }

    @Override
    protected AsyncTaskResult<Diagram[]> doInBackground(String... params) {
        CacooManager x = new CacooManager(params[0]);
        try {
            Diagram[] diagrams = x.downloadDiagrams();
            return new AsyncTaskResult(diagrams);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return new AsyncTaskResult(e);
        }
    }

    @Override
    protected void onPostExecute(AsyncTaskResult<Diagram[]> result) {
        delegate.handle(result);
    }
}