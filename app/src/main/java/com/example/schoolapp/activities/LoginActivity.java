package com.example.schoolapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.schoolapp.Config.MyApplication;
import com.example.schoolapp.R;
import com.example.schoolapp.sync.HttpGetRequest;
import com.example.schoolapp.sync.HttpPostRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void loginMethod(View view)
    {
        Map<String, String> postData = new HashMap<>();
        EditText emailView = findViewById(R.id.email);
        EditText passwordView = findViewById(R.id.password);

        postData.put("Email", emailView.getText().toString());
        postData.put("Password", passwordView.getText().toString());
        HttpPostRequest task = new HttpPostRequest(postData);
        String token= "";
        try {
            String serverIpAddress = ((MyApplication) this.getApplication()).getServerIpAddress();
            token = task.execute(serverIpAddress + "api/login").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(token != "BadRequest")
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("token",token);
            editor.apply();

            Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            finish();
            startActivity(getIntent());
        }


    }
}
