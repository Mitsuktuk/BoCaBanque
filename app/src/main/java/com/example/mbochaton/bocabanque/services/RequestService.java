package com.example.mbochaton.bocabanque.services;

import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.OperationBancaire;
import com.example.mbochaton.bocabanque.models.Utilisateur;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RequestService {
    @GET("restwebboca.comptebancaire")
    Call<List<CompteBancaire>> listComptes();

    @PUT("restwebboca.comptebancaire/{id}")
    Call<CompteBancaire> modifCompte(@Path("id") Long id, @Body CompteBancaire compteBancaire);

    @GET("restwebboca.utilisateur")
    Call<List<Utilisateur>> listUtilisateurs();

    @GET("restwebboca.utilisateur/{id}")
    Call<Utilisateur> utilisateur(@Path("id") Long id);

    @GET("restwebboca.operationbancaire")
    Call<List<OperationBancaire>> listOperations();

    @POST("restwebboca.operationbancaire")
    Call<OperationBancaire> createOperation(@Body OperationBancaire operationBancaire);
}
