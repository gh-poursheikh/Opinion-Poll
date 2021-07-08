package com.example.opinionpoll;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.opinionpoll.model.Person;
import com.example.opinionpoll.utils.MyWebService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;
    private Button loginButton;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username_EditText);
        passwordEditText = findViewById(R.id.password_EditText);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.login_Button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                login();
            }
        });
    }

    private void login() {
        progressBar.setVisibility(View.VISIBLE);
        MyWebService webService = MyWebService.retrofit.create(MyWebService.class);
        Call<Person> call = webService.login(username, password);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: response.code(): " + response.code());
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, getString(R.string.network_error_code) + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    Person person = response.body();
                    if (person.getPersonType().equalsIgnoreCase("Normal")) {
                        if (person.getVoted() == 0) {
                            Intent intent = new Intent(getBaseContext(), NormalUserActivity.class);
                            intent.putExtra("EXTRA_SESSION_ID", person);
                            progressBar.setVisibility(View.GONE);
                            startActivity(intent);
                            finish(); // When the users hit the back reportButton, they will not be able to return
                            // to the current activity because it has been killed off the Back Stack.)
                        } else {
                            usernameEditText.setText("");
                            passwordEditText.setText("");
                            usernameEditText.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, getString(R.string.sorry_you_have_already_voted), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(getBaseContext(), SystemUserActivity.class);
                        intent.putExtra("EXTRA_SESSION_ID", person);
                        progressBar.setVisibility(View.GONE);
                        startActivity(intent);
                        finish(); // When the users hit the back reportButton, they will not be able to return
                        // to the current activity because it has been killed off the Back Stack.)
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "onResponse: " + e.getMessage());
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    usernameEditText.requestFocus();
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, getString(R.string.sorry_record_not_found), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}