import java.io.FileNotFoundException;
import java.util.LinkedList;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;
import ocean.Traversal;
import reporters.NemoDistanceReporter;

/***********************************
 * Question 2 : parcours en largeur
 ***********************************/

/**
 * Parcours en largeur avec calcul de distance au point de depart (Question 2.2)
 */
class BreadthFirstWithDistance extends Traversal {
	/**
	 * Implémente le parcours en largeur avec calcul de distance au point de depart
	 */
	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		if (ocean == null || start == null)
			return false;

		// TODO: implémenter le parcours en largeur avec calcul de distance au point de
		// depart (Question 2.2)
		throw new Error("Méthode BreadthFirstWithDistance.traverse(Ocean, Coordinate) à implémenter (Question 2.2)");
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

		// on lance l'exploration en largeur avec calcul de la longeur du plus court
		// chemin
		System.out.println("Test parcours en largeur avec distance (breadth first search with distance)");
		noSharks.reporters().add(new BasicOceanCanvas("No Sharks - BFS w/ Distance")).add(new NemoDistanceReporter())
				.exploreUsing(new BreadthFirstWithDistance());
	}
}
