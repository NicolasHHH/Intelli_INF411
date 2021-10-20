import java.io.FileNotFoundException;
import java.util.LinkedList;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;

/***********************************
 * Question 2 : parcours en largeur
 ***********************************/

/**
 * Le plus court chemin (Question 2.3)
 */
class BreadthFirstWithShortestPath extends BackTrackingTraversal {
	/**
	 * Implémente le parcours en largeur avec marquage du plus court chemin
	 */
	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		if (ocean == null || start == null)
			return false;

		// TODO: implémenter le parcours en largeur avec marquage du plus court chemin
		// (Question 2.3)		
		throw new Error("Méthode BreadthFirstWithShortestPath.traverse(Ocean, Coordinate) à implémenter (Question 2.3)");
	}

	/**
	 * Lance la visualisation du parcours de l'océan dont la carte se trouve dans
	 * {@code no-sharks.txt}
	 * 
	 * @param args non utilisé
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String fileName = (args.length < 1) ? "no-sharks.txt" : args[0];

		// on crée un nouveau océan à partir de la carte sans requins
		// et on y associe un nouveau rapporteur qui sera utilisé pour suivre toutes les
		// explorations à la suite
		Ocean noSharks = new Ocean(fileName, "data/" + fileName, BasicDirections.values());

		// on lance l'exploration en largeur avec l'affichage du plus court chemin
		System.out.println("Test parcours en largeur et plus court chemin (breadth first search with shortest path)");
		noSharks.reporters().add(new BasicOceanCanvas("No Sharks - BFS w/ Shortest Path"))
				.exploreUsing(new BreadthFirstWithShortestPath());
	}
}
