package com.example.canvala.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RekeningModel implements Serializable {
    @SerializedName("id_rekening")
    Integer idRekening;
    @SerializedName("bank_name")
    String bankName;
    @SerializedName("number")
    String number;
    @SerializedName("rekening_name")
    String rekeningName;

    public RekeningModel(Integer idRekening, String bankName, String number, String rekeningName) {
        this.idRekening = idRekening;
        this.bankName = bankName;
        this.number = number;
        this.rekeningName = rekeningName;
    }

    public Integer getIdRekening() {
        return idRekening;
    }

    public void setIdRekening(Integer idRekening) {
        this.idRekening = idRekening;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRekeningName() {
        return rekeningName;
    }

    public void setRekeningName(String rekeningName) {
        this.rekeningName = rekeningName;
    }
}
