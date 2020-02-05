package br.dexter.iihmobile.SQlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class BD
{
    private SQLiteDatabase bd;

    public BD(Context context)
    {
        BDcore auxBd = new BDcore(context);
        bd = auxBd.getWritableDatabase();
    }

    public void inserir(Data usuario)
    {
        ContentValues valores = new ContentValues();
        valores.put("local", usuario.getLocal());
        valores.put("localidade", usuario.getLocalidade());
        valores.put("q1", usuario.getQ1());
        valores.put("q2", usuario.getQ2());
        valores.put("q3", usuario.getQ3());
        valores.put("q4", usuario.getQ4());
        valores.put("q5", usuario.getQ5());
        valores.put("q6", usuario.getQ6());
        valores.put("q7", usuario.getQ7());
        valores.put("q8", usuario.getQ8());
        valores.put("q9", usuario.getQ9());
        valores.put("q10", usuario.getQ10());
        valores.put("q11", usuario.getQ11());
        valores.put("q12", usuario.getQ12());
        valores.put("score", usuario.getScoreFinal());
        valores.put("data", usuario.getData());
        valores.put("latitude", usuario.getLatitude());
        valores.put("longitude", usuario.getLongitude());
        valores.put("city", usuario.getCity());
        valores.put("state", usuario.getState());
        bd.insert("usuario", null, valores);
    }

    public List<Data> buscar()
    {
        List<Data> list = new ArrayList<>();
        String [] colunas = new String[]{
                "_id", "local", "localidade", "q1", "q2", "q3", "q4", "q5", "q6", "q7", "q8", "q9", "q10", "q11", "q12", "score", "data", "latitude", "longitude", "city", "state"
        };

        @SuppressLint("Recycle")
        Cursor cursor = bd.query("usuario", colunas, null, null, null, null, "local ASC");

        if(cursor.getCount() > 0)
        {
            cursor.moveToFirst();

            do
            {
                Data u = new Data();

                u.setId(cursor.getLong(0));
                u.setLocal(cursor.getString(1));
                u.setLocalidade(cursor.getString(2));
                u.setQ1(cursor.getFloat(3));
                u.setQ2(cursor.getFloat(4));
                u.setQ3(cursor.getFloat(5));
                u.setQ4(cursor.getFloat(6));
                u.setQ5(cursor.getFloat(7));
                u.setQ6(cursor.getFloat(8));
                u.setQ7(cursor.getFloat(9));
                u.setQ8(cursor.getFloat(10));
                u.setQ9(cursor.getFloat(11));
                u.setQ10(cursor.getFloat(12));
                u.setQ11(cursor.getFloat(13));
                u.setQ12(cursor.getFloat(14));
                u.setScoreFinal(cursor.getFloat(15));
                u.setData(cursor.getString(16));
                u.setLatitude(cursor.getFloat(17));
                u.setLongitude(cursor.getFloat(18));
                u.setCity(cursor.getString(19));
                u.setState(cursor.getString(20));
                list.add(u);
            }while(cursor.moveToNext());
        }
        return (list);
    }
}
