package fr.utbm.ia54.acotsp.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.util.Arrays;

import javax.swing.JPanel;

import fr.utbm.ia54.acotsp.Gtsp;
import java.util.ArrayList;



/*--------------------------------------------------------------------*/
class Edge implements Comparable<Edge> {
/*--------------------------------------------------------------------*/

  /* --- instance variables --- */
  protected int i, j;           /* indices of connected vertices */
  protected int c;              /* color index */

  /*------------------------------------------------------------------*/

  public Edge () {}

  public Edge (int i, int j, int c)
  { this.i = i; this.j = j; this.c = c; }

  /*------------------------------------------------------------------*/

  public int compareTo (Edge obj)
  {                             /* --- compare two edges */
    if (this.c < obj.c) return -1;
    if (this.c > obj.c) return +1;
    return 0;                   /* return sign of color difference */
  }  /* compareTo() */

}  /* class Edge() */




public class ACOPanel extends JPanel {
	
	private Gtsp gtsp; // Instance of the gtsp
	private float[][] cities; // Coordinates of the cities
	private double    xoff, yoff; /* translation parameters */
	private Edge[]    edges;      /* (sorted) list of edges */
	private int[]	bestTour; /* best tour found*/
	private double    scale;      /* scaling factor */
	private int[]     xs, ys;     /* screen coordinates */
	private Color[] trailColors; //Colors for pheromone trails
	private Color[] groupColors; //Colors for groups
	private Stroke    thick;      /* stroke for trail drawing */
	private Stroke    thin;       /* stroke for tour drawing */	
	private float[][] pheromoneTrail; /* pheromoneTrail*/
	
	
public ACOPanel() {
		this.gtsp = null;
	    this.xoff  = this.yoff = 0; /* initialize offset */
	    this.scale = 64;          /* and scale */
	    this.groupColors = new Color[256];
	    for (int i = 0; ++i<256;) {
	    	float red = (float) Math.random();
	    	float green = (float) Math.random();
	    	float blue = (float) Math.random();
	    	 
	    	Color col = new Color(red, green, blue);
	    	this.groupColors[i] = col;
	    }
		this.trailColors = new Color[256];
		for (int i = 256; --i >=0;)
			this.trailColors[255-i] = new Color(i/255.0F, i/255.0F, i/255.0F);
	    this.thick = new BasicStroke(7.0F);
	    this.thin  = new BasicStroke(2.0F);
	}

public ACOPanel (Gtsp gtsp) {
		this();
		this.setGtsp(gtsp);
	}
		
public void setGtsp(Gtsp gtsp) {
		this.gtsp = gtsp;
	    if (this.gtsp == null)       /* if no tsp, set default size */
	        this.setPreferredSize(new Dimension(656, 656));
	      else {                      /* if a new TSP was set */
	    	this.cities = gtsp.getNode_coord_section();
	        this.xoff = gtsp.getX();
	        this.yoff = gtsp.getY();   /* set default offset */
	        this.setScale(40.0);      /* and default scale */
	        int n = this.gtsp.getDimension();  /* create the edge vector */
	        this.edges = new Edge[n = (n *(n-1)) >> 1];
	        while (--n >= 0) this.edges[n] = new Edge();
	      }                           /* create the edges */
	      this.revalidate();          /* adapt the enclosing scroll pane */
	      this.repaint();             /* and redraw the TSP */
	    }  /* setTSP() */		
	
public double getScale () { return this.scale; }
	
public void setScale (double scale)
{                             /* --- rescale the display */
  int       i, n;             /* loop variables, number of vertices */
  Dimension d;                /* preferred size of panel */
  int       w, h;             /* size of background rectangle */

  this.scale = scale;         /* set new scaling factor */
  d = new Dimension();        /* compute new preferred size */
  d.width  = (int)(this.gtsp.getWidth()  *scale +16.5);
  d.height = (int)(this.gtsp.getHeight() *scale +16.5);
//  d.width  = (int)(16 * scale +16.5);
//  d.height = (int)(16 * scale +16.5);
  this.setPreferredSize(d);   /* set new preferred size */
  w = 8; h = d.height -8;     /* get the coordinates of the origin */
  n = this.gtsp.getDimension();        /* get the number of points */
  this.xs = new int[n];       /* and create buffer vectors */
  this.ys = new int[n];       /* for the transformed points */
  for (i = n; --i >= 0; ) {   /* traverse the points */
    this.xs[i] = (int)(w +scale *(this.gtsp.getX(i) -this.xoff) +0.5);
    this.ys[i] = (int)(h -scale *(this.gtsp.getY(i) -this.yoff) +0.5);
  }                           /* compute screen coordinates */
}  /* setScale() */

public void paint (Graphics g)
{                             /* --- (re)paint the whole panel */
  int       i, j, k, n;       /* loop variables, number of vertices */
  Dimension d;                /* (preferred) size of panel */
  int       w, h;             /* size of background rectangle */
  int       x, y, ox, oy;     /* coordinates of points */
  double    trl, avg, max;    /* (average/maximal) trail on edge */
  double    scl;              /* scaling factor */
  int[]     tour;             /* best tour found so far */
  Edge      e;                /* to traverse the edges */

  d = this.getSize();         /* get the (preferred) panel size */
  w = d.width; h = d.height;  /* (whichever is larger) */
  d = this.getPreferredSize();
  if (d.width  > w) w = d.width;
  if (d.height > h) h = d.height;
  g.setColor(Color.white);    /* set the background color */
  g.fillRect(0, 0, w, h);     /* draw the background */
  if (this.gtsp == null)       /* if there is no TSP, */
    return;                   /* abort the function */
//  this.setBackground(Color.cyan);
  n = this.gtsp.getDimension();        /* get the number of vertices and */
  w = 8; h = d.height -8;     /* the coordinates of the origin */

  
  
//TODO Afficher la carte des phéromones avec des traits gris d'intensité variable
  /* --- draw the trail --- */
//  if (this.antc != null) {    /* if there is an ant colony */
//    avg = this.antc.getTrailAvg();
//    max = this.antc.getTrailMax();
//    if (max < 2*avg) max = 2*avg;
//    max = 255.0/max;          /* compute color scaling factor */
//    for (k = 0, i = n; --i >= 0; ) {
//      for (j = i; --j >= 0; ) {
//        e   = this.edges[k++];/* traverse the edges */
//        trl = this.antc.getTrail(e.i = i, e.j = j);
//        e.c = (int)(max*trl); /* compute the color index */
//        if (e.c > 255) e.c = 255;
//      }                       /* bound the color index */
//    }                         /* (for programming safety) */
//    Arrays.sort(this.edges, 0, k);
//    ((Graphics2D)g).setStroke(this.thick);
//    for (i = 0; i < k; i++) { /* traverse the edges */
//      e = this.edges[i];      /* get edge and set color */
//      g.setColor(this.cols[e.c]);
//      g.drawLine(this.xs[e.i],this.ys[e.i],this.xs[e.j],this.ys[e.j]);
//    }                         /* draw a line between the vertices */
//    ((Graphics2D)g).setStroke(this.thin);
//    g.setColor(Color.red);    /* draw the best tour in red */
//    tour = this.antc.getBestTour();
//    i    = tour[0];           /* get the tour and its start */
//    for (k = n; --k >= 0; ) { /* traverse the edges of the tour */
//      j = i; i = tour[k];     /* get the next vertex index */
//      g.drawLine(this.xs[i], this.ys[i], this.xs[j], this.ys[j]);
//    }                         /* draw the next edge of the tour */
//  }                           /* (first edge closes tour) */

  /* --- draw the vertices --- */
  for (i = n; --i >= 0; ) {   /* traverse the vertices */
    x = this.xs[i]; y = this.ys[i];
    g.setColor(Color.black);  /* black outline */
    g.fillOval(x-4, y-4, 9, 9);
    
    int group = gtsp.getGroup_list()[i]; /* Choix de la couleur du vertex en fonction du groupe */
    Color col = groupColors[group];
    
    g.setColor(col);    /* red interior */
    g.fillOval(x-3, y-3, 7, 7);
  }                           /* draw a circle */
  
//TODO relier bestTour au sarl
  
  // Test pour affichage du bestTour sur le graph
//  bestTour = new int[3];
//  bestTour[0] = 7-1;
//  bestTour[1] = 8-1;
//  bestTour[2] = 5-1;
  
  /* draw best tour */
  if (bestTour == null) {
//	  System.out.println("Pas de meilleur tour trouvé");
  } else {
	  ((Graphics2D)g).setStroke(this.thin);
	  g.setColor(Color.red);
	  g.drawLine(this.xs[bestTour[0]], this.ys[bestTour[0]], this.xs[bestTour[bestTour.length -1]], this.ys[bestTour[bestTour.length -1]] );
	  for(int cityIndex = 0; cityIndex < bestTour.length-1; cityIndex++) {
		  int city1 = bestTour[cityIndex];
		  int city2 = bestTour[cityIndex+1];
		  g.drawLine(this.xs[city1], this.ys[city1], this.xs[city2], this.ys[city2] );
	  }
  }

}  /* paint() */

public void setBestTour(ArrayList<Integer> integers) {
	int size = integers.size();
	int[] tour =  new int[size];
	
	for (int i = 0 ; i<size ; i++) {
		tour[i] = integers.get(i);
	}
	bestTour = tour;
}

public void setPheromones(ArrayList<ArrayList<Float>> lists) {
	
	int size = lists.size();
	float[][] pheromones = new float[size][size];
	
	for(int i = 0 ; i<size ; i++) {
		int size2 = lists.get(i).size();
		
		float[] row = new float[size2];
		for(int j = 0 ; j<size ; j++) {
			row[j] = lists.get(i).get(j).floatValue();
		}
		pheromones[i] = row;
	}
	pheromoneTrail = pheromones;
	
}

}