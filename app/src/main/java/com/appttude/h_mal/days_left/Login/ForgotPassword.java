package com.appttude.h_mal.days_left.Login;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.appttude.h_mal.days_left.R;


public class ForgotPassword extends Fragment {

    String TAG = "forgotPasswordFragment";
    private EditText editText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        editText = view.findViewById(R.id.reset_pw);

        Button resetPw = view.findViewById(R.id.reset_pw_sign_up);
        resetPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(editText.getText().toString().trim());
            }
        });

        return view;
    }

    private void resetPassword(String emailAddress){
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");

                            FullscreenActivity.fragmentManager.popBackStack();
                        }else {
                            Toast.makeText(getContext(), "Could not reset Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
