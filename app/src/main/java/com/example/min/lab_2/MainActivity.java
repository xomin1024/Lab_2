package com.example.min.lab_2;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    public final static String EXTRA_DATA = "com.example.min.lab_2.ABOUTDATA";
    static final private String TAG = "Umpire Buddy";
    private static final String SHARED_PREFS_KEY = "ReadOutsKey";
    private static final String T_OUTS_KEY = "WriteOutsKey";

    private int scount = 0;
    private int bcount = 0;
    private int total_outs = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The following will print to LogCat.
        Log.i(TAG, "Starting onCreate...");
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            total_outs = savedInstanceState.getInt("total_outs");
        }

        SharedPreferences settings = getSharedPreferences(SHARED_PREFS_KEY, 0);
        total_outs = settings.getInt(T_OUTS_KEY, -1);
        if (total_outs == -1)
            total_outs++;

        View strikeButton = findViewById(R.id.strike_button);

        // This class implements the onClickListener interface.
        // Passing 'this' to setOnClickListener means the
        //   onClick method in this class will get called
        //   when the button is clicked.
        strikeButton.setOnClickListener(this);

        View ballButton = findViewById(R.id.ball_button);
        ballButton.setOnClickListener(this);

        updateCount();
    }

    private void updateCount() {
        TextView strikes = (TextView)findViewById(R.id.strike_count);
        strikes.setText(Integer.toString(scount));

        TextView balls = (TextView)findViewById(R.id.ball_count);
        balls.setText(Integer.toString(bcount));

        TextView outs = (TextView)findViewById(R.id.outs_count);
        outs.setText(Integer.toString(total_outs));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.strike_button:
                // Start count over if user tries to increment
                //   beyond 3.
                if (scount == 2) {
                    scount++;
                    // Builder is an inner class so we have to qualify it
                    // with its outer class: AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Three strikes - Batter out.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            scount = 0;
                            bcount = 0;
                            total_outs++;
                            // Write total_outs to persistent storage
                            SharedPreferences settings = getSharedPreferences(SHARED_PREFS_KEY, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putInt(T_OUTS_KEY, total_outs);
                            // Commit the edits
                            editor.commit();
                            // Note, you have to call update count here because.
                            // the call builder.show() below is non blocking.
                            updateCount();
                        }
                    });
                    builder.show();
                }
                else {
                    scount++;
                }
                break;

            case R.id.ball_button:
                // Start count over if user tries to increment
                //   beyond 4.
                if (bcount == 3) {
                    bcount++;
                    // Builder is an inner class so we have to qualify it
                    // with its outer class: AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Four balls - Batter walked.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("RESET", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            scount = 0;
                            bcount = 0;
                            // Note, you have to call update count here because.
                            //   the call builder.show() below is non blocking.
                            updateCount();
                        }
                    });
                    builder.show();
                }
                else {
                    bcount++;
                }
                break;
        }
        updateCount();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.reset:
                bcount = 0;
                scount = 0;
                updateCount();
                return true;
            case R.id.about:
                Intent intent = new Intent(this, About.class);
                intent.putExtra(EXTRA_DATA, "extra data or parameter you want to pass to activity");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Lifecycle methods follow

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop()");
    }

    // Note that onDestroy() may not be called if
    // the need for RAM is urgent (e.g., an incoming
    // phone call), but the activity will still be
    // shut down. Consequently, it's a good idea
    // to save state that needs to persist between
    // sessions in onPause().
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
    }

    // "icicle" is sometimes used as the name of the
    // parameter because onSaveInstanceState() used to
    // be called onFreeze().
    // The Bundle updated here is the same one passed
    //   to onCreate() above.
    // This method isn't guaranteed to be called. If it
    //   is critical that data be saved, save it in
    //   persistent storage in onPause() rather than here
    //   because this method will not be called in every
    //   situation as described in its documentation.
    // Summary: save data that should persist while the
    //   application is running here. Save data that should
    //   persist between application runs in persistent storage.
    @Override
    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);

        Log.i(TAG, "onSaveInstanceState()");
        icicle.putInt("total_outs", total_outs);
    }

    // Note, by convention most apps restore state in onCreate()
    // This method isn't used often.
    @Override
    protected void onRestoreInstanceState(Bundle icicle) {
        super.onRestoreInstanceState(icicle);
        Log.i(TAG, "onRestoreInstanceState()");
    }
}
