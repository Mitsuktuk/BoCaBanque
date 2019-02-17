package com.example.mbochaton.bocabanque.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mbochaton.bocabanque.R;
import com.example.mbochaton.bocabanque.adapters.TransactionListAdapter;
import com.example.mbochaton.bocabanque.models.OperationBancaire;

import java.util.List;

public class FragmentOperations extends Fragment {
    View view;
    List<OperationBancaire> operations;
    ListView lvMouvements;

    public FragmentOperations(List<OperationBancaire> operations) {
        this.operations = operations;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.operations_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lvMouvements = (ListView) getView().findViewById(R.id.listview_mouvements);
        TransactionListAdapter adapter = new TransactionListAdapter(getActivity().getApplicationContext(), operations);
        lvMouvements.setAdapter(adapter);

        lvMouvements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            }
        });
    }
}
