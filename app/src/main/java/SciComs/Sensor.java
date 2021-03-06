package SciComs;

/**
 * Represents a ScienceWorkflow sensor.
 *
 * Contains fields for the name of the sensor, the id of the sensor, and for the data return type of the sensor.
 *
 * Provides a mechanism for maintaining a sample history for a sensor.
 *
 * @author Aaron Pabst
 *
 */
public class Sensor {
    private String name; // The name of the sensor
    private int id; // The id of the sensor
    private SampleType sensorType; // The data type of the sensor
    private RingBuffer<SensorSample> history;

    /**
     * Creates a Sensor from pre-parsed arguments
     *
     * @param name The name of the sensor
     * @param id The id number of the sensor
     * @param sensorType The return type of the sensor
     * @param maxSamples The maximum number of samples to record
     */
    public Sensor(String name, int id, SampleType sensorType, int maxSamples){
        this.name = name;
        this.id = id;
        this.sensorType = sensorType;
        history = new RingBuffer<SensorSample>(maxSamples);
    }

    /**
     * Creates a Sensor from a raw sensor listing as defined in the ControlAPI.
     *
     * @param senseListing The raw sensor listing
     */
    public Sensor(String senseListing, int maxSamples){
        //System.out.println(senseListing);
        String[] args = senseListing.split(" ");

        String datType = "";
        for(int i = 0; i < args.length-2; i++){
            datType = datType.concat(args[i+2]);
        }

        this.name = args[0];
        this.id = Integer.parseInt(args[1]);
        this.sensorType = new SampleType(datType);
        history = new RingBuffer<SensorSample>(maxSamples);
    }

    /**
     * @return The name of this sensor
     */
    public String getName() { return name; }

    /**
     * @return The id of this sensor
     */
    public int getId() { return id; }

    /**
     * @return The data type of this sensor
     */
    public SampleType getDataType() { return sensorType; }

    /**
     * Adds a sample represented as a string to this sensor's sample history.
     *
     * @param sample A textual sensor sample
     */
    public void addSample(String sample){
        history.add(new SensorSample(sensorType, sample));
    }

    /**
     * Returns all available sample data
     *
     * @return An iteration over the sample history
     */
    public Iterable<SensorSample> getHistory(){
        return history.getData();
    }

    /**
     * Fetches the most recent sample in the history
     *
     * @return The most recent SensorSample
     */
    public SensorSample getMostRecent(){
        return history.getNewest();
    }

    @Override
    public String toString(){
        return name + " " + Integer.toString(id) + " " + sensorType.toString();
    }
}