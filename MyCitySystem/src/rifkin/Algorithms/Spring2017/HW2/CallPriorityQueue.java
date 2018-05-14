package rifkin.Algorithms.Spring2017.HW2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class CallPriorityQueue<Call> extends PriorityQueue<Call> {
	
	public CallPriorityQueue(Comparator<Call> myComp) {
		super(myComp);
	}
	
	public CallPriorityQueue(CallPriorityQueue<Call> callPriorityQueue) {
		super(callPriorityQueue);
	}

	public ArrayList<Call> peek(int size) {
		ArrayList<Call> topK = new ArrayList<Call>();
		Queue<Call> copy = new CallPriorityQueue<Call>(this);
		for(int i = 0; i < size; i++) {
			topK.add(copy.poll());
		}
		
		return topK;
	}
	
}
