import java.io.FileNotFoundException;
import java.util.LinkedList;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;
import ocean.Traversal;

/***********************************
 * Question 2 : parcours en largeur
 ***********************************/

/**
 * Parcours en largeur simple (Question 2.1)
 */
class BreadthFirst extends Traversal {
	/**
	 * Implémente le parcours en largeur simple
	 */
	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		if (ocean == null || start == null)
			return false;

		// TODO: implémenter le parcours en largeur (Question 2.1)
		
		throw new Error("Méthode BreadthFirst.traverse(Ocean, Coordinate) à implémenter (Question 2.1)");
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

		// on lance le parcours en largeur
		System.out.println("Test parcours en largeur (breadth first search)");
		noSharks.reporters().add(new BasicOceanCanvas("No Sharks - BFS")).exploreUsing(new BreadthFirst());
	}
}
