package com.example.bookshere;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    Button register;
    FirebaseAuth mAuth;
    EditText user_name,user_email, pass_word,re_pass_word;
    ProgressBar prg;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        user_name=(EditText) rootView.findViewById(R.id.et_name);
        user_email=(EditText) rootView.findViewById(R.id.reg_email);
        pass_word=(EditText) rootView.findViewById(R.id.et_password);
        re_pass_word=(EditText) rootView.findViewById(R.id.et_repassword);
        register = (Button)rootView.findViewById(R.id.btn_register);
        prg=(ProgressBar)rootView.findViewById(R.id.progressBar2);
        mAuth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user_email.getText().toString().trim();
                String password= pass_word.getText().toString();
                String name = user_name.getText().toString();
                String re_pass = re_pass_word.getText().toString();


                if(name.isEmpty())
                {
                    user_name.setError("Name is empty");
                    user_name.requestFocus();
                    return;
                }
                if(email.isEmpty())
                {
                    user_email.setError("Email is empty");
                    user_email.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    user_email.setError("Enter the valid email address");
                    user_email.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    pass_word.setError("Enter the password");
                    pass_word.requestFocus();
                    return;
                }

                if(password.length()<6)
                {
                    pass_word.setError("Length of the password should be more than 6");
                    pass_word.requestFocus();
                    return;
                }
                if(re_pass.isEmpty())
                {
                    re_pass_word.setError("Enter the Phone Number");
                    re_pass_word.requestFocus();
                    return;
                }
                prg.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            User user=new User(name,email,password,re_pass);
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterFragment.this.getActivity(),"You are successfully Registered", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(RegisterFragment.this.getActivity(),"Swipe left to Verify and Login", Toast.LENGTH_LONG).show();
                                        prg.setVisibility(View.INVISIBLE);
                                    }
                                    else{
                                        Toast.makeText(RegisterFragment.this.getActivity(),"You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(RegisterFragment.this.getActivity(),"You are not Registered! Try again", Toast.LENGTH_SHORT).show();
                            prg.setVisibility(View.INVISIBLE);
                        }


                    }
                });





            }
        });
        return rootView;
    }

}
