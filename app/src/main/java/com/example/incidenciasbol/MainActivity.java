package com.example.incidenciasbol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Spinner spinerTipos;
    private EditText textUbicacion, textDescripcion;
    private ImageView imageFoto1,imageUbica;
    public int index;
    public String ubicacionCapturada;

    private String APP_DIRECTORY = "myPictureApp";
    private String MEDIA_DIRECTORY= APP_DIRECTORY+"media";
    private String TEMPORAL_PICTURE_NAME="temporal.jpg";

    private final int SELECT_PICTURE=200;
    private final int PHOTO_CODE=100;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinerTipos=(Spinner)findViewById(R.id.spinnerTipo);
        textUbicacion=(EditText)findViewById(R.id.textUbicacion);
        textDescripcion=(EditText)findViewById(R.id.textDescripcion);
        imageFoto1=(ImageView)findViewById(R.id.foto);
        imageUbica=(ImageView)findViewById(R.id.imageUbicacion);
        Button botonCamara=(Button)findViewById(R.id.botonCamara);

        String [] tipos = {"Bloqueo","Derrumbe","Intensas Lluvias","Accidente de transito","Nevada","Carretera en mant","Otros"};
        ArrayAdapter <String> adapter = new ArrayAdapter<>(this,R.layout.spiner_tipos_incidencias,tipos);
        spinerTipos.setAdapter(adapter);


        //accion para hacer clic en la imagen de captura de ubicacion
        imageUbica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

            ubicacionCapturada=getIntent().getStringExtra("ubicacionDeMapa");


            textUbicacion.setText(ubicacionCapturada);


        // acciones para el boton de tomar foto
        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] options = {"Tomar foto","Elegir de Galeria","Cancelar"};
                final AlertDialog.Builder builder= new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Elige una opcion");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int seleccion) {
                        if(options[seleccion]=="Tomar foto"){
                            openCamera();
                        }else if(options[seleccion]=="Elegir de Galeria"){
                            Intent intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent,"Selecciona la imagen de la incidencia"), SELECT_PICTURE);
                        }else if(options[seleccion]=="Cancelar"){
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 1000);
        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case PHOTO_CODE:
                if(requestCode==RESULT_OK){
                    String dir=Environment.getExternalStorageDirectory()+File.separator+MEDIA_DIRECTORY+File.separator+TEMPORAL_PICTURE_NAME;
                    decodeBitmap(dir);
                }
            break;

            case SELECT_PICTURE:
                if(requestCode==RESULT_OK){
                    Uri pathFoto=data.getData();
                    imageFoto1.setImageURI(pathFoto);
                }
                break;
        }
    }

    private void decodeBitmap(String dir) {
        Bitmap bitmap;
        bitmap= BitmapFactory.decodeFile(dir);
        imageFoto1.setImageBitmap(bitmap);
    }



    static final int REQUEST_TAKE_PHOTO = 1;
    public void openCamera(){

        File file=new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        file.mkdir();

        String pathFoto=Environment.getExternalStorageDirectory()+File.separator+MEDIA_DIRECTORY+File.separator+TEMPORAL_PICTURE_NAME;
        File newFile=new File(pathFoto);

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(intent, PHOTO_CODE);
        }

         /*
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = crearArchivoImagen();
            } catch (IOException ex) {

                Toast.makeText(this, "No se pudo tomar la foto", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.incidenciasbol",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI.toString());
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }else{
                Toast.makeText(this, "No se pudo crear el archivo", Toast.LENGTH_SHORT).show();
            }
        }*/
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

    String currentPhotoPath;

    public File crearArchivoImagen() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageFoto1.setImageBitmap(imageBitmap);
        }
    }*/

    public void enviarDatos(){

    }
}