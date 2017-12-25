package com.sgerges.epgviewsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sgerges.epgview.animations.DefaultLayoutAnimator;
import com.sgerges.epgview.core.AbsLayoutContainer;
import com.sgerges.epgview.core.EPGAdapter;
import com.sgerges.epgview.core.EPGView;
import com.sgerges.epgviewsample.model.ChannelData;
import com.sgerges.epgviewsample.model.MockDataProvider;
import com.sgerges.epgviewsample.model.ProgramData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinkedHashMap<ChannelData, List<ProgramData>> epgData = MockDataProvider.prepareMockData();

        final EPGView epgView = (EPGView) findViewById(R.id.epg_view);

        DefaultLayoutAnimator anim = (DefaultLayoutAnimator) epgView.getLayoutAnimator();
        anim.animateAllSetsSequentially = false;
        anim.animateIndividualCellsSequentially = false;

        epgView.requestFocus();

        final EPGDataAdapter adapter = new EPGDataAdapter();
        epgView.setAdapter(adapter);
        epgView.setmOnEPGItemSelectedListener(new EPGView.OnEPGItemSelectedListener() {
            @Override
            public void onProgramItemSelected(AbsLayoutContainer parent, int channelIndex, int programIndex) {
                Toast.makeText(MainActivity.this, "Program Clicked channel=" + channelIndex + ", program=" + programIndex, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChannelItemSelected(AbsLayoutContainer parent, int channelIndex) {
                Toast.makeText(MainActivity.this, "Channel Clicked channel=" + channelIndex, Toast.LENGTH_SHORT).show();
            }
        });

        epgView.postDelayed(new Runnable() {
            @Override
            public void run() {

                LinkedHashMap<ChannelData, List<ProgramData>> epgData = MockDataProvider.prepareMockData();
                adapter.updateDataWith(epgData);
                epgView.notifyDataSetChanged();
                epgView.post(new Runnable() {
                    @Override
                    public void run() {
                        epgView.scrollToNow(true);
                    }
                });
            }
        }, 1000);

    }

    private class EPGDataAdapter extends EPGAdapter<ChannelData, ProgramData> {

        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");

        @Override
        protected View getViewForChannel(ChannelData channel, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(MainActivity.this);
            }

            tv.setFocusable(false);
            tv.setBackgroundResource(R.drawable.channel_cell_bg);
            tv.setText(channel.getChannelName());
            tv.setPadding(9,9,9,9);
            tv.setGravity(Gravity.CENTER);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            return tv;
        }

        @Override
        protected View getViewForProgram(ProgramData program, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(MainActivity.this);
            }

            tv.setFocusable(false);
            tv.setPadding(9,9,9,9);
            tv.setBackgroundResource(R.drawable.program_cell_bg);
            String timings = dateFormat.format(new Date(program.getStartTime())) + " - " + dateFormat.format(new Date(program.getEndTime()));
            tv.setText(program.getTitle() + "\r\n" + timings);
            tv.setEllipsize(TextUtils.TruncateAt.END);
            return tv;
        }

        @Override
        public View getViewForTimeCell(Long time, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView != null) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(MainActivity.this);
            }

            tv.setFocusable(false);
            tv.setBackgroundResource(R.drawable.time_cell_bg);
            tv.setText(dateFormat.format(new Date(time)));
            tv.setTextColor(0xFFFFFFFF);
            tv.setPadding(9,9,9,9);
            tv.setGravity(Gravity.CENTER);
            tv.setEllipsize(TextUtils.TruncateAt.END);

            return tv;
        }

        @Override
        protected View getViewForNowLineHead(View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.now_line_head, parent, false);
            }
            return convertView;
        }

        @Override
        public long getStartTimeForProgramAt(int section, int position) {
            ProgramData program = getProgramAt(section, position);
            return program.getStartTime();
        }

        @Override
        public long getEndTimeForProgramAt(int section, int position) {
            ProgramData program = getProgramAt(section, position);
            return program.getEndTime();
        }

        @Override
        public long getViewStartTime() {
            return MockDataProvider.startOfToday;
        }

        @Override
        public long getViewEndTime() {
            return MockDataProvider.endOfToday;
        }
    }
}
