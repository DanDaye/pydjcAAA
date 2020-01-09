package com.wd.pydjc.pred.model;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class PredStatus {
    private static final long serialVersionUID = -1143398202986030484L;

    @JsonIgnore
    private String salt;

    private int featureAnalysis;
    private int modelAnalysis;
    private int svr;
    private int decisiontree;
    private int adaboost;
    private int extratrees;
    private int randomforest;
    private int grandientboosting;
    private int kneighbour;
    private int arima;
    private int gru;
    private int lstm;
    private int rnn;
    private int seq2seq;

    public int getFeatureAnalysis() {
        return featureAnalysis;
    }

    public void setFeatureAnalysis(int featureAnalysis) {
        this.featureAnalysis = featureAnalysis;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public int getModelAnalysis() {
        return modelAnalysis;
    }

    public void setModelAnalysis(int modelAnalysis) {
        this.modelAnalysis = modelAnalysis;
    }

    public int getSvr() {
        return svr;
    }

    public void setSvr(int svr) {
        this.svr = svr;
    }

    public int getDecisiontree() {
        return decisiontree;
    }

    public void setDecisiontree(int decisiontree) {
        this.decisiontree = decisiontree;
    }

    public int getAdaboost() {
        return adaboost;
    }

    public void setAdaboost(int adaboost) {
        this.adaboost = adaboost;
    }

    public int getExtratrees() {
        return extratrees;
    }

    public void setExtratrees(int extratrees) {
        this.extratrees = extratrees;
    }

    public int getRandomforest() {
        return randomforest;
    }

    public void setRandomforest(int randomforest) {
        this.randomforest = randomforest;
    }

    public int getGrandientboosting() {
        return grandientboosting;
    }

    public void setGrandientboosting(int grandientboosting) {
        this.grandientboosting = grandientboosting;
    }

    public int getKneighbour() {
        return kneighbour;
    }

    public void setKneighbour(int kneighbour) {
        this.kneighbour = kneighbour;
    }

    public int getArima() {
        return arima;
    }

    public void setArima(int arima) {
        this.arima = arima;
    }

    public int getGru() {
        return gru;
    }

    public void setGru(int gru) {
        this.gru = gru;
    }

    public int getLstm() {
        return lstm;
    }

    public void setLstm(int lstm) {
        this.lstm = lstm;
    }

    public int getRnn() {
        return rnn;
    }

    public void setRnn(int rnn) {
        this.rnn = rnn;
    }

    public int getSeq2seq() {
        return seq2seq;
    }

    public void setSeq2seq(int seq2seq) {
        this.seq2seq = seq2seq;
    }
}
