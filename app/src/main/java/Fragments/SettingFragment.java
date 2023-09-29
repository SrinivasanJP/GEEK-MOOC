package Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import jp.srini.geek_mooc.Basic_Info;
import jp.srini.geek_mooc.Login;
import com.Srini.geek_mooc.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Backend.BasicInfo_helper;

public class SettingFragment extends Fragment {


    public SettingFragment() {
        // Required empty public constructor
    }

    private RelativeLayout logout,git,other;
    private TextView data[];
    private int ids[];
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;
    private BasicInfo_helper userDetials;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        data = new TextView[11];
        ids = new int[]{R.id.name,R.id.role,R.id.email,R.id.about,R.id.name1,R.id.mobile,R.id.gender,R.id.country,R.id.language,R.id.modifydetials,R.id.deleteData};
        for(int i=0;i<11;i++){
            data[i] = view.findViewById(ids[i]);
        }
        reference = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userDetials = snapshot.getValue(BasicInfo_helper.class);
                setViews();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        logout = view.findViewById(R.id.logout);
        git = view.findViewById(R.id.gitBtn);
        other = view.findViewById(R.id.othersBtn);
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
    public void setViews(){
        data[0].setText(userDetials.getName());
        data[1].setText(userDetials.getEducationalBackground());
        data[2].setText(userDetials.getEmail());
        data[3].setText(userDetials.getAbout());
        data[4].setText(userDetials.getName());
        data[5].setText(userDetials.getMobile());
        data[6].setText(userDetials.getGender());
        data[7].setText(userDetials.getCountry());
        data[8].setText(userDetials.getLanguage());
        data[9].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Basic_Info.class));
            }
        });
        data[10].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Your data have been deleted", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getContext(), Login.class));
                        }else{
                            Toast.makeText(getContext(), "your data can't be deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/"+userDetials.getGit())));
            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://"+userDetials.getWebsite())));

            }
        });

    }
}