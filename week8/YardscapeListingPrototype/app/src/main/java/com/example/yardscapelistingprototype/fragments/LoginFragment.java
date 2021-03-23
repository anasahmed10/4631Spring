package com.example.yardscapelistingprototype.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.yardscapelistingprototype.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private Button btnLogin, btnRegister, btnForgot;
    private EditText emailView, passView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    SignInButton signInButton;
    GoogleSignInAccount googleSignInAccount;
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        btnLogin = (Button) v.findViewById(R.id.loginButton);
        btnRegister = (Button) v.findViewById(R.id.registerButton);
        btnForgot = (Button) v.findViewById(R.id.forgotButton);
        emailView = (EditText) v.findViewById(R.id.emailEntry);
        passView = (EditText) v.findViewById(R.id.passwordEntry);
        signInButton = (SignInButton) v.findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(emailView.toString(), passView.toString());
            }
        });

        // Opens register account fragment
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                Fragment fragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // Opens forgot account fragment
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                Fragment fragment = new ForgotFragment();
                fragmentTransaction.replace(R.id.flContent, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                      GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso);
            }
        });


        return v;
    }


    private void loginUser(String email, final String password) {
        // Attempts to sign the user in or logs the appropriate message in case of error
        try {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) requireContext(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Snackbar.make(requireView(), "Login Successful!", Snackbar.LENGTH_SHORT).show();
                        mUser = mAuth.getCurrentUser();
                        FragmentManager fragmentManager = getParentFragmentManager();
                        fragmentManager.popBackStackImmediate();
                    }
                    else {
                        Snackbar.make(requireView(), "Login Failed!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Snackbar.make(requireActivity().findViewById(R.id.flContent), e.getLocalizedMessage().toString(), Snackbar.LENGTH_LONG);
        }
    }
}