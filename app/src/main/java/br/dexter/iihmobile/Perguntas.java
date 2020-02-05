package br.dexter.iihmobile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.rey.material.app.Dialog;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.dexter.iihmobile.SQlite.BD;
import br.dexter.iihmobile.SQlite.Data;
import in.myinnos.customfontlibrary.TypefaceUtil;

public class Perguntas extends AppCompatActivity
{
    private Data usuario = new Data();

    private Button Title, Resposta0, Resposta1, Resposta2, Resposta3, Resposta4, Resposta5, Proxima;
    private TextView Score, ScoreFinal;

    private LinearLayout linear1, linear2;
    private Button PermitirLocation;

    private int Prox = 1;
    private int QuestionMax = 12;
    private float Score0 = 0, Score1 = 1, Score2 = 2, Score3 = 3, Score4 = 4, Score5 = 5;
    private float Quest1 = 4, Quest2 = 5, Quest3 = 3, Quest4 = 3, Quest5 = 3, Quest6 = 3, Quest7 = 4, Quest8 = 3, Quest9 = 3, Quest10 = 3, Quest11 = 3, Quest12 = 4;
    private float GuardQ1 = 0, GuardQ2 = 0, GuardQ3 = 0, GuardQ4 = 0, GuardQ5 = 0, GuardQ6 = 0, GuardQ7 = 0, GuardQ8 = 0, GuardQ9 = 0, GuardQ10 = 0, GuardQ11 = 0, GuardQ12 = 0;
    private float ScoreGuard = 0;

    private float latitude, longitude;
    private String city, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/minhafonte.ttf");
        setContentView(R.layout.activity_perguntas);

        setTitle("Índice");

        Title = findViewById(R.id.Title_Perguntas);
        Resposta0 = findViewById(R.id.resposta0);
        Resposta1 = findViewById(R.id.resposta1);
        Resposta2 = findViewById(R.id.resposta2);
        Resposta3 = findViewById(R.id.resposta3);
        Resposta4 = findViewById(R.id.resposta4);
        Resposta5 = findViewById(R.id.resposta5);
        Proxima = findViewById(R.id.Proximo);

        linear1 = findViewById(R.id.Linear1);
        linear2 = findViewById(R.id.Linear2);
        PermitirLocation = findViewById(R.id.Permissao);

        Score = findViewById(R.id.Score);
        ScoreFinal = findViewById(R.id.ScoreFinal);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Verificar();
        IniciarP();
    }

    public void Verificar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.VISIBLE);

            Inicializar();
        }

        PermitirLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions()) {
                    linear1.setVisibility(View.GONE);
                    linear2.setVisibility(View.VISIBLE);

                    Inicializar();
                }
            }
        });
    }

    private boolean checkAndRequestPermissions() {
        int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (coarse != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);

            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        Inicializar();
    }

    private void Inicializar() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog);
        dialog.setCancelable(false);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = (float) location.getLatitude();
                longitude = (float) location.getLongitude();

                city = null;
                state = null;
                Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    city = addresses.get(0).getSubAdminArea();
                    state = addresses.get(0).getAdminArea();

                    dialog.dismiss();
                } catch (IOException e) {
                    city = "null";
                    state = "null";
                    dialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
            dialog.dismiss();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) { return; }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

            dialog.show();
        }
    }

    private void showGPSDisabledAlertToUser()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Percebemos que seu GPS está desativado, para uma melhor experiência, precisamos que ative seu GPS")
        .setCancelable(false)
        .setPositiveButton("Vamos ativar",
        new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void IniciarP() {
        Item1();
        Item2();
        Item3();
        Item4();
        Item5();
        Item6();
        Item7();
        Item8();
        Item9();
        Item10();
        Item11();
        Item12();
        Verificacao();
    }

    @SuppressLint("SetTextI18n")
    private void Item1() {
        if (Prox == 1) {
            Resposta5.setVisibility(View.GONE);

            Title.setText("1) Padrão de Uso da Terra além da zona de vegetação ribeirinha");

            Resposta0.setText("0. Cultivos Agrícolas de ciclo curto");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest1);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ1 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1));
                }
            });

            Resposta1.setText("1. Pasto");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest1);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ1 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1));
                }
            });

            Resposta2.setText("2. Cultivos Agrícolas de ciclo longo");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest1);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ1 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1));
                }
            });

            Resposta3.setText("3. Capoeira");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score3 / Quest1);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ1 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1));
                }
            });

            Resposta4.setText("4. Floresta Contínua");
            Resposta4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score4 / Quest1);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ1 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item2() {
        if (Prox == 2) {
            Resposta5.setVisibility(View.VISIBLE);

            Title.setText("2) Largura da Mata Ciliar");

            Resposta0.setText("0. Vegetação arbustiva ciliar ausente");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest2);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ2 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2));
                }
            });

            Resposta1.setText("1. Mata ciliar ausente com alguma vegetação arbustiva");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest2);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ2 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2));
                }
            });

            Resposta2.setText("2. Mata ciliar bem definida de 1 a 5m de largura");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest2);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ2 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2));
                }
            });

            Resposta3.setText("3. Mata ciliar bem definida entre 5 e 30m de largura");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score3 / Quest2);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ2 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2));
                }
            });

            Resposta4.setText("4. Mata ciliar bem definida com mais de 30m");
            Resposta4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score4 / Quest2);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ2 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2));
                }
            });

            Resposta5.setText("5. Continuidade da mata ciliar com a floresta adjacente");
            Resposta5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score5 / Quest2);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ2 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item3() {
        if (Prox == 3) {
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("3) Estado de preservação da Mata Ciliar");

            Resposta0.setText("0. Cicatrizes profundas com barrancos ao longo do seu comprimento");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest3);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ3 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3));
                }
            });

            Resposta1.setText("1. Quebra frequente com algumas cicatrizes e barrancos");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest3);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ3 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3));
                }
            });

            Resposta2.setText("2. Quebra ocorrendo em intervalos maiores que 50m");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest3);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ3 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3));
                }
            });

            Resposta3.setText("3. Mata Ciliar intacta sem quebras de continuidade");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score3 / Quest3);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ3 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item4() {
        if (Prox == 4) {
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("4) Estado da Mata ciliar dentro de uma faixa de 10m");

            Resposta0.setText("0. Vegetação constituída de grama e poucos arbustos");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest4);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ4 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4));
                }
            });

            Resposta1.setText("1. Mescla de grama com algumas árvores pioneiras e arbustos");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest4);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ4 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4));
                }
            });

            Resposta2.setText("2. Espécies pioneiras mescladas com árvores maduras");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest4);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ4 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4));
                }
            });

            Resposta3.setText("3. Mais de 90% da densidade é constituída de árvores não pioneiras ou nativas");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score3 / Quest4);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ4 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item5() {
        if (Prox == 5) {
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("5) Dispositivos de retenção");

            Resposta0.setText("0. Canal livre com poucos dispositivos de retenção");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest5);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ5 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5));
                }
            });

            Resposta1.setText("1. Dispositivo de retenção solto movendo-se com o fluxo");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest5);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ5 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5));
                }
            });

            Resposta2.setText("2. Rochas e/ou troncos presentes mas, preenchidas com sedimento");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest5);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ5 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5));
                }
            });

            Resposta3.setText("3. Canal com rochas e/ou troncos firmemente colocadas no local");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score3 / Quest5);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ5 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item6() {
        if (Prox == 6) {
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("6) Sedimentos no canal");

            Resposta0.setText("0. Canal divido em tranças ou rio canalizado");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest6);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ6 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6));
                }
            });

            Resposta1.setText("1. Barreira de sedimento e pedras, areia e silte comuns");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest6);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ6 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6));
                }
            });

            Resposta2.setText("2. Algumas barreiras de cascalho e pedra bruta e pouco silte");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest6);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ6 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6));
                }
            });

            Resposta3.setText("3. Pouco ou nenhum alargamento resultante de acúmulo de sedimento");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score3 / Quest6);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ6 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item7() {
        if (Prox == 7) {
            Resposta4.setVisibility(View.VISIBLE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("7) Estrutura do barranco do rio");

            Resposta0.setText("0. Barranco instável com solo e areia soltos, facilmente perturbável");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest7);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ7 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7));
                }
            });

            Resposta1.setText("1. Barranco com solo livre e uma camada esparsa de grama e arbustos");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest7);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ7 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7));
                }
            });

            Resposta2.setText("2. Barranco firme, coberto por grama e arbustos");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest7);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ7 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7));
                }
            });

            Resposta3.setText("3. Barranco estável de rochas e/ou solo firme, coberto de grama, arbustos e raízes");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score3 / Quest7);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ7 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7));
                }
            });

            Resposta4.setText("4. Ausência de barrancos");
            Resposta4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score4 / Quest7);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ7 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item8() {
        if (Prox == 8) {
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("8) Escavação sob o barranco");

            Resposta0.setText("0. Escavações severas ao longo do canal, com queda de barrancos");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest8);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ8 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8));
                }
            });

            Resposta1.setText("1. Escavações freqüentes");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest8);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ8 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8));
                }
            });

            Resposta2.setText("2. Escavações apenas nas curvas e constrições");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest8);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ8 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8));
                }
            });

            Resposta3.setText("3. Pouca ou nenhuma evidência, ou restrita a áreas de suporte de raízes");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score3 / Quest8);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ8 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item9() {
        if (Prox == 9) {
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("9) Leito do rio");

            Resposta0.setText("0. Fundo uniforme de silte e areia livres, substrato de pedra ausente");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest9);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ9 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9));
                }
            });

            Resposta1.setText("1. Fundo de silte, cascalho e areia em locais estáveis");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest9);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ9 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9));
                }
            });

            Resposta2.setText("2. Fundo de pedra facilmente móvel, com pouco silte");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest9);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ9 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9));
                }
            });

            Resposta3.setText("3. Fundo de pedras de vários tamanhos, agrupadas, com interstício óbvio");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score3 / Quest9);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ9 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item10() {
        if (Prox == 10) {
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("10) Áreas de corredeiras e poções ou meandros");

            Resposta0.setText("0. Meandros e áreas de corredeiras/poções ausentes ou rio canalizado");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest10);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ10 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10));
                }
            });

            Resposta1.setText("1. Longos poções separando curtas áreas de corredeiras, meandros ausentes");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest10);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ10 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10));
                }
            });

            Resposta2.setText("2. Espaçamento irregular");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest10);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ10 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10));
                }
            });

            Resposta3.setText("3. Distintas, ocorrendo em intervalos de 5 a 7 vezes a largura do rio");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score3 / Quest10);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ10 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item11() {
        if (Prox == 11) {
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("11) Vegetação Aquática");

            Resposta0.setText("0. Algas emaranhadas no fundo, plantas vasculares dominam o canal");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest11);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ11 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11));
                }
            });

            Resposta1.setText("1. Emaranhados de algas, algumas plantas vasculares e poucos musgos");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest11);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ11 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11));
                }
            });

            Resposta2.setText("2. Algas dominantes nos poções, plantas vasculares semi-aquáticas ou aquáticas ao longo da margem");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest11);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ11 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11));
                }
            });

            Resposta3.setText("3. Quando presente consiste de musgos e manchas de algas");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score3 / Quest11);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ11 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Score.setText("Score: 0.0");
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11 + GuardQ12));

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Item12() {
        if (Prox == 12) {
            Resposta4.setVisibility(View.VISIBLE);
            Resposta5.setVisibility(View.GONE);

            Title.setText("12) Detritos");

            Resposta0.setText("0. Sedimento fino anaeróbio, nenhum detrito bruto");
            Resposta0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score0 / Quest12);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ12 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11 + GuardQ12));
                }
            });

            Resposta1.setText("1. Nenhuma folha ou madeira, matéria orgânica bruta e fina com sedimento");
            Resposta1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score1 / Quest12);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ12 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11 + GuardQ12));
                }
            });

            Resposta2.setText("2. Pouca folha e madeira, detritos orgânicos finos, floculentos, sem sedimento");
            Resposta2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score2 / Quest12);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ12 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11 + GuardQ12));
                }
            });

            Resposta3.setText("3. Principalmente folhas e material lenhoso com sedimento");
            Resposta3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));

                    ScoreGuard = (Score3 / Quest12);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ12 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11 + GuardQ12));
                }
            });

            Resposta4.setText("4. Principalmente folhas e material lenhoso sem sedimento");
            Resposta4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorVerde));

                    ScoreGuard = (Score4 / Quest12);
                    ScoreGuard = round(ScoreGuard);
                    Score.setText("Score: " + ScoreGuard);
                    GuardQ12 = ScoreGuard;
                    ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                            + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11 + GuardQ12));
                }
            });

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Prox += 1;
                    IniciarP();

                    Resposta0.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta1.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta2.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta3.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta4.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                    Resposta5.setTextColor(ContextCompat.getColor(Perguntas.this, R.color.colorAccent));
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void Verificacao() {
        if (Prox == 13) {

            Title.setText("Resultado");
            Score.setVisibility(View.GONE);
            ScoreFinal.setText("Score Final: " + (GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                    + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11 + GuardQ12) / QuestionMax);
            Resposta0.setVisibility(View.GONE);
            Resposta1.setVisibility(View.GONE);
            Resposta2.setVisibility(View.GONE);
            Resposta3.setVisibility(View.GONE);
            Resposta4.setVisibility(View.GONE);
            Resposta5.setVisibility(View.GONE);
            Proxima.setText("Salvar");

            Proxima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Prox == 13) {
                        VerificarUsers();
                        Intent intent = new Intent(Perguntas.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(Perguntas.this, "Salvo com sucesso!!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private void VerificarUsers()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            Bundle bundle = intent.getExtras();
            if (bundle != null)
            {
                usuario.setId(bundle.getLong("id"));
                usuario.setLocal(bundle.getString("local"));
                usuario.setLocalidade(bundle.getString("localidade"));
                usuario.setQ1(bundle.getFloat("q1"));
                usuario.setQ2(bundle.getFloat("q2"));
                usuario.setQ3(bundle.getFloat("q3"));
                usuario.setQ4(bundle.getFloat("q4"));
                usuario.setQ5(bundle.getFloat("q5"));
                usuario.setQ6(bundle.getFloat("q6"));
                usuario.setQ7(bundle.getFloat("q7"));
                usuario.setQ8(bundle.getFloat("q8"));
                usuario.setQ9(bundle.getFloat("q9"));
                usuario.setQ10(bundle.getFloat("q10"));
                usuario.setQ11(bundle.getFloat("q11"));
                usuario.setQ12(bundle.getFloat("q12"));
                usuario.setScoreFinal(bundle.getFloat("score"));
                usuario.setData(bundle.getString("data"));
                usuario.setLatitude(bundle.getFloat("latitude"));
                usuario.setLongitude(bundle.getFloat("longitude"));
                usuario.setCity(bundle.getString("city"));
                usuario.setState(bundle.getString("state"));
            }
        }

        SalvarUsuario();
    }

    private void SalvarUsuario() {
        Intent intent = getIntent();
        if (intent != null) {
            String local = intent.getStringExtra("local_get");
            String localidade = intent.getStringExtra("localidade_get");
            String data = intent.getStringExtra("data_get");

            usuario.setLocal(local);
            usuario.setLocalidade(localidade);
            GuardQ1 = round(GuardQ1);
            GuardQ2 = round(GuardQ2);
            GuardQ3 = round(GuardQ3);
            GuardQ4 = round(GuardQ4);
            GuardQ5 = round(GuardQ5);
            GuardQ6 = round(GuardQ6);
            GuardQ7 = round(GuardQ7);
            GuardQ8 = round(GuardQ8);
            GuardQ9 = round(GuardQ9);
            GuardQ10 = round(GuardQ10);
            GuardQ11 = round(GuardQ11);
            GuardQ12 = round(GuardQ12);
            usuario.setQ1(GuardQ1);
            usuario.setQ2(GuardQ2);
            usuario.setQ3(GuardQ3);
            usuario.setQ4(GuardQ4);
            usuario.setQ5(GuardQ5);
            usuario.setQ6(GuardQ6);
            usuario.setQ7(GuardQ7);
            usuario.setQ8(GuardQ8);
            usuario.setQ9(GuardQ9);
            usuario.setQ10(GuardQ10);
            usuario.setQ11(GuardQ11);
            usuario.setQ12(GuardQ12);
            usuario.setScoreFinal((GuardQ1 + GuardQ2 + GuardQ3 + GuardQ4 + GuardQ5 + GuardQ6 + GuardQ7
                    + GuardQ8 + GuardQ9 + GuardQ10 + GuardQ11 + GuardQ12) / QuestionMax);
            usuario.setData(data);
            usuario.setLatitude(latitude);
            usuario.setLongitude(longitude);
            usuario.setCity(city);
            usuario.setState(state);

            BD bd = new BD(this);
            bd.inserir(usuario);
        }
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

    public static float round(float d)
    {
        return BigDecimal.valueOf(d).setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();
    }
}
