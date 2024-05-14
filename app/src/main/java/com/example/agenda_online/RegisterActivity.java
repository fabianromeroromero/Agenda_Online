package com.example.agenda_online;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText edtNombreUser, edtEmailUser, edtPasswordUser, edtPasswordConfir;
    Button btnNewRegister;
    TextView txtTengoCuenta;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String nombre = "", correo = "", password = "", confirmarpassword = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Configura la barra de acción (ActionBar)
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registar");
        actionBar.setDisplayHomeAsUpEnabled(true); // Habilita el botón de "Atrás" en la barra de acción
        actionBar.setDisplayShowHomeEnabled(true); // Habilita mostrar el ícono de la aplicación en la barra de acción

        edtNombreUser = findViewById(R.id.edtNombreUser);
        edtEmailUser = findViewById(R.id.edtEmailUser);
        edtPasswordUser = findViewById(R.id.edtPasswordUser);
        edtPasswordConfir = findViewById(R.id.edtPasswordConfir);
        btnNewRegister = findViewById(R.id.btnNewRegister);
        txtTengoCuenta = findViewById(R.id.txtTengoCuenta);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false); // Evita que se cancele al tocar fuera del diálogo

        // Configura el listener de clics para el botón de registro
        btnNewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarDatos();
            }
        });

        // Configura el listener de clics para el TextView de "Ya tengo cuenta"
        txtTengoCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para validar los datos ingresados por el usuario
    private void validarDatos(){
        nombre = edtNombreUser.getText().toString();
        correo = edtEmailUser.getText().toString();
        password = edtPasswordUser.getText().toString();
        confirmarpassword = edtPasswordConfir.getText().toString();

        // Validaciones de los datos ingresados
        if (TextUtils.isEmpty(nombre)){
            Toast.makeText(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(confirmarpassword)) {
            Toast.makeText(this, "Confirme contaseña", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmarpassword)) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        else {
            crearCuenta(); // Llama al método para crear la cuenta de usuario
        }
    }

    // Método para crear la cuenta de usuario en Firebase
    private void crearCuenta() {
        progressDialog.setMessage("Creando su cuenta");
        progressDialog.show(); // Muestra el ProgressDialog

        // Crea un usuario en Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        GuardarInformacion(); // Llama al método para guardar la información del usuario
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss(); // Descarta el ProgressDialog
                        Toast.makeText(RegisterActivity.this, "correo ya existente"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para guardar la información del usuario en Firebase Realtime Database
    private void GuardarInformacion() {
        progressDialog.setMessage("Guardondo su informacion");
        progressDialog.dismiss();

        //Obtener la identificacion de usuario actual
        String uid= firebaseAuth.getUid();

        // Crea un HashMap para almacenar los datos del usuario
        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid", uid);
        Datos.put("correo", correo);
        Datos.put("nombres", nombre);
        Datos.put("password", password);

        // Obtiene una referencia a la base de datos de Firebase y guarda los datos del usuario
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Cuenta creada con exito", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, MenuPrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "mal"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método que maneja el comportamiento del botón de "Atrás" en la barra de acción
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed(); // Llama al método onBackPressed() para manejar la navegación hacia atrás
        return super.onSupportNavigateUp();
    }
}