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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListPasswordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPasswordsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "category";

    // TODO: Rename and change types of parameters
    private String strCategory;

    public ListPasswordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ListPasswordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListPasswordsFragment newInstance(String param1) {
        ListPasswordsFragment fragment = new ListPasswordsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strCategory = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_passwords, container, false);
    }
    ListView listViewPasswords;
    ArrayAdapter<String> listViewAdapter;
    ArrayList<String> listPasswords = new ArrayList<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Login Credentials");
        listViewPasswords = view.findViewById(R.id.listview_categories);
        listPasswords.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userDocId = FirebaseAuth.getInstance().getCurrentUser().getUid();

       db.collection("users").document(userDocId).collection(strCategory).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               if(task.isSuccessful()){
                   for(QueryDocumentSnapshot doc:task.getResult()){
                       listPasswords.add((String) doc.get("FBAppName"));
                   }
                   listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,listPasswords);
                   listViewPasswords.setAdapter(listViewAdapter);

                   listViewPasswords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                       @Override
                       public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                           String strAppName = listPasswords.get(position);

                           getActivity().getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.contentView, ViewEditPasswordFragment.newInstance(strCategory,strAppName))
                                   .addToBackStack(null)
                                   .commit();

                       }
                   });
               }
           }
       });
    }
}