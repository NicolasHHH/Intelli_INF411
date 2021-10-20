public class Test2 {
	private static void assertPerfect(Maze m) {
		assert(m.isPerfect()) : "maze is not perfect";
	}

	public static void main(String[] args) {
		//Pour s'assurer que les assert's sont activés
		if (!Test2.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
		        System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.print("testing generateRec() for a 5x5 maze... ");
		Maze m = new Maze(5, 5);
		m.getFirstCell().generateRec();
		assertPerfect(m);
		System.out.println("\t\t[OK]");

		System.out.print("testing generateRec() with more mazes of size 5x5... ");
		for(int k = 0; k < 10; ++k) {
			m = new Maze(5, 5, false);
			m.getFirstCell().generateRec();
			assertPerfect(m);
		}
		System.out.println("\t[OK]");

		System.out.print("testing generateRec() for a 25x25 maze... ");
		m = new Maze(25, 25);
		m.getFirstCell().generateRec();
		assertPerfect(m);
		Maze m1 = new Maze(25, 25, false);
		m1.getFirstCell().generateRec();
		Maze m2 = new Maze(25, 25, false);
		m2.getFirstCell().generateRec();
		assert(!m1.equals(m2)) : "La génération de labyrinthe n'est pas aléatoire.";
		System.out.println("\t\t[OK]");

	}
}
