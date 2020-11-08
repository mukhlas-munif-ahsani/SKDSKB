package com.munifahsan.skdskb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.munifahsan.skdskb.BoomarkPage.BookmarkFragment;
import com.munifahsan.skdskb.HomePage.view.HomeFragment;
import com.munifahsan.skdskb.InformasiPage.view.InformasiFragment;
import com.munifahsan.skdskb.SettingPage.SettingFragment;
import com.munifahsan.skdskb.TryoutPage.TryoutFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getFragmentPage(new HomeFragment());

        mBottomNavigationView.setItemIconTintList(null);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.itemHome:
                fragment = new HomeFragment();
                break;
            case R.id.itemInformasi:
                fragment = new InformasiFragment();
                break;
            case R.id.itemTryout:
                fragment = new TryoutFragment();
                break;
            case R.id.itemBookmark:
                fragment = new BookmarkFragment();
                break;
            case R.id.itemSetting:
                fragment = new SettingFragment();
        }

        return loadFragment(fragment);
    }

    private void getFragmentPage(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
        }
    }

    // method untuk load fragment yang sesuai
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}