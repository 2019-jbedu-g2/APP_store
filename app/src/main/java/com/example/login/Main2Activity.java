package com.example.login;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    //test 입니다.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //test
    }

    public void onBackButtonClicked(View v) {
        Toast.makeText(getApplicationContext(), "메인화면으로 돌아갑니다", Toast.LENGTH_LONG).show();
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