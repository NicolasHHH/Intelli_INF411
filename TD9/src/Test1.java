
import java.io.FileNotFoundException;
import java.util.HashMap;

import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;
import reporters.BackTrackReporter2;
import reporters.CountMarks;
import reporters.NoMarkOverwrites;
import reporters.StackReporter;
import reporters.TestReporter;
import reporters.Timer;
import reporters.WallReporter;

public class Test1 extends Test {
	private static final String expectedTrace = "(16,27)(17,27)(18,27)(18,28)(18,29)(18,30)(19,30)(20,30)(21,30)(22,30)(22,31)(23,31)(24,31)(24,32)(24,33)(24,34)(24,35)(24,36)(24,37)(25,37)(26,37)(27,37)(28,37)(29,37)(30,37)(30,38)(31,38)(31,39)(30,39)(29,39)(28,39)(27,39)(26,39)(25,39)(24,39)(23,39)(22,39)(21,39)(20,39)(19,39)(18,39)(17,39)(16,39)(15,39)(14,39)(14,38)(14,37)(15,37)(16,37)(16,36)(16,35)(15,35)(14,35)(13,35)(12,35)(11,35)(10,35)(10,34)(10,33)(9,33)(8,33)(7,33)(7,32)(7,31)(6,31)(5,31)(5,30)(5,29)(5,28)(6,28)(7,28)(7,27)(7,26)(7,25)(7,24)(7,23)(7,22)(8,22)(9,22)(9,21)(9,20)(9,19)(9,18)(9,17)(9,16)(10,16)(10,15)(10,14)(10,13)(10,12)(9,12)(8,12)(7,12)(6,12)(5,12)(5,11)(5,10)(4,10)(3,10)(3,11)(3,12)(3,13)(3,14)(2,14)(1,14)(1,13)(1,12)(1,11)(1,10)(1,9)(2,9)(3,9)(3,8)(3,7)(3,6)(3,5)(3,4)(3,3)(3,2)(3,1)(2,1)(1,1)";
	private static final String expectedTraceWithNemo = "(16,26)(16,27)(17,27)(18,27)(18,28)(18,29)(18,30)(19,30)(20,30)(21,30)(22,30)(22,31)(23,31)(24,31)(24,32)(24,33)(24,34)(24,35)(24,36)(24,37)(25,37)(26,37)(27,37)(28,37)(29,37)(30,37)(30,38)(31,38)(31,39)(30,39)(29,39)(28,39)(27,39)(26,39)(25,39)(24,39)(23,39)(22,39)(21,39)(20,39)(19,39)(18,39)(17,39)(16,39)(15,39)(14,39)(14,38)(14,37)(15,37)(16,37)(16,36)(16,35)(15,35)(14,35)(13,35)(12,35)(11,35)(10,35)(10,34)(10,33)(9,33)(8,33)(7,33)(7,32)(7,31)(6,31)(5,31)(5,30)(5,29)(5,28)(6,28)(7,28)(7,27)(7,26)(7,25)(7,24)(7,23)(7,22)(8,22)(9,22)(9,21)(9,20)(9,19)(9,18)(9,17)(9,16)(10,16)(10,15)(10,14)(10,13)(10,12)(9,12)(8,12)(7,12)(6,12)(5,12)(5,11)(5,10)(4,10)(3,10)(3,11)(3,12)(3,13)(3,14)(2,14)(1,14)(1,13)(1,12)(1,11)(1,10)(1,9)(2,9)(3,9)(3,8)(3,7)(3,6)(3,5)(3,4)(3,3)(3,2)(3,1)(2,1)(1,1)";

	public static void main(String[] args) throws FileNotFoundException {
		System.out.print("Test de la méthode DepthFirst.traverse()...\t");

		boolean canvas = true;
		boolean print = true;
		boolean result = true;

		HashMap<String, Object> data = new HashMap<>(); 
		
		Timer timer = new Timer("timer", data, print);
		CountMarks countMarks = new CountMarks("markCounters", data, print);
		StackReporter stackReporter = new StackReporter("stack", data, 300, print);
		NoMarkOverwrites noMarkOverwrites = new NoMarkOverwrites();
		BackTrackReporter2 backTrackReporter = new BackTrackReporter2("backTrack", data, print);
		WallReporter wallReporter = new WallReporter();
		
		Traversal traversal = new DepthFirst();
		boolean nemoMarkedAsPath = false;
		
		if (result) {
			try {
				TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks};
				result = test("not-lost.txt", reporters, traversal, data, canvas, print);
			}
			catch (StackOverflowError e) 
			{
				if (print)
					System.err.println("\nAuriez-vous oublié de faire le test isMarked() ?");					
				throw e;
			}	
			
			if (!result) {
				if (print) {
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
					System.out.println("Auriez-vous oublié le test isNemoAt() ?"
							+ " (Lorsque Nemo se trouve dans la même case que Marin, il ne faut pas chercher plus loins.)");
				}
			}
			else {
				HashMap<Mark, Integer> notLostMarks = countMarks.getData("not-lost.txt");
				nemoMarkedAsPath = notLostMarks.size() == 1 && notLostMarks.get(Traversal.path) != null;
				
				if (notLostMarks.size() > 1 && !nemoMarkedAsPath) {
					if (print)
						System.out.println("\nEst-ce que vous avez bien mis le test isNemoAt() suffisamment tôt ?"
								+ " (Lorsque Nemo se trouve dans la même case que Marin, il ne faut pas chercher plus loins.)");
					result = false;
				}	
			}
		}
		
		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks};
			result = test("small.txt", reporters, traversal, data, canvas, print);
			if (!result)
				if (print) {
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
				}

			HashMap<Mark, Integer> smallMarks = countMarks.getData("small.txt");
			if (smallMarks.get(Ocean.defaultMark) == null) {
				if (print) 
					System.out.println("\nVous avez oublié de poser la marque par défaut lors de l'exploration");
				result = false;				
			}
		}

		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter};
			result = test("small2.txt", reporters, traversal, data, canvas, print);
			if (!result) 
				if (print) {
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
				}
			else {				
			}
		}
		
		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks, noMarkOverwrites, wallReporter, backTrackReporter};
			result = test("no-sharks.txt", reporters, traversal, data, canvas, print);
			if (!result)
				if (print)
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");

			if (stackReporter.getData("no-sharks.txt") < 50) {
				if (print) {
					System.out.println("\nIl semblerait que votre récursion s'arrête trop tôt.");
					System.out.println(
							"\tEst-ce que vous regardez bien la valeur retournée par les appels récursifs à traverse(...) ?");
					System.out.println("\tEst-ce que vous ne la renvoyez pas directement ?");
				}
				result = false;
			}

			HashMap<Mark, Integer> noSharkMarks = countMarks.getData("no-sharks.txt");
			Integer defaultMark = noSharkMarks.get(Ocean.defaultMark);
			Integer path = noSharkMarks.get(Traversal.path);
			Integer deadEnd = noSharkMarks.get(Traversal.deadEnd);

			if (noSharkMarks.keySet().size() != 3 || defaultMark == null || path == null || deadEnd == null) {
				if (print) {
					System.out.println("\nVous n'utilisez pas correctement toutes les marques:");
					System.out.println("\tIl doit en avoir trois types : defaultMark, path, deadEnd");
				}
				result = false;
			} else if (defaultMark != path + deadEnd - (nemoMarkedAsPath ? 1 : 0)) {
				if (print) {
					System.out.println("\nIl y a un problème dans votre utilisation de marques : ");
					System.out.println("\tchaque cellule doit être marquée une fois par 'defaultMark' lorsqu'on y rentre, ");
					System.out.println("\tpuis une fois soit par 'deadEnd', soit par 'path', lorsqu'on en sorte.");
				}
				result = false;
			}
			
			String trace = backTrackReporter.toString();
			if (!trace.equals(expectedTrace) && !trace.equals(expectedTraceWithNemo)) {
				if (print) {
					System.out.println("Le chemin de retour est incorrect");
					System.out.println("Celui attendu est : ");
					System.out.println(expectedTrace);
					System.out.println("Sinon, avec la cellule de Nemo : ");
					System.out.println(expectedTraceWithNemo);
				}
				result = false;
			}
		}

		if (result) {
			// Si Nemo n'est pas là, on ne peut pas le trouver => retour attendu est 'false'
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter};
			result = !test("no-nemo.txt", reporters, traversal, data, canvas, print);
			if (!result)
				if (print) {
					System.out.println("\nVous trouvez Nemo, alors qu'il n'est pas dans l'océan ! (traverse() doit renvoyer 'false')");
				}
			else {			
			}
		}		

		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter};
			result = test("with-hole.txt", reporters, traversal, data, canvas, print);
			if (!result) 
				if (print) {
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
				}
			else {				
			}
		}

		System.out.println(result ? "[OK]" : "[NOK]");
		assert result;
	}
}
