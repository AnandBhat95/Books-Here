package com.example.bookshere;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
        implements Serializable {

    Context context;
    List<ImageUploadInfo> MainImageUploadInfoList;

    String var;

    public RecyclerViewAdapter(Context context, List<ImageUploadInfo> TempList) {

        this.MainImageUploadInfoList = TempList;

        this.context = context;

    }

    public void filterList(ArrayList<ImageUploadInfo> filterllist) {
        MainImageUploadInfoList = filterllist;
        notifyDataSetChanged();
    }
    public void filterLis(ArrayList<ImageUploadInfo> filterllist) {
        MainImageUploadInfoList = filterllist;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_items, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,  int position) {
        ImageUploadInfo UploadInfo = MainImageUploadInfoList.get(position);
        System.out.println("Image data"+UploadInfo);
        int k = position;

        holder.imageNameTextView.setText(UploadInfo.getImageName());
        Picasso.with(context)
                .load(UploadInfo.getImageURL())
                .resize(350, 300)
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String newArray[] = new String[55];
                newArray[0]=UploadInfo.imageName;
                newArray[1]=UploadInfo.imageURL;
                newArray[2]=UploadInfo.c_name;
                newArray[3]=UploadInfo.c_year;
                newArray[4]=UploadInfo.buk_year;
                newArray[5]=UploadInfo.buk_price;
                newArray[6]=UploadInfo.u_user;
                newArray[7]=UploadInfo.keys;
                newArray[8]= UploadInfo.author;
                newArray[9]= UploadInfo.contact;


                Intent intent=new Intent(view.getContext(), BookInforamtion.class);
                intent.putExtra("Bo_na", newArray);
                view.getContext().startActivity(intent);
            }
        });//Glide.with(context).load(UploadInfo.getImageURL()).into(holder.imageView);


    }

    @Override
    public int getItemCount() {

        return MainImageUploadInfoList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView imageNameTextView;
        public TextView courseName;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);

            imageNameTextView = (TextView) itemView.findViewById(R.id.ImageNameTextView);

        }
    }
}

