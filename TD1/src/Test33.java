import java.util.LinkedList;
import java.util.Scanner;

public class Test33 {

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

	// teste la méthode winner
	static void testWinner(String i1, String i2, int o) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Battle b = new Battle(di1, di2, new Deck());
		assert(o == b.winner()) : "\nPour la bataille dont les joueurs ont les paquets " + si1 + " et " + si2 + ", l'appel de winner() devrait renvoyer " + o + ".";
	}

	// teste la méthode game(int turns)
	static void testGame(String i1, String i2, int turns, int o) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Battle b = new Battle(di1, di2, new Deck());
		assert(o == b.game(turns)) : "\nPour la bataille dont les joueurs ont les paquets " + si1 + " et " + si2 + ", l'appel de game(" + turns + ") devrait renvoyer " + o + ".";
	}

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test33.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}

		// tests de la méthode winner
		System.out.print("Test de la méthode winner ... ");
		// égalité
		testWinner("", "", 0);
		testWinner("1", "1", 0);
		testWinner("1 1", "1 1", 0);
		// player1 gagne
		testWinner("1", "", 1);
		testWinner("1 1", "1", 1);
		testWinner("1 1 1", "1", 1);
		// player2 gagne
		testWinner("", "1", 2);
		testWinner("1", "1 1", 2);
		testWinner("1", "1 1 1", 2);
		System.out.println("[OK]");

		// tests de la méthode game(int turns)
		System.out.print("Test de la méthode game(int turns) ... ");
		// égalité
		testGame("", "", 1, 0);
		testGame("1", "1", 1, 0);
		testGame("1 1", "1 1", 1, 0);
		// player1 gagne
		testGame("1", "", 1, 1);
		testGame("1 1", "1", 1, 1);
		testGame("1 1 1", "1", 1, 1);
		testGame("1 1 2", "1 1 1", 2, 1);
		// player2 gagne
		testGame("", "1", 1, 2);
		testGame("1", "1 1", 1, 2);
		testGame("1", "1 1 1", 1, 2);
		testGame("1 1 1 3 3", "1 1 2", 5, 2);

		// une bataille un peu spéciale
		testGame("1 2 1 2","2 1 2 1", 1, 2);
		testGame("1 2 1 2","2 1 2 1", 2, 0);
		testGame("1 2 1 2","2 1 2 1", 3, 2);
		testGame("1 2 1 2","2 1 2 1", 4, 0);
		testGame("1 2 1 2","2 1 2 1", 5, 1);
		testGame("1 2 1 2","2 1 2 1", 6, 0);
		testGame("1 2 1 2","2 1 2 1", 7, 1);
		testGame("1 2 1 2","2 1 2 1", 8, 0);
		testGame("1 2 1 2","2 1 2 1", 9, 1);
		testGame("1 2 1 2","2 1 2 1", 10, 0);
		testGame("1 2 1 2","2 1 2 1", 11, 1);
		System.out.println("[OK]");

	}
}
