
public class Test21a {

	// calcule la déviation d'un tableau par rapport à la loi binomiale
	static double deviation(double[] hist) {
		int n = hist.length-1;
		double max = 0;
		for (int i = 0; i <= n; i++) {
			// calcul du coefficient binomial (n,i)
			double coeff = 1;
			for (int j = 1; j <= i; j++)
				coeff *= (double) (n + 1 - j) / j;
			coeff /= (long) 1 << n;
			// comparaison avec hist[i]
			max = Math.max(max, Math.abs(coeff - hist[i]));
		}
		return max;
	}

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test21a.class.desiredAssertionStatus()) {
			System.err.println("Vous devez passer l'option -ea à la machine virtuelle Java.");
			System.err.println("Voir la section 'Activer assert' du préambule du TD.");
			System.exit(1);
		}

		System.out.print("Test de la méthode cut ... ");
		int n = 52; // taille du jeu
		int m = 100000; // nombre de coupes aléatoires
		double[] hist = new double[n + 1];
		Deck d = new Deck(13);
		// calcule de m coupes aléatoires selon la méthode cut
		for (int i = 0; i < m; i++) {
			int c = d.cut();
			assert(0 <= c && c < n) : "\nL'appel de cut devrait renvoyer un nombre entre 0 (inclus) et " + n + " (exclu).";
			hist[c] += 1./m;
		}
		// calcule de la déviation par rapport à la loi binomiale en norme sup
		double max = deviation(hist);
		assert(max < 0.003) : ("\nPour n = " + n + " et m = " + m
				+ ", votre méthode cut donne une déviation en norme sur de " + max + " qui dépasse 0.003.\n"
				+ "Il y a sans doute un problème.");
		assert(max < 0.0025) : ("\nPour n = " + n + " et m = " + m
				+ ", votre méthode cut donne une déviation en norme sur de " + max + " qui dépasse 0.0025.\n"
				+ "C'est possible mais peu probable. Recommencez le test.");
		System.out.println("[OK]");
	}
}
