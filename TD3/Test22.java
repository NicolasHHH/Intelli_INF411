public class Test22 {

	public static void main(String[] args) {
		
		// vérifie que les asserts sont activés
		if (!Test22.class.desiredAssertionStatus()) {
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println("(Run As -> Run configurations -> Arguments -> VM Arguments)");
			System.exit(1);
	    }

		System.out.print("Test de la méthode simulation22() ... ");
		int nbCalls = NetworkSimulation.simulation22(524287, 1000000);
		assert(nbCalls == 1840579) : "\nLe nombre total d'appels (en ignorant les appels aux répondeurs) devrait être 1840579 (vous en avez enregistré " + nbCalls + ").";
		assert(NetworkSimulation.network.caller == 910544) : "\nLe dernier numéro appelant devrait être 910544 (vous avez enregistré " + NetworkSimulation.network.caller + ").";
		assert(NetworkSimulation.network.callee == 524287) : "\nLe dernier numéro appelé devrait être 524287 (vous avez enregistré " + NetworkSimulation.network.callee + ").";
		System.out.println("[OK]");
	}
}
