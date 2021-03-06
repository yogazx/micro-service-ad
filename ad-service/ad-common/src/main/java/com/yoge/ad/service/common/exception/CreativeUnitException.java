package com.yoge.ad.service.common.exception;

public class CreativeUnitException extends AdException {
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public CreativeUnitException(String message) {
        super(message);
    }
}
