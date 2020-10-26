package com.synapse.model;

import android.graphics.Bitmap;

public class BitmapData {
    String QR_text;
    Bitmap QR_bitmap;

    public BitmapData(String QR_text, Bitmap QR_bitmap) {
        this.QR_text = QR_text;
        this.QR_bitmap = QR_bitmap;
    }

    public String getQR_text() {
        return QR_text;
    }

    public void setQR_text(String QR_text) {
        this.QR_text = QR_text;
    }

    public Bitmap getQR_bitmap() {
        return QR_bitmap;
    }

    public void setQR_bitmap(Bitmap QR_bitmap) {
        this.QR_bitmap = QR_bitmap;
    }
}
