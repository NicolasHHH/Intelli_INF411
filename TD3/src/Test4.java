
public class Test4 {

	public static void main(String[] args) {
		
		// vérifie que les asserts sont activés
		if (!Test4.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
	    }

		System.out.print("Test de la méthode simulation4() ... ");
		int nbCalls = NetworkSimulation.simulation4(524287, 1000000);
		assert(nbCalls == 2325629) : "\nLe nombre total d'appels (en ignorant les appels aux répondeurs) devrait être 2325629 (vous en avez enregistré " + nbCalls + ").";
		nbCalls = NetworkSimulation.simulation4(123499, 500000); // reinitialisation
                assert(nbCalls == 1169065) : "\nLe nombre total d'appels (en ignorant les appels aux répondeurs) devrait être 1169065 (vous en avez enregistré " + nbCalls + ").";
		System.out.println("[OK]");
	}
}
