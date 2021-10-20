package ocean;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import reporters.OceanReporter;

/**
 * Encode la structure de l'océan
 */
public class Ocean {
	/***************************
	 * Les paramètres statiques
	 ***************************/
	
	/**
	 * Les directions pour les déplacements
	 */
	
	private final Direction[] directions;

	// Les valeurs des marques fournis par défaut
	// (d'autres classes utilisant celle-ci peuvent rajouter d'autres marques)

	// On ne peut pas utiliser null comme marque, car on les compare avec equals()
	// Ici on crée un objet d'une classe anonyme implémentant l'interface Mark
	/**
	 * Valeur représentant l'absence d'une marque
	 */
	public static final Mark noMark = new Mark() {
		@Override
		public Integer toInteger() {
			return null;
		}

		@Override
		public String toString() {
			return null;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || !(obj instanceof Mark))
				return false;
			Mark mark = (Mark) obj;
			return (mark.toInteger() == null) && (mark.toString() == null);
		}
	};

	/**
	 * Valeur par défaut pour une marque
	 * 
	 * <p>
	 * À utiliser lorsqu'il n'y a pas besoind de stocker d'autre information que la
	 * présence d'une marque
	 * </p>
	 */
	public static final Mark defaultMark = new IntMark(0, "defaultMark");

	// Les caractères utilisés pour stocker/récupérer la carte de l'océan dans un
	// fichier

	/**
	 * Le caractère utilisé pour marquer Marin dans la carte de l'océan stockée dans
	 * un fichier
	 */
	static final char Marlin = 'M';

	/**
	 * Le caractère utilisé pour marquer Nemo dans la carte de l'océan stockée dans
	 * un fichier
	 */
	static final char Nemo = 'N';

	/**
	 * Le caractère utilisé pour marquer la présence de Marin et Nemo dans la même
	 * case, dans la carte de l'océan stockée dans un fichier
	 */
	static final char Fishes = 'F'; // Marin & Nemo

	/**
	 * Le caractère utilisé pour marquer une falaise dans la carte de l'océan
	 * stockée dans un fichier
	 */
	static final char Wall = '#';

	/**
	 * Le caractère utilisé pour marquer un requin dans la carte de l'océan stockée
	 * dans un fichier
	 */
	static final char Shark = 'S';

	/**
	 * Le caractère utilisé pour marquer une case vide dans la carte de l'océan
	 * stockée dans un fichier
	 */
	static final char Empty = ' ';

	/**************************
	 * Les champs de la classe
	 **************************/

	/**
	 * Le nom de la carte de l'océan (c'est-à-dire, du fichier la contenant)
	 */
	public final String name;

	/*
	 * Tous les autres champs sont privés. Pour y accéder, utiliser les méthodes
	 * plus bas. Cela évite de créer des dépendances sur la représentation concrète
	 * de ces données. Cela évite également que ces données soient corrompus de
	 * l'extérieur: bien que les références sont finales, *le contenu* des tableaux
	 * peut être modifié. Notamment, le contenu du tableau marks est appelé à être
	 * modifié ! Il s'agit donc de garder le contrôle sur ce processus.
	 */

	/**
	 * La taille de l'océan (on suppose l'océan carré)
	 */
	private final int size;

	/**
	 * Les emplacements des falaises
	 */
	private final boolean[][] walls;

	/**
	 * Les emplacements des requins
	 */
	private final boolean[][] sharks;

	/**
	 * Les marques pouvant être utilisés de l'extérieur, notamment pour
	 * l'exploration (voir {@link #isMarked(Coordinate)} et
	 * {@link #setMark(Coordinate, Mark)})
	 */
	private final Mark[][] marks;

	// Ces deux champs ne sont pas finaux -- on peut imaginer que les poissons se
	// déplacent dans l'océan !
	// En revanche, il sont privés -- on veut bien garder le contrôle

	/**
	 * Les coordonnées de Marin
	 */
	private Coordinate marlin;

	/**
	 * Les coordonnées de Nemo
	 */
	private Coordinate nemo;

	/**
	 * La liste de rapporteurs
	 * 
	 * <p>
	 * On ne s'occupe pas dans la classe {@link Ocean} de l'affichage de l'océan. (À
	 * priori, on ne sait même pas comment celui-ci sera réalisé.) Cette tache est
	 * déléguée à un {@link OceanReporter <em>rapporteur</em>}.
	 * </p>
	 * 
	 * <p>
	 * Toute classe implémentant l'interface {@code OceanReporter} doit implémenter
	 * trois méthodes :
	 * </p>
	 * 
	 * <ul>
	 * <li>{@code void initialise(Ocean)},</li>
	 * <li>{@code void report(Coordinate)},</li>
	 * <li>{@code void finish()},</li>
	 * </ul>
	 * 
	 * <p>
	 * appelées avant, pendant et après l'exploration de l'océan, respectivement
	 * (voir {@link #exploreUsing(Traversal)}).
	 * </p>
	 */
	private LinkedList<OceanReporter> reporters;
	
	/**
	 * Détermine si les rapporteurs doivent travailler
	 */
	private boolean reportsEnabled = true;

	/********************
	 * Les constructeurs
	 ********************/

	/**
	 * Récupère la carte de l'océan dans un fichier, laissant rapporteur à null
	 * 
	 * @param name le nom de l'océan à créer
	 * @param fileName le nom du fichier à lire
	 * @param directions le tableau de toutes les directions possibles
	 * @throws FileNotFoundException si le fichier n'est pas accessible
	 */
	public Ocean(String name, String fileName, Direction[] directions) throws FileNotFoundException {
		this.directions = directions;
		
		this.name = name;
		// Ouvre le fichier
		Scanner input = new Scanner(new File(fileName));
		// et vérifie qu'il n'est pas vide
		assert input.hasNext() : "Empty ocean map in " + fileName;

		// La première ligne doit contenir un seul entier, la taille de l'océan
		size = input.nextInt();
		// Cela nous permet d'initialiser les tableaux
		walls = new boolean[size][size];
		sharks = new boolean[size][size];
		marks = new Mark[size][size];
		input.nextLine();

		// On charge au plus size lignes
		for (int y = 0; y < size; y++) {
			String current;
			try {
				// On essaie de lire une ligne
				current = input.nextLine();
				// Cela peut échouer s'il y a moins que size lignes dans le fichier
			} catch (NoSuchElementException e) {
				// Dans ce cas on affiche une erreur et on termine
				throw new Error("Not enough lines in the ocean map " + fileName + " (" + y + ", where " + size
						+ " were expected)");
			}

			// On parse une ligne de la carte
			loadOneRow(current, size, y);

			// Dans la foulée on efface les marques
			// Actuellement, la marque noMark est définie comme null, ce qui est la valeur
			// par défaut attribuée lors de l'initialisation du tableau marks. Toutefois,
			// cette définition pourrait changer. Il faut, donc, effacer les marques
			// explicitement afin de s'assurer qu'il n'y en aurait pas, quel que soit la
			// définiton de l'absence de marque.
			for (int x = 0; x < size; x++)
				marks[x][y] = noMark;
		}

		// Enfin, on ferme le fichier
		input.close();

		// On initialise la liste de rapporteurs
		reporters = new LinkedList<>();
	}

	/**
	 * Récupère la carte de l'océan dans un fichier, laissant rapporteur à null
	 * 
	 * @param name le nom de l'océan à créer
	 * @param fileName le nom du fichier à lire
	 * @throws FileNotFoundException si le fichier n'est pas accessible
	 */
	public Ocean(String name, String fileName) throws FileNotFoundException {
		this (name, fileName, BasicDirections.values());
	}
	
	/**
	 * Initialise la liste de rapporteurs avec un seul élément — celui passé en
	 * argument
	 * 
	 * @return la référence de l'océan afin de pourvoir enchaîner des ajouts de
	 *         rapporteurs (voir {@link #add(OceanReporter)})
	 */
	public Ocean reporters() {
		// on initialise la liste de rapporteurs
		reporters = new LinkedList<>();
		// on renvoit this pour que la chaîne puisse être continué
		return this;
	}

	/**
	 * Rajoute le rapporteur passé en argument dans la liste
	 * 
	 * @param reporter la référence vers le rapporteur
	 * @return la référence de l'océan afin de pourvoir enchaîner des ajouts de
	 *         rapporteurs (voir {@link #add(OceanReporter)})
	 */
	public Ocean add(OceanReporter reporter) {
		// on rajoute le rapporteur passé en argument à la liste
		reporters.add(reporter);
		// on renvoit this pour que la chaîne puisse être continué
		return this;
	}

	/***************************************************
	 * Les méthodes permettant l'exploration de l'océan
	 ***************************************************/

	/**
	 * Le tableau de toutes les directions disponibles
	 * 
	 * @return une copie du tableau de toutes les directions
	 */
	public Direction[] directions() {
		return Arrays.copyOf(directions, directions.length);
	}
	
	/*
	 * Traversal est une classe abstraite qui représente un algorithme de parcours
	 * de l'océan. On peut implémenter autant qu'on veut de tels algorithmes en
	 * définissant des classes dérivées de Traversal.
	 */
	
	/* Les méthodes explore() permettent l'utilisation de ces algorithmes tout en
	 * gardant le contrôle sur leur mise en place.
	 * 
	 * Par ailleurs, un algorithme d'exploration ne se préoccupe que de
	 * l'exploration ! L'affichage de son tracé est délégué à un rapporteur. (S'il
	 * n'y a pas de rapporteur, alors aucun affichage ne sera fait.)
	 */
	
	/**
	 * Exploration de l'océan
	 * 
	 * @param traversal algorithme d'exploration ({@code Traversal} est une classe
	 *                  abstraite qui représente un algorithme de parcours de
	 *                  l'océan. On peut implémenter autant qu'on veut de tels
	 *                  algorithmes en définissant des classes dérivées de
	 *                  {@code Traversal}.)
	 * @return {@code true} si Nemo a été retrouvé, {@code false} sinon
	 */
	public boolean exploreUsing(Traversal traversal) {
		// On initialise les rapporteurs
		for (OceanReporter reporter : reporters)
			reporter.initialise(this);

		// On enlève toutes les marques
		reset();

		// On lance l'exploration
		boolean found = traversal.traverse(this, new Coordinate(marlin));

		// On termine le rapport
		for (OceanReporter reporter : reporters)
			reporter.finish();

		// On renvoie le résultat de l'exploration
		return found;
	}

	/*****************************************************************
	 * Les méthodes permettant la consultation des données de l'océan
	 *****************************************************************/

	/*
	 * À noter que ces méthodes ne permettent aucune modification de ces données
	 */

	/**
	 * @return la taille de l'océan 
	 */
	public int size() {
		return size;
	}

	/**
	 * Est-ce que {@code c} est une case valide?
	 * 
	 * @param c la cellule à tester
	 * @return {@code true} si les coordonnées {@code c.x} et {@code c.y} sont
	 *         toutes les deux comprises entre {@code 0} (inclu) et {@code size}
	 *         (exclu), {@code false} sinon
	 */
	public boolean isValid(Coordinate c) {
		return 0 <= c.x && c.x < size && 0 <= c.y && c.y < size;
	}

	/**
	 * Est-ce que {@code c} est un mur ?
	 * 
	 * @param c la cellule à tester
	 * @return {@code true} si {@code c} est un mur, {@code false} sinon
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public boolean isWall(Coordinate c) {
		if (!isValid(c))
			throw new Error("wrong coordinates: " + c);

		return walls[c.x][c.y];
	}

	/**
	 * Est-ce qu'il y a un requin dans {@code c} ?
	 * 
	 * @param c la cellule à tester
	 * @return {@code true} si {@code c} contient un requin, {@code false} sinon
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public boolean isThereASharkAt(Coordinate c) {
		if (!isValid(c))
			throw new Error("wrong coordinates: " + c);

		return sharks[c.x][c.y];
	}

	/**
	 * Est-ce que Nemo se trouve dans {@code c} ?
	 * 
	 * @param c la cellule à tester
	 * @return {@code true} si Nemo se trouve dans {@code c}, {@code false} sinon
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public boolean isNemoAt(Coordinate c) {
		if (!isValid(c))
			throw new Error("wrong coordinates: " + c);

		return c.equals(nemo);
	}

	/**
	 * Est-ce que Marin se trouve dans {@code c} ?
	 * 
	 * @param c la cellule à tester
	 * @return {@code true} si Marin se trouve dans {@code c}, {@code false} sinon
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public boolean isMarlinAt(Coordinate c) {
		if (!isValid(c))
			throw new Error("wrong coordinates: " + c);

		return c.equals(marlin);
	}

	/**
	 * Est-ce que {@code c} comporte une marque quelconque ?
	 * 
	 * @param c la cellule à tester
	 * @return {@code true} si {@code c} comporte une marque, {@code false} sinon
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public boolean isMarked(Coordinate c) {
		if (!isValid(c))
			throw new Error("wrong coordinates: " + c);

		return marks[c.x][c.y] != noMark;
	}

	/**
	 * La marque dans {@code c}
	 * 
	 * @param c la cellule dont on souhaite récupérer la marque
	 * @return la marque qui se trouve dans {@code c}
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public Mark getMark(Coordinate c) {
		if (!isValid(c))
			throw new Error("wrong coordinates: " + c);

		return marks[c.x][c.y];
	}

	/**************************************************
	 * Les méthodes permettant de modifier le marquage
	 **************************************************/

	/**
	 * Désactive tous les rapporteurs, mais leur donne une chance de bloquer cette action
	 * @return {@code true} si les rapporteurs sont bien désactivés
	 */
	public boolean disableReports () {
		// On sauvegarde l'état d'activation actuel 
		boolean old = reportsEnabled;

		// On tente de désactiver les rapporteurs
		reportsEnabled = false;
		boolean ok = true;
		
		// On notifie la désactivation à tous les rapporteurs
		// NB : On n'arrête pas la boucle lorsque l'un des rapporteurs bloque la désactivation
		// car, sinon, il aurait fallu se souvenir lesquels des rapporteurs ont été notifiés
		// et lesquels ne l'ont pas été, afin de pouvoir, le cas échéant, leur notifier 
		// l'annulation de cette opération 
		for (OceanReporter reporter : reporters)
			ok = ok && reporter.notifySuspension();
		
		// Si au moins un s'oppose à la désactivation on l'annule  
		if (!ok) {
			// On notifie l'annulation à tous les rapporteurs
			for (OceanReporter reporter : reporters)
				reporter.cancelSuspension();
			// On retablit l'état précédent 
			reportsEnabled = old;
		}
		
		return ok;
	}
	
	/**
	 * Active tous les rapporteurs, mais leur donne une chance de bloquer cette action
	 * @return {@code true} si les rapporteurs sont bien activés
	 */
	public boolean enableReports() {
		// On sauvegarde l'état d'activation actuel 
		boolean old = reportsEnabled;

		// On tente d'activer les rapporteurs
		reportsEnabled = true;		
		boolean ok = true;
		
		// On notifie l'activation à tous les rapporteurs
		// NB : On n'arrête pas la boucle lorsque l'un des rapporteurs bloque l'activation
		// car, sinon, il aurait fallu se souvenir lesquels des rapporteurs ont été notifiés
		// et lesquels ne l'ont pas été, afin de pouvoir, le cas échéant, leur notifier 
		// l'annulation de cette opération 
		for (OceanReporter reporter : reporters)
			ok = ok && reporter.notifyActivation();
		
		// Si au moins un s'oppose à l'activation on l'annule  
		if (!ok) {
			// On notifie l'annulation à tous les rapporteurs
			for (OceanReporter reporter : reporters)
				reporter.cancelActivation();
			// On retablit l'état précédent 
			reportsEnabled = old;
		}
		
		return ok;
	}
	/**
	 * Pose la marque {@code mark} et notifie le {@link #reporters rapporteur}
	 * 
	 * @param c    la cellule où la marque doit être posée
	 * @param mark la marque à poser
	 * @return la marque précédente qui se trouvait dans {@code c} auparavant
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public Mark setMark(Coordinate c, Mark mark) {
		if (!isValid(c))
			throw new Error("wrong coordinates: " + c);

		// Note la marque précédente
		Mark old = marks[c.x][c.y];
		// Pose la nouvelle marque
		marks[c.x][c.y] = mark;

		if (reportsEnabled) { 
			// Notifier tous les rapporteurs pour qu'ils puissent mettre à jour leur rapport
			for (OceanReporter reporter : reporters)
				reporter.report(c, old);
		}
		
		// Renvoie la marque précédente
		return old;
	}

	/**
	 * Pose une marque de valeur {@code mark} et notifie le {@link #reporters
	 * rapporteur}
	 * 
	 * @param c    la cellule où la marque doit être posée
	 * @param mark la marque à poser
	 * @return la marque précédente qui se trouvait dans {@code c} auparavant
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public Mark setMark(Coordinate c, int mark) {
		// À noter que les rapporteurs seront notifiés dans cet appel
		return setMark(c, new IntMark(mark));
	}

	/**
	 * Pose la marque par défaut ({@link #defaultMark}) et notifie le
	 * {@link #reporters rapporteur}
	 * 
	 * @param c la cellule où la marque doit être posée
	 * @return la marque précédente qui se trouvait dans {@code c} auparavant
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public Mark setMark(Coordinate c) {
		// À noter que les rapporteurs seront notifiés dans cet appel
		return setMark(c, defaultMark);
	}

	/**
	 * Enlève la marque et notifie le {@link #reporters rapporteur}
	 * 
	 * @param c la cellule où la marque doit être posée
	 * @return la marque enlevée
	 * @throws Error si {@code c} n'est pas valide (voir
	 *               {@link #isValid(Coordinate)})
	 */
	public Mark unMark(Coordinate c) {
		// À noter que les rapporteurs seront notifiés dans cet appel
		return setMark(c, noMark);
	}

	/**
	 * Enlève toutes les marques de l'océan et notifie le {@link #reporters
	 * rapporteur}
	 */
	void reset() {
		// On enlève toutes les marques
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				marks[i][j] = noMark;

		// Puis on notifie le rapporteur
		// pour qu'il puisse mettre à jour son rapport
		// Comme il n'y a pas de cellule courante, on passe null
		for (OceanReporter reporter : reporters)
			reporter.report(null, null);
	}

	/***************************************************************************
	 * Les méthodes permettant de stocker/récupérer la carte de l'océan dans un
	 * fichier
	 ***************************************************************************/

	/**
	 * Méthode auxilière utilisée dans le constructeur {@link #Ocean(String, String, Direction[])}
	 * 
	 * <p>
	 * Récupère dans {@code source} l'information pour la ligne {@code y} de l'océan
	 * (au plus {@code size} cellules)
	 * </p>
	 * 
	 * @param source une chaine de caractères représentant une ligne de la carte de
	 *               l'océan
	 * @param size   la taille de l'océan
	 * @param y      le numéro de la ligne à importer
	 */
	private void loadOneRow(String source, int size, int y) {
		// min:
		// si la ligne dépasse la taille, jeter le trop
		// si la ligne est trop courte, on considère qu'il n'y a rien dans cette partie
		// de l'océan
		for (int x = 0; x < Math.min(size, source.length()); x++) {
			switch (source.charAt(x)) {
			case Fishes: // Marin et Nemo
				marlin = new Coordinate(x, y);
				nemo = new Coordinate(x, y);
				break;
			case Marlin:
				marlin = new Coordinate(x, y);
				break;
			case Nemo:
				nemo = new Coordinate(x, y);
				break;
			case Wall:
				walls[x][y] = true;
				break;
			case Shark:
				sharks[x][y] = true;
				break;
			default:
				break;
			}
		}
	}

	/**
	 * Crée une chaîne de caractères avec la carte de l'océan
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// On met la taille de l'océan sur la première ligne
		sb.append(size + "\n");
		// Pour chaque ligne
		for (int y = 0; y < size; y++) {
			// Pour chaque case
			for (int x = 0; x < size; x++) {
				// On crée l'objet avec les coordonnées de la cellule pour les tests
				Coordinate c = new Coordinate(x, y);
				// On met un caractère en fonction du contenu de la cellule
				if (isMarlinAt(c) && isNemoAt(c))
					sb.append(Fishes);
				else if (isMarlinAt(c))
					sb.append(Marlin);
				else if (isNemoAt(c))
					sb.append(Nemo);
				else if (isWall(c))
					sb.append(Wall);
				else if (isThereASharkAt(c))
					sb.append(Shark);
				else
					sb.append(Empty);
			}
			// On passe à la ligne
			sb.append("\n");
		}
		// On renvoie le résultat
		return sb.toString();
	}
}
