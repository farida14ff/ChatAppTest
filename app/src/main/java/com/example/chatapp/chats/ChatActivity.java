package com.example.chatapp.chats;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.EditText;

import com.example.chatapp.R;
import com.example.chatapp.contacts.ContactsActivity;
import com.example.chatapp.contacts.ContactsAdapter;
import com.example.chatapp.interfaces.OnItemClickListener;
import com.example.chatapp.models.Chat;
import com.example.chatapp.models.Messages;
import com.example.chatapp.models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private List<Messages> list = new ArrayList<>();

    private EditText editText;
    private Users users;
    private Chat chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.recyler_view_chat);
        editText = findViewById(R.id.edit_text);
        users  = (Users) getIntent().getSerializableExtra("user");
        chat  = (Chat) getIntent().getSerializableExtra("chat");
        if (chat == null){
            chat = new Chat();
            ArrayList<String> userIds = new ArrayList<>();
            userIds.add(users.getId());
            userIds.add(FirebaseAuth.getInstance().getUid());
            chat.setUserId(userIds);

        }else {

            initList();
            getMessages();
        }
    }

    private void getMessages() {
        FirebaseFirestore.getInstance().collection("chats")
                .document(chat.getId())
                .collection("messages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentChange change : snapshots.getDocumentChanges()){
                            switch (change.getType()){
                                case ADDED:
                                    list.add(change.getDocument().toObject(Messages.class));
                                    break;
                                case REMOVED:
                                    break;
                                case MODIFIED:
                                    break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

    }


    public void initList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration( new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        adapter = new MessagesAdapter(this,list);
        recyclerView.setAdapter(adapter);
        
    }
    public void onClickSend(View view) {
        String text = editText.getText().toString().trim();
        if (chat.getId() != null) {
            sendMessage(text);
            editText.setText("");
        }
        else {
            createChat(text);
        }

    }

    private void createChat(final String text) {
        FirebaseFirestore.getInstance().collection("chats")
                .add(chat)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        chat.setId(documentReference.getId());
                        sendMessage(text);
                    }
                });

    }

    private void sendMessage(String text) {
        Map<String, Object> map = new HashMap<>();
        map.put("text",text);
        FirebaseFirestore.getInstance().collection("chats")
                .document(chat.getId())
                .collection("messages")
                .add(map);
    }
}
