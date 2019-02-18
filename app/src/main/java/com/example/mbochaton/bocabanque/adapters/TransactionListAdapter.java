package com.example.mbochaton.bocabanque.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mbochaton.bocabanque.R;
import com.example.mbochaton.bocabanque.models.OperationBancaire;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

import static java.util.Comparator.comparing;

public class TransactionListAdapter extends BaseAdapter {
    private Context mContext;
    private List<OperationBancaire> mOperationBancaireList;
    private int type;

    public TransactionListAdapter(Context mContext, List<OperationBancaire> mOperationBancaireList) {
        this.mContext = mContext;
        this.mOperationBancaireList = mOperationBancaireList;
        //Collections.sort(mOperationBancaireList, comparing(OperationBancaire::getDate).reversed());
    }

    @Override
    public int getCount() {
        return mOperationBancaireList.size();
    }

    @Override
    public Object getItem(int position) {
        return mOperationBancaireList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View v = View.inflate(mContext, R.layout.item_mouvement_list, null);

        double montant = mOperationBancaireList.get(position).getMontant();

        TextView tvDescription = (TextView)v.findViewById(R.id.tv_Description);
        TextView tvMontant = (TextView)v.findViewById(R.id.tv_Montant);
        ImageView ivIcone = (ImageView)v.findViewById(R.id.img);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        tvDescription.setText(mOperationBancaireList.get(position).getDescription()/* + " - " + dateFormat.format(mOperationBancaireList.get(position).getDate())*/);
        tvMontant.setText(String.valueOf(montant) + "€");

        type = mOperationBancaireList.get(position).getType();

        switch(type){
            case 0:
                ivIcone.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_import_export));
                ivIcone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.cerclebackgroundgreen));
                break;
            case 1:
                ivIcone.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.supermarche));
                ivIcone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.cerclebackgroundyellow));
                break;
            case 2:
                ivIcone.setImageResource(R.drawable.restaurant);
                ivIcone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.cerclebackgroundblue));
                break;
            case 3:
                ivIcone.setImageResource(R.drawable.avion);
                ivIcone.setBackground(ContextCompat.getDrawable(mContext, R.drawable.cerclebackgroundpink));
                break;
        }

        if (montant < 0) {
            tvMontant.setTextColor(Color.RED);
        } else {
            tvMontant.setTextColor(Color.GREEN);
            tvMontant.setText("+" + tvMontant.getText());
        }

        v.setTag(mOperationBancaireList.get(position).getId());
        return v;
    }
}
