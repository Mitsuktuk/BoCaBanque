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

public class FragmentInterne extends Fragment {
    Spinner spinnerDebiteur;
    Spinner spinnerCrediteur;
    List<String> list  = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;

    View view;

    public FragmentInterne() {    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.interne_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        spinnerDebiteur = (Spinner) getView().findViewById(R.id.deb_int_spinner_services);
        spinnerCrediteur = (Spinner) getView().findViewById(R.id.cred_int_spinner_services);
        spinnerAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDebiteur.setAdapter(spinnerAdapter);
        spinnerCrediteur.setAdapter(spinnerAdapter);

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

        list.add("Sparta");
        list.add("Coder");
        list.add("Healthy");
        list.add("Android");
        list.add("Developer");

        spinnerAdapter.addAll(list);
        spinnerAdapter.notifyDataSetChanged();
    }
}
