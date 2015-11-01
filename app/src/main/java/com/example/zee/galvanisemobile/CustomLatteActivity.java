package com.example.zee.galvanisemobile;

import android.app.Dialog;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomLatteActivity extends AppCompatActivity {

    public static final String TAG = "AndroidDrawing";
    private static String FIREBASE_URL = "https://galvanize-drawing.firebaseIO.com/";

    private com.example.zee.galvanisemobile.MenuItem customFood;

    private Firebase mRef;
    private Firebase mBoardsRef;
    private Firebase mSegmentsRef;
    private FirebaseDrawingAdapter<HashMap> mBoardListAdapter;
    private ValueEventListener mConnectedListener;
    private Toolbar toolbar;
    private String key = "";
    private EditText quantity;
    private Button returnToNormal;

    AQuery androidAQuery=new AQuery(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_list);
        setToolbar();

        getCustomizableFood();

        Firebase.setAndroidContext(this);
        mRef = new Firebase(FIREBASE_URL);
        setBoardsRef();
    }

    public void getCustomizableFood() {

        Intent i = getIntent();
        customFood = i.getParcelableExtra("customFoodObject");
    }

    private void setBoardsRef() {

        mBoardsRef = mRef.child("boardmetas");
        mSegmentsRef = mRef.child("boardsegments");
        mBoardsRef.keepSynced(true);
        SyncedBoardManager.setContext(this);
        SyncedBoardManager.restoreSyncedBoards(mSegmentsRef);

        if (customFood != null) {
            setUpBoardDetails();
        }

        if (customFood != null && customFood.getcustomArtId() == null) {
            createBoard();
        }
    }

    private void setUpBoardDetails() {

        returnToNormal = (Button)findViewById(R.id.return_to_normal);

        if (customFood.getcustomArtId() != null) {

            //key = customFood.getcustomArtId();
            //mBoardListAdapter.changeStringKey(mBoardsRef, key);

            TextView loadingImage = (TextView)findViewById(R.id.loadingImage);
            loadingImage.setVisibility(View.INVISIBLE);

            returnToNormal.setVisibility(View.VISIBLE);

            // view or edit an existing custom latte art
            ImageView imageView = (ImageView)findViewById(R.id.image);
            TextView foodNameText = (TextView)findViewById(R.id.foodNameText);

            androidAQuery.id(imageView).image(customFood.getThumbnail(), true, true);
            foodNameText.setText(customFood.getItemName());

        } else {
            // this is a new custom latte, load the drawing board
            returnToNormal.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {

        if (customFood != null) {
            setUpBoardDetails();
        }

        super.onResume();
    }

    public void setToolbar() {

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setUpDrawingAdapter() {

        if (customFood != null && customFood.getcustomArtId() != null) {
            key = customFood.getcustomArtId();
        }

        final ListView boardList = (ListView) this.findViewById(R.id.BoardList);
        mBoardListAdapter = new FirebaseDrawingAdapter<HashMap>(mBoardsRef, HashMap.class, R.layout.board_in_list, this, key) {
            @Override
            protected void populateView(View v, HashMap model) {

                // display the board's thumbnail if it is available
                ImageView thumbnailView = (ImageView) v.findViewById(R.id.board_thumbnail);
                if (model.get("thumbnail") != null){
                    try {
                        thumbnailView.setImageBitmap(DrawingActivity.decodeFromBase64(model.get("thumbnail").toString()));
                        thumbnailView.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    thumbnailView.setVisibility(View.INVISIBLE);
                }

                TextView priceText = (TextView) v.findViewById(R.id.priceText);
                priceText.setText("Price: $ " + String.format("%.2f", customFood.getPromoPrice()));

                TextView addCart2 = (TextView) v.findViewById(R.id.random_text);
                addCart2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        handleAddToCartDialog();

                    }
                });

            }
        };
        boardList.setAdapter(mBoardListAdapter);
        boardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBoard(mBoardListAdapter.getModelKey(position));
            }
        });
        mBoardListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                boardList.setSelection(mBoardListAdapter.getCount() - 1);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        // Set up a notification to let us know when we're connected or disconnected from the Firebase servers
        mConnectedListener = mRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean connected = (Boolean) dataSnapshot.getValue();

                if (connected) {
                    Toast.makeText(CustomLatteActivity.this, "Connected to Firebase", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Couldn't connect to firebase.");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // No-op
            }
        });

        setUpDrawingAdapter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Clean up our listener so we don't have it attached twice.
        mRef.getRoot().child(".info/connected").removeEventListener(mConnectedListener);
        mBoardListAdapter.cleanup();

    }

    private void createBoard() {
        // create a new board
        final Firebase newBoardRef = mBoardsRef.push();
        Map<String, Object> newBoardValues = new HashMap<>();
        newBoardValues.put("createdAt", ServerValue.TIMESTAMP);
        android.graphics.Point size = new android.graphics.Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        newBoardValues.put("width", size.x);
        newBoardValues.put("height", size.y);
        newBoardRef.setValue(newBoardValues, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase ref) {
                if (firebaseError != null) {

                    Log.e(TAG, firebaseError.toString());
                    throw firebaseError.toException();

                } else {
                    // once the board is created, start a DrawingActivity on it
                    key = newBoardRef.getKey();
                    customFood.setcustomArtId(key);
                    mBoardListAdapter.changeStringKey(ref, key);
                    openBoard(key);
                }
            }
        });
    }

    private void openBoard(String key) {

        Intent intent = new Intent(this, DrawingActivity.class);
        intent.putExtra("FIREBASE_URL", FIREBASE_URL);
        intent.putExtra("BOARD_ID", key);

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.i(TAG, "Selected item " + id);

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.action_cart) {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void handleAddToCartDialog() {

        final Dialog dialog = new Dialog(CustomLatteActivity.this);
        dialog.setContentView(R.layout.dialog_add_to_cart);
        dialog.setTitle("Select Quantity");

        Button plusButton = (Button) dialog.findViewById(R.id.plus_button);
        Button minusButton = (Button) dialog.findViewById(R.id.minus_button);

        Button confirmAdd = (Button) dialog.findViewById(R.id.confirm_add);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);

        quantity = (EditText) dialog.findViewById(R.id.quantity);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputFromDialog = quantity.getText().toString();

                if (inputFromDialog.isEmpty()) {
                    quantity.setText(String.valueOf(1), TextView.BufferType.EDITABLE);
                } else {
                    int currValue = Integer.parseInt(inputFromDialog);
                    quantity.setText(String.valueOf(currValue + 1), TextView.BufferType.EDITABLE);
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputFromDialog = quantity.getText().toString();

                if (inputFromDialog.isEmpty()) {
                    quantity.setText(String.valueOf(1), TextView.BufferType.EDITABLE);
                } else {
                    int currValue = Integer.parseInt(inputFromDialog);
                    if (currValue <= 1) {
                        quantity.setText(String.valueOf(1), TextView.BufferType.EDITABLE);
                    } else {
                        quantity.setText(String.valueOf(currValue - 1), TextView.BufferType.EDITABLE);
                    }
                }
            }
        });

        confirmAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String inputFromDialog = quantity.getText().toString();

                if (inputFromDialog.isEmpty() || Integer.parseInt(inputFromDialog) <= 0) {
                    alertEmptyItem();
                } else {
                    // create order item and add to shopping cart
                    OrderItem orderItem = new OrderItem(customFood, Integer.parseInt(inputFromDialog));
                    ShoppingCart.addOrderItem(orderItem);

                    sendPersistentBroadcastMessage();

                    dialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void onClick_returnToNormal(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void alertEmptyItem() {
        Toast.makeText(this, "Minimum quantity should be 1.", Toast.LENGTH_SHORT).show();
    }

    private void sendPersistentBroadcastMessage() {
        Intent myIntent = new Intent("GalvaniseBroadcast");
        myIntent.putExtra("AddtoCart", true);
        sendBroadcast(myIntent);
    }

}
