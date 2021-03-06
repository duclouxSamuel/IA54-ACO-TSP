package fr.utbm.ia54.acotsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Gtsp {
	
	
	/** Attributs **/
	
	private String name; /* Nom du probl�me*/
	
	private int dimension; /* Taille du probl�me*/
	
	private int groupDimension; /* Nombre de groupes dans le probl�me */
	
	private float[][] nodeCoordSection; /* matrice de positions */
	
	private int[] groupList; /* Liste des appartenances aux clusters (correspond � GTSP_SET_SECTION)*/

	private float[][] nodeDist; /* Matrice de distances */
	
	private   double     bbx, bby;/* bounding box of vertices */
	  
	private   double     bbw, bbh;/* (position and width and height) */
	
	private   boolean    valid;   /* flag for valid bounding box */	
	
	
	/** Initializers **/
	
	public Gtsp() {	}
	
	public Gtsp(File file) throws IOException {
		this.load(file);
	}

	public Gtsp(String name, int dimension, float[][] node_coord_section, 
				int[] group_list, float[][] node_dist) {
		this.name = name;
		this.dimension = dimension;
		this.nodeCoordSection = node_coord_section;
		this.groupList = group_list;
		this.nodeDist = node_dist;
	}
	
	
	/** Getters & Setters **/
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDimension() {
		return dimension;
	}

	public void setDimension(int dimension) {
		this.dimension = dimension;
	}

	public float[][] getNode_coord_section() {
		return nodeCoordSection;
	}

	public void setNode_coord_section(float[][] node_coord_section) {
		this.nodeCoordSection = node_coord_section;
	}

	public int[] getGroup_list() {
		return groupList;
	}

	public void setGroup_list(int[] group_list) {
		this.groupList = group_list;
	}

	public float[][] getNode_dist() {
		return nodeDist;
	}

	public void setNode_dist(float[][] node_dist) {
		this.nodeDist = node_dist;
	}

	public int getGroupDimension() {
		return groupDimension;
	}

	public void setGroupDimension(int groupDimension) {
		this.groupDimension = groupDimension;
	}


	/** Methodes **/
	
	/* Charge un fichier tsp dans l'objet GTSP */
	public void load(File file) throws IOException {
		BufferedReader reader = null;
		String line = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				
				if (line.equals("GTSP_SET_SECTION:")) {
					/* Load Group_list */
					this.loadGtspSetSection(reader);
					
				} else if (line.equals("NODE_COORD_SECTION")) {	
					/* Load node_coord_section */
					this.loadNodeCoordSection(reader);
					
				} else if (line.equals("EOF")) {
					break;
				} else if (line.isEmpty()) {
					//do nothing
				} else {
					String[] tokens = line.split(":");
					String key = tokens[0].trim();
					//System.out.println(tokens[0].trim());
					//String value = tokens[1].trim();
					
					if (key.equals("NAME")) {
						name = tokens[1].trim();
					} else if (key.equals("DIMENSION")) {
						dimension = Integer.parseInt(tokens[1].trim());
					}
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}	
		/* Load Node_dist_section*/
		this.loadGtspNodeDist();
		
//		System.out.println("Le fichier GTSP est charg�");
//		System.out.println("Nombre de groupes :" + groupDimension);
//		System.out.println("Nombre de villes :" + dimension);
	}
	
	
	/* Charge la section des coordonn�es dans l'objet GTSP */
	public void loadNodeCoordSection(BufferedReader reader) throws IOException {
		
		float[][] coord_list = new float[dimension][2];
		
		for (int i = 0; i < dimension; i++) {
			String line = reader.readLine();
			String[] tokens = line.trim().split("\\s+");

			if (tokens.length != 3) {
				throw new IOException(
						"invalid number of tokens for node entry");
			}

			float[] position = new float[2];
			//int id = Integer.parseInt(tokens[0]);

			for (int j = 0; j < 2; j++) {
				position[j] = Float.parseFloat(tokens[j+1]);
			}
			coord_list[i][0] = position[0];
			coord_list[i][1] = position[1];
		}
		nodeCoordSection = coord_list;
	}
	
	
	/* Charge la section des appartenances aux groupes dans le GTSP */
	public void loadGtspSetSection(BufferedReader reader) throws IOException {
		
		String line = "";
		int[] groupListTemp = new int[dimension];
		int clusterId = 1;
		while(true) {
			line = reader.readLine();
			if(line.equals("EOF")) {
				break;
			}

			String[] tokens = line.trim().split("\\s");
			
			int i = 1;
			while(true) {
				if(tokens[i].equals("-1")) {
					break;
				}
				int node = Integer.parseInt(tokens[i]);
				groupListTemp[node-1] = clusterId;
				i++;
			}
			clusterId++;
		}
		groupList = groupListTemp;
		groupDimension = clusterId-1;
	
		
	}
	
	/* Initialise la matrice des distances dans le GTSP */
	public void loadGtspNodeDist() {
		
		float[][] coordinates = nodeCoordSection;
		float[][] distances = new float[dimension][dimension];
		
		for(int i = 0; i < dimension; i++) {
			for(int j = 0; j < dimension; j++) {
				
				if (j==i) { 				//La distance entre une ville et elle m�me est 0
					distances[i][j] = 0;
				} else {					//Calcul de la distance Euclidienne
					
					float xi = coordinates[i][0];
					float yi = coordinates[i][1];
					float xj = coordinates[j][0];
					float yj = coordinates[j][1];
					double distance_x_y = Math.sqrt(Math.pow((xj-xi),2) + Math.pow((yj-yi),2));
					distances[i][j] = (float) distance_x_y;
				}	
			}
		}
		
		nodeDist = distances;
		
	}
	
	private void bbox ()
	{                             /* --- compute bounding box */
	    int    i;                   /* loop variable */
	    double x, y;                /* coordinates of a vertex */
	    double xmax, ymax;          /* maximal x- and y-coordinates */

	    this.bbx = Double.MAX_VALUE; xmax = -Double.MAX_VALUE;
	    this.bby = Double.MAX_VALUE; ymax = -Double.MAX_VALUE;
	    for (i = this.nodeCoordSection.length; --i >= 0; ) {
	      x = this.nodeCoordSection[i][0];          /* traverse the vertices */
	      y = this.nodeCoordSection[i][1];          /* of the problem */
	      if (x < this.bbx) this.bbx = x;
	      if (x > xmax)     xmax     = x;
	      if (y < this.bby) this.bby = y;
	      if (y > ymax)     ymax     = y;
	    }                           /* find minimum and maximum coords. */
	    this.bbw = xmax -this.bbx;  /* compute the width and height */
	    this.bbh = ymax -this.bby;  /* of the bounding box */
	    this.valid = true;          /* the bounding box is now valid */
	  }  /* bbox() */


	  public double getX ()
	  { if (!this.valid) this.bbox(); return this.bbx; }

	  public double getY ()
	  { if (!this.valid) this.bbox(); return this.bby; }

	  public double getWidth ()
	  { if (!this.valid) this.bbox(); return this.bbw; }

	  public double getHeight ()
	  { if (!this.valid) this.bbox(); return this.bbh; }

	public double getX(int i) {
		return this.nodeCoordSection[i][0];
	}

	public double getY(int i) {
		return this.nodeCoordSection[i][1];
	}
}
