package org.milestogo.exceptions;

/**
 * Created by shekhargulati on 04/03/14.
 */
public class StatusNotFoundException extends RuntimeException {
    public StatusNotFoundException(String message) {
        super(message);
    }
}
