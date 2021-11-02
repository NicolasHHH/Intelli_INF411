
public class Test33 {

	public static void main(String[] args) {

		// vérifie que les asserts sont activés
		if (!Test33.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
	    }

		System.out.print("Test de la méthode simulation33() ... ");
		int size = NetworkSimulation.simulation33(524287, 1000000);
		assert(NetworkSimulation.network.nbCalls == 1840581) : "\nLe nombre total d'appels devrait être 1840581 (vous en avez enregistré " + NetworkSimulation.network.nbCalls + ").";
		assert(size == 972133) : "\nLa taille de la classe du président devrait être 972133 (vous en avez enregistré " + size + ").";
		System.out.println("[OK]");
	}
}
