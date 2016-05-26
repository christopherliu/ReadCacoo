package net.christopherliu.system;

/**
 * This class boxes up the result from an AsyncTask, and allows us to return an error if one occurs
 * during the execution of that AsyncTask.
 * <p/>
 * From: http://stackoverflow.com/a/6312491/40352
 * Created by Christopher Liu on 5/26/2016.
 */
public class AsyncTaskResult<T> {
    private T result;
    private Exception error;

    public T getResult() {
        return result;
    }

    public Exception getError() {
        return error;
    }

    public AsyncTaskResult(T result) {
        super();
        this.result = result;
    }

    public AsyncTaskResult(Exception error) {
        super();
        this.error = error;
    }
}