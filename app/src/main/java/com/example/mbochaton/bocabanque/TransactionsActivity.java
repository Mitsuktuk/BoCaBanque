package com.example.mbochaton.bocabanque;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import com.example.mbochaton.bocabanque.adapters.ViewPagerAdapter;
import com.example.mbochaton.bocabanque.fragments.FragmentExterne;
import com.example.mbochaton.bocabanque.fragments.FragmentInterne;
import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.Utilisateur;
import com.example.mbochaton.bocabanque.services.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter tabs_adapter;

    private List<String> mCompteBancaireList = new ArrayList<>();
    private List<String> mUtilisateurList = new ArrayList<>();

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
                idUser = extras.getLong("idUser");
            }
        } else {
            idUser = (Long)savedInstanceState.getSerializable("idUser");
        }

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Krub-Regular.ttf");
        TextView titreTextView = (TextView)findViewById(R.id.tv_titre);
        titreTextView.setTypeface(tf);

        tabLayout = (TabLayout)findViewById(R.id.transactions_tabs);
        viewPager = (ViewPager)findViewById(R.id.transactions_container);
        tabs_adapter = new ViewPagerAdapter(getSupportFragmentManager());

        loadComptes();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadComptes() {
        RequestHelper.provideService().listComptes().enqueue(new Callback<List<CompteBancaire>>() {
            @Override
            public void onResponse(Call<List<CompteBancaire>> call, Response<List<CompteBancaire>> response) {
                if(response.isSuccessful()) {
                    List<CompteBancaire> comptes = response.body();

                    for (int i = 0; i < comptes.size(); i++) {
                        if(comptes.get(i).getIdUser() == idUser) {
                            mCompteBancaireList.add(comptes.get(i).getIntitule());
                        }
                    }
                    loadUtilisateurs();
                } else {
                    Toast.makeText(TransactionsActivity.this, "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompteBancaire>> call, Throwable t) {
                Toast.makeText(TransactionsActivity.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadUtilisateurs() {
        RequestHelper.provideService().listUtilisateurs().enqueue(new Callback<List<Utilisateur>>() {
            @Override
            public void onResponse(Call<List<Utilisateur>> call, Response<List<Utilisateur>> response) {
                if(response.isSuccessful()) {
                    List<Utilisateur> utilisateurs = response.body();
                    for (int i = 0; i < utilisateurs.size(); i++) {
                        if(utilisateurs.get(i).getId() != idUser && !utilisateurs.get(i).getEmail().equals("admin")) {
                            mUtilisateurList.add(utilisateurs.get(i).getEmail());
                        }
                    }
                    tabs_adapter.addFragment(new FragmentInterne(mCompteBancaireList, idUser), "Internes");
                    tabs_adapter.addFragment(new FragmentExterne(mCompteBancaireList, mUtilisateurList, idUser), "Externes");
                    viewPager.setAdapter(tabs_adapter);
                    tabLayout.setupWithViewPager(viewPager);
                } else {
                    Toast.makeText(TransactionsActivity.this, "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Utilisateur>> call, Throwable t) {
                Toast.makeText(TransactionsActivity.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }
}
