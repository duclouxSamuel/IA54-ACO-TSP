package fr.utbm.ia54.acotsp.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import fr.utbm.ia54.acotsp.ACOParameters;
import fr.utbm.ia54.acotsp.Gtsp;
import fr.utbm.ia54.acotsp.NewOptimization;
import fr.utbm.ia54.acotsp.OptimizationFinished;
import io.sarl.core.DefaultContextInteractions;
import io.sarl.lang.core.Agent;


public class GuiPanel extends JFrame
 implements MouseListener, MouseMotionListener, MouseWheelListener{
	
	private static final Font font  = new Font("Dialog", Font.BOLD,  12);
	private static final Font small = new Font("Dialog", Font.PLAIN, 10);
	
	
	private File currentFile; //Fichier en cours d'utilisation
	private Gtsp gtsp; // Variable GTSP
	private JDialog runopt; // Run optimization dialog box
	private JFileChooser chooser; // file chooser
	private JTextField status = null; // Status Text Bar
	public	ACOPanel panel;
	private JScrollPane  scroll  = null;  /* scroll pane viewport */
	private JDialog      params  = null;  /* parameter dialog box */
	private float bestPathLength;
	
	private ACOParameters acoParameters;
	
    private int          mx, my;          /* mouse coordinates */
    private int          mode    = 0;     /* mouse operation mode */
    private double       scale, factor;   /* buffer for scaling factor */ 
	
	
	
    /**************** Initializer *********************/    
	public GuiPanel() {
		super("ACO GTSP");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		
		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(createMenu(), BorderLayout.NORTH);
		
	    /* --- create and set the main panel --- */
	    this.panel = new ACOPanel(gtsp);
	    this.panel.setLayout(new BorderLayout());
	    this.panel.setPreferredSize(new Dimension(656, 656));
//	    this.getContentPane().add(this.panel, BorderLayout.CENTER); 
	    this.panel.addMouseListener(this);
	    this.panel.addMouseMotionListener(this);
	    this.scroll = new JScrollPane(this.panel);
	    this.getContentPane().add(this.scroll, BorderLayout.CENTER);
		
		
		/* --- create and set a status bar --- */
	    this.status = new JTextField("");
	    this.status.setEditable(false);
	    this.getContentPane().add(this.status, BorderLayout.SOUTH);

	}
	
	/*--------------------------------------------------------------------*/
	
	  private JFileChooser createChooser ()
	  {                             /* --- create a file chooser */
	    JFileChooser fc = new JFileChooser("data/instances");
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setFileHidingEnabled(true);
	    fc.setAcceptAllFileFilterUsed(true);
	    fc.setMultiSelectionEnabled(false);
	    fc.setFileView(null);       /* create a standard file chooser */
	    return fc;                  /* without customized filters */
	  }  /* createChooser() */
	  
	  /*--------------------------------------------------------------------*/
	  
	  public void loadGTSP (File file)
	  {                             /* --- load a traveling salesman p. */
	    if (file == null) {         /* if no file name is given */
	      if (chooser == null) chooser = createChooser();
	      chooser.setDialogType(JFileChooser.OPEN_DIALOG);
	      int r = chooser.showDialog(this, null);
	      if (r != JFileChooser.APPROVE_OPTION) return;
	      file = chooser.getSelectedFile();
	    }                           /* get the selected file */
	    try {                       /* load the traveling salesman prob. */
	      this.gtsp = new Gtsp(file);
	      this.panel.setGtsp(this.gtsp);
	      this.status.setText("traveling salesman problem loaded ("
	                        +file.getName() +"). Manage the zoom by clicking on the mouse wheel"); }
	    catch (IOException e) {
	      String msg = e.getMessage();
	      this.status.setText(msg); System.err.println(msg);
	      JOptionPane.showMessageDialog(this, msg,
	        "Error", JOptionPane.ERROR_MESSAGE);
	    }                           /* set the status text */
	    this.currentFile = file;           /* note the new file name */
	    this.repaint();
	  }  /* loadGTSP() */
	  
	  /*--------------------------------------------------------------------*/
	  
	  private JDialog createRunOpt() {
		  final JDialog dlg = new JDialog(this, "Run Optimization...");
		  GridBagLayout      g    = new GridBagLayout();
		  GridBagConstraints lc   = new GridBagConstraints();
		  GridBagConstraints rc   = new GridBagConstraints();
		  JPanel             grid = new JPanel(g);
		  JPanel             bbar;
		  JLabel             lbl;
		  JTextArea          help;
		  JButton            btn;
		  
		  grid.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		  lc.fill      =              /* fill fields in both directions */
		  rc.fill      = GridBagConstraints.BOTH;
		  rc.weightx   = 1.0;         /* resize only the input fields, */
		  lc.weightx   = 0.0;         /* but not the labels */
		  lc.weighty   = 0.0;         /* resize lines equally */
		  rc.weighty   = 0.0;         /* in vertical direction */
		  lc.ipadx     = 10;          /* gap between labels and inputs */
		  lc.ipady     = 10;          /* make all lines of the same height */
		  rc.gridwidth = GridBagConstraints.REMAINDER;

		  lbl = new JLabel("Number of iterations:");
		  g.setConstraints(lbl,  lc);
		  grid.add(lbl);
		  final JSpinner numberOfIterations = new JSpinner(
				  new SpinnerNumberModel(30, 1, 999999, 1));
		  g.setConstraints(numberOfIterations, rc);
		  grid.add(numberOfIterations);
		  
		  lbl = new JLabel("Number of ants:");
		  g.setConstraints(lbl,  lc);
		  grid.add(lbl);
		  final JSpinner numberOfAnts = new JSpinner(
				  new SpinnerNumberModel(30, 1, 999999, 1));
		  g.setConstraints(numberOfAnts, rc);
		  grid.add(numberOfAnts);
		  
		  lbl = new JLabel("Mutation frequency:");
		  g.setConstraints(lbl,  lc);
		  grid.add(lbl);
		  final JSpinner mutationFrequency = new JSpinner(
				  new SpinnerNumberModel(20, 1, 999999, 1));
		  g.setConstraints(mutationFrequency, rc);
		  grid.add(mutationFrequency);
		  
		  lbl = new JLabel("Mutation ratio:");
		  g.setConstraints(lbl,  lc);
		  grid.add(lbl);
		  final JTextField mutationRatio = new JTextField("0.2");
		  g.setConstraints(mutationRatio, rc);
		  grid.add(mutationRatio);
		  
		  lbl = new JLabel("Pheromone Evaporation Factor:");
		  g.setConstraints(lbl,  lc);
		  grid.add(lbl);
		  final JTextField pheromoneEvaporationFactor = new JTextField("0.2");
		  g.setConstraints(pheromoneEvaporationFactor, rc);
		  grid.add(pheromoneEvaporationFactor);
		  
		  lbl = new JLabel("Pheromone Regulation Factor:");
		  g.setConstraints(lbl,  lc);
		  grid.add(lbl);
		  final JTextField pheromoneRegulationFactor = new JTextField("0.2");
		  g.setConstraints(pheromoneRegulationFactor, rc);
		  grid.add(pheromoneRegulationFactor);
		  
		  lbl = new JLabel("Visibility Regulation Factor:");
		  g.setConstraints(lbl,  lc);
		  grid.add(lbl);
		  final JTextField visibilityRegulationFactor = new JTextField("0.2");
		  g.setConstraints(visibilityRegulationFactor, rc);
		  grid.add(visibilityRegulationFactor);
		  
		  bbar = new JPanel(new GridLayout(1, 2, 5, 5));
		    bbar.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 3));
		    btn = new JButton("Ok"); bbar.add(btn);
		    btn.addActionListener(new ActionListener () {
		      public void actionPerformed (ActionEvent e) {
		        dlg.setVisible(false);
		        
		    	float pheromoneEvaporation = Float.parseFloat(pheromoneEvaporationFactor.getText());
		    	float pheromoneRegulation = Float.parseFloat(pheromoneRegulationFactor.getText());
		    	float visibilityRegulation = Float.parseFloat(visibilityRegulationFactor.getText());
		    	float probabilityOfMutation = Float.parseFloat(mutationRatio.getText());
		    	int frequencyOfMutation = (int) mutationFrequency.getValue();
		    	int ants = (int) numberOfAnts.getValue();
		    	int iterations = (int) numberOfIterations.getValue();
		    	
		    	
		        applyParameters(pheromoneEvaporation,
		        		pheromoneRegulation,
		        		visibilityRegulation,
        				ants,
        				iterations,
        				frequencyOfMutation,
        				probabilityOfMutation);
		        
		        
		        runOptimization();
		        
		      } } );

		  
		  btn = new JButton("Apply"); bbar.add(btn);
		  btn.addActionListener(new ActionListener () {
		    public void actionPerformed (ActionEvent e) {
		    	
		    	float pheromoneEvaporation = Float.parseFloat(pheromoneEvaporationFactor.getText());
		    	float pheromoneRegulation = Float.parseFloat(pheromoneRegulationFactor.getText());
		    	float visibilityRegulation = Float.parseFloat(visibilityRegulationFactor.getText());
		    	float probabilityOfMutation = Float.parseFloat(mutationRatio.getText());
		    	
		    	
		    	
		    	int frequencyOfMutation = (int) mutationFrequency.getValue();
		    	int ants = (int) numberOfAnts.getValue();
		    	int iterations = (int) numberOfIterations.getValue();
		    	
		    	
		        applyParameters(pheromoneEvaporation,
		        		pheromoneRegulation,
		        		visibilityRegulation,
        				ants,
        				iterations,
        				frequencyOfMutation,
        				probabilityOfMutation);
		    } } );
		  btn = new JButton("Close"); bbar.add(btn);
		  btn.addActionListener(new ActionListener () {
		    public void actionPerformed (ActionEvent e) {
		      dlg.setVisible(false); } } );
		  
		  dlg.getContentPane().add(grid, BorderLayout.CENTER);
		  dlg.getContentPane().add(bbar, BorderLayout.SOUTH);
		  dlg.setLocationRelativeTo(this);
		  dlg.setLocation(664, 465);
		  dlg.pack();
		  
		  return dlg;
	  }
	  
	  /*--------------------------------------------------------------------*/
 
	public JMenuBar createMenu() {
		  
		JMenuBar mbar = new JMenuBar();
		JMenu menu;
		JMenuItem item;
		
		menu = mbar.add(new JMenu("File"));
		menu.setMnemonic('f');
		item = menu.add(new JMenuItem("Load GTSP..."));
		item.setMnemonic('l');
		item.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Note : only load files in TSPLIB format"
						+ "\nwith at least the following sections:"
						+ "\n-NODE_COORD_SECTION\n-GROUP_SET_SECTION");
				GuiPanel.this.loadGTSP(null); } } );
	    menu.addSeparator();
	    item = menu.add(new JMenuItem("Quit"));
	    item.setMnemonic('q');
	    item.addActionListener(new ActionListener() {
	        public void actionPerformed (ActionEvent e) {
	          System.exit(0); } } );     /* terminate the program */
		menu = mbar.add(new JMenu("Actions"));
		item = menu.add(new JMenuItem("Run Optimization..."));
	    item.setMnemonic('o');
	    item.addActionListener(new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
//	        if (ACODemo.this.runopt == null)
//	          ACODemo.this.runopt = createRunOpt();
//	        ACODemo.this.runopt.setVisible(true);
	    	  //TODO lancer l'optimisation à partir de ce point
	    	  if (gtsp != null) {
		    	  params = createRunOpt();
		    	  params.setVisible(true);
	    	  } else {
	    		  JOptionPane.showMessageDialog(null, "Please load a GTSP file before accessing this");
	    	  }
	      } } );
	    item = menu.add(new JMenuItem("Stop Optimization"));
	    item.setMnemonic('s');
	    item.addActionListener(new ActionListener() {
	      public void actionPerformed (ActionEvent e) {
	    	  stopOptimization();

	        //TODO Stop optimization
	      } } );
	    
		return mbar;
			}
	
	//Conversion des float et int en Float et Integer, et création de l'instance ACOParameters
	public ACOParameters applyParameters(float pheromoneEvaporationFactor,
								float pheromoneRegulationFactor,
								float visibilityRegulationFactor,
								int numberOfAnts,
								int numberOfIterations,
								int frequencyOfMutation,
								float probabilityOfMutation) {
		
		ArrayList<ArrayList<Float>> distances = convertToFloatArrayList(gtsp.getNode_dist());
		ArrayList<ArrayList<Float>> positions = convertToFloatArrayList(gtsp.getNode_coord_section());
		ArrayList<Integer> clusters = convertToIntegerArrayList(gtsp.getGroup_list());
		
		ACOParameters params = new ACOParameters(
				(Integer) gtsp.getDimension(),
				(Integer) gtsp.getGroupDimension(),
				distances,
				positions,
				clusters,
				(Float) pheromoneEvaporationFactor,
				(Float) pheromoneRegulationFactor,
				(Float) visibilityRegulationFactor,
				(Integer) numberOfAnts,
				(Integer) numberOfIterations,
				(Integer) frequencyOfMutation,
				(Float) probabilityOfMutation);
		this.acoParameters = params;
		return params;
	}
	
	/*--------------------------------------------------------------------*/


	public void runOptimization() {
	      //DefaultContextInteractions _$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = Agent.$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
	      //NewOptimization _newOptimization = new NewOptimization(acoParameters);
	      //_$CAPACITY_USE$IO_SARL_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_newOptimization);
	    	}
	
	//TODO code
	public void stopOptimization() {
		status.setText("Stop");
	}
	
	
	
/**************** Mouse moves handlers *********************/
	
	public void mouseDragged (MouseEvent e)
	  {                             /* --- handle mouse movement */
	    JViewport view;             /* viewport of scrollpane */
	    Point     refp;             /* coordinates of upper left corner */
	    Dimension size;             /* extension of viewing area */
	    int       xmax, ymax, d;    /* maximum view position, movement */
	    double    scl;              /* new scaling factor */

	    if ((this.gtsp == null)         /* check for a TSP and */
	    ||  (this.mode <= 0)) return;  /* for a valid operation mode */
	    view = this.scroll.getViewport();
	    refp = view.getViewPosition(); /* get reference point */
	    if (this.mode == 1) {       /* if to do panning */
	      refp.x += this.mx -e.getX();
	      refp.y += this.my -e.getY();
	      size  = this.panel.getPreferredSize();
	      xmax  = size.width; ymax  = size.height;
	      size  = view.getExtentSize();  /* get maximum reference point */
	      xmax -= size.width; ymax -= size.height;
	      if (refp.x > xmax) { this.mx -= refp.x -xmax; refp.x = xmax; }
	      if (refp.x < 0)    { this.mx -= refp.x;       refp.x = 0;    }
	      if (refp.y > ymax) { this.my -= refp.y -ymax; refp.y = ymax; }
	      if (refp.y < 0)    { this.my -= refp.y;       refp.y = 0;    }
	      view.setViewPosition(refp); }
	    else {                      /* if to do scaling */
	      d = (e.getY() -refp.y) -this.my;
	      scl = Math.pow((this.mode > 2) ? 1.004 : 1.02, d);
	      this.panel.setScale(this.scale *scl);
	    }                           /* set new scaling factor */
	    this.panel.revalidate();    /* adapt the enclosing scroll pane */
	    this.panel.repaint();       /* and redraw the TSP */
	  }  /* mouseDragged() */


	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	public void mouseWheelMoved(MouseWheelEvent e) {
	    int notches = e.getWheelRotation();
	    double temp = scale - (notches * 0.2);
	    // minimum zoom factor is 1.0
	    temp = Math.max(temp, 1.0);
	    if (temp != scale) {
	        scale = temp;
	        repaint();
	    }
	}
		
	
	public void mousePressed (MouseEvent me)
	  {                             /* --- handle mouse clicks */
	    int m;                      /* event modifiers */

	    if (this.gtsp == null) return;  /* check for a TSP */
	    this.mode = 0;              /* clear the operation mode */
	    this.mx = me.getX();        /* note the coordinates of the point */
	    this.my = me.getY();        /* at which the mouse was pressed */
	    m = me.getModifiersEx();    /* get the modifiers (buttons) */
//	    System.out.println(m);
	    switch (m) {                /* depending on the mouse button */
	      case InputEvent.BUTTON1_DOWN_MASK:
	        this.mode = 1; break;   /* panning mode */
	      case InputEvent.BUTTON2_DOWN_MASK:  /* scaling (low  factor) */
	      case InputEvent.BUTTON3_DOWN_MASK:  /* scaling (high factor) */
	        this.mode = (m == InputEvent.BUTTON2_DOWN_MASK) ? 2 : 3;
	        this.scale = this.panel.getScale();
	        this.my   -= this.scroll.getViewport().getViewPosition().y;
	        break;                /* adapt position to viewport */
	    }                           /* (set the mouse operation mode) */
	  }  /* mousePressed() */

	  public ArrayList<ArrayList<Float>> convertToFloatArrayList(final float[][] array) {
		    ArrayList<ArrayList<Float>> response = new ArrayList<ArrayList<Float>>();
		    for (int i = 0; (i < array.length); i++) {
		      {
		        ArrayList<Float> temp = new ArrayList<Float>();
		        for (int j = 0; (j < array[i].length); j++) {
		          temp.add(Float.valueOf(array[i][j]));
		        }
		        response.add(temp);
		      }
		    }
		    return response;
		  }
		  
	  public ArrayList<Integer> convertToIntegerArrayList(final int[] array) {
		    ArrayList<Integer> response = new ArrayList<Integer>();
		    for (int i = 0; (i < array.length); i++) {
		      response.add(Integer.valueOf(array[i]));
		    }
		    return response;
		  }
	
	  public void setStatus(String s) {
		  status.setText(s);
	  }
	  
	  public ACOParameters getAcoParameters() {
		  return acoParameters;
	  }
	  
	  public void setAcoParameters(ACOParameters params) {
		  this.acoParameters = params;
	  }
	  
	  public void setPath(ArrayList<Integer> path, Float pathLength ) {
		  panel.setBestTour(path);
		  this.bestPathLength = pathLength;
	  }
	
	
}


