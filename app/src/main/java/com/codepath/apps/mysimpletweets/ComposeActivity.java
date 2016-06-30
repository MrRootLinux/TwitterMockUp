package com.codepath.apps.mysimpletweets;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.io.Serializable;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    TwitterClient client;
    EditText etTweet;
    Button  btnCancel;
    ImageView ivProfilePic;
    TextView tvCount;
    Tweet tweet;
    static int MAX_COUNT = 140;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = TwitterApplication.getRestClient();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        ivProfilePic = (ImageView) findViewById(R.id.ivProfileImage);
        etTweet = (EditText) findViewById(R.id.etTweet);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        tvCount = (TextView) findViewById(R.id.tvCount);


        // Attached Listener to Edit Text Widget
        etTweet.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            public void afterTextChanged(Editable s) {
                // Display Remaining Character with respective color
                int count = MAX_COUNT - s.length();
                tvCount.setText(Integer.toString(count));
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onCancel(View view){
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void onSubmitTweet(View view){

        etTweet = (EditText) findViewById(R.id.etTweet);

        client.postUpdate(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweet = Tweet.fromJSON(response);
                Intent intent = new Intent();
                intent.putExtra("tweet", Parcels.wrap(tweet));
                //RESULT_OK means success
                setResult(RESULT_OK, intent);
                finish();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
