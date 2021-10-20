
public class Test31 {

	// teste le constructeur de Battle
	static void testBattle(int nbVals) {
		Battle b = new Battle(nbVals);
		// on vérifie que chaque joueur a bien 2*nbVals cartes
		assert(b.player1.cards.size() == 2*nbVals && b.player1.cards.size() == 2*nbVals) : "\n Après l'appel de Battle(" + nbVals + "), chaque joueur devrait avoir " + (2*nbVals) + " cartes.";
		// on reconstruit le paquet complet et on vérifie qu'il est valide
		Deck d = new Deck();
		d.pickAll(b.player1.copy());
		d.pickAll(b.player2.copy());
		assert(d.cards.size() == 4*nbVals && d.isValid(nbVals)) : "\nLes cartes sont mal réparties.";
		// on vérifie que les cartes ont été mélangées avant d'être distribuées
		// pour ça on vérifie qu'on ne peut pas obtenir le paquet rangé à partir de player1 et player2 de 4 façons différentes
		String tri = new Deck(nbVals).toString();
		//   * d'abord player1, ensuite player2 (déjà construit)
		assert(!d.toString().equals(tri)) : "\nLes cartes n'ont pas été mélangées avant la répartition.";
		//   * d'abord player2, ensuite player1
		d = new Deck();
		d.pickAll(b.player2.copy());
		d.pickAll(b.player1.copy());
		assert(!d.toString().equals(tri)) : "\nLes cartes n'ont pas été mélangées avant la répartition.";
		//   * alternance player1 puis player2
		d = new Deck();
		for (int i = 0; i < 2*nbVals; i++) {
			d.pick(b.player1);
			d.pick(b.player2);
		}
		assert(!d.toString().equals(tri)) : "\nLes cartes n'ont pas été mélangées avant la répartition.";
		//   * alternance player2 puis player1
		d = new Deck();
		for (int i = 0; i < 2*nbVals; i++) {
			d.pick(b.player2);
			d.pick(b.player1);
		}
		assert(!d.toString().equals(tri)) : "\nLes cartes n'ont pas été mélangées avant la répartition.";
	}
	
	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test31.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}
		
		// tests du constructeur de Battle
		System.out.print("Test du constructeur de Battle ... ");
		testBattle(13);
		testBattle(20);
		System.out.println("[OK]");
	}
}
