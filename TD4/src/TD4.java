/* TD4. Rebroussement, solution et génération de labyrinthes
 * Ce fichier contient 2 classes:
 * 	- Cell modélise une case du labyrinthe,
 * 	- Maze modélise un labyrinthe.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import java.util.stream.Collectors;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.nio.file.Files;
import java.nio.file.Paths;


class Cell { // modélise une case du labyrinthe

	private boolean isMarked; // si la case est marquée
	private boolean isExit; // si la case est la sortie du labyrinthe
	private ArrayList<Cell> neighbors; // liste des cases voisines
	private ArrayList<Boolean> walls; // pour chacune des cases voisines, s'il y a un mur dans sa direction
	private Maze maze; // référence au labyrinthe
	public Cell next;


	Cell(Maze maze) { 
		isMarked = false;
		isExit = false;
		neighbors = new ArrayList<>();
		walls = new ArrayList<>();
		this.maze = maze;
	}

	// renvoie la liste des cases voisines
	// seulement celles sans mur si ignoreWalls==false
	List<Cell> neighbors(boolean ignoreWalls) {
		if(ignoreWalls)
			return new ArrayList<>(neighbors);
		else
			return neighbors.stream().filter(cell -> this.hasPassageTo(cell)).collect(Collectors.toList());
	}

	// renvoie true ssi la case c est une voisine et il n'y a pas de mur dans sa direction
	boolean hasPassageTo(Cell c) {
		if(c == null || this.neighbors.indexOf(c) == -1 || c.neighbors.indexOf(this) == -1)
			return false;

		return !this.walls.get(this.neighbors.indexOf(c));
	}

	// supprime le mur dans la direction de la case c
	void breakWall(Cell c) {
		if(c == null || this.neighbors.indexOf(c) == -1 || c.neighbors.indexOf(this) == -1)
			throw new IllegalArgumentException("cells are not neighbors");

		this.walls.set(this.neighbors.indexOf(c), false);
		c.walls.set(c.neighbors.indexOf(this), false);
	}

	// renvoie true ssi la case est marquée
	boolean isMarked() {
		return isMarked;
	}

	// ajoute une marque si b==true, enlève la marque si b==false
	void setMarked(boolean b) {
		isMarked = b;
	}

	// renvoie true ssi la case est une sortie
	boolean isExit() {
		return isExit;
	}

	// déclare la case comme sortie si b==true, déclare la case comme non-sortie si b==false
	void setExit(boolean b) {
		isExit = b;
	}

	// renvoie true ssi la case est isolée (i.e. a des murs dans toutes les directions)
	boolean isIsolated() {
		return neighbors(false).isEmpty();
	}

	// rajoute une case dans la liste des cases voisines
	void addNeighbor(Cell n) {
		neighbors.add(n);
		walls.add(true);
	}


	// Question 1

	// renvoie true ssi il existe un chemin de la case actuelle vers une sortie
	boolean searchPath() {
		maze.slow();
		setMarked(true);
		if (isExit==true) {
			return true;
		}
		for(Cell voisin : neighbors(false)) {
			if(voisin.isMarked() ==false && voisin.searchPath()==true) {
				return true;
			}
		}
		setMarked(false);
		return false;
	}


	// Question 2

	// génère un labyrinthe parfait par rebroussement récursif
	void generateRec() {
		maze.slow();
		ArrayList<Cell> voisinage = new ArrayList<>();
		
		for(Cell voisin1 : neighbors(true)) {
			if(voisin1.isIsolated() == true) {
				voisinage.add(voisin1);
			}
		}
		Collections.shuffle(voisinage);
		for(Cell voisin : voisinage) {
			if(voisin.isIsolated() == true) {
				breakWall(voisin);
				voisin.generateRec();
			}
		}
	}
}


class Maze { // modélise un labyrinthe

	private int height, width;
	private Cell[][] grid;


	// Question 3

	// génère un labyrinthe parfait par rebroussement itératif
	void generateIter(int selectionMethod) {
		Bag cells = new Bag(selectionMethod);
		cells.add(getFirstCell());

		while(!cells.isEmpty()) {
			slow();
			Cell current = cells.peek();
			
			// les cases adjacents dans un ordre aléa
			ArrayList<Cell> voisinage = new ArrayList<>();
			for(Cell voisin1 : current.neighbors(true)) {
				if(voisin1.isIsolated() == true) {
					voisinage.add(voisin1);
				}
			}
			Collections.shuffle(voisinage);
			//
			
			
			for(Cell voisin : voisinage) {
				if(voisin.isIsolated() == true) {
					current.breakWall(voisin);
					cells.add(voisin);
					break;
				}
			}
			if(voisinage.isEmpty()) {
				cells.pop();
			}
		}
	}


	// Question 4

	// génère un labyrinthe avec l'algorithme de Wilson
	void generateWilson() {
		// générateur aléa
		ArrayList<Cell> cells = new ArrayList<Cell>();
		for (Cell[] row : grid)
			for (Cell c : row)
				cells.add(c);
		Collections.shuffle(cells);
		
		// 2. Initialiser un labyrinthe partiel en marquant une case
		cells.get(0).setMarked(true);
		//????
		Random rng = new Random();
		// 3. Tant qu'il reste un cell non marqué...
		
		for (Cell cell : cells) {
			if (cell.isMarked())
				continue;
			// Marche alÃ©atoire partant de `cell`, jusqu'Ã  tomber sur une case marquÃ©e
			for (Cell current = cell; !current.isMarked(); current = current.next) {
				List<Cell> neighbors = current.neighbors(true);
				current.next = neighbors.get(rng.nextInt(neighbors.size()));
			}
			// On repart de `cell`, et on suit le chemin (sans les boucles), tout en
			// effaçant les murs. L'effacement des boucles est implicite
			for (Cell current = cell; !current.isMarked(); current = current.next) {
				slow();
				current.breakWall(current.next);
				current.setMarked(true); // Ce chemin est ajouté au labyrinthe
			}
		}
	}

	// renvoie la cellule avec les coordonnées (i, j)
	Cell getCell(int i, int j) {
		if(i < 0 || i >= height || j < 0 || j >= width)
			throw new IllegalArgumentException("invalid indices");

		return grid[i][j];
	}

	// renvoie la cellule avec les coordonnées (0, 0)
	Cell getFirstCell() {
		return getCell(0, 0);
	}

	// traduit coordonnées en numéro de cellule
	int coordToInt(int i, int j) {
		if(i < 0 || i >= height || j < 0 || j >= width)
			throw new IndexOutOfBoundsException();

		return i*width + j;
	}

	// traduit un numéro de cellule en Coordinate
	Coordinate intToCoord(int x) {
		if(x < 0 || x >= height*width)
			throw new IndexOutOfBoundsException();

		return new Coordinate(x/width, x%width);
	}


	// ralentit l'affichage du labyrinthe si une fenêtre graphique est ouverte
	void slow(){
		if (frame == null) return;

		try {
			Thread.sleep(10);
			frame.repaint();
		} catch (InterruptedException e) {}
	}

	private MazeFrame frame;
	private static final int step = 20;

	Maze(int height, int width) {
		this(height, width, true);
	}

	Maze(int height, int width, boolean window) {
		if((height <= 0) || (width <= 0))
			throw new IllegalArgumentException("height and width of a Maze must be positive");

		this.height = height;
		this.width = width;

		grid = new Cell[height][width];

		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				grid[i][j] = new Cell(this);

		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				if(i < height - 1) {
					grid[i][j].addNeighbor(grid[i+1][j]);
					grid[i+1][j].addNeighbor(grid[i][j]);
				}

				if(j < width - 1) {
					grid[i][j].addNeighbor(grid[i][j+1]);
					grid[i][j+1].addNeighbor(grid[i][j]);
				}
			}
		}

		grid[height-1][width-1].setExit(true);

		if(window)
			frame = new MazeFrame(grid, height, width, step);
	}

	Maze(String path) throws IOException {
		this(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8));
	}

	Maze(String path, boolean window) throws IOException {
		this(Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8), window);
	}

	Maze(List<String> lines) {
		this(lines, true);
	}

	Maze(List<String> lines, boolean window) {
		if(lines.size() < 2)
			throw new IllegalArgumentException("too few lines");

		this.height = Integer.parseInt(lines.get(0));
		this.width = Integer.parseInt(lines.get(1));

		this.grid = new Cell[height][width];
		for(int i = 0; i < height; ++i)
			for(int j = 0; j < width; ++j)
				grid[i][j] = new Cell(this);

		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				if(i < height - 1) {
					grid[i][j].addNeighbor(grid[i+1][j]);
					grid[i+1][j].addNeighbor(grid[i][j]);
				}

				if(j < width - 1) {
					grid[i][j].addNeighbor(grid[i][j+1]);
					grid[i][j+1].addNeighbor(grid[i][j]);
				}
			}
		}

		grid[height-1][width-1].setExit(true);

		int i = 0;
		int j = 0;

		for(String line : lines.subList(2, lines.size())) {

			for(int k = 0; k < line.length(); ++k) {
				switch(line.charAt(k)) {
					case 'N':
						grid[i][j].breakWall(grid[i-1][j]);
						break;
					case 'E':
						grid[i][j].breakWall(grid[i][j+1]);
						break;
					case 'S':
						grid[i][j].breakWall(grid[i+1][j]);
						break;
					case 'W':
						grid[i][j].breakWall(grid[i][j-1]);
						break;
					case '*':
						grid[i][j].setMarked(true);
						break;
					default:
						throw new IllegalArgumentException("illegal character");
				}
			}
			++j;
			if(j >= width) {
				j = 0;
				++i;
			}
		}

		if(window)
			frame = new MazeFrame(grid, height, width, step);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(height);
		sb.append('\n');
		sb.append(width);
		sb.append('\n');

		for(int i = 0; i < height; ++i) {
			for(int j = 0; j < width; ++j) {
				if(i > 0 && grid[i][j].hasPassageTo(grid[i-1][j]))
					sb.append('N');
				if(j < width-1 && grid[i][j].hasPassageTo(grid[i][j+1]))
					sb.append('E');
				if(i < height-1 && grid[i][j].hasPassageTo(grid[i+1][j]))
					sb.append('S');
				if(j > 0 && grid[i][j].hasPassageTo(grid[i][j-1]))
					sb.append('W');
				if(grid[i][j].isMarked())
					sb.append('*');
				sb.append('\n');
			}
		}

		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Maze))
			return false;
		Maze that = (Maze)o;

		return this.toString().equals(that.toString());
	}

	@Override
	public int hashCode() {
		return this.toString().hashCode();
	}

	boolean isPerfect() {
		UnionFind uf = new UnionFind(height*width);

		// union find cycle detection
		for(int i = 0; i < height; ++i) {
			// horizontal edges
			for(int j = 0; j < width-1; ++j) {
				if(grid[i][j].hasPassageTo(grid[i][j+1])) {
					if(uf.sameClass(coordToInt(i,j), coordToInt(i,j+1)))
						return false;
					uf.union(coordToInt(i,j), coordToInt(i,j+1));
				}
			}

			// there are no vertical edges in last row, so we're done
			if(i == height-1)
				continue;

			// vertical edges
			for(int j = 0; j < width; ++j) {
				if(grid[i][j].hasPassageTo(grid[i+1][j])) {
					if(uf.sameClass(coordToInt(i,j), coordToInt(i+1,j)))
						return false;
					uf.union(coordToInt(i,j), coordToInt(i+1,j));
				}
			}
		}

		// check if connected
		return (uf.getSize(0) == height*width);
	}

	void clearMarks() {
		for (Cell[] row : grid)
			for (Cell c : row)
				c.setMarked(false);
	}
}

