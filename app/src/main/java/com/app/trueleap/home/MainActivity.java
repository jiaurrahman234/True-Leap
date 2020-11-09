package com.app.trueleap.home;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.app.trueleap.R;
import com.app.trueleap.auth.LoginActivity;
import com.app.trueleap.base.BaseActivity;
import com.app.trueleap.databinding.ActivityMainBinding;
import com.app.trueleap.dialogFragment.NotifiactionDialogFragment;
import com.app.trueleap.external.Converter;
import com.app.trueleap.external.Utils;
import com.app.trueleap.home.studentsubject.HomeSubjectsFragment;
import com.app.trueleap.interfaces.responseCallback;
import com.app.trueleap.localization.ChangeLanguageActivity;
import com.app.trueleap.notification.NotificationActivity;
import com.app.trueleap.notification.NotificationModel;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, responseCallback {

    ActivityMainBinding binding;
    Toolbar toolbar;
    DrawerLayout drawer;
    MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        instance = this;
        loadFragment(new HomeSubjectsFragment());
        initNavigation();
        getNotifications(this);
    }
    private void initNavigation() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        displaySelectedScreen(item.getItemId());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem = menu.findItem(R.id.action_notification);
        menuItem.setIcon(Converter.convertLayoutToImage(this, localStorage.getNotificationCount(), R.drawable.ic_baseline_notifications_24));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(@NonNull ArrayList<NotificationModel> value) {
        if(value.size()>0) {
            ArrayList<NotificationModel> NewNotification = new ArrayList<>();
            for(int i=0 ; i < value.size() ; i++) {
                if(!value.get(i).getViewed()){
                    NewNotification.add(value.get(i));
                }
            }
            notificationDialog(NewNotification);
            invalidateOptionsMenu();
        }
    }

    private void notificationDialog(ArrayList<NotificationModel> notificationArrayList){
        NotifiactionDialogFragment alert = new NotifiactionDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("notificationlist",notificationArrayList);
        alert.setArguments(bundle);
        FragmentManager transaction = getSupportFragmentManager();
        alert.show(transaction,Utils.Notification_dialog_fragment);
        alert.setCancelable(false);
    }

}