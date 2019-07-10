package com.appandroid.app.eizaguirre;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar; //Declaración de la clase toolbar

    Button botonBuscar, botonLimpiar;

    TextView referencia,precio;

    public static Spinner marcaSpinner, modeloSpinner, colorSpinner, tipoSpinner;


    DBHelper BD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        //setContentView ( R.layout.activity_main );
        //Para diseño en tablets o teléfonos

        if(esTablet(getApplicationContext())){ //tablet

            setContentView(R.layout.activity_main_tablet );

        }else{ // telefono
            setContentView(R.layout.activity_main );


        }

        toolbar = (Toolbar) findViewById (R.id.tool_bar);
        setSupportActionBar ( toolbar );


        getSupportActionBar().setDisplayShowTitleEnabled(false);



        //Botones

        botonBuscar = findViewById(R.id.buscar);

        botonLimpiar = findViewById(R.id.limpiar);


        //Spinners

        marcaSpinner = findViewById ( R.id.spinner_marca ) ;

        marcaSpinner.setOnItemSelectedListener(this);

        modeloSpinner = findViewById ( R.id.spinner_modelo ) ;

        modeloSpinner.setOnItemSelectedListener(this);

        colorSpinner = findViewById ( R.id.spinner_color ) ;

        colorSpinner.setOnItemSelectedListener(this);

        tipoSpinner = findViewById ( R.id.spinner_tipo ) ;

       //TextViews para resultados

        referencia = findViewById ( R.id.referenciaResult );

        precio = findViewById ( R.id.precioResult );
        //Crear SimpleCursorAdapter para conectar spinner con BBDD

        //Instancia de BBDD

        BD = new DBHelper ( this );
        BD.open ();



        loadSpinnerMarca();
       // loadSpinnerModelo();
        loadSpinnerTipo();
        //loadSpinnerColor();

        //Colocar botones a la escucha

        //LIMPIAR

         botonLimpiar.setOnClickListener(new View.OnClickListener()

        { //Limpiar pantalla

            public void onClick (View v){

                precio.setText ( "" );
                referencia.setText ( "" );

            }

        });




        botonBuscar.setOnClickListener(new View.OnClickListener()

        {//Botón buscar

            public void onClick (View v){ //BUSCAR


                SQLiteDatabase db = BD.getReadableDatabase ();

                // Define a projection that specifies which columns from the database
                // you will actually use after this query.
                String[] projection = {
                        //BaseColumns._ID,

                        BD.COLUMNA6,  //Referencia
                        BD.COLUMNA7   //Precio

                };

                // Filter results WHERE "title" = 'My Title'

                String selection = BD.COLUMNA2 + " = ? AND " + BD.COLUMNA3 + " = ? AND " + BD.COLUMNA4 + " = ? AND " + BD.COLUMNA5 + " = ?";

                String[] selectionArgs = {marcaSpinner.getSelectedItem ().toString (), modeloSpinner.getSelectedItem ().toString (),
                        tipoSpinner.getSelectedItem ().toString (), colorSpinner.getSelectedItem ().toString ()};


                try {

                    Cursor cursor = db.query (  //Para crear una tabla virtual
                            "TarifasWC",   // The table to query
                            projection,             // The array of columns to return (pass null to get all)
                            selection,              // The columns for the WHERE clause
                            selectionArgs,          // The values for the WHERE clause
                            null,                   // don't group the rows
                            null,                   // don't filter by row groups
                            null               // The sort order
                    );

                    cursor.moveToFirst (); //Para mover cursor al primer registro

                    referencia.setText ( cursor.getString ( 0) );   //Coluumna 1 de tabla virtual generada en la consulta
                    precio.setText ( cursor.getString ( 1 ) + " €" ); //Coluumna 0 de tabla virtual generada en la consulta

                    cursor.close();
                    db.close ();


                } catch (Exception e) {

                    Toast.makeText ( getApplicationContext (), "No se encontró registro", Toast.LENGTH_LONG ).show (); //Para capturar error en caso de colocar en la búsqueda un registro inexistente


                }


            }
        } );




    }



    @Override
    public void onPause() {
        super.onPause ();
        BD.close ();
    }

    @Override
    public void onResume() {
        super.onResume ();
        BD.open ();
    }


    //Para configurar diferentes pantallas según tamaño
    public static boolean esTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    //Para crear menú en a toolbar

    public boolean onCreateOptionsMenu (Menu menu){

            getMenuInflater ().inflate ( R.menu.menu_activity, menu); //Para rellenar el menu con los items del XML

            return true;

    }




    public boolean onOptionsItemSelected (MenuItem opcionmenu){ //Para dar función a los menús

            int id = opcionmenu.getItemId ();

            if (id==R.id.informacionMenu){
                Intent info = new Intent ( this, Informacion.class );
                startActivity ( info );
                return true;

            }

            if (id==R.id.salirMenu){

                finish ();
                return true;

            }

            return super.onOptionsItemSelected ( opcionmenu ); //llamamos a la clase padre y le pasamos el item opcionmenu
    }



    private void loadSpinnerMarca() {
        DBHelper db = new DBHelper(getApplicationContext());

        List<String> marcas = db.getMarca();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, marcas);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        // attaching data adapter to spinner
        marcaSpinner.setAdapter(dataAdapter);
    }


    private void loadSpinnerModelo() {
        DBHelper db = new DBHelper(getApplicationContext());

        List<String> modelos = db.getModelo();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, modelos);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        modeloSpinner.setAdapter(dataAdapter);
    }


    private void loadSpinnerTipo() {
        DBHelper db = new DBHelper(getApplicationContext());

        List<String> tipos = db.getTipo();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, tipos);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        // attaching data adapter to spinner
        tipoSpinner.setAdapter(dataAdapter);
    }

    private void loadSpinnerColor() {
        DBHelper db = new DBHelper(getApplicationContext());

        List<String> colores = db.getColor();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, colores);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        // attaching data adapter to spinner
        colorSpinner.setAdapter(dataAdapter);
    }



    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { //Métodos de la interfae AdapterView.OnItemSelectedListener

        String labels = parent.getItemAtPosition(position).toString();


        String marca= String.valueOf(marcaSpinner.getSelectedItem());
        String modelo= String.valueOf(modeloSpinner.getSelectedItem());



        switch (parent.getId()){
            case R.id.spinner_marca:

                DBHelper db = new DBHelper(getApplicationContext());

                List<String> modelos = db.getModelo();

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, modelos);
                dataAdapter.setDropDownViewResource(R.layout.spinner_item);

                modeloSpinner.setAdapter(dataAdapter);



                break;

            case R.id.spinner_modelo:

                db = new DBHelper(getApplicationContext());

                List<String> colores = db.getColor();

                dataAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, colores);
                dataAdapter.setDropDownViewResource(R.layout.spinner_item);

                colorSpinner.setAdapter(dataAdapter);


                break;


        }



       }



    public void onNothingSelected(AdapterView<?> arg0) {


    }






}

