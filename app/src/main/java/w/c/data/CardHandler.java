package w.c.data;

import android.util.Base64;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CardHandler {
    private String cookie1;
    private String cookie2;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private String password;

    private enum signType {
        SynCard, SynSno
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCookie1() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://ecard.scse.com.cn:8070/")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.header("Set-Cookie");
            this.cookie1 = result.substring(0, result.indexOf(";"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getRandomImg() {
        InputStream inputStream = null;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://ecard.scse.com.cn:8070/Account/GetCheckCodeImg?rad=22")
                .addHeader("Cookie", cookie1)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
//            byte[] bytes = response.body().bytes();
//            FileOutputStream fileOutputStream = new FileOutputStream("./1.jpg");
//            fileOutputStream.write(bytes);
//            fileOutputStream.close();
            inputStream = response.body().byteStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public void login(String username, String checkCode) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("SignType", signType.SynSno.toString())
                .add("UserAccount", username)
                .add("Password", Base64.encodeToString(this.password.getBytes(), Base64.DEFAULT))
                .add("NextUrl", "")
                .add("CheckCode", checkCode)
                .add("openid", "")
                .add("Schoolcode", "SCSE")
                .build();
        Request request = new Request.Builder()
                .url("http://ecard.scse.com.cn:8070/Account/Login")
//                .addHeader("Accept","*/*")
//                .addHeader("Accept-Encoding","gzip, deflate")
//                .addHeader("Accept-Language","en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,la;q=0.6,gl;q=0.5,da;q=0.4,lb;q=0.3")
//                .addHeader("Cache-Control","no-cache")
//                .addHeader("Connection","keep-alive")
//                .addHeader("Content-Length","104")
//                .addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cookie", cookie1)
//                .addHeader("Host","ecard.scse.com.cn:8070")
//                .addHeader("Origin","http://ecard.scse.com.cn:8070")
//                .addHeader("Pragma","no-cache")
                .addHeader("Referer", "http://ecard.scse.com.cn:8070/Account/Login")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
//                .addHeader("X-Requested-With","XMLHttpRequest")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String cookie2 = response.header("Set-Cookie");
            this.cookie2 = cookie2.substring(0, cookie2.indexOf(";"));
            String result = response.body().string();
//            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            boolean success = jsonObject.getBoolean("success");
            String msg = jsonObject.getString("msg");
            System.out.println(success + " " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getInfo() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://ecard.scse.com.cn:8070/SynCard/Manage/BasicInfo")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .addHeader("Cookie", cookie1 + "; " + cookie2)
                .build();
        String result = null;
        try {
            Response response = okHttpClient.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void getTransferPage() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://ecard.scse.com.cn:8070/SynCard/Manage/Transfer")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .addHeader("Cookie", cookie1 + "; " + cookie2)
                .build();
        try {
            okHttpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void transfer(String amount) {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("FromCard", "bcard")
                .add("ToCard", "card")
                .add("ToSubNo", "")
                .add("Amount", amount)
                .add("Password", Base64.encodeToString(this.password.getBytes(), Base64.DEFAULT))
                .build();
        Request request = new Request.Builder()
                .url("http://ecard.scse.com.cn:8070/SynCard/Manage/TransferPost")
//                .addHeader("Accept","*/*")
//                .addHeader("Accept-Encoding","gzip, deflate")
//                .addHeader("Accept-Language","en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7,la;q=0.6,gl;q=0.5,da;q=0.4,lb;q=0.3")
//                .addHeader("Cache-Control","no-cache")
//                .addHeader("Connection","keep-alive")
//                .addHeader("Content-Length","65")
//                .addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("Cookie", cookie1 + "; " + cookie2)
//                .addHeader("Host","ecard.scse.com.cn:8070")
//                .addHeader("Origin","http://ecard.scse.com.cn:8070")
//                .addHeader("Pragma","no-cache")
                .addHeader("Referer", "http://ecard.scse.com.cn:8070/SynCard/Manage/Transfer")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
//                .addHeader("X-Requested-With","XMLHttpRequest")
                .post(formBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
//            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            boolean success = jsonObject.getBoolean("success");
            String msg = jsonObject.getString("msg");
            System.out.println(success + " " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://ecard.scse.com.cn:8070/Account/SignOutForWap")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36")
                .addHeader("Cookie", cookie1 + ";" + cookie2)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String result = response.body().string();
//            System.out.println(result);
            JSONObject jsonObject = new JSONObject(result);
            boolean success = jsonObject.getBoolean("success");
            String msg = jsonObject.getString("msg");
            System.out.println(success + " " + msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
