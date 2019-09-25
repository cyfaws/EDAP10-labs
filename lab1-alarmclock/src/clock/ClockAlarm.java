package clock;
import java.util.concurrent.Semaphore;

public class ClockAlarm  implements Runnable{
	private ClockData data;
    private ClockOutput out;
    Semaphore alarmSemaphore;
    
	public ClockAlarm(ClockOutput out, ClockData data, Semaphore alarmSemaphore) {
        this.data = data;
        this.out = out;
        this.alarmSemaphore = alarmSemaphore;
    }
    
    public void run() {
		while(true){
			try {
				alarmSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("catch");
            
            while(true){
            	System.out.println("tryrel");
            	if(!data.getGiveAlarm()){
            		System.out.println("rel");
            		alarmSemaphore.release();
                    break;
            	}
            	out.alarm();
            	try {
                    Thread.sleep(500);

      	          } catch (InterruptedException e) {
      	              e.printStackTrace();
      	          }
            }
            
		}
	}
}


           
