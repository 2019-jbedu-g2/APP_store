package com.example.login;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import static java.time.chrono.IsoEra.CE;

public class CustomerItemView extends LinearLayout {
    TextView textView;
    TextView textView2;
    TextView textView3;

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

        textView = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView4);
        textView3 = findViewById(R.id.textView5);
    }

    public void setType(String type) {
        if (type == "ONLINE") {
            textView.setTextColor(0xFF0771CE);
        } else {
            textView.setTextColor(0xFFDB110D);
        }
        textView.setText(type);
    }

    public void setBarcode(String barcode) {
        textView2.setText(barcode);
    }

    public void setStatus(String status) {
        textView3.setText(status);
    }
}
