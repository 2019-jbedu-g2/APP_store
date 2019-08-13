package com.example.login;

public class CustomerItem {
    String type;            // 방문 고객 타입 : 방문고객 / 온라인 고객
    String barcode;         // 고객번호
    String status;          //상태  :  줄서는중 / 미루기

    public CustomerItem(String type, String barcode, String status) {
        this.type = type;
        this.barcode = barcode;
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
