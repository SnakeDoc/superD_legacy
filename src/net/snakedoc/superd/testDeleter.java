package net.snakedoc.superd;

import java.io.File;

/*USED TO TEST DELETER CLASS DURING CONSTRUCTION
*/

public class testDeleter {
    public static void main(String[] args){
        File[] files = new File[3];
        Deleter deleter = new Deleter();
        files[0]=new File("C:\\TESTDATA");
        files[1]=new File("C:\\TESTDATA1");
        files[2]=new File("C:\\TESTDATA2");
        deleter.buildGUI(files);
    }
}
