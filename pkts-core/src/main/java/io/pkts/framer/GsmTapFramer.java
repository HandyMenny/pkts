/**
 *
 */
package io.pkts.framer;

import io.pkts.buffer.Buffer;
import io.pkts.packet.gsmtap.GsmTapPacket;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.gsmtap.impl.GsmTapPacketImpl;
import io.pkts.protocol.Protocol;

import java.io.IOException;

/**
 * @author jonas@jonasborjesson.com
 *
 */
public final class GsmTapFramer implements Framer<TransportPacket, GsmTapPacket> {

    public GsmTapFramer() {
        // left empty intentionally
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Protocol getProtocol() {
        return Protocol.GSMTAP;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GsmTapPacket frame(final TransportPacket parent, final Buffer buffer) throws IOException {
        // length in number of 32bit words
        int hdrLength = buffer.getUnsignedByte(1) * 4;
        Buffer header = buffer.readBytes(hdrLength);
        Buffer payload = buffer.readBytes(buffer.getReadableBytes());
        return new GsmTapPacketImpl(parent, header, payload);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(final Buffer data) throws IOException {
        return true;
    }

}
