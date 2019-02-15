package com.example.mbochaton.bocabanque.services;

import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.OperationBancaire;
import com.example.mbochaton.bocabanque.models.Utilisateur;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RequestService {
    @GET("restful.comptebancaire")
    Call<List<CompteBancaire>> listComptes();

    @GET("restful.utilisateur")
    Call<List<Utilisateur>> listUtilisateurs();

    @GET("restful.operationbancaire")
    Call<List<OperationBancaire>> listOperations();
}
