package net.christopherliu.cacooapi;

/**
 * Created by Christopher Liu on 5/26/2016.
 */
public class InvalidKeyException extends Exception {
    public InvalidKeyException(String msg) {
        super(msg);
    }
    public InvalidKeyException(String msg, Exception internalException) {
        super(msg, internalException);
    }
}
