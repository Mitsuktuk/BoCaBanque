package com.example.mbochaton.bocabanque.services;

import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.OperationBancaire;
import com.example.mbochaton.bocabanque.models.Utilisateur;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestService {
    @GET("restservice.comptebancaire")
    Call<List<CompteBancaire>> listComptes();

    @GET("restservice.utilisateur")
    Call<List<Utilisateur>> listUtilisateurs();

    @GET("restservice.utilisateur/{id}")
    Call<Utilisateur> utilisateur(@Path("id") Long id);

    @GET("restservice.operationbancaire")
    Call<List<OperationBancaire>> listOperations();
}
