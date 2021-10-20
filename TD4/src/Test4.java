import java.io.IOException;

public class Test4 {
	private static void assertPerfect(Maze m) {
		assert(m.isPerfect()) : "maze is not perfect";
	}

	public static void main(String[] args) throws IOException {
		//Pour s'assurer que les assert's sont activés
		if (!Test4.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
		        System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.print("testing whether generateWilson() generates perfect mazes... ");
		Maze m = new Maze(25, 25);
		m.generateWilson();
		assertPerfect(m);
		for(int k = 0; k < 10; ++k) {
			m = new Maze(5, 5, false);
			m.generateWilson();
			assertPerfect(m);
		}
		System.out.println("\t[OK]");

		System.out.print("testing uniformity of generateWilson()... ");
		Maze m3 = new Maze("maze3.txt", false);
		int cnt = 0;

		// Chernoff bound parameters
		final int nbMazes = 192;
		final int K = 1000000;
		final double mu = K / (double) nbMazes;
		final double eps = 0.01;
		final double delta = Math.sqrt( 3*Math.log(2/eps)/mu );

		for(int k = 0; k < K; ++k) {
			m = new Maze(3, 3, false);
			m.generateWilson();
			m.clearMarks();
			if(m.equals(m3))
				++cnt;
		}

		assert( cnt > (1-delta)*mu && cnt < (1+delta)*mu ) : "not uniform";
		System.out.println("\t\t\t[OK]");
	}
}
