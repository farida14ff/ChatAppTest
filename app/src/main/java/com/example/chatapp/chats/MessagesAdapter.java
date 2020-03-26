package com.example.chatapp.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.interfaces.OnItemClickListener;
import com.example.chatapp.models.Messages;
import com.example.chatapp.models.Users;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHoler> {

    private List<Messages> list;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;


    public MessagesAdapter(Context context, List<Messages> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  inflater.inflate(R.layout.list_users,parent,false);
        return new ViewHoler(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        holder.build(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHoler extends RecyclerView.ViewHolder{

        private TextView textView;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
            textView = itemView.findViewById(R.id.textView);

        }

        public void build(Messages messages) {
            textView.setText(messages.getText());
        }
    }
}
