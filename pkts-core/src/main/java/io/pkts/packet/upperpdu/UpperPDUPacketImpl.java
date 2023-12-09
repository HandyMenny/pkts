/** */
package io.pkts.packet.upperpdu;

import io.pkts.buffer.Buffer;
import io.pkts.packet.PCapPacket;
import io.pkts.packet.Packet;
import io.pkts.packet.impl.AbstractPacket;
import io.pkts.protocol.Protocol;
import java.io.IOException;
import java.util.Arrays;

public final class UpperPDUPacketImpl extends AbstractPacket implements UpperPDUPacket {
    private final PCapPacket parent;

    private final PDUOption[] options;

    public UpperPDUPacketImpl(
            final Protocol protocol,
            final PCapPacket parent,
            final PDUOption[] options,
            final Buffer payload) {
        super(protocol, parent, payload);
        this.parent = parent;
        this.options = options;
    }

    @Override
    public long getArrivalTime() {
        return parent.getArrivalTime();
    }

    /** {@inheritDoc} */
    @Override
    public void verify() {
        // nothing to verify
    }

    @Override
    public String toString() {
        String sb =
                "EXPORTED PDU "
                        + " Dissector: "
                        + getDissector()
                        + " Payload Length: "
                        + getPayload().getReadableBytes()
                        + " Options: "
                        + Arrays.toString(options);

        return sb;
    }

    @Override
    public PDUOption[] getOptions() {
        return options.clone();
    }

    @Override
    public String getDissector() {
        for (PDUOption opt : options) {
            if (opt.getType() == PDUOption.TagOption.DISSECTOR_NAME) {
                return opt.getValueAsText();
            }
        }
        return null;
    }

    @Override
    public UpperPDUPacket clone() {
        final PCapPacket pkt = this.parent.clone();
        return new UpperPDUPacketImpl(
                getProtocol(), pkt, this.options.clone(), getPayload().clone());
    }

    @Override
    public Packet getNextPacket() throws IOException {
        return null;
    }
}
