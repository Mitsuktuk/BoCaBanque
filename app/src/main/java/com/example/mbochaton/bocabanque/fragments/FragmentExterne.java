package com.example.mbochaton.bocabanque.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mbochaton.bocabanque.R;
import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.OperationBancaire;
import com.example.mbochaton.bocabanque.services.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentExterne extends Fragment {
    Spinner spinnerDebiteur, spinnerCrediteur;
    ArrayAdapter<String> spinnerAdapterDeb, spinnerAdapterCred;
    List<String> comptes = new ArrayList<>();
    List<String> utilisateurs = new ArrayList<>();
    private EditText montant;
    private CardView envoyer;
    private OperationBancaire operationDebit, operationCredit;
    private CompteBancaire compteDeb, compteCred;
    private long maxId, idUser;

    View view;

    public FragmentExterne(List<String> comptes, List<String> utilisateurs, long idUser) {
        this.comptes.addAll(comptes);
        this.utilisateurs.addAll(utilisateurs);
        this.idUser = idUser;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.externe_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinnerDebiteur = (Spinner) getView().findViewById(R.id.deb_ext_spinner_services);
        spinnerCrediteur = (Spinner) getView().findViewById(R.id.cred_ext_spinner_services);
        spinnerAdapterDeb = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapterDeb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAdapterCred = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapterCred.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDebiteur.setAdapter(spinnerAdapterDeb);
        spinnerCrediteur.setAdapter(spinnerAdapterCred);

        spinnerDebiteur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int selectedPosition= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCrediteur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                int selectedPosition= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerAdapterDeb.addAll(comptes);
        spinnerAdapterDeb.notifyDataSetChanged();
        spinnerAdapterCred.addAll(utilisateurs);
        spinnerAdapterCred.notifyDataSetChanged();
        montant = (EditText) getView().findViewById(R.id.montant);
        envoyer = (CardView) getView().findViewById(R.id.envoyer_card);/*
        envoyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operationDebit = new OperationBancaire();
                operationDebit.setDescription("-" + montant.getText().toString() + "€");

                operationCredit = new OperationBancaire();
                operationCredit.setDescription("+" + montant.getText().toString() + "€");

                RequestHelper.provideService().listOperations().enqueue(new Callback<List<OperationBancaire>>() {
                    @Override
                    public void onResponse(Call<List<OperationBancaire>> call, Response<List<OperationBancaire>> response) {
                        if (response.isSuccessful()) {
                            List<OperationBancaire> operations = response.body();
                            maxId = 0;
                            for (int i = 0; i < operations.size(); i++) {
                                if (operations.get(i).getId() > maxId) {
                                    maxId = operations.get(i).getId();
                                }
                            }

                            loadCompteDeb();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<OperationBancaire>> call, Throwable t) {
                    }
                });
            }
        });*/
    }/*

    private void loadCompteDeb() {
        RequestHelper.provideService().listComptes().enqueue(new Callback<List<CompteBancaire>>() {
            @Override
            public void onResponse(Call<List<CompteBancaire>> call, Response<List<CompteBancaire>> response) {
                if(response.isSuccessful()) {
                    List<CompteBancaire> comptes = response.body();
                    String intCompteDeb = spinnerDebiteur.getSelectedItem().toString();
                    for (int i = 0; i < comptes.size(); i++) {
                        if(comptes.get(i).getIdUser() == idUser && comptes.get(i).getIntitule().equals(intCompteDeb)) {
                            compteDeb = comptes.get(i);
                        }
                    }
                    operationDebit.setIdcompte(compteDeb.getId());
                    operationDebit.setId(maxId + 1);
                    RequestHelper.provideService().createOperation(operationDebit).enqueue(new Callback<OperationBancaire>() {
                        @Override
                        public void onResponse(Call<OperationBancaire> call, Response<OperationBancaire> response) {
                            if(response.isSuccessful()) {
                                loadCompteCred();
                            } else {
                                Toast.makeText(getActivity(), "Opération bancaire non créé", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<OperationBancaire> call, Throwable t) {
                            Toast.makeText(getActivity(), "Opération bancaire débit non créé", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompteBancaire>> call, Throwable t) {
                Toast.makeText(getActivity(), "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadCompteCred() {
        RequestHelper.provideService().listComptes().enqueue(new Callback<List<CompteBancaire>>() {
            @Override
            public void onResponse(Call<List<CompteBancaire>> call, Response<List<CompteBancaire>> response) {
                if(response.isSuccessful()) {
                    List<CompteBancaire> comptes = response.body();
                    String intCompteCred = spinnerCrediteur.getSelectedItem().toString();
                    for (int i = 0; i < comptes.size(); i++) {
                        if(comptes.get(i).getIdUser() == idUser && comptes.get(i).getIntitule().equals(intCompteCred)) {
                            compteCred = comptes.get(i);
                        }
                    }
                    operationCredit.setIdcompte(compteCred.getId());
                    operationCredit.setId(maxId + 2);
                    RequestHelper.provideService().createOperation(operationCredit).enqueue(new Callback<OperationBancaire>() {
                        @Override
                        public void onResponse(Call<OperationBancaire> call, Response<OperationBancaire> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(getActivity(), "Transfert validé", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Opération bancaire crédit non créé", Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<OperationBancaire> call, Throwable t) {
                            Toast.makeText(getActivity(), "Opération bancaire crédit non créé", Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
                    Toast.makeText(getActivity(), "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompteBancaire>> call, Throwable t) {
                Toast.makeText(getActivity(), "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }*/
}
