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
import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Deleter {

    public static void buildGUI(final File[] duplicates){
        JButton jb = new JButton("Delete Selected Files");
        final JList files = new JList(duplicates);
        jb.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int[] listIndicies = files.getSelectedIndices();
                File[] filesSelected = new File[listIndicies.length];
                for (int i = 0; i < listIndicies.length; i++){
                    filesSelected[i]= duplicates[listIndicies[i]];
                }
                for (int j = 0; j < filesSelected.length ; j++){
                    System.out.println(filesSelected[j].toString());
                }
            }
        });
        JFrame frame = new JFrame("Select Files to Delete");
        JPanel panel = new JPanel();

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
}
