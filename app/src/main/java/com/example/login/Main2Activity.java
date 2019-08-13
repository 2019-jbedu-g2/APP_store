package com.example.login;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class Main2Activity extends AppCompatActivity {
    WebSocketClient wsc;
    ContentValues info = new ContentValues();
    Button ScannerButton,RefreshButton,OffButton;
    String s = "";
    TextView storeView,textView,Type,Status,TimeView;
    String wsURL = "ws://192.168.0.20:8000/";
    String url = "http://192.168.0.20:8000/";
    String result ="";
    String PhoneNum = "";

    //LinkedHashMap <String,String> offLineList = new LinkedHashMap();
    ArrayList BarcodeNum = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initControls();

        s = getIntent().getStringExtra("result");
        textView.setText(getIntent().getStringExtra("storeName"));
        WebSocketConnect(s);
        ScannerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this, OnlineCustomer.class);
                intent.putExtra("barcodenum",BarcodeNum);
                startActivityForResult(intent,1);
            }
        });
        RefreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wsc.send("");
            }
        });
        OffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, OfflineCustomer.class);
                intent.putExtra("barcodenum",BarcodeNum);
                startActivityForResult(intent,2);
            }
        });
    }
    public void WebSocketConnect(String storeNum){
        try{
            Draft d = new Draft_6455();
            StringBuffer SB = new StringBuffer();
            SB.append(wsURL).append("queue/").append(storeNum).append("/master");

            wsc = new WebSocketClient(new URI(SB.toString()),d) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    wsc.send("");
                    wsc.send("");
                }

                @Override
                public void onMessage(final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            storeView.setText("");
                            Type.setText("");
                            Status.setText("");
                            try {
                                JSONObject jsonObject = new JSONObject(message);
                                String messagedata = jsonObject.getString("message");
                                System.out.println(messagedata);
                                JSONArray JSON = new JSONArray(messagedata);
                               // System.out.println(offLineList.entrySet());
                                for (int i = 0; i <JSON.length();i++) {
                                    JSONObject jsonobject = JSON.getJSONObject(i);
                                    String OnOff = jsonobject.getString("onoffline");
                                    if(jsonobject.getString("status").equals("줄서는중")||jsonobject.getString("status").equals("미루기")) {
                                        if (OnOff.equals("false")) {
                                            storeView.append(jsonobject.getString("barcode"));
                                            Type.append("온라인고객");
                                        } else {
                                            String Code = jsonobject.getString("barcode");
                                            storeView.append(jsonobject.getString("barcode"));
                                            Type.append("방문고객");
                                        }
                                        Status.append(jsonobject.getString("status"));
                                        BarcodeNum.add(jsonobject.getString("barcode"));
                                        storeView.append("\n");
                                        Type.append("\n");
                                        Status.append("\n");
                                    }
                                }
                                Date date = new Date(System.currentTimeMillis());
                                SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                TimeView.setText(SDF.format(date));
                            }catch (JSONException e){
                                e.getStackTrace();
                            }
                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "서버 연결이 종료되였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onError(Exception ex) {
                    ex.getStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "서버와의 연결에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            };
        }catch(URISyntaxException E){
            E.getStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        wsc.connect();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 1 ){
            if(resultCode==RESULT_OK){
                wsc.send("");
            }
        }
        if(requestCode == 2){
            if(resultCode==RESULT_OK){
                wsc.send("");
            }
        }
    }

    private void initControls(){
        if (ScannerButton == null) {
            ScannerButton = (Button) findViewById(R.id.button3);
        }
        if (storeView == null) {
            storeView = (TextView) findViewById(R.id.storeView);
        }
        if (textView == null){
            textView = (TextView) findViewById(R.id.textView);
        }
        if (Type == null) {
            Type = (TextView) findViewById(R.id.Type);
        }
        if (Status == null){
            Status = (TextView) findViewById(R.id.Status);
        }
        if (RefreshButton == null){
            RefreshButton = (Button) findViewById(R.id.RefreshButton);
        }
        if (TimeView == null){
            TimeView = (TextView) findViewById(R.id.TimeView);
        }
        if (OffButton == null){
            OffButton = (Button) findViewById(R.id.button4);
        }
    }

    public void onBackButtonClicked(View v) {
        wsc.close();
        finish();

    }
    public void onClick(View v){
    final EditText etEdit = new EditText(this);
    AlertDialog.Builder dialog = new AlertDialog.Builder(Main2Activity.this);
    dialog.setTitle("입력");
    dialog.setView(etEdit);
// OK 버튼 이벤트
    dialog.setPositiveButton("예약", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            PhoneNum = "";
            PhoneNum = etEdit.getText().toString();
            info.clear();
            StringBuffer SB = new StringBuffer();
            SB.append(s).append("/").append(PhoneNum);
            info.put("account/off",SB.toString());
            NetworkTask networkTask = new NetworkTask(url,info);
            networkTask.execute();
            }
        });
// Cancel 버튼 이벤트
    dialog.setNegativeButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                 dialog.cancel();
             }
         });
    dialog.show();


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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Main2Activity.this, "예약되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            wsc.send("");
        }
    }
}