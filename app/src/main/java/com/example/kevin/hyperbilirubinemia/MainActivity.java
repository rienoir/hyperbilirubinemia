package com.example.kevin.hyperbilirubinemia;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String DEBUGTAG = "KVN";
    private Integer age;
    private double result;
    private String[] arrAges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.tToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        setButtonListener();
        arrAges = getResources().getStringArray(R.array.ages);
        age = 0;
        Intent intent = getIntent();
        int flag = intent.getIntExtra("ShowResult", 0);
        result = intent.getDoubleExtra("Result", 0.0);

        if (flag == 1) {
            ageDialogBuilder();
        }

        setButtonListener();
    }

    private int checkingRiskZone(double result, int age) {
        switch (age) {
            default:
            case 12:
                if (result < 4.0)
                    return 1;

                else if (result >= 4.0 && result < 5.0)
                    return 2;

                else if (result >= 5.0 && result < 7.0)
                    return 3;

                else
                    return 4;


            case 14:
                if (result < 4.25)
                    return 1;

                else if (result >= 4.25 && result < 5.5)
                    return 2;

                else if (result >= 5.5 && result < 7.1)
                    return 3;

                else
                    return 4;


            case 16:
                if (result < 4.5)
                    return 1;

                else if (result >= 4.5 && result < 5.6)
                    return 2;

                else if (result >= 5.6 && result < 7.25)
                    return 3;

                else
                    return 4;


            case 18:
                if (result < 4.8)
                    return 1;

                else if (result >= 4.8 && result < 5.9)
                    return 2;

                else if (result >= 5.9 && result < 7.5)
                    return 3;

                else
                    return 4;


            case 20:
                if (result < 4.9)
                    return 1;

                else if (result >= 4.9 && result < 6.0)
                    return 2;

                else if (result >= 6.0 && result < 7.6)
                    return 3;
                else
                    return 4;

            case 22:
                if (result < 4.95)
                    return 1;

                else if (result >= 4.95 && result < 6.0)
                    return 2;

                else if (result >= 6.0 && result < 7.7)
                    return 3;

                else
                    return 4;


            case 24:
                if (result < 5.0)
                    return 1;
                else if (result >= 5.0 && result < 6.1)
                    return 2;

                else if (result >= 6.1 && result < 7.8)
                    return 3;
                else
                    return 4;
            case 26:
                if (result < 5.2)
                    return 1;
                else if (result >= 5.2 && result < 6.7)
                    return 2;
                else if (result >= 6.7 && result < 8.5)
                    return 3;
                else
                    return 4;
            case 28:
                if (result < 5.8)
                    return 1;
                else if (result >= 5.8 && result < 7.0)
                    return 2;
                else if (result >= 7.0 && result < 9)
                    return 3;
                else
                    return 4;
            case 30:
                if (result < 6.0)
                    return 1;
                else if (result >= 6.0 && result < 7.8)
                    return 2;
                else if (result >= 7.8 && result < 9.8)
                    return 3;
                else
                    return 4;
            case 32:
                if (result < 6.2)
                    return 1;
                else if (result >= 6.2 && result < 8.0)
                    return 2;
                else if (result >= 8.0 && result < 10.0)
                    return 3;
                else
                    return 4;
            case 34:
                if (result < 6.85)
                    return 1;
                else if (result >= 6.85 && result < 8.5)
                    return 2;
                else if (result >= 8.5 && result < 10.7)
                    return 3;
                else
                    return 4;
            case 36:
                if (result < 7.0)
                    return 1;
                else if (result >= 7.0 && result < 9.0)
                    return 2;
                else if (result >= 9.0 && result < 11.0)
                    return 3;
                else
                    return 4;
            case 38:
                if (result < 7.5)
                    return 1;
                else if (result >= 7.5 && result < 9.50)
                    return 2;
                else if (result >= 9.5 && result < 11.8)
                    return 3;
                else
                    return 4;
            case 40:
                if (result < 7.9)
                    return 1;
                else if (result >= 7.9 && result < 10.0)
                    return 2;
                else if (result >= 10.0 && result < 12.1)
                    return 3;
                else
                    return 4;
            case 42:
                if (result < 8.0)
                    return 1;
                else if (result >= 8.0 && result < 10.05)
                    return 2;
                else if (result >= 10.05 && result < 12.2)
                    return 3;
                else
                    return 4;
            case 44:
                if (result < 8.1)
                    return 1;
                else if (result >= 8.1 && result < 10.1)
                    return 2;
                else if (result >= 10.1 && result < 12.5)
                    return 3;
                else
                    return 4;
            case 46:
                if (result < 8.3)
                    return 1;
                else if (result >= 8.3 && result < 10.4)
                    return 2;
                else if (result >= 10.4 && result < 12.9)
                    return 3;
                else
                    return 4;
            case 48:
                if (result < 8.8)
                    return 1;
                else if (result >= 8.8 && result < 10.9)
                    return 2;
                else if (result >= 10.9 && result < 13.1)
                    return 3;
                else
                    return 4;
            case 50:
                if (result < 8.9)
                    return 1;
                else if (result >= 8.9 && result < 11.0)
                    return 2;
                else if (result >= 11.0 && result < 13.5)
                    return 3;
                else
                    return 4;
            case 52:
                if (result < 9.0)
                    return 1;
                else if (result >= 9.0 && result < 11.3)
                    return 2;
                else if (result >= 11.3 && result < 13.9)
                    return 3;
                else
                    return 4;
            case 54:
                if (result < 9.1)
                    return 1;
                else if (result >= 9.1 && result < 11.8)
                    return 2;
                else if (result >= 11.8 && result < 14.0)
                    return 3;
                else
                    return 4;
            case 56:
                if (result < 9.3)
                    return 1;
                else if (result >= 9.3 && result < 12.0)
                    return 2;
                else if (result >= 12.0 && result < 14.7)
                    return 3;
                else
                    return 4;
            case 58:
                if (result < 9.5)
                    return 1;
                else if (result >= 9.5 && result < 12.2)
                    return 2;
                else if (result >= 12.2 && result < 14.9)
                    return 3;
                else
                    return 4;
            case 60:
                if (result < 9.7)
                    return 1;
                else if (result >= 9.7 && result < 12.5)
                    return 2;
                else if (result >= 12.5 && result < 15.1)
                    return 3;
                else
                    return 4;
            case 62:
                if (result < 9.9)
                    return 1;
                else if (result >= 9.9 && result < 12.8)
                    return 2;
                else if (result >= 12.8 && result < 15.2)
                    return 3;
                else
                    return 4;
            case 64:
                if (result < 10.1)
                    return 1;
                else if (result >= 10.1 && result < 12.9)
                    return 2;
                else if (result >= 12.9 && result < 15.4)
                    return 3;
                else
                    return 4;
            case 66:
                if (result < 10.3)
                    return 1;
                else if (result >= 10.3 && result < 13.0)
                    return 2;
                else if (result >= 13.0 && result < 15.7)
                    return 3;
                else
                    return 4;
            case 68:
                if (result < 10.6)
                    return 1;
                else if (result >= 10.6 && result < 13.05)
                    return 2;
                else if (result >= 13.05 && result < 15.8)
                    return 3;
                else
                    return 4;
            case 70:
                if (result < 10.95)
                    return 1;
                else if (result >= 10.95 && result < 13.1)
                    return 2;
                else if (result >= 13.1 && result < 15.9)
                    return 3;
                else
                    return 4;
            case 72:
                if (result < 11.1)
                    return 1;
                else if (result >= 11.1 && result < 13.4)
                    return 2;
                else if (result >= 13.4 && result < 16.0)
                    return 3;
                else
                    return 4;
            case 74:
                if (result < 11.1)
                    return 1;
                else if (result >= 11.1 && result < 13.7)
                    return 2;
                else if (result >= 13.7 && result < 16.0)
                    return 3;
                else
                    return 4;
            case 76:
                if (result < 11.2)
                    return 1;
                else if (result >= 11.2 && result < 13.9)
                    return 2;
                else if (result >= 13.9 && result < 16.1)
                    return 3;
                else
                    return 4;
            case 78:
                if (result < 11.3)
                    return 1;
                else if (result >= 11.3 && result < 14.0)
                    return 2;
                else if (result >= 14.0 && result < 16.2)
                    return 3;
                else
                    return 4;
            case 80:
                if (result < 11.4)
                    return 1;
                else if (result >= 11.4 && result < 14.1)
                    return 2;
                else if (result >= 14.1 && result < 16.3)
                    return 3;
                else
                    return 4;
            case 82:
                if (result < 11.5)
                    return 1;
                else if (result >= 11.5 && result < 14.5)
                    return 2;
                else if (result >= 14.5 && result < 16.6)
                    return 3;
                else
                    return 4;
            case 84:
                if (result < 11.6)
                    return 1;
                else if (result >= 11.6 && result < 14.8)
                    return 2;
                else if (result >= 14.8 && result < 16.9)
                    return 3;
                else
                    return 4;
            case 86:
                if (result < 11.8)
                    return 1;
                else if (result >= 11.8 && result < 14.9)
                    return 2;
                else if (result >= 14.9 && result < 16.95)
                    return 3;
                else
                    return 4;
            case 88:
                if (result < 11.95)
                    return 1;
                else if (result >= 11.95 && result < 14.95)
                    return 2;
                else if (result >= 14.95 && result < 17.0)
                    return 3;
                else
                    return 4;
            case 90:
                if (result < 12.0)
                    return 1;
                else if (result >= 12.0 && result < 15.0)
                    return 2;
                else if (result >= 15.0 && result < 17.0)
                    return 3;
                else
                    return 4;
            case 92:
                if (result < 12.1)
                    return 1;
                else if (result >= 12.1 && result < 15.0)
                    return 2;
                else if (result >= 15.0 && result < 17.1)
                    return 3;
                else
                    return 4;
            case 94:
                if (result < 12.2)
                    return 1;
                else if (result >= 12.2 && result < 15.1)
                    return 2;
                else if (result >= 15.1 && result < 17.2)
                    return 3;
                else
                    return 4;
            case 96:
                if (result < 12.3)
                    return 1;
                else if (result >= 12.3 && result < 15.2)
                    return 2;
                else if (result >= 15.2 && result < 17.3)
                    return 3;
                else
                    return 4;
            case 98:
                if (result < 12.5)
                    return 1;
                else if (result >= 12.5 && result < 15.3)
                    return 2;
                else if (result >= 15.3 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 100:
                if (result < 12.6)
                    return 1;
                else if (result >= 12.6 && result < 15.3)
                    return 2;
                else if (result >= 15.3 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 102:
                if (result < 12.7)
                    return 1;
                else if (result >= 12.7 && result < 15.4)
                    return 2;
                else if (result >= 15.4 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 104:
                if (result < 12.8)
                    return 1;
                else if (result >= 12.8 && result < 15.4)
                    return 2;
                else if (result >= 15.4 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 106:
                if (result < 12.9)
                    return 1;
                else if (result >= 12.9 && result < 15.5)
                    return 2;
                else if (result >= 15.5 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 108:
                if (result < 12.95)
                    return 1;
                else if (result >= 12.95 && result < 15.5)
                    return 2;
                else if (result >= 15.5 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 110:
                if (result < 13.0)
                    return 1;
                else if (result >= 13.0 && result < 15.6)
                    return 2;
                else if (result >= 15.6 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 112:
                if (result < 13.0)
                    return 1;
                else if (result >= 13.0 && result < 15.7)
                    return 2;
                else if (result >= 15.7 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 114:
                if (result < 13.0)
                    return 1;
                else if (result >= 13.0 && result < 15.8)
                    return 2;
                else if (result >= 15.8 && result < 17.6)
                    return 3;
                else
                    return 4;
            case 116:
                if (result < 13.05)
                    return 1;
                else if (result >= 13.05 && result < 15.9)
                    return 2;
                else if (result >= 15.9 && result < 17.6)
                    return 3;
                else
                    return 4;
            case 118:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.95)
                    return 2;
                else if (result >= 15.95 && result < 17.6)
                    return 3;
                else
                    return 4;
            case 120:
                if (result < 13.2)
                    return 1;
                else if (result >= 13.2 && result < 15.95)
                    return 2;
                else if (result >= 15.95 && result < 17.6)
                    return 3;
                else
                    return 4;
            case 122:
                if (result < 13.2)
                    return 1;
                else if (result >= 13.2 && result < 15.9)
                    return 2;
                else if (result >= 15.9 && result < 17.5)
                    return 3;
                else
                    return 4;
            case 124:
                if (result < 13.2)
                    return 1;
                else if (result >= 13.2 && result < 15.8)
                    return 2;
                else if (result >= 15.8 && result < 17.5)
                    return 3;
                else
                    return 4;
            case 126:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.7)
                    return 2;
                else if (result >= 15.7 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 128:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.65)
                    return 2;
                else if (result >= 15.65 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 130:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.6)
                    return 2;
                else if (result >= 15.6 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 132:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.55)
                    return 2;
                else if (result >= 15.55 && result < 17.4)
                    return 3;
                else
                    return 4;
            case 134:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.5)
                    return 2;
                else if (result >= 15.5 && result < 17.35)
                    return 3;
                else
                    return 4;
            case 136:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.45)
                    return 2;
                else if (result >= 15.45 && result < 17.35)
                    return 3;
                else
                    return 4;
            case 138:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.4)
                    return 2;
                else if (result >= 15.4 && result < 17.3)
                    return 3;
                else
                    return 4;
            case 140:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.35)
                    return 2;
                else if (result >= 15.35 && result < 17.3)
                    return 3;
                else
                    return 4;
            case 142:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.3)
                    return 2;
                else if (result >= 15.3 && result < 17.25)
                    return 3;
                else
                    return 4;
            case 144:
                if (result < 13.1)
                    return 1;
                else if (result >= 13.1 && result < 15.2)
                    return 2;
                else if (result >= 15.2 && result < 17.2)
                    return 3;
                else
                    return 4;
        }
    }

    private void ageDialogBuilder() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Pilih umur bayi (dalam jam)");
        dialogBuilder.setItems(R.array.ages, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(MainActivity.DEBUGTAG, "" + which);
                age = Integer.valueOf(arrAges[which]);
                alertDialogBuilder();
            }
        });

        dialogBuilder.create().show();
    }

    private void alertDialogBuilder() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        int zone = checkingRiskZone(result, age);
        
        dialogBuilder.setTitle("Hasil");

        dialogBuilder.setMessage("Jumlah kadar bilirubin: " + result + " mg/dl. Zona Risiko: " + zone);

        dialogBuilder.create().show();

    }

    private void setButtonListener() {
        ImageButton but1 = findViewById(R.id.detect_button);
        ImageButton but2 = findViewById(R.id.info_button);
        ImageButton but3 = findViewById(R.id.instruct_button);

        but1.setOnClickListener(this);
        but2.setOnClickListener(this);
        but3.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.detect_button):
                Intent detect = new Intent(this, DetectionActivity.class);
                startActivity(detect);
                Log.d(DEBUGTAG, "detect button clicked");
                break;
            case (R.id.info_button):
                Intent info = new Intent(this, InfoActivity.class);
                startActivity(info);
                Log.d(DEBUGTAG, "info button clicked");
                break;
            case (R.id.instruct_button):
                Intent instruct = new Intent(this, InstructActivity.class);
                startActivity(instruct);
                Log.d(DEBUGTAG, "instruct button clicked");
                break;
            default:
                break;
        }
    }

}
