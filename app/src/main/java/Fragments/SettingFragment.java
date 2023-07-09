package Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.geek_mooc.Login;
import com.example.geek_mooc.R;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }

    private Button logout;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        logout = view.findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                getActivity().finish();
                startActivity(new Intent(getContext(), Login.class));

            }
        });
        return view;
    }
}