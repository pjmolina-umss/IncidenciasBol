package com.example.incidenciasbol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Spinner spinerTipos;
    private EditText textUbicacion, textDescripcion;
    private ImageView imageFoto1,imageFoto2,imageUbica;
    public int index;
    public String ubicacionCapturada;



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

            ubicacionCapturada=getIntent().getStringExtra("ubicacionDeMapa");


            textUbicacion.setText(ubicacionCapturada);


    }

   /* public void onSaveInstanceState(Bundle estado){
        //index=spinerTipos.getSelectedItemPosition();
        //Toast.makeText(this, index, Toast.LENGTH_SHORT).show();
        estado.putInt("tipo",3);
        estado.putString("descripcion","Holachoy");
        super.onSaveInstanceState(estado);
    }


    public void onRestoreInstanceState(Bundle estado){
        super.onRestoreInstanceState(estado);
        index=estado.getInt("tipo");
        textDescripcion.setText(estado.getString("descripcion"));
        spinerTipos.setSelection(index);
    }

    */

    public void onPause(){
        super.onPause();
        String descrip=textDescripcion.getText().toString();
        int index=spinerTipos.getSelectedItemPosition();
        //ubicacionCapturada=textUbicacion.getText().toString();
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor miEditor=datos.edit();

        miEditor.putString("descripcion",descrip);
        miEditor.putInt("tipo",index);
        miEditor.putString("ubicacion",ubicacionCapturada);
        miEditor.apply();
    }

    public void onResume(){
        super.onResume();

        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        String descrip=datos.getString("descripcion","");
        int index=datos.getInt("tipo",0);
        //ubicacionCapturada=datos.getString("ubicacion","Hola");
        textDescripcion.setText(descrip);
        spinerTipos.setSelection(index);
        //textUbicacion.setText(ubicacionCapturada);

    }
}