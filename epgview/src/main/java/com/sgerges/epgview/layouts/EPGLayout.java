/*******************************************************************************
 * Copyright 2013 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sgerges.epgview.layouts;

import android.graphics.Rect;
import android.text.format.DateUtils;

import com.sgerges.epgview.core.EPGAdapter;
import com.sgerges.epgview.core.FreeFlowItem;
import com.sgerges.epgview.core.Section;
import com.sgerges.epgview.core.SectionedAdapter;
import com.sgerges.epgview.utils.ViewUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EPGLayout extends FreeFlowLayoutBase implements FreeFlowLayout {

    public static final int TYPE_CHANNEL = 0;
    public static final int TYPE_CELL = 1;
    public static final int TYPE_NOW_LINE = 2;
    public static final int TYPE_TIME_BAR = 3;
    public static final int TYPE_TIME_BAR_NOW_HEAD = 4;
    public static final int TYPE_PREV_PROGRAMS_OVERLAY = 5;

    private static final String TAG = "EPGLayout";

    private Map<Object, FreeFlowItem> proxies = new HashMap<>();

    private EPGAdapter itemsAdapter;

    private EPGLayoutParams layoutParams;

    //to calculate the total end of the view
    private int maxEnd;

    private int gridTop = 0;

    public EPGLayout() {
        layoutParams = new EPGLayoutParams();
    }

    @Override
    public void setAdapter(SectionedAdapter adapter) {
        if(adapter instanceof EPGAdapter) {
            itemsAdapter = (EPGAdapter) adapter;
        } else {
            throw new IllegalArgumentException("EPGLayout only accepts EPGAdapter");
        }
    }

    @Override
    public void setLayoutParams(FreeFlowLayoutParams params) {
        if(params instanceof EPGLayoutParams)
            this.layoutParams = (EPGLayoutParams) params;
        else
            throw new IllegalArgumentException("EPGLayout can only use EPGLayoutParams");
    }

    public EPGLayoutParams getLayoutParams() {
        return layoutParams;
    }

    public void prepareLayout() {

        proxies.clear();

        if (itemsAdapter == null)
            return;

        gridTop = itemsAdapter.shouldDisplayTimeLine() ? layoutParams.timeLineHeight : 0;
        int programsStart = itemsAdapter.shouldDisplaySectionHeaders() ? layoutParams.channelCellWidth : 0;
        long viewStartTime = itemsAdapter.getViewStartTime();

        //========== Now line & Now Head
        if(viewStartTime < System.currentTimeMillis() && System.currentTimeMillis() < itemsAdapter.getViewEndTime()) {

            FreeFlowItem nowLineItem = new FreeFlowItem();
            nowLineItem.frame = prepareNowLineFrame();
            nowLineItem.type = TYPE_NOW_LINE;
            nowLineItem.zIndex = 1;
            nowLineItem.data = "NOW_LINE";
            proxies.put("NOW_LINE", nowLineItem);

            FreeFlowItem nowHeadItem = new FreeFlowItem();
            nowHeadItem.frame = prepareNowHeadFrame();
            nowHeadItem.type = TYPE_TIME_BAR_NOW_HEAD;
            nowHeadItem.zIndex = 4;
            nowHeadItem.data = "NOW_HEAD";
            proxies.put("NOW_HEAD", nowHeadItem);

            FreeFlowItem prevOverlayItem = new FreeFlowItem();
            prevOverlayItem.frame = preparePrevOverlayFrame();
            prevOverlayItem.type = TYPE_PREV_PROGRAMS_OVERLAY;
            prevOverlayItem.zIndex = 1;
            prevOverlayItem.data = "PREV_OVERLAY";
            proxies.put("PREV_OVERLAY", prevOverlayItem);
        }

        //========== Time Line

        Calendar currentTime = Calendar.getInstance();
        currentTime.setTimeInMillis(viewStartTime);

        //Make sure we are before the starting time by 1 hour, and at the head of the hour
        currentTime.add(Calendar.HOUR_OF_DAY, -1);
        currentTime.set(Calendar.MINUTE, 0);
        currentTime.set(Calendar.SECOND, 0);
        currentTime.set(Calendar.MILLISECOND, 0);

        while (currentTime.getTimeInMillis() - (30 * DateUtils.MINUTE_IN_MILLIS) < itemsAdapter.getViewEndTime()) {
            FreeFlowItem timeCell = new FreeFlowItem();
            timeCell.type = TYPE_TIME_BAR;
            timeCell.zIndex = 3;

            //Our cell should have the time Text in center
            // to achieve that, the full cell will be 15 min before, 15 min after
            int timeDiffMin = (int) ((currentTime.getTimeInMillis() - viewStartTime)/DateUtils.MINUTE_IN_MILLIS) - 15;
            Rect timeCellFrame = new Rect();
            timeCellFrame.left = programsStart + (timeDiffMin * layoutParams.minuteWidth);
            timeCellFrame.right = timeCellFrame.left + (30 * layoutParams.minuteWidth);//cell width is always 30 mins
            timeCellFrame.top = 0;
            timeCellFrame.bottom = timeCellFrame.top + layoutParams.timeLineHeight;

            timeCell.frame = timeCellFrame;
            timeCell.data = currentTime.getTimeInMillis();

            proxies.put(currentTime.getTimeInMillis(), timeCell);

            currentTime.add(Calendar.MINUTE, 30);
        }

        //========= Channels Headers

        for (int sectionIndex = 0; sectionIndex < itemsAdapter.getNumberOfSections(); sectionIndex++) {

            Section section = itemsAdapter.getSection(sectionIndex);

            if (itemsAdapter.shouldDisplaySectionHeaders()) {

                FreeFlowItem header = new FreeFlowItem();
                header.itemSection = sectionIndex;
                header.itemIndex = -1;
                header.zIndex = 2;

                Rect hframe = new Rect();
                hframe.left = 0;
                hframe.right = layoutParams.channelCellWidth;
                hframe.top = gridTop + (sectionIndex * layoutParams.channelRowHeight);
                hframe.bottom = hframe.top + layoutParams.channelRowHeight;

                header.frame = hframe;
                header.data = section.getHeaderData();
                header.type = TYPE_CHANNEL;
                proxies.put(header.data, header);
            }

            //========= Programs Rows

            for (int programIndex = 0; programIndex < section.getDataCount(); programIndex++) {

                FreeFlowItem descriptor = new FreeFlowItem();
                descriptor.itemSection = sectionIndex;
                descriptor.itemIndex = programIndex;

                Rect frame = new Rect();
                frame.left = programsStart + detectProgramLeft(sectionIndex, programIndex);
                frame.right = programsStart + detectProgramRight(sectionIndex, programIndex);

                frame.top = gridTop + (sectionIndex * layoutParams.channelRowHeight);
                frame.bottom = frame.top + layoutParams.channelRowHeight;

                int programEnd = frame.right;

                descriptor.frame = frame;
                descriptor.data = section.getDataAtIndex(programIndex);
                descriptor.zIndex = 0;
                proxies.put(descriptor.data, descriptor);

                descriptor.type = TYPE_CELL;

                //to calculate the total end of the view
                if (maxEnd < programEnd)
                    maxEnd = programEnd;
            }
        }
    }

    public Rect preparePrevOverlayFrame() {

        int programsStart = itemsAdapter.shouldDisplaySectionHeaders() ? layoutParams.channelCellWidth : 0;

        Rect nowLineFrame = new Rect();
        nowLineFrame.top = 0;
        nowLineFrame.left = 0;
        nowLineFrame.right = programsStart + detectNowLeft();
        nowLineFrame.bottom = gridTop + itemsAdapter.getNumberOfSections() * layoutParams.channelRowHeight;
        return nowLineFrame;
    }

    public Rect prepareNowLineFrame() {

        int programsStart = itemsAdapter.shouldDisplaySectionHeaders() ? layoutParams.channelCellWidth : 0;

        Rect nowLineFrame = new Rect();
        nowLineFrame.top = 0;
        nowLineFrame.left = programsStart + detectNowLeft();
        nowLineFrame.right = nowLineFrame.left + layoutParams.nowLineWidth;
        nowLineFrame.bottom = gridTop + itemsAdapter.getNumberOfSections() * layoutParams.channelRowHeight;
        return nowLineFrame;
    }

    public Rect prepareNowHeadFrame() {

        int programsStart = itemsAdapter.shouldDisplaySectionHeaders() ? layoutParams.channelCellWidth : 0;

        Rect nowLineFrame = new Rect();
        nowLineFrame.top = 0;
        nowLineFrame.left = programsStart + detectNowLeft();
        nowLineFrame.right = nowLineFrame.left + 100;
        nowLineFrame.bottom = nowLineFrame.top + layoutParams.timeLineHeight;
        return nowLineFrame;
    }

    private int detectNowLeft() {

        long viewStartTime = itemsAdapter.getViewStartTime();
        return (int) (((System.currentTimeMillis() - viewStartTime) / DateUtils.MINUTE_IN_MILLIS) * layoutParams.minuteWidth);
    }

    private int detectProgramLeft(int section, int index) {
        long programStartTime = itemsAdapter.getStartTimeForProgramAt(section, index);
        long viewStartTime = itemsAdapter.getViewStartTime();

        if(programStartTime < viewStartTime) {
            return 0;
        } else {
            return (int) (((programStartTime - viewStartTime) / DateUtils.MINUTE_IN_MILLIS) * layoutParams.minuteWidth);
        }
    }

    private int detectProgramRight(int section, int index) {

        long programEnd = itemsAdapter.getEndTimeForProgramAt(section, index);
        long viewStartTime = itemsAdapter.getViewStartTime();
        long viewEndTime = itemsAdapter.getViewEndTime();

        if(programEnd > viewEndTime && layoutParams.cutProgramsToEdges) {
            programEnd = viewEndTime;
        }

        return (int) (((programEnd - viewStartTime) / DateUtils.MINUTE_IN_MILLIS) * layoutParams.minuteWidth);
    }

    /**
     * This method it to decide which frames are going to be displayed.
     * <p>
     *     That by comparing all cells frame with the current viewPort frame
     *     If the cell frame is within the visible port, then display it.
     *     <br/>
     *     Also it keep in minds the cell type and will it stick Hor/Ver
     * {@inheritDoc}
     */
    @Override
    public Map<Object, FreeFlowItem> getItemProxies(int viewPortLeft, int viewPortTop) {
        HashMap<Object, FreeFlowItem> desc = new HashMap<>();
        for (FreeFlowItem fd : proxies.values()) {

            if(fd.type == TYPE_CHANNEL) {
                //in case of channel cell only check visibility for Y index
                //since in X index it will be always visible
                if (fd.frame.bottom > viewPortTop && fd.frame.top < viewPortTop + height) {
                    desc.put(fd.data, fd);
                }
//            } else if(fd.type == TYPE_PREV_PROGRAMS_OVERLAY) {
//                if (fd.frame.right > viewPortLeft && fd.frame.left < viewPortLeft + width) {
//                    desc.put(fd.data, fd);
//                }
            } else if(fd.type == TYPE_TIME_BAR_NOW_HEAD) {
                //in case of Time bar cell only check visibility for X index
                //since in Y index it will be always visible on top
                if (fd.frame.right > viewPortLeft && fd.frame.left < viewPortLeft + width) {
                    desc.put(fd.data, fd);
                }
            } else if(fd.type == TYPE_TIME_BAR) {
                //in case of Time bar cell only check visibility for X index
                //since in Y index it will be always visible on top
                if (fd.frame.right > viewPortLeft && fd.frame.left < viewPortLeft + width) {
                    desc.put(fd.data, fd);
                }
            } else {
                if (fd.frame.bottom > viewPortTop
                        && fd.frame.top < viewPortTop + height
                        && fd.frame.right > viewPortLeft
                        && fd.frame.left < viewPortLeft + width) {

                    desc.put(fd.data, fd);
                }
            }
        }

        return desc;
    }

    @Override
    public FreeFlowItem getItemAt(float x, float y) {
        return ViewUtils.getItemAt(proxies, (int) x, (int) y);
    }

    @Override
    public boolean horizontalScrollEnabled() {
        return true;
    }

    @Override
    public boolean verticalScrollEnabled() {
        return true;
    }

    /**
     * The content width here represented by
     *  Channel Column width + displayed duration
     *  We calc display duration by adding getting this data from adapter, and detect how long is the duration (1 day or more)
     *  then we multiply this duration to pixles for min
     * @return total width if the view
     */
    @Override
    public int getContentWidth() {

        return maxEnd;
    }

    @Override
    public int getContentHeight() {
        if (itemsAdapter == null || itemsAdapter.getNumberOfSections() <= 0) {
            return 0;
        }

        int sectionIndex = itemsAdapter.getNumberOfSections() - 1;
        Section s = itemsAdapter.getSection(sectionIndex);

        if (s.getDataCount() == 0)
            return 0;

        Object lastFrameData = s.getDataAtIndex(s.getDataCount() - 1);
        FreeFlowItem fd = proxies.get(lastFrameData);
        if (fd == null) {
            return 0;
        }
        return (fd.frame.top + fd.frame.height());
    }

    @Override
    public FreeFlowItem getFreeFlowItemForItem(Object data) {
        return proxies.get(data);
    }

    public FreeFlowItem getNowLineFreeFlowItem() {
        return proxies.get("NOW_LINE");
    }

    public static class EPGLayoutParams extends FreeFlowLayoutParams {
        public int channelCellWidth = 250;
        public int channelRowHeight = 250;
        public int minuteWidth = 20;
        public int nowLineWidth = 1;
        public int nowLineColor = 0xFFFF0000;
        public int timeLineHeight = 150;
        public boolean cutProgramsToEdges = true;
    }
}
