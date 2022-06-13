package com.mineaurion.api.query.lib;

import java.net.*;
import java.util.Random;

/**
 * A class that handles Minecraft Query protocol requests
 *
 * @author Ryan McCann
 */
public class MCQuery {
    public final static byte HANDSHAKE = 9;
    public final static byte STAT = 0;

    private final String address;
    private final int queryPort; // the default minecraft query port

    public MCQuery(String address) {
        this(address, 25565);
    }

    public MCQuery(String address, int queryPort) {
        this.address = address;
        this.queryPort = queryPort;
    }

    /**
     * Use this to get more information, including players, from the server.
     *
     * @return a <code>QueryResponse</code> object
     */
    public QueryResponse fullStat() throws MCQueryException {
        QueryRequest req = new QueryRequest();
        req.type = HANDSHAKE;
        req.sessionID = new Random().nextInt(5);

        int val = 11 - req.toBytes().length; //should be 11 bytes total
        byte[] input = ByteUtils.padArrayEnd(req.toBytes(), val);

        try {
            int token = Integer.parseInt(new String(sendUDP(input)).trim());
            req = new QueryRequest();
            req.type = STAT;
            req.sessionID = new Random().nextInt(5);
            req.setPayload(token);
            req.payload = ByteUtils.padArrayEnd(req.payload, 4); //for full stat, pad the payload with 4 null bytes
            return new QueryResponse(sendUDP(req.toBytes()));
        } catch (NumberFormatException e) {
            throw new MCQueryException("Is the server offline?", e);
        }
    }

    private byte[] sendUDP(byte[] input) throws MCQueryException {
        DatagramSocket socket = null; //prevent socket already bound exception
        try {
            int localPort = 25566; // the local port we're connected to the server on
            while (socket == null) {
                try {
                    socket = new DatagramSocket(localPort); //create the socket
                } catch (BindException e) {
                    ++localPort; // increment if port is already in use
                }
            }
            //create a packet from the input data and send it on the socket
            InetAddress address = InetAddress.getByName(this.address); //create InetAddress object from the address
            DatagramPacket packet1 = new DatagramPacket(input, input.length, address, queryPort);
            socket.send(packet1);

            //receive a response in a new packet
            byte[] out = new byte[1024];
            DatagramPacket packet = new DatagramPacket(out, out.length);
            socket.setSoTimeout(500); //one half second timeout
            socket.receive(packet);
            socket.close();

            return packet.getData();
        } catch (SocketTimeoutException e) {
            throw new MCQueryException("Socket Timeout! Is the server offline?", e);
        } catch (UnknownHostException e) {
            throw new MCQueryException("Unknown host!", e);
        } catch (Exception e) {
            throw new MCQueryException("Exception when contacting the server", e);
        }
    }
}
