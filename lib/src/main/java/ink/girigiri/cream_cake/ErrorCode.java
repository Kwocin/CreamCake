package ink.girigiri.cream_cake;


public  class ErrorCode {
    /**
     * request success
     */
    public static final int SUCCESS = 200;

    /**
     * 异常信息返回
     * @param errorCode code 代码
     * @param cause message 内容
     * @return
     */
    public static String getErrorMessage(int errorCode, String cause) {
        return cause;
    }
}
