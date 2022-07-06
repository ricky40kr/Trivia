package com.example.trivia.model;

import androidx.annotation.NonNull;

public class Question {
    private String statement;
    private boolean answerTrue;

    public Question() {
    }

    public Question(String statement, boolean answerTrue) {
        this.statement = statement;
        this.answerTrue = answerTrue;
    }

    public String getAnswer() {
        return statement;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }


    @NonNull
    @Override
    public String toString() {
        return "Question{" +
                "statement='" + statement + '\'' +
                ", answer=" + answerTrue +
                '}';
    }
}
