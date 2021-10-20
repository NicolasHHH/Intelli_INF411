import java.util.LinkedList;
import java.util.Scanner;

public class Test32 {

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

	// teste la méthode isOver
	static void testIsOver(String i1, String i2, boolean o) {
		Deck di1 = stringToDeck(i1);
		Deck di2 = stringToDeck(i2);
		assert(new Battle(di1, di2, new Deck()).isOver() == o) : "\nPour la bataille dont les joueurs ont les paquets " + di1 + " et " + di2 + ", l'appel de isOver() devrait renvoyer " + o + ".";
	}
	
	// teste la méthode oneRound
	static void testOneRound(String i1, String i2, String o1, String o2, boolean o) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Deck do1 = stringToDeck(o1);
		Deck do2 = stringToDeck(o2);
		Battle b = new Battle(di1, di2, new Deck());
		assert(o == b.oneRound()) : "\nPour la bataille dont les joueurs ont les paquets " + si1 + " et " + si2 + ", l'appel de oneRound() devrait renvoyer " + o + ".";
		assert(do1.toString().equals(b.player1.toString())) : "\nPour la bataille dont les joueurs ont les paquets " + si1 + " et " + si2 + ", l'appel de oneRound() devrait transformer le paquet du premier joueur en " + do1 + " plutôt que " + b.player1 + ".";
		assert(do2.toString().equals(b.player2.toString())) : "\nPour la bataille dont les joueurs ont les paquets " + si1 + " et " + si2 + ", l'appel de oneRound() devrait transformer le paquet du deuxième joueur en " + do2 + " plutôt que " + b.player2 + ".";
		assert(!o || b.trick.cards.isEmpty()) : "\nPour la bataille dont les joueurs ont les paquets " + si1 + " et " + si2 + ", le pli après l'appel de oneRound() devrait être vide.";
	}
	
	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test32.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}
		
		// tests de la méthode isOver
		System.out.print("Test de la méthode isOver ... ");
		testIsOver("", "", true);
		testIsOver("1", "", true);
		testIsOver("", "1", true);
		testIsOver("1", "1", false);
		System.out.println("[OK]");

		// tests de la méthode oneRound
		System.out.print("Test de la méthode oneRound ... ");
		// pas assez de cartes
		testOneRound("", "", "", "", false);
		testOneRound("1", "", "1", "", false);
		testOneRound("1", "1", "", "", false);
		testOneRound("1 1", "1 2", "", "", false);
		testOneRound("1 1 2 2 3 3", "1 2 2 1 3 3", "", "", false);
		// assez de cartes
		testOneRound("1", "2", "", "1 2", true);
		testOneRound("2", "1", "2 1", "", true);
		testOneRound("1 1 2", "1 1 3", "", "1 1 1 1 2 3", true);
		testOneRound("1 1 2 3 2 3", "1 1 2 1 3 2", "3", "2 1 1 1 1 2 2 3 1 2 3", true);
		testOneRound("1 2 3 4 1 2 3 4 1 2 3 4 4 3 2 1", "1 2 3 4 1 2 3 4 1 2 3 4 1 2 3 4", "3 2 1 1 1 2 2 3 3 4 4 1 1 2 2 3 3 4 4 1 1 2 2 3 3 4 4 4 1", "2 3 4", true);
		System.out.println("[OK]");
	}

}
