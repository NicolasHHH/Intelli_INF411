import java.util.Random;

public class Test24 {
	public static void main(String[] args) {

		if (!Test24.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}
		
		System.out.print("Test de cumulative... ");
		 testCumulative(16, 0);
		 testCumulative(31, 0);
		 testCumulative(32, 0);
		 testCumulative(33, 0);
		 testCumulative(145, 12);
		 testCumulative(1000, 0);
		 testCumulative(10000, 0);
		 testCumulative(100000, 0);
		 System.out.println("\t\t\t [OK].");
	}
	
	static void testCumulative(int size, int offset) {
		Fenwick t = new Fenwick(offset, offset + size);

		testWellFormed(t);
		testWellMaintained(t);

		int[] acc = new int[size];
		Random r = new Random();
		int nb = 100000;
		for (int i = 0; i < nb; i++) {
			int s = r.nextInt(size);
			acc[s]++;
			t.inc(offset + s);
		}

		testWellFormed(t);
		testWellMaintained(t);

		assert t.cumulative(offset) == 0 : "\ncumulative(lo) doit être égal à zéro.";
		assert t.cumulative(
				offset + size + 10) == nb : "\nSi i >= hi, cumulative(i) doit renvoyer la somme de tous les éléments.";

		long startTime = System.currentTimeMillis();

		for (int i = 0; i < size; i++) {
			assert acc[i] == t.cumulative(offset + i + 1)
					- t.cumulative(offset + i) : "\nMauvaise valeur de retour pour cumulative.";
		}

		long endTime = System.currentTimeMillis();
		// System.out.println(String.format("%d appels à 'cumulative' dans [%d, %d] en
		// %d millisecondes.", 2 * size, t.lo,
		// t.hi, endTime - startTime));

		if (endTime > startTime + 60)
			System.out.println("\nL'implémentation de 'cumulative' est lente.");

		assert endTime < startTime
				+ 200 : "\nL'implémentation de cumulative n'a probablement pas une complexité logarithmique.";

	}
	
	/*
	 * Tout ce qui suit concerne les méthodes générales utilisées dans les Tests.
	 */

	static int nbLeafs(Fenwick t) {
		if (t.left == null) {
			return 1;
		} else {
			return nbLeafs(t.left) + nbLeafs(t.right);
		}
	}

	static int depth(Fenwick t) {
		if (t.left == null) {
			return 0;
		} else {
			return 1 + Math.max(depth(t.left), depth(t.right));
		}
	}

	static void testWellFormed(Fenwick t) {
		if (t.left == null && t.right == null && t.hi == t.lo + 1)
			return;

		assert t.left != null && t.right != null : String.format("\nLe nœud interne [%d, %d[ n'a qu'un seul fils.", t.lo, t.hi);
		assert t.left.lo == t.lo && t.right.hi == t.hi && t.left.hi == t.right.lo : String.format(
				"\nLe nœud [%d, %d[ est mal recouvert par ses enfants [%d, %d[ et [%d, %d[.", t.lo, t.hi, t.left.lo,
				t.right.hi, t.right.lo, t.right.hi);

		testWellFormed(t.left);
		testWellFormed(t.right);
	}

	static void testWellMaintained(Fenwick t) {
		if (t.left != null) {
			assert t.acc == t.left.acc + t.right.acc : String.format(
					"\nL'accumulateur de l'intervalle [%d,%d[ n'est pas égal\n"
							+ "à la sommes des accumulateurs des enfants : %d != %d + %d",
					t.lo, t.hi, t.acc, t.left.acc, t.right.acc);
			testWellMaintained(t.left);
			testWellMaintained(t.right);
		}
	}

	static Fenwick tGet(Fenwick t,int i) {
		return t.lo <= i && i < t.hi ? (t.left == null ? t : (tGet(t.left,i) == null) ? tGet(t.right,i) : tGet(t.left,i)) : null;
	}
	
}
