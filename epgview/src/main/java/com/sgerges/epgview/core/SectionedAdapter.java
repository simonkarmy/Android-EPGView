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

import android.view.View;
import android.view.ViewGroup;

public interface SectionedAdapter {

	long getItemId(int section, int position);

	View getItemView(int section, int position, View convertView, ViewGroup parent);

	View getHeaderViewForSection(int section, View convertView, ViewGroup parent);

	View getViewForTimeCell(Long time, View convertView, ViewGroup parent);

	int getNumberOfSections();

	Section getSection(int index);

	boolean shouldDisplaySectionHeaders();

}
