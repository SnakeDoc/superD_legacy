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

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class Deleter extends JFrame {
    
    /**
     * Serial Version UID
     */
    private static final long serialVersionUID = 8653510235141881228L;
    
    final File[] duplicates;
    
    // constructor
    public Deleter(File[] duplicates) {
        this.duplicates = duplicates;
    }
    
    // block constructor that will break gui
    public Deleter() {
        throw new IllegalArgumentException
            ("Invalid Constructor! Correct use: new Deleter(File[] duplicates)");
    }

    public void buildGUI(final File[] duplicates){
        //build button
 //       JButton jb = new JButton("Delete Selected Files");
        //build list box for duplicates
        final JList<File> files = new JList<File>(duplicates);
        //set width of list box
        files.setFixedCellWidth(450);
        //add action listener that handles deletion to jbutton jb
 /*       jb.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                //get selected files marked for deletion
                int[] listIndicies = files.getSelectedIndices();
                //build file array of selected files
                File[] filesSelected = new File[listIndicies.length];
                for (int i = 0; i < listIndicies.length; i++){
                    filesSelected[i]= duplicates[listIndicies[i]];
                }
                //delete files
                //TODO add deletion of deleted files from jList for public release; might need to switch to ArrayList or other data model
                for (int j = 0; j < filesSelected.length ; j++){
                    System.out.println(filesSelected[j].toString());
                    /*TODO UNCOMMENT THIS AFTER VERIFIED WORKING
                    filesSelected[j].delete();
                    */
  /*              }
            }
        });
        */
        JButton btnExt = new JButton(" Exit ");
        btnExt.addActionListener(new ActionListener(){
  
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
        
        //build frame for GUI
        JFrame frame = new JFrame("Select Files to Delete");
        //build panel for GUI components
        JPanel panel = new JPanel(new GridLayout());
        JPanel jPnl2 = new JPanel();
        
        // setup some radio buttons
        JRadioButton rdoBtnL = new JRadioButton("File L");
        JRadioButton rdoBtnR = new JRadioButton("File R");
        
        // setup button group
        ButtonGroup btnGrp1 = new ButtonGroup();
        btnGrp1.add(rdoBtnL);
        btnGrp1.add(rdoBtnR);
        
        // register action listener
        rdoBtnL.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e){
            System.out.println("Clicked radio btn");
            }
        });
        rdoBtnR.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e){
            System.out.println("Clicked radio btn");
            }
        });
        
        
        
        //set list to allow multiple selection
        files.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //add a scrollable jlist to panel
        panel.setLayout(new GridLayout(0 , 20));
       // panel.add(new JScrollPane(files));
        //add the button
 //       panel.add(jb);
        panel.add(rdoBtnL);
        panel.add(rdoBtnR);
       // jPnl2.add(btnExt);
        //set panel visible
        panel.setVisible(true);
       // jPnl2.setVisible(true);
        //set default action of frame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //add panel to frame
        frame.add(panel);
       // frame.add(jPnl2);
        //set frame size
        //frame.setSize(500,800);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        //frame.setUndecorated(true); // sets no frame, would need an exit button.
        //show GUI
        frame.setVisible(true);
    }
}
