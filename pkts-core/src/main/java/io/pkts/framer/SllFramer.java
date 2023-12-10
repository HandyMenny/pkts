/** */
package io.pkts.framer;

import io.pkts.buffer.Buffer;
import io.pkts.packet.MACPacket;
import io.pkts.packet.PCapPacket;
import io.pkts.packet.impl.MACPacketImpl;
import io.pkts.protocol.Protocol;
import java.io.IOException;

/**
 * SLL is the linux cooked-mode capture.
 *
 * <p>http://wiki.wireshark.org/SLL
 *
 * <p>Also, check out the source file pcap/sll.h in libpcap. it contains the structure and the
 * defined values
 *
 * @author jonas@jonasborjesson.com
 */
public class SllFramer implements Framer<PCapPacket, MACPacket> {

    /** See pcap/sll.h for the meaning of these values */
    private static final byte LINUX_SLL_HOST = (byte) 0x00;

    private static final byte LINUX_SLL_BROADCAST = (byte) 0x01;
    private static final byte LINUX_SLL_MULTICAST = (byte) 0x02;
    private static final byte LINUX_SLL_OTHERHOST = (byte) 0x03;
    private static final byte LINUX_SLL_OUTGOING = (byte) 0x04;

    /**
     * See pcap/sll.h for explanation of what this is. Also note that these values are actually two
     * bytes starting with 0x00 but I left that out here.
     *
     * <p>Novell 802.3 frames without 802.2 LLC header
     */
    private static final byte LINUX_SLL_P_802_3 = (byte) 0x01;

    /** 802.2 frames (not D/I/X Ethernet) */
    private static final byte LINUX_SLL_P_802_2 = (byte) 0x04;

    /** */
    public SllFramer() {}

    /** {@inheritDoc} */
    @Override
    public Protocol getProtocol() {
        return Protocol.SLL;
    }

    /** {@inheritDoc} */
    @Override
    public MACPacket frame(final PCapPacket parent, final Buffer buffer) throws IOException {
        if (parent == null) {
            throw new IllegalArgumentException("The parent frame cannot be null");
        }

        int headerSize = 16;

        if (buffer.getReadableBytes() < headerSize) {
            throw new FramingException("not enough bytes for header", getProtocol());
        }

        EthernetFramer.EtherType etherType =
                EthernetFramer.getEtherTypeSafe(buffer.getByte(14), buffer.getByte(15));

        if (etherType == EthernetFramer.EtherType.Dot1Q) {
            // add 802.1q header
            headerSize += 4;
        }

        final Buffer headers = buffer.readBytes(headerSize);
        final Buffer payload = buffer.slice(buffer.capacity());
        return new MACPacketImpl(Protocol.SLL, parent, headers, payload);
    }

    /**
     * (taken from pcap/sll.sh)
     *
     * <p>For captures on Linux cooked sockets, we construct a fake header that includes:
     *
     * <p>a 2-byte "packet type" which is one of:
     *
     * <p>LINUX_SLL_HOST packet was sent to us LINUX_SLL_BROADCAST packet was broadcast
     * LINUX_SLL_MULTICAST packet was multicast LINUX_SLL_OTHERHOST packet was sent to somebody else
     * LINUX_SLL_OUTGOING packet was sent *by* us;
     *
     * <p>a 2-byte Ethernet protocol field;
     *
     * <p>a 2-byte link-layer type;
     *
     * <p>a 2-byte link-layer address length;
     *
     * <p>an 8-byte source link-layer address, whose actual length is specified by the previous
     * value.
     *
     * <p>All fields except for the link-layer address are in network byte order.
     *
     * <p>DO NOT change the layout of this structure, or change any of the LINUX_SLL_ values below.
     * If you must change the link-layer header for a "cooked" Linux capture, introduce a new DLT_
     * type (ask "tcpdump-workers@lists.tcpdump.org" for one, so that you don't give it a value that
     * collides with a value already being used), and use the new header in captures of that type,
     * so that programs that can handle DLT_LINUX_SLL captures will continue to handle them
     * correctly without any change, and so that capture files with different headers can be told
     * apart and programs that read them can dissect the packets in them.
     *
     * <p>{@inheritDoc}
     */
    @Override
    public boolean accept(final Buffer buffer) throws IOException {
        buffer.markReaderIndex();
        try {
            final Buffer test = buffer.readBytes(16);
            final byte b1 = test.getByte(0);
            final byte b2 = test.getByte(1);
            final byte b14 = test.getByte(14);
            final byte b15 = test.getByte(15);
            return validatePacketType(b1, b2) && isKnownEtherType(b14, b15);
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
