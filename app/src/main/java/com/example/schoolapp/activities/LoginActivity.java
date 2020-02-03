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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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
        HttpPostRequest task = new HttpPostRequest(postData,"");
        HttpGetRequest getRequest = new HttpGetRequest();

        String token= "";
        String person="";
        try {
            String serverIpAddress = ((MyApplication) this.getApplication()).getServerIpAddress();
            token = task.execute(serverIpAddress + "api/login").get();
            JSONObject tokenObject = new JSONObject(token);
            String tokenDetail = tokenObject.getString("token");
            person=getRequest.execute(serverIpAddress+"api/GetPerson", tokenDetail).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(token != "BadRequest")
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("token",token);
            editor.putString("person",person);
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
