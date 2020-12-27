package com.example.pitholemanagment.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pitholemanagment.Model.Response;
import com.example.pitholemanagment.Model.Token;
import com.example.pitholemanagment.Network.Jsonapi;
import com.example.pitholemanagment.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Account extends Fragment
{

    Retrofit retrofit;
    Jsonapi jsonapi;
    TextView fullnametv,name_tv,email_tv,phn_tv,total,resolves,pending;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    static  String myprefs="MYPREFFILE";
    LinearLayout linearLayout1,linearLayout2;
    RelativeLayout relativeLayout;
    ImageView imageView;
    ScrollView scrollView;
    public Account() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.account_fragment, container, false);
        retrofit=new Retrofit.Builder()
                .baseUrl("https://bug-slayerss.herokuapp.com/check-user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        scrollView=(ScrollView) v.findViewById(R.id.scrollview);
        imageView=(ImageView) v.findViewById(R.id.profileimg);
        jsonapi=retrofit.create(Jsonapi.class);
        progressBar=(ProgressBar) v.findViewById(R.id.progress_main);
        name_tv=(TextView) v.findViewById(R.id.name);
        email_tv=(TextView) v.findViewById(R.id.email);
        phn_tv=(TextView) v.findViewById(R.id.phnno);
        linearLayout2=(LinearLayout) v.findViewById(R.id.linear2);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        getuser();
        return v;
    }

    private void getuser()
    {
        sharedPreferences=getActivity().getSharedPreferences(myprefs,0);
        Token token=new Token(sharedPreferences.getString("token","tkn"));
        Call<Response> call=jsonapi.getuser(token);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
            {
                Response response1=response.body();
                //fullnametv.setText(response1.getName());
                if(response1!=null) {
                    name_tv.setText(response1.getName());
                    email_tv.setText(response1.getEmail());
                    phn_tv.setText(response1.getPhnNo());
                    int p = response1.getPendingRequest().size();
                    int r = response1.getSolvedRequest().size();
                    int t = p + r;
                    progressBar.setVisibility(ProgressBar.GONE);
                    imageView.setVisibility(View.VISIBLE);
                    linearLayout2.setVisibility(View.VISIBLE);
                    // relativeLayout.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(getContext(),"NULL RESPONSE",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {

            }
        });
    }
}