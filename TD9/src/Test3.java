
import java.io.FileNotFoundException;
import java.util.HashMap;

import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;
import reporters.BackTrackReporter;
import reporters.CountMarks;
import reporters.NoMarkOverwrites;
import reporters.SharkReporter;
import reporters.StackReporter;
import reporters.TestReporter;
import reporters.Timer;
import reporters.WallReporter;

public class Test3 extends Test {
	public static void main(String[] args) throws FileNotFoundException {

		boolean canvas = true;
		boolean print = true;
		boolean result = true;

		HashMap<String, Object> data = new HashMap<>();

		Timer timer = new Timer("timer", data, print);
		CountMarks countMarks = new CountMarks("markCounters", data, print);
		StackReporter stackReporter = new StackReporter("stack", data, 1000, print);
		NoMarkOverwrites noMarkOverwrites = new NoMarkOverwrites();
		WallReporter wallReporter = new WallReporter();
		BackTrackReporter backTrackReporter = new BackTrackReporter("backTrack", data, false);
		SharkReporter sharkReporter = new SharkReporter("sharks", data, print);
		backTrackReporter.setLimit(10000);

		Traversal traversal = new AvoidSharks();

		System.out.print("Test de la méthode AvoidSharks.traverse()...\t");

		if (result) {
			TestReporter[] reporters = new TestReporter[] { timer, stackReporter, countMarks, noMarkOverwrites, sharkReporter,
					wallReporter };
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
					System.out.println(
							"\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");

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
			}
		}

		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks, noMarkOverwrites, sharkReporter, backTrackReporter, wallReporter};
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
			TestReporter[] reporters = new TestReporter[] { timer, stackReporter, countMarks, noMarkOverwrites,
					backTrackReporter, sharkReporter, wallReporter };
			result = test("one-shark.txt", reporters,  traversal, data, canvas, print);
			if (!result)
				if (print)
					System.out.println(
							"\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");

			if (stackReporter.getData("one-shark.txt") > 10) {
				if (print) {
					System.out.println("\nIl semblerait que vous ayez implementé une méthode récursive,"
							+ "tandis qu'on vous en a demandé une itérative.");
				}
				result = false;
			}

			HashMap<Mark, Integer> oneSharkMarks = countMarks.getData("one-shark.txt");
			oneSharkMarks.remove(Traversal.path);
			oneSharkMarks.remove(Ocean.defaultMark);
			
			for (Direction dir : BasicDirections.values())
				oneSharkMarks.remove(dir);

			if (!oneSharkMarks.isEmpty()) {
				if (print) {
					System.out.println("\nVous n'utilisez pas correctement les marques:");
					System.out.print("\tIl n'y doit pas en avoir d'autre marques que " + Ocean.defaultMark + ", " + Traversal.path);
					for (Direction dir : BasicDirections.values())
						System.out.print(", " + dir);
					
					System.out.print("\n\t(Vous mettez en plus: ");
					for (Mark mark : oneSharkMarks.keySet())
						System.out.print(mark + ", ");
					System.out.println(")");
				}
				result = false;
			}

			if (backTrackReporter.getCount() == 0) {
				if (print)
					System.out.println("\nEst-ce qu vous auriez oublié de lancer backTrack() ?");
				result = false;
			} else if (!backTrackReporter.getData("one-shark.txt").equals(new Coordinate(1, 1))) {
				if (print) {
					System.out.println("\nIl y a un problème avec votre marquage");
				}
				result = false;
			}
			
			if (sharkReporter.getData("one-shark.txt") > 0) {
				if (print) 
					System.out.println("Le chemin trouvé par votre algorithme passe par un requin."
							+ "Dans cet océan, il est possible de l'éviter.");
				result = false;
			}
		}
		
		if (result) {
			TestReporter[] reporters = new TestReporter[] { timer, stackReporter, countMarks, sharkReporter };
			result = test("many-sharks.txt", reporters,  traversal, data, canvas, print);
			if (!result) {
				if (print)
					System.out.println("Vous n'avez pas trouvé Nemo");
			} 
			
			if (sharkReporter.getData("many-sharks.txt") > 2) {
				if (print) 
					System.out.println("Le chemin trouvé par votre algorithme passe par un requin."
							+ " Dans cet océan, il est possible de l'éviter.");
				result = false;
			}
		}
		
		if (result) {
			// Si Nemo n'est pas là, on ne peut pas le trouver => retour attendu est 'false'
			TestReporter[] reporters = new TestReporter[] { timer, stackReporter, countMarks, sharkReporter };
			result = !test("no-nemo.txt", reporters,  traversal, data, canvas, print);
			if (!result)
				if (print) {
					System.out.println(
							"\nVous trouvez Nemo, alors qu'il n'est pas dans l'océan ! (traverse() doit renvoyer 'false')");
				} else {
				}
		}

		if (result) {
			TestReporter[] reporters = new TestReporter[] { timer, stackReporter, countMarks, sharkReporter };
			result = test("no-walls.txt", reporters,  traversal, data, canvas, print);
			if (!result)
				if (print) {
					System.out.println(
							"\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
				} else {
				}
		}

		System.out.println(result ? "[OK]" : "[NOK]");
		assert result;		
	}
}
