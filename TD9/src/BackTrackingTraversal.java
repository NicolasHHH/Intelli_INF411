import ocean.Coordinate;
import ocean.Direction;
import ocean.Ocean;
import ocean.Traversal;

/***********************************
 * Question 2 : parcours en largeur
 ***********************************/

/**
 * Une classe abstraite, qui étend celle {@link Traversal} avec le rebroussement
 * de chemin (Question 2.3)
 */
abstract class BackTrackingTraversal extends Traversal {
	/**
	 * Implémente le rebroussement de chemin
	 * 
	 * @param ocean   l'océan en train d'être exploré
	 * @param current le point de départ du rebroussement, c'est-à-dire, la cellule
	 *                où l'exploration s'était arrêtée
	 */
	void backTrack(Ocean ocean, Coordinate current) {
		// TODO: Implémenter le rebroussement de chemin en supposant que les cellules 
		// 	     de l'océan sont marquées avec les directions à suivre
		
		throw new Error("Méthode BackTrackingTraversal.backTrack(Ocean, Coordinate, Coordinate) à implémenter (Question 2.3)");
	}
}
