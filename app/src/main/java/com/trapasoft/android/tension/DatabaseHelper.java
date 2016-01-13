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

        // debug. Para mostrar algun tipo de datos mientras desarrollamos
        // eliminar al pasar a produccion
        rellenaDatosDummy(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /**
     * Crea un registro de tension
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

                RegTension reg = new RegTension();
                reg.setId(new Long(c.getLong(c.getColumnIndex(KEY_ID))).intValue());
                reg.setSist(c.getLong(c.getColumnIndex(KEY_SIST)));
                reg.setDiast(c.getLong(c.getColumnIndex(KEY_DIAST)));
                reg.setPulso(c.getInt(c.getColumnIndex(KEY_PULSO)));

                lista.add(reg);
            }
        }

        return lista;
    }

    /**
     * Lista de datos de tensión de la última semana
     * @return un ArrayList que puede ser nulo
     */
    public ArrayList<RegTension> getLastWeekRegs() {

        /*
         SELECT ID, DATE(FECHAHORA) FECHA, TIME(FECHAHORA) HORA, SIST, DIAST, PULSO FROM DATOS_TENSION
        WHERE DATE(FECHAHORA) BETWEEN DATE('NOW','-7 DAYS')  AND DATE('NOW')
        ORDER BY FECHAHORA DESC;
        */

        String consulta = "SELECT " + KEY_ID + ", " + KEY_FECHAHORA + ", DATE("+ KEY_FECHAHORA +") as FECHA, TIME("+ KEY_FECHAHORA + ") as HORA, "+
                KEY_SIST +", "+ KEY_DIAST + ", " +KEY_PULSO+" FROM "+
                TABLE_NAME +
                " WHERE DATE(" + KEY_FECHAHORA + ") BETWEEN DATE('NOW','-7 DAYS')  AND DATE('NOW') " +
                " ORDER BY " + KEY_FECHAHORA + " DESC";
        ArrayList<RegTension> lista = new ArrayList<RegTension>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(consulta,null);

        if (c.moveToFirst()) {
            do {

                RegTension reg = new RegTension();
                reg.setId(new Long(c.getLong(c.getColumnIndex(KEY_ID))).intValue());
                reg.setFechahora(c.getString(c.getColumnIndex("FECHA")), c.getString(c.getColumnIndex("HORA")));
                reg.setSist(c.getLong(c.getColumnIndex(KEY_SIST)));
                reg.setDiast(c.getLong(c.getColumnIndex(KEY_DIAST)));
                reg.setPulso(c.getInt(c.getColumnIndex(KEY_PULSO)));

                lista.add(reg);
            } while(c.moveToNext());
        }

        return lista;
    }


    /**
     * rellena datos falsos para probar
     * eliminar la llamada a este metodo al pasar a produccion
     */
    public void rellenaDatosDummy(SQLiteDatabase db) {

        String CADENA_BORRADO = "DELETE FROM " + TABLE_NAME;

        String CADENA_INSERCION = "INSERT INTO " + TABLE_NAME + " VALUES " +
                "(1,'2015-12-16 08:30:00',123.0,43.4,65), " +
                "(2,'2015-12-15 08:30:00',145.0,54.3,87), " +
                "(3,'2015-12-14 09:00',2137716191.0,1907218830.0,2039565267), " +
                "(4,'2015-12-13 09:00',936145377.0,636708761.0,355103750), " +
                "(5,'2015-12-12 22:12:13',1215825599.0,150240575.0,704096693), " +
                "(6,'2015-12-12 09:00',589265238.0,1935915075.0,1814042643), " +
                "(7,'2015-12-11 09:00',924859463.0,2005480717.0,965027506), " +
                "(8,'2015-12-10 09:00',1182112391.0,1068432038.0,1865900)";

        db.execSQL(CADENA_BORRADO);
        db.execSQL(CADENA_INSERCION);

    }
}
