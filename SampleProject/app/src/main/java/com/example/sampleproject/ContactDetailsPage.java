package com.example.sampleproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ContactDetailsPage extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseDatabase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details_page);
        EditText name=findViewById(R.id.contactName);
        EditText number=findViewById(R.id.editTextPhone);
        Button save=findViewById(R.id.savebutton);
        Button cancel=findViewById(R.id.cancelButton);
        ref = FirebaseDatabase.getInstance().getReference().getDatabase();

        //saving Contact info starts here
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialoger=new AlertDialog.Builder(ContactDetailsPage.this);
                alertDialoger.setMessage("Do you want confirm saving this contact?");
               // db=FirebaseFirestore.getInstance();
                alertDialoger.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        String data1 = name.getText().toString();
                        String data2 = number.getText().toString();
                        //Changing First Character of name to uppercase
                        String cname=data1.substring(0,1).toUpperCase()+data1.substring(1);

                        //should add more specific regular expressions here
                        if (data2.length()!=10) {
                            Toast.makeText(getApplicationContext(), "Enter correct Number", Toast.LENGTH_SHORT).show();
                        }
                        else if (data1.isEmpty()) {
                            Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_LONG).show();

                        } else {
                            Map<String, Object> contact = new HashMap<>();
                            contact.put("name", cname);
                            contact.put("Number", data2);

                            ref.getReference().child("contact").child(data2).setValue(contact).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "Contact Saved Successfully", Toast.LENGTH_LONG).show();
                                            Intent backtohome = new Intent(ContactDetailsPage.this, MainActivity.class);
                                            startActivity(backtohome);
                                        }
                                    }
                            );









//                                    db.collection("ContactsList").add(contact).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                                @Override
//                                public void onSuccess(DocumentReference documentReference) {
//                                    Toast.makeText(getApplicationContext(), "Contact Saved Successfully", Toast.LENGTH_LONG).show();
//                                    Intent backtohome = new Intent(ContactDetailsPage.this, MainActivity.class);
//                                    startActivity(backtohome);
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
//                                }
//                            });
                        }
                    }
                });
                alertDialoger.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                });
                AlertDialog alertDialog = alertDialoger.create();
                alertDialog.show();
            }
        });
        //saving contact info ends here

        //cancel button onClickListener starts here
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder discard=new AlertDialog.Builder(ContactDetailsPage.this);
                discard.setMessage("Are you sure Do you want to discard this new Contact?");
                //cancelling cancel operation
                discard.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing here

                    }
                });
                //confirm cancelling
                discard.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent backtohome=new Intent(ContactDetailsPage.this,MainActivity.class);
                        startActivity(backtohome);
                    }
                });
                AlertDialog alertDiscard =discard.create();
                alertDiscard.show();
            }
        });
        //cancel button onClickListener ends here

    }
}