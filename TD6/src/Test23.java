import java.util.Random;

public class Test23 {
	public static void main(String[] args) {

		if (!Test23.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}
		System.out.print("Test de inc... \n");
		testInc(16, 0);
		testInc(31, 0);
		testInc(32, 0);
		testInc(33, 0);
		testInc(145, 12);
		testInc(1000, 0);
		testInc(10000, 0);
		testInc(100000, 0);
		System.out.println("\t\t\t [OK].");
	}
	
	static void testInc(int size, int offset) {
		Fenwick t = new Fenwick(offset, offset + size);

		testWellFormed(t);

		long startTime = System.currentTimeMillis();

		int[] acc = new int[size];
		Random r = new Random();
		int nb = 100000;

		for (int i = 0; i < nb; i++) {
			int s = r.nextInt(size);
			acc[s]++;
			t.inc(offset + s);
		}

		long endTime = System.currentTimeMillis();
		// System.out.println(String.format("%d appels à 'inc' dans [%d, %d[ en %d
		// millisecondes (%d appels/ms).", nb,
		// t.lo, t.hi, endTime - startTime, (nb) / (endTime - startTime)));

		if (endTime > startTime + 80)
			System.out.println("L'implémentation de 'inc' est lente.");

		assert endTime < startTime + 400 : "L'implémentation de inc n'a probablement pas une complexité logarithmique.";

		testWellFormed(t);
		testWellMaintained(t);

		for (int i = 0; i < size; i++)
			assert acc[i] == tGet(t,offset + i).acc : "La valeur de l'accumulateur aux feuilles est incorrecte.";
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

		assert t.left != null && t.right != null : String.format("Le nœud interne [%d, %d[ n'a qu'un seul fils.", t.lo, t.hi);
		assert t.left.lo == t.lo && t.right.hi == t.hi && t.left.hi == t.right.lo : String.format(
				"Le nœud [%d, %d[ est mal recouvert par ses enfants [%d, %d[ et [%d, %d[.", t.lo, t.hi, t.left.lo,
				t.right.hi, t.right.lo, t.right.hi);

		testWellFormed(t.left);
		testWellFormed(t.right);
	}

	static void testWellMaintained(Fenwick t) {
		if (t.left != null) {
			assert t.acc == t.left.acc + t.right.acc : String.format(
					"L'accumulateur de l'intervalle [%d,%d[ n'est pas égal\n"
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
