package com.sgerges.epgview.core;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon Gerges on 5/28/17.
 * <p>
 */

public abstract class EPGAdapter<C, P> {

    private List<Section<C, P>> sections;

    public EPGAdapter(LinkedHashMap<C, List<P>> channelToProgramsMap) {

        sections = new ArrayList<>(channelToProgramsMap.size());
        for (Map.Entry<C, List<P>> channelEntry : channelToProgramsMap.entrySet()) {
            Section<C, P> section = new Section<>();
            section.setHeaderData(channelEntry.getKey());
            section.setData(channelEntry.getValue());
            sections.add(section);
        }
    }

    View getItemView(int section, int position, View convertView, ViewGroup parent) {

        P program = sections.get(section).getDataAtIndex(position);
        return getViewForProgram(program, convertView, parent);
    }

    View getHeaderViewForSection(int section, View convertView, ViewGroup parent) {

        C channel = sections.get(section).getHeaderData();
        return getViewForChannel(channel, convertView, parent);
    }

    long getItemId(int channelIndex, int programIndex) {
        return channelIndex * 1000 + programIndex;
    }


    int getNumberOfChannels() {
        return sections.size();
    }

    Section getSection(int index) {
        if (index < sections.size() && index >= 0)
            return sections.get(index);

        return null;
    }

    View getOverlayViewForPrevPrograms(int color, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = new View(parent.getContext());
            convertView.setBackgroundColor(color);
        }
        return convertView;
    }

    protected P getProgramAt(int section, int position) {
        return sections.get(section).getDataAtIndex(position);
    }

    protected boolean shouldDisplaySectionHeaders() {
        return true;
    }

    protected boolean shouldDisplayTimeLine() {
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Abstract Methods
    ///////////////////////////////////////////////////////////////////////////

    protected abstract View getViewForChannel(C channel, View convertView, ViewGroup parent);
    protected abstract View getViewForProgram(P program, View convertView, ViewGroup parent);
    protected abstract View getViewForTimeCell(Long time, View convertView, ViewGroup parent);
    protected abstract View getViewForNowLineHead(View convertView, ViewGroup parent);

    public abstract long getStartTimeForProgramAt(int section, int position);
    public abstract long getEndTimeForProgramAt(int section, int position);

    public abstract long getViewStartTime();
    public abstract long getViewEndTime();
}
