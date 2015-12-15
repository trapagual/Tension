package com.trapasoft.android.tension;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by alejandro on 15/12/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private String TAG = this.getClass().getSimpleName();

    private static final String DATABASE_NAME = "tension.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "datos_tension";

    // claves de los campos
    private static final String KEY_ID = "id";
    private static final String KEY_FECHAHORA = "fechahora";
    private static final String KEY_SIST = "sist";
    private static final String KEY_DIAST = "diast";
    private static final String KEY_PULSO = "pulso";

    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" +
            KEY_ID + " INTEGER PRIMARY KEY ," +
            KEY_FECHAHORA + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
            KEY_SIST + " REAL, " +
            KEY_DIAST + " REAL, " +
            KEY_PULSO + " INTEGER " +
            ")";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v(TAG, "CREACION DE LA B.D.: " + CREATE_TABLE);
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * Devuelve el ROWID del registro creado
     * Sirve para insertarlo en el objeto RegTension si lo necesitamos
     */
    public int creaRegistro(RegTension t){
        long c;

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_FECHAHORA, t.getFechahora());
        values.put(KEY_SIST, t.getSist());
        values.put(KEY_DIAST, t.getDiast());
        values.put(KEY_PULSO, t.getPulso());

        c = db.insert(TABLE_NAME, null, values);
        db.close();

        // lo pasamos a int
        return new Long(c).intValue();
    }

    public ArrayList<RegTension> getAllRegs() {
        String consulta = "SELECT * FROM " + TABLE_NAME +" ORDER BY " + KEY_FECHAHORA + " DESC";
        ArrayList<RegTension> lista = new ArrayList<RegTension>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(consulta,null);
        if (c != null) {
            while(c.moveToNext()) {

                RegTension reg = new RegTension()
                reg.setId(new Long(c.getLong(c.getColumnIndex(KEY_ID))).intValue());
                reg.setSist(c.getLong(c.getColumnIndex(KEY_SIST)));
                reg.setDiast(c.getLong(c.getColumnIndex(KEY_DIAST)));
                reg.setPulso(c.getInt(c.getColumnIndex(KEY_PULSO)));

                lista.add(reg);
            }
        }

        return lista;
    }



}
