package SciComs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents the data type of a sensor sample.
 *
 * @author Aaron Pabst
 */
public class SampleType {
    private String[] types;

    /**
     * Creates a new SampleType object from a valid sensor type listing as defined in the ControlAPI
     *
     * @param datType
     */
    public SampleType(String datType) {
        types = datType.split(" ");
    }

    /**
     * Converts a sample represented as a string into an appropriately typed Object.
     *
     * The converted data may be a scalar or vector value, depending on the data type
     *
     * @param val
     * @return The numerical data encoded in an instance of Object
     */
    public Object parse(String val){
        if(types.length > 1){
            String[] vals = val.split(" ");
            Collection<Object> coll = new ArrayList<Object>();
            for(int i = 0; i < types.length; i++){
                coll.add(new SampleType(types[i]).parse(vals[i]));
            }
            return coll;
        }
        else{
            switch(types[0]){
                case "ushort": return Integer.parseInt(val);
                case "short": return Short.parseShort(val);
                case "uint": return Long.parseLong(val);
                case "ulong": return new BigInteger(val);
                case "long": return Long.parseLong(val);
                case "float": return Float.parseFloat(val);
                case "double": return Double.parseDouble(val);
                case "int": return Integer.parseInt(val);
            }
        }

        return null;
    }

    public String toString(){
        String str = "";
        for(int i = 0; i < types.length; i++){
            if(i != types.length)
                str = str.concat(types[i] + " ");
            else
                str = str.concat(types[i]);
        }
        return str;
    }
}