//Matthew Usnick
//Fall 2011
//Paint.java

import java.awt.Dimension;
import javax.swing.*;

//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@Paint@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
/** Task: Paint is the driver program for a simple paint program that allows
*         the user to draw with customized "brushes" or basic shapes. Colors can
*         be customized. The program supports saving and loading of projects. 
* 
* @author Matthew Usnick
*/
public class Paint 
{
    /////////////////////////////////main()/////////////////////////////////////
    /** Task: To create a new PaintWindow, which contains the paint application.
    * 
    * @param args is not used in the program.
    */
    public static void main(String args[])
    {
        //create a new thread to run the program
        SwingUtilities.invokeLater(new Runnable()
        {	
            //*****************************run()********************************
            /** Task: Starts the thread. 
            */
            public void run()
            {
                //set dimensions for the program
                int width = 1000;
                int height = 700;
				
                //create Dimension object to set min, max, and preferred width 
                //and height
                Dimension dim = new Dimension(width,height);
				
                //create new Window Frame
                PaintWindow win = new PaintWindow(width,height);
				
                //set min, max, and preferred dimensions
                win.setMaximumSize(dim);
                win.setMinimumSize(dim);
                win.setPreferredSize(dim);
				
            }//end run()
        }); //end Runnable anonymous class
        
    }//end main()
    
}//end Paint.java
