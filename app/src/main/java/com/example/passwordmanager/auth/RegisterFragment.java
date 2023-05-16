package com.example.passwordmanager.auth;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.passwordmanager.R;

public class RegisterFragment extends Fragment {
    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    EditText editTextName, editTextEmail, editTextPassword;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Register");
        editTextName = view.findViewById(R.id.editTextName);
        editTextEmail = view.findViewById(R.id.editTextEmail);
        editTextPassword = view.findViewById(R.id.editTextPassword);

        final String TAG = "pwdmgr";

        view.findViewById(R.id.buttonRegisterSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid name!", Toast.LENGTH_SHORT).show();
                } else if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Enter valid email!", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Master password!", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                                                    HashMap<String, Object> data = new HashMap<>();
                                                    data.put("name", name);
                                                    data.put("uid", user.getUid());
                                                    ArrayList<String> categories = new ArrayList<>();
                                                    categories.add(getString(R.string.miscellaneous));
                                                    data.put("categories",categories);

                                                    db.collection("users").document(user.getUid()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Log.d(TAG, "register user: "+ task.isSuccessful());
                                                            if(task.isSuccessful()){
                                                                Log.d(TAG, "register task successful: ");
                                                                mListener.successGotoDashboardFragment();
                                                            } else {
                                                                task.getException().printStackTrace();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        view.findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoLogin();
            }
        });

    }

    RegisterFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (RegisterFragmentListener) context;
    }

    public interface RegisterFragmentListener{
        void gotoLogin();
        void successGotoDashboardFragment();
    }
}