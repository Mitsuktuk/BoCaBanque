package com.example.mbochaton.bocabanque.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mbochaton.bocabanque.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentExterne extends Fragment {
    Spinner spinnerDebiteur;
    Spinner spinnerCrediteur;
    ArrayAdapter<String> spinnerAdapterDeb;
    ArrayAdapter<String> spinnerAdapterCred;
    List<String> comptes = new ArrayList<>();
    List<String> utilisateurs = new ArrayList<>();

    View view;

    public FragmentExterne(List<String> comptes, List<String> utilisateurs) {
        this.comptes.addAll(comptes);
        this.utilisateurs.addAll(utilisateurs);
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
    }
}
