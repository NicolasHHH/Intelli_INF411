
public class Test33 {
	public static void main(String[] args) {

		if (!Test33.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.print("Test de countInversionsBest... ");

		testBestCorrect(new int[] {}, 0);
		testBestCorrect(new int[] { 42 }, 0);
		testBestCorrect(new int[] { 8, 4, 2, 1 }, 6);
		testBestCorrect(new int[] { 3, 1, 2 }, 2);
		testBestCorrect(new int[] { -3, -1, -2 }, 1);
		testBestCorrect(new int[] { 8, 4, 2, 4 }, 4);
		for (int n = 1; n <= 10000; n *= 2) {
			int[] a = new int[n];
			for (int i = 0; i < n; i++)
				a[i] = -n + (int) (2 * n * Math.random());
			testBestCorrect(a);
		}
		testBestRapide(1000);
		testBestRapide(10000);
		System.out.println("\t\t[OK]");
	}

	static void testBestCorrect(int[] a, int res) {
		int r = CountInversions.countInversionsBest(a);
		String s = "[";
		for (int i = 0; i < a.length; i++)
			s += a[i] + " ";
		s += ']';
		assert r == res : "\n Le nombre d'inversions de " + s + " est égal à " + res + ", vous obtenez " + r;
	}

	static void testBestCorrect(int[] a) {
		int r = CountInversions.countInversionsBest(a);
		int res = CountInversions.countInversionsNaive(a);
		String s = "[";
		for (int i = 0; i < a.length; i++)
			s += a[i] + " ";
		s += ']';
		assert r == res : "\n Pour le tableau " + s + ", vous obtenez :\n " + res
				+ " inversions avec  la méthode `countInversionNaive` \n" + " et " + r
				+ " inversions avec  la méthode `countInversionBest`.";
	}

	static void testBestRapide(int size) {
		long startTime = System.currentTimeMillis();
		for (int n = 0; n < 10; n++) {
			int[] a = new int[size];
			for (int i = 0; i < n; i++) {
				a[i] = -size + (int) (2 * size*size * Math.random());
			}
			CountInversions.countInversionsBest(a);
		}
		long endTime = System.currentTimeMillis();
		
//		System.out.println("Temps pour taille "+size+" mis par Best "+ (endTime-startTime));

//		startTime = System.currentTimeMillis();
//		for (int n = 0; n < 10; n++) {
//			int[] a = new int[size];
//			for (int i = 0; i < n; i++) {
//				a[i] = -size + (int) (2 * size*size * Math.random());
//			}
//			CountInversions.countInversionsFen(a);
//		}
//		endTime = System.currentTimeMillis();
//		System.out.println("Temps pour taille "+size+" mis par Fen "+ (endTime-startTime));


		if (endTime > startTime + 4*(size/1000))
			System.out.println("L'implémentation de 'countInversionsBest' est lente.");

		assert endTime < startTime
				+ (size/100) : "\nL'implémentation de 'countInversionsBest' n'a probablement pas une complexité en O(N log(N)).";
	}

}
