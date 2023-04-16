package com.aks.smpc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aks.smpc.R;
import com.aks.smpc.data.TableData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ListCapturedImagesAdapter extends RecyclerView.Adapter<ListCapturedImagesAdapter.ViewHolder> {
    List<TableData> tableDataList;
    Context context;

    public ListCapturedImagesAdapter(List<TableData> tableDataList,Context context) {
        this.tableDataList=tableDataList;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_captured_fish_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //bind view holder
        //holder.imvShowImage
        Glide
                .with(context)
                .load(tableDataList.get(position).getString1())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imvShowImage);
        holder.tvDateTime.setText(tableDataList.get(position).getString2());
    }

    @Override
    public int getItemCount() {
        return tableDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        TextView tvDateTime;
        ImageView imvShowImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName=itemView.findViewById(R.id.tvFileName);
            tvDateTime=itemView.findViewById(R.id.tvDateTime);
            imvShowImage=itemView.findViewById(R.id.imvShowImage);
        }
    }
}
