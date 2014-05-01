package org.miles2run.exceptions;

import java.io.Serializable;

/**
 * Created by shekhargulati on 21/03/14.
 */
public class ViewException extends RuntimeException implements Serializable {

    public ViewException() {
    }

    public ViewException(String message) {
        super(message);
    }

    public ViewException(String message, Throwable cause) {
        super(message, cause);
    }
}
