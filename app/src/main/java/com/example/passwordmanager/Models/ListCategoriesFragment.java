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
import android.widget.Toast;

import com.example.passwordmanager.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListCategoriesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListCategoriesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListCategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListCateoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListCategoriesFragment newInstance(String param1, String param2) {
        ListCategoriesFragment fragment = new ListCategoriesFragment();
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
        return inflater.inflate(R.layout.fragment_list_categories, container, false);
    }

    ListView listViewCategories;
    ArrayAdapter<String> listViewAdapter;
    ArrayList<String> categories = new ArrayList<>();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listViewCategories = view.findViewById(R.id.listview_categories);
        getActivity().setTitle("Categories");
        categories.clear();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userDocId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference userDoc = db.collection("users").document(userDocId);

        userDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    categories = (ArrayList<String>)task.getResult().get("categories");
                    if(categories != null){
                        listViewAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1,android.R.id.text1,categories);
                        listViewCategories.setAdapter(listViewAdapter);

                        listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                                String selectedCategory = categories.get(position);
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.contentView, ListPasswordsFragment.newInstance(selectedCategory))
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }
                }
                else{
                    Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
}