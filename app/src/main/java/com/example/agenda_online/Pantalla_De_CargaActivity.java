package com.example.agenda_online;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Pantalla_De_CargaActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_de_carga);

        firebaseAuth = FirebaseAuth.getInstance();

        Thread thread = new Thread(){
            @Override
            public void run(){
                try {
                    sleep(2000);// Dormir el hilo durante 2000 milisegundos (2 segundos)
                }catch (Exception e){
                    e.printStackTrace();
                }
                finally {
                    /*/ Crear un Intent para iniciar MainActivity
                    Intent intent = new Intent(Pantalla_De_CargaActivity.this, MainActivity.class);
                    startActivity(intent);// Iniciar la actividad*/
                    verificarUsuario();
                }
            }
        };
        thread.start();
    }

    private void verificarUsuario(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null){
            Intent intent = new Intent(Pantalla_De_CargaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(Pantalla_De_CargaActivity.this, MenuPrincipalActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish(); // Finaliza la actividad actual
    }
}