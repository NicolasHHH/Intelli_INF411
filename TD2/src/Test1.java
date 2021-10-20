public class Test1 {

	// crée une ligne de fruits à partir d'une chaîne de caractères
	static Row stringToRow(String s) {
		int[] fruits = new int[s.length()];
		for (int i = 0; i < s.length(); i++)
			fruits[i] = (s.charAt(i) == '0' ? 0 : 1);
		return new Row(fruits);
	}

	// teste la méthode addFruit
	// on suppose que si + "f" et so sont de même longueur
	static void testAddFruit(String si, int f, String so) {
		assert (stringToRow(si).addFruit(f).equals(stringToRow(so))) : "\nLa ligne\n" + si
				+ "apres l'appel de addFruit(" + f + ") devrait être la ligne\n" + so + ".";
	}

	// teste la méthode allStableRows
	static void testAllStableRows(int n, int r) {
		int x = Row.allStableRows(n).size();
		assert (x == r) : "\nIl y a " + r + " lignes stables de largeur " + n
				+ " (votre méthode allStableRows en trouve " + x + ").";
	}

	// teste la méthode areStackable
	static void testAreStackable(String s1, String s2, String s3, boolean e) {
		assert (e == stringToRow(s1).areStackable(stringToRow(s2), stringToRow(s3))) : "\nLes lignes\n" + s1 + "\n" + s2
				+ "\n" + s3 + "\n" + (e ? "devraient " : "ne devraient pas ") + "être empilables.";
	}

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test1.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		// tests de la méthode addFruit
		System.out.print("Test de la méthode addFruit ... ");
		testAddFruit("", 0, "0");
		testAddFruit("", 1, "1");
		testAddFruit("011", 0, "0110");
		testAddFruit("0100" + "1", 0, "010010");
		testAddFruit("100110", 1, "1001101");
		System.out.println("[OK]");

		// tests de la méthode allStableRows
		System.out.print("Test de la méthode allStableRows ... ");
		int[] nums = new int[] { 1, 2, 4, 6, 10, 16, 26, 42, 68, 110, 178, 288, 466, 754, 1220, 1974, 3194, 5168, 8362,
				13530 };
		for (int n = 0; n < 20; n++)
			testAllStableRows(n, nums[n]);
		System.out.println("[OK]");

		// tests de la méthode areStackable
		System.out.print("Test de la méthode areStackable ... ");
		// taille différente
		testAreStackable("1010", "011", "100", false);
		// test de la permière et dernière colonne (au cas ou une boucle for commence à
		// 1 au lieu de 0)
		testAreStackable("1", "1", "1", false);
		testAreStackable("0", "0", "0", false);
		// autres exemples
		testAreStackable("1011", "0110", "1100", true);
		testAreStackable("1011", "0110", "1101", true);
		testAreStackable("0001", "0110", "1100", true);
		testAreStackable("1101", "1110", "1100", false);
		testAreStackable("101011", "011011", "110011", false);
		testAreStackable("101", "011", "111", false);
		System.out.println("[OK]");
	}
}
