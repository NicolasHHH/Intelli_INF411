import java.io.FileNotFoundException;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;

/***************************************
 * Question 4 : question complementaire
 ***************************************/

class Bonus extends BackTrackingTraversal {

	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {
		// TODO: implémenter le parcours trouvant le plus court chemin tout en evitant
		// le plus de requins (Question 4)
		
		throw new Error("Méthode Bonus.traverse(Ocean, Coordinate) à implémenter (Question 4)");
	}

	/**
	 * Lance la visualisation du parcours de l'océan dont la carte se trouve dans
	 * {@code one-shark.txt}
	 * 
	 * @param args non utilisé
	 * @throws FileNotFoundException lorsque la carte n'est pas accessible
	 */
	public static void main(String[] args) throws FileNotFoundException {
		String fileName = (args.length < 1) ? "many-sharks.txt" : args[0];

		// on crée un nouveau océan à partir de la carte avec requins one-shark.txt
		// et on y associe un nouveau rapporteur qui sera utilisé pour suivre toutes les
		// explorations à la suite
		Ocean sharks = new Ocean(fileName, "data/" + fileName, BasicDirections.values());

		System.out.println("Test plus court chemin evitant le plus de requins (avoid sharks)");
		sharks.reporters().add(new BasicOceanCanvas("Avoid Sharks - Shortest Path")).exploreUsing(new Bonus());
	}
}
