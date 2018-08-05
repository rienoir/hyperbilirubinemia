package com.example.kevin.hyperbilirubinemia;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InstructActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruct);

        Toolbar toolbar = findViewById(R.id.tToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imageView = findViewById(R.id.instructImageView);
        InputStream is = null;
        try{
             is = getAssets().open("instruction.png");
        } catch(IOException e){
            e.printStackTrace();
        }

        imageView.setImageBitmap(BitmapFactory.decodeStream(is));

        //WebView webView = findViewById(R.id.instructWebView);
        //webView.loadUrl("file:///android_asset/" + "instruction.png");
        //loadInfoFile();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
/*
    private void loadInfoFile(){
        BufferedReader reader = null;

        try{
           //
            // TextView textView = (TextView) findViewById(R.id.instruction);
            reader = new BufferedReader(new InputStreamReader(getAssets().open("instruction.txt")));

            //read file
            String line;
            while((line = reader.readLine()) != null){
                textView.append(line);
                textView.append("\n");
            }
        } catch(IOException e){
            Log.d(MainActivity.DEBUGTAG, "Can't read the file");
            Toast.makeText(InstructActivity.this, getString(R.string.cant_read_file) , Toast.LENGTH_LONG).show();
        } finally{
            if(reader != null) {
                try {
                    reader.close();
                } catch(IOException e){
                    Log.d(MainActivity.DEBUGTAG, "Can't close the file");
                }
            }
        }
    }*/
}
