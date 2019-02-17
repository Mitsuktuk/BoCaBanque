package com.example.mbochaton.bocabanque;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mbochaton.bocabanque.adapters.CompteBancaireListAdapter;
import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.services.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComptesActivity extends AppCompatActivity {
    private ListView lvComptes;
    private CompteBancaireListAdapter adapter, refreshAdapter;
    private List<CompteBancaire> mCompteBancaireList, refreshList;
    public Intent i;
    private long idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptes);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                idUser = 0;
            } else {
                idUser = extras.getLong("idUser");
            }
        } else {
            idUser = (Long) savedInstanceState.getSerializable("idUser");
        }

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/Krub-Regular.ttf");
        TextView tv = (TextView) findViewById(R.id.tv_titre);
        tv.setTypeface(tf);

        Toolbar tb = (Toolbar) findViewById(R.id.tb);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        lvComptes = (ListView) findViewById(R.id.listview_comptes);

        mCompteBancaireList = new ArrayList<>();
        refreshList = new ArrayList<>();
        loadComptes();

        lvComptes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompteBancaire cb = (CompteBancaire)parent.getItemAtPosition(position);
                final Intent i = new Intent(ComptesActivity.this, MouvementsActivity.class);
                i.putExtra("idCompteBancaire", cb.getId());
                i.putExtra("intituleCompteBancaire", cb.getIntitule());
                startActivity(i);
            }

        });
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
                            mCompteBancaireList.add(comptes.get(i));
                        }
                    }
                    adapter = new CompteBancaireListAdapter(getApplicationContext(), mCompteBancaireList);
                    lvComptes.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<CompteBancaire>> call, Throwable t) {
                Toast.makeText(ComptesActivity.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }
}
