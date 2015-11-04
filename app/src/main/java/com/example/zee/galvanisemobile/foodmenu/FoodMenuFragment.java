package com.example.zee.galvanisemobile.foodmenu;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private List<FoodItem> overallFeedsList;
    private List<FoodItem> filteredFeedsList = new ArrayList<FoodItem>();
    private int tabPosition = 0;

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
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();

        if (bundle != null) {

            tabPosition = bundle.getInt("position");
        }

        overallFeedsList = ((MainActivity)getActivity()).getFeedsList();

        if (!overallFeedsList.isEmpty()) {

            filterList();
        }

        mAdapter = new FoodMenuItemAdapter(getActivity().getApplicationContext(), filteredFeedsList);
        mRecyclerView.setAdapter(mAdapter);

        return layout;
    }


    private void filterList() {

        filteredFeedsList = new ArrayList<FoodItem>();

        for (int i = 0; i < overallFeedsList.size(); i++) {

            FoodItem item = overallFeedsList.get(i);

            if (tabPosition == 0 || item.getCategory() == tabPosition) {

                filteredFeedsList.add(item);
            }
        }
    }

}
