
public class Test21b {

	// teste la méthode split
	static void testSplit(Deck d) {
		Deck r = d.copy();
		Deck l = r.split();
		// vérifie que le premier paquet n'est pas vide
		assert(!l.cards.isEmpty()) : "\nLe paquet renvoyé par la méthode split est vide. C'est possible mais peu probable. Recommencez le test.";
		// vérifie que le paquet renvoyé par la methode split est la première partie du paquet
		while (!l.cards.isEmpty())
			assert(l.cards.removeFirst() == d.cards.removeFirst()) : "\nLa méthode split doit renvoyer la première partie du paquet.";
		// vérifie que le paquet qui reste après la méthode split est la deuxième partie du paquet
		while (!r.cards.isEmpty())
			assert(r.cards.removeFirst() == d.cards.removeFirst()) : "\nLa méthode split doit supprimer la première partie du paquet.";
	}

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test21b.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}

		// tests de la méthode split
		System.out.print("Test de la méthode split ... ");
		for (int i = 0; i < 100; i++)
			testSplit(new Deck(13));
		System.out.println("[OK]");
	}
}
