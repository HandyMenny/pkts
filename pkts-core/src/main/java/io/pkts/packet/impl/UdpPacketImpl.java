/** */
package io.pkts.packet.impl;

import io.pkts.buffer.Buffer;
import io.pkts.packet.IPPacket;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.UDPPacket;
import io.pkts.protocol.Protocol;

/**
 * @author jonas@jonasborjesson.com
 */
public final class UdpPacketImpl extends TransportPacketImpl implements UDPPacket {

    private final Buffer headers;

    /**
     * @param parent
     * @param headers
     */
    public UdpPacketImpl(final IPPacket parent, final Buffer headers, final Buffer payload) {
        super(parent, Protocol.UDP, headers, payload);
        this.headers = headers;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isUDP() {
        return true;
    }

    @Override
    public int getHeaderLength() {
        return this.headers.capacity();
    }

    @Override
    public int getLength() {
        return this.headers.getUnsignedShort(4);
    }

    public void setLength(final int length) {
        this.headers.setUnsignedShort(4, length);
    }

    @Override
    public int getChecksum() {
        return this.headers.getUnsignedShort(6);
    }

    @Override
    public TransportPacket clone() {
        final IPPacket parent = getParentPacket().clone();
        return new UdpPacketImpl(parent, this.headers.clone(), getPayload().clone());
    }
}
