package com.example.zee.galvanisemobile.cart;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zee.galvanisemobile.R;
import com.example.zee.galvanisemobile.payment.ReceiptItem;
import com.example.zee.galvanisemobile.orderitem.FinalisedOrderItemAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinalisedCartFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FinalisedOrderItemAdapter mAdapter;


    public FinalisedCartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_finalised_cart, container, false);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.finalised_cart_list);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FinalisedOrderItemAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        return layout;
    }

    public void modifyAdapterContents(ReceiptItem receiptItem) {

        mAdapter.updateList(receiptItem.getOrderItems());
    }


}
