import java.util.HashSet;

public class Test23 {
	
	// renvoie le nombre de sous-tableaux consécutifs de t de taille a ne contenant que b éléments distincts 
	static int countFewValues(Object[] t, int a, int b) {
		int count = 0;
		HashSet<Object> values = new HashSet<Object>();
		for (int i = 0; i < t.length - (a - 1); i++) {
			values.clear();
			for (int k = 0; k < a; k++)
				values.add(t[i+k]);
			count += (values.size() <= b ? 1 : 0);
		}
		return count;
	}

	// teste si un paquet est mal mélangé (ie. contient au moins 14 paires ou 3 quadruplets, ou 2 octuplets).
	static boolean isPoorlyMixed(Deck d) {
		Object[] t = d.cards.toArray();
		return countFewValues(t, 2, 1) >= 14 || countFewValues(t, 4, 1) >= 3 || countFewValues(t, 8, 2) >= 2;  
	}

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test23.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}

		// test de la méthode riffleShuffle
		System.out.print("Test de la méthode riffleShuffle ... (peut prendre jusqu'à 1 minute) ... ");
		int countPoorlyMixed = 0;
		for (int i = 0; i < 1000000; i++) {
			Deck d = new Deck(13);
			d.riffleShuffle(7);
			if (isPoorlyMixed(d))
				countPoorlyMixed++;
			assert(countPoorlyMixed < 5) : "\nLe mélange effectué par riffleShuffle semble mauvais.";
		}
		System.out.println("[OK]");
	}

}
