package com.prog.pl.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class HttpUtil {
    //public static final String BASE_URL = "http://192.168.1.100:8080/LibraryManage/";
    public static final String BASE_URL = "http://10.0.3.2:8080/ZhiHu/";
    public static final String FILE_PATH = "/sdcard/zhihu";
    private static final int REQUEST_TIMEOUT = 8000;// 设置请求超时8秒钟
    private static final int SO_TIMEOUT = 8000; // 设置等待数据超时时间8秒钟

    public static HttpGet getHttpGet(String url) {
        HttpGet request = new HttpGet(url);
        return request;
    }

    // 获得Post请求对象request
    public static HttpPost getHttpPost(String url) {
        HttpPost request = new HttpPost(url);
        return request;
    }

    // 根据请求获得响应对象response
    public static HttpResponse getHttpResponse(HttpGet request)
            throws ClientProtocolException, IOException {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpResponse response = new DefaultHttpClient(httpParams)
                .execute(request);
        return response;
    }

    // 根据请求获得响应对象response
    public static HttpResponse getHttpResponse(HttpPost request) {
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpResponse response=null;
        try {
            response = new DefaultHttpClient(httpParams).execute(request);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return response;
    }

    // 发�?Post请求，获得响应查询结�?
    public static String queryStringForPost(String url) {
        // 根据url获得HttpPost对象
        HttpPost request = HttpUtil.getHttpPost(url);
        String result = null;
        try {
            // 获得响应对象
            Log.i("Main",""+getHttpResponse(request));
            HttpResponse response = HttpUtil.getHttpResponse(request);
            // 判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得响应
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "ERROR";
            return result;
        }
        if (result == null) {
            HttpResponse response;
            try {
                response = HttpUtil.getHttpResponse(request);
                result = EntityUtils.toString(response.getEntity());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    // 获得响应查询结果
    public static String queryStringForPost(HttpPost request) {
        String result = null;
        try {
            // 获得响应对象

            HttpResponse response = HttpUtil.getHttpResponse(request);
            // 判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得响应
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
            else {
                result="No Response";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "No Response";
            return result;

        }
        return result;
    }

    // 发�?Get请求，获得响应查询结�?
    public static String queryStringForGet(String url) {
        // 获得HttpGet对象
        HttpGet request = HttpUtil.getHttpGet(url);
        String result = null;
        try {
            // 获得响应对象
            HttpResponse response = HttpUtil.getHttpResponse(request);
            // 判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得响应
                result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = "No Response";
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            result = "No Response";
            return result;
        }
        return null;
    }

    public static void downFile(String url, String path, String fileName)
            throws IOException {
        String FileName = "";
        int fileSize;
        if (fileName == null || fileName == "")
            FileName = url.substring(url.lastIndexOf("/") + 1);
        else
            FileName = fileName;

        if (FileName.indexOf("/") > 0) {

            File file = new File(path
                    + FileName.substring(0, FileName.indexOf("/")));
            // 判断文件夹是否存�?如果不存在则创建文件�?
            if (!file.exists()) {
                file.mkdir();
            }

        }
        // 取得文件名，如果输入新文件名，则使用新文件名
        URL Url = new URL(url);
        URLConnection conn = Url.openConnection();
        conn.connect();
        InputStream is = conn.getInputStream();
        fileSize = conn.getContentLength();// 根据响应获取文件大小
        if (fileSize <= 0) { // 获取内容长度�?
            throw new RuntimeException("无法获知文件大小 ");
        }
        if (is == null) { // 没有下载�?
            throw new RuntimeException("无法获取文件");
        }
        File destDir = new File(path);
        if (!destDir.exists()) {
            try {
                boolean bb = destDir.mkdirs();// bb为true 说明创建成功
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        FileOutputStream FOS = new FileOutputStream(path + FileName); // 创建写入文件内存流，通过此流向目标写文件
        byte buf[] = new byte[1024];
        int downLoadFilePosition = 0;
        int numread;
        while ((numread = is.read(buf)) != -1) {
            FOS.write(buf, 0, numread);
            downLoadFilePosition += numread;
        }
        try {
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getHtmlToFile(String url, String path, String filename)
            throws IOException, URISyntaxException {
        URI u = new URI(url);
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, REQUEST_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);
        HttpGet httpget = new HttpGet(u);
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String content = httpclient.execute(httpget, responseHandler);
        // content = new String(content.getBytes("ISO-8859-1"),"UTF-8");
        // 目标页面编码为UTF-8,没这个会乱码

        FileOutputStream fos = new FileOutputStream(path + filename);

        fos.write(content.getBytes());
        fos.close();

    }

    public static boolean sendXML(String path, String xml) throws Exception {
        byte[] data = xml.getBytes();
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5 * 1000);
        conn.setDoOutput(true);// ���ͨ��post�ύ��ݣ����������������������
        conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            return true;
        }
        return false;
    }

    public static byte[] sendGetRequest(String path,
                                        Map<String, String> params, String enc) throws Exception {
        StringBuilder sb = new StringBuilder(path);
        sb.append('?');
        // ?method=save&title=435435435&timelength=89&
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(entry.getKey()).append('=')
                    .append(URLEncoder.encode(entry.getValue(), enc))
                    .append('&');
        }
        sb.deleteCharAt(sb.length() - 1);

        URL url = new URL(sb.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);

        if (conn.getResponseCode() == 200) {
            return readStream(conn.getInputStream());
        }
        return null;
    }

    public static boolean sendPostRequest(String path,
                                          Map<String, String> params, String enc) throws Exception {
        // title=dsfdsf&timelength=23&method=save
        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append('=')
                        .append(URLEncoder.encode(entry.getValue(), enc))
                        .append('&');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        byte[] entitydata = sb.toString().getBytes();// �õ�ʵ��Ķ��������
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5 * 1000);
        conn.setDoOutput(true);// ���ͨ��post�ύ��ݣ����������������������
        // Content-Type: application/x-www-form-urlencoded
        // Content-Length: 38
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length",
                String.valueOf(entitydata.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(entitydata);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            return true;
        }
        return false;
    }

    public static byte[] SendPostRequest(String path,
                                         Map<String, String> params, String enc) throws Exception {
        // title=dsfdsf&timelength=23&method=save
        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append(entry.getKey()).append('=')
                        .append(URLEncoder.encode(entry.getValue(), enc))
                        .append('&');
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        byte[] entitydata = sb.toString().getBytes();// �õ�ʵ��Ķ��������
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5 * 1000);
        conn.setDoOutput(true);// ���ͨ��post�ύ��ݣ����������������������
        // Content-Type: application/x-www-form-urlencoded
        // Content-Length: 38
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length",
                String.valueOf(entitydata.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(entitydata);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            return readStream(conn.getInputStream());
        }
        return null;
    }

    // SSL HTTPS Cookie
    public static boolean sendRequestFromHttpClient(String path,
                                                    Map<String, String> params, String enc) throws Exception {
        List<NameValuePair> paramPairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                paramPairs.add(new BasicNameValuePair(entry.getKey(), entry
                        .getValue()));
            }
        }
        UrlEncodedFormEntity entitydata = new UrlEncodedFormEntity(paramPairs,
                enc);// �õ�����������ʵ�����?
        HttpPost post = new HttpPost(path); // form
        post.setEntity(entitydata);
        DefaultHttpClient client = new DefaultHttpClient(); // �����?
        HttpResponse response = client.execute(post);// ִ������
        if (response.getStatusLine().getStatusCode() == 200) {
            return true;
        }
        return false;
    }

    /**
     * ��ȡ��
     *
     * @param inStream
     * @return �ֽ�����
     * @throws Exception
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        return outSteam.toByteArray();
    }

    /* �ϴ��ļ���Server�ķ��� */
    public static String uploadFile(String filename) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            URL url = new URL(HttpUtil.BASE_URL + "UpPhotoServlet");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* ����Input��Output����ʹ��Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
			/* ���ô��͵�method=POST */
            con.setRequestMethod("POST");
			/* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
			/* ����DataOutputStream */
            DataOutputStream ds = new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "
                    + "name=\"file1\";filename=\"" + filename + "\"" + end);
            ds.writeBytes(end);
			/* ȡ���ļ���FileInputStream */
            File file = new File(HttpUtil.FILE_PATH + "/" + filename);
            FileInputStream fStream = new FileInputStream(file);
			/* ����ÿ��д��1024bytes */
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
			/* ���ļ���ȡ�����������?*/
            while ((length = fStream.read(buffer)) != -1) {
				/* ������д��DataOutputStream�� */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
            fStream.close();
            ds.flush();
			/* ȡ��Response���� */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
			/* ��Response��ʾ��Dialog */
			/* �ر�DataOutputStream */
            ds.close();
            return b.toString();
        } catch (Exception e) {
            return "";
        }

    }

}
