package ink.girigiri.cream_cake.exception;


import ink.girigiri.cream_cake.HttpConfig;

/**
 * 服务器返回的异常
 */
public class ServerResponseException extends RuntimeException {

    private int errorCode;

    public ServerResponseException(int errorCode, String cause) {
        super(HttpConfig.getInstance().getErrorCode().getErrorMessage(errorCode, cause), new Throwable(cause));
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
