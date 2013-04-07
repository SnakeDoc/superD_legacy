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

package net.snakedoc.superd;

import java.io.File;
import java.io.IOException;

import com.almworks.sqlite4java.SQLiteStatement;
import com.vanomaly.jutils.DirectoryScanner;
import com.vanomaly.jutils.Hasher;

public class ScanFiles extends DirectoryScanner {
	public static File[] scanFiles(String dir, DeDupeObj[] deDupeObj, 
			SQLiteStatement st) throws IOException {
		//DedupeR.count++;
		System.out.println("SCANFILES CLASS");
		DeDupeSQL sql = new DeDupeSQL();
		Hasher getHash = new Hasher();
		File[] files = DirectoryScanner.getList(dir, st);
		System.out.println(files.length);
		for (int i = 0; i < files.length; i++) {
			DedupeR.count++;
			System.out.println(i);
			if (files[i].isFile() && files[i].canRead()) {
				String file = files[i].toString();
				String hash = getHash.getSHA256(files[i].toString());
				sql.sqlDB(st, file, hash);
				DedupeR.count++;
				System.out.println("Processing: \n\tFile: " + deDupeObj[DedupeR.count].filepath
						+ "\n\tHash: " + deDupeObj[DedupeR.count].filehash);
			}
		}
		return files;
	}
}
