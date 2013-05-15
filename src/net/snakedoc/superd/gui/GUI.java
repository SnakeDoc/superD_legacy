package net.snakedoc.superd.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;

public class GUI extends JFrame {
	
	public static void main(String[] args) {
		GUI g = new GUI();
		g.doGUI();
	}

	public void doGUI() {
		
		Box box1 = new Box(BoxLayout.Y_AXIS);
		JPanel jPnl1 = new JPanel(new GridLayout(11, 2));
		JPanel jPnl2 = new JPanel();
		
		JRadioButton jRdoBtnAllL = new JRadioButton("Select Column");
		JRadioButton jRdoBtnAllR = new JRadioButton("Select Column");
		JRadioButton jRdoBtn1 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn2 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn3 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn4 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn5 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn6 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn7 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn8 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn9 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn10 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn11 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn12 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn13 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn14 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn15 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn16 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn17 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn18 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn19 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		JRadioButton jRdoBtn20 = new JRadioButton("/var/cache/yum/x86_64/18/fedora/packages/someFile.txt");
		
		jPnl1.add(jRdoBtnAllL);			jPnl1.add(jRdoBtnAllR);
		jPnl1.add(jRdoBtn1);			jPnl1.add(jRdoBtn11);
		jPnl1.add(jRdoBtn2);			jPnl1.add(jRdoBtn12);
		jPnl1.add(jRdoBtn3);			jPnl1.add(jRdoBtn13);
		jPnl1.add(jRdoBtn4);			jPnl1.add(jRdoBtn14);
		jPnl1.add(jRdoBtn5);			jPnl1.add(jRdoBtn15);
		jPnl1.add(jRdoBtn6);			jPnl1.add(jRdoBtn16);
		jPnl1.add(jRdoBtn7);			jPnl1.add(jRdoBtn17);
		jPnl1.add(jRdoBtn8);			jPnl1.add(jRdoBtn18);
		jPnl1.add(jRdoBtn9);			jPnl1.add(jRdoBtn19);
		jPnl1.add(jRdoBtn10);			jPnl1.add(jRdoBtn20);
		
		JButton jBtn1 = new JButton(" Dedupe ");
		JButton jBtn2 = new JButton(" Pause ");
		JButton jBtn3 = new JButton(" Save Database ");
		JButton jBtn4 = new JButton(" Exit ");
		
		jPnl2.setOpaque(true);
		jPnl2.setBackground(Color.BLACK);
		
		jPnl2.add(jBtn1);
		jPnl2.add(jBtn2);
		jPnl2.add(jBtn3);
		jPnl2.add(jBtn4);
		
		box1.add(jPnl1);
		box1.add(jPnl2);
		
		this.add(box1);
		
		this.setTitle(" superD - File Deduplicator ");
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setVisible(true);
		
	}
	
}
