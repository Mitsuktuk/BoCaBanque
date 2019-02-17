package com.example.mbochaton.bocabanque.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class OperationBancaire {
    private long id;
    private String description;
    private double montant;
    @SerializedName("dateoperation")
    private Date date;
    @SerializedName("typeoperation")
    private int type;
    private long idcompte;

    public OperationBancaire() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getIdcompte() {
        return idcompte;
    }

    public void setIdcompte(long idcompte) {
        this.idcompte = idcompte;
    }
}
