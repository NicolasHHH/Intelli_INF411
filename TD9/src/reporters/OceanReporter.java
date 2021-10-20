package reporters;
import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;

/**
 * Interface d'un rapporteur pour l'affichage de l'océan
 * 
 * <p>
 * Définit les méthodes suivantes :
 * </p>
 * <ul>
 * <li>{@link #initialise(Ocean)} &mdash; appelée avant le début de
 * l'exploration</li>
 * <li>{@link #report(Coordinate, Mark) } &mdash; appelée à chaque modification du marquage de
 * l'océan</li>
 * <li>{@link #finish()} &mdash; appelée à l'issue de l'exploration</li>
 * </ul>
 */
public interface OceanReporter {
	/**
	 * Appelée avant le début de l'exploration
	 * 
	 * @param ocean la référence de l'océan qui sera exploré
	 */
	void initialise(Ocean ocean);

	/**
	 * Appelée à chaque modification du marquage de l'océan
	 * 
	 * @param current la cellule sur laquelle la dernière modifications a été
	 *                effectuée
	 * @param old la marque qui se trouvait dans {@code current} précédemment
	 */
	void report(Coordinate current, Mark old);

	/**
	 * Appelée à l'issue de l'exploration
	 */
	void finish();

	/**
	 * Appelée lorsque quelqu'un tente de désactiver le rapporteur
	 * @return {@code true} si la désactivation est acceptée
	 */
	boolean notifySuspension();

	/**
	 * Appelée lorsque la désactivation est annulée par un des rapporteurs
	 */
	void cancelSuspension();

	/**
	 * Appelée lorsque quelqu'un tente d'activer le rapporteur
	 * @return {@code true} si l'activation est acceptée
	 */
	boolean notifyActivation();

	/**
	 * Appelée lorsque l'activation est annulée par un des rapporteurs
	 */
	void cancelActivation ();

}
