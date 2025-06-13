package com.example.fotannouncer;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.cardholder> {
    Context context;
    ArrayList<card> list;

    public adapter(Context context, ArrayList<card> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public cardholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.newscard, parent, false);
        return new cardholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull cardholder holder, int position) {
        card newsItem = list.get(position); // Correctly get the card object

        holder.title.setText(newsItem.getTitle());
        holder.content.setText(newsItem.getContent());

        // Load image using Glide and handle Firebase Storage gs:// URL
        if (newsItem.getImageUrl() != null && !newsItem.getImageUrl().isEmpty()) {
            String gsUrl = newsItem.getImageUrl();

            // Check if it's a gs:// URL and convert it
            if (gsUrl.startsWith("gs://")) {
                try {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(gsUrl);
                    storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context)
                                    .load(uri.toString()) // Load the actual download URL
                                    .placeholder(R.drawable.ic_launcher_background) // Placeholder image (replace with your own)
                                    .error(R.drawable.ic_launcher_foreground) // Error image (replace with your own)
                                    .into(holder.imageU);
                        }
                    }).addOnFailureListener(exception -> {
                        // Handle any errors getting the download URL
                        Log.e("FirebaseStorage", "Failed to get download URL: " + exception.getMessage());
                        holder.imageU.setImageResource(R.drawable.ic_launcher_foreground); // Show an error image
                    });
                } catch (IllegalArgumentException e) {
                    Log.e("FirebaseStorage", "Invalid gs:// URL: " + gsUrl, e);
                    holder.imageU.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else {
                // If it's already an http(s):// URL, load directly
                Glide.with(context)
                        .load(gsUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground)
                        .into(holder.imageU);
            }
        } else {
            holder.imageU.setImageDrawable(null); // Clear image if no URL
            // Or set a default placeholder
            // holder.imageU.setImageResource(R.drawable.default_news_image);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class cardholder extends RecyclerView.ViewHolder {
        TextView title, content;
        ImageView imageU; // Corrected ImageView declaration

        public cardholder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            imageU = itemView.findViewById(R.id.imageu);
        }
    }
}