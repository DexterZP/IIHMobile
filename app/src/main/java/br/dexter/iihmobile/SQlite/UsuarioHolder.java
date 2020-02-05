package br.dexter.iihmobile.SQlite;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import br.dexter.iihmobile.R;

public class UsuarioHolder extends RecyclerView.ViewHolder
{
    public TextView local, localidade, latitude, longitude, data, q1, q2, q3, q4, q5, q6, q7, q8, q9, q10, q11, q12, scoreFinal, SetImagens;
    public Button SendExport;
    public LinearLayout ContainerImagens;
    public ImageView SetImage1, SetImage2, SetImage3, SetImage4;

    public UsuarioHolder(View view)
    {
        super(view);

        local = view.findViewById(R.id.SetLocal);
        localidade = view.findViewById(R.id.SetLocalidade);
        latitude = view.findViewById(R.id.SetLatitude);
        longitude = view.findViewById(R.id.SetLongitude);
        data = view.findViewById(R.id.SetData);
        q1 = view.findViewById(R.id.SetQ1);
        q2 = view.findViewById(R.id.SetQ2);
        q3 = view.findViewById(R.id.SetQ3);
        q4 = view.findViewById(R.id.SetQ4);
        q5 = view.findViewById(R.id.SetQ5);
        q6 = view.findViewById(R.id.SetQ6);
        q7 = view.findViewById(R.id.SetQ7);
        q8 = view.findViewById(R.id.SetQ8);
        q9 = view.findViewById(R.id.SetQ9);
        q10 = view.findViewById(R.id.SetQ10);
        q11 = view.findViewById(R.id.SetQ11);
        q12 = view.findViewById(R.id.SetQ12);
        scoreFinal = view.findViewById(R.id.SetScoreFinal);

        ContainerImagens = view.findViewById(R.id.ContainerImagens);
        SetImage1 = view.findViewById(R.id.SetImage1);
        SetImage2 = view.findViewById(R.id.SetImage2);
        SetImage3 = view.findViewById(R.id.SetImage3);
        SetImage4 = view.findViewById(R.id.SetImage4);
        SetImagens = view.findViewById(R.id.SetImagens);

        SendExport = view.findViewById(R.id.SetExportar);
    }
}
