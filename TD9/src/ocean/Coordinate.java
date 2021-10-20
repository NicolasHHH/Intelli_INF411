package ocean;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Référencement des cases de l'océan
 * 
 * <p>
 * L'océan est modélisé par une matrice pleine dont chaque cellule correspond à
 * une case du labyrinthe. Les relations d'adjacence sont implicites, chaque
 * cellule ayant par convention 4 voisines : une à gauche (ouest), une en
 * dessous (sud), une à droite (est), une au dessus (nord). Cette classe fournit
 * plusieurs méthodes pour manipuler les coordonnées de cellules.
 * </p>
 */
public class Coordinate {
	/***************************
	 * Les champs de la classe *
	 ***************************/

	/**
	 * Coordonnée horizontale
	 * 
	 * <p>
	 * Augmentent de gauche (0) à droite
	 * </p>
	 */
	int x;

	/**
	 * Coordonnée verticale
	 * 
	 * <p>
	 * Augmentent de haut (0) vers le bas
	 * </p>
	 */
	int y;

	/*********************
	 * Les constructeurs *
	 *********************/

	/**
	 * Construit une cellule à partir des deux coordonnées
	 * 
	 * @param x coordonnée horizontale
	 * @param y coordonnée verticale
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Construit une cellule à partir d'une autre
	 * 
	 * @param that la cellule à copier
	 */
	public Coordinate(Coordinate that) {
		this.x = that.x;
		this.y = that.y;
	}

	/*************************************************************
	 * Les méthodes permettant de retrouver les cellules voisines
	 *************************************************************/

	/**
	 * Coordonnées de la cellule voisine dans la direction donnée
	 * @param dir direction vers la cellule voisine recherchée
	 * @return les coordonnées de la cellule voisine
	 */
	public Coordinate moveTo(Direction dir) {
		return dir.move(this);
	}

	/**
	 * @return la collection de tous les voisins
	 * 
	 * @param directions la collection de directions vers lesquelles on 
	 * 		  peut se déplacer
	 */
	public Collection<Coordinate> neighbours(Direction[] directions) {
		Collection<Coordinate> l = new LinkedList<Coordinate>();
		for (Direction dir : directions)
			l.add(moveTo(dir));

		return l;
	}

	/**
	 * @return la collection de tous les voisins avec les directions "par défaut"
	 */
	public Collection<Coordinate> neighbours() {
		return neighbours(BasicDirections.values());
	}

	/**************************************************
	 * Rédifinitions des méthodes de la classe Object *
	 **************************************************/

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Coordinate))
			return false;

		Coordinate that = (Coordinate) o;
		return (this.x == that.x) && (this.y == that.y);
	}
}
