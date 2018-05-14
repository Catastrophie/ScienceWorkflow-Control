package edu.utah.chpc.crystal.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by crystal on 2/1/2018.
 */

public class UDPHandler {

    String dstAddress;
    int port;
    private boolean word;


    public UDPHandler(String addr, int port) {
        dstAddress = addr;
        this.port = port;
    }

    public void send(String data) {
        //instance of class
        (new UDPThreadSend(data, port)).start();
    }

    public void sendReceive(String data, UDPDataReceiveHandler handOff) {
        (new UDPThreadSendReceive(data, handOff, port)).start();

    }


    public interface UDPDataReceiveHandler {
        public void receiveHandler(String handOff);

    } //pass a function as an class...

    // utility class
    private class UDPThreadSend extends Thread {

        int port;
        // global variable

        String message;

        public UDPThreadSend(String send, int port) {

            this.port = port;
            message = send;
        }

        public void run() {
            DatagramSocket socket;


            try {
                socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(dstAddress);

                byte[] buf;
                String s = message;
                buf = s.getBytes();

                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);

                socket.close();

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();


            }
        }
    }
    public class UDPThreadSendReceive extends Thread { //ask Aaron why it was private?

        // global vs local

        String message;
        UDPDataReceiveHandler received;
        int port;


        public UDPThreadSendReceive(String send, UDPDataReceiveHandler receive, int port) {
            this.port = port;

            message = send;
            received = receive;
        }

        public void run() {


            try {

                DatagramSocket socket = new DatagramSocket();
                InetAddress address = InetAddress.getByName(dstAddress);

                byte[] buf;
                String s = message;
                buf = s.getBytes();

                DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
                socket.send(packet);

                byte[] recvbuff = new byte[1024];
                packet = new DatagramPacket(recvbuff, recvbuff.length);

                socket.receive(packet);
                System.out.println("received");
                String data = new String(recvbuff);
                received.receiveHandler(data);
                socket.close();


            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}


