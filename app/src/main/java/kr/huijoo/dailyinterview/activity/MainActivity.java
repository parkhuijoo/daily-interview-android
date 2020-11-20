package kr.huijoo.dailyinterview.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kr.huijoo.dailyinterview.R;
import kr.huijoo.dailyinterview.fragment.HomeFragment;
import kr.huijoo.dailyinterview.fragment.ListFragment;
import kr.huijoo.dailyinterview.fragment.ProfileFragment;

/**
 * MainActivity.java
 * 작성자 : 박희주
 * V1.0
 */

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private ListFragment listFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // BottomNavigationView 설정, 클릭에 따른 Fragment 전환 리스너 부착
        bottomNavigationView = findViewById(R.id.bottomnavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.main1_icon:
                        setFrag(0);
                        break;
                    case R.id.main2_icon:
                        setFrag(1);
                        break;
                    case R.id.main4_icon:
                        setFrag(2);
                        break;
                }
                return true;
            }
        });

        // 부착할 각 Fragment 객체 생성
        homeFragment = new HomeFragment();
        listFragment = new ListFragment();
        profileFragment = new ProfileFragment();
        setFrag(0);

    }

    /**
     * BottomNavigation 클릭에 따른 Fragment 전환
     * @param n = 몇 번째 버튼인가
     */
    private void setFrag(int n){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.MainNonavi, homeFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.MainNonavi, listFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.MainNonavi, profileFragment);
                ft.commit();
                break;
        }
    }

}