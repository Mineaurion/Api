package com.mineaurion.api.query.lib;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lib from GitHub, couple modification to add some getter. See the link below
 * @see <a href="https://github.com/ryan-shaw/MCJQuery">MCJQuery</a>
 */
public class MCQuery {
    /**
     * The target address and port
     */
    private final InetSocketAddress address;
    private final InetSocketAddress queryAddress;
    /**
     * <code>null</code> if no successful request has been sent, otherwise a Map
     * containing any metadata received except the player list
     */
    private final Map<String, String>	values = new HashMap<>();
    /**
     * <code>null</code> if no successful request has been sent, otherwise an
     * array containing all online player usernames
     */
    private String[] playerList = new String[0];

    private boolean status = false;

    public MCQuery(String host, int port) {
        this(new InetSocketAddress(host, port), new InetSocketAddress(host, port));
    }

    /**
     * Create a new instance of this class
     * @param address The servers IP-address
     */
    public MCQuery(InetSocketAddress queryAddress, InetSocketAddress address) {
        this.address = address;
        this.queryAddress = queryAddress;
    }

    /**
     * Get the additional values if the Query has been sent
     *
     * @return The data
     */
    public Map<String, String> getValues() {
        return values;
    }

    /**
     * Get the online usernames if the Query has been sent
     *
     * @return The username array
     */
    public String[] getPlayerList() {
        return playerList;
    }

    public Integer getOnlinePlayers(){
        return Integer.parseInt(getValues().getOrDefault("numplayers", "0"));
    }

    public Integer getMaxPlayers(){
        return Integer.parseInt(getValues().getOrDefault("maxplayers", "0"));
    }

    public boolean getStatus(){
        return status;
    }

    /**
     * Request the UDP query
     * @throws IOException if anything goes wrong during the request
     */
    public void sendQueryRequest() throws IOException {
        InetSocketAddress local = queryAddress;
        if(queryAddress.getPort() == 0){
            local = new InetSocketAddress(queryAddress.getAddress(), address.getPort());
        }

        try (DatagramSocket socket = new DatagramSocket()) {
            final byte[] receiveData = new byte[10240];
            socket.setSoTimeout(2000);
            sendPacket(socket, local, 0xFE, 0xFD, 0x09, 0x01, 0x01, 0x01, 0x01);
            final int challengeInteger;
            {
                receivePacket(socket, receiveData);
                byte byte1 = -1;
                int i = 0;
                byte[] buffer = new byte[11];
                for (int count = 5; (byte1 = receiveData[count++]) != 0; )
                    buffer[i++] = byte1;
                challengeInteger = Integer.parseInt(new String(buffer).trim());
            }
            sendPacket(socket, local, 0xFE, 0xFD, 0x00, 0x01, 0x01, 0x01, 0x01, challengeInteger >> 24, challengeInteger >> 16, challengeInteger >> 8, challengeInteger, 0x00, 0x00, 0x00, 0x00);

            final int length = receivePacket(socket, receiveData).getLength();
            final AtomicInteger cursor = new AtomicInteger(5);
            while (cursor.get() < length) {
                final String s = readString(receiveData, cursor);
                if (s.length() == 0)
                    break;
                else {
                    final String v = readString(receiveData, cursor);
                    values.put(s, v);
                }
            }
            status = true;
            readString(receiveData, cursor);
            final Set<String> players = new HashSet<>();
            while (cursor.get() < length) {
                final String name = readString(receiveData, cursor);
                if (name.length() > 0)
                    players.add(name);
            }
            playerList = players.toArray(new String[0]);
        }
    }

    /**
     * Helper method to send a datagram packet
     *
     * @param socket The connection the packet should be sent through
     * @param targetAddress The target IP
     * @param data The byte data to be sent
     */
    private void sendPacket(DatagramSocket socket, InetSocketAddress targetAddress, byte... data) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, targetAddress.getAddress(), targetAddress.getPort());
        socket.send(sendPacket);
    }

    /**
     * Helper method to send a datagram packet
     *
     * @see MCQuery#sendPacket(DatagramSocket, InetSocketAddress, byte...)
     * @param socket The connection the packet should be sent through
     * @param targetAddress The target IP
     * @param data The byte data to be sent, will be cast to bytes
     */
    private void sendPacket(DatagramSocket socket, InetSocketAddress targetAddress, int... data) throws IOException {
        final byte[] d = new byte[data.length];
        int i = 0;
        for(int j : data)
            d[i++] = (byte)(j & 0xff);
        sendPacket(socket, targetAddress, d);
    }

    /**
     * Receive a packet from the given socket
     *
     * @param socket the socket
     * @param buffer the buffer for the information to be written into
     * @return the entire packet
     */
    private DatagramPacket receivePacket(DatagramSocket socket, byte[] buffer) throws IOException {
        final DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        socket.receive(dp);
        return dp;
    }

    /**
     * Read a String until 0x00
     *
     * @param array The byte array
     * @param cursor The mutable cursor (will be increased)
     * @return The string
     */
    private String readString(byte[] array, AtomicInteger cursor) {
        final int startPosition = cursor.incrementAndGet();
        for(; cursor.get() < array.length && array[cursor.get()] != 0; cursor.incrementAndGet());
        return new String(Arrays.copyOfRange(array, startPosition, cursor.get()));
    }
}
