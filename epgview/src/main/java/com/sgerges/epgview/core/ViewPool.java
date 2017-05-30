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

import android.util.SparseArray;
import android.view.View;

import java.util.ArrayList;

public class ViewPool {

	private SparseArray<ArrayList<View>> viewPool;

    public ViewPool() {
        this.viewPool = new SparseArray<>();
    }

    public void returnViewToPool(View view, int viewType) {

		ArrayList<View> typePool = viewPool.get(viewType);

		if (typePool == null) {
			typePool = new ArrayList<>();
			viewPool.put(viewType, typePool);
		}
		typePool.add(view);
	}

	public View getViewFromPool(int viewType) {
		if (viewPool.get(viewType) == null || viewPool.get(viewType).size() == 0)
			return null;

		return viewPool.get(viewType).remove(0);
	}

}
