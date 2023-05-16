package com.example.passwordmanager.Models;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.passwordmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewEditPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewEditPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "category";
    private static final String ARG_PARAM2 = "app_name";

    // TODO: Rename and change types of parameters
    private String mCategory;
    private String mAppName;

    public ViewEditPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ViewEditPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewEditPasswordFragment newInstance(String strCategory, String strAppName) {
        ViewEditPasswordFragment fragment = new ViewEditPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, strCategory);
        args.putString(ARG_PARAM2, strAppName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCategory = getArguments().getString(ARG_PARAM1);
            mAppName = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_edit_password, container, false);
    }
    TextInputLayout textInputAppName,textInputUserName,textInputPassword;
    Button btnEdit,btnSave;
    AutoCompleteTextView categoryDropdown;
    ArrayList<String> listCategories = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("View/Edit Login Details");
        textInputAppName = view.findViewById(R.id.txt_input_App_name);
        textInputUserName = view.findViewById(R.id.txt_input_username);
        textInputPassword = view.findViewById(R.id.txt_input_password);
        btnEdit = view.findViewById(R.id.btn_edit);
        btnSave = view.findViewById(R.id.btn_update_save);

        textInputAppName.getEditText().setText(mAppName);
        textInputAppName.getEditText().setEnabled(false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String strDocId = mCategory+"_"+mAppName;


        //db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().
        String userDocId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userDoc = db.collection("users").document(userDocId);
        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    listCategories = (ArrayList<String>)task.getResult().get("categories");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.drop_down,listCategories);
                     categoryDropdown = view.findViewById(R.id.autoComplete);
                    categoryDropdown.setAdapter(adapter);
                    Log.d("demo", "onComplete: ");
                    categoryDropdown.setText(mCategory);
                    categoryDropdown.setEnabled(false);
                }
            }
        });

        db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection(mCategory).document(strDocId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    textInputUserName.getEditText().setText((String)task.getResult().get(getString(R.string.FBUserName)));
                    textInputUserName.getEditText().setEnabled(false);
                    textInputPassword.getEditText().setText((String)task.getResult().get(getString(R.string.FBPassword)));
                    textInputPassword.getEditText().setEnabled(false);
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputUserName.getEditText().setEnabled(true);
                textInputPassword.getEditText().setEnabled(true);
                categoryDropdown.setEnabled(true);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Save Updated Login data

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentView, new DashboardFragment())
                        .commit();
            }
        });
    }
}