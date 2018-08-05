package com.example.kevin.hyperbilirubinemia;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        Toolbar toolbar = findViewById(R.id.tToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //loadInfoFile();

        ImageView imageView = findViewById(R.id.infoImageView);
        InputStream is = null;
        try{
            is = getAssets().open("info.png");
        } catch(IOException e){
            e.printStackTrace();
        }

        imageView.setImageBitmap(BitmapFactory.decodeStream(is));

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
            TextView textView = findViewById(R.id.textViewInfo);
            reader = new BufferedReader(new InputStreamReader(getAssets().open("info.txt")));

            //read file
            String line;
            while((line = reader.readLine()) != null){
                textView.append(line);
                textView.append("\n");
            }
        } catch(IOException e){
            Log.d(MainActivity.DEBUGTAG, "Can't read the file");
            Toast.makeText(InfoActivity.this, getString(R.string.cant_read_file) , Toast.LENGTH_LONG).show();
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
