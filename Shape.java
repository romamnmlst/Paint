//Matthew Usnick
//CS211S Final Project
//due: 12/14/11

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$Shape$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
/** Task: Shape is an object of a single brush stroke or shape. The type is 
*         defined when the object is created. The object contains the location
*         to be drawn, as well as additional information such as brush type,
*         brush size, color, and shape dimensions. This object is serializable.
* 
* @author Matthew Usnick
*/

//import statements-------------------------------------------------------------
import java.awt.Color;
import java.io.Serializable;

public class Shape implements Serializable
{
    //:::::::::::::::::::::::::::::::Variables::::::::::::::::::::::::::::::::::
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 580120126955952935L;
	//constant static variables for the painting mode of the object-------------
    final static int BRUSH = 0;
    final static int LINE = 1;
    final static int RECT = 2;
    final static int OVAL = 3;
    final static int FILL_RECT = 4;
    final static int FILL_OVAL = 5;
    

    final static int MIDDLE = 1;
    final static int END = 3;

	
    //the painting mode of this object------------------------------------------
    private int mode;
    private int paintMode;

    //the Color of this object--------------------------------------------------
    private Color color;
		
    //variables for Brush mode--------------------------------------------------
    //x and y location of the brush stroke
    private int x;
    private int y;
    //the brush shape
    private String brush;
    //the size of the brush
    private int fontSize;
	
    //variables for the Shape modes---------------------------------------------
    //start locations of the shape
    private int firstX;
    private int firstY;
    //end locations of the shape
    private int secondX;
    private int secondY;
	
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Constructors!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
    //default constructor-------------------------------------------------------
    public Shape()
    {
        mode = BRUSH;
        paintMode = END;
        color = Color.black;
        x = 0;
        y = 0;
        brush = "~";
        fontSize = 12;
        firstX = 0;
        firstY = 0;
        secondX = 0;
        secondY = 0;
    }
	
    //brush constructor---------------------------------------------------------
    //use this constructor when the object is a Brush stroke
    public Shape(int xloc, int yloc, Color c, String b, int fSize, 
                                                          int shapeMode, int pm)
    {
        //set brush variables
        mode = shapeMode;
        paintMode = pm;
        x = xloc;
        y = yloc;
        color = c;
        brush = b;
        fontSize = fSize;
		
        //set shape variables to 0
        firstX = 0;
        firstY = 0;
        secondX = 0;
        secondY = 0;
	}
    
    //shape constructor---------------------------------------------------------
    //use this constructor when the object is a Shape
    public Shape(int x1, int y1, int x2, int y2, Color c, int shapeMode)
    {
        //set shape variables
        mode = shapeMode;
        paintMode = 3;
        firstX = x1;
        firstY = y1;
        secondX = x2;
        secondY = y2;
        color = c;
		
        //set brush variables to 0
        x = 0;
        y = 0;
        brush = "";
        fontSize = 0;
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<Getters<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    public int getX()
    {
        return x;
    }
	
    public int getY()
    {
        return y;
    }
	
    public Color getColor()
    {
        return color;
    }
	
    public String getBrush()
    {
        return brush;
    }
	
    public int getFontSize()
    {
        return fontSize;
    }
	
    public int getFirstX()
    {
        return firstX;
    }
	
    public int getFirstY()
    {
        return firstY;
    }
	
    public int getSecondX()
    {
        return secondX;
    }
	
    public int getSecondY()
    {
        return secondY;
    }
	
    public int getMode()
    {
        return mode;
    }
    
    public int getPaintMode()
    {
    	return paintMode;
    }
    
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Setters>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    public void setX(int xloc)
    {
        x = xloc;
	}
	
    public void setY(int yloc)
    {
        y = yloc;
    }
	
    public void setColor(Color c)
    {
        color = c;
    }
	
    public void setBrush(String b)
    {
        brush = b;
    }
	
    public void setFontSize(int fSize)
    {
        fontSize = fSize;
    }
	
    public void setFirstX(int x1)
    {
        firstX = x1;
    }
	
    public void setFirstY(int y1)
    {
        firstY = y1;
    }
	
    public void setSecondX(int x2)
    {
        secondX = x2;
    }
	
    public void setSecondY(int y2)
    {
        secondY = y2;
    }
	
    public void setMode(int m)
    {
        mode = m;
    }
    
    public void setPaintMode(int pm)
    {
    	paintMode = pm;
    }
	
    //)))))))))))))))))))))))))))))))toString(((((((((((((((((((((((((((((((((((
    public String toString()
    {
        return ("mode:" + mode + " brush:" + brush + " color:" + color 
             + " fontSize:" + fontSize + " x:" + x + " y:" + y + " firstX:" 
             + firstX + " firstY:" + firstY + " secondX:" + secondX 
             + " secondY:" + secondY);
    }
	
}//end Shape class 