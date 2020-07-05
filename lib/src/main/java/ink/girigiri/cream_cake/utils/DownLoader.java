package ink.girigiri.cream_cake.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import ink.girigiri.cream_cake.RetrofitCreator;
import ink.girigiri.cream_cake.api.RxServerApi;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * 下载
 */
public class DownLoader {
    private String destDir;
    private String fileName;
    private HashMap<String, Object> params;
    private DownloadCallBack callBack;

    public DownLoader(String destDir, String fileName, HashMap<String, Object> params) {
        this.destDir = destDir;
        this.fileName = fileName;
        this.params = params;
    }
    public void setDownLoadCallBack(DownloadCallBack callBack){
        this.callBack = callBack;
    }
    public void defaultDownLoad(String url) {
        RxServerApi serverApi = RetrofitCreator.getBaseRetrofitBuilder().build().create(RxServerApi.class);
        Observable<Response<ResponseBody>> downloadResponse = serverApi.download(url, params);
        analyDownLoadResponse(downloadResponse);
    }
    public void postDownLoad(String url) {
        RxServerApi serverApi = RetrofitCreator.getBaseRetrofitBuilder().build().create(RxServerApi.class);
        Observable<Response<ResponseBody>> downloadResponse = serverApi.postDownload(url, params);
        analyDownLoadResponse(downloadResponse);
    }
    public void downLoad(Observable<Response<ResponseBody>> observable){
        analyDownLoadResponse(observable);
    }

    /**
     * 解析下载文件
     * @param observable
     */
    private void analyDownLoadResponse(Observable<Response<ResponseBody>> observable) {
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe((response) -> {
                    //拿到响应体
                    ResponseBody responseBody = response.body();
                    InputStream inputStream = null;
                    long total = 0;
                    long responseLength;
                    int progress=0;
                    if (responseBody.contentType().type().equals("json")){
                        if (callBack!=null) {
                            callBack.onError("Is not a file");
                        }
                        return;
                    }
                    FileOutputStream fos = null;
                    try {
                        byte[] buff = new byte[2048];
                        int len;
                        responseLength = responseBody.contentLength();
                        inputStream = responseBody.byteStream();
                        final File file = new File(destDir, fileName);
                        File dir = new File(destDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        fos = new FileOutputStream(file);
                        while ((len = inputStream.read(buff)) != -1) {
                            fos.write(buff, 0, len);
                            total += len;
                            progress = (int) ((total * 100) / responseLength);
                            if (callBack!=null) {
                                callBack.onProgress(progress);
                            }
                        }
                        if (callBack!=null) {
                            callBack.onCompleted();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (fos != null) {
                            try {
                                fos.flush();
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }, (e) -> {
                    if (callBack!=null) {
                     callBack.onError(e.getMessage());
                    }
                });
    }

    /**
     * 下载回调接口
     */
    public interface DownloadCallBack {
        void onError(String msg);

        void onProgress(int progress);

        void onCompleted();
    }
}
