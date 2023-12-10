/** */
package io.pkts.framer;

import io.pkts.buffer.Buffer;
import io.pkts.packet.MACPacket;
import io.pkts.packet.PCapPacket;
import io.pkts.packet.impl.MACPacketImpl;
import io.pkts.protocol.Protocol;
import java.io.IOException;

/**
 * SLL is the linux cooked-mode capture v2. Check out the source file pcap/sll.h in libpcap. it
 * contains the structure and the defined values
 */
public class Sll2Framer implements Framer<PCapPacket, MACPacket> {

    /** See pcap/sll.h for the meaning of these values */
    private static final byte LINUX_SLL_HOST = (byte) 0x00;

    private static final byte LINUX_SLL_BROADCAST = (byte) 0x01;
    private static final byte LINUX_SLL_MULTICAST = (byte) 0x02;
    private static final byte LINUX_SLL_OTHERHOST = (byte) 0x03;
    private static final byte LINUX_SLL_OUTGOING = (byte) 0x04;

    /** */
    public Sll2Framer() {}

    /** {@inheritDoc} */
    @Override
    public Protocol getProtocol() {
        return Protocol.SLL2;
    }

    /** {@inheritDoc} */
    @Override
    public MACPacket frame(final PCapPacket parent, final Buffer buffer) throws IOException {
        if (parent == null) {
            throw new IllegalArgumentException("The parent frame cannot be null");
        }

        int headerSize = 20;

        if (buffer.getReadableBytes() < headerSize) {
            throw new FramingException("not enough bytes for header", getProtocol());
        }

        EthernetFramer.EtherType etherType =
                EthernetFramer.getEtherTypeSafe(buffer.getByte(0), buffer.getByte(1));

        if (etherType == EthernetFramer.EtherType.Dot1Q) {
            // add 802.1q header
            headerSize += 4;
        }

        final Buffer headers = buffer.readBytes(headerSize);
        final Buffer payload = buffer.slice(buffer.capacity());
        return new MACPacketImpl(Protocol.SLL2, parent, headers, payload);
    }

    /** {@inheritDoc} */
    @Override
    public boolean accept(final Buffer buffer) throws IOException {
        buffer.markReaderIndex();
        try {
            final Buffer test = buffer.readBytes(16);
            final byte b1 = test.getByte(0);
            final byte b2 = test.getByte(1);
            final byte b9 = test.getByte(8);
            final byte b10 = test.getByte(9);
            return validatePacketType(b9, b10) && isKnownEtherType(b1, b2);
        } catch (final IndexOutOfBoundsException e) {
            return false;
        } finally {
            buffer.resetReaderIndex();
        }
    }

    private boolean isKnownEtherType(final byte b1, final byte b2) {
        return EthernetFramer.getEtherTypeSafe(b1, b2) != null;
    }

    private boolean validatePacketType(final byte b1, final byte b2) {
        if (b1 != (byte) 0x00) {
            return false;
        }

        return b2 == LINUX_SLL_HOST
                || b2 == LINUX_SLL_BROADCAST
                || b2 == LINUX_SLL_MULTICAST
                || b2 == LINUX_SLL_OTHERHOST
                || b2 == LINUX_SLL_OUTGOING;
    }
}
