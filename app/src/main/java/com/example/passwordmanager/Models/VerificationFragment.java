package com.example.passwordmanager.Models;

import android.content.Context;
import androidx.biometric.BiometricPrompt;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public VerificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VerificationFragment newInstance(String param1, String param2) {
        VerificationFragment fragment = new VerificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Password Manager");
        EditText editTextMasterPwd =  view.findViewById(R.id.edittext_master_password);
        executor = ContextCompat.getMainExecutor(getContext());
        biometricPrompt = new BiometricPrompt(this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
//                Toast.makeText(getContext(),
//                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
//                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                        mListener.goToDashboardFragment();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for password manager")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

        biometricPrompt.authenticate(promptInfo);

        view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(editTextMasterPwd.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Enter Master Password", Toast.LENGTH_SHORT).show();
               }
               else{
                   FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                   String email = currentUser.getEmail();
                   AuthCredential credential = EmailAuthProvider
                           .getCredential(email, editTextMasterPwd.getText().toString());
                   currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mListener.goToDashboardFragment();
                            }
                       }
                   });
               }
            }
        });

        view.findViewById(R.id.btn_fingerprint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Biometric login for password manager")
                        .setSubtitle("Log in using your biometric credential")
                        .setNegativeButtonText("Use account password")
                        .build();

                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    DashboardFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (DashboardFragmentListener) context;
    }

    interface DashboardFragmentListener{
        void logout();
        void goToDashboardFragment();
    }
}