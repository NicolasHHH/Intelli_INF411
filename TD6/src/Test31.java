
public class Test31 {

	public static void main(String[] args) {

		if (!Test31.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}
		
		System.out.print("Test de countInversionsNaive... ");
		
		testNaive(new int[] {},0);
		testNaive(new int[] { 42 },0);
		testNaive(new int[] { 8, 4, 2, 1 },6);
		testNaive(new int[] { 3, 1, 2 },2);
		testNaive(new int[] { -3, -1, -2 },1);
		testNaive(new int[] { 8, 4, 2, 4 },4);
		for (int n = 1; n <= 10000; n *= 2) {
			int[] a = new int[n];
			for (int i = 0; i < n; i++)
				a[i] = i;
			testNaive(a,0);
			for (int i = 0; i < n; i++)
				a[i] = n - i;
			testNaive(a,n * (n - 1) / 2);
			for (int i = 0; i < n; i++)
				a[i] = -n + (int) (2 * n * Math.random());
			//testNaive(a);
		}
		System.out.println("\t\t [OK].");
	}
	
	static void testNaive(int[] a, int res) {
		int r = CountInversions.countInversionsNaive(a);
		String s="[";
		for (int i = 0; i<a.length;i++)
			s+=a[i]+" ";
		s+=']';
		assert r == res:
			 "\nLe nombre d'inversions de "+s+" est égal à "+res+", vous obtenez " + r;
	}

}
