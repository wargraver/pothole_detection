package com.example.pitholemanagment.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pitholemanagment.Model.Response;
import com.example.pitholemanagment.Model.user;
import com.example.pitholemanagment.Network.Jsonapi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pitholemanagment.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForgetPass extends AppCompatActivity
{
   EditText forgetemail;
   Button reset;
   ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        forgetemail=(EditText) findViewById(R.id.email_forgot);
        reset=(Button) findViewById(R.id.btn_reset);
        progressBar=(ProgressBar) findViewById(R.id.progressf);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=forgetemail.getText().toString().trim();
                 if(email.length()==0)
                    forgetemail.setError("Enter email");
                else if(!Patterns.EMAIL_ADDRESS.matcher((CharSequence) email).matches())
                {
                    forgetemail.setError("Enter valid email");
                }
                else
                 {
                     progressBar.setVisibility(ProgressBar.VISIBLE);
                     Retrofit retrofit = new Retrofit.Builder()
                             .baseUrl("https://bug-slayerss.herokuapp.com/user/")
                             .addConverterFactory(GsonConverterFactory.create())
                             .build();
                     Jsonapi jsonapi=retrofit.create(Jsonapi.class);
                     Call<Response> call=jsonapi.resetpass(new user(email));
                     call.enqueue(new Callback<Response>() {
                         @Override
                         public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                         {
                         Response response1=response.body();
                         if(response1!=null)
                         {
                             if(!response1.getError().equals("null"))
                             {
                                 Toast.makeText(getApplicationContext(),response1.getError()+"",Toast.LENGTH_LONG).show();
                                 progressBar.setVisibility(ProgressBar.GONE);
                             }
                             else
                             {
                                 Toast.makeText(getApplicationContext(),"RESET LINK SEND",Toast.LENGTH_LONG).show();
                                 Intent i=new Intent(getApplicationContext(),Login.class);
                                 startActivity(i);
                                 progressBar.setVisibility(ProgressBar.GONE);
                                 finish();
                             }
                         }
                         else
                             Toast.makeText(getApplicationContext(),"NULL BODY",Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(ProgressBar.GONE);
                         }
                         @Override
                         public void onFailure(Call<Response> call, Throwable t)
                         {
                             Toast.makeText(getApplicationContext(),t.getMessage()+"",Toast.LENGTH_LONG).show();
                             progressBar.setVisibility(ProgressBar.GONE);
                         }
                     });

                 }
            }
        });

    }
}