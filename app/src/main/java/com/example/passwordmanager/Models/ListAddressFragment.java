package com.example.passwordmanager.Models;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.passwordmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListAddressFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListAddressFragment newInstance(String param1, String param2) {
        ListAddressFragment fragment = new ListAddressFragment();
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
        return inflater.inflate(R.layout.fragment_list_address, container, false);
    }
    ListView listViewAddress;
    ArrayAdapter<String> listViewAdapter;
    ArrayList<String> listAddresses = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Saved Addresses");
        listViewAddress = view.findViewById(R.id.listview_address);
        listAddresses.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userDocId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("users").document(userDocId).collection("Address").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc:task.getResult()){
                        listAddresses.add((String) doc.get("Address_Name"));
                    }
                    listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,listAddresses);
                    listViewAddress.setAdapter(listViewAdapter);
                }

                listViewAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String strAddressName = listAddresses.get(position);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.contentView, ViewAddressFragment.newInstance(strAddressName))
                                .addToBackStack(null)
                                .commit();
                    }
                });

            }
        });
    }
}