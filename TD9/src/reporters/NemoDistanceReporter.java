package reporters;
import java.util.HashMap;

import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;

/**
 * Dans l'hypothèse que la cellule de Nemo est marquée avec la distance de
 * Marin, ce rapporteur surveille l'exploration, puis, lorsque Nemo est trouvé,
 * imprime la distance dans la console.
 */
public class NemoDistanceReporter extends TestReporter {
	private Integer distance;

	public NemoDistanceReporter(String name, HashMap<String, Object> data, boolean print) {
		super(name, data, print);
	}

	public NemoDistanceReporter() {
		super();
	}
	
	@Override
	public void initialise(Ocean ocean) {
		super.initialise(ocean);
		distance = null;
	}
	
	/**
	 * Teste si la cellule courante habrite Nemo et, si c'est bien le cas, imprime
	 * la distance dans la console
	 * 
	 * @param current la cellule courante dans laquelle la marque vient d'être
	 *                changée
	 * @param old     la marque précédente dans {@code current}
	 */
	@Override
	public void report(Coordinate current, Mark old) {
		super.report(current, old);
		
		if (current != null && old != null && ocean.isNemoAt(current)) {
			if (Ocean.noMark.equals(old)) {
				distance = ocean.getMark(current).toInteger();
				putData(distance);
			} else 
				sb.append("\nLa cellule contenant Nemo est marquée deux fois !\n");
		}
	}

	@Override
	public void finish() {
		sb.append((distance != null) ? "Distance : " + distance : "Nemo n'a jamais été marqué !");
		super.finish();
	}
	
	@Override
	public Integer getData(String oceanName) {
		Object depth = super.getData(oceanName);
		return (depth instanceof Integer) ? (Integer) depth : null;
	}

}
