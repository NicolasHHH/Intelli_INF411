package reporters;
import java.util.HashMap;

import ocean.Coordinate;
import ocean.IntMark;
import ocean.Mark;

public class DistanceReporter extends TestReporter {
	private Integer lastMark; 

	public DistanceReporter(String name, HashMap<String, Object> data, boolean print) {
		super(name, data, print);
	}

	public DistanceReporter() {
		super();
	}

	@Override
	public void report(Coordinate current, Mark old) {
		if (current == null)
			return;
		
		Mark mark = ocean.getMark(current);
		
		if (mark == null || !(mark instanceof IntMark))
			return; 
		
		// Des marques spécifiques, e.g. Traversal.path ou Traversal.deadEnd, 
		// peuvent être des IntMark, mais alors leurs label seront probablement
		// différents de leur valeurs. E.g. Traversal.path.toString() = "path"
		if (!mark.toString().equals(mark.toInteger().toString()))
			return;
		
		ocean.disableReports();
		ocean.setMark(current, mark.toInteger() + 1);
		ocean.enableReports();

		super.report(current, old);
		
		lastMark = ocean.getMark(current).toInteger();
	}
	
	@Override
	public void finish() {
		putData(lastMark);
		super.finish();
	}
	
	@Override
	public Integer getData(String oceanName) {
		Object data = super.getData(oceanName);
		return (data instanceof Integer) ? (Integer) data : null;
	}
	
}
