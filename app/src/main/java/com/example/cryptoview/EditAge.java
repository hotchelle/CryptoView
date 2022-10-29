package com.example.cryptoview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class EditAge extends AppCompatActivity {
    EditText mAge;
    TextView mFullName, mEmail, mPhone;
    Button mRegisterBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fstore;
    String userID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editage);

        mFullName = findViewById(R.id.FullName);
        mRegisterBtn = findViewById(R.id.Edit);
        mEmail = findViewById(R.id.userEmail);
        mAge = findViewById(R.id.userAge);
        mPhone = findViewById(R.id.userPhone);


        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException error) {
                mPhone.setText(documentSnapshot.getString("phone"));
                mFullName.setText(documentSnapshot.getString("name"));
                mEmail.setText(documentSnapshot.getString("email"));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = mPhone.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String age = mAge.getText().toString().trim();
                String name = mFullName.getText().toString().trim();

                DocumentReference documentReference = fstore.collection("users").document(userID);
                Map<String,Object> user = new HashMap<>();
                user.put("name", name);
                user.put("email", email);
                user.put("phone", phone);
                user.put("age", age);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditAge.this, "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditAge.this, "Failure", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });
    }
}
