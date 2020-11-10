package com.example.incidenciasbol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Spinner spinerTipos;
    private EditText textUbicacion, textDescripcion;
    private ImageView imageFoto1,imageFoto2,imageUbica;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinerTipos=(Spinner)findViewById(R.id.spinnerTipo);
        textUbicacion=(EditText)findViewById(R.id.textUbicacion);
        textDescripcion=(EditText)findViewById(R.id.textDescripcion);
        imageFoto1=(ImageView)findViewById(R.id.foto1);
        imageFoto2=(ImageView)findViewById(R.id.foto2);
        imageUbica=(ImageView)findViewById(R.id.imageUbicacion);

        String [] tipos = {"Bloqueo","Derrumbe","Intensas Lluvias","Accidente de transito","Nevada","Carretera en mant","Otros"};
        ArrayAdapter <String> adapter = new ArrayAdapter<>(this,R.layout.spiner_tipos_incidencias,tipos);
        spinerTipos.setAdapter(adapter);

        imageUbica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        String ubicacionCapturada=getIntent().getStringExtra("ubicacionDeMapa");


            textUbicacion.setText(ubicacionCapturada);


    }
}