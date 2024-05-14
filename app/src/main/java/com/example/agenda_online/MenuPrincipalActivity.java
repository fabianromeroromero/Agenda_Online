package com.example.agenda_online;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuPrincipalActivity extends AppCompatActivity {

    Button btnCerrarSecion;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);

        btnCerrarSecion = findViewById(R.id.btnCerrarSecion);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        btnCerrarSecion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirAplicacion();
            }
        });
    }

    private void SalirAplicacion() {
        firebaseAuth.signOut();
        Intent intent = new Intent(MenuPrincipalActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Cerraste secion exitosamente", Toast.LENGTH_SHORT).show();
    }
}