/** */
package io.pkts.packet.impl;

import io.pkts.buffer.Buffer;
import io.pkts.frame.UnknownEtherType;
import io.pkts.framer.EthernetFramer;
import io.pkts.framer.IPv4Framer;
import io.pkts.framer.IPv6Framer;
import io.pkts.packet.IPPacket;
import io.pkts.packet.MACPacket;
import io.pkts.packet.PCapPacket;
import io.pkts.packet.PacketParseException;
import io.pkts.protocol.Protocol;
import java.io.IOException;

/**
 * @author jonas@jonasborjesson.com
 */
public final class MACPacketImpl extends AbstractPacket implements MACPacket {

    private static final IPv4Framer ipv4Framer = new IPv4Framer();
    private static final IPv6Framer ipv6Framer = new IPv6Framer();

    private final PCapPacket parent;
    private final String sourceMacAddress;
    private final String destinationMacAddress;

    /** If the headers are set then this overrides any of the source stuff set above. */
    private final Buffer headers;

    /** Construct a new {@link MACPacket} based on the supplied headers. */
    public MACPacketImpl(
            final Protocol protocol,
            final PCapPacket parent,
            final Buffer headers,
            final Buffer payload) {
        super(protocol, parent, payload);
        this.parent = parent;
        this.headers = headers;
        this.sourceMacAddress = null;
        this.destinationMacAddress = null;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSourceMacAddress() {
        if (this.sourceMacAddress != null) {
            return this.sourceMacAddress;
        }

        try {
            return toHexString(this.headers, 6, 6);
        } catch (final IOException e) {
            throw new RuntimeException("Unable to read data from the underlying Buffer.", e);
        }
    }

    public static String toHexString(final Buffer buffer, final int start, final int length)
            throws IOException {
        final StringBuilder sb = new StringBuilder();
        for (int i = start; i < start + length; ++i) {
            final byte b = buffer.getByte(i);
            sb.append(String.format("%02X", b));
            if (i < start + length - 1) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    public final String getDestinationMacAddress() {
        if (this.destinationMacAddress != null) {
            return this.destinationMacAddress;
        }

        try {
            return toHexString(this.headers, 0, 6);
        } catch (final IOException e) {
            throw new RuntimeException("Unable to read data from the underlying Buffer.", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void verify() {
        // nothing to verify
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Destination Mac Address: ")
                .append(this.destinationMacAddress)
                .append(" Source Mac Address: ")
                .append(this.sourceMacAddress);
        return sb.toString();
    }

    @Override
    public long getArrivalTime() {
        return this.parent.getArrivalTime();
    }

    /** {@inheritDoc} */
    @Override
    public void setSourceMacAddress(final String macAddress) {
        setMacAddress(macAddress, true);
    }

    /** {@inheritDoc} */
    @Override
    public void setDestinationMacAddress(final String macAddress) {
        setMacAddress(macAddress, false);
    }

    /**
     * Helper method for setting the mac address in the header buffer.
     *
     * @param macAddress the mac address to parse
     * @param setSourceMacAddress whether this is to bet set as the source mac address or not. False
     *     implies destination mac address of course.
     * @throws IllegalArgumentException
     */
    private void setMacAddress(final String macAddress, final boolean setSourceMacAddress)
            throws IllegalArgumentException {
        if (macAddress == null || macAddress.isEmpty()) {
            throw new IllegalArgumentException(
                    "Null or empty string cannot be a valid MAC Address.");
        }
        // very naive implementation first.
        final String[] segments = macAddress.split(":");
        if (segments.length != 6) {
            throw new IllegalArgumentException("Invalid MAC Address. Not enough segments");
        }

        final int offset = setSourceMacAddress ? 6 : 0;
        for (int i = 0; i < 6; ++i) {
            final byte b =
                    (byte)
                            ((Character.digit(segments[i].charAt(0), 16) << 4)
                                    + Character.digit(segments[i].charAt(1), 16));
            this.headers.setByte(i + offset, b);
        }
    }

    @Override
    public MACPacket clone() {
        final PCapPacket pkt = this.parent.clone();
        return new MACPacketImpl(getProtocol(), pkt, this.headers.clone(), getPayload().clone());
    }

    public Protocol getNextProtocol() throws IOException {
        EthernetFramer.EtherType etherType = EthernetFramer.EtherType.None;
        if (getProtocol() == Protocol.ETHERNET_II) {
            try {
                etherType = EthernetFramer.getEtherType(headers.getByte(12), headers.getByte(13));
            } catch (UnknownEtherType e) {
                throw new PacketParseException(
                        12,
                        String.format("Unknown Ethernet type 0x%02x%02x", e.getB1(), e.getB2()));
            }
        } else if (getProtocol() == Protocol.SLL) {
            try {
                etherType = EthernetFramer.getEtherType(headers.getByte(14), headers.getByte(15));
            } catch (UnknownEtherType e) {
                throw new PacketParseException(
                        14,
                        String.format("Unknown Ethernet type 0x%02x%02x", e.getB1(), e.getB2()));
            }
        } else if (getProtocol() == Protocol.SLL2) {
            try {
                etherType = EthernetFramer.getEtherType(headers.getByte(0), headers.getByte(1));
            } catch (UnknownEtherType e) {
                throw new PacketParseException(
                        0, String.format("Unknown Ethernet type 0x%02x%02x", e.getB1(), e.getB2()));
            }
        }

        if (etherType == EthernetFramer.EtherType.Dot1Q) {
            int maxSize = headers.capacity();
            try {
                etherType =
                        EthernetFramer.getEtherType(
                                headers.getByte(maxSize - 2), headers.getByte(maxSize - 1));
            } catch (UnknownEtherType e) {
                throw new PacketParseException(
                        maxSize - 2,
                        String.format("Unknown Ethernet type 0x%02x%02x", e.getB1(), e.getB2()));
            } catch (IndexOutOfBoundsException e) {
                throw new PacketParseException(maxSize - 4, "Not enough bytes in this header");
            }
        }

        switch (etherType) {
            case IPv4:
                return Protocol.IPv4;
            case IPv6:
                return Protocol.IPv6;
            case ARP:
                return Protocol.ARP;
            default:
                return Protocol.UNKNOWN;
        }
    }

    @Override
    public IPPacket getNextPacket() throws IOException {
        final Buffer payload = getPayload();
        if (payload == null) {
            return null;
        }
        switch (getNextProtocol()) {
            case IPv4:
                return ipv4Framer.frame(this, payload);
            case IPv6:
                return ipv6Framer.frame(this, payload);
            default:
                return null;
        }
    }
}
