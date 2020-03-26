package csapat.app.auth;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import csapat.app.BaseCompat;
import csapat.app.R;

public class RegisterActivity extends BaseCompat {

    private static final String TAG = "EmailPassword";

    private FirebaseAuth mAuth;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etUsername;
    private FirebaseFirestore db;
    private List<String> patrols;
    private AutoCompleteTextView patrolsTextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        patrols = new ArrayList<>();
        getSuggestions();

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etLastName);
        etLastName = findViewById(R.id.etFirstName);
        etUsername = findViewById(R.id.etUsername);
        patrolsTextview = findViewById(R.id.autocomplete_patrol);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, patrols);
        patrolsTextview.setAdapter(adapter);


        Button registerBtn = findViewById(R.id.buttonRegister);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    createUserWithEmailAndPassword();


            }
        });
    }

    private void getSuggestions() {

        db.collection("patrols")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                patrols.add(document.getString("name"));
                            }
                        } else {
                        }
                    }
                });

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }



    public void createUserWithEmailAndPassword(){
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        createUserInDB();
                                        mAuth.signOut();
                                        Toast.makeText(RegisterActivity.this, "Kérlek erősítsd meg az email címed!",
                                                Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }
                            });
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // [START_EXCLUDE]

                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    public void createUserInDB() {

        String patrol = patrolsTextview.getText().toString();
        String email = etEmail.getText().toString();
        String fn = etFirstName.getText().toString();
        String ln = etLastName.getText().toString();
        String un = etUsername.getText().toString();
        String fullName = String.format("%s %s", fn, ln);
        final String[] referenceID = new String[1];


        Map<String, Object> user = new HashMap<>();
        user.put("firstName", fn);
        user.put("lastName", ln);
        user.put("fullName", fullName);
        user.put("patrol", patrol);
        user.put("rank", 1);
        user.put("email", email);
        user.put("username", un);
        user.put("score", 0);
        user.put("answeredNextMeetingRequest", false);
        user.put("achievedBadges", new ArrayList<Long>(0));

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        referenceID[0] = documentReference.getId();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }


}
