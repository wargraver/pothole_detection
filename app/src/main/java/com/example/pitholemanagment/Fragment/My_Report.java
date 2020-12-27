package com.example.pitholemanagment.Fragment;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pitholemanagment.Adapter.adapter;
import com.example.pitholemanagment.Model.PendingRequest;
import com.example.pitholemanagment.Model.Report;
import com.example.pitholemanagment.Model.Response;
import com.example.pitholemanagment.Model.SolvedRequest;
import com.example.pitholemanagment.Model.Token;
import com.example.pitholemanagment.Model.user;
import com.example.pitholemanagment.Network.Jsonapi;
import com.example.pitholemanagment.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.content.Context.MODE_PRIVATE;
public class My_Report extends Fragment
{

     SwipeRefreshLayout swipeRefreshLayout;
     RecyclerView recyclerView;
    public static List<Report> list;
    Retrofit retrofit;
    Jsonapi jsonapi;
    View view;
    SharedPreferences sharedPreferences;
    static  String myprefs="MYPREFFILE";
    TextView total,resolved,pending;
    ProgressBar progressBar;
    int r,p;
    LinearLayout linearLayout;
    public My_Report() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
         view=inflater.inflate(R.layout.fragment_my__report, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recycler);
        swipeRefreshLayout=(SwipeRefreshLayout)  view.findViewById(R.id.swipeview);
        recyclerView.setHasFixedSize(false);
        list=new ArrayList<Report>();
        total=(TextView) view.findViewById(R.id.total);
        resolved=(TextView) view.findViewById(R.id.resolved);
        pending=(TextView) view.findViewById(R.id.pending);
        progressBar=(ProgressBar) view.findViewById(R.id.progress_main);
        linearLayout=(LinearLayout) view.findViewById(R.id.linearl);
        linearLayout.setVisibility(View.INVISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        retrofit=new Retrofit.Builder()
                .baseUrl("https://bug-slayerss.herokuapp.com/check-user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        r=0;
        p=0;
        jsonapi=retrofit.create(Jsonapi.class);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        doYourUpdate();
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh()
                    {
                        swipeRefreshLayout.setRefreshing(true);
                        doYourUpdate();
                    }
                }
        );
        return view;
    }
    private void doYourUpdate()
    {
        //Take token from shared prefrences
        sharedPreferences=getActivity().getSharedPreferences(myprefs,MODE_PRIVATE);
        String token=sharedPreferences.getString("token","def");
        //String token="eyJhbGciOiJIUzI1NiJ9.dXRrYXJzaGtyc2luZ2hld0B4eXouY29t.JZaa-KgLFcUsmHKrAHBbycmH2zXCLZbNJjBl3ZocbkI";
        Token tk=new Token(token);
        list.clear();
        Call<Response> responseCALL=jsonapi.getreports(tk);
        responseCALL.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful())
                {
                    Response response1=response.body();
                    if(response1!=null)
                    {

                      List<PendingRequest> req=response1.getPendingRequest();
                      p=req.size();
                      for(PendingRequest pq:req)
                      {
                          String cordi=pq.getCoordinates();
                          String lati=cordi.substring(0,cordi.indexOf(" "));
                          String longi=cordi.substring((cordi.indexOf(" ")+1));
                          double lat=Double.valueOf(lati);
                          double lon=Double.valueOf(longi);
                          Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
                          String add="";
                          try {
                              List<Address> address = geoCoder.getFromLocation(lat,lon,1);
                              if((address!=null)&&address.size()>0)
                              {
                                  add=add+address.get(0).getLocality()+" "+address.get(0).getCountryName();
                              }
                          } catch (IOException e) {
                              e.printStackTrace();
                          }
                          add=add+" "+pq.getAddress();
                          list.add(new Report(pq.getId(),"PENDING",add,pq.getCoordinates()));
                          add="";
                          //Toast.makeText(getContext(),pq.getCoordinates(),Toast.LENGTH_LONG).show();
                      }
                      List<SolvedRequest> solvedRequests=response1.getSolvedRequest();
                      r=solvedRequests.size();
                      for(SolvedRequest solvedRequest:solvedRequests)
                      {
                          list.add(new Report(solvedRequest.getId(),"SOLVED",solvedRequest.getAddress(),solvedRequest.getCoordinates()));
                      }
                         if(list.size()==0)
                         {
                          // SET TextView NO DATA FOUND LOGO
                         }
                         else
                             {
                             adapter adapter = new adapter(view.getContext(), list);
                             recyclerView.setAdapter(adapter);

                         }
                         total.setText((p+r)+"");
                         pending.setText(p+"");
                         resolved.setText(r+"");
                        linearLayout.setVisibility(View.VISIBLE);
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(ProgressBar.GONE);
                    }
                    else
                    {
                        Toast.makeText(getContext(),"NULL RESPONSE",Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(ProgressBar.GONE);
                    }

                }
                else
                {
                    Toast.makeText(getContext(),"NO RESPONSE",Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
            @Override
            public void onFailure(Call<Response> call, Throwable t)
            {
                Toast.makeText(getContext(),t.getMessage()+" RESPOSE FAILED",Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }
}