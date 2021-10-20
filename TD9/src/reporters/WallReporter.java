package reporters;
import ocean.Coordinate;
import ocean.Mark;

public class WallReporter extends TestReporter {
	@Override
	public void report(Coordinate current, Mark old) {
		super.report(current, old);
		
		if (current == null)
			return;

		if (ocean.isWall(current))
			throw new Error("\nAuriez-vous oublié de faire le test isWall() ?"
					+ " (Vous avez essayé de marquer la cellule " + current + ", qui est une falaise, "
						+ "avec la marque " + ocean.getMark(current) + ".)");
	}
}
