package com.example.mbochaton.bocabanque.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mbochaton.bocabanque.R;
import com.example.mbochaton.bocabanque.models.CompteBancaire;

import java.util.List;

public class CompteBancaireListAdapter extends BaseAdapter {
    private Context mContext;
    private List<CompteBancaire> mCompteBancaireList;

    public CompteBancaireListAdapter(Context mContext, List<CompteBancaire> mCompteBancaireList) {
        this.mContext = mContext;
        this.mCompteBancaireList = mCompteBancaireList;
    }

    @Override
    public int getCount() {
        return mCompteBancaireList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCompteBancaireList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext, R.layout.item_compte_list, null);

        TextView tvIntitule = (TextView)v.findViewById(R.id.tv_intitule);
        TextView tvSolde = (TextView)v.findViewById(R.id.tv_solde);
        tvIntitule.setText(mCompteBancaireList.get(position).getIntitule());
        tvSolde.setText(String.valueOf(mCompteBancaireList.get(position).getSolde()) + "€");

        v.setTag(mCompteBancaireList.get(position).getId());

        return v;
    }
}
