package com.example.zee.galvanisemobile.foodmenu;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.zee.galvanisemobile.MainActivity;
import com.example.zee.galvanisemobile.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FoodMenuFragment extends Fragment {

    private RecyclerView mRecyclerView; //  private MenuRecyclerAdapter adapter;
    private FoodMenuItemAdapter mAdapter; //  private MenuRecyclerAdapter adapter;

    private static final String TAG = "RecyclerViewExample";
    private List<FoodItem> overallFeedsList;
    private List<FoodItem> filteredFeedsList = new ArrayList<FoodItem>();
    private int tabPosition = 0;

    private LinearLayout feedNotAvailable;

    public static FoodMenuFragment getInstance(int position) {

        FoodMenuFragment menuTabFragment = new FoodMenuFragment();
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

        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_menu, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.menu_list);
        feedNotAvailable = (LinearLayout)layout.findViewById(R.id.feedNotAvailable);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();

        if (bundle != null) {

            tabPosition = bundle.getInt("position");
        }

        overallFeedsList = ((MainActivity)getActivity()).getFeedsList();

        if (overallFeedsList.isEmpty()) {

            feedNotAvailable.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);

            Button reconnectButton = (Button)layout.findViewById(R.id.try_reconnect);
            reconnectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity)getActivity()).getJSONFeed();
                }
            });

        } else {

            feedNotAvailable.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            filterList();
        }

        mAdapter = new FoodMenuItemAdapter(getActivity().getApplicationContext(), filteredFeedsList);
        mRecyclerView.setAdapter(mAdapter);

        return layout;
    }


    private void filterList() {

        if (!filteredFeedsList.isEmpty())
            return;

        for (int i = 0; i < overallFeedsList.size(); i++) {

            FoodItem item = overallFeedsList.get(i);

            if (tabPosition == 0 || item.getCategory() == tabPosition) {

                filteredFeedsList.add(item);
            }
        }
    }

}
