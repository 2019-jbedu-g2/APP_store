package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;
    public static final String TAG = "MainActivity";
    String url = "http://192.168.0.8:8000/account/";
    String url2 = "http://192.168.0.8:8000/";
    EditText idText = null;
    EditText pwText = null;
    Button loginButton;
    ContentValues info = new ContentValues();
    String id = "";
    String pw = "";
    String result, result1,result2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initControls();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = idText.getText().toString().trim();
                pw = pwText.getText().toString().trim();
                if(id.equals("")||pw.equals("")){
                    Toast.makeText(getApplicationContext(), "빈칸이 있습니다.", Toast.LENGTH_LONG).show();
                }else {
                    info.clear();
                    info.put("login", id);
                    info.put("zzzzzzzz", pw);
                    MainActivity.NetworkTask networkTask = new MainActivity.NetworkTask(url, info);
                    networkTask.execute();  // 비동기 task 작동.
                }
            }
        });
    }

    private void initControls() {
        if (idText == null) {
            idText = (EditText) findViewById(R.id.idText);
        }
        if (pwText == null) {
            pwText = (EditText) findViewById(R.id.pwText);
        }
        if (loginButton == null) {
            loginButton = (Button) findViewById(R.id.loginButton);
        }
    }

    public class NetworkTask extends AsyncTask<Void, Void, String> {

        String url;
        ContentValues values;

        public NetworkTask(String url, ContentValues values) {
            this.url = url;
            this.values = values;
        }

        //background작업 시작전 ui작업을 진행.
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // background 작업 진행
        protected String doInBackground(Void... params) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result = requestHttpURLConnection.request(url, values); // 해당 주소로 부터 결과물 얻음
            return result;
        }

        // 끝난후 ui진행
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            result1 = s.substring(0,9);
            result2 = s.substring(10).trim();
//            if(s.equals("215486211") || s.equals("289461321") || s.equals("246899512")
//                    || s.equals("234879246") || s.equals("264875312") || s.equals("216484531")
//                    || s.equals("246879513") || s.equals("202157945")) {
            if(s.equals("아이디랑 비밀번호가 다릅니다.")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "아이디와 비밀번호를 다시 확인하세요", Toast.LENGTH_LONG).show();
                    }
                });
            }
            else {
//                info.clear();
//                info.put("queue/"+s,"master");
//               // info.put("master","");
//                MainActivity.NetworkTask2 networkTask2 = new MainActivity.NetworkTask2(url2,info);
//                networkTask2.execute();
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("result",result1);
                intent.putExtra("storeName",result2);
                startActivity(intent);
            }
//            else {
//                Toast.makeText(getApplicationContext(),"아이디와 비밀번호를 다시 확인하세요",Toast.LENGTH_LONG).show();
//            }

        }
    }
//
//    public class NetworkTask2 extends AsyncTask<Void, Void, String> {
//
//        String url2;
//        ContentValues values;
//
//        public NetworkTask2(String url2, ContentValues values) {
//            this.url2 = url2;
//            this.values = values;
//        }
//
//        //background작업 시작전 ui작업을 진행.
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        // background 작업 진행
//        protected String doInBackground(Void... params) {
//            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
//            result = requestHttpURLConnection.request(url2, values); // 해당 주소로 부터 결과물 얻음
//            return result;
//        }
//
//        // 끝난후 ui진행
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//        }
//    }
}
