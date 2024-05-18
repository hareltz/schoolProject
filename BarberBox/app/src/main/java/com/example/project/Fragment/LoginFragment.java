package com.example.project.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.project.R;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    public void LoginBT(View view)
    {

    }

    public void ToRegister(View view)
    {
        /*if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).replaceFragment(new RegisterFragment());
        }*/
    }
}