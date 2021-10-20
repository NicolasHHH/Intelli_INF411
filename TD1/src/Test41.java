import java.util.LinkedList;
import java.util.Scanner;

public class Test41 {
	
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

	// teste la méthode game()
	static void testGame(String i1, String i2, int o) {
		Deck di1 = stringToDeck(i1);
		String si1 = di1.toString();
		Deck di2 = stringToDeck(i2);
		String si2 = di2.toString();
		Battle b = new Battle(di1, di2, new Deck());
		assert(o == b.game()) : "\nPour la bataille dont les joueurs ont les paquets " + si1 + " et " + si2 + ", l'appel de game() devrait renvoyer " + o + ".";
	}
	
	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test23.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}
		
		// tests de la méthode game()
		System.out.print("Test de la méthode game() ... ");
		// égalité
		testGame("", "", 0);
		testGame("1", "1", 0);
		// player1 gagne
		testGame("1", "", 1);
		testGame("1 1", "1", 1);		
		testGame("2", "1", 1);		
		// player2 gagne
		testGame("", "1", 2);
		testGame("1", "1 1", 2);
		testGame("1", "2", 2);
		// partie infinie
		testGame("1 2 1 2","2 1 2 1", 3);
		System.out.println("[OK]");
	}
}
