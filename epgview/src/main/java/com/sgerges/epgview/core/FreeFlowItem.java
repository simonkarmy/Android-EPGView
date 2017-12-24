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
package com.sgerges.epgview.core;

import android.graphics.Rect;
import android.view.View;

import java.util.Comparator;

public class FreeFlowItem {
	public int itemIndex;
	public int itemSection;
	public Object data;
	public int zIndex;
	public Rect frame;
	public View view;
	public int type;
	public boolean clickable;

    public FreeFlowItem() {
    }

    public static FreeFlowItem clone(FreeFlowItem desc) {
		if (desc == null)
			return null;

		FreeFlowItem fd = new FreeFlowItem();
		fd.itemIndex = desc.itemIndex;
		fd.itemSection = desc.itemSection;
		fd.data = desc.data;
		fd.frame = new Rect(desc.frame);
		fd.zIndex = desc.zIndex;
		fd.view = desc.view;
		fd.type = desc.type;
		fd.clickable = desc.clickable;
		return fd;
	}

//	public boolean isMovingByTime() {
//		return type == EPGLayout.TYPE_NOW_LINE || type == EPGLayout.TYPE_TIME_BAR_NOW_HEAD;
//	}

	static class ZIndexComparator implements Comparator<FreeFlowItem> {

		@Override
		public int compare(FreeFlowItem o1, FreeFlowItem o2) {
			return o1.zIndex - o2.zIndex;
		}
	}
}
