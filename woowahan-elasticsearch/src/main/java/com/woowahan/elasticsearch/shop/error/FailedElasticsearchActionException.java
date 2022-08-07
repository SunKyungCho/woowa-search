package com.woowahan.elasticsearch.shop.error;

public class FailedElasticsearchActionException extends RuntimeException{
    public FailedElasticsearchActionException() {
        super();
    }

    public FailedElasticsearchActionException(String message) {
        super(message);
    }

    public FailedElasticsearchActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedElasticsearchActionException(Throwable cause) {
        super(cause);
    }

    protected FailedElasticsearchActionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
