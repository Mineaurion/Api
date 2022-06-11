package com.mineaurion.api.query.lib;

import java.util.ArrayList;


public class QueryResponse {
    private final int onlinePlayers;
    private final int maxPlayers;
    private final short port;
    private final String hostname;
    private final String gameID;
    private final String version;
    private final String plugins;
    private final ArrayList<String> playerList;

    public QueryResponse(byte[] data) {
        byte[][] temp = ByteUtils.split(ByteUtils.trim(data));
        onlinePlayers = Integer.parseInt(new String(temp[15]));
        maxPlayers = Integer.parseInt(new String(temp[17]));
        port = Short.parseShort(new String(temp[19]));
        hostname = new String(temp[21]);
        gameID = new String(temp[7]);
        version = new String(temp[9]);
        plugins = new String(temp[11]);
        playerList = new ArrayList<>();
        for (int i = 25; i < temp.length; i++) {
            playerList.add(new String(temp[i]));
        }
    }

    public String toString() {
        String delimiter = ", ";
        StringBuilder str = new StringBuilder();
        str.append(onlinePlayers);
        str.append(delimiter);
        str.append(maxPlayers);
        str.append(delimiter);
        str.append(port);
        str.append(delimiter);
        str.append(hostname);
        str.append(delimiter);
        str.append(gameID);
        str.append(delimiter);
        str.append(version);
        //plugins for non-vanilla (eg. Bukkit) servers
        if (plugins.length() > 0) {
            str.append(delimiter);
            str.append(plugins);
        }
        // player list
        str.append(delimiter);
        str.append("Players: ");
        str.append('[');
        for (String player : playerList) {
            str.append(player);
            if (playerList.indexOf(player) != playerList.size() - 1) {
                str.append(',');
            }
        }
        str.append(']');

        return str.toString();
    }

    public int getOnlinePlayers() {
        return onlinePlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Returns an <code>ArrayList</code> of strings containing the connected players' usernames.
     * Note that this will return null for basic status requests.
     *
     * @return An <code>ArrayList</code> of player names
     */
    public ArrayList<String> getPlayerList() {
        return playerList;
    }
}
