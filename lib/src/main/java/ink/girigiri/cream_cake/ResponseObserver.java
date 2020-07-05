package ink.girigiri.cream_cake;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import ink.girigiri.cream_cake.exception.NoDataExceptionException;
import ink.girigiri.cream_cake.exception.ServerResponseException;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;


public abstract class ResponseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T response) {
        onSuccess(response);
        onFinish();
    }

    @Override
    public void onError(Throwable e) {

        if (e instanceof HttpException) {     //   HTTP错误
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException
                || e instanceof UnknownHostException) {   //   连接错误
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {   //  连接超时
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {   //  解析错误
            onException(ExceptionReason.PARSE_ERROR);
        } else if (e instanceof ServerResponseException) {
            onFail(e.getMessage());
        } else if (e instanceof NoDataExceptionException) {
            onSuccess(null);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
        onFinish();
    }

    @Override
    public void onComplete() {
    }

    /**
     * 请求成功
     *
     * @param response 服务器返回的数据
     */
    abstract public void onSuccess(T response);
    abstract public void errorMessage(String error);

    /**
     * 服务器返回数据，但响应码不为200
     *
     */
    /**
     * 服务器返回数据，但响应码不为1000
     */
    public void onFail(String message) {

    }

    public void onFinish() {
    }

    /**
     * 请求异常
     *
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:

                errorMessage((String)HttpConfig.getInstance().getApplication().getResources().getText(R.string.connect_error));
                break;

            case CONNECT_TIMEOUT:

                errorMessage((String) HttpConfig.getInstance().getApplication().getResources().getText(R.string.connect_timeout));
                break;

            case BAD_NETWORK:
                errorMessage((String) HttpConfig.getInstance().getApplication().getResources().getText(R.string.bad_network));
                break;

            case PARSE_ERROR:

                errorMessage((String) HttpConfig.getInstance().getApplication().getResources().getText(R.string.parse_error));
                break;

            case UNKNOWN_ERROR:
            default:

                errorMessage((String) HttpConfig.getInstance().getApplication().getResources().getText(R.string.unknown_error));
                break;
        }
    }

    /**
     * 请求网络失败原因
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
