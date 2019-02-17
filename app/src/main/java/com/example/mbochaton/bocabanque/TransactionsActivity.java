package com.example.mbochaton.bocabanque;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbochaton.bocabanque.adapters.TransactionListAdapter;
import com.example.mbochaton.bocabanque.adapters.ViewPagerAdapter;
import com.example.mbochaton.bocabanque.fragments.FragmentExterne;
import com.example.mbochaton.bocabanque.fragments.FragmentInterne;
import com.example.mbochaton.bocabanque.fragments.FragmentOperations;
import com.example.mbochaton.bocabanque.fragments.FragmentStatistiques;
import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.OperationBancaire;
import com.example.mbochaton.bocabanque.services.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ListView lvTransactions;
    private TransactionListAdapter adapter;
    private List<OperationBancaire> mOperationBancaireList;

    private long idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idUser = 0;
            } else {
                idUser = extras.getLong("idCompteBancaire");
            }
        } else {
            idUser = (Long)savedInstanceState.getSerializable("idCompteBancaire");
        }

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Krub-Regular.ttf");
        TextView titreTextView = (TextView)findViewById(R.id.tv_titre);
        titreTextView.setTypeface(tf);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tabLayout = (TabLayout)findViewById(R.id.transactions_tabs);
        viewPager = (ViewPager)findViewById(R.id.transactions_container);

        ViewPagerAdapter tabs_adapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabs_adapter.addFragment(new FragmentInterne(), "Internes");
        tabs_adapter.addFragment(new FragmentExterne(), "Externes");
        viewPager.setAdapter(tabs_adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
