package com.example.brian.myrestaurants.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.brian.myrestaurants.Constants;
import com.example.brian.myrestaurants.R;
import com.example.brian.myrestaurants.adapters.FirebaseRestaurantViewHolder;
import com.example.brian.myrestaurants.models.Restaurant;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity {
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mRestaurantReference = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS).child(uid);
        setUpFirebaseAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

    private void setUpFirebaseAdapter() {
        mFirebaseAdapter
                = new FirebaseRecyclerAdapter<Restaurant, FirebaseRestaurantViewHolder>
                (Restaurant.class, R.layout.restaurant_list_item,
                        FirebaseRestaurantViewHolder.class, mRestaurantReference) {
            @Override
            protected void populateViewHolder(FirebaseRestaurantViewHolder viewHolder,
                                              Restaurant model, int position) {
                viewHolder.bindRestaurant(model);

            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }
}
