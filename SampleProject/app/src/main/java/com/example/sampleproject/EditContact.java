package com.example.sampleproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class EditContact extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseDatabase ref;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        TextView user=findViewById(R.id.nameView);
        TextView Numb=findViewById(R.id.numberView);
        Button back=findViewById(R.id.button3);
        Button updateContact=findViewById(R.id.button);
        Button DeleteContact=findViewById(R.id.button2);
        ref = FirebaseDatabase.getInstance().getReference().getDatabase();

        db=FirebaseFirestore.getInstance();

        //getting the database collection
        CollectionReference ContactListCollection=db.collection("ContactsList");

        name=" check code or db";
        String Number= "0";
        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            name=extras.getString("ContactName");
            Number=extras.getString("ContactNumber");
        }
        user.setText(name);
        Numb.setText(Number);


        //Previous page Button code starts here
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back=new Intent(EditContact.this,MainActivity.class);
                startActivity(back);
            }
        });
        //Previous page Button code ends here

        //delete contact code starts here
        String finalNumber = Number;
        DeleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog=new AlertDialog.Builder(EditContact.this);
                alertDialog.setMessage("Do you want to confirm Deletion ?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
//
                                ref.getReference().child("contact").child(finalNumber).removeValue().addOnCompleteListener(
                                        new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(getApplicationContext(),"Contact deleted From ContactList",Toast.LENGTH_LONG).show();
                                              Intent deleteSuccess=new Intent(EditContact.this,MainActivity.class);
                                              startActivity(deleteSuccess);
                                            }

                                        });
                            }
                        }
                );
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        AlertDialog alertDialname = alertDialog.create();
                        alertDialname.show();
                    }

        });
        //delete contact code ends here

        //update contact code starts here

        updateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder updateDialog=new AlertDialog.Builder(EditContact.this);
                updateDialog.setMessage("Do you wanna Confirm Updating this contact ?");
                //query for getting the opened contact details
                Query updateQuery=ContactListCollection.whereEqualTo("name",name);
                updateDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String newName=user.getText().toString();
                        String newNumber=Numb.getText().toString();
                        if (newNumber != finalNumber){
                            ref.getReference().child("contact").child(finalNumber).removeValue().addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        }

                                    });

                        }
                        Map<String, Object> contact = new HashMap<>();
                        contact.put("name", newName);
                        contact.put("Number", newNumber);

                        ref.getReference().child("contact").child(newNumber).setValue(contact).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(getApplicationContext(), "Contact Updated Successfully", Toast.LENGTH_LONG).show();
                                        Intent backtohome = new Intent(EditContact.this, MainActivity.class);
                                        startActivity(backtohome);
                                    }
                                }
                        );

                    }
                });
                updateDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog alertUpdate=updateDialog.create();
                alertUpdate.show();
            }
        });
        //updating contact code ends here

    }
}