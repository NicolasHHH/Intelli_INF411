public class CheckAssertions {
	public static void check() {
		try {
			assert false;
			System.err.println("Assertions désactivées.");
			System.err.println("Vous devez activer l'option -ea de la JVM");
			System.err.println(
					"(Window → Preferences → Java → Installed JREs → java-8-openjdk → Edit... → Default VM arguments)");
			System.exit(1);
		} catch (AssertionError e) {
		}
	}
}
