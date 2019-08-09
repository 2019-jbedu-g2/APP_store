package com.example.login;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestHttpURLConnection {

    public String request(String _url, ContentValues _params) {
        HttpURLConnection urlConn = null;
        StringBuffer sbParams = new StringBuffer();
        if (_params == null) {
            sbParams.append("");
        }else {
            boolean isAnd = false;
            String key;
            String value;
            for(Map.Entry<String, Object> parameter : _params.valueSet()){

                key = parameter.getKey();
                value = parameter.getValue().toString();

                // 파라미터가 두개 이상일때
                if (isAnd)
                    sbParams.append("");

                sbParams.append(key).append("/").append(value);
                if (!isAnd)
                    if (_params.size() >= 2)
                        isAnd = true;
            }
        }
        try{
            _url = _url + sbParams.toString();
            System.out.println(_url);
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setReadTimeout(1000);
            urlConn.setConnectTimeout(1000);
            urlConn.setRequestMethod("GET");
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Accept", "application/json");
            urlConn.setRequestProperty("Context_Type", "application/json");

            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            String line;
            StringBuffer page = new StringBuffer();

            while ((line = reader.readLine()) != null){
                page.append(line);
            }
            return page.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }
        return null;
    }
}
