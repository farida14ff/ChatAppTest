package com.example.chatapp.contacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.chatapp.R;
import com.example.chatapp.chats.ChatActivity;
import com.example.chatapp.interfaces.OnItemClickListener;
import com.example.chatapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ContactsAdapter adapter;
    private List<Users> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        recyclerView = findViewById(R.id.recyler_view);
        initList();
        getContacts();
    }


    private void initList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration( new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new ContactsAdapter(this,list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(ContactsActivity.this, ChatActivity.class);
                intent.putExtra("user",list.get(position));
                startActivity(intent);

            }
        });
    }

    private void getContacts() {
        FirebaseFirestore.getInstance().collection("users")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshots) {
                        for (DocumentSnapshot snapshot : snapshots){
                            Users users = snapshot.toObject(Users.class);
                            if (users != null){
                                users.setId(snapshot.getId());
                            }
                            list.add(users);
                        }
                        adapter.notifyDataSetChanged();

                    }
                });
    }
}
