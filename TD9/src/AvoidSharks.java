import java.io.FileNotFoundException;
import java.util.LinkedList;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;

/**********************************
 * Question 3 : éviter les requins
 **********************************/

/**
 * Parcours utilisant une Dequeue pour éviter les requins
 */
class AvoidSharks extends BackTrackingTraversal {
	/**
	 * Implémente le parcours évitant les requins
	 */
	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		if (ocean == null || start == null)
			return false;
		// TODO: implémenter le parcours utilisant une Dequeue pour éviter les requins
		// (Question 3)
		
		throw new Error("Méthode AvoidSharks.traverse(Ocean, Coordinate) à implémenter (Question 3)");
	}

	/**
	 * Lance la visualisation du parcours de l'océan dont la carte se trouve dans
	 * {@code one-shark.txt}
	 * 
	 * @param args non utilisé
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String fileName = (args.length < 1) ? "one-shark.txt" : args[0];

		// on crée un nouveau océan à partir de la carte avec requins one-shark.txt
		// et on y associe un nouveau rapporteur qui sera utilisé pour suivre toutes les
		// explorations à la suite
		Ocean sharks = new Ocean(fileName, "data/" + fileName, BasicDirections.values());

		System.out.println("Test evitement des requins (avoid sharks)");
		sharks.reporters().add(new BasicOceanCanvas("Finding Nemo - With Sharks")).exploreUsing(new AvoidSharks());
	}
}
