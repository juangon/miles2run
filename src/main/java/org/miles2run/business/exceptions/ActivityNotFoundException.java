package org.miles2run.business.exceptions;

/**
 * Created by shekhargulati on 04/03/14.
 */
public class ActivityNotFoundException extends RuntimeException {
    public ActivityNotFoundException(String message) {
        super(message);
    }
}
