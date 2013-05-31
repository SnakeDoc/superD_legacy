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

package net.snakedoc.superd.filescan;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.snakedoc.jutils.io.DirectoryScanner;
import net.snakedoc.jutils.io.HasherException;
import net.snakedoc.jutils.io.Hasher;
import net.snakedoc.superd.data.DeDupeObj;
import net.snakedoc.superd.data.DedupeSQL;

public class ScanFiles extends DirectoryScanner {
    
    private final static int BUFFER = 16384; // Buffer size in Bytes to use in hashing algorithm.
                                             // Has the potential to directly impact performance.
    
	public static List<File> scanFiles(String dir, DeDupeObj[] deDupeObj) throws IOException {
		//DedupeR.count++;
		System.out.println("SCANFILES CLASS");
		DedupeSQL sql = new DedupeSQL();
		Hasher getHash = new Hasher();
		DirectoryScanner dirScan = new DirectoryScanner();
		List<File> files = dirScan.getList(dir);
		System.out.println(files.size());
		for (int i = 0; i < files.size(); i++) {
//			DedupeR.count++;
			System.out.println(i);
			if (files.get(i).isFile() && files.get(i).canRead()) {
				String file = files.get(i).toString();
				String hash = "";
                try {
                    hash = getHash.getHash(files.get(i).toString(), "SHA-512", BUFFER);
                } catch (HasherException e) {
                    //TODO log out
                    e.printStackTrace();
                }
				sql.writeRecord(file, hash);
//				DedupeR.count++;
//				System.out.println("Processing: \n\tFile: " + deDupeObj[DedupeR.count].filepath
//						+ "\n\tHash: " + deDupeObj[DedupeR.count].filehash);
			}
		}
		return files;
	}
}
