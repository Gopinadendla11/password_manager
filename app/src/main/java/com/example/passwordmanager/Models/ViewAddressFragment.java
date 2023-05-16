package com.example.passwordmanager.Models;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Use the {@link ViewAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewAddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mAddressName;

    public ViewAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ViewAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewAddressFragment newInstance(String param1) {
        ViewAddressFragment fragment = new ViewAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAddressName = getArguments().getString(ARG_PARAM1);
        }
    }
    TextInputLayout textInputAddName,textInputAddLine1,textInputAddApt,textInputAddCity, textInputAddState,textInputAddZip;
    Button btnEdit,btnSave;
    ArrayList<String> listCategories = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("View/Edit Address");
        textInputAddName = view.findViewById(R.id.textInputLayout_add_name);
        textInputAddLine1 = view.findViewById(R.id.textInputLayout_add_line1);
        textInputAddApt = view.findViewById(R.id.textInputLayout_add_apt);
        textInputAddCity = view.findViewById(R.id.textInputLayout_city);
        textInputAddState = view.findViewById(R.id.textInputLayout_state);
        textInputAddZip = view.findViewById(R.id.textInputLayout_Zipcode);
        btnEdit = view.findViewById(R.id.btn_address_edit);
        btnSave = view.findViewById(R.id.btn_address_edit_save);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userDocId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userDoc = db.collection("users").document(userDocId);
        userDoc.collection("Address").document("Address_"+mAddressName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    textInputAddName.getEditText().setText((String)task.getResult().get("Address_Name"));
                    textInputAddName.getEditText().setEnabled(false);

                    textInputAddLine1.getEditText().setText((String)task.getResult().get("Address_Line1"));
                    textInputAddLine1.getEditText().setEnabled(false);

                    textInputAddApt.getEditText().setText((String)task.getResult().get("Address_Apt"));
                    textInputAddApt.getEditText().setEnabled(false);

                    textInputAddCity.getEditText().setText((String)task.getResult().get("Address_City"));
                    textInputAddCity.getEditText().setEnabled(false);

                    textInputAddState.getEditText().setText((String)task.getResult().get("Address_State"));
                    textInputAddState.getEditText().setEnabled(false);

                    textInputAddZip.getEditText().setText((String)task.getResult().get("Address_ZipCode"));
                    textInputAddZip.getEditText().setEnabled(false);

                }

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputAddName.getEditText().setEnabled(true);
                textInputAddLine1.getEditText().setEnabled(true);
                textInputAddApt.getEditText().setEnabled(true);
                textInputAddCity.getEditText().setEnabled(true);
                textInputAddState.getEditText().setEnabled(true);
                textInputAddZip.getEditText().setEnabled(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save Updated Address data

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentView, new DashboardFragment())
                        .commit();
            }
        });
    }
}