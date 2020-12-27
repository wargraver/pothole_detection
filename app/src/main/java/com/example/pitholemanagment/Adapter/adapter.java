package com.example.pitholemanagment.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pitholemanagment.Model.Report;
import com.example.pitholemanagment.R;

import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder>
{
    Context context;
    List<Report> list;
    public adapter(Context context, List<Report> list)
    {
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
       View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.repot_row,null);
       return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.ViewHolder holder, int position)
    {
     int i=position;
     String p_status=list.get(i).getStatus();
     holder.id.setText("Id-:"+list.get(position).getId());
     holder.status.setText(list.get(i).getStatus());
     holder.place.setText(list.get(i).getPlace().toUpperCase());
     holder.cordi.setText("Co-ordi-:"+list.get(i).getCordinates());
     if(p_status.equals("SOLVED"))
     {
         //holder.status.setBackground(context.getResources().getDrawable(R.drawable.green_btn));
         holder.status.setTextColor(context.getResources().getColor(R.color.green));
         holder.place.setTextColor(context.getResources().getColor(R.color.green));
         holder.id.setTextColor(context.getResources().getColor(R.color.black));
         holder.cordi.setTextColor(context.getResources().getColor(R.color.black));
         holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_green));
         holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.done));

     }
     else
     {
         holder.status.setTextColor(context.getResources().getColor(R.color.red));
         holder.place.setTextColor(context.getResources().getColor(R.color.red));
         holder.id.setTextColor(context.getResources().getColor(R.color.black));
         holder.cordi.setTextColor(context.getResources().getColor(R.color.black));
         holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.light_red));
         holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.wait1));
     }

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView id,place,status,cordi;
        CardView cardView;
        ImageView imageView;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            place=(TextView) itemView.findViewById(R.id.id);
            cordi=(TextView) itemView.findViewById(R.id.place);
            id=(TextView) itemView.findViewById(R.id.coordinate);
            status=(TextView) itemView.findViewById(R.id.status);
            cardView=(CardView) itemView.findViewById(R.id.cardview);
            imageView=(ImageView) itemView.findViewById(R.id.imageView3);
        }

    }
}
