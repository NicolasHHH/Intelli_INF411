import java.io.FileNotFoundException;

import graphics.BasicOceanCanvas;
import graphics.OceanCanvas;
import ocean.BasicDirections;
import ocean.Ocean;
import reporters.NemoDistanceReporter;

/**
 * La classe permettant de visualiser tous les algorithmes d'exploration de
 * l'océan
 */
class FindNemo {
	/**
	 * Lance la visualisation
	 * 
	 * <p>
	 * Deux possibilités :
	 * </p>
	 * <ul>
	 * <li>Afficher tous les parcours dans la même fenêtre avec
	 * {@link #testAllInOneWindow(String, String)}</li>
	 * <li>Afficher chaque parcours dans sa propre fenêtre avec
	 * {@link #testAllInSeparateWindows(String, String)}</li>
	 * </ul>
	 * 
	 * @param fileName le nom du fichier avec la carte de l'océan sans requins
	 * @param withSharks le nom du fichier avec la carte de l'océan avec requins
	 * @param oneWindow {@code true} si tous les parcours doivent être
	 * 		  affichés dans la même fenêtre, {@code false} sinon
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	public static void findNemo(String fileName, boolean oneWindow) throws FileNotFoundException {
		if (oneWindow)
			testAllInOneWindow(fileName);
		else
			testAllInSeparateWindows(fileName);
	}

	/**
	 * Parse les arguments pour lancer la visualisation de l'exploration
	 * 
	 * @param args peut être utilisé pour donner le nom de la carte de l'océan
	 * 			   (par défaut "many-sharks.txt") et pour indiquer si la même 
	 * 			   fenêtre doit être utilisée pour afficher tous les parcours
	 */
	public static void main(String[] args) {
		// On parse les arguments
		String fileName = (args.length < 1) ? "many-sharks.txt" : args[0];
		boolean oneWindow = (args.length < 2) ? false : Boolean.parseBoolean(args[1]);
		
		// On essaie de lancer l'affichage
		// (peut échouer si le fichier avec la carte n'est disponible)
		try {
			findNemo(fileName, oneWindow);
		} catch (FileNotFoundException e) {
			System.err.println("La carte '" + fileName + "' n'a pas été trouvée");
		}
	}

	/**
	 * Lance la visualisation en affichant tous les parcours dans la même fenêtre
	 * 
	 * @param fileName le nom du fichier avec la carte de l'océan
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	private static void testAllInOneWindow(String fileName) throws FileNotFoundException {
		// on crée un nouveau océan à partir de la carte passée en argument
		Ocean ocean = new Ocean("ocean", "data/" + fileName, BasicDirections.values());
		OceanCanvas canvas = new BasicOceanCanvas("Finding Nemo");
		
		// on lance le parcours en profondeur
		System.out.println("Test parcours en profondeur (depth first search)");
		ocean.reporters().add(canvas).exploreUsing(new DepthFirst());

		// et ainsi de suite pour les autres parcours
		System.out.println("Test parcours en largeur (breadth first search)");
		ocean.reporters().add(canvas).exploreUsing(new BreadthFirst());

		System.out.println("Test parcours en largeur avec distance (breadth first search with distance)");
		ocean.reporters().add(canvas).add(new NemoDistanceReporter()).exploreUsing(new BreadthFirstWithDistance());

		System.out.println("Test parcours en largeur et plus court chemin (breadth first search with shortest path)");
		ocean.reporters().add(canvas).exploreUsing(new BreadthFirstWithShortestPath());
		
		System.out.println("Test evitement des requins (avoid sharks)");
		ocean.reporters().add(canvas).exploreUsing(new AvoidSharks());

		System.out.println("Test plus court chemin evitant le plus de requins (avoid sharks)");
		ocean.reporters().add(canvas).exploreUsing(new Bonus());

	}

	/**
	 * Lance la visualisation en affichant chaque parcours dans sa propre fenêtre
	 * 
	 * @param fileName le nom du fichier avec la carte de l'océan
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	public static void testAllInSeparateWindows(String fileName) throws FileNotFoundException {
		// on crée un nouveau océan à partir de la carte sans requins passée en argument
		Ocean ocean = new Ocean("ocean", "data/" + fileName, BasicDirections.values());

		// on lance l'exploration en profondeur avec l'affichage dans une nouvelle
		// fenêtre
		System.out.println("Test parcours en profondeur (depth first search)");
		ocean.reporters().add(new BasicOceanCanvas("Depth First Search")).exploreUsing(new DepthFirst());

		// on lance l'exploration en largeur avec l'affichage dans une nouvelle fenêtre
		System.out.println("Test parcours en largeur (breadth first search)");
		ocean.reporters().add(new BasicOceanCanvas("Breadth First Search")).exploreUsing(new BreadthFirst());

		// on lance l'exploration en largeur avec calcul de la longeur du plus court
		// chemin et l'affichage dans une nouvelle fenêtre
		System.out.println("Test parcours en largeur avec distance (breadth first search with distance)");
		ocean.reporters().add(new BasicOceanCanvas("BFS w/ Distance")).add(new NemoDistanceReporter())
				.exploreUsing(new BreadthFirstWithDistance());

		// on lance l'exploration en largeur avec l'affichage du parcours et du plus
		// court chemin dans une nouvelle fenêtre
		System.out.println("Test parcours en largeur et plus court chemin (breadth first search with shortest path)");
		ocean.reporters().add(new BasicOceanCanvas("BFS w/ Shortest Path")).exploreUsing(new BreadthFirstWithShortestPath());

		// on lance l'exploration en évitant les requins dans une nouvelle fenêtre
		System.out.println("Test evitement des requins (avoid sharks)");
		ocean.reporters().add(new BasicOceanCanvas("Avoid Sharks")).exploreUsing(new AvoidSharks());

		// on lance l'exploration bonus dans une nouvelle fenêtre
		System.out.println("Test plus court chemin evitant le plus de requins (avoid sharks)");
		ocean.reporters().add(new BasicOceanCanvas("Avoid Sharks - Shortest Path")).exploreUsing(new Bonus());

	}

}
