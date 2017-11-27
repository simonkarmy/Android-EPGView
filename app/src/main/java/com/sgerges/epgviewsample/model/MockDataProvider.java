package com.sgerges.epgviewsample.model;

import android.text.format.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Simon Gerges on 5/29/17.
 * <p>
 */

public class MockDataProvider {

    public static long startOfToday;
    public static long endOfToday;

    public static LinkedHashMap<ChannelData, List<ProgramData>> prepareMockData() {

        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);
        startOfToday = now.getTimeInMillis();

        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        now.set(Calendar.MILLISECOND, 999);
        endOfToday = now.getTimeInMillis();

        Random random = new Random();

        LinkedHashMap<ChannelData, List<ProgramData>> data = new LinkedHashMap<>();

        for (int channelIndex = 0; channelIndex < 80; channelIndex++) {

            ChannelData channelData = new ChannelData();
            channelData.setChannelName("Channel " + channelIndex);
            channelData.setChannelNumber(channelIndex);

            long timeProgress = startOfToday + (random.nextInt(60) *60*1000);

            List<ProgramData> channelPrograms = new ArrayList<>();
            while (timeProgress < endOfToday) {

                ProgramData program = new ProgramData();

                program.setTitle(channelData.getChannelName() + " " + "Program " + channelPrograms.size());
                program.setStartTime(timeProgress);

                //minimum 5 minutes and max 120 min
                int durationMin = 5 + random.nextInt(120);
                timeProgress += (durationMin * DateUtils.MINUTE_IN_MILLIS);
                program.setEndTime(timeProgress);

                channelPrograms.add(program);
            }

            data.put(channelData, channelPrograms);
        }

        return data;
    }
}
