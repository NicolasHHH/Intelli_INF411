package reporters;
import java.util.HashMap;

import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;

public class SharkReporter extends TestReporter {
	private Integer sharks;
	
	public SharkReporter(String name, HashMap<String, Object> data, boolean print) {
		super(name, data, print);
	}

	public SharkReporter() {
		super();
	}

	@Override
	public void initialise(Ocean ocean) {
		super.initialise(ocean);
		sharks = 0;
	}
	
	@Override
	public void report(Coordinate current, Mark old) {
		if (current == null)
			return;
		
		Mark mark = ocean.getMark(current);
		
		if (mark == null || !mark.equals(Traversal.path))
			return; 
		
		super.report(current, old);

		if (ocean.isThereASharkAt(current))
			sharks++;		
	}
	
	@Override
	public void finish() {
		putData(sharks);
		sb.append(sharks + " requin(s) rencontré(s)");
		super.finish();
	}
	
	@Override
	public Integer getData(String oceanName) {
		Object data = super.getData(oceanName);
		return (data instanceof Integer) ? (Integer) data : null;
	}	
}
