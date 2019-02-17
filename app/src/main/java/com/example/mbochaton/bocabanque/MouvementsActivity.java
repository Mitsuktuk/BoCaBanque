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

import com.example.mbochaton.bocabanque.adapters.CompteBancaireListAdapter;
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

        loadOperations();

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
        RequestHelper.provideService().listOperations().enqueue(new Callback<List<OperationBancaire>>() {
            @Override
            public void onResponse(Call<List<OperationBancaire>> call, Response<List<OperationBancaire>> response) {
                if(response.isSuccessful()) {
                    List<OperationBancaire> operations = response.body();

                    for (int i = 0; i < operations.size(); i++) {
                        if(operations.get(i).getIdcompte() == idCompteBancaire) {
                            mOperationBancaireList.add(operations.get(i));
                        }
                    }

                    ViewPagerAdapter tabs_adapter = new ViewPagerAdapter(getSupportFragmentManager());
                    tabs_adapter.addFragment(new FragmentOperations(mOperationBancaireList), "Opérations");
                    tabs_adapter.addFragment(new FragmentStatistiques(), "Statistiques");
                    viewPager.setAdapter(tabs_adapter);
                    tabLayout.setupWithViewPager(viewPager);
                }
            }

            @Override
            public void onFailure(Call<List<OperationBancaire>> call, Throwable t) {
                Toast.makeText(MouvementsActivity.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }
}
