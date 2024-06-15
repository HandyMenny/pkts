/** */
package io.pkts.framer;

import io.pkts.buffer.Buffer;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.gsmtap.GsmTapV3Packet;
import io.pkts.packet.gsmtap.impl.GsmTapV3PacketImpl;
import io.pkts.protocol.Protocol;
import java.io.IOException;

/** GSMTAP v3 DRAFT */
public final class GsmTapV3Framer implements Framer<TransportPacket, GsmTapV3Packet> {

    public GsmTapV3Framer() {
        // left empty intentionally
    }

    /** {@inheritDoc} */
    @Override
    public Protocol getProtocol() {
        return Protocol.GSMTAPV3;
    }

    /** {@inheritDoc} */
    @Override
    public GsmTapV3Packet frame(final TransportPacket parent, final Buffer buffer)
            throws IOException {
        // length in number of 32bit words
        int hdrLength = buffer.getUnsignedShort(2) * 4;
        Buffer header = buffer.readBytes(hdrLength);
        Buffer payload = buffer.readBytes(buffer.getReadableBytes());
        return new GsmTapV3PacketImpl(parent, header, payload);
    }

    /** {@inheritDoc} */
    @Override
    public boolean accept(final Buffer data) throws IOException {
        int version = data.getUnsignedByte(0);

        // accept only version 3
        return version == 3;
    }
}
