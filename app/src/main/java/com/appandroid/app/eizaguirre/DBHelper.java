package com.appandroid.app.eizaguirre;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static com.appandroid.app.eizaguirre.MainActivity.marcaSpinner;
import static com.appandroid.app.eizaguirre.MainActivity.modeloSpinner;

public class DBHelper extends SQLiteOpenHelper {

    //Ruta por defecto de las bases de datos en el sistema Android
    private static String DB_PATH = "/data/data/com.appandroid.app.eizaguirre/databases/";
    private static String DB_NAME = "TapasWC.db";

    private SQLiteDatabase myDataBase;
    private final Context myContext;

    /**
     * Constructor
     * Toma referencia hacia el contexto de la aplicación que lo invoca para
     * poder acceder a los 'assets' y 'resources' de la aplicación.
     * Crea un objeto DBOpenHelper que nos permitirá controlar la apertura de
     * la base de datos.
     *
     * @param context
     */
    public DBHelper(Context context) {
        super ( context, DB_NAME, null, 1 );
        this.myContext = context;
    }

    /**
     * Crea una base de datos vacía en el sistema y la reescribe con nuestro
     * fichero de base de datos.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase ();

        if (dbExist) {
            //la base de datos existe y no hacemos nada.
        } else {
            //Llamando a este método se crea la base de datos vacía en la ruta
            //por defecto del sistema de nuestra aplicación por lo que podremos
            //sobreescribirla con nuestra base de datos.
            this.getReadableDatabase ();

            try {

                copyDataBase ();

            } catch (IOException e) {
                throw new Error ( "Error copiando Base de Datos" );
            }
        }

    }

    /**
     * Comprueba si la base de datos existe para evitar copiar siempre el
     * fichero cada vez que se abra la aplicación.
     *
     * @return true si existe, false si no existe
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {

            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase ( myPath, null, SQLiteDatabase.OPEN_READONLY );

        } catch (SQLiteException e) {

            //si llegamos aqui es porque la base de datos no existe todavía.

        }
        if (checkDB != null) {

            checkDB.close ();

        }
        return checkDB != null ? true : false;
    }

    /**
     * Copia nuestra base de datos desde la carpeta assets a la recién creada
     * base de datos en la carpeta de sistema, desde dónde podremos acceder a
     * ella.
     * Esto se hace con bytestream.
     */
    private void copyDataBase() throws IOException {

        //Abrimos el fichero de base de datos como entrada
        InputStream myInput = myContext.getAssets ().open ( DB_NAME );

        //Ruta a la base de datos vacía recién creada
        String outFileName = DB_PATH + DB_NAME;

        //Abrimos la base de datos vacía como salida
        OutputStream myOutput = new FileOutputStream ( outFileName );

        //Transferimos los bytes desde el fichero de entrada al de salida
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read ( buffer )) > 0) {
            myOutput.write ( buffer, 0, length );
        }

        //Liberamos los streams
        myOutput.flush ();
        myOutput.close ();
        myInput.close ();

    }

    public void open() throws SQLException {

        //Abre la base de datos
        try {
            createDataBase ();
        } catch (IOException e) {
            throw new Error ( "Ha sido imposible crear la Base de Datos" );
        }

        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase ( myPath, null, SQLiteDatabase.OPEN_READONLY );

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null) myDataBase.close ();
        super.close ();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

//Establecemos los nombres de las columnas

    public static final String COLUMNA1 = "ID";
    public static final String COLUMNA2 = "Marca";
    public static final String COLUMNA3 = "Modelo";
    public static final String COLUMNA4 = "Tipo";
    public static final String COLUMNA5 = "Color";
    public static final String COLUMNA6 = "Referencia";
    public static final String COLUMNA7 = "Precio";
    public static final String TABLE_NAME = "TarifasWC";
    //Array de strings para su uso en los diferentes métodos
   // private static final String[] cols = new String[] { COLUMNA1, COLUMNA2, COLUMNA3, COLUMNA4, COLUMNA5, COLUMNA6 };


    //Métodos para coger la información de la BBDD y alimentar los spinners

    public List<String> getMarca(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT Marca FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }


    public List<String> getModelo(){
        List<String> list = new ArrayList<String>();

        String marca= marcaSpinner.getSelectedItem().toString ();

        // Select All Query
        String selectQuery = "SELECT DISTINCT Modelo FROM TarifasWC where Marca = " + "'" + marca + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }



    public List<String> getTipo(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT Tipo FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }

    public List<String> getColor(){

        List<String> list = new ArrayList<String>();

        String modelo= modeloSpinner.getSelectedItem().toString ();

        // Select All Query

        String selectQuery = "SELECT DISTINCT Color FROM TarifasWC where Modelo = " + "'" + modelo + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }


}






