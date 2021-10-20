
import java.io.FileNotFoundException;
import java.util.HashMap;

import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;
import reporters.CountMarks;
import reporters.NoMarkOverwrites;
import reporters.StackReporter;
import reporters.TestReporter;
import reporters.Timer;
import reporters.WallReporter;

public class Test21 extends Test {
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
		
		Traversal traversal = new BreadthFirst();

		System.out.print("Test de la méthode BreadthFirst.traverse()...\t");
		
		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks, noMarkOverwrites, wallReporter};
			result = test("small.txt", reporters, traversal, data, canvas, print);
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
			Integer defaultMarkCounter = smallMarks.get(Ocean.defaultMark);
			if (defaultMarkCounter == null) {
				if (print) 
					System.out.println("\nVous n'avez posé aucune marque par défaut lors de l'exploration");
				result = false;				
			} else if (defaultMarkCounter < 8) {
				if (print) 
					System.out.println("\nVous avez posé très peu de marques par défaut lors de l'exploration."
							+ "\nEst-ce que vous auriez oublié de marquer ou de rajouter dans la liste à traiter les voisins de la cellule en cours ?");
				result = false;								
			} else if (defaultMarkCounter > 52) {
				if (print) 
					System.out.println("\nVous avez posé beaucoup trop de marques : " + defaultMarkCounter);					
				result = false;				
			}
		}
				
		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, countMarks, noMarkOverwrites, wallReporter};
			result = test("no-sharks.txt", reporters, traversal, data, canvas, print);
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
			Integer defaultMark = noSharkMarks.get(Ocean.defaultMark);
			Integer path = noSharkMarks.get(Traversal.path);
			Integer deadEnd = noSharkMarks.get(Traversal.deadEnd);

			if (noSharkMarks.keySet().size() != 1 || defaultMark == null || path != null || deadEnd != null) {
				if (print) {
					System.out.println("\nVous n'utilisez pas correctement toutes les marques:");
					System.out.println("\tIl ne doit en avoir qu'un seul type : defaultMark");
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

		System.out.println(result ? "[OK]" : "[NOK]");
		assert result;
	}
}
