import java.io.IOException;

public class Test1 {
	public static void main(String[] args) throws IOException {
		//Pour s'assurer que les assert's sont activés
		if (!Test1.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
		        System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
		}

		System.out.print("testing searchPath() with 25x25 maze... ");

		Maze m25 = new Maze("maze25.txt");
		m25.getFirstCell().searchPath();

		Maze m25sol = new Maze("maze25sol.txt", false);
		assert(m25.equals(m25sol)) : "not the correct solution";

		System.out.println("\t\t[OK]");
	}
}
