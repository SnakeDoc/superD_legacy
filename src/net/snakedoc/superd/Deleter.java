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

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Deleter {
//TODO BUILD DELETER CLASS

//TODO build GUI using jlist to select files to delete. Need list of duplicates
    public static void buildGUI(File[] duplicates){
        JButton jb = new JButton("Delete Selected Files");
        JFrame frame = new JFrame("Select Files to Delete");
        JPanel panel = new JPanel();
        JList files = new JList(duplicates);
        files.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        panel.add(new JScrollPane(files));
        panel.add(jb);
        panel.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.setSize(300,800);
        frame.setVisible(true);
    }
    public Deleter(){

    }

    public static void deleteFile(File[] files){
        for (File file : files){
            file.delete();
        }
    }



}
