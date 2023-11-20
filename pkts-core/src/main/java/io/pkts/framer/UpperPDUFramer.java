/** */
package io.pkts.framer;

import io.pkts.buffer.Buffer;
import io.pkts.packet.PCapPacket;
import io.pkts.packet.upperpdu.PDUOption;
import io.pkts.packet.upperpdu.PDUOption.TagOption;
import io.pkts.packet.upperpdu.UpperPDUPacket;
import io.pkts.packet.upperpdu.UpperPDUPacketImpl;
import io.pkts.protocol.Protocol;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UpperPDUFramer implements Framer<PCapPacket, UpperPDUPacket> {

    public UpperPDUFramer() {}

    /** {@inheritDoc} */
    @Override
    public Protocol getProtocol() {
        return Protocol.UPPPER_PDU;
    }

    /** {@inheritDoc} */
    @Override
    public UpperPDUPacket frame(final PCapPacket parent, final Buffer buffer)
            throws IOException, FramingException {
        if (parent == null) {
            throw new IllegalArgumentException("The parent frame cannot be null");
        }
        List<PDUOption> options = new ArrayList<>();
        while (true) {
            PDUOption nextOption = parseNextOption(buffer);
            options.add(nextOption);
            if (nextOption.getType() == TagOption.END_OF_OPT) {
                break;
            }
        }
        PDUOption[] optionsArray = options.toArray(new PDUOption[0]);

        final Buffer payload = buffer.readBytes(buffer.getReadableBytes());
        return new UpperPDUPacketImpl(Protocol.UPPPER_PDU, parent, optionsArray, payload);
    }

    @Override
    public boolean accept(final Buffer data) {
        return false;
    }

    private PDUOption parseNextOption(final Buffer buffer) throws IOException {
        TagOption type = TagOption.valueOf(buffer.readShort());
        short length = buffer.readShort();
        Buffer payload;
        if (length > 0) {
            payload = buffer.readBytes(length);
        } else {
            payload = null;
        }
        return new PDUOption(type, payload);
    }
}
