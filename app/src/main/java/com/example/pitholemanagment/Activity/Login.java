package com.example.pitholemanagment.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.example.pitholemanagment.Model.Response;
import com.example.pitholemanagment.Model.user;
import com.example.pitholemanagment.Network.Jsonapi;
import androidx.appcompat.app.AppCompatActivity;

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
public class Login extends AppCompatActivity
{
    EditText eemail,epass;
    Button login_btn;
    Retrofit retrofit;
    Jsonapi jsonapi;
    TextView signup;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    TextView forgetpass;
    static  String myprefs="MYPREFFILE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        eemail=(EditText) findViewById(R.id.input_email);
        epass=(EditText) findViewById(R.id.input_password);
        login_btn=(Button) findViewById(R.id.btn_login);
        signup=(TextView) findViewById(R.id.link_signUp);
        progressBar=(ProgressBar) findViewById(R.id.progress);
        forgetpass=(TextView) findViewById(R.id.forgotPassword);
        sharedPreferences=getSharedPreferences(myprefs,MODE_PRIVATE);
       if(!sharedPreferences.getString("token","default").equals("default"))
        {
           Intent i = new Intent(getApplicationContext(), Main.class);
           startActivity(i);
           finish();
        }
        retrofit=new Retrofit.Builder()
                .baseUrl("https://bug-slayerss.herokuapp.com/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonapi=retrofit.create(Jsonapi.class);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ForgetPass.class);
                startActivity(i);
            }
        });
    }
    private void login()
    {
        String email=eemail.getText().toString().trim();
        String pass=epass.getText().toString().trim();
        if(email.length()==0)
        {
            eemail.setError("Enter name");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher((CharSequence) email).matches())
        {
            eemail.setError("Enter valid email");
        }
        else if(pass.length()==0)
        {
            epass.setError("Enter password");
        }
        else
        {
            user user=new user(pass,email);
            progressBar.setVisibility(ProgressBar.VISIBLE);
            Call<Response> responseCall=jsonapi.login(user);
            responseCall.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                {
                    if(response!=null)
                    {
                        Response response1 = response.body();
                        if (response1 != null)
                        {

                            if(!response1.getToken().equals("null"))
                            {
                                String Token=response1.getToken();
                                Intent i = new Intent(getApplicationContext(), Main.class);
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                                SharedPreferences.Editor editor=sharedPreferences.edit();
                                editor.putString("token",Token);
                                editor.commit();
                                startActivity(i);
                                finish();
                            }
                            else
                                {
                                Toast.makeText(getApplication(), response1.getError() + "", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            }


                        } else
                            {
                                progressBar.setVisibility(ProgressBar.INVISIBLE);
                            Toast.makeText(getApplication(), "NULL RESPONSE", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(getApplication(), "NULL RESPONSE", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(ProgressBar.INVISIBLE);
                    }

                }
                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(getApplication(),t.getMessage()+"",Toast.LENGTH_LONG).show();
                }
            });

        }
    }

}
