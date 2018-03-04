package com.example.omriakerman.goigahf2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.lang.reflect.Field;

public class StudentHomeScreenActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private BottomNavigationView navigation;
    private TabsPagerAdapter mTabsPagerAdapter;
    private ViewPager viewPager;
    private Fragment containerFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_history:
                    myToolbar.setTitle(R.string.title_history);
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_calendar:
                    myToolbar.setTitle(R.string.title_calendar);
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_profile:
                    myToolbar.setTitle(R.string.title_profile);
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_chat:
                    myToolbar.setTitle(R.string.title_chat);
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home_screen);

        myToolbar = findViewById(R.id.toolbar);
        myToolbar.setTitle("Student");
        //toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));
        setSupportActionBar(myToolbar);

        mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.studentContainer);
        viewPager.setAdapter(mTabsPagerAdapter);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);
        navigation.setSelectedItemId(R.id.navigation_profile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int selectedItemId = navigation.getSelectedItemId();
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.student_home_screen, menu); //this is the default toolbar menu

        if(selectedItemId == R.id.navigation_history){
            //fetch toolbar menu for history tab
        } else if(selectedItemId == R.id.navigation_calendar) {
            //fetch toolbar menu for calendar tab
        } else if(selectedItemId == R.id.navigation_profile){
            //fetch toolbar menu for profile tab
        } else if(selectedItemId == R.id.navigation_chat){
            //fetch toolbar menu for chat tab
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_logout){
            StartingActivity.connectedAs=-1;
            startActivity(new Intent(this, StartingActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @SuppressLint("RestrictedApi")
    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new StudentHistoryFragment();
                case 1:
                    return new StudentCalendarFragment();
                case 2:
                    return new StudentProfileFragment();
                case 3:
                    return new ChatFragment();
                default:
                    return new NextLessonsFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }
    }


}
