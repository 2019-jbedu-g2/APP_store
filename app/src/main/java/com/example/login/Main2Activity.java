package com.example.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class Main2Activity extends AppCompatActivity {
    WebSocketClient wsc;
    Button ScannerButton;
    String barcodenumber = "";
    String s = "";
    TextView storeView,textView;
    String URL = "ws://192.168.0.8:8000/queue/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initControls();
        Intent getin = getIntent();
        s = getIntent().getStringExtra("result");
        textView.setText(getIntent().getStringExtra("storeName"));
        WebSocketConnect(s);
        //storeView.setText(s);
        ScannerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator intent = new IntentIntegrator(Main2Activity.this); // zxing 내부의 스캐너 호출
                intent.setBeepEnabled(true);        // 바코드 인식시에 비프음의 여부
                intent.initiateScan();              // 스캔화면으로 넘어감.
            }
        });
    }
    public void WebSocketConnect(String storeNum){
        try{
            Draft d = new Draft_6455();
            StringBuffer SB = new StringBuffer();
            SB.append(URL);
            SB.append(storeNum);
            SB.append("/master");

            wsc = new WebSocketClient(new URI(SB.toString()),d) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    wsc.send("");
                }

                @Override
                public void onMessage(final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            storeView.setText(message);
                            System.out.println(message);
                        }
                    });
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Toast.makeText(getApplicationContext(), "서버 연결에 종료되였습니다.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception ex) {
                    ex.getStackTrace();
                    Toast.makeText(getApplicationContext(), "서버 연결에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                }
            };
        }catch(URISyntaxException E){
            E.getStackTrace();
            Toast.makeText(getApplicationContext(), "서버 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }
        wsc.connect();
    }
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        //  com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
        //  = 0x0000c0de; // Only use bottom 16 bits
        if(requestCode == IntentIntegrator.REQUEST_CODE){
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);  // 결과물을 받을 그릇 생성
            if(result == null) {
                Toast.makeText(this, "Cancelled",Toast.LENGTH_LONG).show();     // 결과물이 없다면 취소 토스트 출력
            } else {
                barcodenumber=result.getContents();                       // 결과물을 받아서 변수에 집어넣음
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);            // 재시도.
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
    }

    public void onBackButtonClicked(View v) {
        Toast.makeText(getApplicationContext(), "메인화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
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
            String inputValue = etEdit.getText().toString();
            Toast.makeText(Main2Activity.this,"예약되었습니다.", Toast.LENGTH_SHORT).show();
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
}