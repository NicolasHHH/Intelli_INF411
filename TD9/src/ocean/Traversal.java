package ocean;
/**
 * Une classe abstraite fournissant les éléments de base pour un algorithme
 * d'exploration
 */
public abstract class Traversal {
	/***********************************
	 * Les champs et méthodes statiques 
	 ***********************************/

	/**
	 * La marque à poser dans l'océan sur les cases d'un chemin vers Nemo, lorsqu'un
	 * tel chemin est trouvé
	 */
	public static final Mark path = new IntMark(200, "path");

	/**
	 * La marque à poser dans l'océan sur les cases d'un chemin se terminant par un
	 * cul-de-sac
	 */
	public static final Mark deadEnd = new IntMark(100, "deadEnd");

	static final public String NOT_A_DIRECTION = "Mark is not a Direction";
	
	/**
	 * Récupère la marque de la cellule {@code c} sous forme d'une direction
	 * 
	 * @param ocean l'océan que l'on explore
	 * @param c     la cellule dont la marque doit être récupérée
	 * @return la marque posée dans {@code c} sous forme d'une direction
	 */
	public static Direction getDirection(Ocean ocean, Coordinate c) {
		Mark mark = ocean.getMark(c);
		if (mark != null && mark instanceof Direction)
			return (Direction) mark;
		else 
			return null;
	}

	/**
	 * Récupère la marque de la cellule {@code c} sous forme d'un entier
	 * 
	 * @param ocean l'océan que l'on explore
	 * @param c     la cellule dont la marque doit être récupérée
	 * @return la marque posée dans {@code c} sous forme d'un entier
	 */
	public static Integer getInteger(Ocean ocean, Coordinate c) {
		Mark mark = ocean.getMark(c);
		assert (mark != null) && (!Ocean.noMark.equals(mark)) : "Pas de marque dans la cellule " + c + ", où l'on cherche à récupérer un entier";
		assert (mark instanceof IntMark) : "La marque dans la cellule " + c + " n'est pas une IntMark";
		return mark.toInteger();
	}

	/**
	 * Méthode principale qui définit l'algorithme d'exploration. Cette méthode sera
	 * appelée par une des méthodes {@link Ocean#exploreUsing(Traversal)}}
	 * 
	 * <p>
	 * À implémenter dans les classes dérivées !
	 * </p>
	 * 
	 * @param ocean  l'océan à explorer
	 * @param start  la cellule de départ (la plupart de temps, ce seraient les
	 *               coordonnées de la case où se trouve Marin, mais ça peut être
	 *               n'importe quelle cellule)
	 * @return {@code true} si Nemo a été trouvé, {@code false} sinon
	 */
	public abstract boolean traverse(Ocean ocean, Coordinate start);
}
