package reporters;


import java.util.HashMap;

import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;

public class CountMarks extends TestReporter {
	private HashMap<Mark, Integer> markCounters;

	public CountMarks(String name, HashMap<String, Object> data, boolean print) {
		super(name, data, print);
	}

	@Override
	public void initialise(Ocean ocean) {
		super.initialise(ocean);
		this.markCounters = new HashMap<> ();
		putData(markCounters);
	}
	
	@Override
	public void report(Coordinate current, Mark old) {
		super.report(current, old);
		
		if (current == null)
			return;
		
		Mark mark = ocean.getMark(current);
		if (Ocean.noMark.equals(mark))
			return;
		
		Integer counter = markCounters.get(mark);
		
		if (counter == null)
			markCounters.put(mark, 1);
		else
			markCounters.replace(mark, counter + 1);
	}

	@Override
	public void finish() {
		sb.append("Marques utilisées : (");
		
		for (Mark mark : markCounters.keySet())
			sb.append(mark + ": " + markCounters.get(mark) + ", ");
		
		if (markCounters.keySet().size() > 0)
			sb.delete(sb.length() - 2, sb.length());
		
		sb.append(")");
		super.finish();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public HashMap<Mark, Integer> getData(String oceanName) {
		Object obj = super.getData(oceanName);		
		return (obj instanceof HashMap<?,?>) ? (HashMap<Mark, Integer>) obj : null;
	}
}
