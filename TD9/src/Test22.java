
import java.io.FileNotFoundException;
import java.util.HashMap;

import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;
import reporters.CountMarks;
import reporters.DistanceReporter;
import reporters.NemoDistanceReporter;
import reporters.NoMarkOverwrites;
import reporters.StackReporter;
import reporters.TestReporter;
import reporters.Timer;
import reporters.WallReporter;

public class Test22 extends Test {
	public static void main(String[] args) throws FileNotFoundException {

		boolean canvas = true;
		boolean print = true;
		boolean result = true;

		HashMap<String, Object> data = new HashMap<>(); 
		
		Timer timer = new Timer("timer", data, print);
		CountMarks countMarks = new CountMarks("markCounters", data, print);
		StackReporter stackReporter = new StackReporter("stack", data, 1000, print);
		NemoDistanceReporter nemoDistanceReporter = new NemoDistanceReporter("nemoDistance", data, print);
		NoMarkOverwrites noMarkOverwrites = new NoMarkOverwrites();
		WallReporter wallReporter = new WallReporter();
		DistanceReporter distanceReporter = new DistanceReporter("distance", data, print);
		
		Traversal traversal = new BreadthFirstWithDistance();

		System.out.print("Test de la méthode BreadthFirstWithDistance.traverse()...\t");
		
		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, nemoDistanceReporter, countMarks, noMarkOverwrites, wallReporter};
			result = test("small.txt", reporters, traversal, data, canvas, print);
			if (!result) {
				if (print) {
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
					
					if (timer.getCount() <= 1)
						System.out.println("Vous n'avez exploré aucune cellule :"
								+ " Est-ce que vous auriez oublié d'ajouter la cellule de départ dans la liste à traiter ?");
					else if (timer.getCount() <= 3)
						System.out.println("Vous n'avez exploré que très peu de cellules :"
								+ " Est-ce que vous avez oublié d'ajouter les voisins dans la liste à traiter ?");
					else
						System.out.println("Est-ce que vous auriez oublié le test isNemoAt() ?");
				}							
			} else {
				Integer distance = nemoDistanceReporter.getData("small.txt");
				if (distance == null) {
					if (print)
						System.out.println("Est-ce que vous avez oublié de marquer les cellules avec leur distances de Marin ?");
					result = false;								
				} else  if (distance == 0) {
					if (print)
						System.out.println("La distance est mal calculée. Vous avez certainement oublié les incréments dans les marquages.");
					result = false;				
				} else if (distance != 9) {
					if (print)
						System.out.println("La distance est mal calculée. Vérifiez les incréments dans les marquages.");
					result = false;								
				}
			}				
					
			HashMap<Mark, Integer> smallMarks = countMarks.getData("small.txt");
			Integer markCounter = 0;
			if (smallMarks != null) 
				for (Mark mark : smallMarks.keySet())
					markCounter += smallMarks.get(mark);
			
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
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, nemoDistanceReporter};
			result = test("small2.txt", reporters, traversal, data, canvas, print);
			if (!result) 
				if (print) {
					System.out.println("\nVous ne trouvez pas Nemo, alors que vous auriez dû le trouver (traverse() doit renvoyer 'true')");
				}
			else {				
			}
		}
				
		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, nemoDistanceReporter, countMarks, noMarkOverwrites, wallReporter};
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

			if (defaultMark != null || path != null || deadEnd != null) {
				if (print) {
					System.out.println("\nVous n'utilisez pas correctement toutes les marques:");
					System.out.println("\tIl ne doit y avoir que des nombre entiers (pas de defaultMark, path ou deadEnd)");
				}
				result = false;
			}
		}

		if (result) {
			TestReporter[] reporters = new TestReporter[] { distanceReporter };
			result = test("no-sharks.txt", reporters, traversal, data, canvas, false);
			Integer distance = distanceReporter.getData("no-sharks.txt");			

			if (distance < 110)
				if (print) {
					System.out.println("Il semblerait que vous calculez les distances dans une variable locale de votre");
					System.out.println("\tméthode traverse() au lieu d'utiliser celle de la cellule précedente pour");
					System.out.println("\tcalculer la suivante. Cela peut marcher mais rend votre algorithme");
					System.out.println("\tsensiblement plus compliqué.");
				}
		}
		
		if (result) {
			// Si Nemo n'est pas là, on ne peut pas le trouver => retour attendu est 'false'
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, nemoDistanceReporter};
			result = !test("no-nemo.txt", reporters, traversal, data, canvas, print);
			if (!result)
				if (print) {
					System.out.println("\nVous trouvez Nemo, alors qu'il n'est pas dans l'océan ! (traverse() doit renvoyer 'false')");
				}
			else {			
			}
		}		

		if (result) {
			TestReporter[] reporters = new TestReporter[] {timer, stackReporter, nemoDistanceReporter};
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
