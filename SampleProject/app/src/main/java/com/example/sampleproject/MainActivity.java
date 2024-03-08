package com.example.sampleproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private MyRecyclerViewAdapter.RecyclerViewClickListener Listener;
    ArrayList<contact> contactList;
    MyRecyclerViewAdapter adapter;
    FirebaseFirestore db;
    FirebaseDatabase ref;
    CollectionReference ContactListCollection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton addContact=findViewById(R.id.add_fab);
        db=FirebaseFirestore.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().getDatabase();
        contactList=new ArrayList<contact>();
        ContactListCollection=db.collection("ContactsList");

        ref.getReference().child("contact").addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String name= (String) ds.child("name").getValue();
                            String Number= (String) ds.child("Number").getValue();
                            contactList.add(new contact(name, Number));

                        }
                        Collections.sort(contactList, new Comparator<contact>() {
                            @Override
                            public int compare(contact contact1, contact contact2) {
                                return contact1.getContactName().compareToIgnoreCase(contact2.getContactName());
                            }
                        });
                        //Log.d(TAG, "Contact List: " + Arrays.toString(contactList.toArray()));
                        //RecyclerViewCode
                        RecyclerView rv=findViewById(R.id.MyRecyclerView);
                        rv.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        setOnClickListener();
                        adapter=new MyRecyclerViewAdapter(contactList,Listener);
                        rv.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
        );






        //floatingButton adding new contact code starts here
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move=new Intent(MainActivity.this,ContactDetailsPage.class);
                startActivity(move);
            }
        });
        // adding new contact code ends here

        //searchView code starts here
        SearchView search=findViewById(R.id.searchView);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String text=newText;
                adapter.filter(text);
                return true;
            }
        });


    }
    //setOnClickListener Method
    public void setOnClickListener() {
        Listener = new MyRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(MainActivity.this, EditContact.class);
                String contactName=contactList.get(position).getContactName();
                String contactNumber=contactList.get(position).getNumber();
                intent.putExtra("ContactName",contactName);
                intent.putExtra("ContactNumber", contactNumber);
                startActivity(intent);
//                Query query=ContactListCollection.whereEqualTo("name",contactName);
//                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            QuerySnapshot querySnapshot = task.getResult();
//                            if (querySnapshot!=null && !querySnapshot.isEmpty()) {
//                                for (DocumentSnapshot document : task.getResult()) {
//                                    String contactNumb = document.getString("Number");
//                                    intent.putExtra("ContactNumber", contactNumb);
//                                    Log.d(TAG, "Got the contact Number " + contactNumb);
//                                }
//                                startActivity(intent);
//                            }else {
//                                Log.d(TAG, "No Matching contact Info found", task.getException());
//                            }
//                        } else {
//                            Log.d(TAG, "error getting Mobile Number:", task.getException());
//                        }
//                    }
//                });
            }
        };
    }



}