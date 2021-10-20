package reporters;


import java.util.HashMap;

import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;

public class StackReporter extends TestReporter {

	private Integer maxStack;
	private Integer noBackTrack;
	private Integer threshold;

	public StackReporter(String name, HashMap<String, Object> data, Integer threshold, boolean print) {
		super (name, data, print);
		this.threshold = threshold;
	}

	@Override
	public void initialise(Ocean ocean) {
		super.initialise(ocean);
		maxStack = 0;
		noBackTrack = 0;
	}

	@Override
	public void report(Coordinate current, Mark old) {
		super.report(current, old);
		
		try {
			throw new Exception();
		} catch (Exception e) {
			StackTraceElement[] stack = e.getStackTrace();
			
			int noBackTrack = filterBackTrack (stack);
			if (noBackTrack > this.noBackTrack)
				this.noBackTrack = noBackTrack;
			
			if (stack.length > maxStack) 
				maxStack = stack.length;
			
			if (threshold != null && stack.length > threshold)
				throw new StackOverflowError();
		}	
	}

	// On autorise la méthode BackTrackTraversal.backTrack() récursive
	// Du coup, pour detecter la récursivité dans traverse(), il faut enlever 
	// les appels à backTrack() 
	private int filterBackTrack(StackTraceElement[] stack) {
		int count = 0;
		for (StackTraceElement ste : stack) 
			if (!ste.getMethodName().equals("backTrack"))
				count++;
		return count;
	}

	@Override
	public void finish() {
		putData(noBackTrack);
		sb.append("La profondeur maximale de la pile d'appels : " + maxStack);
		super.finish();
	}
	
	@Override
	public Integer getData(String oceanName) {
		Object depth = super.getData(oceanName);
		return (depth instanceof Integer) ? (Integer) depth : null;
	}

}
