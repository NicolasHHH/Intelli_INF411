package ocean;

public interface Direction extends Mark {
	/**
	 * La direction opposée
	 * 
	 * @return la direction opposée de {@code this}
	 */
	public Direction getOpposite();

	/**
	 * Coordonnées de la cellule voisine de {@code coordinate} dans 
	 * cette direction
	 * @param coordinate la cellule dont on cherche la voisine
	 * @return les coordonnées de la cellule voisine
	 */
	public Coordinate move(Coordinate coordinate);

	/**
	 * La position de cette direction dans la liste de toutes
	 * les directions possibles
	 * @return La position à compter de 0
	 * @deprecated N'est gardée que pour ne pas casser les tests 23, 3 et 4 
	 */
	public int ordinal();

	/**
	 * Encodage de la direction sous forme d'un nombre entier (nécessaire pour
	 * implémenter l'interface {@link Mark})
	 */
	@Override
	default public Integer toInteger() {
		// Les ordinaux dans une énumération commencent à 0. Afin de maintenir les mêmes
		// valeurs qui ont été utilisées dans la version précédente du TD (et, par
		// conséquence, les même couleurs à l'affichage) on rajoute 1.
		return ordinal() + 1;
	}
	
}
