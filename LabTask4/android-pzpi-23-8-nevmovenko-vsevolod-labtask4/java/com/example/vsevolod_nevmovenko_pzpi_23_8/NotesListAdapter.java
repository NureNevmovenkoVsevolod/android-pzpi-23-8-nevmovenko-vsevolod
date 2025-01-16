package com.example.notes.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.notes.Models.Notes;
import com.example.notes.NotesClickListener;
import com.example.notes.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesListAdapter extends RecyclerView.Adapter <NotesViewHolder>{
    Context context;
    List<Notes> list;
    NotesClickListener listener;
    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        Log.d("NotesListAdapter", "Adapter initialized with " + this.list.size() + " notes.");
    }
    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);
        holder.textView_notes.setText(list.get(position).getNotes());
        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);
        String imageUrl = list.get(position).getImageUrl();
        Log.d("NotesListAdapter", "Binding note at position " + position + " with image URL: " + imageUrl);
        if (!imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.imageView_note);
            holder.imageView_note.setVisibility(View.VISIBLE); 
        } else {
            holder.imageView_note.setVisibility(View.GONE);
        }
        if(list.get(position).isPinned()){
            holder.imageView_pin.setImageResource(R.drawable.pin_icon);
        } else{
            holder.imageView_pin.setImageResource(0);
        }
     
        String priority = list.get(position).getPriority();
        int colorCode;
        switch (priority) {
            case "High":
                colorCode = R.color.color1;
                break;
            case "Medium":
                colorCode = R.color.color2;
                break;
            case "Low":
                colorCode = R.color.color3;
                break;
            default:
                colorCode = R.color.white; 
                break;
        }
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));
        Log.d("NotesListAdapter", "Set background color for note at position " + position + " to " + colorCode);
        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });
        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });

    }

    private int getRandomColor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.color1);
        colorCode.add(R.color.color2);
        colorCode.add(R.color.color3);

        Random random = new Random();
        int randomColor = random.nextInt(colorCode.size());
        return colorCode.get(randomColor);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList (List<Notes> filteredList) {
        list = filteredList;
        notifyDataSetChanged();
        Log.d("NotesListAdapter", "Filtered list updated. New size: " + list.size());
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notes_container;
    TextView textView_title;
    TextView textView_notes;
    TextView textView_date;
    ImageView imageView_pin;
    ImageView imageView_note;


    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_container = itemView.findViewById(R.id.notes_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_date = itemView.findViewById(R.id.textView_date);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
        imageView_note = itemView.findViewById(R.id.imageView_note);
    }
}
