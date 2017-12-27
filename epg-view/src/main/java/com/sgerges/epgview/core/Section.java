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

import java.util.ArrayList;
import java.util.List;

public class Section<C, P> {

	protected List<P> data;
	protected C headerData;
	protected int selectedIndex = 0;

	public Section() {
		data = new ArrayList<>();
	}

	public List<P> getData() {
		return data;
	}
	
	public void clearData() {
		data.clear();
	}
	
	public P getDataAtIndex(int index) {
		return data.get(index);
	}

	public int getDataCount() {
		if(data != null) {
			return data.size();
		} else {
			return 0;
		}
	}

	public void setData(List<P> newData) {
	    if(newData != null) {
            this.data.clear();
            this.data.addAll(newData);
        }
	}

	public String getSectionTitle() {
		return headerData.toString();
	}
	
	public void setHeaderData(C headerData){
		this.headerData = headerData;
	}
	
	public C getHeaderData(){
		return headerData;
	}

	public void addItem(P item) {
		data.add(item);
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
}
