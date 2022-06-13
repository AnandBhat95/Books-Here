package com.example.bookshere;


import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookshere.databinding.BuySellScreenBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class LoginFragment extends Fragment {

    private Button login;
    private TextView forgot_pass,swipe_right;
    private EditText user_em, pass_wordd;
    FirebaseAuth mAuth;


    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;





    public LoginFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedpreferences = getContext().getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        login = (Button)rootView.findViewById(R.id.btn_login);
        user_em=(EditText)rootView.findViewById(R.id.et_email);
        forgot_pass=(TextView)rootView.findViewById(R.id.forgot) ;
        swipe_right=(TextView)rootView.findViewById(R.id.swipeRight) ;
        swipe_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginFragment.this.getActivity(),"Please swipe right to Register", Toast.LENGTH_LONG).show();

            }
        });
        pass_wordd=(EditText)rootView.findViewById(R.id.log_pass);
        mAuth=FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent=new Intent(LoginFragment.this.getActivity(), BuySellScreen.class);
            startActivity(intent);
        }
        loginPreferences = getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginFragment.this.getActivity(),ForgotPassword.class);
                startActivity(intent);

            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Action to be performed on button click
                switch (v.getId()){
                    case R.id.btn_login:
                        userLogin();
                        break;

                }

            }

            private void userLogin() {
                String em = user_em.getText().toString().trim();
                String pas= pass_wordd.getText().toString();

                if(em.isEmpty())
                {
                    user_em.setError("Email is empty");
                    user_em.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(em).matches())
                {
                    user_em.setError("Enter the valid email address");
                    user_em.requestFocus();
                    return;
                }
                if(pas.isEmpty())
                {
                    pass_wordd.setError("Enter the password");
                    pass_wordd.requestFocus();
                    return;
                }

                if(pas.length()<6)
                {
                    pass_wordd.setError("Length of the password should be more than 6");
                    pass_wordd.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(em,pas).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                            if (user.isEmailVerified()){
                                SharedPreferences.Editor editor =  sharedpreferences.edit();
                                editor.putString("UserName",em);
                                editor.commit();


                            Intent intent=new Intent(LoginFragment.this.getActivity(), BuySellScreen.class);
                            startActivity(intent);
                            }
                            else {
                                user.sendEmailVerification();

                                Toast.makeText(LoginFragment.this.getActivity(),"Verify Your Mail and Login", Toast.LENGTH_LONG).show();

                            }
                        }
                        else {
                            Toast.makeText(LoginFragment.this.getActivity(),"Bad Credentials..Try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        return rootView;
    }

}
