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
 * Use the {@link AddNewAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewAddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddNewAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewAddressFragment newInstance(String param1, String param2) {
        AddNewAddressFragment fragment = new AddNewAddressFragment();
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

    EditText editTextLine1,editTextApt, editTextCity,editTextState,editTextZip,editTextAddressName;
    String strCategory = "Address";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_address, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextLine1 = view.findViewById(R.id.editText_address_1);
        editTextApt = view.findViewById(R.id.editText_apt);
        editTextCity = view.findViewById(R.id.editText_city);
        editTextState = view.findViewById(R.id.editText_state);
        editTextZip = view.findViewById(R.id.editText_zip_code);
        editTextAddressName = view.findViewById(R.id.editText_address_name);

        view.findViewById(R.id.btn_address_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strAddressName = editTextAddressName.getText().toString();
                String strAddLine1 = editTextLine1.getText().toString();
                String strApt = editTextApt.getText().toString();
                String strCity = editTextCity.getText().toString();
                String strState = editTextState.getText().toString();
                String strZipCode = editTextZip.getText().toString();

                if(strAddressName.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter a Name for this Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strAddLine1.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter the Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strCity.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter City", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(strState.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter State", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(strZipCode.isEmpty()) {
                    Toast.makeText(getActivity(), "Please Enter Zip Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String strCurrentUserId = FirebaseAuth.getInstance().getUid();

                HashMap<String,Object> data = new HashMap<>();
                data.put(getString(R.string.FBDataType),"2");
                data.put("Address_Name",strAddressName);
                data.put("Address_Line1",strAddLine1);
                data.put("Address_Apt",strApt);
                data.put("Address_City",strCity);
                data.put("Address_State",strState);
                data.put("Address_ZipCode",strZipCode);

                CollectionReference categoryCollection = db.collection("users").document(strCurrentUserId).collection(strCategory);

                String docId = strCategory+"_"+ strAddressName;
                categoryCollection.document(docId).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Address Saved Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentView, new DashboardFragment()).commit();
                    }
                });
            }
        });
    }
}