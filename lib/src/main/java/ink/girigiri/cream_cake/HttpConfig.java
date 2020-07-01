package ink.girigiri.cream_cake;

import android.app.Application;

public class HttpConfig {
    private  Application APPLICATION;
    private  int TIMEOUT=5000;
    private  String HOST="";
    private  ErrorCode ERROR_CODE=new ErrorCode();
    private  static HttpConfig instance = new HttpConfig();
    private HttpConfig(){}
    public static HttpConfig  getInstance(){
        return instance;
    }
    public  HttpConfig init(Application application){
        instance.APPLICATION=application;
        return instance;
    }
    public HttpConfig timeOut(int TIMEOUT){
        instance.TIMEOUT=TIMEOUT;
        return instance;
    }
    public HttpConfig host(String host){
        instance.HOST=host;
        return instance;
    }
    public HttpConfig errorCode(ErrorCode errorCode){
        instance.ERROR_CODE=errorCode;
        return instance;
    }

    public Application getApplication() {
        return APPLICATION;
    }

    public int getTimeOut() {
        return TIMEOUT;
    }

    public String getHost() {
        return HOST;
    }

    public ErrorCode getErrorCode() {
        return ERROR_CODE;
    }
}
