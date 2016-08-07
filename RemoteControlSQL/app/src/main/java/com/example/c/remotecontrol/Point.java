package com.example.c.remotecontrol;

import java.util.Date;
import java.util.UUID;

/**
 * Created by c on 2016-08-07.
 */
public class Point {
    private String mBeverage;
    private UUID mId;
    private Date mDate;
    private String mTel;

    public Point() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Point(UUID id) {
        mId = id;
        mDate = new Date();
    }

    @Override
    public String toString() {
        return "Point{" +
                "mTel='" + mTel + '\'' +
                ", mBeverage='" + mBeverage + '\'' +
                '}';
    }

    public String getBeverage() {
        return mBeverage;
    }

    public void setBeverage(String beverage) {
        mBeverage = beverage;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getTel() {
        return mTel;
    }

    public void setTel(String tel) {
        mTel = tel;
    }
}
