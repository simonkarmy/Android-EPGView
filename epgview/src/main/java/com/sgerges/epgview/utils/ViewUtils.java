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
package com.sgerges.epgview.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;

import com.sgerges.epgview.core.FreeFlowItem;

import java.util.Map;


public class ViewUtils {
	public static FreeFlowItem getItemAt(Map<?, FreeFlowItem> frameDescriptors, int x, int y){
        FreeFlowItem returnValue = null;

        int maxZIndex = 0;
		for(FreeFlowItem item : frameDescriptors.values()) {
			if(item.frame.contains(x, y)) {

				//Only return item with max zIndex and clickable, because may be 2 cells are on top of each others
				if(returnValue == null || item.zIndex > maxZIndex) {
					if(item.clickable) {
						returnValue = item;
						maxZIndex = item.zIndex;
					}
				}
            }
	    }
		return returnValue;
	}
	
	public static Point getScreenSize(Activity activity){
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size;
	}
	
	public static float dipToPixels(Context context, float dipValue) {
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}
	
}
