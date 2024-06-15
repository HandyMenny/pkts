/** */
package io.pkts.framer;

import io.pkts.buffer.Buffer;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.gsmtap.GsmTapPacket;
import io.pkts.packet.gsmtap.impl.GsmTapPacketImpl;
import io.pkts.protocol.Protocol;
import java.io.IOException;

/**
 * @author jonas@jonasborjesson.com
 */
public final class GsmTapFramer implements Framer<TransportPacket, GsmTapPacket> {

    public GsmTapFramer() {
        // left empty intentionally
    }

    /** {@inheritDoc} */
    @Override
    public Protocol getProtocol() {
        return Protocol.GSMTAP;
    }

    /** {@inheritDoc} */
    @Override
    public GsmTapPacket frame(final TransportPacket parent, final Buffer buffer)
            throws IOException {
        // length in number of 32bit words
        int hdrLength = buffer.getUnsignedByte(1) * 4;
        Buffer header = buffer.readBytes(hdrLength);
        Buffer payload = buffer.readBytes(buffer.getReadableBytes());
        return new GsmTapPacketImpl(parent, header, payload);
    }

    private boolean scatUnofficialExtension(final Buffer data) {
        // In GSMTAPv3 Draft the second byte is reserved and == 0, while in scat unofficial v3 == 7
        // see:
        // https://github.com/fgsect/scat/blob/d83b5cd3867463a49e76b8e54cd36a6f12100bf9/src/scat/util.py#L301

        try {
            int version = data.getUnsignedByte(0);
            int headerLength = data.getUnsignedByte(1);

            return version == 3 && headerLength == 7;
        } catch (IndexOutOfBoundsException ignored) {
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean accept(final Buffer data) throws IOException {
        int version = data.getUnsignedByte(0);

        // accept only version 2 and scat unofficial v3
        return version == 2 || scatUnofficialExtension(data);
    }
}
