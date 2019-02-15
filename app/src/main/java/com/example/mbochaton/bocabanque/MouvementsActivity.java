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

public class MouvementsActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private ListView lvMouvements;
    private TransactionListAdapter adapter;
    private List<OperationBancaire> mOperationBancaireList;

    private long idCompteBancaire;
    private String intituleCompteBancaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mouvements);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idCompteBancaire = 0;
                intituleCompteBancaire = null;
            } else {
                idCompteBancaire = extras.getLong("idCompteBancaire");
                intituleCompteBancaire = extras.getString("intituleCompteBancaire");
            }
        } else {
            idCompteBancaire = (Long)savedInstanceState.getSerializable("idCompteBancaire");
            intituleCompteBancaire = (String)savedInstanceState.getSerializable("intituleCompteBancaire");
        }

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Krub-Regular.ttf");
        TextView titreTextView = (TextView)findViewById(R.id.tv_titre);
        titreTextView.setText(intituleCompteBancaire);
        titreTextView.setTypeface(tf);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        viewPager = (ViewPager)findViewById(R.id.container);

        mOperationBancaireList = new ArrayList<>();
        /*if(compteBancaire.getId() == 1){
            mOperationBancaireList.add(new OperationBancaire(1,"Pepe Joe",-22.5, Calendar.getInstance().getTime(), 2));
            mOperationBancaireList.add(new OperationBancaire(2,"Intermarché",-94.4, Calendar.getInstance().getTime(),1));
            mOperationBancaireList.add(new OperationBancaire(3,"Mcdonalds",-31.4, Calendar.getInstance().getTime(), 2));
            mOperationBancaireList.add(new OperationBancaire(4,"Vol Emirates",1050, Calendar.getInstance().getTime(), 3));
        }*/

        ViewPagerAdapter tabs_adapter = new ViewPagerAdapter(getSupportFragmentManager());
        tabs_adapter.addFragment(new FragmentOperations(mOperationBancaireList), "Opérations");
        tabs_adapter.addFragment(new FragmentStatistiques(), "Statistiques");
        viewPager.setAdapter(tabs_adapter);
        tabLayout.setupWithViewPager(viewPager);

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

    private void loadOperations() {
        RequestHelper.provideService().listComptes().enqueue(new Callback<List<CompteBancaire>>() {
            @Override
            public void onResponse(Call<List<CompteBancaire>> call, Response<List<CompteBancaire>> response) {
                if(response.isSuccessful()) {
                    List<CompteBancaire> comptes = response.body();
                    CompteBancaire compte = null;

                    for (int i = 0; i < comptes.size(); i++) {
                        if(comptes.get(i).getId() == idCompteBancaire) {
                            compte = comptes.get(i);
                            break;
                        }
                    }
                    /*if (compte != null) {
                        compte.getOperationbancaireCollection();
                    }*/


                    //adapter = new CompteBancaireListAdapter(getApplicationContext(), mCompteBancaireList);
                    //lvComptes.setAdapter(adapter);
                } else {
                    Toast.makeText(MouvementsActivity.this, "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompteBancaire>> call, Throwable t) {
                Toast.makeText(MouvementsActivity.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }
}
