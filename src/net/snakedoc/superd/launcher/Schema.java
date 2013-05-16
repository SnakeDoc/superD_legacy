/*******************************************************************************
 *  Copyright 2013 Jason Sipula, Trace Hagan                                   *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package net.snakedoc.superd.launcher;

import net.snakedoc.jutils.io.ReadFileToString;

public class Schema {
	public String getSchema() {
		ReadFileToString readFile = new ReadFileToString();
		return cleanString(readFile.readFromFile("db/schema.sql"), "\\n");
	}
	public String cleanString(String input, String stripChars) {
		String output = "";
		String[] temp = input.split(stripChars);
		for (int i = 0; i < temp.length; i++) {
			output += temp[i];
		}
		return output;
	}
}
