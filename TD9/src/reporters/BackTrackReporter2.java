package reporters;
import java.util.HashMap;

import ocean.Coordinate;
import ocean.Mark;
import ocean.Traversal;

public class BackTrackReporter2 extends TestReporter {
	private Coordinate last;
	
	public BackTrackReporter2(String name, HashMap<String, Object> data, boolean print) {
		super(name, data, print);
	}

	public BackTrackReporter2() {
		super();
	}

	@Override
	public void report(Coordinate current, Mark old) {
		if (current == null)
			return;
		
		last = new Coordinate(current);
		Mark mark = ocean.getMark(current);
		
		if (mark == null || !mark.equals(Traversal.path))
			return; 
		
		super.report(current, old);
		
		sb.append(current);
	}
	
	@Override
	public void finish() {
		putData(last);
		if (print)
			System.out.println("Chemin de retour :");
		super.finish();
	}
	
	@Override
	public Coordinate getData(String oceanName) {
		Object coord = super.getData(oceanName);
		return (coord instanceof Coordinate) ? (Coordinate) coord : null;
	}
}
