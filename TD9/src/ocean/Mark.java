package ocean;
import graphics.OceanCanvas;

/**
 * L'interface devant être implémentée par une classe pour que les objets de
 * cette classe puissent être déposés comme marques dans un océan.
 * 
 * <p>
 * La représentation sous forme d'un nombre entier peut être utilisée par un
 * rapporteur sans avoir à connaître la nature de la marque. En particulier le
 * rapporteur {@link OceanCanvas} l'utilise pour calculer la couleur da la
 * marque à afficher.
 * </p>
 *
 */
public interface Mark {
	/**
	 * @return une représentation de la marque sous forme d'un nombre entier
	 */
	public Integer toInteger();
}
