package com.example.mbochaton.bocabanque.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompteBancaire implements Parcelable {
    private long id;
    private String intitule;
    private double solde;
    @SerializedName("iduser")
    private long idUser;

    public CompteBancaire(long id, String intitule, double solde, long idUser) {
        this.id = id;
        this.intitule = intitule;
        this.solde = solde;
        this.idUser = idUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    //parcel part
    public CompteBancaire(Parcel in){
        String[] data= new String[3];

        in.readStringArray(data);
        this.id = Long.parseLong(data[0]);
        this.intitule = data[1];
        this.solde = Double.parseDouble(data[2]);
        this.idUser = Long.parseLong(data[3]);
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeStringArray(new String[]{String.valueOf(this.id), this.intitule, String.valueOf(this.solde), String.valueOf(this.idUser)});
    }

    public static final Parcelable.Creator<CompteBancaire> CREATOR= new Parcelable.Creator<CompteBancaire>() {

        @Override
        public CompteBancaire createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new CompteBancaire(source);  //using parcelable constructor
        }

        @Override
        public CompteBancaire[] newArray(int size) {
            // TODO Auto-generated method stub
            return new CompteBancaire[size];
        }
    };

}
