package io.pkts.packet.upperpdu;

import io.pkts.buffer.Buffer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class PDUOption {
    private final TagOption type;
    private final Buffer value;

    public PDUOption(TagOption type, Buffer value) {
        this.type = type;
        this.value = value;
    }

    public boolean isText() {
        return type.ascii || type.utf8;
    }

    public String getValueAsText() {
        if (!isText()) {
            return null;
        }
        Charset charset = StandardCharsets.US_ASCII;
        if (type.utf8) {
            charset = StandardCharsets.UTF_8;
        }
        return new String(value.getArray(), charset);
    }

    public Buffer getValue() {
        return value;
    }

    public TagOption getType() {
        return type;
    }

    @Override
    public String toString() {
        if (getValue() == null) {
            return type.toString();
        }

        String value;
        if (isText()) {
            value = getValueAsText();
        } else {
            value = Arrays.toString(getValue().getArray());
        }

        return type + ": " + value;
    }

    public enum TagOption {
        END_OF_OPT(0),
        OPTIONS_LENGTH(10), // Deprecated
        LINKTYPE(11), // Deprecated
        DISSECTOR_NAME(12, true, false),
        HEUR_DISSECTOR_NAME(13, true, false),
        DISSECTOR_TABLE_NAME(14, true, false),
        IPV4_SRC(20),
        IPV4_DST(21),
        IPV6_SRC(22),
        IPV6_DST(23),
        PORT_TYPE(24),
        SRC_PORT(25),
        DST_PORT(26),
        SS7_OPC(28),
        SS7_DPC(29),
        ORIG_FNO(30),
        DVBCI_EVT(31),
        DISSECTOR_TABLE_NAME_NUM_VAL(32),
        COL_PROT_TEXT(33, false, true),
        TCP_INFO_DATA(34),
        P2P_DIRECTION(35),
        COL_INFO_TEXT(36, false, true),
        UNKNOWN(Integer.MAX_VALUE);

        private static final TagOption[] values = values();
        private final int type;
        private final boolean ascii;
        private final boolean utf8;

        TagOption(final int type) {
            this.type = type;
            this.ascii = false;
            this.utf8 = false;
        }

        TagOption(final int type, final boolean ascii, final boolean utf8) {
            this.type = type;
            this.ascii = ascii;
            this.utf8 = utf8;
        }

        public static TagOption valueOf(int type) {
            for (TagOption tag : values) {
                if (tag.type == type) {
                    return tag;
                }
            }
            return UNKNOWN;
        }

        public int getType() {
            return type;
        }

    }
}

