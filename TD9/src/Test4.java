
import java.io.FileNotFoundException;
import java.util.HashMap;

import ocean.BasicDirections;
import ocean.Coordinate;
import ocean.Direction;
import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;
import reporters.BackTrackReporter;
import reporters.BackTrackReporter2;
import reporters.CountMarks;
import reporters.SharkReporter;
import reporters.StackReporter;
import reporters.TestReporter;
import reporters.Timer;

public class Test4 extends Test {
	private static final String expectedTrace = "(17,4)(17,3)(17,2)(17,1)(16,1)(15,1)(14,1)(13,1)(12,1)(11,1)(10,1)(9,1)(8,1)(7,1)(6,1)(5,1)(4,1)(3,1)(2,1)"; 
	private static final String expectedTraceTer = "(18,16)(18,15)(18,14)(19,14)(20,14)(21,14)(22,14)(23,14)(23,15)(23,16)(24,16)(24,17)(24,18)(25,18)(26,18)(27,18)(28,18)(28,19)(28,20)(29,20)(30,20)(31,20)(32,20)(33,20)(33,19)(33,18)(33,17)(33,16)(32,16)(31,16)(31,15)(31,14)(32,14)(33,14)(34,14)(35,14)(36,14)(37,14)(37,13)(37,12)(37,11)(37,10)(37,9)(37,8)(37,7)(37,6)(37,5)(37,4)(37,3)(36,3)(35,3)(34,3)(33,3)(32,3)(31,3)(30,3)(29,3)(28,3)(27,3)(26,3)(25,3)(24,3)(23,3)(22,3)(21,3)(20,3)(19,3)(18,3)(17,3)(16,3)(15,3)(14,3)(13,3)(12,3)(11,3)(10,3)(9,3)(8,3)(7,3)(6,3)(5,3)(4,3)(3,3)(2,3)(1,3)(1,4)(1,5)(1,6)(1,7)(1,8)(1,9)(1,10)(1,11)(1,12)(1,13)(1,14)(1,15)(1,16)(1,17)(1,18)(1,19)(1,20)(1,21)(1,22)(1,23)(1,24)(1,25)(1,26)(1,27)(1,28)(1,29)(1,30)(1,31)(1,32)(1,33)(1,34)(1,35)(1,36)(1,37)(1,38)(1,39)(2,39)(3,39)(4,39)(5,39)(6,39)(7,39)(8,39)(9,39)(10,39)(11,39)(12,39)(13,39)(14,39)(15,39)(16,39)(17,39)(18,39)(19,39)(20,39)(21,39)(22,39)(23,39)(24,39)(25,39)(26,39)(27,39)(28,39)(29,39)(30,39)(31,39)(32,39)(33,39)(34,39)(35,39)(36,39)(37,39)(38,39)(39,39)(39,38)(39,37)(39,36)(39,35)(39,34)(39,33)(39,32)(39,31)(39,30)(39,29)(39,28)(39,27)(39,26)(39,25)(39,24)(39,23)(39,22)(39,21)(39,20)(39,19)(39,18)(39,17)(39,16)(39,15)(39,14)(39,13)(39,12)(39,11)(39,10)(39,9)(39,8)(39,7)(39,6)(39,5)(39,4)(39,3)(39,2)(39,1)(38,1)(37,1)(36,1)(35,1)(34,1)(33,1)(32,1)(31,1)(30,1)(29,1)(28,1)(27,1)(26,1)(25,1)(24,1)(23,1)(22,1)(21,1)(20,1)(19,1)(18,1)(17,1)(16,1)(15,1)(14,1)(13,1)(12,1)(11,1)(10,1)(9,1)(8,1)(7,1)(6,1)(5,1)(4,1)(3,1)(2,1)";
	public static void main(String[] args) throws FileNotFoundException {
		
		System.out.print("Test de la méthode Bonus.traverse()...\t");
		Traversal traversal = new Bonus();

		testShortestSharks(traversal);
	}
		
	public static void testShortestSharks(Traversal traversal) throws FileNotFoundException {
		boolean canvas = true;
		boolean print = true;
		boolean result = true;

		HashMap<String, Object> data = new HashMap<>();

		Timer timer = new Timer("timer", data, print);
		CountMarks countMarks = new CountMarks("markCounters", data, print);
		StackReporter stackReporter = new StackReporter("stack", data, 1000, print);
		BackTrackReporter backTrackReporter = new BackTrackReporter("backTrack", data, false);
		SharkReporter sharkReporter = new SharkReporter("sharks", data, print);
		backTrackReporter.setLimit(10000);
		BackTrackReporter2 pathReporter = new BackTrackReporter2("path", data, print);
		TestReporter[] reporters = new TestReporter[] { timer, stackReporter, countMarks, backTrackReporter, pathReporter, sharkReporter };
		
		if (result) {
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
			result = test("one-shark.txt", reporters, traversal, data, canvas, print);
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
					System.out.print("\tIl n'y doit pas en avoir d'autre marques que " + Traversal.path + ", " + Ocean.defaultMark);
					for (Direction dir : BasicDirections.values())
						System.out.print(", " + dir);
					System.out.print("\n\t(Vous mettez en plus: ");
					for (Mark mark : oneSharkMarks.keySet())
						System.out.print(mark + ", ");
					System.out.println(")");
				}
				result = false;
			}

			int pathLength = backTrackReporter.getCount(); 
			int sharkCount = sharkReporter.getData("one-shark.txt");
			if (print)
				System.out.println("Longueur du chemin trouvé : " + pathLength);

			if (pathLength == 0) {
				if (print)
					System.out.println("\nEst-ce qu vous auriez oublié de lancer backTrack() ?");
				result = false;
			} else if (!backTrackReporter.getData("one-shark.txt").equals(new Coordinate(1, 1))) {
				if (print) {
					System.out.println("\nIl y a un problème avec votre marquage : le rebroussement n'arrive pas jusqu'à Marin");
				}
				result = false;
			} else if (pathLength > 104) {
				if (print)
					System.out.println("La longueur de votre chemin est " + pathLength + ", contre 104 pour le chemin le plus court");
				result = false;
			}

			if (sharkCount > 0) {
				if (print) 
					System.out.println("Le chemin trouvé par votre algorithme passe par un requin."
							+ "Dans cet océan, il est possible de l'éviter.");
				result = false;
			}
		}
		
		if (result) {
			result = test("many-sharks.txt", reporters, traversal, data, canvas, print);
			int pathLength = backTrackReporter.getCount(); 
			int sharkCount = sharkReporter.getData("many-sharks.txt");
			if (print)
				System.out.println("Longueur du chemin trouvé : " + pathLength);
					
			if (!result) {
				if (print)
					System.out.println("Vous n'avez pas trouvé Nemo");
			} 
			
			if (sharkCount > 2) {
				if (print) 
					System.out.println("Le chemin trouvé par votre algorithme passe par " + sharkCount + " requins."
							+ " Dans cet océan, il y a un chemin avec 2 requins seulement (voir le dessin dans la Question 4 de l'énoncé).");
				result = false;
			}
			
			if (pathLength > 274) {
				if (print)
					System.out.println("La longueur de votre chemin est " + pathLength + ", contre 104 pour le chemin le plus court");
				result = false;			
			}
		}

		if (result) {
			result = test("many-sharks-bis.txt", reporters, traversal, data, canvas, print);
			int pathLength = backTrackReporter.getCount(); 
			int sharkCount = sharkReporter.getData("many-sharks-bis.txt");
			if (print)
				System.out.println("Longueur du chemin trouvé : " + pathLength);
					
			if (!result) {
				if (print)
					System.out.println("Vous n'avez pas trouvé Nemo");
			} 
			
			if (sharkCount > 1) {
				if (print) 
					System.out.println("Le chemin trouvé par votre algorithme passe par un requin."
							+ " Dans cet océan, il est possible de l'éviter.");
				result = false;
			}
			
			if (pathLength > 19) {
				if (print)
					System.out.println("La longueur de votre chemin est " + pathLength + ", contre 19 pour le chemin le plus court");
				result = false;			
			}
			
			String trace = pathReporter.toString();
			if (!trace.equals(expectedTrace)) {
				if (print) {
					System.out.println("Le chemin de retour est incorrect");
					System.out.println("Celui attendu est : ");
					System.out.println(expectedTrace);
				}
				result = false;
			}
		}

		if (result) {
			result = test("many-sharks-ter.txt", reporters, traversal, data, canvas, print);
			int pathLength = backTrackReporter.getCount(); 
			if (print)
				System.out.println("Longueur du chemin trouvé : " + pathLength);
					
			if (!result) {
				if (print)
					System.out.println("Vous n'avez pas trouvé Nemo");
			} 
						
			if (pathLength > 234) {
				if (print)
					System.out.println("La longueur de votre chemin est " + pathLength + ", contre 234 pour le chemin le plus court");
				result = false;			
			}
			
			String trace = pathReporter.toString();
			if (!trace.equals(expectedTraceTer)) {
				if (print) {
					System.out.println("Le chemin de retour est incorrect");
					System.out.println("Celui attendu est : ");
					System.out.println(expectedTraceTer);
				}
				result = false;
			}
		}
		
		System.out.println(result ? "[OK]" : "[NOK]");
		assert result;		
	}
}
