package com.example.mbochaton.bocabanque;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.Utilisateur;

public class HomeActivity extends AppCompatActivity implements CardView.OnClickListener {
    private CardView comptesCard;
    private CardView nfcCard;
    private CardView transactionsCard;
    private DrawerLayout mDrawerLayout;
    private String prenom;
    private String nom;
    private long idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                prenom = null;
                nom = null;
                idUser = 0;
            } else {
                prenom = extras.getString("prenom");
                nom = extras.getString("nom");
                idUser = extras.getLong("idUser");
            }
        } else {
            prenom = (String) savedInstanceState.getSerializable("prenom");
            nom = (String) savedInstanceState.getSerializable("nom");
            idUser = (Long) savedInstanceState.getSerializable("idUser");
        }

        comptesCard = (CardView)findViewById(R.id.comptes_card);
        comptesCard.setOnClickListener(this);

        nfcCard = (CardView)findViewById(R.id.nfc_card);
        nfcCard.setOnClickListener(this);

        transactionsCard = (CardView)findViewById(R.id.transactions_card);
        transactionsCard.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.tb);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.activity_home);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navIdentity = (TextView) headerView.findViewById(R.id.identity);
        navIdentity.setText(prenom + " " + nom);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_exit: {
                                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                                builder.setCancelable(true);
                                builder.setTitle("Déconnexion");
                                builder.setMessage("Confirmer la déconnexion ?");
                                builder.setPositiveButton("Valider",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                                                startActivity(i);
                                            }
                                        });
                                builder.setNegativeButton(android.R.string.cancel,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                menuItem.setChecked(false);
                                            }
                                });;
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                break;
                            }
                            default:
                                menuItem.setChecked(true);
                                break;
                        }

                        mDrawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()) {
            case R.id.comptes_card :
                i = new Intent(this, ComptesActivity.class);
                i.putExtra("idUser", idUser);
                break;
            case R.id.nfc_card :
                i = new Intent(this, NFCActivity.class);
                break;
            case R.id.transactions_card :
                i = new Intent(this, TransactionsActivity.class);
                i.putExtra("idUser", idUser);
                break;
            default :
                i = new Intent(this, ComptesActivity.class);
                break;
        }

        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
