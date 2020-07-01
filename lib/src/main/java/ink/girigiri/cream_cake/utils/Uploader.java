package ink.girigiri.cream_cake.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ink.girigiri.lib.RetrofitCreator;
import ink.girigiri.lib.api.RxServerApi;
import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class Uploader {

    private String fieldName;
    private File file;
    private HashMap<String, Object> params;


    /**
     * @param fieldName   属性名
     * @param file        上传的文件
     * @param otherParams 其他参数
     */
    public Uploader(String fieldName, File file, HashMap<String, Object> otherParams) {
        this.fieldName = fieldName;
        this.file = file;
        this.params = params;
    }


    public Observable<String> defaultUpload(String url) {
        RxServerApi serverApi = RetrofitCreator.getBaseRetrofitBuilder().build().create(RxServerApi.class);
        RequestBody requestBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
        MultipartBody.Part body
                = MultipartBody.Part.createFormData(fieldName, file.getName(), requestBody);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null) {
            List<String> keyList = new ArrayList<>(params.keySet());
            for (int i = 0; i < params.size(); i++) {
                String key = keyList.get(i);
                builder.addFormDataPart(key, params.get(key).toString());
            }

        }
        return serverApi.upload(url, body, builder.build().parts());
    }
}


