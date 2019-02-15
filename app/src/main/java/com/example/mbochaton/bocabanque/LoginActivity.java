package com.example.mbochaton.bocabanque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mbochaton.bocabanque.adapters.CompteBancaireListAdapter;
import com.example.mbochaton.bocabanque.models.CompteBancaire;
import com.example.mbochaton.bocabanque.models.Utilisateur;
import com.example.mbochaton.bocabanque.services.RequestHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements CardView.OnClickListener {
    private CardView loginCard;
    private List<Utilisateur> utilisateursList;
    private EditText loginEditText;
    private String loginText;
    private EditText passEditText;
    private String passText;
    private Utilisateur user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loginEditText = (EditText)findViewById(R.id.loginText);
        passEditText = (EditText)findViewById(R.id.passText);

        utilisateursList = new ArrayList<>();
        //loadUtilisateurs();

        loginCard = (CardView)findViewById(R.id.login_card);
        loginCard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        loginText = loginEditText.getText().toString();
        passText = passEditText.getText().toString();

        /*if(!utilisateursList.isEmpty() || !loginText.isEmpty() || !passText.isEmpty()) {
            for (int i = 0; i < utilisateursList.size(); i++) {
                if(utilisateursList.get(i).getEmail().equals(loginText)) {
                    user = utilisateursList.get(i);
                    break;
                }
            }

            if(user != null) {
                if(user.getMdp().equals(passText)) {
                    Intent i = new Intent(this, HomeActivity.class);
                    i.putExtra("prenom", user.getPrenom());
                    i.putExtra("nom", user.getNom());
                    i.putExtra("idUser", user.getId());
                    startActivity(i);
                }
            }
        }*/
        Intent i = new Intent(this, HomeActivity.class);
        i.putExtra("prenom", "yo");
        i.putExtra("nom", "yo");
        i.putExtra("idUser", 2);
        startActivity(i);
    }

    private void loadUtilisateurs() {
        RequestHelper.provideService().listUtilisateurs().enqueue(new Callback<List<Utilisateur>>() {
            @Override
            public void onResponse(Call<List<Utilisateur>> call, Response<List<Utilisateur>> response) {
                if(response.isSuccessful()) {
                    List<Utilisateur> utilisateurs = response.body();
                    utilisateursList.addAll(utilisateurs);
                    Toast.makeText(LoginActivity.this, utilisateurs.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(LoginActivity.this, "La requête a atteint le serveur, mais on s'est pris un " + response.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Utilisateur>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "La requête a échoué", Toast.LENGTH_LONG).show();
            }
        });
    }
}
