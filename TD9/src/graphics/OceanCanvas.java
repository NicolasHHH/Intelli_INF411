package graphics;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;
import reporters.OceanReporter;

/**
 * Fournit un rapporteur (voir l'interface {@link OceanReporter}) qui peint
 * l'océan dans une fenêtre utilisant {@link java.awt.Frame java.awt.Frame}.
 */
public abstract class OceanCanvas extends Canvas implements OceanReporter {
	protected static final long serialVersionUID = 1L;
	/**
	 * La fenêtre où l'océan sera peint
	 */
	protected final Frame frame;

	/**
	 * Référence vers l'océan à peindre
	 */
	protected Ocean ocean;

	/**
	 * La taille d'une cellule de l'océan (son sens précis dépend du maillage)
	 */
	protected final int cellSize;

	/**
	 * Crée un canevas et une fenêtre pour afficher le labyrinthe dedans
	 * 
	 * @param title texte à afficher dans la barre de titre de à la fenêtre
	 * @param cellSize la taille d'une cellule 
	 */
	public OceanCanvas(String title, int cellSize) {
		this.cellSize = cellSize;
		
		frame = new Frame(title);
		frame.setLocation(100, 100);

		// Fermeture de la fenêtre provoquera l'arrêt immédiat de l'application
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Il est important de rajouter le panel pour que la taille de la fenêtre puisse
		// être calculée automatiquement (voir frame.pack() dans initialise() )
		Panel p = new Panel();
		p.add(this);
		frame.add(p);
	}

	/**
	 * Crée un canevas en utilisant la taille de cellule par défaut (10 points)
	 * @param title texte à afficher dans la barre de titre de à la fenêtre
	 */
	public OceanCanvas(String title) {
		this(title, 10);
	}	
	
	/**
	 * Calcule la largeur du canevas nécessaire pour accommoder l'océan
	 * @return la largeur du canevas en pixels 
	 */
	protected abstract int computeSizeX();

	/**
	 * Calcule la hauteur du canevas nécessaire pour accommoder l'océan
	 * @return la hauteur du canevas en pixels 
	 */
	protected abstract int computeSizeY();
	
	/**
	 * Définit la taille de la fenêtre en fonction de celle de l'océan
	 */
	@Override
	public void initialise(Ocean ocean) {
		// stocke la référence de l'océan pour utilisation ultérieure
		this.ocean = ocean;
		// il est important d'appeler setSize sur this et non sur frame pour que la
		// taille soit calculée correctement
		setSize(computeSizeX(), computeSizeY());

		// calcule la taille de la fenêtre en fonction de la taille de this
		frame.pack();
		// rend la fenêtre visible
		frame.setVisible(true);
		// empêche le changement de taille de la fenêtre
		// il est important de le faire après frame.setVisible(true) -- sinon la fenêtre
		// se déplace créant un effet visuel désagréable
		frame.setResizable(false);
	}

	/**
	 * Rafraîchit la fenêtre lorsque le marquage de l'océan évolue
	 * 
	 * @param current non utilisé
	 * @param old non utilisés
	 */
	@Override
	public void report(Coordinate current, Mark old) {
		repaint();
		slow();
	}

	/**
	 * Attend une seconde
	 */
	@Override
	public void finish() {
		slow(1000);
	}

	/**
	 * Replique le comportement de {@code Canvas.update(Graphics g)} sans effacer
	 * avant pour eviter que l'image ne clignote lors d'un rafraichissement de la
	 * fenêtre
	 */
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * Méthode auxilière
	 * 
	 * <p>
	 * Convertit la valeur d'une marque dans une couleur pour l'affichage graphique
	 * </p>
	 * 
	 * @param mark la valeur à convertir
	 * @return la couleur correspondante
	 */
	protected Color colorFromMark(int mark) {
		// on decompose la roue chromatique en 360 degres
		int h = ((int) (3 * mark)) % 360;

		// conversion en rgb
		int hi = (int) Math.floor((float) h / 60) % 6;
		float f = (float) h / 60 - hi;
		float p = 0;
		float q = 1 - f;

		switch (hi) {
		case 0:
			return new Color(1, f, p);
		case 1:
			return new Color(q, 1, p);
		case 2:
			return new Color(p, 1, f);
		case 3:
			return new Color(p, q, 1);
		case 4:
			return new Color(f, p, 1);
		case 5:
			return new Color(1, p, q);
		default:
			throw new Error("Probleme de conversion hsv->rgb");
		}
	}

	/**
	 * Méthode auxiliaire pour dessiner une cellule
	 * @param g contexte graphique
	 * @param x coordonnée x du point de référence 
	 * @param y coordonnée x du point de référence
	 * @param size parametre définissant la taille de la cellule 
	 * 		  (utilisation peut varier en fonction de la forme de la cellule)
	 */
	protected abstract void drawCell(Graphics g, int x, int y, int size);
	
	/**
	 * Méthode auxiliaire pour dessiner une marque
	 * @param g contexte graphique
	 * @param x coordonnée x du point de référence 
	 * @param y coordonnée x du point de référence
	 * @param size parametre définissant la taille de la cellule 
	 * 		  (utilisation peut varier en fonction de la forme de la cellule)
	 */
	protected abstract void drawMark(Graphics g, int x, int y, int size);
	
	/**
	 * Peint l'océan dans la fenêtre
	 * 
	 * @param g the specified Graphics context
	 */
	@Override
	public void paint(Graphics g) {
		// dessiner les murs et les marques
		for (int x = 0; x < ocean.size(); x++) {
			for (int y = 0; y < ocean.size(); y++) {
				Coordinate c = new Coordinate(x, y);
				if (ocean.isWall(c)) {
					g.setColor(Color.gray);
					drawCell(g, x, y, cellSize);
				} else if (ocean.isThereASharkAt(c)) {
					g.setColor(Color.black);
					drawCell(g, x, y, cellSize);
				} else if (ocean.isNemoAt(c)) {
					g.setColor(Color.orange);
					drawCell(g, x, y, cellSize);
				} else if (ocean.isMarlinAt(c)) {
					g.setColor(Color.red);
					drawCell(g, x, y, cellSize);
				} else if (ocean.isMarked(c)) {
					// la teinte est fonction de la marque
					g.setColor(colorFromMark(ocean.getMark(c).toInteger()));
					drawMark (g, x, y, cellSize);
				} else { // (x,y) n'est pas marqué
					g.setColor(getBackground());
					drawCell(g, x, y, cellSize);
					g.setColor(Color.white);
					drawMark (g, x, y, cellSize);
				}
			}
		}
	}

	/**
	 * Méthode auxiliaire pour ralentir l'exploration afin qu'elle soit observable à
	 * l'œil
	 * 
	 * @param duration nombre de millisecondes à atteindre
	 */
	public static void slow(int duration) {
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Méthode auxiliaire pour ralentir l'exploration de 10 millisecondes
	 */
	public static void slow() {
		slow(5);
	}

	/*
	 * Méthodes utilisés pour la coordination avec d'autres rapporteurs
	 * lors de l'activation ou désactivation de ceux-là
	 */
	
	@Override
	public boolean notifySuspension() {
		return true;
	}

	@Override
	public void cancelSuspension() { }

	@Override
	public boolean notifyActivation() {
		return true;
	}
	
	@Override
	public void cancelActivation() { }

}