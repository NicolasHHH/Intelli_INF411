
public class Test31 {

	public static void main(String[] args) {

		if (!Test31.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.println("--Test de la methode sqDist...");
		double[] a = new double[] { 0.0, 1.0 };
		double[] b = new double[] { 1.0, 0.0 };
		double r = KDTree.sqDist(a, b);
		assert (r == 2.0) : "Erreur sqDist([0.0,1.0],[1.0,0.0]) doit etre egale a 2.0, la distance obtenue est " + r;

		a[0] = -1.0;
		b[1] = -1.0;
		r = KDTree.sqDist(a, b);
		assert (r == 8.0) : "Erreur sqDist([-1.0,1.0],[1.0,-1.0]) doit etre egale a 8.0, la distance obtenue est " + r;

		a[0] = 8.0;
		a[1] = 17.0;
		b[1] = 1.0;
		r = KDTree.sqDist(a, b);
		assert (r == 305.0) : "Erreur sqDist([8.0,17.0],[1.0,1.0]) doit etre egale a 305.0, la distance obtenue est " + r;

		a[0] = .625;
		a[1] = .25;
		b[0] = .5;
		b[1] = .125;
		r = KDTree.sqDist(a, b);

		assert (r == 0.03125) : "Erreur sqDist([8.0,17.0],[1.0,1.0]) doit etre egale a 0.03125, la distance obtenue est "
				+ r;
		System.out.println("[OK]");
	}

}
