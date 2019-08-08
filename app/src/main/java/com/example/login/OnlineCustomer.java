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

public class OnlineCustomer extends Activity {
    EditText BarcodeNumber;
    Button Close, Confirm, Scanner;
    ContentValues info = new ContentValues();
    String url = "http://192.168.0.8:8000/account/";
    String result="";
    String Num = "";
    ArrayList barnum = new ArrayList();

    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 타이틀바 삭제.
        initControls();
        setContentView(R.layout.online_customer);
        Intent intent = getIntent();
        barnum = intent.getStringArrayListExtra("barcodenum");
        if (Scanner == null) {
            Scanner = (Button) findViewById(R.id.scannerButton);
        }
        if (Confirm == null) {
            Confirm = (Button) findViewById(R.id.confirmButton);
        }
        if (Close == null) {
            Close = (Button) findViewById(R.id.closeButton);
        }
        if (BarcodeNumber == null){
            BarcodeNumber = (EditText) findViewById(R.id.barcodeText);
        }


        // 스캐너 누를 시
        Scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intent = new IntentIntegrator(OnlineCustomer.this); // zxing 내부의 스캐너 호출
                intent.setBeepEnabled(true);        // 바코드 인식시에 비프음의 여부
                intent.initiateScan();              // 스캔화면으로 넘어감.
            }
        });

        // 확인 버튼 누를 시
        Confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (BarcodeNumber.getText().toString().trim().equals("") || BarcodeNumber.getText().toString().trim().equals(null)) {                                             // 빈칸인지 확인
                    Toast.makeText(getApplicationContext(), "번호를 입력하세요.", Toast.LENGTH_LONG).show();
                } else if (barnum.contains(BarcodeNumber.getText().toString().trim())) {                         // 대기 명단내 존재하는지 확인
                    info.clear();
                    info.put("confirm", BarcodeNumber.getText().toString().trim());
                    OnlineCustomer.NetworkTask networkTask = new OnlineCustomer.NetworkTask(url, info);
                    networkTask.execute();  // 비동기 task 작동.
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
    private void initControls(){
        if (Scanner == null) {
            Scanner = (Button) findViewById(R.id.scannerButton);
        }
        if (Confirm == null) {
            Confirm = (Button) findViewById(R.id.confirmButton);
        }
        if (Close == null) {
            Close = (Button) findViewById(R.id.closeButton);
        }
        if (BarcodeNumber == null){
            BarcodeNumber = (EditText) findViewById(R.id.barcodeText);
        }
    }


    // 바코드 스캐너를 통해 받은 값을 전달 받음.
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        //  com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
        //  = 0x0000c0de; // Only use bottom 16 bits
        if(requestCode == IntentIntegrator.REQUEST_CODE){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);  // 결과물을 받을 그릇 생성
            if(result == null) {
                Toast.makeText(this, "취소하였습니다.",Toast.LENGTH_LONG).show();     // 결과물이 없다면 취소 토스트 출력
            } else {
                BarcodeNumber.setText(result.getContents());                       // 결과물을 받아서 텍스트창에 넣음.
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);            // 재시도.
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
