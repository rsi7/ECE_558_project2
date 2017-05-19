// TODO : Implement basic HomeActivity to start quiz
// TODO : Create background image & icon
// TODO : need to handle landscape changes
// TODO : use Spinner to select a different quiz

package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.Spinner;
import android.content.Intent;

/**
 * Created by riqbal on 5/18/2017.
 */

public class HomeActivity extends Activity {

    // tag for debug printing & identification
    private static final String TAG = "HomeActivity";

    // key-value pairs to stash when activity is interrupted

    // extra data being passed from HomeActivity --> QuizActivity

    // GUI elements
    private Spinner mSpinnerQuizzes;
    private Button mButtonStart;

    // Class attributes

    //////////////
    // onCreate //
    //////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_home);

        // grab the Spinner for use by HomeActivity
        mSpinnerQuizzes = (Spinner) findViewById(R.id.spinner_quizzes);

        // wire up the 'Start' button
        mButtonStart = (Button) findViewById(R.id.button_start);
        mButtonStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, QuizActivity.class);
                startActivity(i);
            } // onClick

        }); // onClickListener -- mButtonStart

    } // onCreate

} // HomeActivity