package com.example.zee.galvanisemobile;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private RecyclerView mRecyclerView; //  private MenuRecyclerAdapter adapter;
    private MenuItemAdapter mAdapter; //  private MenuRecyclerAdapter adapter;

    private static final String TAG = "RecyclerViewExample";
    private List<MenuItem> feedsList = new ArrayList<MenuItem>();
    private int tabPosition = 0;

    public static MenuFragment getInstance(int position) {

        MenuFragment menuTabFragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        menuTabFragment.setArguments(args);
        return menuTabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if (bundle != null) {
            tabPosition = bundle.getInt("position");
        }

        // Downloading data from below url
        final String url = "http://galvanize.space/catalogs.json";
        new AsyncHttpTask().execute(url);

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_menu, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.menu_list);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MenuItemAdapter(getActivity().getApplicationContext(), feedsList);
        mRecyclerView.setAdapter(mAdapter);

        return layout;
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

                Log.d(TAG, e.getLocalizedMessage());
                result = 0;

            }

            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 1) {

                mAdapter = new MenuItemAdapter(getActivity().getApplicationContext(), feedsList);
                mRecyclerView.setAdapter(mAdapter);

            } else {

                Toast.makeText(getActivity(), "Couldn't fetch data. Please check your Internet connectivity.", Toast.LENGTH_SHORT).show();
            }
            //MenuFragment.this.onDataLoaded();
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);

            JSONArray posts = response.optJSONArray("items");
            feedsList = new ArrayList<>();

            for (int i = 0; i < posts.length(); i++) {

                JSONObject post = posts.optJSONObject(i);
                MenuItem item = new MenuItem();
                item.setCategoryViaName(post.optString("category"));

                if (tabPosition == 0 || item.getCategory() == tabPosition) {

                    item.setId(post.optInt("id"));
                    item.setItemName(post.optString("name"));
                    item.setThumbnail(post.optString("image"));
                    item.setPromoPrice(post.optInt("price"));
                    item.setItemDesc(post.optString("description"));
                    feedsList.add(item);
                }
            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }


}
