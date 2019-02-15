package com.example.mbochaton.bocabanque.models;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.mbochaton.bocabanque.HomeActivity;
import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Utilisateur implements Parcelable {
    @SerializedName("idclient")
    private long id;
    private String prenom;
    private String nom;
    private String email;
    @SerializedName("motdepasse")
    private String mdp;

    public Utilisateur(long id, String prenom, String nom, String email, String mdp) {
        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.mdp = mdp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    //parcel part
    public Utilisateur(Parcel in){
        String[] data= new String[3];

        in.readStringArray(data);
        this.id= Long.parseLong(data[0]);
        this.prenom= data[1];
        this.nom= data[2];
        this.email= data[3];
        this.mdp= data[4];
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeStringArray(new String[]{ String.valueOf(this.id), this.prenom , this.nom, this.email, this.mdp });
    }

    public static final Parcelable.Creator<Utilisateur> CREATOR= new Parcelable.Creator<Utilisateur>() {

        @Override
        public Utilisateur createFromParcel(Parcel source) {
            // TODO Auto-generated method stub
            return new Utilisateur(source);  //using parcelable constructor
        }

        @Override
        public Utilisateur[] newArray(int size) {
            // TODO Auto-generated method stub
            return new Utilisateur[size];
        }
    };

}