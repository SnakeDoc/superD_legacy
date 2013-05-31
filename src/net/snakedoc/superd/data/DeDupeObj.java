/*******************************************************************************
 *  Copyright (c) 2013 superD contributors, snakedoc.net and others            *
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

package net.snakedoc.superd.data;

public class DeDupeObj {
	private String filepath = "";
	private String filehash = "";
	
	//setters
	public void setFilePath(String filepath) {
	    this.filepath = filepath;
	}
	
	public void setFileHash(String filehash) {
	    this.filehash = filehash;
	}
	
	// getters
	public String getFilePath() {
	    return this.filepath;
	}
	
	public String getFileHash() {
	    return this.filehash;
	}
}
