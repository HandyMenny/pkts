/**
 *
 */
package io.pkts.packet.upperpdu;

import io.pkts.packet.Packet;

public interface UpperPDUPacket extends Packet, Cloneable {
    @Override
    UpperPDUPacket clone();

    PDUOption[] getOptions();

    String getDissector();
}
