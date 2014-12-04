package com.example.jaredfranze.hellow;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.lang.Override;import java.lang.System;import java.util.Calendar;

/**
 * Created by izuchukwuelechi on 12/4/14.
 */
public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    Calendar anchorDate;

    public ScreenSlidePagerAdapter(FragmentManager fm, Calendar anchorDate) {
        super(fm);
        this.anchorDate = anchorDate;
    }

    public void setAnchorDate(Calendar anchorDate) {
        this.anchorDate = anchorDate;
    }

    @Override
    public Fragment getItem(int position) {
        System.out.println("frag " + position + " off "+ offset(position));

        Calendar week = Calendar.getInstance();
        week.setTimeInMillis(anchorDate.getTimeInMillis());
        week.add(Calendar.DAY_OF_YEAR, offset(position));

        long dateMillis = week.getTimeInMillis();
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle data = new Bundle();
        data.putLong("date", dateMillis);
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getCount() {
        return 53;
    }

    public int getInitialPosition() {
        return (getCount() / 2) + 1;
    }

    public int offset(int position) {
        return (position - getInitialPosition()) * 7;
    }
}