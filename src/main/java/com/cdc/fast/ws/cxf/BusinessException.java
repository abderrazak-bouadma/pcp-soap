package com.cdc.fast.ws.cxf;

/**
 * Created with IntelliJ IDEA.
 * User: Utilistateur
 * Date: 07/06/13
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class BusinessException extends RuntimeException {

    public BusinessException() {
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
