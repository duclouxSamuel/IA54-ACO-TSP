package fr.utbm.ia54.acotsp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

public class createSolutions {

	int tailleDossier = 64; // nombre de fichiers dans le dossier des instances
	String [] instances; // Le nom de chaque instance
	float[] optimalParcours = new float[64]; // Le parcours optimal de chaque parcours
	
	public void getSolutions() throws IOException {
		String [] nomsFichiers = new String[tailleDossier] ;
	  File dir = new File("data/instances");
	  File[] directoryListing = dir.listFiles();
	  dir = new File("data/SolutionsText");
	  File[] solutionsListing = dir.listFiles();
	  
	  System.out.println(solutionsListing.toString());
	  if (directoryListing != null) {
		  
		  int i = 0;
		  for (File child : directoryListing) {
		  nomsFichiers[i] = child.getName();
		  
		  
//		  System.out.println(child.getName());
		  for (File sol : solutionsListing) {
			  if (sol.getName().equals(nomsFichiers[i])) {
				  optimalParcours[i] = getOptimalTour(child, sol);
//				  System.out.println(optimalParcours[i]);
				  break;
			  }
		  }
		 
		  i++;
		  System.out.println(child.getName() + ": " + optimalParcours[i-1]);
		  }

	  	}
	}
	
	
	//Retourne le parcours optimal stocké dans le fichier de solutions correspondant au gtsp
	public float getOptimalTour(File gtsp, File sol) throws IOException {
		
		Gtsp child = new Gtsp(gtsp);
		
		float[][] distances = child.getNode_dist(); //On charge la matrice des distances du gtsp
		long sizeOfTour = Files.lines(sol.toPath()).count() -2;
		int[] parcours = new int[(int) sizeOfTour +1];
		
		String line = "";
		
		BufferedReader reader = null;
		reader = new BufferedReader(new FileReader(sol));
				
		//On saute les deux premières lignes qui ne contiennent pas des données de parcours
		line = reader.readLine();
		line = reader.readLine();
		
		float longueurParcours = 0;
		parcours[0] = Integer.parseInt(reader.readLine());
		for(int i = 1; i < sizeOfTour; i++) {
			line = reader.readLine();
			if (line.isEmpty()) break;
			parcours[i] = Integer.parseInt(line);
//			System.out.println("i : " + i + "file : " +  sol.toPath() + distances[parcours[i] - 1][parcours[i-1] - 1] + " size : " + sizeOfTour);
			try {
			longueurParcours += distances[parcours[i] - 1][parcours[i-1] - 1];
			} catch(ArrayIndexOutOfBoundsException e) {
				return 0;
			}
		}
		try {
		longueurParcours += distances[parcours[0] - 1][parcours[(int) (sizeOfTour-1)] - 1];
		} catch(Exception e) {
			return 0;
		}
		reader.close();
		return longueurParcours;
	}
	
	
	public static void main(String args[]) throws IOException {
		createSolutions test = new createSolutions();
		test.getSolutions();
	}
	
	  
	
}
