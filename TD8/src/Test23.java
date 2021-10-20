import java.io.File;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Set;
import java.io.FileNotFoundException;

public class Test23 {

	public static Integer find(Singly<Occurrence> occ, String word) {
		while (occ != null) {
			if (occ.element.word.equals(word))
				return occ.element.count;
			occ = occ.next;
		}
		return null;
	}

	private static void test(String textfile) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(textfile));
		sc.useDelimiter("[\\p{javaWhitespace}\\p{Punct}]+");
		Singly<String> chain = null;
		HashMap<String, Integer> dico = new HashMap<String, Integer>();
		Integer n = null;
		String current = null;
		while (sc.hasNext()) {
			current = sc.next();
			chain = new Singly<String>(current, chain);
			n = dico.get(current);
			if (n != null)
				dico.put(current, n + 1);
			else
				dico.put(current, 1);
		}
		sc.close();
		Set<String> words = dico.keySet();
		int nb_mots_diff = words.size();
		Singly<String> chaincopy = Singly.copy(chain);
		Singly<Occurrence> occ_list = Occurrence.count(chaincopy);
		assert (Singly.length(occ_list) == nb_mots_diff) : "\nIl y a "
				+ nb_mots_diff + " mots différents alors que "
				+ "votre algorithme de comptage en dénombre "
				+ Singly.length(occ_list);
		Singly<Occurrence> cursor = occ_list;
		while (cursor != null) {
			n = dico.get(cursor.element.word);
			assert (n != null) : "\nLe mot \"" + cursor.element.word
					+ "\" n'apparaît pas dans le texte";
			assert (n.equals(cursor.element.count)) : "\nLe texte contient "
					+ n + " occurences du mot \"" + cursor.element.word
					+ "\" mais votre programme en compte "
					+ cursor.element.count;
			cursor = cursor.next;
		}
		for (String word : words) {
			assert (find(occ_list, word) != null) : "\n" + "Le mot \"" + word
					+ "\" apparaît dans le texte, mais pas dans votre décompte";
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		
		//Pour s'assurer que les assert's sont actives
		if (!Test23.class.desiredAssertionStatus()) {
	        System.err.println("Vous devez activer globalement l'option -ea de la JVM");
	        System.err.println("Voir la rubirque \"Activer Assert\" au début du sujet");
	        System.exit(1);
	      }
		
		System.out.println("Question 2.3");
		
		System.out.println("Test de la méthode «count», ce test va prendre quelques secondes :");
		// La liste vide
		assert(Occurrence.count(null)==null) : "Le cas de la liste vide est mal traité.";
		// À partir de texte
		System.out.println("Comptage des mots du texte contenu dans le fichier «dummy.txt»");
		test("./TD8/dummy.txt");
		System.out.println("Comptage des mots du roman «Le tour du monde en 80 jours» (J. Verne)");
		test("./TD8/ltdme80j.txt");
		System.out.println("Comptage des mots du roman «Dracula» (B. Stoker)");
		test("./TD8/dracula.txt");
		System.out.println("Comptage des mots du roman «Ulysses» (J. Joyce)");
		test("./TD8/ulysses.txt");
		System.out.println("[OK]");
	}

}
