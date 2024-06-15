/** */
package io.pkts.packet.gsmtap.impl;

import io.pkts.buffer.Buffer;
import io.pkts.packet.Packet;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.gsmtap.GsmTapV3Packet;
import io.pkts.packet.gsmtap.GsmTapV3SubType.IGsmTapV3SubType;
import io.pkts.packet.gsmtap.GsmTapV3Type;
import io.pkts.packet.impl.AbstractPacket;
import io.pkts.protocol.Protocol;
import java.io.IOException;

public final class GsmTapV3PacketImpl extends AbstractPacket implements GsmTapV3Packet {

    private final TransportPacket parent;
    private final Buffer headers;

    public GsmTapV3PacketImpl(
            final TransportPacket parent, final Buffer headers, final Buffer payload) {
        super(Protocol.GSMTAPV3, parent, payload);
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
    public GsmTapV3Packet clone() {
        final TransportPacket p = this.parent.clone();
        // TODO: clone
        return new GsmTapV3PacketImpl(p, this.headers.clone(), getPayload().clone());
    }

    @Override
    public Packet getNextPacket() throws IOException {
        return null;
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
    public GsmTapV3Type getType() {
        return GsmTapV3Type.valueOf(getTypeAsInt());
    }

    private int getTypeAsInt() {
        return headers.getUnsignedShort(4);
    }

    @Override
    public IGsmTapV3SubType getSubType() {
        return IGsmTapV3SubType.valueOf(getType(), getSubTypeAsInt());
    }

    private int getSubTypeAsInt() {
        return headers.getUnsignedShort(6);
    }
}
