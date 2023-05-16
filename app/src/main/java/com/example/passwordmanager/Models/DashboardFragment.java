package com.example.passwordmanager.Models;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.passwordmanager.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    
    final String TAG = "demo";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashboardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardFragment newInstance(String param1, String param2) {
        DashboardFragment fragment = new DashboardFragment();
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
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    ImageButton imgBtnAddNew,imgBtnViewData,imgBtnGeneratePwd,imgBtnViewCards,imgBtnViewAddress;
    Button btnLogout;
    DashboardFragmentListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (DashboardFragmentListener) context;

    }

    public  interface  DashboardFragmentListener{
        void Logout();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Dashboard");
        imgBtnAddNew = view.findViewById(R.id.img_btn_add_new);
        imgBtnViewData = view.findViewById(R.id.img_btn_view_edit);
        imgBtnGeneratePwd = view.findViewById(R.id.img_btn_generate_pwd);
        btnLogout = view.findViewById(R.id.btn_logout);
        imgBtnViewCards =  view.findViewById(R.id.img_btn_view_cards);
        imgBtnViewAddress=view.findViewById(R.id.img_Btn_view_address);

        imgBtnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: imgbtnAddNew");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentView, new AddNewFragment()).addToBackStack(null).commit();
            }
        });

        imgBtnViewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.contentView, new ListCategoriesFragment()).addToBackStack(null).commit();
            }
        });

        imgBtnGeneratePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentView, new GeneratePasswordFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.Logout();
            }
        });

        imgBtnViewCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentView, new ListCardsFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
        imgBtnViewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentView, new ListAddressFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}