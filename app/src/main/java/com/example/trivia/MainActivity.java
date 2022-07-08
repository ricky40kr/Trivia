package com.example.trivia;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBinding;
import com.example.trivia.model.Question;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String MESSAGE_ID = "sharedPref";
    private ActivityMainBinding binding;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int highestScore = 0;

    List<Question> questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        questionList = new Repository().getQuestion(questionArrayList -> {
            binding.questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
            updateCounter(questionArrayList);
            binding.scoreView.setText(String.format(getString(R.string.scoreDisp), score, questionList.size() * 2));
        });


        binding.buttonTrue.setOnClickListener(view -> {
                    checkAnswer(true);
                    updateQuestion();
                }
        );
        binding.buttonFalse.setOnClickListener(view -> {
                    checkAnswer(false);
                    updateQuestion();
                }
        );
        binding.buttonNext.setOnClickListener(view -> {
            currentQuestionIndex += 1 % questionList.size();
            updateQuestion();
        });

        SharedPreferences getSharedData = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
        int value = getSharedData.getInt("HS", 0);
        binding.highScore.setText("Highest Score : "+value);
    }

    private void checkAnswer(boolean userAnswer) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMessageId;
        if (userAnswer == answer) {
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
            incScore();
        } else {
            snackMessageId = R.string.incorrect_answer;
            shakeAnimation();
            decScore();
        }
        Snackbar.make(binding.cardView, snackMessageId, Snackbar.LENGTH_SHORT).show();
    }

    private void updateCounter(ArrayList<Question> questionArrayList) {
        binding.textViewOutOf.setText(String.format(getString(R.string.formatted), currentQuestionIndex + 1, questionArrayList.size()));
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.questionTextview.setText(question);
        updateCounter((ArrayList<Question>) questionList);
    }

    private void decScore() {
        if (score == 0) {
            Toast.makeText(MainActivity.this, "Already At Zero!", Toast.LENGTH_SHORT).show();
        } else {
            score -= 2;
            binding.scoreView.setText(String.format(getString(R.string.scoreDisp), score, questionList.size() * 2));
        }
    }

    private void incScore() {
        score = (score+2)%questionList.size()+2;
        binding.scoreView.setText(String.format(getString(R.string.scoreDisp), score, questionList.size() * 2));
        if (score > highestScore) {
            highestScore = score;
            binding.highScore.setText("Highest Score : " + highestScore);
            SharedPreferences sPref = getSharedPreferences(MESSAGE_ID, MODE_PRIVATE);
            SharedPreferences.Editor editor = sPref.edit();
            editor.putInt("HS", highestScore);  // Key-Value pair
            editor.apply(); // saving to disk!
        }

        if (score == questionList.size() * 2) {
            Toast.makeText(MainActivity.this, "Wow Full Score!", Toast.LENGTH_SHORT).show();
        }
    }

    private void fadeAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_animation);
        binding.cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextview.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}