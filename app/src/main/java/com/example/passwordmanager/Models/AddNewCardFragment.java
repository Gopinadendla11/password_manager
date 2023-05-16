package com.example.passwordmanager.Models;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNewCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewCardFragment newInstance(String param1, String param2) {
        AddNewCardFragment fragment = new AddNewCardFragment();
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

    EditText editTextCardHolderName,editTextCardNumber,editTextCardExpiry,editTextCardCVV;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_card, container, false);
    }
    String strCategory = "Cards";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextCardHolderName = view.findViewById(R.id.edittext_cardname);
        editTextCardNumber = view.findViewById(R.id.edittext_cardnumber);
        editTextCardExpiry = view.findViewById(R.id.edittext_expiry);
        editTextCardCVV = view.findViewById(R.id.edittext_securitycode);

        view.findViewById(R.id.btn_card_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strCardName = editTextCardHolderName.getText().toString();
                String strCardNumber = editTextCardNumber.getText().toString();
                String strCardExpiry = editTextCardExpiry.getText().toString();
                String strCardCvv = editTextCardCVV.getText().toString();

                if(strCardName.equals(getString(R.string.select_category))) {
                    Toast.makeText(getActivity(), "Please Enter CardHolder Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strCardNumber.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Card Number", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strCardExpiry.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Card Expiry Date", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strCardCvv.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Card CVV", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String strCurrentUserId = FirebaseAuth.getInstance().getUid();

                HashMap<String,Object> data = new HashMap<>();
                data.put(getString(R.string.FBDataType),"1");
                data.put("Card_Name",strCardName);
                data.put("Card_Number",strCardNumber);
                data.put("Card_Expiry",strCardExpiry);
                data.put("Card_CVV",strCardCvv);

                CollectionReference categoryCollection = db.collection("users").document(strCurrentUserId).collection(strCategory);

                String docId = strCategory+"_"+ strCardName;
                categoryCollection.document(docId).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Card Data Saved Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentView, new DashboardFragment()).commit();
                    }
                });

            }
        });
    }
}