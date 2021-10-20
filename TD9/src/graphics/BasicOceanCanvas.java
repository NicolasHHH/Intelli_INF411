package graphics;
import java.awt.Graphics;
import java.io.FileNotFoundException;

import ocean.BasicDirections;
import ocean.Ocean;
import reporters.OceanReporter;

/**
 * Fournit un rapporteur (voir l'interface {@link OceanReporter}) qui peint
 * l'océan dans une fenêtre utilisant {@link java.awt.Frame java.awt.Frame}.
 */
public class BasicOceanCanvas extends OceanCanvas {
	private static final long serialVersionUID = 1L;

	/**
	 * Construction d'un canevas
	 * @param title texte à afficher dans la barre de titre de à la fenêtre
	 */
	public BasicOceanCanvas(String title) {
		super(title);
	}

	@Override
	protected int computeSizeX() {
		return cellSize * ocean.size();	
	}
	
	@Override
	protected int computeSizeY() {
		return cellSize * ocean.size();
	}
	
	@Override
	protected void drawCell(Graphics g, int x, int y, int size) {
		g.fillRect(x * size, y * size, size, size);
	}

	@Override
	protected void drawMark(Graphics g, int x, int y, int size) {
		g.fillOval(x * size + size / 4, y * size + size / 4, size - size / 2, size - size / 2);
	}

	/**
	 * Test du canevas
	 * 
	 * <p>
	 * Charge l'océan à partir d'un fichier, puis le peint dans un canevas
	 * </p>
	 * 
	 * @param args non utilisé
	 * @throws FileNotFoundException si le fichier avec la carte de l'océan n'est
	 *                               pas accessible
	 */
	public static void main(String[] args) throws FileNotFoundException {
		OceanCanvas canvas = new BasicOceanCanvas("Finding Nemo");
		Ocean ocean = new Ocean("one-shark.txt", "data/one-shark.txt", BasicDirections.values());
		canvas.initialise(ocean);
	}

}