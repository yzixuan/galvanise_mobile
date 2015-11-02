package com.example.zee.galvanisemobile;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.zee.galvanisemobile.cart.CartActivity;
import com.example.zee.galvanisemobile.cart.ShoppingCart;
import com.example.zee.galvanisemobile.estimote.CafeBeacon;
import com.example.zee.galvanisemobile.foodmenu.FoodItem;
import com.example.zee.galvanisemobile.foodmenu.FoodMenuFragment;
import com.example.zee.galvanisemobile.navigation.NavigationDrawerFragment;
import com.example.zee.galvanisemobile.tabs.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NavigationDrawerFragment drawerFragment;
    private Toolbar toolbar;
    private CafeBeacon cafeBeacon;
    private ViewPager mPager;
    private SlidingTabLayout mTabs;
    private List<FoodItem> feedsList = new ArrayList<FoodItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setNavigationDrawer();
        getJSONFeed();

        cafeBeacon = new CafeBeacon(this);
        cafeBeacon.setUpBeaconManager();
    }

    public List<FoodItem> getFeedsList() {
        return feedsList;
    }


    public void setToolbar() {

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setNavigationDrawer() {

        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

    }

    public void setTabs() {

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(new MenuPagerAdapter(getSupportFragmentManager()));
        mTabs = (SlidingTabLayout)findViewById(R.id.tabs);
        mTabs.setViewPager(mPager);
    }

    @Override
    public void onNewIntent(Intent intent){
        Bundle extras = intent.getExtras();
        if(extras != null){
            if(extras.containsKey("NotificationMessage")) {
                if (ShoppingCart.getDiscount() >= 0.2) {
                    toastDiscount(ShoppingCart.getDiscount());
                }
                else {
                    cafeBeacon.handleBeaconDialog();
                }
            }
        }
    }

    private void toastDiscount(double discount) {
        Toast.makeText(this, "A " + Math.round(discount * 100) + "% discount has been included", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            startShareActivity("Galvanise Cafe", getResources().getString(R.string.social_share_text));
        }

        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void startShareActivity(String subject, String text) {
        try {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(android.content.Intent.EXTRA_TEXT, text);
            startActivity(intent);
        }
        catch(android.content.ActivityNotFoundException e) {

        }
    }

    class MenuPagerAdapter extends FragmentPagerAdapter {

        String[] tabs;

        public MenuPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        @Override
        public Fragment getItem(int position) {
            FoodMenuFragment menuTabFragment = FoodMenuFragment.getInstance(position);
            return menuTabFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 8;
        }
    }


    public void getJSONFeed() {

        // Downloading data from below url
        final String url = "http://galvanize.space/catalogs.json";
        new AsyncHttpTask().execute(url);
    }

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;
            HttpURLConnection urlConnection;

            try {

                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                int statusCode = urlConnection.getResponseCode();

                // 200 represents HTTP OK (SUCCESS)
                if (statusCode == 200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = r.readLine()) != null) {

                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1; // Successful
                    return result;

                } else {

                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {

                result = 0;

            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {

                setTabs();

            } else {

                Toast.makeText(MainActivity.this, "Couldn't fetch data. Please check your Internet connectivity.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);

            JSONArray posts = response.optJSONArray("items");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {

                JSONObject post = posts.optJSONObject(i);
                FoodItem item = new FoodItem();
                item.setCategoryViaName(post.optString("category"));

                item.setId(post.optInt("id"));
                item.setItemName(post.optString("name"));
                item.setThumbnail(post.optString("image"));
                item.setPromoPrice(post.optInt("price"));
                item.setItemDesc(post.optString("description"));
                feedsList.add(item);

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }
}
