package com.example.cryptoview;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    TextView fullName, email, phone, age;
    Button logoutBtn, homePage, favPage, newsPage;
    ImageButton editEmail, editAge, editPhone, editName, profilePage;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        homePage = findViewById(R.id.home);
        favPage = findViewById(R.id.fav);
        newsPage = findViewById(R.id.news);

        fullName = findViewById(R.id.userName);
        email = findViewById(R.id.userEmail);
        phone = findViewById(R.id.userPhone);
        age = findViewById(R.id.userAge);
        logoutBtn = findViewById(R.id.LogoutButton);
        editEmail = findViewById(R.id.editEmail);
        editAge = findViewById(R.id.editAge);
        editPhone = findViewById(R.id.editPhone);
        editName = findViewById(R.id.editName);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {
                phone.setText(documentSnapshot.getString("phone"));
                fullName.setText(documentSnapshot.getString("name"));
                email.setText(documentSnapshot.getString("email"));
                age.setText(documentSnapshot.getString("age"));
            }
        });

        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        favPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Favorites.class);
                startActivity(intent);
            }
        });
        newsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), News.class);
                startActivity(intent);
            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Profile.this);
                alertDialog.setTitle("Change Name");
                alertDialog.setMessage("Enter Your Name");

                final EditText input = new EditText(Profile.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
//                                DocumentReference documentReference = fstore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("name", "newName");
                                user.put("email", "Hello world");
                                user.put("phone", phone);
                                user.put("age", age);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(Profile.this, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Profile.this, "Failure", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                alertDialog.show();
            }

        });

////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                EditText changeName = new EditText(v.getContext());
//                AlertDialog.Builder nameChangeDialog = new AlertDialog.Builder(v.getContext());
//                nameChangeDialog.setTitle("Change Name");
//                nameChangeDialog.setMessage("Enter Your Name");
//                nameChangeDialog.setView(changeName);
//
//                nameChangeDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(Profile.this, "It is a work", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                nameChangeDialog.setNegativeButton("HELLO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//
//                nameChangeDialog.create().show();
////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                DocumentReference documentReference = fstore.collection("users").document(userID);
//                Map<String,Object> user = new HashMap<>();
//                user.put("name", "newName");
//                user.put("email", "Hello world");
//                user.put("phone", phone);
//                user.put("age", age);
//
//                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(Profile.this, "Success", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Profile.this, "Failure", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

    }
}