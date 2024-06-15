package io.pkts.packet.gsmtap;

import io.pkts.buffer.Buffer;
import io.pkts.packet.impl.ApplicationPacket;
import java.io.IOException;

public interface GsmTapV3Packet extends ApplicationPacket {
    int UDP_PORT = 4729;

    Buffer getHeaders();

    int getVersion() throws IOException;

    GsmTapV3Type getType() throws IOException;

    GsmTapV3SubType.IGsmTapV3SubType getSubType() throws IOException;
}
