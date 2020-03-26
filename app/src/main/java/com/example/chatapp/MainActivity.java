package com.example.chatapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.chatapp.chats.ChatActivity;
import com.example.chatapp.contacts.ContactsActivity;
import com.example.chatapp.contacts.ContactsAdapter;
import com.example.chatapp.interfaces.OnItemClickListener;
import com.example.chatapp.models.Chat;
import com.example.chatapp.models.Messages;
import com.example.chatapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter adapter;
    private List<Chat> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(this,PhoneActivity.class));
        }
        recyclerView = findViewById(R.id.recyler_view_my_chats);
        intiList();
        getChats();
    }

    private void getChats() {
        String myUserId = FirebaseAuth.getInstance().getUid();
        FirebaseFirestore.getInstance().collection("chats")
                .whereArrayContains("userId", myUserId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange change : snapshots.getDocumentChanges()){
                            switch (change.getType()){
                                case ADDED:
                                    Chat chat = change.getDocument().toObject(Chat.class);
                                    chat.setId(change.getDocument().getId());
                                    list.add(chat);
                                    break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void intiList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration( new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new ChatAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener( new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("chat",list.get(position));
                startActivity(intent);

            }
        });
    }

    public void onClickContacts(View view) {
        startActivity(new Intent(this, ContactsActivity.class));
    }
}
