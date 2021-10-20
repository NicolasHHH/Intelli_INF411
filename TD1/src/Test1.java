import java.util.LinkedList;
import java.util.Scanner;

public class Test1 {

	// crée un paquet de cartes à partir d'une chaîne de caractères
	static Deck stringToDeck(String s) {
		Scanner sc = new Scanner(s);
		LinkedList<Integer> cards = new LinkedList<Integer>();
		while (sc.hasNextInt()) {
			cards.addLast(sc.nextInt());
		}
		sc.close();
		return new Deck(cards);
	}

	// teste la méthode pick
	static void testPick(String i1, String i2, String o1, String o2, Integer e) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Deck do1 = stringToDeck(o1);
		Deck do2 = stringToDeck(o2);
		int x = di1.pick(di2);
		assert(x == e) : "\nPour les paquets d1 = " + si1 + " et d2 = " + si2
				+ ", l'appel de d1.pick(d2) devrait renvoyer " + e + " au lieu de " + x + ".";
		assert(di1.toString().equals(do1.toString())) : "\nPour les paquets d1 = " + si1 + " et d2 = " + si2
				+ ", l'appel de d1.pick(d2) devrait transformer d1 en  " + do1 + " au lieu de " + di1 + ".";
		assert(di2.toString().equals(do2.toString())) : "\nPour les paquets d1 = " + si1 + " et d2 = " + si2
				+ ", l'appel de d1.pick(d2) devrait transformer d2 en " + do2 + " au lieu de " + di2 + ".";
	}

	// teste la méthode pickAll
	static void testPickAll(String i1, String i2, String o1) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Deck do1 = stringToDeck(o1);
		di1.pickAll(di2);
		assert(di1.toString().equals(do1.toString())) : "\nPour les paquets d1 = " + si1 + " et d2 = " + si2
				+ ", l'appel de d1.pickAll(d2) devrait transformer d1 en " + do1 + " au lieu de " + di1 + ".";
		assert(di2.cards.isEmpty()) : "\nL'appel de d1.pickAll(d2) devrait vider d2.";
	}

	// teste la méthode isValid
	static void testIsValid(int nbVals, String s, boolean b) {
		Deck d = stringToDeck(s);
		assert(d.isValid(nbVals) == b) : "\nPour le paquet d = " + d + ", l'appel de d.isValid() devrait renvoyer " + b
				+ ".";
	}

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test1.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}

		// tests de la méthode pick
		System.out.print("Test de la méthode pick ... ");
		testPick("", "", "", "", -1);
		testPick("1 2", "", "1 2", "", -1);
		testPick("1 2 3", "4 5 6", "1 2 3 4", "5 6", 4);
		testPick("", "1", "1", "", 1);
		System.out.println("[OK]");

		// tests de la méthode pickAll
		System.out.print("Test de la méthode pickAll ... ");
		testPickAll("", "", "");
		testPickAll("1 1", "", "1 1");
		testPickAll("1 2 3", "4 5 6", "1 2 3 4 5 6");
		testPickAll("", "1 2 3", "1 2 3");
		System.out.println("[OK]");

		// tests de la méthode isValid
		System.out.print("Test de la méthode isValid ... ");
		// valeurs hors des bornes
		testIsValid(1, "0", false);
		testIsValid(1, "1 1 1 2", false);
		// valeurs répétées trop de fois
		testIsValid(1, "1 1 1 1 1", false);
		testIsValid(3, "3 1 3 2 3 2 1 3 2 3", false);
		// paquets valides
		testIsValid(1, "", true);
		testIsValid(1, "1 1", true);
		testIsValid(2, "1 1 1 2", true);
		testIsValid(3, "1 2 2 3 2 2 1 3 3", true);
		testIsValid(3, "1 3 1 3 3", true);
		System.out.println("[OK]");
	}
}
