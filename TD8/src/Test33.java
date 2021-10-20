public class Test33 {

	private static void test(Double[] data, double min, double max) {
		Singly<Double> chain = null;
		if (data.length != 0)
			chain = new Singly<Double>(data);
		Singly<Double> chainbis = Singly.copy(chain);
		Pair<Double> m = Median.median(chainbis);
		assert (m.first.equals(min) && m.second.equals(max)) : "\n"
				+ "La médiane de l'echantillon\n"
				+ chain
				+"\nse trouve dans l'intervalle ["
				+ min
				+ ","
				+ max
				+ "] alors que votre programme situe la médiane dans l'intervalle ["
				+ m.first + "," + m.second + "]";
	}

	public static void main(String[] args) {
		//Pour s'assurer que les assert's sont actives
		if (!Test33.class.desiredAssertionStatus()) {
	        System.err.println("Vous devez activer globalement l'option -ea de la JVM");
	        System.err.println("Voir la rubirque \"Activer Assert\" au début du sujet");
	        System.exit(1);
	      }
		System.out.print("Testing median: ");
		test(new Double[0], Double.NaN, Double.NaN);
		test(new Double[] { 1. }, 1., 1.);
		test(new Double[] { 2., 1. }, 1., 2.);
		test(new Double[] { 3., 3. }, 3., 3.);
		test(new Double[] { 1., 4., 3., 2. }, 2., 3.);
		test(new Double[] { 5., 2., 4., 1., 3. }, 3., 3.);
		test(new Double[] { 1.3, 0.7, 0.3, 2.1, 0.7 }, 0.7, 0.7);
		System.out.println("[OK]");

	}

}
