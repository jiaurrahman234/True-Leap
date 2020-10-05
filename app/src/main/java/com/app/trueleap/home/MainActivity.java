package com.app.trueleap.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityMainBinding;
import com.app.trueleap.external.Utils;
import com.app.trueleap.home.studentsubject.HomeSubjectsFragment;
import com.app.trueleap.localization.ChangeLanguageActivity;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityMainBinding binding;
    Toolbar toolbar;
    DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        loadFragment(new HomeSubjectsFragment());
        initNavigation();
    }
    private void initNavigation() {
        toolbar = findViewById(R.id.toolbar);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        drawer.closeDrawer(Gravity.LEFT);
        toolbar.setTitle(getResources().getString(R.string.app_name));
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(binding.frameContainer.getId(), fragment, Utils.Home_Subject_Fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        switch (itemId) {
            case R.id.nav_home:
                break;
            case R.id.nav_changeLanguage:
                startActivity(new Intent(MainActivity.this, ChangeLanguageActivity.class));
                break;
            case R.id.nav_logout:
                try {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.MyAlertDialogStyle);
                    builder.setTitle(context.getResources().getString(R.string.confirm))
                            .setIcon(R.drawable.logo)
                            .setMessage(context.getResources().getString(R.string.exit_msg))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    localStorage.logoutUser();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertTheme(alertDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
    }
}