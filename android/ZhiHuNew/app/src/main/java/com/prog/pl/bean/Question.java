package com.prog.pl.bean;

/**
 * Created by ads_123 on 2016/11/5.
 */

public class Question {
    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    private String topic;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    private String question;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    private String answer;

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }


    private int support;
    public String getQuestionuser() {
        return questionuser;
    }

    public void setQuestionuser(String questionuser) {
        this.questionuser = questionuser;
    }

    private String questionuser;
    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    private int comment;
}
