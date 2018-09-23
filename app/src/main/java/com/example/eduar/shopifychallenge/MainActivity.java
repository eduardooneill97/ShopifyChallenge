package com.example.eduar.shopifychallenge;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements TagListFragment.TagSelectionCallback{

    private static String TAG_LIST_FRAGMENT = "TagListFragment";
    private static String PRODUCT_LIST_FRAGMENT = "ProductListFragment";

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            Fragment tagListFragment = TagListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, tagListFragment)
                    .addToBackStack(TAG_LIST_FRAGMENT)
                    .commit();
        }
    }

    @Override
    public void onTagSelected(String tag) {
        Fragment productsListFragment = ProductListFragment.newInstance(tag);
        fm.beginTransaction()
                .replace(R.id.fragment_container, productsListFragment)
                .addToBackStack(PRODUCT_LIST_FRAGMENT)
                .commit();
    }

}
