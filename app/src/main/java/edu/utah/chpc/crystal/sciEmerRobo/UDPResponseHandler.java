package edu.utah.chpc.crystal.sciEmerRobo;

/**
 * Contains a procedure to run when UDP data is returned from the remote host.
 *
 * @author Aaron Pabst
 */
public interface UDPResponseHandler {
    public void handler(String response);
}
