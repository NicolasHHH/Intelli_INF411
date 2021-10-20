import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;

import graphics.BasicOceanCanvas;
import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;
import reporters.BackTrackReporter;
import reporters.CountMarks;
import reporters.NoMarkOverwrites;
import reporters.StackReporter;
import reporters.TestReporter;
import reporters.Timer;
import reporters.WallReporter;

class TestBackTrackingTraversal extends BackTrackingTraversal {	
	Coordinate current, start;
	String trace;
	TestReporter[] reporters;
	
	public void init (Coordinate current, Coordinate start, String trace, TestReporter[] reporters) {
		this.current = current;
		this.start = start;
		this.trace = trace;
		this.reporters = reporters;
	}
	
	private static void prepareOcean (Ocean ocean, Coordinate start, String trace, TestReporter[] reporters) {
		Direction[] directions = ocean.directions(); 
		Random rnd = new Random();
		
		for (int x = 0; x < ocean.size(); x++)
			for (int y = 0; y < ocean.size(); y++) {
				Coordinate c = new Coordinate(x, y);
				if (!ocean.isWall(c)) 
					ocean.setMark(c, directions[rnd.nextInt(directions.length)]);
			}
				
		String[] moves = trace.split(",");
		for (int i = 0; i < moves.length; i++) {
			String[] move = moves[i].split(":");
			Direction dir = directions[Integer.parseInt(move[0])];
			int count = Integer.parseInt(move[1]);
			
			for (int j = 0; j < count; j++) {
				ocean.setMark(start, dir);
				start = start.moveTo(dir);
			}
			
			ocean.setMark(start);
		}
		
		for (TestReporter reporter : reporters)
			reporter.initialise(ocean);
	}
	
	@Override
	public boolean traverse(Ocean ocean, Coordinate start) {		
		prepareOcean(ocean, current, trace, reporters);
		backTrack(ocean, current);
		return true;
	}
}

public class Test23 extends Test {
	private final static String trace1 = "2:9,1:2"; // 9 steps east, then 2 steps south
	private final static String trace2 = "2:1";     // 1 step  east
	
	private static int stepsInATrace (String trace) {
		int count = 0;
		String[] moves = trace.split(",");
		for (String move : moves) 
			count += Integer.parseInt(move.split(":")[1]);
		return count;
	}
	
	private static String traceToString(String trace) {
		Direction directions[] = BasicDirections.values();
		StringBuilder sb = new StringBuilder();
		String[] moves = trace.split(",");
		for (String move : moves) {
			sb.append(directions[Integer.parseInt(move.split(":")[0])]);
			sb.append(":");
			sb.append(move.split(":")[1]);
			sb.append(",");
		}
		sb.delete(sb.length()-1, sb.length());
		return sb.toString();
	}

	private static boolean testBackTrack() throws FileNotFoundException {
		boolean canvas = true;
		boolean print = true;
		boolean result = true;

		String name = "small.txt";
		
		HashMap<String, Object> data = new HashMap<>(); 
		
		Timer timer = new Timer("timer", data, print);
		CountMarks countMarks = new CountMarks("markCounters", data, print);
		BackTrackReporter backTrackReporter = new BackTrackReporter("backTrack", data, print);
		TestReporter[] reporters = new TestReporter[] {timer, countMarks, backTrackReporter}; 
		
		TestBackTrackingTraversal traversal = new TestBackTrackingTraversal();
		
		System.out.print("Test de la méthode BackTrackingTraversal.backTrack()...\t");

		Ocean ocean = new Ocean(name, "data/" + name).reporters();
		if (canvas)
			ocean = ocean.add(new BasicOceanCanvas("backTrack : " + name));		
		
		
		if (result) {
			Coordinate current = new Coordinate(8, 1);
			Coordinate start = new Coordinate(9, 1);
			String trace = trace2;

			if (print)
				System.out.println("\n...sur le labyrinth '" + name + "' avec le chemin " + traceToString(trace) + "...");
			
			traversal.init(current, start, trace, reporters);
			int traceLen = stepsInATrace(trace);
			backTrackReporter.setLimit(traceLen);
			
			try {
				result = ocean
						.add(timer)
						.add(countMarks)
						.add(backTrackReporter)
						.exploreUsing(traversal);
			} catch (StackOverflowError e) {
				backTrackReporter.finish();
				if (!backTrackReporter.getData(name).equals(start)) {
					if (print) {
						System.out.println("backTrack() termine sur la cellule " 
								+ backTrackReporter.getData(name) + " au lieu de " + start + " demandée");
						System.out.println("Est-ce que vous utilisez la direction opposée au lieu de celle marquée dans la cellule ?");					
					}
					result = false;
				} 					
			}
			
		}		

		if (result) {
			Coordinate current = new Coordinate(1, 1);
			Coordinate start = new Coordinate(10, 3);
			String trace = trace1;

			if (print)
				System.out.println("\n...sur le labyrinth '" + name + "' avec le chemin " + traceToString(trace) + "...");
			
			traversal.init(current, start, trace, reporters);
			int traceLen = stepsInATrace(trace);
			backTrackReporter.setLimit(traceLen);
			
			try {
				result = ocean.exploreUsing(traversal);
			} catch (StackOverflowError e) {
				for (TestReporter reporter : reporters)
					reporter.finish();
				
				if (print)
					System.out.println("backTrack() fait plus de pas que prévu (" 
							+ backTrackReporter.getCount() + " au lieu de " + traceLen + ")");
				result = false;
			}
			
			if (result) {
	 			HashMap<Mark, Integer> backTrackMarks = countMarks.getData(name);
				Integer path = backTrackMarks.get(Traversal.path);
				
				if (backTrackMarks.keySet().size() == 0) {
					if (print) {
						System.out.println("backTrack() devrait poser des marques "
								+ Traversal.path + " mais ne pose aucune marque du tout");
						System.out.println("Est-ce que vous avez oublié un appel à setMark() ?"
								+ " Sinon, vérifiez vos condition d'arrêt de boucle");
					}
					result = false;
				} else if (!(backTrackMarks.keySet().size() == 1 && path != null)) {
					if (print)
						System.out.println("backTrack() ne devrait poser que la marque " + Traversal.path + "."
								+ " Vous utilisez " + backTrackMarks.keySet());
					result = false;
				} else if (!backTrackReporter.getData(name).equals(start)) {
					if (print) {
						System.out.println("backTrack() termine sur la cellule " 
								+ backTrackReporter.getData(name) + " au lieu de la cellule demandée " + start);
						if (ocean.isMarlinAt(backTrackReporter.getData(name)))
							System.out.println("Est-ce que vous utilisez isMarlinAt() comme condition d'arrêt au lieu de la comparaison avec start");					
					}
					result = false;
				} else if (!backTrackReporter.toString().equals(trace)) {
					if (print)
						System.out.println("Le chemin trouvé par backTrack() n'est pas bon : "
								+ backTrackReporter.toString() + " au lieu de " + trace);
					result = false;
				}					
			}
		}
		
		System.out.println(result ? "[OK]" : "[NOK]");
		if (print)
			System.out.println();
		return result;
	}

	public static boolean testTraverse() throws FileNotFoundException {

		boolean canvas = true;
		boolean print = true;
		boolean result = true;

		HashMap<String, Object> data = new HashMap<>(); 
		
		Timer timer = new Timer("timer", data, print);
		CountMarks countMarks = new CountMarks("markCounters", data, print);
		StackReporter stackReporter = new StackReporter("stack", data, 1000, print);
		NoMarkOverwrites noMarkOverwrites = new NoMarkOverwrites();
		WallReporter wallReporter = new WallReporter();
		BackTrackReporter backTrackReporter = new BackTrackReporter("backTrack", data, print);
		backTrackReporter.setLimit(10000);
		
		Traversal traversal = new BreadthFirstWithShortestPath();

		System.out.print("Test de la méthode BreadthFirstWithShortestPath.traverse()...\t");

		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks, noMarkOverwrites, wallReporter};
			try {
				result = test("small.txt", reporters,  traversal, data, canvas, print);
			} catch (AssertionError e) {
				if (e.getMessage().equals(Traversal.NOT_A_DIRECTION)) {
					if (print) {
						System.out.println("\nIl y a un problème avec votre marquage : "
								+ "backTrack() tombe sur des cellules, qui ne sont pas marqués par des directions."
								+ "\n\tEst-ce que vous n'auriez pas oublié de prendre les directions opposées pour le marquage ?");
					}
					result = false;
				} else					
					throw e;
			}
			if (!result) {
				if (print) {
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
					
					if (timer.getCount() <= 1)
						System.out.println("Vous n'avez exploré aucune cellule :"
								+ " Est-ce que vous auriez oublié d'ajouter la cellule de départ dans la liste à traiter ?");
					else
						System.out.println("Est-ce que vous auriez oublié le test isNemoAt() ?");
				}							
			}
			
			HashMap<Mark, Integer> smallMarks = countMarks.getData("small.txt");
			Integer markCounter = 0;
			for (Direction dir : BasicDirections.values()) {
				Integer count = smallMarks.get(dir);
				markCounter += (count == null) ? 0 : count;
			}
			
			if (markCounter == 0) {
				if (print) 
					System.out.println("\nVous n'avez posé aucune marque lors de l'exploration");
				result = false;				
			} else if (markCounter < 8) {
				if (print) 
					System.out.println("\nVous avez posé très peu de marques lors de l'exploration."
							+ "\nEst-ce que vous auriez oublié de marquer ou de rajouter dans la liste à traiter les voisins de la cellule en cours ?");
				result = false;								
			} else if (markCounter > 52) {
				if (print) 
					System.out.println("\nVous avez posé beaucoup trop de marques : " + markCounter);					
				result = false;				
			}
		}

		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks, noMarkOverwrites, backTrackReporter, wallReporter};
			result = test("small2.txt", reporters,  traversal, data, canvas, print);
			int pathLength = backTrackReporter.getCount(); 
			if (print)
				System.out.println("Longueur du chemin trouvé : " + pathLength);
			
			if (pathLength == 0) {
				if (print)
					System.out.println("\nEst-ce qu vous auriez oublié de lancer backTrack() ?");
				result = false;
			} else if (pathLength > 8) {
				if (print)
					System.out.println("La longueur de votre chemin est " + pathLength + ", contre 9 pour le chemin le plus court");
				result = false;
			}
		}

				
		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks, noMarkOverwrites, backTrackReporter, wallReporter};
			result = test("no-sharks.txt", reporters,  traversal, data, canvas, print);
			if (!result)
				if (print)
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");

			if (stackReporter.getData("no-sharks.txt") > 10) {
				if (print) {
					System.out.println("\nIl semblerait que vous ayez implementé une méthode récursive,"
							+ "tandis qu'on vous en a demandé une itérative.");
				}
				result = false;
			}

			HashMap<Mark, Integer> noSharkMarks = countMarks.getData("no-sharks.txt");
			noSharkMarks.remove(Traversal.path);
			for (Direction dir : BasicDirections.values())
				noSharkMarks.remove(dir);

			if (noSharkMarks.size() > 1 || (noSharkMarks.size() == 1 && noSharkMarks.get(Ocean.defaultMark) == null)) {
				if (print) {
					System.out.println("\nVous n'utilisez pas correctement les marques:");
					System.out.print("\tIl n'y doit pas en avoir d'autre marques que " 
							+ Traversal.path);
					for (Direction dir : BasicDirections.values())
						System.out.print(", " + dir);
					System.out.println(" et au plus une " + Ocean.defaultMark);
					System.out.println("\t(Il se peut que vous ayez copié-collé un appel ocean.setMark(start, 0) de la question 2.2.");
					System.out.println("\tCette marque ne sert à rien. Si toutefois, vous voulez en poser une ici, utilisez la marque par défaut"
							+ " avec ocean.setMark(start).)");
				}
				result = false;
			}
		
			if (backTrackReporter.getCount() == 0) {
				if (print)
					System.out.println("\nEst-ce qu vous auriez oublié de lancer backTrack() ?");
				result = false;
			} else if (!backTrackReporter.getData("no-sharks.txt").equals(new Coordinate(1, 1))) {
				if (print) {
					System.out.println("\nIl y a un problème avec votre marquage");
				}
				result = false;
			}
		}
		
		if (result) {
			// Si Nemo n'est pas là, on ne peut pas le trouver => retour attendu est 'false'
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter};
			result = !test("no-nemo.txt", reporters,  traversal, data, canvas, print);
			if (!result)
				if (print) {
					System.out.println("\nVous trouvez Nemo, alors qu'il n'est pas dans l'océan ! (traverse() doit renvoyer 'false')");
				}
			else {			
			}
		}		

		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter};
			result = test("with-hole.txt", reporters,  traversal, data, canvas, print);
			if (!result) 
				if (print) {
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
				}
			else {				
			}
		}

		System.out.println(result ? "[OK]" : "[NOK]");
		return result;
	}

	public static void main(String[] args) throws FileNotFoundException {
		assert testBackTrack();
		assert testTraverse();
	}
	
}
