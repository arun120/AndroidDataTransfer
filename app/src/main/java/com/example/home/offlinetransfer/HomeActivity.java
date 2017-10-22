package com.example.home.offlinetransfer;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {



    FragmentPagerAdapter adapterViewPager;
    Bundle args;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);







        ViewPager mViewPager;
        mViewPager=(ViewPager) findViewById(R.id.pager);

        args=new Bundle();




        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),args,HomeActivity.this);
        mViewPager.setAdapter(adapterViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);







    }









    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private final int NUM_ITEMS = 2;
        Bundle args;
        private Context context;

        public MyPagerAdapter(FragmentManager fragmentManager, Bundle a, Context context) {

            super(fragmentManager);
            this.context=context;
            args=a;
        }

        // Returns total number of pages
        @Override public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override   public android.support.v4.app.Fragment getItem(int position) {

            android.support.v4.app.Fragment fragment;

            switch (position) {


                case 0:                 // Fragment # 0 - This will show FirstFragment

                    fragment=new WifiFragment();
                    break;
                case 1:
                    fragment=new DTMFFragment();
                    break;

                default:
                    return null;
            }
            fragment.setArguments(args);

            return fragment;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {


                case 0:
                    return "WiFi";

                case 1:
                    return "DTMF";



                default:
                    return null;
            }
        }

    }







}
