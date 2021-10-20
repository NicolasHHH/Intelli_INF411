package reporters;
import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;

public class NoMarkOverwrites extends TestReporter {

	@Override
	public void report(Coordinate current, Mark old) {
		super.report(current, old);
		
		if (current == null || old == null || Ocean.noMark.equals(old))
			return;

		Mark curMark = ocean.getMark(current);
		if (curMark.equals(old))
			throw new Error("\nAuriez-vous oublié de faire le test isMarked() ?"
					+ " (Vous avez essayé de marquer la cellule " + current + " avec " + old
					+ ", alors que cette marque y est déjà posée.)");
		else if (!curMark.equals(Traversal.deadEnd) && !curMark.equals(Traversal.path))
			throw new Error("\nLa marque dans la cellule " + current + " est ecrasée par une marque differente de "
					+ Traversal.path + " et " + Traversal.deadEnd);
	}
}
