package game;

import java.util.Queue;
import java.util.ArrayDeque;

public final class MessageLog {
	private Queue<String> messageLog = null;
	private int size;
	
	MessageLog(int size) {
		this.size = size;
		messageLog = new ArrayDeque<String>(size);
	}
	
	public void add(String msg) {
		if (messageLog.size() >= size)
			messageLog.poll();
		messageLog.add(msg);
	}
	
	
}
