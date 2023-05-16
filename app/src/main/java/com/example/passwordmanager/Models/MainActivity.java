package com.example.passwordmanager.Models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//import android.util.Log;

import com.example.passwordmanager.R;
import com.google.firebase.auth.FirebaseAuth;
import com.example.passwordmanager.auth.LoginFragment;
import com.example.passwordmanager.auth.RegisterFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener,
        RegisterFragment.RegisterFragmentListener, VerificationFragment.DashboardFragmentListener, DashboardFragment.DashboardFragmentListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView, new LoginFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentView,  new VerificationFragment() )
                    //.add(R.id.contentView,  new DashboardFragment() )
                    .commit();
        }
    }

    @Override
    public void gotoRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new RegisterFragment())
                .commit();
    }

    @Override
    public void successGotoDashboardFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new DashboardFragment())
                .commit();
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new LoginFragment())
                .commit();
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new LoginFragment())
                .commit();
    }

    @Override
    public void goToDashboardFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new DashboardFragment())
                .commit();
    }

    @Override
    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentView, new LoginFragment())
                .commit();
    }
}