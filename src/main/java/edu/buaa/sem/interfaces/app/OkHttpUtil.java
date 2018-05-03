package edu.buaa.sem.interfaces.app;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static OkHttpClient mOkHttpClient;

    static {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.readTimeout(30, TimeUnit.SECONDS);//读取超时
        clientBuilder.connectTimeout(10, TimeUnit.SECONDS);//连接超时
        clientBuilder.writeTimeout(60, TimeUnit.SECONDS);//写入超时
        // 运行请求重定向
        clientBuilder.followRedirects(true);
        // https支持
        clientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        clientBuilder.sslSocketFactory(initSSLSocketFactory(), initTrustManager());
        // 生成Client对象
        mOkHttpClient = clientBuilder.build();
    }

    public static SSLSocketFactory initSSLSocketFactory() {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            X509TrustManager[] xTrustArray = new X509TrustManager[]
                    {initTrustManager()};
            sslContext.init(null,
                    xTrustArray, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }

    public static X509TrustManager initTrustManager() {
        X509TrustManager mTrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
        return mTrustManager;
    }

    /**
     * 创建一个Get请求
     * @param url
     * @param params
     * @return 返回一个创建好的Get请求对象
     */
    public static Request getRequest(String url, Map<String,String> params){
        StringBuilder stringBuilder = new StringBuilder(url).append("?");
        if(params != null){
            for (Map.Entry<String,String> entry : params.entrySet()){
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return new Request.Builder().url(stringBuilder.substring(0,stringBuilder.length()-1))
                .get().build();
    }
    /**
     * 创建一个Post请求
     * @param url
     * @param params
     * @return 返回一个创建好的Post请求对象
     */
    public static Request postRequest(String url,Map<String,String> params){
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if(params != null){
            for (Map.Entry<String,String> entry : params.entrySet()){
                // 将请求参数遍历添加到请求构建类中
                mFormBodyBuild.add(entry.getKey(),entry.getValue());
            }
        }
        // 通过请求构建类的build方法获取到真正的请求体对象
        FormBody formBody = mFormBodyBuild.build();
        return new Request.Builder().url(url).post(formBody).build();
    }

    /**
     * 创建一个post请求，接收json文本
     * @param url
     * @param json
     * @return 返回一个创建好的Post请求对象
     */
    public static Request postRequest(String url, String json){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        return new Request.Builder().url(url).post(body).build();
    }

    /**
     * 通过构造好的Get Request去发送请求
     * @param request
     * @return Call
     */
    public static Response sendSync(Request request)throws IOException{
        Call call = mOkHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 通过构造好的Get Request,Callback去发送请求
     * @param request
     * @param commCallback
     * @return Call
     */
    public static Call sendAsync(Request request, Callback commCallback){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(commCallback);
        return call;
    }

    /**
     * 通过构造好的Get Request去发送请求
     * @return Call
     */
    public static Response sendGetRequestWithUrl(String url,Map<String,String> params)throws IOException{
        Request request = OkHttpUtil.getRequest(url, params);
        return sendSync(request);
    }

    /**
     * 通过构造好的Get Request去发送请求
     * @return Call
     */
    public static Response sendGetRequestWithUrl(String url)throws IOException{
        Request request = OkHttpUtil.getRequest(url, null);
        return sendSync(request);
    }

    /**
     * 通过构造好的Post Request去发送请求
     * @return Call
     */
    public static Response sendPostRequestWithUrl(String url,Map<String,String> params)throws IOException{
        Request request = OkHttpUtil.postRequest(url, params);
        return sendSync(request);
    }
     /**
     * 通过构造好的Post Request去发送请求
     * @return Call
     */
    public static Response sendPostRequestWithUrl(String url,String json)throws IOException{
        Request request = OkHttpUtil.postRequest(url, json);
        return sendSync(request);
    }

}
