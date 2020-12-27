package com.example.pitholemanagment.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.pitholemanagment.Fragment.About;
import com.example.pitholemanagment.Fragment.Account;
import com.example.pitholemanagment.Fragment.My_Report;
import com.example.pitholemanagment.Fragment.New_Report;
import com.example.pitholemanagment.Model.Response;
import com.example.pitholemanagment.Model.Token;
import com.example.pitholemanagment.Network.Jsonapi;
import com.example.pitholemanagment.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main extends AppCompatActivity
{
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    TextView title;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    static  String myprefs="MYPREFFILE";
    Retrofit retrofit;
    Jsonapi jsonapi;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.bottom_nav);
        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("MY REPORTS");
        //progressBar=(ProgressBar) findViewById(R.id.progress_main);
        //progressBar.setVisibility(ProgressBar.VISIBLE);
        sharedPreferences=getSharedPreferences(myprefs,MODE_PRIVATE);
        progressDialog=new ProgressDialog(Main.this);
        retrofit=new Retrofit.Builder()
                .baseUrl("https://bug-slayerss.herokuapp.com/user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonapi=retrofit.create(Jsonapi.class);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new My_Report(),null)
                .addToBackStack(null)
                .commit();

        /*
        Implement onbackpressed so if user by mistakes press back he is not taken out!

         */
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                int id=item.getItemId();
                Fragment fragment=null;
                switch (id)
                {
                    case R.id.my_reports:
                        title.setText("MY REPORTS");
                        fragment=new My_Report();
                        break;
                    case R.id.new_report:
                        title.setText("NEW REPORTS");
                        fragment=new New_Report();
                        break;
                    case R.id.about:
                        title.setText("ABOUT");
                        fragment=new About();
                        break;
                    case R.id.account:
                        title.setText("ACCOUNT");
                        fragment=new Account();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + id);
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,fragment,null)
                        .commit();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.Logout:
               // progressBar.setVisibility(ProgressBar.VISIBLE);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("LOGING OUT..."); // Setting Message
                progressDialog.setTitle(""); // Setting Title
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                //progressDialog.show(); // Display Progress Dialog
                progressDialog.setCancelable(false);
                progressDialog.show();
                String tk=sharedPreferences.getString("token","m");
                Token token=new Token(tk);

                SharedPreferences.Editor editor=sharedPreferences.edit();
                Call<Response> responseCall=jsonapi.logout(token);
                responseCall.enqueue(new Callback<Response>() {
                    @Override
                    public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                    {

                        editor.putString("token","default");
                        editor.commit();
                        Intent i=new Intent(getApplicationContext(),Login.class);
                        startActivity(i);
                        progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(),""+sharedPreferences.getString("to","def"),Toast.LENGTH_LONG).show();
                        finish();
                        // progressBar.setVisibility(ProgressBar.INVISIBLE);

                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t)
                    {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Logout FAILED "+t.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });


                //Toast.makeText(getApplicationContext(),sharedPreferences.getString("token","he"),Toast.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}