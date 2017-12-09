package com.hieuapp.lunch;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.hieuapp.lunch.dishes.ExploreDishesFragment;
import com.hieuapp.lunch.saved.SavedFragment;
import com.hieuapp.lunch.ui.SearchActivity;
import com.hieuapp.lunch.user.ProfileFragment;
import com.hieuapp.lunch.util.AccountUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;


public class LunchIOActivity extends BaseActivity {
    private BottomNavigationView navigationView;

    private final String EXPLORE_FRAGMENT = "explore";
    private final String SAVED_FRAGMENT = "saved";
    private final String PROFILE_FRAGMENT = "profile";

    public static final Map<String, String> session = new HashMap<>();

    public static Tabs CURRENT_TAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_content);
        session.put("uid",AccountUtils.getFacebookId(this));

        showHashKey(this);

        ExploreDishesFragment dishesFragment = (ExploreDishesFragment)
                getSupportFragmentManager().findFragmentByTag(EXPLORE_FRAGMENT);
        if(dishesFragment == null){
            dishesFragment = new ExploreDishesFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_content, dishesFragment);
            transaction.commit();
        }

        navigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(navigationListener);
    }

    /**
     * processing switch bwt tabs
     */
    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment nextFragment;
                    FragmentTransaction transaction = LunchIOActivity.this.getSupportFragmentManager().beginTransaction();
                    switch (item.getItemId()) {
                        case R.id.explorer_tab:
                            if (CURRENT_TAB != Tabs.EXPLORER) {
                                if (LunchIOActivity.this.getSupportFragmentManager().getBackStackEntryCount() != 0) {
                                    Fragment currentFragment = LunchIOActivity.this.getSupportFragmentManager()
                                            .findFragmentById(R.id.frame_content);
                                    transaction.remove(currentFragment);
                                    LunchIOActivity.this.getSupportFragmentManager().popBackStack();
                                } else {
                                    nextFragment = new ExploreDishesFragment();
                                    transaction.replace(R.id.frame_content, nextFragment);
                                }
                            }
                            break;
                        case R.id.saved_tab:
                            if (CURRENT_TAB != Tabs.SAVED) {
                                nextFragment = new SavedFragment();
                                transaction.replace(R.id.frame_content, nextFragment);
                                if (LunchIOActivity.this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
                                    transaction.addToBackStack("test");
                                }
                            }
                            break;
                        case R.id.profile_tab:
                            if (CURRENT_TAB != Tabs.PROFILE) {
                                nextFragment = new ProfileFragment();
                                transaction.replace(R.id.frame_content, nextFragment);
                                if (LunchIOActivity.this.getSupportFragmentManager().getBackStackEntryCount() == 0) {
                                    transaction.addToBackStack("test");
                                }
                            }

                            break;
                    }

                    transaction.commit();

                    return true;
                }
            };

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
    }

    @Override
    public void onBackPressed() {
        if(CURRENT_TAB != Tabs.EXPLORER){
            FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_content);
            transaction.remove(fragment);
            transaction.commit();

        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId() == R.id.action_search){
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }

    }

    public void showHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.hieuapp.lunch",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                String sign= Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.e("KeyHash:", sign);
            }
            Log.d("KeyHash:", "****------------***");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
