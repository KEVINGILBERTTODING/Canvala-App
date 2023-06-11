package com.example.canvala.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InformationModel implements Serializable {
    @SerializedName("berat")
    Integer berat;
    @SerializedName("harga_total")
    Integer hargaTotal;

    @SerializedName("qty")
    Integer qty;

    public InformationModel(Integer berat, Integer hargaTotal, Integer qty) {
        this.berat = berat;
        this.hargaTotal = hargaTotal;
        this.qty = qty;
    }

    public Integer getBerat() {
        return berat;
    }

    public void setBerat(Integer berat) {
        this.berat = berat;
    }

    public Integer getHargaTotal() {
        return hargaTotal;
    }

    public void setHargaTotal(Integer hargaTotal) {
        this.hargaTotal = hargaTotal;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}
