// TODO : need to handle landscape changes
// TODO : prevent questions from being answered again
// TODO : JSON parser on a single item
// TODO : Persistent storage
// TODO : Combine JSON parser & persistent storage
// TODO : Merge JSON parser & persistent storage into Quiz

package com.bignerdranch.android.geoquiz;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.TextView;
import android.content.Intent;

public class QuizActivity extends Activity implements View.OnClickListener {

    // tag for debug printing & identification
    private static final String TAG = "QuizActivity";
    
    // key-value pairs to stash when activity is interrupted
    private static final String KEY_INDEX = "index";
    private static final String KEY_QUIZ_SCORE = "QuizScore";
    private static final String KEY_IS_CHEATER = "IsCheater";

    // GUI elements
    private TextView mTextViewQuestion;
    private RadioButton mRadioButtonA;
    private RadioButton mRadioButtonB;
    private RadioButton mRadioButtonC;
    private RadioButton mRadioButtonD;
    private Button mButtonNext;
    private Button mButtonCheat;

    // Class attributes
    private int     mCurrentIndex = 0;
    private double  mQuizScore = 0.00;
    private boolean mIsCheater = false;

    // temporary QuizItem data
    String[][]  QuizChoices = { {"A: Chien", "B: Pero", "C: Inu", "D: Hund"},
                                {"A: Chat", "B: Gato", "C: Neko", "D: Catze"},
                                {"A: Poisson", "B: Sakana", "C: Fisch", "D: Peces"},
                                {"A: Conejo", "B: Hase", "C: Lapin", "D: Usagi"},
                                {"A: Vogel", "B: Pajaro", "C: Oiseau", "D: Tori"} };

    private QuizItem[] mQuizItemArray = new QuizItem[]{
            new QuizItem("What is the Japanese word for dog?", QuizChoices[0], 'C'),
            new QuizItem("What is the Spanish word for cat?", QuizChoices[1], 'B'),
            new QuizItem("What is the French word for fish?", QuizChoices[2], 'A'),
            new QuizItem("What is the German word for rabbit?", QuizChoices[3], 'B'),
            new QuizItem("What is the Japanese word for bird?", QuizChoices[4], 'D')
    };

    ////////////////////
    // updateQuestion //
    ////////////////////

    private void updateQuestion() {

        QuizItem question = mQuizItemArray[mCurrentIndex];
        String question_text = question.toString();

        mTextViewQuestion.setText(question_text);

        String choice_A = question.getQuizItemChoices()[0];
        mRadioButtonA.setText(choice_A);

        String choice_B = question.getQuizItemChoices()[1];
        mRadioButtonB.setText(choice_B);

        String choice_C = question.getQuizItemChoices()[2];
        mRadioButtonC.setText(choice_C);

        String choice_D = question.getQuizItemChoices()[3];
        mRadioButtonD.setText(choice_D);

    } // updateQuestion

    /////////////////
    // checkAnswer //
    /////////////////

    private void checkAnswer() {

        boolean answerCorrect =  mQuizItemArray[mCurrentIndex].checkResult();
        int messageResId = 0;

        if (mIsCheater) {
            messageResId = R.string.toast_cheat;
        }

        else {
            if (answerCorrect) {
                messageResId = R.string.toast_correct;
                mQuizScore = mQuizScore + 1;
            }
            else {
                messageResId = R.string.toast_incorect;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();

    } // checkAnswer

    //////////////
    // onCreate //
    //////////////

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // start off the app by inflating the view
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_quiz);

        // wire up the TextView layout object
        // grab Question resource ID and set TextView to that

        mTextViewQuestion = (TextView) findViewById(R.id.text_question);

        // restore previous question index if available
        // also restore quiz score & cheat status
        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER, false);
            mQuizScore = savedInstanceState.getDouble(KEY_QUIZ_SCORE, 0.0);
        }

        // wire up Radio Button A
        mRadioButtonA = (RadioButton) findViewById(R.id.radio_A);
        mRadioButtonA.setOnClickListener(this);

        // wire up Radio Button B
        mRadioButtonB = (RadioButton) findViewById(R.id.radio_B);
        mRadioButtonB.setOnClickListener(this);

        // wire up Radio Button C
        mRadioButtonC = (RadioButton) findViewById(R.id.radio_C);
        mRadioButtonC.setOnClickListener(this);

        // wire up Radio Button D
        mRadioButtonD = (RadioButton) findViewById(R.id.radio_D);
        mRadioButtonD.setOnClickListener(this);

        // now that all radio buttons are wired,
        // can update the question text & radio text
        updateQuestion();

        // wire up the Next button
        // with anonymous inner class

        mButtonNext = (Button) findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mCurrentIndex == (mQuizItemArray.length - 1)) {
                    Intent i = new Intent(QuizActivity.this, ResultsActivity.class);
                    double QuizPercentage = (mQuizScore / ((double)(mQuizItemArray.length))*100.00);
                    i.putExtra(ResultsActivity.EXTRA_QUIZ_PERCENT, QuizPercentage);
                    startActivity(i);
                }
                else {
                    mCurrentIndex = (mCurrentIndex + 1);
                    mIsCheater = false;
                    updateQuestion();
                }

            } // onClick
        }); // onClickListener -- mButtonNext

        // wire up the Cheat button
        // with anonymous inner class

        mButtonCheat = (Button) findViewById(R.id.button_cheat);
        mButtonCheat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(QuizActivity.this, CheatActivity.class);
                char correctAnswer = mQuizItemArray[mCurrentIndex].getQuizItemAnswer();
                i.putExtra(CheatActivity.EXTRA_ANSWER_CHARACTER, correctAnswer);
                startActivityForResult(i, 0);
            } // onClick

        }); // onClickListener -- mButtonCheat

    } // onCreate

    /////////////////////
    // OnClickListener //
    /////////////////////

    public void onClick(View v) {

        switch (v.getId()) {
            case (R.id.radio_A):
                mQuizItemArray[mCurrentIndex].collectResponse('A');
                break;
            case (R.id.radio_B):
                mQuizItemArray[mCurrentIndex].collectResponse('B');
                break;
            case (R.id.radio_C):
                mQuizItemArray[mCurrentIndex].collectResponse('C');
                break;
            case (R.id.radio_D):
                mQuizItemArray[mCurrentIndex].collectResponse('D');
                break;
        }

        checkAnswer();

    } // onClick -- mRadioButton(A,B,C,D)

    //////////////////////
    // onActivityResult //
    //////////////////////

    // check if cheat answer was shown or not
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;
        mIsCheater = data.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOWN, false);
    }

    /////////////////////////
    // onSaveInstanceState //
    /////////////////////////

    // save index of current question
    // along with score & cheat status
    @Override
    public void onSaveInstanceState (Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");

        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(KEY_IS_CHEATER, mIsCheater);
        savedInstanceState.putDouble(KEY_QUIZ_SCORE, mQuizScore);

    } // onSaveInstanceState

    /////////////
    // onStart //
    /////////////

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    /////////////
    // onPause //
    /////////////

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    //////////////
    // onResume //
    //////////////

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    ////////////
    // onStop //
    ////////////

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    ///////////////
    // onDestroy //
    ///////////////

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }
} // QuizActivity