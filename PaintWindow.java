//Matthew Usnick
//Fall 2011
//PaintWindow.java

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$PaintWindow$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
/** Task: PaintWindow is a Frame that contains all of the variables, components,
*         and classes used in the "Java Paint" program (Except for the Shape
*         class).
*
* @author Matthew Usnick
*/
public class PaintWindow extends JFrame 
{
    //:::::::::::::::::::::::::::::::Variables::::::::::::::::::::::::::::::::::
	//auto-generated serialVersionUID
	private static final long serialVersionUID = 1L;
	
	//inputed width and height for program--------------------------------------
    int currentWidth;
    int currentHeight;
	
    //Panels--------------------------------------------------------------------
    //Main Panel that components are added to
    PaintPanel pp;
    //Panel that contains brush select and brush size elements
    JPanel brushSelectPanel;
    //Panel that contains JColorChooser
    JPanel colorPanel;
    //Panel that contains brushSelectPanel and drawModePanel
    JPanel controlPanel;
    //Panel that contains optionPanel, colorPanel, brushSelectPanel, and 
    //controlPanel
    JPanel menuPanel;
    //Panel that contains Brush and Shape selection buttons
    JPanel drawModePanel;
    //Panel that contains clear, save, load, help, and exit buttons
    JPanel optionPanel;
	
    //PaintCanvas extends Canvas------------------------------------------------
    PaintCanvas canvas;
	
    //Labels--------------------------------------------------------------------
    JLabel brushShapeLabel;
    JLabel brushSizeLabel;
	
    //Buttons-------------------------------------------------------------------
    //option buttons
    
    JButton undoButton;
    JButton redoButton;
    JButton clearButton;	
    JButton saveButton;
    JButton loadButton;
    JButton helpButton;
    JButton exitButton;	

    //draw mode buttons
    JButton brushModeButton;
    JButton lineModeButton;
    JButton rectModeButton;
    JButton ovalModeButton;
	
    //Brush Field and Spinner---------------------------------------------------
    //brush text input field
    JTextField brushField;
	
    //spinner for brushSize
    JSpinner spinner;
	
    //Font Variables------------------------------------------------------------
    Font currentFont;
    int currentFontSize;
	
    //Brush Mode:--------------------------------------------------------------- 
    //0 = Shape.BRUSH 
    //1 = Shape.LINE
    //2 = Shape.RECT
    //3 = Shape.OVAL
    //4 = Shape.FILL_RECT
    //5 = Shape.FILL_OVAL
    int mode;
	
    //X/Y variables-------------------------------------------------------------
    //current x/y locations
    int currentX;
    int currentY;
	
    //x/y locations for shape tools
    int firstX;
    int firstY;
    int secondX;
    int secondY;
	
    //Color Variable------------------------------------------------------------
    Color currentColor;
	
    //Brush Character(s)--------------------------------------------------------
    String brush;
		
    //Drawing History-----------------------------------------------------------
    //adds a Shape object every time a brush stroke or shape is created
    ArrayList<Shape> history;
    
    Stack<Shape> redoStack;
	
    //Booleans------------------------------------------------------------------
    //clear action yes/no
    boolean clear;
	
    //tracks if there are unsaved changes to the painting
    boolean changesMade;
	
    //tracks if rect and oval mode buttons are normal, or fill
    //(true = normal, false = fill)
    boolean rectMode;
    boolean ovalMode;
    
	
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!Constructors!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	
    //----------------------------PaintWindow()---------------------------------
    /** Task: default constructor calls constructor with parameters
    */
    public PaintWindow()
    {
        //if no dimensions are given, call Window constructor with these values
        this(1000, 700);
    }//end default Window constructor
	
    //----------------------------PaintWindow()---------------------------------
    /** Task: Constructor with parameters that initializes the Window Frame,
    *         and adds all elements to it.
    *        
    * @param width is the width of the Frame
    * @param height is the height of the Frame        
    */
    public PaintWindow(int width, int height)
    {
        //size variables for the program
        currentWidth = width;
        currentHeight = height;
		
        //set size of PaintWindow object
        setSize(currentWidth, currentHeight);	
		
        //set window title
        setTitle("Java Paint");
		
        //set close option
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
        //build the panel (add all components)
        buildPanel(width, height);
		
        //add the panel to this PaintWindow 
        add(pp);
		
        //make visible
        setVisible(true);
		
        //turn off resize
        setResizable(false);
		
        //pack the Frame
        this.pack();
    }//end Window constructor with parameters
	
    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Methods%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    //*******************************buildPanel()*******************************
    /** Task: initializes all variables, sets up all listeners, and adds all 
    *         elements to their respective panels/frames
    * 
    * @param width is the width to be used
    * @param height is the height to be used
    */
    private void buildPanel(int width, int height)
    {		
        //set boolean variables 
        clear = false;
        rectMode = false;
        ovalMode = false;
        changesMade = false;
		
        //set default brush options
        brush = "~";
        currentColor = Color.BLACK;
        currentFontSize = 12;
        currentFont = new Font("Helvetica", Font.PLAIN, currentFontSize);
		
        //create history ArrayList 
        history = new ArrayList<Shape>();
        redoStack = new Stack<Shape>();
		
        //create optionPanel buttons--------------------------------------------
        undoButton = new JButton("Undo");
        undoButton.setForeground(Color.gray);
        undoButton.setToolTipText("Click to undo a paint action.");
        
        redoButton = new JButton("Redo");
        redoButton.setForeground(Color.gray);
        redoButton.setToolTipText("Click to redo a paint action.");
        
        clearButton = new JButton("Clear");
        clearButton.setToolTipText("Click to clear your current painting.");
		
        saveButton = new JButton("Save");
        saveButton.setToolTipText("Click to save your Java Paint project.");
		
        loadButton = new JButton("Load");
        loadButton.setToolTipText("Click to load a saved Java Paint project.");
		
        helpButton = new JButton("Help");
        helpButton.setToolTipText("Click to load a help file.");
		
        exitButton = new JButton("Exit");
        exitButton.setToolTipText("Click to exit Java Paint.");
		
        //create JColorChooser--------------------------------------------------
        final JColorChooser jcc = new JColorChooser();
		
        //disable Preview section in JCC
        jcc.setPreviewPanel(new JPanel());
		
        //create brushSelectPanel elements--------------------------------------
        brushShapeLabel = new JLabel("Brush Select");
        brushSizeLabel = new JLabel("Brush Size");
		
        brushField = new JTextField(35);
        brushField.setText(brush);
        brushField.setToolTipText("Enter up to 140 characters and press return" 
                                + " to form a customized paint brush.");
		
        //set spinner with initial value, low value, high value, and step size
        spinner = new JSpinner(new SpinnerNumberModel(12, 5, 400, 1));
        spinner.setToolTipText("Select a brush size between 5 and 400");

        //create drawModePanel elements-----------------------------------------
        //default drawing mode is Brush Mode
        brushModeButton = new JButton
                                   ("<html><strong>Brush Mode</strong></html>");
        brushModeButton.setForeground(Color.red);
        brushModeButton.setToolTipText("Click to paint in \"Brush\" mode. " 
                               + "Click or click and drag the mouse to paint.");
	
        lineModeButton = new JButton("Line Mode");
        lineModeButton.setToolTipText("Click to draw a straight line. Click " 
                 + "where you want the line to begin and drag to where you want"
                 + " it to end.");
		
        rectModeButton = new JButton("Rectangle Mode");
        rectModeButton.setToolTipText("Click to draw a rectangle. Click again " 
                + "to toggle between \"outline\" and \"fill\" modes. Click " 
                + "where you want the shape to begin and drag to where you want"
                + " it to end.");
		
        ovalModeButton = new JButton("Oval Mode");
        ovalModeButton.setToolTipText("Click to draw an oval. Click again to " 
                + "toggle between \"outline\" and \"fill\" modes. Click where " 
                + "you want the shape to begin and drag to where you want it to" 
                + " end.");
		
        //create panels---------------------------------------------------------
        Dimension menuDimension = new Dimension(460, currentHeight+50);
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
        menuPanel.setPreferredSize(menuDimension);
        menuPanel.setMinimumSize(menuDimension);
        menuPanel.setMaximumSize(menuDimension);
        menuPanel.setBorder(BorderFactory.createRaisedBevelBorder());
		
        optionPanel = new JPanel();
        optionPanel.setBorder(new TitledBorder(
                                  BorderFactory.createTitledBorder("Options")));
        optionPanel.setLayout(new GridLayout(1,2));
        optionPanel.setPreferredSize(new Dimension(450, 20));

        colorPanel = new JPanel();
        colorPanel.setBorder(new TitledBorder(
                             BorderFactory.createTitledBorder("Color Select")));
        colorPanel.setPreferredSize(new Dimension(450,240));
		
        controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.PAGE_AXIS));
		
        brushSelectPanel = new JPanel();
        brushSelectPanel.setLayout(new GridLayout(2,2));
        brushSelectPanel.setBorder(new TitledBorder(
                               BorderFactory.createTitledBorder("Brush Type")));
        brushSelectPanel.setPreferredSize(new Dimension(100,10));
		
        drawModePanel = new JPanel();
        drawModePanel.setBorder(new TitledBorder(
                                BorderFactory.createTitledBorder("Draw Mode")));
        drawModePanel.setLayout(new GridLayout(2,2));
        drawModePanel.setPreferredSize(new Dimension(450, 200));
        drawModePanel.setMinimumSize(new Dimension(450, 200));
        drawModePanel.setMaximumSize(new Dimension(450, 200));

        //add action listeners for buttons--------------------------------------
        saveButton.addActionListener(new SaveButtonListener());
        exitButton.addActionListener(new ExitButtonListener());
        brushField.addActionListener(new BrushFieldListener());
        brushModeButton.addActionListener(new BrushModeListener());
        lineModeButton.addActionListener(new LineModeListener());
        rectModeButton.addActionListener(new RectModeListener());
        ovalModeButton.addActionListener(new OvalModeListener());
        helpButton.addActionListener(new HelpButtonListener());
		
        //add change listeners for spinner and JColorChooser--------------------
        //spinner
        spinner.addChangeListener(new ChangeListener() 
        {
            /** Task: updates the brushSize and sets drawing mode to Brush mode
            *  @param ChangeEvent is a change in the fontSize field 
            */
            public void stateChanged(ChangeEvent e) 
            {
                currentFontSize = (Integer) spinner.getValue();
                currentFont = new Font("Helvetica",Font.PLAIN,currentFontSize);
                brushFocus();
            }//end stateChanged
        });//end ChangeListener anonymous class
		
        //JColorChooser
        jcc.getSelectionModel().addChangeListener(new ChangeListener()
        {
            /** Task: updates the currentColor to the newly selected color in
            *        JCC 
            */
            public void stateChanged(ChangeEvent ce)
            {
                //update currentColor to newly selected color in JCC
                currentColor = jcc.getColor();
            }//end stateChanged()
        });//end ChangeListener anonymous class
		
        //add elements to sub-panels--------------------------------------------
        optionPanel.add(undoButton);
        optionPanel.add(redoButton);
        optionPanel.add(clearButton);
        optionPanel.add(saveButton);
        optionPanel.add(loadButton);
        optionPanel.add(helpButton);
        optionPanel.add(exitButton);
		
        colorPanel.add(jcc);
		
        brushSelectPanel.add(brushShapeLabel);
        brushSelectPanel.add(brushSizeLabel);
        brushSelectPanel.add(brushField);
        brushSelectPanel.add(spinner);
		
        drawModePanel.add(brushModeButton);
        drawModePanel.add(lineModeButton);
        drawModePanel.add(rectModeButton);
        drawModePanel.add(ovalModeButton);
		
        //add sub-panels to controlPanel----------------------------------------
        controlPanel.add(brushSelectPanel);
        controlPanel.add(drawModePanel);
		
        //add sub-panels to menuPanel-------------------------------------------
        menuPanel.add(optionPanel);
        menuPanel.add(colorPanel);
        menuPanel.add(controlPanel);
        //set exact bounds for menuPanel
        menuPanel.setBounds(1, 1, 450, 700);
		
        //create PaintPanel (JPanel)--------------------------------------------
        pp = new PaintPanel();
        pp.setLayout(new BoxLayout(pp, BoxLayout.LINE_AXIS));

        //create PaintCanvas (Canvas)-------------------------------------------
        PaintCanvas pc = new PaintCanvas();
        //set exact bounds for paint canvas
        pc.setBounds(451, 1, 700,700);
		
        //add menuPanel and PaintCanvas to JPanel-------------------------------
        pp.add(menuPanel);
        pp.add(pc);
	
    }//end buildPanel()
	
    //*******************************brushFocus()*******************************
    /** Task: Sets the drawing mode to "Brush" and updates the DrawMode buttons
    *         to reflect this change.
    */
    public void brushFocus()
    {
        //set mode to Brush
        mode = Shape.BRUSH;
		
        //turn off rect and oval modes
        rectMode = false;
        ovalMode = false;

        //update Draw Mode button labels----------------------------------------
        brushModeButton.setText("<html><strong>Brush Mode</strong></html>");
        brushModeButton.setForeground(Color.red);
		
        lineModeButton.setText("Line Mode");
        lineModeButton.setForeground(null);
		
        rectModeButton.setText("Rectangle Mode");
        rectModeButton.setForeground(null);
		
        ovalModeButton.setText("Oval Mode");
        ovalModeButton.setForeground(null);
    }//end brushFocus()
	
    //*******************************lineFocus()********************************
    /** Task: Sets the drawing mode to "Line" and updates the DrawMode buttons
    *         to reflect this change.
    */
    public void lineFocus()
    {
        //set mode to Line
        mode = Shape.LINE;
		
        //turn off rect and oval modes
        rectMode = false;
        ovalMode = false;

        //update Draw Mode button labels----------------------------------------
        brushModeButton.setText("Brush Mode");
        brushModeButton.setForeground(null);
		
        lineModeButton.setText("<html><strong>Line Mode</strong></html>");
        lineModeButton.setForeground(Color.red);
		
        rectModeButton.setText("Rectangle Mode");
        rectModeButton.setForeground(null);
		
        ovalModeButton.setText("Oval Mode");
        ovalModeButton.setForeground(null);
    }//end lineFocus()
	
    //*******************************rectFocus()********************************
    /** Task: Sets the drawing mode to "Rect" and updates the DrawMode buttons
    *         to reflect this change. If currently in Rect mode, then change to
    *         FillRect mode.
    */
    public void rectFocus()
    {
        //if current mode is FillRect
        if(!rectMode)
        {
            //set to Rect
            mode = Shape.RECT;
            rectModeButton.setText(
                                "<html><strong>Rectangle Mode</strong></html>");
			rectModeButton.setForeground(Color.red);
		    rectMode = true;
		}
        //if current mode Rect
        else
        {
            //set to FillRect
            mode = Shape.FILL_RECT;
            rectModeButton.setText(
                           "<html><strong>Fill Rectangle Mode</strong></html>");
            rectModeButton.setForeground(Color.red);
            rectMode = false;		
        }//end else
		
        //turn off oval mode
        ovalMode = false;

        //update Draw Mode button labels----------------------------------------
        brushModeButton.setText("Brush Mode");
        brushModeButton.setForeground(null);
		
        lineModeButton.setText("Line Mode");
        lineModeButton.setForeground(null);

        ovalModeButton.setText("Oval Mode");
        ovalModeButton.setForeground(null);
    }//end rectFocus()
	
    //*******************************ovalFocus()********************************
    /** Task: Sets the drawing mode to "Oval" and updates the DrawMode buttons
    *         to reflect this change. If currently in Oval mode, then change to
    *         FillOval mode.
    */
    public void ovalFocus()
    {
        //if current mode is FillOval
        if(!ovalMode)
        {
            //set to Oval mode
            mode = Shape.OVAL;
            ovalModeButton.setText("<html><strong>Oval Mode</strong></html>");
            ovalModeButton.setForeground(Color.red);
            ovalMode = true;
        }
        //if current mode is Oval mode
        else
        {
            //set to FillOval mode
            mode = Shape.FILL_OVAL;
            ovalModeButton.setText("<html><strong>Fill Oval Mode</strong></html>");
            ovalModeButton.setForeground(Color.red);
            ovalMode = false;
        }//end else
		
        //turn off rect mode;
        rectMode = false;

        //update Draw Mode button labels----------------------------------------
        brushModeButton.setText("Brush Mode");
        brushModeButton.setForeground(null);
		
        lineModeButton.setText("Line Mode");
        lineModeButton.setForeground(null);
		
        rectModeButton.setText("Rectangle Mode");
        rectModeButton.setForeground(null);
    }//end ovalFocus()
	
    //*********************************save()***********************************
    /** Task: Saves the current file to disk.
    */
    public void save()
    {
        //variable to check save option
        boolean saveFile = true;
		
        //variable to hold filename
        String fileName = null;
	
        //create new File Chooser
        JFileChooser jfc = new JFileChooser();
		
        //set directory to current directory
        jfc.setCurrentDirectory(new File(System.getProperty("user.dir")));
		
        //set FileExtension filter to .ser
        FileNameExtensionFilter f = new FileNameExtensionFilter(".ser", "ser");
        jfc.setFileFilter(f);

        //variable to track users response to save prompt
        int option = jfc.showSaveDialog(saveButton);
		
        //if the user chooses to save the file in the JFC
        if(option == JFileChooser.APPROVE_OPTION)
        {		
            //create FOS
            FileOutputStream fos = null;
			
            //attempt:
            try 
            {
                //get the file name the user entered
                fileName = jfc.getSelectedFile().getName();
				
                //if the file already has .ser at the end
                if(fileName.endsWith(".ser"))
                {
                    //remove the .ser
                    fileName = fileName.substring(0, fileName.length() -4);
                }
				
                //create a file with extension .ser
                File currentFile = new File(fileName + ".ser");
				
                //if the file already exists
                if(currentFile.exists())
                {
                    //ask the user if they want to overwrite the file
                    int response = JOptionPane.showConfirmDialog(saveButton, 
                      "This file already exists. Would you like to overwrite?");
					
                    //if YES 
                    if(response == JOptionPane.YES_OPTION)
                    {
                        //set saveFile option to true
                        saveFile = true;
                    }
                    //otherwise
                    else
                    {
                        //set saveFile option to false
                        saveFile = false;
                    }
                }//if currentFile.exists()
				
                //create new FOS
                fos = new FileOutputStream(currentFile);
				
            }catch (FileNotFoundException e) 
            {
                //if file cannot be found, display error message
                JOptionPane.showMessageDialog(saveButton, 
                                                "The file could not be found.");
            }//end catch

            //create OOS
            ObjectOutputStream oos;
			
            //if the user chose to save the file
            if(saveFile)
            {
                //attempt:
                try 
                {
                    //create OOS with FOS
                    oos = new ObjectOutputStream(fos);
					
                    //write the history ArrayList to the file
                    oos.writeObject(history);
					
                    //end the save process
                    oos.flush();
                    oos.close();
                }catch (IOException e) 
                {
                    //if there is an error during the save operation display and
                    //error message
                    JOptionPane.showMessageDialog(saveButton, 
                                                "The file could not be saved.");
                }//end catch	
            }//end if(saveFile)
			
            //update Frame title to include saved file name
            setTitle("Java Paint - " + fileName);
			
            //set changesMade variable to false 
            changesMade = false;
        }//end if(option == JFileChooser.APPROVE_OPTION)
    }//end save()
	
    //****************************updateUndoRedo()******************************
    /** Task: This method clears the redo stack, sets the redo button text to 
    *         gray to indicate that there is nothing to redo, and sets the undo
    *         button text to black, to show that the clear option can be undone. 
    */
    public void updateUndoRedo()
    {
    	//set undoButton text to black
        undoButton.setForeground(Color.black);
        
        //set the redoButton to text to gray
        redoButton.setForeground(Color.gray);
        
        //clear the redoStack
        redoStack.clear();
    }
	
    //+++++++++++++++++++++++++++++INNER CLASSES++++++++++++++++++++++++++++++++
	
    //@@@@@@@@@@@@@@@@@@@@*@@@@@PaintPanel(InnerClass)@@@@@@@@@@@@@@@@@@@@@@@@@@
    /** Task: A panel that calls super.paintComponents(g). All elements of the 
    *         program are added to this panel, and this panel is added to the 
    *         frame.
    */
    public class PaintPanel extends JPanel 
    {
        //auto-generated serialVersionUID
		private static final long serialVersionUID = 2424619767580371220L;

		//constructor
        public PaintPanel()
        {
        	//get graphics
            Graphics g = getGraphics();
            
            //pass graphics to super paintComponent 
            super.paintComponents(g);
        }
    }//end PaintPanel inner class
	
    //@@@@@@@@@@@@@@@@@@@@@@@@@PaintCanvas(InnerClass)@@@@@@@@@@@@@@@@@@@@@@@@@@
    /** Task: This is the Paint Canvas where all painting operations are 
    *         executed.
    */
    public class PaintCanvas extends Canvas
    {
        //auto-generated serialVersionUID
		private static final long serialVersionUID = 3481041567667044532L;

		//!!!!!!!!!!!!!!!!!!!!!!!!!!!!Constructors!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //default constructor---------------------------------------------------
        public PaintCanvas()
        {
            //call constructor with these dimensions
            this(500,500);
        }//end PaintCanvas() constructor
		
        //constructor with parameters-------------------------------------------
        public PaintCanvas(int width, int height)
        {
            //set cavnas size
            setSize(width, height);
			
            //set canvas background
            setBackground(Color.WHITE);
			
            //set visible
            setVisible(true);
			
            //add mouse motion listeners 
            addMouseMotionListener(new MyMouseMotionListener());	
            addMouseListener(new MyMouseListener());
			
            //add actionListener for clear button
            clearButton.addActionListener(new ClearButtonListener());
            undoButton.addActionListener(new UndoButtonListener());
            redoButton.addActionListener(new RedoButtonListener());
            loadButton.addActionListener(new LoadButtonListener());

        }//end PaintCanvas() constructor
		
		
        //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Methods%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		
        //*******************************paint()********************************
        /** Task: Paints history and then new Shape. Includes algorithm to allow
        *         user to drag shapes in any direction to preview them before
        *         adding to the paint history.
        *        
        * @param g is the graphics object
        */
        public void paint(Graphics g)
        {
            //if clear button is pushed
            if(clear)
            {			
                //set color to white
                g.setColor(Color.WHITE);
				
                //draw rectangle that covers entire area of canvas and add to
                //history
                history.add(new Shape(0,0, currentWidth*2, currentHeight*2, 
                		                         Color.white, Shape.FILL_RECT));
                
                //turn off clear mode 
                clear = false;
            }
            //otherwise paint specified brush or shape
            else
            {
                //draw from history---------------------------------------------
                for(int i=0; i<history.size(); i++)
                {
                    //get current Shape color
                    g.setColor(history.get(i).getColor());
								
                    //perform paint operation for corresponding draw method
                    switch(history.get(i).getMode())
                    {
                        //if brush
                        case Shape.BRUSH:
                            //set font size
                            g.setFont(new Font("Helvetica", Font.PLAIN, 
                                                 history.get(i).getFontSize()));

                            //draw current Brush stroke  
                            g.drawString(history.get(i).getBrush(),
                                         history.get(i).getX(),
                                         history.get(i).getY());
                            break;
						
                        //if line
                        case Shape.LINE:
                            //draw line
                            g.drawLine(history.get(i).getFirstX(), 
                                       history.get(i).getFirstY(), 
                                       history.get(i).getSecondX(), 
                                       history.get(i).getSecondY());
                            break;
									   
                        //if rectangle
                        case Shape.RECT:
                            //draw rectangle
                            g.drawRect(history.get(i).getFirstX(), 
                                       history.get(i).getFirstY(), 
                                       history.get(i).getSecondX(), 
                                       history.get(i).getSecondY());
                            break;
						
                        //if oval
                        case Shape.OVAL:
                            //draw oval
                            g.drawOval(history.get(i).getFirstX(), 
                                       history.get(i).getFirstY(), 
                                       history.get(i).getSecondX(), 
                                       history.get(i).getSecondY());
                            break;
						
                        //if Fill Rect
                        case Shape.FILL_RECT:
                            //draw Fill Rect
                            g.fillRect(history.get(i).getFirstX(), 
                                       history.get(i).getFirstY(), 
                                       history.get(i).getSecondX(), 
                                       history.get(i).getSecondY());
                            break;
						
                        //if Fill Oval
                        case Shape.FILL_OVAL:
                            //draw Fill Oval
                            g.fillOval(history.get(i).getFirstX(), 
                                       history.get(i).getFirstY(), 
                                       history.get(i).getSecondX(), 
                                       history.get(i).getSecondY());
                            break;
                    }//end switch				
                }//end for loop (draw history)
				
                //draw new event------------------------------------------------				
                //set color to currently selected color
                g.setColor(currentColor);
				
                //draw the specified shape
                switch(mode)
                {
                    //brush
                    case Shape.BRUSH:
                        g.setFont(new Font("Helvetica", Font.PLAIN, 
                                                              currentFontSize));
                        g.drawString(brush, currentX, currentY);
                        break;	
						
                    //line	
                    case Shape.LINE:
                        g.drawLine(firstX, firstY, secondX, secondY);
                        break;
						
                    //rectangle	
                    case Shape.RECT:
                        //if SW direction
                        if((secondX < firstX) && !(secondY < firstY) )
                        {
                            g.drawRect(secondX, firstY, firstX-secondX, 
                                                                secondY-firstY);
                        }
                        //if NE direction
                        else if((secondY < firstY) && !(secondX < firstX))
                        {
                            g.drawRect(firstX, secondY, secondX-firstX, 
                                                                firstY-secondY);
                        }
                        //if NW direction
                        else if((secondX < firstX) && (secondY < firstY))
                        {
                            g.drawRect(secondX, secondY,firstX-secondX, 
                                                                firstY-secondY);
                        }
                        //if SE direction
                        else
                        {
                            g.drawRect(firstX, firstY, secondX-firstX, 
                                                                secondY-firstY);
                        }

                        break;
						
                    //oval
                    case Shape.OVAL:
                        //if SW direction
                        if((secondX < firstX) && !(secondY < firstY) )
                        {
                            g.drawOval(secondX, firstY, firstX-secondX, 
                            	                                secondY-firstY);
                        }
                        //if NE direction
                        else if((secondY < firstY) && !(secondX < firstX))
                        {
                            g.drawOval(firstX, secondY, secondX-firstX, 
                                                                firstY-secondY);
                        }
                        //if NW direction
                        else if((secondX < firstX) && (secondY < firstY))
                        {
                            g.drawOval(secondX, secondY,firstX-secondX, 
                                                                firstY-secondY);
                        }
                        //if SE direction
                        else
                        {
                            g.drawOval(firstX, firstY, secondX-firstX, 
                                                                secondY-firstY);
                        }

                        break;
						
                    //Fill Rect	
                    case Shape.FILL_RECT:
                        //if SW direction
                        if((secondX < firstX) && !(secondY < firstY) )
                        {
                            g.fillRect(secondX, firstY, firstX-secondX, 
                                                                secondY-firstY);
                        }
                        //if NE direction
                        else if((secondY < firstY) && !(secondX < firstX))
                        {
                            g.fillRect(firstX, secondY, secondX-firstX, 
                                                                firstY-secondY);
                        }
                        //if NW direction
                        else if((secondX < firstX) && (secondY < firstY))
                        {
                            g.fillRect(secondX, secondY,firstX-secondX, 
                                                                firstY-secondY);
                        }
                        //if SE direction
                        else
                        {
                            g.fillRect(firstX, firstY, secondX-firstX, 
                                                                secondY-firstY);
                        }

                        break;
					
                    //Fill Oval
                    case Shape.FILL_OVAL:
                        //if SW direction
                        if((secondX < firstX) && !(secondY < firstY) )
                        {
                            g.fillOval(secondX, firstY, firstX-secondX, 
                                                                secondY-firstY);
                        }
                        //if NE direction
                        else if((secondY < firstY) && !(secondX < firstX))
                        {
                            g.fillOval(firstX, secondY, secondX-firstX, 
                                                                firstY-secondY);
                        }
                        //if NW direction
                        else if((secondX < firstX) && (secondY < firstY))
                        {
                            g.fillOval(secondX, secondY,firstX-secondX, 
                                                                firstY-secondY);
                        }
                        //if SE direction
                        else
                        {
                            g.fillOval(firstX, firstY, secondX-firstX, 
                                                                secondY-firstY);
                        }

                        break;
					
                }//end switch
            }//end paint()
        }//end PaintCanvas inner class

        //@@@@@@@@@@@@@@@@@@@@@ClearButtonListener(InnerClass)@@@@@@@@@@@@@@@@@@
        /** Task: Clears the canvas when the clear button is pressed.
        */
        private class ClearButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent ae)
            {
                    //set clear boolean to true
                    clear = true;

                    //update the undo/redo button's text color and clear the
                    //redo history
                    updateUndoRedo();
                    
                    //repaint
                    repaint();

				
            }//end actionPerformed
        }//end ClearButtonListener inner class
        
        //$$$$$$$$$$$$$$$$$$$UndoButtonListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$
        /** Task: This class listens for the undo button being clicked. When
        *         clicked, the last drawing operation is undone.
        */
        private class UndoButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent ae)
            {        		
                //if there are items in the paint history
                if(!history.isEmpty())
                {
                    //if the current history entry is a brush
                    if(history.get(history.size()-1).getMode() == Shape.BRUSH)
                    {
                        //undo all items until coming to the next END tag or
                        //until there is nothing left to undo
                        do
                        {
                            //remove an item from the history and add it to the
                            //redo stack.
                            redoStack.push(history.remove(history.size()-1));

                        }while(history.size() != 0 
                               && history.get(history.size()-1).getPaintMode() 
                                  != Shape.END);
                    }
                    //if entry is not a brush (is a line/rect/oval)
                    else
                    {
                        //remove the entry from the history and add it to the 
                        //redo stack
                        redoStack.push(history.remove(history.size()-1));  			
                    }
        			
                    //set the redoButton color to black, to indicate there are
                    //now actions to redo
                    redoButton.setForeground(Color.black);
        			
                    //if there is nothing left in the history
                    if(history.isEmpty())
                    {
                        //set the undo button to gray, to indicate that there is
                        //nothing left to undo
                        undoButton.setForeground(Color.gray);
                    }
        			
                }//end if !history.isEmpty()
        		
                //repaint the canvas
                repaint();
        		
            }//end actionPerfomed()
        }//end UndoButtonListener class
        
        //$$$$$$$$$$$$$$$$$$$RedoButtonListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$
        /** Task: This class listens for the undo button being clicked. When 
        *         clicked, the drawing operation is redone. 
        */
        private class RedoButtonListener implements ActionListener
        {
            public void actionPerformed(ActionEvent ae)
            {
                //if the redo stack is not empty
                if(!redoStack.isEmpty())
                {
                    //if the next action to redo is a Brush
                    if(redoStack.peek().getMode() == Shape.BRUSH)
                    {
                        //if the brush is a single brush stroke
                        if( redoStack.peek().getPaintMode() == Shape.END )
                        {
                            //remove the action from the redo stack and add it
                            //to the history
                            history.add(redoStack.pop());
                        }
                        //if the next action is a string of brush strokes
                        else
                        {
                            //Until the end of the string of brush strokes is 
                            //reached.(or until there are no more actions to 
                            //redo
                            do
                            {
                                //remove all of the brush strokes from the redo 
                                //stack and add them to the history,
                                history.add(redoStack.pop());
        						
                            }while((!redoStack.isEmpty()) 
                                    && (redoStack.peek().getPaintMode() 
                                        != Shape.END));
        					
                            //remove the last brush stroke of the string of 
                            //brush strokes from the redo stack and add to the
                            //history
                            history.add(redoStack.pop());
        					
                        }//end else
                    }
                    //otherwise the entry is not a brush (is a line/oval/rect)
                    else
                    {
                        //remove the entry from the redo stack and add it to the
                        //history
                        history.add(redoStack.pop());
                    }
        			
                    //set the undo button text to black to indicate there are
                    //now items to undo
                    undoButton.setForeground(Color.black);    			
        			
                    //if there are no more items in the redo stack
                    if(redoStack.isEmpty())
                    {
                        //set the redo button text to gray to indicate that 
                        //there are no more actions to redo
                        redoButton.setForeground(Color.gray);
                    }
        			
                }//end if !stack.isEmpty()
        		
                //repaint the canvas
                repaint();
        		
            }//end actionPerformed()
        }//end RedoButtonListener class
        
        //$$$$$$$$$$$$$$$$$$$LoadButtonListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$
        /** Task: detects if the load button is clicked. If so, load a saved 
        *         file.
        */
        private class LoadButtonListener implements ActionListener
        {
        	//Suppress warnings for unchecked casting of loaded object
            @SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent ae)
            {
                //create a new JFC
                JFileChooser jfc = new JFileChooser();
    			
                //set current directory
                jfc.setCurrentDirectory(
                                      new File(System.getProperty("user.dir")));
    			
                //set the FileNameExtensionFiler to .ser
                FileNameExtensionFilter f = 
                                     new FileNameExtensionFilter(".ser", "ser");
                jfc.setFileFilter(f);

                //display JFC and store user response
                int option = jfc.showOpenDialog(loadButton);
    			
                //if the user selects a file to load
                if(option == JFileChooser.APPROVE_OPTION)
                {
                    //get the filename of the file to load				
                    String fileName = jfc.getSelectedFile().getName();
    				
                    //create a FIS
                    FileInputStream fis = null;
    				
                    //attempt:
                    try 
                    {
                        //load file into the FIS
                        fis = new FileInputStream(fileName);
    					
                    } catch (FileNotFoundException e) 
                    {
                        //If file not found, display an error message
                        JOptionPane.showMessageDialog(saveButton, 
                                                "The file could not be found.");
                    }
    				
                    //create OIS
                    ObjectInputStream ois = null;
    				
                    //attempt:
                    try 
                    {
                        //load the FIS into the OIS
                        ois = new ObjectInputStream(fis);
    					
                        //attempt:
                        try 
                        {
                            //cast the loaded Object to ArrayList<Shape> and 
                            //set the history variable to the newly loaded
                            //ArrayList
                            history = (ArrayList<Shape>) ois.readObject();
                        } catch (ClassNotFoundException e) 
                        {
                            //if file could not be loaded, display and error
                        	//message
                            JOptionPane.showMessageDialog(saveButton, 
                                        "The file object could not be loaded.");

                        }//end catch
    					
                        //close OIS
                        ois.close();
    					
                    } catch (IOException e) 
                    {
                        //if there is an IOException, display and error message
                        JOptionPane.showMessageDialog(saveButton, 
                                               "The file could not be loaded.");

                    }//end catch
    				
                    //set Frame title to show newly loaded file name
                    setTitle("Java Paint - " + fileName);
    				
                    //call repaint
                    repaint();
    				
                    //set changes made to false
                    changesMade = false;
    			
                }//end if(option == JFileChooser.APPROVE_OPTION)	
    			
            }//end actionPerformed()
        }//end LoadButtonListener inner class
		
        //$$$$$$$$$$$$$$$$MyMouseMotionListener(NestedInnerClass)$$$$$$$$$$$$$$$
        /** Task: Listen for mouse motion events within PaintCanvas
        */
        private class MyMouseMotionListener extends MouseMotionAdapter
        {
            //**************************mouseMoved()****************************
            /** Task: detects mouse movement in the paint canvas, and draws a
            *         preview of the brush wherever the mouse is.
            * @param me is the mouse event 
            */
            public void mouseMoved(MouseEvent me)
            {
                //if in brush mode
                if(mode == Shape.BRUSH)
                {
                    //get mouse location and adjust to be closer to the 
                    //mouse pointer
                    currentX = me.getX() - currentFontSize/4;
                    currentY = currentFontSize/4 + me.getY();
					
                    //repaint
                    repaint();
                }//end if
            }//end mouseMoved()
			
            //**************************mouseDragged()**************************
            /** Task: detects when the mouse is pressed and dragged. If in brush
            *         mode, get the mouse location, add a new brush Shape object
            *         to the history, repaint, and set changesMade to true.
            *        
            *  @param me is the mouse event 
            */
            public void mouseDragged(MouseEvent me)
            {	
                //if Brush mode
                if(mode == Shape.BRUSH)
                {	                	
                    //get mouse location and adjust to be closer to the mouse
                    //pointer
                    currentX = me.getX() - currentFontSize/4;
                    currentY = currentFontSize/4 + me.getY();
				
                    //add a new Bursh shape to the history ArrayList
                    history.add(new Shape(currentX, currentY, currentColor, 
                           brush, currentFontSize, Shape.BRUSH, Shape.MIDDLE ));
					
                    //update undo/redo buttons and stack
                    updateUndoRedo();
                    
                    //set changesMade to true
                    changesMade = true;
					
                    //repaint
                    repaint();
                }
                //if any mode besides Brush
                else if (mode == Shape.LINE || mode == Shape.RECT 
                      || mode == Shape.OVAL || mode == Shape.FILL_RECT 
                      || mode == Shape.FILL_OVAL)
                {
                    //get the second location of the shape
                    //(first location is tracked with mousePressed
                    secondX = me.getX();
                    secondY = me.getY();
					
                    //repaint
                    repaint();
                }//else
				
            }//end mouseDragged()
        }//end MyMouseListener nested inner class
		
        //$$$$$$$$$$$$$$$$$$$MyMouseListener(NestedInnerClass)$$$$$$$$$$$$$$$$$$
        /** Task: detects mouse press, release, and exit events
        * 
        */
        private class MyMouseListener extends MouseAdapter
        {
            //**************************mousePressed()**************************
            /** Task: detects the mouse being pressed, and gets the x/y location
            * 
            * @param me is the mouse event
            */
            public void mousePressed(MouseEvent me)
            {
                //get coordinates and store in first variables
                firstX = me.getX();
                firstY = me.getY();
				
            }//end mousePressed()
			
            //**************************mouseReleased()*************************
            /** Task: detects the mouse being released, and gets the x/y 
            *         location.
            *        
            *  @param me is the mouse event
            */
            public void mouseReleased(MouseEvent me)
            {
                //get the coordinates and store in the second variables
                secondX = me.getX();
                secondY = me.getY();
           
                //if currently in Brush mode
                if(mode == Shape.BRUSH)
                {
                	//get mouse location and adjust to be closer to the mouse
                    //pointer
                    currentX = me.getX() - currentFontSize/4;
                    currentY = currentFontSize/4 + me.getY();
					
                    //add new brush stroke Shape to history
                    history.add(new Shape(currentX, currentY, currentColor, 
                               brush, currentFontSize, Shape.BRUSH, Shape.END));
                }
                
                //if there is a difference in first and second coordinates
                if((firstX != secondX) && (firstY != secondY))
                {
                    //if in Line mode
                    if(mode == Shape.LINE)
                    {
                        //add a new Line shape to the history
                        history.add(new Shape(firstX, firstY, secondX, secondY, 
                                                     currentColor, Shape.LINE));
                    }//if
					
                    //if in Rect or FillRect mode
                    if(mode == Shape.RECT || mode == Shape.FILL_RECT)
                    {
                        //variable to specify normal or fill mode
                        int currentShape;
						
                        //if Rect mode
                        if(mode == Shape.RECT)
                        {
                            //set shape to Rect
                            currentShape = Shape.RECT;
                        }
                        //if in Fill Rect mode
                        else
                        {
                            //set shape to Fill Rect
                            currentShape = Shape.FILL_RECT;
                        }//end else
						
                        //if direction is SW
                        if((secondX < firstX) && !(secondY < firstY) )
                        {
                            //calculate position and width and height and 
                            //add new Rect/Fill_Rect Shape to history
                            history.add(new Shape(secondX, firstY, 
                                                 firstX-secondX, secondY-firstY, 
                                                 currentColor, currentShape));
                        }
                        //if direction is NE
                        else if((secondY < firstY) && !(secondX < firstX))
                        {
                            //calculate position and width and height and 
                            //add new Rect/Fill_Rect Shape to history
                            history.add(new Shape(firstX, secondY, 
                                                 secondX-firstX, firstY-secondY, 
                                                 currentColor, currentShape));
                        }
                        //if direction is NW
                        else if((secondX < firstX) && (secondY < firstY))
                        {
                            //calculate position and width and height and 
                            //add new Rect/Fill_Rect Shape to history
                            history.add(new Shape(secondX, secondY,
                                                 firstX-secondX, firstY-secondY, 
                                                 currentColor, currentShape));
                        }
                        //if direction is SE
                        else
                        {
                            //calculate position and width and height and 
                            //add new Rect/Fill_Rect Shape to history
                            history.add(new Shape(firstX, firstY, 
                                                 secondX-firstX, secondY-firstY, 
                                                   currentColor, currentShape));
                        }//end else

                    }//end if Rect/FillRect
					
                    //mode is Oval or FillOVal
                    if(mode == Shape.OVAL || mode == Shape.FILL_OVAL)
                    {				
                        //variable to specify normal or fill mode
                        int currentShape;
						
                        //if mode is Oval
                        if(mode == Shape.OVAL)
                        {
                            //set shape to Oval
                            currentShape = Shape.OVAL;
                        }
                        //if mode is FillOval
                        else
                        {
                            //set shape to FillOval
                            currentShape = Shape.FILL_OVAL;
                        }
					
                        //if direction is SW
                        if((secondX < firstX) && !(secondY < firstY) )
                        {
                            //calculate position and width and height and 
                            //add new Oval/Fill_Oval Shape to history
                            history.add(new Shape(secondX, firstY, 
                                                 firstX-secondX, secondY-firstY, 
                                                   currentColor, currentShape));
                        }
                        //if direction is NE
                        else if((secondY < firstY) && !(secondX < firstX))
                        {
                            //calculate position and width and height and 
                            //add new Oval/Fill_Oval Shape to history
                            history.add(new Shape(firstX, secondY, 
                                                 secondX-firstX, firstY-secondY, 
                                                   currentColor, currentShape));
                        }
                        //if direction is NW
                        else if((secondX < firstX) && (secondY < firstY))
                        {
                            //calculate position and width and height and 
                            //add new Oval/Fill_Oval Shape to history
                            history.add(new Shape(secondX, secondY,
                                                 firstX-secondX, firstY-secondY, 
                                                 currentColor, currentShape));
                        }
                        //if direction is SE
                        else
                        {
                            //calculate position and width and height and 
                            //add new Oval/Fill_Oval Shape to history
                            history.add(new Shape(firstX, firstY, 
                                                 secondX-firstX, secondY-firstY, 
                                                   currentColor, currentShape));
                        }//end else
                    }//end if Oval/Fill_Oval
					
                    //set changes made to true
                    changesMade = true;
					
                    //update undo/redo buttons and stack
                    updateUndoRedo();
                    
                    //call repaint
                    repaint();
                }//end if((firstX != secondX) && (firstY != secondY))
				
            }//end mouseReleased()
			
            //**************************mouseExited()*************************
            /** Task: detects if mouse has exited the canvas area. When the 
            *        mouse exits, set all coordinates off screen to discard
            *        mouse brush preview.
            *        
            * @param me is the mouse event
            */
            public void mouseExited(MouseEvent me)
            {
                //set all coordinates off screen
                currentX = -20;
                currentY = -20;
                firstX = -20;
                firstY = -20;
                secondX = -20;
                secondY = -20;
				
                //repaint
                repaint();
            }//end mouseExited()
        }//end MouseListener inner class		
    }//end Paint Canvas inner class

    //$$$$$$$$$$$$$$$$$$$$$BrushFieldListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$$$
    /** Task: detects if a new brush is specified in the brush text field and 
    *         updates the brush and enters brush mode.
    */
    private class BrushFieldListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            //get newly entered brush
            String temp = brushField.getText();
			
            //if the size of the brush is over 140 characters
            if(temp.length()>140)
            {
                //display a warning that the brush is too long
                JOptionPane.showMessageDialog(brushSelectPanel,
                          "Please enter no more than 140 characters"
                        + "\nYour brush has been truncated to 140 characters.",
                          "Your Brush Is Too Long",
                          JOptionPane.WARNING_MESSAGE);
				
                //truncate the brush to 140 characters
                brush = temp.substring(0, 140);
				
                //set the brush field to the truncated brush
                brushField.setText(temp.substring(0, 140));

            }
            //if valid length
            else
            {
                //set brush to the newly entered brush
                brush = temp;
            }
			
            //select the string in the brush field
            brushField.selectAll();

            //set mode and focus to Brush
            brushFocus();
			
        }//end actionPerformed()
    }//end BrushFieldListener inner class
	    
    //$$$$$$$$$$$$$$$$$$$$$ExitButtonListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$$$
    /** Task: detects if the exit button is pressed. If so, ask the user if they
    *        really want to exit. If there have been changes made without
    *        saving, ask the user if they wish to save first.
    */
    private class ExitButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            //ask user if they want to exit the program
            int response = JOptionPane.showConfirmDialog(optionPanel, 
                                              "Are you sure you want to quit?");
			
            //if they want to exit
            if(response == JOptionPane.YES_OPTION)
            {
                //if changes have been made without saving
                if(changesMade)
                {
                    //ask if they want to save their work first
                    int saveResponse = JOptionPane.showConfirmDialog(
                          optionPanel, "You have made changes to your " +
                          "document,\nwould you like to save before quitting?");
					
                    //if no
                    if(saveResponse == JOptionPane.NO_OPTION)
                    {
                        //exit the program
                        System.exit(0);
                    }
                    //if they cancel
                    else if (saveResponse == JOptionPane.CANCEL_OPTION)
                    {
                        //do nothing
                    }
                    //if yes
                    else
                    {
                        //save the file
                        save();
                        //exit the program
                        System.exit(0);
                    }//end else
                }
                //if no changes made
                else
                {
                    //exit the program
                    System.exit(0);
                }//end else
				
            }//end if(response == JOptionPane.YES_OPTION)
        }//end actionPerformed()
    }//end ExitButtonListener inner class
	
    //$$$$$$$$$$$$$$$$$$$$$HelpButtonListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$$$
    /** Task: detect if the help button is clicked. If so, display a help
    *         popup.
    */
    private class HelpButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            //display Help info popup
            JOptionPane.showMessageDialog(pp, 
            "Java Paint Help:\n\n" +
            "Java Paint is a simple program that allows you to paint\n" +
            "with characters or simple lines and shapes.\n\n" 
            +
            "Use the draw mode buttons to choose your painting tool.\n" +
            "To paint, simply click and drag your mouse in the canvas area\n" +
            "To draw a line or shape, simply click where you want it to " +
            "begin,\n" +
            "and release where you want it to end.\n\n" 
            +
            "You can toggle between outline mode and fill mode, for the\n" +
            "rectangle and oval modes, by clicking their respective " +
            "button again.\n\n" 
            +
            "Your \"brush\" can be made of up to 140 characters, giving\n" +
            "you many interesting combinations to work with. Additionally,\n" +
            "you can set the size of the brush strokes.\n\n" 
            +
            "Use the color chooser to customize the color you wish to use\n" +
            "for your brush or shape. \n\n" +
            "You may also save and load Java Paint projects. If you are\n" +
            "unhappy with your current painting, just hit the \"clear\"\n" +
            "button to erase what is on the canvas. Java paint also allows\n" +
            " infinite undos, so you'll never loose your work.\n\n" 
            +
            "Enjoy painting with Java Paint!\n\n" 
            +
            "Java Paint was created by Matthew Usnick, December 2011 ");
        }//end actionPerformed
    }//end HelpButtonListener inner class
	
    //$$$$$$$$$$$$$$$$$$$$$SaveButtonListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$$$
    /** Task: detects if the save button is clicked. If so, save the current
    *        painting.
    */
    private class SaveButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            //call the save method
            save();
        }//end actionPerformed
    }//end SaveButtonListener inner class
		
    //$$$$$$$$$$$$$$$$$$$$$BrushModeListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$$$$
    /** Task: detects if the Brush Mode button is clicked. If so, set paint mode
    *        to Brush, and update DrawMode buttons. 
    */
    private class BrushModeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {		
            //set paint mode to Brush, and update DrawMode buttons. 
            brushFocus();
			
        }//end actionPerformed()
    }//end BrushModeListener inner class
	
    //$$$$$$$$$$$$$$$$$$$$$LineModeListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$$$$$
    /** Task: detects if the Line Mode button is clicked. If so, set paint mode
     *        to Line and update DrawMode buttons
     */
    private class LineModeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            //set paint mode to Line, and update DrawMode buttons
            lineFocus();
			
        }//end actionPerformed()
    }//end LineModeListener inner class
	
    //$$$$$$$$$$$$$$$$$$$$$RectModeListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$$$$$
    /** Task: detects if the Rect Mode button is clicked. If so, set paint mode
    *         to Rect and update DrawMode buttons
    */
    private class RectModeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            //set paint mode to Rect, and update DrawMode buttons
            rectFocus();
        }//end actionPerformed()
    }//end RectModeListener inner class
	
    //$$$$$$$$$$$$$$$$$$$$$OvalModeListener(InnerClass)$$$$$$$$$$$$$$$$$$$$$$$$$
    /** Task: detects if Oval Mode button is clicked. If so, set paint mode to
    *        Oval and update DrawMode buttons. 
    */
    private class OvalModeListener implements ActionListener
    {
        public void actionPerformed(ActionEvent ae)
        {
            //set paint mode to Oval and update DrawMode buttons
            ovalFocus();
        }//end actionPerformed()
    }//end OvalModeListener inner class	
	
}//end Window.java
