package SciComs;
import android.view.MotionEvent;

/*import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;*/
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//import javax.swing.Timer;

import edu.utah.chpc.crystal.sciEmerRobo.MovementDial;
import edu.utah.chpc.crystal.sciEmerRobo.Robotic_Sen_Cont;
import edu.utah.chpc.crystal.sciEmerRobo.UDPHandler;
import edu.utah.chpc.crystal.sciEmerRobo.UDPResponseHandler;

/**
 * Abstracts away all tasks related to controlling and communicating with the SciWorkflow robot.
 *
 * Provides methods for sending movement commands to a remote ControlServer instance.
 *
 * Automatically retrieves and maintains sensor information.
 *
 * @author Aaron Pabst Revision by Crystal Young
 * @version 0.2
 * Model
 */
public class Robot {
    private UDPHandler handler;

    private ArrayList<Sensor> sensors;
    private Lock sensorLock;

    private Timer updateTimer;

    private int sensorIndex; // Uses by the sense get receive handler to index into the sensor collection

    private int histSize;

    private int rcvPending;
    private int updateInterval;


    OnTouchListener _touchListener = null;





    /**
     * Initilizes a connection to the science workflow robot at addr:portno
     *
     * @param addr - The ip address of the robot
     * @param portno - The port number of the control service
     * @param updateInterval - The amount of time to wait between sensor updates
     * @param histSize - The number of sensor samples to maintain
     */
    public Robot(String addr, int portno, int updateInterval, int histSize){
        handler = new UDPHandler(addr, portno);
        sensors = new ArrayList<Sensor>();
        sensorLock = new ReentrantLock();
        this.histSize = histSize;
        rcvPending = 0;
        this.updateInterval = updateInterval;
        //this._touchListener.onTouch() = Robotic_Sen_Cont.onTouchEvent();

        senseList(); // Populate sensor collection

        sensorIndex = 0;

    }
    public interface OnTouchListener {
        void onTouch();
    }

    public void setOnTouchListener(OnTouchListener listener) {
        _touchListener = listener;
    }


    public void onTouch() {
            updateTimer = new Timer(true); // Set up an event to update the sensor history at a set time interval
            updateTimer.scheduleAtFixedRate(new ServerTask(), 1, updateInterval);
    }


    /**
     * Sends a drive command to the robot with parameters theta and speed
     *
     * @param theta The direction to insert into the command
     * @param speed The speed to insert into the command
     */
    public void drive(int theta, int speed){
        handler.send("DRIVE " + Integer.toString(theta) + " " + Integer.toString(speed));
    }

    /**
     * Sends a pan command to the robot with parameters x any y
     *
     * @param x The horizontal position to move the camera to
     * @param y The vertical position to move the camera to
     */
    public void pan(int x, int y){
        handler.send("PAN + " + Integer.toString(x) + " " + Integer.toString(y));
    }

    private void senseList(){
        rcvPending++;
        handler.sendReceive("SENSE_LIST", new ReceiveSensorList());
    }

    private void senseGet(Sensor sensor){
//		System.out.println("get");
        rcvPending++;
        handler.sendReceive("SENSE_GET " + sensor.getId(), new ReceiveSensorSample());
    }

    /**
     * Calls senseGet on all known sensors
     */
    private void updateAll(){
//		System.out.println(sensors.size());
        for(Sensor s : sensors)
            senseGet(s);
    }

    /**
     * @return True if there is a pending response handler
     */
    public boolean rcvPending(){
        return rcvPending != 0;
    }

    /**
     * Produces a copy of the sensor list and current history as it exists when this method
     * is called. This copy is not updated after it's returned to the user and must be called
     * again to obtain an up to date copy.
     *
     * @return A sensor array containing all the sensors as they exist at this moment in time
     */
    public Sensor[] getSensorSnapshot(){
        sensorLock.lock();
        Sensor[] copy = new Sensor[sensors.size()];
        sensors.toArray(copy);
        sensorLock.unlock();
        return copy;
    }

    /**
     * Produces a map of sensor names to sensor ids
     *
     * @return A HashMap mapping names to id numbers
     */
    public HashMap<String, Integer> getIdMapping(){
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        sensorLock.lock();
        for(Sensor s : sensors){
            map.put(s.getName(), s.getId());
        }
        sensorLock.unlock();
        return map;
    }

    public SensorSample getMostRecent(int id){
        sensorLock.lock();
        SensorSample sample = sensors.get(id).getMostRecent(); // TODO - EHHHHH.....
        sensorLock.unlock();
        return sample; // XXX I don't think this is thread safe........
    }

    public void kill(){
        updateTimer.cancel();
        updateTimer.purge();
        handler.kill();
    }

    /**
     * Call back for when a sense_list response comes in
     */
    private class ReceiveSensorList implements UDPResponseHandler{
        @Override
        public void handler(String response) {
            sensorLock.lock();
            sensors.clear();
            for(String s : response.split("\n")){
                if(s.split(" ").length > 1 && s.split(" ").length < 10) // Sometimes we can pick up malformed ASCII, so we need to make sure the line is good
                    sensors.add(new Sensor(s, histSize));
            }
            sensorLock.unlock();
            rcvPending--;
        }
    }



//	private void dumpBytes(String s){
//		byte[] arr = s.getBytes();
//		for(int i = 0; i < arr.length; i++){
//			System.out.print(arr[i] + ", ");
//		}
//		System.out.println("");
//	}

    /**
     * Callback for when a sense_get response comes in
     */
    private class ReceiveSensorSample implements UDPResponseHandler{

        @Override
        public void handler(String response) {
//			dumpBytes(response);
//			System.out.println(response);
            sensorLock.lock();
            sensors.get(sensorIndex).addSample(response);
            if(sensorIndex < sensors.size()-1)
                sensorIndex++;
            else
                sensorIndex = 0;
            sensorLock.unlock();
            rcvPending--;
        }

    }
    private class ServerTask extends TimerTask {
        public void run() {
            // Iterate over sensor list and call sense get for each one
            updateAll();
        }
    }
}
