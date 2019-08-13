package com.example.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import static java.time.chrono.IsoEra.CE;

public class CustomerItemView extends LinearLayout {
    TextView CustomerTypeView;  // 고객 타입 뷰
    TextView BarcodeNumberView; // 고객넘버 뷰
    TextView StatusView;        // 상태 뷰

    public CustomerItemView(Context context) {
        super(context);
        init(context);
    }

    public CustomerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.customer, this, true);
        if(CustomerTypeView == null){
            CustomerTypeView = findViewById(R.id.textView2);
        }
        if(BarcodeNumberView == null){
            BarcodeNumberView = findViewById(R.id.textView4);
        }
        if(StatusView == null){
            StatusView = findViewById(R.id.textView5);
        }
    }

    public void setType(String type) {
        if (type == "ONLINE") {
            CustomerTypeView.setTextColor(0xFF0771CE);
        } else {
            CustomerTypeView.setTextColor(0xFFDB110D);
        }
        CustomerTypeView.setText(type);
    }

    public void setBarcode(String barcode) {
        BarcodeNumberView.setText(barcode);
    }

    public void setStatus(String status) {
        StatusView.setText(status);
    }
}
