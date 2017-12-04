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

public abstract class EPGAdapter<C, P> implements SectionedAdapter {

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

    @Override
    public View getItemView(int section, int position, View convertView, ViewGroup parent) {

        P program = sections.get(section).getDataAtIndex(position);
        return getViewForProgram(program, convertView, parent);
    }

    @Override
    public View getHeaderViewForSection(int section, View convertView, ViewGroup parent) {

        C channel = sections.get(section).getHeaderData();
        return getViewForChannel(channel, convertView, parent);
    }

    @Override
    public long getItemId(int section, int position) {
        return section * 1000 + position;
    }

    @Override
    public int getNumberOfSections() {
        return sections.size();
    }

    @Override
    public Section getSection(int index) {
        if (index < sections.size() && index >= 0)
            return sections.get(index);

        return null;
    }

    public P getProgramAt(int section, int position) {
        return sections.get(section).getDataAtIndex(position);
    }


    @Override
    public boolean shouldDisplaySectionHeaders() {
        return true;
    }

    public boolean shouldDisplayTimeLine() {
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////
    // Abstract Methods
    ///////////////////////////////////////////////////////////////////////////

    protected abstract View getViewForChannel(C channel, View convertView, ViewGroup parent);
    protected abstract View getViewForProgram(P program, View convertView, ViewGroup parent);

    public abstract long getStartTimeForProgramAt(int section, int position);
    public abstract long getEndTimeForProgramAt(int section, int position);

    public abstract long getViewStartTime();
    public abstract long getViewEndTime();
}
