package jp.srini.geek_mooc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import com.Srini.geek_mooc.R;;
import com.google.android.material.navigation.NavigationBarView;

import Fragments.CreateCourseFragment;
import Fragments.HomeFragment;
import Fragments.ManageCourseFragment;
import Fragments.RegisteredCoursesFragment;
import Fragments.SettingFragment;

public class Home extends AppCompatActivity {
    private Button vLogout;
    private NavigationBarView bottomNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNav = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    selected = new HomeFragment();
                } else if (itemId == R.id.regCourse) {
                    selected = new RegisteredCoursesFragment();
                } else if (itemId == R.id.createCourse) {
                    selected = new CreateCourseFragment();
                } else if (itemId == R.id.setting) {
                    selected = new SettingFragment();
                } else if (itemId == R.id.manageCourse) {
                    selected = new ManageCourseFragment();
                } else {
                    selected = null;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selected).commit();
                return true;
            }

        });



    }
}