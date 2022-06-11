package com.mineaurion.api.query.lib;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class QueryRequest {
    private final ByteArrayOutputStream byteStream;
    private final DataOutputStream dataStream;
    private static final byte[] MAGIC = {(byte) 0xFE, (byte) 0xFD};
    public byte type;
    public int sessionID;
    public byte[] payload;

    public QueryRequest()
    {
        this.byteStream = new ByteArrayOutputStream(1460);
        this.dataStream = new DataOutputStream(byteStream);
    }

    //convert the data in this request to a byte array to send to the server
    byte[] toBytes()
    {
        byteStream.reset();

        try
        {
            dataStream.write(MAGIC);          // byte 0,1
            dataStream.write(type);           // byte 2
            dataStream.writeInt(sessionID);   // byte 3,4,5,6
            dataStream.write(payloadBytes()); // byte 7+
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return byteStream.toByteArray();
    }

    private byte[] payloadBytes()
    {
        return type == MCQuery.HANDSHAKE ? new byte[]{} : payload;
    }

    protected void setPayload(int load)
    {
        this.payload = ByteUtils.intToBytes(load);
    }
}
