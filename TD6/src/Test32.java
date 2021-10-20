
public class Test32 {
	public static void main(String[] args) {

		if (!Test32.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.print("Test de countInversionsFen...");

		testFenCorrect(new int[] {}, 0);
		testFenCorrect(new int[] { 42 }, 0);
		testFenCorrect(new int[] { 8, 4, 2, 1 }, 6);
		testFenCorrect(new int[] { 3, 1, 2 }, 2);
		testFenCorrect(new int[] { -3, -1, -2 }, 1);
		testFenCorrect(new int[] { 8, 4, 2, 4 }, 4);
		for (int n = 1; n <= 10000; n *= 2) {
			int[] a = new int[n];
			for (int i = 0; i < n; i++) 
				a[i] = -n + (int) (2 * n * Math.random());
			testFenCorrect(a);
//			System.out.println("Test pour n ="+n);
//			System.out.println("max "+max+" min "+min);
		}
		testFenRapide(10000);
		testFenRapide(100000);
		System.out.println("\t\t[OK]");
	}

	static void testFenCorrect(int[] a, int res) {
		int r = CountInversions.countInversionsFen(a);
		String s = "[";
		for (int i = 0; i < a.length; i++)
			s += a[i] + " ";
		s += ']';
		assert r == res : "\nLe nombre d'inversions de " + s + " est égal à " + res + ", vous obtenez " + r;
	}
	
	static void testFenCorrect(int[] a) {
		int r = CountInversions.countInversionsFen(a);
		int res = CountInversions.countInversionsNaive(a);
		String s = "[";
		for (int i = 0; i < a.length; i++)
			s += a[i] + " ";
		s += ']';
		assert r == res : "\nPour le tableau "+s+", vous obtenez :\n "
				+res +" inversions avec  la méthode `countInversionNaive` \n"
				+ " et "+r +" inversions avec  la méthode `countInversionFen`.";
	}

	static void testFenRapide(int size) {
		long startTime = System.currentTimeMillis();
		for (int n = 0; n < 10; n++) {
			int[] a = new int[size];
			for (int i = 0; i < n; i++) {
				a[i] = -size + (int) (2 * size * Math.random());
			}
			CountInversions.countInversionsFen(a);
		}
		long endTime = System.currentTimeMillis();
	
		if (endTime > startTime + 4*(size/1000))
			System.out.println("L'implémentation de 'countInversionsFen' est lente.");

		assert endTime < startTime
				+ (size/100) : "\nL'implémentation de 'countInversionsFen' n'a probablement pas la complexité demandée.";
	}

}
