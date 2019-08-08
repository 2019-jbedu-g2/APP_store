package com.example.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class OfflineCustomer extends Activity {
    EditText PhoneNumber;
    Button Close, Confirm, Cancel;
    ContentValues info = new ContentValues();
    String url = "http://192.168.0.8:8000/account/";
    String result="";
    String Num = "";
    HashMap<String,String> phonenum = new HashMap();

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 삭제.
        initControls();
        setContentView(R.layout.offline_customer);
        Intent intent = getIntent();
        phonenum = (HashMap<String,String>) intent.getSerializableExtra("phonenum");
        if (Cancel == null) {
            Cancel = (Button) findViewById(R.id.offcancelButton);
        }
        if (Confirm == null) {
            Confirm = (Button) findViewById(R.id.offconfirmButton);
        }
        if (Close == null) {
            Close = (Button) findViewById(R.id.offcloseButton);
        }
        if (PhoneNumber == null){
            PhoneNumber = (EditText) findViewById(R.id.PhoneNumText);
        }


        // 줄서기 취소 누를 시
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PhoneNumber.getText().toString().trim().equals("") || PhoneNumber.getText().toString().trim().equals(null)) {                                             // 빈칸인지 확인
                    Toast.makeText(getApplicationContext(), "번호를 입력하세요.", Toast.LENGTH_LONG).show();
                } else if (phonenum.containsValue(PhoneNumber.getText().toString().trim())) {                         // 대기 명단내 존재하는지 확인
                    info.clear();
                    String Key = getMapKey(PhoneNumber.getText().toString().trim());
                    if (Key.equals(null)){
                        Toast.makeText(getApplicationContext(), "해당 고객 번호가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    }else {
                        info.put("cancel", Key);
                        OfflineCustomer.NetworkTask networkTask = new OfflineCustomer.NetworkTask(url, info);
                        networkTask.execute();  // 비동기 task 작동.
                    }
                } else {                                                 // 존재하지 않을때의 처리
                    Toast.makeText(getApplicationContext(), "해당 고객 번호가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 확인 버튼 누를 시
        Confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (PhoneNumber.getText().toString().trim().equals("") || PhoneNumber.getText().toString().trim().equals(null)) {                                             // 빈칸인지 확인
                    Toast.makeText(getApplicationContext(), "번호를 입력하세요.", Toast.LENGTH_LONG).show();
                } else if (phonenum.containsValue(PhoneNumber.getText().toString().trim())) {                         // 대기 명단내 존재하는지 확인
                    info.clear();
                    String Key = getMapKey(PhoneNumber.getText().toString().trim());
                    if (Key.equals(null)){
                        Toast.makeText(getApplicationContext(), "해당 고객 번호가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                    }else {
                        info.put("confirm", Key);
                        OfflineCustomer.NetworkTask networkTask = new OfflineCustomer.NetworkTask(url, info);
                        networkTask.execute();  // 비동기 task 작동.
                    }
                } else {                                                 // 존재하지 않을때의 처리
                    Toast.makeText(getApplicationContext(), "해당 고객 번호가 존재하지 않습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 취소 시
        Close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
    public String getMapKey(String value){
        for (Map.Entry<String,String> entry : phonenum.entrySet()) {
            if(entry.getValue().equals(value)){
                return entry.getKey();
            }
        }
        return null;
    }
    private void initControls(){

        if (Cancel == null) {
            Cancel = (Button) findViewById(R.id.offcancelButton);
        }
        if (Confirm == null) {
            Confirm = (Button) findViewById(R.id.offconfirmButton);
        }
        if (Close == null) {
            Close = (Button) findViewById(R.id.offcloseButton);
        }
        if (PhoneNumber == null){
            PhoneNumber = (EditText) findViewById(R.id.PhoneNumText);
        }
    }


    // 바깥 레이어 클릭시 닫히지 않게 하기
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
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
            if(s.equals("완료되었습니다.")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "확인되었습니다. 환영합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }else if(s.equals("취소되었습니다.")){
                Toast.makeText(getApplicationContext(), "취소 처리되었습니다.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }else{
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "고객번호를 확인하여주십시오.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }
}
