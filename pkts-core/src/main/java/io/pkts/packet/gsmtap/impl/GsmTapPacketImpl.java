/** */
package io.pkts.packet.gsmtap.impl;

import io.pkts.buffer.Buffer;
import io.pkts.packet.Packet;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.gsmtap.GsmTapPacket;
import io.pkts.packet.impl.AbstractPacket;
import io.pkts.protocol.Protocol;
import java.io.IOException;
import java.io.OutputStream;

public final class GsmTapPacketImpl extends AbstractPacket implements GsmTapPacket {

    private final TransportPacket parent;
    private final Buffer headers;

    public GsmTapPacketImpl(
            final TransportPacket parent, final Buffer headers, final Buffer payload) {
        super(Protocol.GSMTAP, parent, payload);
        this.parent = parent;
        this.headers = headers;
    }

    @Override
    public long getArrivalTime() {
        return parent.getArrivalTime();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GSMTAP ");
        sb.append(" Version: ")
                .append(getVersion())
                .append(" Type: ")
                .append(getType())
                .append("(")
                .append(getTypeAsInt())
                .append(")")
                .append(" SubType: ")
                .append(getSubType())
                .append("(")
                .append(getSubTypeAsInt())
                .append(")")
                .append(" Header Length: ")
                .append(getHeaders().getReadableBytes())
                .append(" Payload Length: ")
                .append(getPayload().getReadableBytes());
        return sb.toString();
    }

    @Override
    public GsmTapPacket clone() {
        final TransportPacket p = this.parent.clone();
        // TODO: clone
        return new GsmTapPacketImpl(p, this.headers.clone(), getPayload().clone());
    }

    @Override
    public Packet getNextPacket() throws IOException {
        return null;
    }

    @Override
    public void write(final OutputStream out, final Buffer payload) throws IOException {
        // TODO Auto-generated method stub
        throw new RuntimeException("Haven't implemented this yet");
    }

    @Override
    public Buffer getHeaders() {
        return headers;
    }

    @Override
    public int getVersion() {
        return headers.getUnsignedByte(0);
    }

    @Override
    public Type getType() {
        return Type.valueOf(getTypeAsInt());
    }

    private int getTypeAsInt() {
        return headers.getUnsignedByte(2);
    }

    @Override
    public SubType getSubType() {
        return SubType.valueOf(getType(), getSubTypeAsInt());
    }

    private int getSubTypeAsInt() {
        return headers.getUnsignedByte(12);
    }
}
