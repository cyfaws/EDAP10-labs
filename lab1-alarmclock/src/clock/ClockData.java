package clock;

import java.util.concurrent.Semaphore;

public class ClockData {
	private int 		time;
    private int 		alarmTime;
    private int 		alarmDuration = 5;
    private boolean 	alarmToggle = false;
    private boolean 	giveAlarm = false;
    private boolean 	giveAlarmOR = false;
    private Semaphore 	timeMutex;
    private Semaphore 	alarmTimeMutex;
    private Semaphore 	alarmToggleMutex;
    private Semaphore 	alarmSemaphore;
    private ClockOutput out;
    
	public ClockData(int time, int alarmTime, Semaphore alarmSemaphore) {
		this.time 				= time;
		this.alarmTime 			= alarmTime;
		this.timeMutex 			= new Semaphore(1);
		this.alarmTimeMutex 	= new Semaphore(1);
		this.alarmToggleMutex 	= new Semaphore(1);
		this.alarmSemaphore 	= alarmSemaphore;

	}

	public int getTime() {
		try {
			timeMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int returnTime = time;
		timeMutex.release();
		return returnTime;
	}
	
	public int formatTime(int time){
		int fTime = time;
		if(fTime%100>=60){
			fTime += 40;
		}
		if(fTime%10000>=6000){
			fTime += 4000;
		}
		if(fTime%1000000>=240000){
			fTime -= 240000;
		}
		return fTime;
	}
	
	public void setTime(int time)  {
		int fTime = formatTime(time);
		try {
			timeMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.time = fTime;
		timeMutex.release();
	}
	
	public void setAlarmTime(int time)  {
		int fTime = formatTime(time);
		try {
			alarmTimeMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.alarmTime = fTime;
		alarmTimeMutex.release();
	}

	public void updateAlarm(){
		if(!getAlarmToggle()){
			System.out.println(giveAlarm);
			System.out.println(giveAlarmOR);
			if(giveAlarm){
				stopAlarm();
			}
			return;
		}
		
		int currTime = getTime();
		int aTime = getAlarmTime();

		if(giveAlarm){
			if(currTime >= aTime + this.alarmDuration || giveAlarmOR){
				stopAlarm();
			}
		}else{
			if(currTime >= aTime && currTime <= aTime + this.alarmDuration){
				if(!giveAlarmOR){
					startAlarm();
				}
			}else{
				giveAlarmOR = false;
			}
		}
	}
	
	private void startAlarm(){
		giveAlarm = true;
		alarmSemaphore.release();
	}
	
	public void setAlarmOR(){
		giveAlarmOR = true;
	}
	
	private void stopAlarm(){
		giveAlarm = false;
		System.out.println("trystop");
		try {
			alarmSemaphore.acquire();
			System.out.println("stopped");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getGiveAlarm(){
		return this.giveAlarm;
	}
	
	public int getAlarmTime() {
		try {
			alarmTimeMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int returnAlarmTime = alarmTime;
		alarmTimeMutex.release();
		return returnAlarmTime;
	}

	public boolean getAlarmToggle() {
		try {
			alarmToggleMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean returnAlarmToggle = alarmToggle;
		alarmToggleMutex.release();
		return returnAlarmToggle;
	}
	
	public void toggleAlarm(){
		try {
			alarmToggleMutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		this.alarmToggle = !this.alarmToggle;

		alarmToggleMutex.release();
	}
	
	public void incrementTime() {
		int inc = 1;
		setTime(getTime()+inc);
	}
}
