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
 * Use the {@link ViewCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mCardDocName;


    public ViewCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ViewCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewCardFragment newInstance(String param1) {
        ViewCardFragment fragment = new ViewCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCardDocName = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_card, container, false);
    }
    TextInputLayout textInputCardName,textInputCardNumber,textInputCardExpiry,textInputCardCvv;
    Button btnEdit,btnSave;
    ArrayList<String> listCategories = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("View/Edit Saved Cards");
        textInputCardName = view.findViewById(R.id.tli_card_name);
        textInputCardNumber = view.findViewById(R.id.tli_card_number2);
        textInputCardExpiry = view.findViewById(R.id.tli_card_expiry2);
        textInputCardCvv = view.findViewById(R.id.tli_card_cvv2);
        btnEdit = view.findViewById(R.id.btn_address_edit);
        btnSave = view.findViewById(R.id.btn_address_edit_save);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userDocId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userDoc = db.collection("users").document(userDocId);
        userDoc.collection("Cards").document("Cards_"+mCardDocName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    textInputCardName.getEditText().setText((String)task.getResult().get("Card_Name"));
                    textInputCardName.getEditText().setEnabled(false);

                    textInputCardNumber.getEditText().setText((String)task.getResult().get("Card_Number"));
                    textInputCardNumber.getEditText().setEnabled(false);

                    textInputCardExpiry.getEditText().setText((String)task.getResult().get("Card_Expiry"));
                    textInputCardExpiry.getEditText().setEnabled(false);

                    textInputCardCvv.getEditText().setText((String)task.getResult().get("Card_CVV"));
                    textInputCardCvv.getEditText().setEnabled(false);

                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputCardName.getEditText().setEnabled(true);
                textInputCardNumber.getEditText().setEnabled(true);
                textInputCardExpiry.getEditText().setEnabled(true);
                textInputCardCvv.getEditText().setEnabled(true);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Save Updated Card data

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentView, new DashboardFragment())
                        .commit();
            }
        });

    }
}