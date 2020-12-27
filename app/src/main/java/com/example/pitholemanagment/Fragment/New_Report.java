package com.example.pitholemanagment.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.pitholemanagment.Model.Report_Pothole_Model;
import com.example.pitholemanagment.Model.Response;
import com.example.pitholemanagment.Network.Jsonapi;
import com.example.pitholemanagment.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class New_Report extends Fragment
{
    Button button,button2;
    TextView textView;
    ImageView imageView;
    FirebaseVisionImageLabeler labeler;
    private  final int REQUEST_LOCATION = 1;
    String latitude, longitude;
    LocationManager locationManager;
    double lati=-1,longi=-1;
    Location locationGPS;
    Retrofit retrofit;
    Jsonapi jsonapi;
    int flagl=0;
    int flagp=0;
    int flag=0;
    TextView resulttv;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    String loc="";
    Float poth=0.0f;
    Float norm=0.0f;
    static  String myprefs="MYPREFFILE";
    public New_Report()
    {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("manifest.json")
                .build();
        View view=inflater.inflate(R.layout.fragment_new__report, container, false);
        button=(Button) view.findViewById(R.id.button);
        button2=(Button) view.findViewById(R.id.button2);
        textView=(TextView) view.findViewById(R.id.Result_tv);
        imageView=(ImageView) view.findViewById(R.id.imageView);
        textView=(TextView) view.findViewById(R.id.Result_tv);
        progressBar=(ProgressBar) view.findViewById(R.id.progress);
        imageView.setImageDrawable(getActivity().getDrawable(R.drawable.no_imag));
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        retrofit=new Retrofit.Builder()
                .baseUrl("https://bug-slayerss.herokuapp.com/check-user/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonapi=retrofit.create(Jsonapi.class);
        ActivityCompat.requestPermissions( getActivity(),
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        flagl=0;
        flagp=0;
        //getLocation();
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
            Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            getLocation();
            progressBar.setVisibility(View.VISIBLE);
            startActivityForResult(i,300);
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                 if(flag==0)
                 {
                  Toast.makeText(getContext(),"PLEASE CLICK A PICTURE",Toast.LENGTH_LONG).show();
                 }
                 else if(flagl==0)
                 {
                     Toast.makeText(getContext(),"ALLOW LOCATION ACCESS",Toast.LENGTH_LONG).show();
                 }
                 else if(flagp==0)
                 {
                     Toast.makeText(getContext(),"SORRY NO POTHOLE WAS DETECTED",Toast.LENGTH_LONG).show();
                 }
                 else
                 {
                     progressBar.setVisibility(View.VISIBLE);
                     sharedPreferences=getActivity().getSharedPreferences(myprefs,MODE_PRIVATE);
                     String token=sharedPreferences.getString("token","def");
                     Report_Pothole_Model m=new Report_Pothole_Model(token,loc,latitude+" "+longitude);
                     Call<Response> call=jsonapi.reportpothole(m);
                     call.enqueue(new Callback<Response>() {
                         @Override
                         public void onResponse(Call<Response> call, retrofit2.Response<Response> response)
                         {
                                     Response response1=response.body();
                                     if(response1!=null)
                                     {
                                         if(response1.getError().equals("null"))
                                         Toast.makeText(getContext(), "REPORTED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                                         else
                                             Toast.makeText(getContext(), response1.getError()+"", Toast.LENGTH_LONG).show();
                                     }
                                     else
                                     {
                                         Toast.makeText(getContext(), "NULL BODY", Toast.LENGTH_LONG).show();
                                     }

                         }
                         @Override
                         public void onFailure(Call<Response> call, Throwable t)
                         {
                            Toast.makeText(getContext(),"FAILED",Toast.LENGTH_LONG).show();
                         }
                     });
                     progressBar.setVisibility(View.INVISIBLE);
                 }

            }
        });

        try {
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options =
                    new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder(localModel)
                            .setConfidenceThreshold(0.0f)
                            .build();
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
        } catch (FirebaseMLException e) {

        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==300)
        {

            Bitmap bitmap=(Bitmap) data.getExtras().get("data");
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
            imageView.setImageBitmap(bitmap);
            flag=1;
            latitude=String.valueOf(lati);
            longitude=String.valueOf(longi);
            image = FirebaseVisionImage.fromBitmap(bitmap);
            labeler.processImage(image)
                    .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                        @Override
                        public void onSuccess(List<FirebaseVisionImageLabel> labels)
                        {
                            //String str="";
                            for (FirebaseVisionImageLabel label: labels)
                            {
                                String text = label.getText();
                                float confidence = label.getConfidence();
                                if(text.equals("normal"))
                                    norm=confidence;
                                else
                                    poth=confidence;
                            }
                            if(lati==-1&&longi==-1)
                            {
                                Toast.makeText(getContext(),"LOCATION NOT DETECTED",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault()); //it is Geocoder
                                try {
                                    List<Address> address = geoCoder.getFromLocation(locationGPS.getLatitude(), locationGPS.getLongitude(), 1);
                                    //Toast.makeText(getContext(),address.get(0).getSubLocality().length(),Toast.LENGTH_LONG).show();

                                         loc=address.get(0).getLocality()+" "+address.get(0).getCountryName()+" ("+address.get(0).getPostalCode()+")";
                                   // String fnialAddress = builder.toString(); //This is the complete address.

                                } catch (IOException e) {}
                                catch (NullPointerException e) {}
                            }
                            String str="";
                            if(lati==-1&&longi==-1)
                            {
                                str = str + "LOCATION:-NOT DETECTED\n";
                                flagl=0;
                            }
                            else
                                {
                                str = str + "LOCATION:-"+loc+"\n";
                                flagl=1;

                            }
                            if((((poth*100)>75&&norm*100<25)||(poth*100>90))&&(norm*100<75)) {
                                str = str + "POTHOLE:-DETECTED";
                                flagp=1;
                            }
                            else {
                                str = str + "POTHOLE:-NOT DETECTED";
                                flagp=0;
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.VISIBLE);
                            textView.setText(str);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            // Task failed with an exceptiod
                        }
                    });
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void OnGPS()
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation()
    {
        if (ActivityCompat.checkSelfPermission(
                getActivity().getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            OnGPS();
        } else
            {
            locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null)
            {
                 lati = locationGPS.getLatitude();
                 longi = locationGPS.getLongitude();

            } else
                {
                Toast.makeText(getActivity(), "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}