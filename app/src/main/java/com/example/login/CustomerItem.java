package com.example.login;

public class CustomerItem {
    String type;
    String barcode;
    String status;

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
