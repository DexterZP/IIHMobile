package br.dexter.iihmobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.app.Dialog;

import br.dexter.iihmobile.SQlite.BD;
import br.dexter.iihmobile.SQlite.Data;
import br.dexter.iihmobile.SQlite.UsuarioAdapter;

public class Visualizar extends AppCompatActivity
{
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_main);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        setTitle("Salvos");

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        BD bd = new BD(this);
        UsuarioAdapter adapter = new UsuarioAdapter(this, bd.buscar());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        
        if(bd.buscar().size() == 0) {
            BuscarDados();
        }
    }

    public void BuscarDados()
    {
        if(user != null && isOnline())
        {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.sincronizar);
            dialog.setCancelable(false);
            dialog.show();

            ValueEventListener valueEventListener = new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if(!dataSnapshot.exists()) {
                        dialog.dismiss();
                        return;
                    }

                    for(DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        final String state = snap.getKey(); assert state != null;

                        ValueEventListener valueEventListener1 = new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                            {
                                for(DataSnapshot snap : dataSnapshot.getChildren())
                                {
                                    final String city = snap.getKey(); assert city != null;

                                    ValueEventListener valueEventListener2 = new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            for(DataSnapshot snap : dataSnapshot.getChildren())
                                            {
                                                final String local = snap.getKey(); assert local != null;

                                                ValueEventListener valueEventListener3 = new ValueEventListener()
                                                {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                                    {
                                                        Data usuario = new Data();

                                                        String data = dataSnapshot.child("Data").getValue(String.class);
                                                        String local = dataSnapshot.child("Local").getValue(String.class);
                                                        String localidade = dataSnapshot.child("Localidade").getValue(String.class);
                                                        Float latitude = dataSnapshot.child("Latitude").getValue(Float.class);
                                                        Float longitude = dataSnapshot.child("Longitude").getValue(Float.class);
                                                        Float Q1 = dataSnapshot.child("Pergunta 1").getValue(Float.class);
                                                        Float Q2 = dataSnapshot.child("Pergunta 2").getValue(Float.class);
                                                        Float Q3 = dataSnapshot.child("Pergunta 3").getValue(Float.class);
                                                        Float Q4 = dataSnapshot.child("Pergunta 4").getValue(Float.class);
                                                        Float Q5 = dataSnapshot.child("Pergunta 5").getValue(Float.class);
                                                        Float Q6 = dataSnapshot.child("Pergunta 6").getValue(Float.class);
                                                        Float Q7 = dataSnapshot.child("Pergunta 7").getValue(Float.class);
                                                        Float Q8 = dataSnapshot.child("Pergunta 8").getValue(Float.class);
                                                        Float Q9 = dataSnapshot.child("Pergunta 9").getValue(Float.class);
                                                        Float Q10 = dataSnapshot.child("Pergunta 10").getValue(Float.class);
                                                        Float Q11 = dataSnapshot.child("Pergunta 11").getValue(Float.class);
                                                        Float Q12 = dataSnapshot.child("Pergunta 12").getValue(Float.class);
                                                        Float scoreFinal = dataSnapshot.child("Score Final").getValue(Float.class);

                                                        usuario.setLocal(local);
                                                        usuario.setLocalidade(localidade);
                                                        usuario.setLatitude(latitude);
                                                        usuario.setLongitude(longitude);
                                                        usuario.setQ1(Q1);
                                                        usuario.setQ2(Q2);
                                                        usuario.setQ3(Q3);
                                                        usuario.setQ4(Q4);
                                                        usuario.setQ5(Q5);
                                                        usuario.setQ6(Q6);
                                                        usuario.setQ7(Q7);
                                                        usuario.setQ8(Q8);
                                                        usuario.setQ9(Q9);
                                                        usuario.setQ10(Q10);
                                                        usuario.setQ11(Q11);
                                                        usuario.setQ12(Q12);
                                                        usuario.setScoreFinal(scoreFinal);
                                                        usuario.setData(data);
                                                        usuario.setCity(city);
                                                        usuario.setState(state);

                                                        BD bd = new BD(Visualizar.this);
                                                        bd.inserir(usuario);

                                                        startActivity(getIntent());
                                                        finish();

                                                        dialog.dismiss();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                                };
                                                databaseReference.child("IIH Mobile - Índice").child(user.getUid()).child(state).child(city).child(local).addListenerForSingleValueEvent(valueEventListener3);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                                    };
                                    databaseReference.child("IIH Mobile - Índice").child(user.getUid()).child(state).child(city).addListenerForSingleValueEvent(valueEventListener2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        };
                        databaseReference.child("IIH Mobile - Índice").child(user.getUid()).child(state).addListenerForSingleValueEvent(valueEventListener1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            };
            databaseReference.child("IIH Mobile - Índice").child(user.getUid()).addListenerForSingleValueEvent(valueEventListener);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnectedOrConnecting();
        }
        return false;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}