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
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Main2Activity extends AppCompatActivity {
    Button ScannerButton;
    String barcodenumber = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initControls();
        ScannerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator intent = new IntentIntegrator(Main2Activity.this); // zxing 내부의 스캐너 호출
                intent.setBeepEnabled(true);        // 바코드 인식시에 비프음의 여부
                intent.initiateScan();              // 스캔화면으로 넘어감.
            }
        });
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