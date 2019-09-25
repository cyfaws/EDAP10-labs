import java.util.concurrent.Semaphore;

import clock.ClockAlarm;
import clock.ClockData;
import clock.ClockInput;
import clock.ClockInput.UserInput;
import clock.ClockOutput;
import clock.ClockTimer;
import emulator.AlarmClockEmulator;

public class ClockMain {

    public static void main(String[] args) throws InterruptedException {
        AlarmClockEmulator emulator = new AlarmClockEmulator();

        ClockInput  in  = emulator.getInput();
        ClockOutput out = emulator.getOutput();
        Semaphore alarmSemaphore = new Semaphore(0);
        ClockData cData = new ClockData(0,0,alarmSemaphore);
        ClockAlarm alarm = new ClockAlarm(out, cData, alarmSemaphore);
        ClockTimer timer = new ClockTimer(out, cData);
        Thread timerThread = new Thread(timer);
        Thread alarmThread = new Thread(alarm);
        timerThread.start();
        alarmThread.start();
        
        cData.setTime(0*10000 + 0*100 + 00);// arbitrary time: just an example
        //cData.setAlarmTime(23*10000 + 46*100 + 42);
        out.displayTime(cData.getTime());   

        Semaphore sem = in.getSemaphore();

        while (true) {
            sem.acquire();                        // wait for user input
            UserInput userInput = in.getUserInput();
            int choice = userInput.getChoice();
            int value = userInput.getValue();
            
            switch(choice){
            case 1: // user set new clock time
            	cData.setTime(value);
            	break;
            case 2: // user set new alarm time
            	cData.setAlarmTime(value);
            	break;
            case 3:	// user toggled alarm
            	cData.toggleAlarm();
            	out.setAlarmIndicator(cData.getAlarmToggle());
            	break;
            case 4:	// user pressed nonsense
            	break;
            }
            cData.setAlarmOR();
            System.out.println("choice = " + choice + "  value=" + value);
        }
    }
}
