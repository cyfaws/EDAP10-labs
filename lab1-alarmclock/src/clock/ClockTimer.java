package clock;
import java.util.concurrent.Semaphore;

public class ClockTimer implements Runnable{
	private ClockOutput out;
    private ClockData data;


	public ClockTimer(ClockOutput out, ClockData data) {
	    this.out = out;
	    this.data = data;
	}
	
	public void run() {
        int t0 = (int)System.currentTimeMillis();
        int tTarget = t0 + 1000;

        while(true) {
        	data.updateAlarm();
            t0 = (int)System.currentTimeMillis();

            tTarget += 1000;
            int timeToSleep = Math.max(0, tTarget - t0);
            
            try {
				Thread.sleep(timeToSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
        	data.incrementTime();
        	out.displayTime(data.getTime());
        }
    }
}