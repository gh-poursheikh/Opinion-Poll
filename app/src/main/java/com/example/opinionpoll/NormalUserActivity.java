package com.example.opinionpoll;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.opinionpoll.model.Person;
import com.example.opinionpoll.model.Product;
import com.example.opinionpoll.model.Rating;
import com.example.opinionpoll.utils.MyWebService;
import com.example.opinionpoll.utils.RecyclerViewItemAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NormalUserActivity extends AppCompatActivity {

    private static final String TAG = NormalUserActivity.class.getSimpleName();
    
    private ArrayList<Product> productList = new ArrayList<>();
    private ArrayList<Rating> ratingList = new ArrayList<>();
    private Person person;
    private RecyclerView.Adapter adapter;
    private TextView welcomeUserTextView;
    private boolean isLoading = false;
    private String firstName, lastName;
    private int personId;
    private static final int DEFAULT_RATING_NUMBER = 0;
    MyWebService webService = MyWebService.retrofit.create(MyWebService.class);

    private enum Status {SUCCESS, FAILURE}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_user);

        Button submitButton = findViewById(R.id.submitButton);
        getExtras();
        welcomeUserTextView = findViewById(R.id.welcomeUserTextView);
        welcomeUserTextView.setText(String.format("Dear %1$s %2$s!\nWelcome to Opinion Poll application!\nPlease give each item from 1 to 5 stars.", firstName, lastName));

        buildRecyclerView();
        pullProducts(0);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingsValid()) {
                    pushRatings();
                }
            }
        });
    }

    private void pullProducts(int productId) {
        Call<ArrayList<Product>> call = webService.getProduct(productId);
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(NormalUserActivity.this, "Network Error Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    for (Product item : response.body()) {
                        productList.add(item);
                        adapter.notifyDataSetChanged();
                        isLoading = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse from pullProducts: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Log.d(TAG, "onFailure from pullProducts: " + t.getMessage());
            }
        });
    }

    private void pushRatings() {
        Map<String, String> requestMap = new HashMap<>();
        Gson gson = new Gson();
        requestMap.put("params", gson.toJson(ratingList));

        Call<Map<String, String>> call = webService.setRating(requestMap);
        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(NormalUserActivity.this, "Network Error Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    if (response.body().get("success").equalsIgnoreCase("Insertion and Update successful.")) {
                        openDialog(Status.SUCCESS);
                    } else {
                        openDialog(Status.FAILURE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.d(TAG, "onFailure from pushRatings: " + t.getMessage());
            }
        });

    }

    private boolean ratingsValid() {
        if (ratingList.size() < productList.size()) {
            Toast.makeText(this, "Please rate all items!", Toast.LENGTH_SHORT).show();
            return false;
        }

        for(Rating item : ratingList) {
            if (item.getRatingNumber() == DEFAULT_RATING_NUMBER) {
                Toast.makeText(this, "Please give each item from 1 to 5 stars.", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void buildRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewItemAdapter(productList, ratingList, personId, this);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!isLoading) {
                    if (layoutManager.findLastVisibleItemPosition() == productList.size() - 1) {
                        pullProducts(productList.get(productList.size() - 1).getProductId());
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void getExtras() {
        try {
            person = getIntent().getParcelableExtra("EXTRA_SESSION_ID");
            personId = person.getPersonId();
            firstName = person.getFirstName();
            lastName = person.getLastName();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "getExtras: " + e.getMessage());
        }
    }

    private void openDialog(Status status) {
        String title = null;
        String message = null;
        switch (status) {
            case SUCCESS:
                title = "Congratulations!";
                message = "Your feedback was saved successfully. Thanks for your participation and sincerity.";
                break;
            case FAILURE:
                title = "Sorry!";
                message = "Something went wrong during insertion operation. We apologize for any inconvenience caused.\nPlease contact your system administrator.";
                break;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });
        alertDialog.show();
    }
}