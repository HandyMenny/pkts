package io.pkts;

import io.pkts.buffer.BoundedInputStreamBuffer;
import io.pkts.buffer.Buffer;
import io.pkts.buffer.Buffers;
import io.pkts.frame.PcapGlobalHeader;
import io.pkts.framer.FramerManager;
import io.pkts.framer.FramingException;
import io.pkts.framer.PcapFramer;
import io.pkts.packet.Packet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

/**
 * @author jonas@jonasborjesson.com
 */
public class Pcap {

    private final PcapGlobalHeader header;
    private final Buffer buffer;
    private final FramerManager framerManager;

    private Pcap(final PcapGlobalHeader header, final Buffer buffer) {
        assert header != null;
        assert buffer != null;
        this.header = header;
        this.buffer = buffer;
        this.framerManager = FramerManager.getInstance();
    }

    public void loop(final PacketHandler callback) throws IOException, FramingException {
        final ByteOrder byteOrder = this.header.getByteOrder();
        final PcapFramer framer = new PcapFramer(this.header, this.framerManager);
        int count = 1;

        Packet packet = null;
        boolean processNext = true;
        while ((packet = framer.frame(null, this.buffer)) != null && processNext) {
            final long time = packet.getArrivalTime();
            this.framerManager.tick(time);
            processNext = callback.nextPacket(packet);
        }
    }

    /**
     * Capture packets from the input stream
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static Pcap openStream(final InputStream is) throws IOException {
        final Buffer stream = Buffers.wrap(is);
        final PcapGlobalHeader header = PcapGlobalHeader.parse(stream);
        return new Pcap(header, stream);
    }

    /**
     * Capture packets from the input stream
     *
     * @param is
     * @param bufferCapacity Size of buffer, must be larger than PCAPs largest framesize. See
     *     SNAPLENGTH for tcpdump, et.al.
     * @return
     * @throws IOException
     */
    public static Pcap openStream(final InputStream is, final int bufferCapacity)
            throws IOException {
        final Buffer stream = new BoundedInputStreamBuffer(bufferCapacity, is);
        final PcapGlobalHeader header = PcapGlobalHeader.parse(stream);
        return new Pcap(header, stream);
    }

    /**
     * @param file the pcap file
     * @return a new {@link Pcap}
     * @throws FileNotFoundException in case the file doesn't exist.
     * @throws IOException
     */
    public static Pcap openStream(final File file) throws FileNotFoundException, IOException {
        final InputStream is = new FileInputStream(file);
        return openStream(is);
    }

    /**
     * @param file
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static Pcap openStream(final String file) throws FileNotFoundException, IOException {
        return openStream(new File(file));
    }

    public void close() {
        // TODO
    }

    /** */
    public PcapGlobalHeader getPcapHeader() {
        return this.header;
    }
}
