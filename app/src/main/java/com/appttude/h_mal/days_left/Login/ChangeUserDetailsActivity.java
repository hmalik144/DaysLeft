package com.appttude.h_mal.days_left.Login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appttude.h_mal.days_left.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import static com.appttude.h_mal.days_left.Global.FirebaseClass.auth;

public class ChangeUserDetailsActivity extends AppCompatActivity {

    private static String TAG = "ChangeUserDetailsActivity";

    private TextView email;
    private TextView name;
    private TextView changePw;

    private FirebaseUser user;

    private static final String EMAIL_CONSTANT = "Email Address";
    private static final String PW_CONSTANT = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_details);

        email = findViewById(R.id.change_email);
        name = findViewById(R.id.change_profile_name);
        changePw = findViewById(R.id.change_pw);

        user = auth.getCurrentUser();

        Button button = findViewById(R.id.submit_profile);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeUserDetailsActivity.this);
                dialog.setTitle("Update Username");

                final EditText titleBox = new EditText(ChangeUserDetailsActivity.this);
                titleBox.setText(user.getDisplayName());
                dialog.setView(titleBox);
                dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateProfile(titleBox.getText().toString().trim());
                    }
                });
                dialog.create().show();

            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(EMAIL_CONSTANT);
            }
        });

        changePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(PW_CONSTANT);
            }
        });


    }

    private void updateProfile(String profileName){
        UserProfileChangeRequest.Builder profileUpdatesBuilder = new UserProfileChangeRequest.Builder();

        if (!TextUtils.isEmpty(profileName)){
            profileUpdatesBuilder.setDisplayName(profileName);
        }

        UserProfileChangeRequest profileUpdates = profileUpdatesBuilder.build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
//                            viewController.reloadDrawer();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChangeUserDetailsActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void changeCredentials(String email, String password, final String changeText, final String selector){
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, password); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");

                        user = FirebaseAuth.getInstance().getCurrentUser();
                        if (selector.equals(EMAIL_CONSTANT)){
                            user.updateEmail(changeText)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User email address updated.");
                                                Toast.makeText(ChangeUserDetailsActivity.this, "Email Update Successful", Toast.LENGTH_SHORT).show();

//                                                viewController.reloadDrawer();
                                            }else {
                                                Toast.makeText(ChangeUserDetailsActivity.this, "Email Update Unsuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        if (selector.equals(PW_CONSTANT)){
                            user.updatePassword(changeText)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User email address updated.");
                                                Toast.makeText(ChangeUserDetailsActivity.this, "Password Update Successful", Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(ChangeUserDetailsActivity.this, "Password Update Unsuccessful", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }



                    }
                });
    }

    private void showDialog(final String update){
        //Make new Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(ChangeUserDetailsActivity.this);
        dialog.setTitle("Update " + update);

        LinearLayout layout = new LinearLayout(ChangeUserDetailsActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(28,0,56,0);

        final EditText box1 = new EditText(ChangeUserDetailsActivity.this);
        box1.setHint("Current Email Address");
        box1.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        layout.addView(box1); // Notice this is an add method

        final EditText box2 = new EditText(ChangeUserDetailsActivity.this);
        box2.setHint("Current Password");
        box2.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(box2); // Another add method

        final EditText box3 = new EditText(ChangeUserDetailsActivity.this);
        if (update.equals(PW_CONSTANT)){
            box3.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }else {
            box3.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        }
        box3.setHint("New " + update);
        layout.addView(box3); // Another add method

        dialog.setView(layout);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = box1.getText().toString().trim();
                String password = box2.getText().toString().trim();
                String textThree = box3.getText().toString().trim();

                changeCredentials(email,password,textThree,update);
            }
        });
        dialog.create().show();
    }
}
