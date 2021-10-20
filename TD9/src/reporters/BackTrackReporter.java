package reporters;
import java.util.HashMap;

import ocean.Coordinate;
import ocean.Direction;
import ocean.Mark;
import ocean.Ocean;
import ocean.Traversal;

public class BackTrackReporter extends TestReporter {
	private Direction currentDir;
	private int currentCount;
	private Coordinate last;
	private int limit;
	
	public BackTrackReporter(String name, HashMap<String, Object> data, boolean print) {
		super(name, data, print);
	}

	public BackTrackReporter() {
		super();
	}

	public void setLimit (int limit) {
		this.limit = limit;
	}
	
	@Override
	public void initialise(Ocean ocean) {
		super.initialise(ocean);
		currentDir = null;
		currentCount = 0;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void report(Coordinate current, Mark old) {
		if (current == null)
			return;
		
		last = new Coordinate(current);
		Mark mark = ocean.getMark(current);
		
		if (mark == null || !mark.equals(Traversal.path))
			return; 
		
		super.report(current, old);
		
		if (counter > limit)
			throw new StackOverflowError();
		
		if (old == null || !(old instanceof Direction))
			return;
		
		Direction dir = (Direction) old;
		
		if (currentDir != null && !currentDir.equals(dir)) {
			sb.append(currentDir.ordinal());
			sb.append(":");
			sb.append(currentCount);
			sb.append(",");
		}

		if (currentDir == null || !currentDir.equals(dir)) {
			currentDir = dir;
			currentCount = 1;
			return; 
		}
		
		last = last.moveTo(currentDir);
		currentCount++;		
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void finish() {
		if (currentDir != null) {
			sb.append(currentDir.ordinal());
			sb.append(":");
			sb.append(currentCount);
		}
		putData(last);
		super.finish();
	}
	
	@Override
	public Coordinate getData(String oceanName) {
		Object coord = super.getData(oceanName);
		return (coord instanceof Coordinate) ? (Coordinate) coord : null;
	}
}
