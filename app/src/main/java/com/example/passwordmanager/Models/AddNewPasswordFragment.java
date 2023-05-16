package com.example.passwordmanager.Models;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewPasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewPasswordFragment newInstance(String param1, String param2) {
        AddNewPasswordFragment fragment = new AddNewPasswordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        return inflater.inflate(R.layout.fragment_add_new_password, container, false);


    }
    EditText editTextPwd,editTextAppName,editTextUserName;
    Spinner spinnerCategory;
    ImageView imageViewGeneratePwd;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Spinner categoryDropdown = view.findViewById(R.id.spinner_category);
        String[] items = new String[]{getString(R.string.select_category),getString(R.string.miscellaneous),"Banking","Social Media"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        categoryDropdown.setAdapter(adapter);

        editTextPwd =  view.findViewById(R.id.editText_pwd);
        editTextAppName =  view.findViewById(R.id.editText_app_name);
        editTextUserName = view.findViewById(R.id.editText_username);
        spinnerCategory = view.findViewById(R.id.spinner_category);
        imageViewGeneratePwd = view.findViewById(R.id.imageview_generate_pwd);

        view.findViewById(R.id.imageview_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("demo", "onClick: copy");
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("selected text",  editTextPwd.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });

        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strCategory = spinnerCategory.getSelectedItem().toString();
                String strAppName = editTextAppName.getText().toString();
                String strUserName = editTextUserName.getText().toString();
                String strPassword = editTextPwd.getText().toString();

                if(strCategory.equals(getString(R.string.select_category))) {
                    Toast.makeText(getActivity(), "Please select a category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strAppName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter the Application name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strUserName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter the User name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strPassword.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter the Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String strCurrentUserId = FirebaseAuth.getInstance().getUid();

                HashMap<String,Object> data = new HashMap<>();
                data.put(getString(R.string.FBDataType),"0");
                data.put(getString(R.string.FBAppName),strAppName);
                data.put(getString(R.string.FBUserName),strUserName);
                data.put(getString(R.string.FBPassword),strPassword);

                CollectionReference categoryCollection = db.collection("users").document(strCurrentUserId).collection(strCategory);

                String docId = strCategory+"_"+ strAppName;
                categoryCollection.document(docId).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentView, new DashboardFragment()).commit();
                    }
                });
            }
        });

        imageViewGeneratePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentView,new GeneratePasswordFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentView, new DashboardFragment()).commit();
            }
        });
    }
}