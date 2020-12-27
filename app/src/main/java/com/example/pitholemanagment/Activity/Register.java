package com.example.pitholemanagment.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.pitholemanagment.Model.Response;
import com.example.pitholemanagment.Model.user;
import com.example.pitholemanagment.Network.Jsonapi;
import com.example.pitholemanagment.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {


    EditText ename,eemail,epass,econfirmpass,e_no;
    Button register_btn;
    Retrofit retrofit;
    Jsonapi jsonapi;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ename=(EditText) findViewById(R.id.input_namer);
        eemail=(EditText) findViewById(R.id.input_emailr);
        epass=(EditText) findViewById(R.id.input_passwordr);
        e_no=(EditText) findViewById(R.id.input_phn);
        econfirmpass=(EditText) findViewById(R.id.input_cpasswordr);
        register_btn=(Button) findViewById(R.id.btn_register);
        progressBar=(ProgressBar) findViewById(R.id.progress);
        retrofit=new Retrofit.Builder()
                .baseUrl("https://bug-slayerss.herokuapp.com/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         jsonapi=retrofit.create(Jsonapi.class);
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
            register();
            }
        });
    }

    private void register()
    {
        String name,email,pass,cpass,phn;
        name=ename.getText().toString().trim();
        email=eemail.getText().toString().trim();
        pass=epass.getText().toString().trim();
        cpass=econfirmpass.getText().toString().trim();
        phn=e_no.getText().toString().trim();
        if(name.length()==0)
            ename.setError("Enter  name");
        else if(email.length()==0)
            eemail.setError("Enter email");
        else if(!Patterns.EMAIL_ADDRESS.matcher((CharSequence) email).matches())
        {
            eemail.setError("Enter valid email");
        }
        else if(phn.length()==0)
        {
            e_no.setError("Enter number");
        }
        else if(phn.length()!=10)
        {
            e_no.setError("Enter valid number");
        }
       else if(pass.length()==0)
            epass.setError("Enter password");
       else if(pass.length()<6)
            epass.setError("Password should be of atleast 6 characters");
       else if(cpass.length()==0)
            econfirmpass.setError("Confirm password");
       else if(!pass.equals(cpass))
        {
            Toast.makeText(getApplicationContext(),"Password did not match",Toast.LENGTH_LONG).show();
        }
       else
        {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            user user=new user(name,pass,email,phn);
            Call<Response> responseCall=jsonapi.register(user);
            responseCall.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Response response1=response.body();
                    if(response1!=null)
                    {
                        if (response1 != null)
                        {
                            if(response1.getError().equals("null"))
                            {
                                Toast.makeText(getApplication(), "USER REGISTERED", Toast.LENGTH_LONG).show();
                                Intent i=new Intent(getApplicationContext(),Login.class);
                                startActivity(i);
                                finish();
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            }
                            else {
                                Toast.makeText(getApplication(), response1.getError() + "", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            }
                        }
                        else
                            {
                            Toast.makeText(getApplication(), "NULL RESPONSE Body", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(ProgressBar.INVISIBLE);
                        }
                    }
                    else {
                        Toast.makeText(getApplication(), "NULL RESPONSE", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t)
                {
                    Toast.makeText(getApplication(),t.getMessage()+"",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            });
        }
    }
}