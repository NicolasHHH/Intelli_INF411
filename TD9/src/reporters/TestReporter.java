package reporters;


import java.util.HashMap;

import ocean.Coordinate;
import ocean.Mark;
import ocean.Ocean;

public abstract class TestReporter implements OceanReporter {
	protected final String name;
	protected final HashMap<String, Object> data;
	protected StringBuilder sb;

	protected boolean print; 
	protected int counter;
	protected Ocean ocean;

	public TestReporter(String name, HashMap<String, Object> data, boolean print) {
		this.name = name;
		this.data = data;
		this.print = print;
//		this.sb = new StringBuilder();
	}

	public TestReporter() {
		this (null, null, true);
	}
	
	@Override 
	public void initialise(Ocean ocean) {
		// stocke la référence de l'océan pour utilisation ultérieure
		this.ocean = ocean;
		counter = 0;
		sb = new StringBuilder();
	}
	
	@Override
	public void report(Coordinate current, Mark old) {
		if (current != null)
			counter++;
	}
	
	@Override
	public void finish() {
		if (print && sb.length() > 0) 
			System.out.println(sb);		
	}
	

	@Override
	public boolean notifySuspension() {
		return true;
	}
	
	@Override
	public void cancelSuspension() { }

	@Override
	public boolean notifyActivation() {
		return true;
	}

	@Override
	public void cancelActivation() { }
	
	protected Object putData(Object value) {
		if (data == null)
			return null;
		
		return data.put(ocean.name + ":" + name, value);
	}

	protected Object replaceData(Object value) {
		return (data != null) ? data.replace(ocean.name + ":" + name, value) : null;
	}
	
	public Object getData(String oceanName) {
		return data.get(oceanName + ":" + name);
	}
	
	public int getCount() {
		return counter;
	}
	
	@Override
	public String toString() {
		return sb.toString();
	}
}
