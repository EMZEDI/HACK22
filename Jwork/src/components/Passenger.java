package components;

public class Passenger {
	
	private int waitingTime;
	
	public Passenger() {
		waitingTime = 0;
	}
	
	public void waitOneMinute() {
		waitingTime++;
	}
	
	public int getWaitingTime() {
		return waitingTime;
	}

}
