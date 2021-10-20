package ocean;
/**
 * Énumération de toutes les directions dans lesquelles il est possible de se
 * déplacer dans l'océan. À chaque direction on associe son opposée afin de
 * pouvoir rebrousser le chemin.
 * 
 * <p>
 * Quatre directions : west, south, east et north
 * </p>
 */
public enum BasicDirections implements Direction {
	/*
	 * La liste de directions. 
	 */
	
	/**
	 * Déplacement à gauche dans l'océan
	 * (-1 horizontalement, sans changer la position verticale) 
	 */
	WEST("west", -1, 0), 
	
	/**
	 * Déplacement vers le bas dans l'océan
	 * (+1 verticalement, sans changer la position horizontale) 
	 */
	SOUTH("south", 0, 1), 
	
	/**
	 * L'opposée de {@link WEST}
	 */
	EAST("east", WEST), 
	
	/**
	 * L'opposée de {@link SOUTH}
	 */
	NORTH("north", SOUTH);

	/**
	 * Distance horizontale à parcourir lors d'un déplacement unitaire
	 */
	private final int dx;

	/**
	 * Distance verticale à parcourir lors d'un déplacement unitaire
	 */
	private final int dy;

	/**
	 * L'étiquette textuelle
	 */
	final String label; 

	/**
	 * La direction opposée à celle considérée ({@code this})
	 */
	BasicDirections opposite;	
	
	/**
	 * Le constructeur de base
	 * 
	 * @param label l'étiquette textuelle
	 * @param opposite la direction opposée à celle en train d'être construite
	 * @param dx distance horizontale à parcourir lors d'un déplacement unitaire
	 * @param dy distance verticale à parcourir lors d'un déplacement unitaire
	 */
	private BasicDirections(String label, BasicDirections opposite, int dx, int dy) {
		this.label = label;
		this.opposite = opposite;
		this.dx = dx;
		this.dy = dy;

		// les deux directions sont mutuellement opposées
		if (opposite != null)
			opposite.opposite = this;
	}

	/**
	 * Le constructeur sans direction opposée
	 * 
	 * @param label l'étiquette textuelle
	 * @param dx distance horizontale à parcourir lors d'un déplacement unitaire
	 * @param dy distance verticale à parcourir lors d'un déplacement unitaire
	 */
	private BasicDirections(String label, int dx, int dy) {
		this(label, null, dx, dy);
	}

	/**
	 * Le constructeur avec la direction opposée
	 * 
	 * @param label l'étiquette textuelle
	 * @param opposite la direction opposée à celle en train d'être construite
	 */
	private BasicDirections(String label, BasicDirections opposite) {
		// on construit la direction en iversant les dx et dy de celle opposée
		this(label, opposite, -opposite.dx, -opposite.dy);		
	}

	/**
	 * La direction opposée
	 * 
	 * @return la direction opposée de {@code this}
	 */
	public BasicDirections getOpposite() {
		return opposite;
	}
		
	/**
	 * La description textuelle de la direction
	 * 
	 * @return l'étiquette associée à la direction
	 */
	@Override
	public String toString() {
		return label;		
	}

	/**
	 * Coordonnées de la cellule voisine de {@code coordinate} dans 
	 * cette direction
	 * @param coordinate la cellule dont on cherche la voisine
	 * @return les coordonnées de la cellule voisine
	 */
	@Override
	public Coordinate move(Coordinate coordinate) {
		return new Coordinate(coordinate.x + dx, coordinate.y + dy);
	}
}
