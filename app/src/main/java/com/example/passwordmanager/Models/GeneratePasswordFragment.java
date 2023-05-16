package com.example.passwordmanager.Models;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.passwordmanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneratePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneratePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GeneratePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GeneratePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneratePasswordFragment newInstance(String param1, String param2) {
        GeneratePasswordFragment fragment = new GeneratePasswordFragment();
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
        return inflater.inflate(R.layout.fragment_generate_password, container, false);
    }

    Button btnGeneratePassword;
    Switch switchCaps,switchSmallCase,switchNumbers,switchSpecialChars;
    EditText EditTextNCharacters;
    TextView textViewGeneratedPassword;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Generate New Password");
        btnGeneratePassword = view.findViewById(R.id.btn_gen_password);
        switchCaps = view.findViewById(R.id.switch_caps);
        switchSmallCase = view.findViewById(R.id.switch_small);
        switchNumbers = view.findViewById(R.id.switch_numbers);
        switchSpecialChars = view.findViewById(R.id.switch_special_chars);
        EditTextNCharacters = view.findViewById(R.id.edittext_nchars);
        textViewGeneratedPassword = view.findViewById(R.id.textview_generated_password);


        btnGeneratePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String chars = "";
                if(switchCaps.isChecked())  chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
                if(switchSmallCase.isChecked()) chars += "abcdefghijklmnopqrstuvxyz";
                if(switchNumbers.isChecked()) chars += "12345678901234567890";
                if(switchSpecialChars.isChecked()) chars+= "!@#$%^&*_";

                int nChars = 0;
                if(EditTextNCharacters.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Please enter the number of characters required", Toast.LENGTH_SHORT).show();
                }
                else nChars = Integer.parseInt(EditTextNCharacters.getText().toString());

                if(chars.isEmpty()){
                    Toast.makeText(getActivity(), "Please select any condition", Toast.LENGTH_SHORT).show();
                    return;
                }

                StringBuilder strGeneratedPassword = new StringBuilder(nChars);

                for (int i = 0; i < nChars; i++) {
                    int index = (int)(chars.length() * Math.random());

                    strGeneratedPassword.append(chars.charAt(index));
                }
                textViewGeneratedPassword.setText(strGeneratedPassword);
            }
        });
    }


}