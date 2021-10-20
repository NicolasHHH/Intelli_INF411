import java.util.LinkedList;

public class Test3 {

	// teste le constructeur de HashTable
	static void testHashTable() {
		HashTable t = new HashTable();
		assert (t.buckets != null) : "\nLe champ buckets n'a pas été initialisé dans le constructeur de HashTable.";
		assert (t.buckets.size() == HashTable.M) : "\nLe vecteur bucket devrait avoir taille " + HashTable.M
				+ ". Actuellement, sa capacité est " + t.buckets.capacity() + " et sa taille est " + t.buckets.size()
				+ ".";
		for (LinkedList<Quadruple> bucket : t.buckets) {
			assert (bucket != null
					&& bucket.equals(new LinkedList<>())) : "\nChaque case de bucket doit contenir une liste vide.";
		}
	}

	// teste les méthodes add et find
	static void testAddFind(HashTable t, Row r1, Row r2, int height, long result, boolean alreadyIn) {
		if (alreadyIn) {
			// on vérifie que (r1, r2, height) est présent dans la table t
			assert (t.find(r1, r2, height) != null) : "\nLe triplet (" + r1 + ", " + r2 + ", " + height
					+ ") devrait être dans la table.";
			// on vérifie que la première recherche n'a pas supprimé l'entrée
			assert (t.find(r1, r2, height) != null) : "\nLe triplet (" + r1 + ", " + r2 + ", " + height
					+ ") devrait être dans la table.";
			// on vérifie que le résultat associé est correct
			assert (t.find(r1, r2, height).equals(result)) : "\nLe résultat associe au triplet (" + r1 + ", " + r2
					+ ", " + height + ") devrait être " + result + ".";
		} else {
			// sinon, on vérifie que (r1, r2, height) n'est pas présent dans la table t,
			assert (t.find(r1, r2, height) == null) : "\nLe triplet (" + r1 + ", " + r2 + ", " + height
					+ ") ne devrait pas être dans la table.";
			// on ajoute (r1, r2, height, result) à la table t
			t.add(r1, r2, height, result);
			// et on vérifie qu'il est bien dans la table t
			testAddFind(t, r1, r2, height, result, true);
		}
	}

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test3.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		// test du constructeur de HashTable
		System.out.print("Test du constructeur de HashTable ... ");
		testHashTable();
		System.out.println("[OK]");

		// tous les mots sur 0/1 de taille au plus 10
		Row[] rows = new Row[512];
		for (int i = 0; i < 512; i++) {
			int[] bits = new int[10];
			for (int j = 0; j < 10; j++)
				bits[j] = (i >> j) & 1;
			rows[i] = new Row(bits);
		}

		HashTable t = new HashTable();

		// tests des méthodes hashCode et bucket
		System.out.print("Test des méthodes hashCode et bucket ... ");
		int[] count = new int[HashTable.M];
		// on calcule les bucket de beaucoup de triplets ...
		for (int i1 = 0; i1 < 512; i1++)
			for (int i2 = 0; i2 < 512; i2++)
				for (int height = 0; height < 100; height++) {
					int b = t.bucket(rows[i1], rows[i2], height);
					assert (0 <= b && b < HashTable.M) : "\nLa méthode bucket doit renvoyer un nombre entre 0 et "
							+ HashTable.M + ".";
					count[b]++;
				}
		// ... et on vérifie qu'ils sont bien répartis entre 0 et M
		int nbZeros = 0;// nb de sceaux utilisés
		int mini = Integer.MAX_VALUE;
		int maxi = 0;
		for (int i = 0; i < HashTable.M; i++) {
			nbZeros += (count[i] == 0 ? 1 : 0);
			mini = Math.min(mini, count[i]);
			maxi = Math.max(maxi, count[i]);
		}
		assert (2 * nbZeros < HashTable.M) : "\nLa méthode bucket n'utilise même pas la moitié de la table.";
		assert (maxi < 1000 * (mini + 1)) : "\nLa méthode bucket utilise l'un des seaux 1000 fois plus qu'un autre.";
		System.out.println("[OK]");

		// tests des méthodes add et find
		System.out.print("Test des méthodes add et find ... ");
		// on commence par un cas particulier
		Row r1 = new Row(new int[] { 0, 0, 1, 0, 1 });
		Row r2 = new Row(new int[] { 1, 0, 1, 0, 1 });
		testAddFind(t, r1, r2, 2, 23, false);
		// un autre cas particulier
		Row r3 = new Row(new int[] { 0, 0, 1, 1, 0 });
		Row r4 = new Row(new int[] { 0, 1, 1, 1, 0 });
		testAddFind(t, r3, r4, 3, 42, false);
		// on vérifie qu'on utilise bien equals et pas ==
		Row r3bis = new Row(new int[] { 0, 0, 1, 1, 0 });
		Row r4bis = new Row(new int[] { 0, 1, 1, 1, 0 });
		testAddFind(t, r3bis, r4bis, 3, 42, true);
		// tests avec des collisions nombreuses
		for (int i1 = 0; i1 < 512; i1++)
			for (int i2 = 0; i2 < 512; i2++)
				testAddFind(t, rows[i1], rows[i2], i1 * i2, 0L, false);
		System.out.println("[OK]");
	}
}
