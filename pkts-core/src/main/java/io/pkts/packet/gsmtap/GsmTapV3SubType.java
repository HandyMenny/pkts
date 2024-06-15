package io.pkts.packet.gsmtap;

/**
 * GSMTAP V3 Sub Types from <a
 * href="https://gitea.osmocom.org/peremen/gsmtapv3/src/branch/master/include/osmocom/core/gsmtapv3.h">GSMTAP
 * V3 Proposal</a>
 */
public class GsmTapV3SubType {
    public interface IGsmTapV3SubType {
        static IGsmTapV3SubType valueOf(GsmTapV3Type type, int subType) {
            switch (type) {
                case BASEBAND_DIAG:
                    return BasebandDiagSubtype.valueOf(subType);
                case UM:
                    return UMSubtype.valueOf(subType);
                case UMTS_RRC:
                    return UmtsRRCSubType.valueOf(subType);
                case LTE_RRC:
                    return LteRRCSubType.valueOf(subType);
                case NR_RRC:
                    return NrRRCSubType.valueOf(subType);
                case NAS_EPS:
                case NAS_5GS:
                    return NASSubType.valueOf(subType);
                default:
                    return UnknownSubType.UNKNOWN;
            }
        }
    }

    enum UnknownSubType implements IGsmTapV3SubType {
        UNKNOWN
    }

    enum BasebandDiagSubtype implements IGsmTapV3SubType {
        UNKNOWN(-1),
        QUALCOMM(0x0001), /* Qualcomm DIAG */
        SAMSUNG(0x0002), /* Samsung SDM */
        MEDIATEK(0x0003),
        UNISOC(0x0004),
        HISILICON(0x0005),
        INTEL(0x0006);

        private final int index;
        private static final BasebandDiagSubtype[] values = values();

        BasebandDiagSubtype(int index) {
            this.index = index;
        }

        public static BasebandDiagSubtype valueOf(int x) {
            for (BasebandDiagSubtype type : values) {
                if (type.index == x) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    enum UMSubtype implements IGsmTapV3SubType {
        UNKNOWN(0x0000),
        BCCH(0x0001),
        CCCH(0x0002),
        RACH(0x0003),
        AGCH(0x0004),
        PCH(0x0005),
        SDCCH(0x0006),
        SDCCH4(0x0007),
        SDCCH8(0x0008),
        FACCH_F(0x0009),
        FACCH_H(0x000a),
        PACCH(0x000b),
        CBCH52(0x000c),
        PDCH(0x000d),
        PTCCH(0x000e),
        CBCH51(0x000f),
        VOICE_F(0x0010), /* voice codec payload (FR/EFR/AMR) */
        VOICE_H(0x0011), /* voice codec payload (HR/AMR) */

        ACCH(0x0100);

        private final int index;
        private static final UMSubtype[] values = values();

        UMSubtype(int index) {
            this.index = index;
        }

        public static UMSubtype valueOf(int x) {
            for (UMSubtype type : values) {
                if (type.index == x) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    enum UmtsRRCSubType implements IGsmTapV3SubType {
        UNKNOWN(-1),
        DL_DCCH(0x0001),
        UL_DCCH(0x0002),
        DL_CCCH(0x0003),
        UL_CCCH(0x0004),
        RRC_PCCH(0x0005),
        DL_SHCCH(0x0006),
        UL_SHCCH(0x0007),
        BCCH_FACH(0x0008),
        BCCH_BCH(0x0009),
        BCCH_BCH2(0x000a),
        RRC_MCCH(0x000b),
        RRC_MSCH(0x000c);

        private final int index;
        private static final UmtsRRCSubType[] values = values();

        UmtsRRCSubType(int index) {
            this.index = index;
        }

        public static UmtsRRCSubType valueOf(int x) {
            for (UmtsRRCSubType type : values) {
                if (type.index == x) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    enum LteRRCSubType implements IGsmTapV3SubType {
        UNKNOWN(-1),
        BCCH_BCH(0x0001),
        BCH_MBMS(0x0002),
        BCCH_DL_SCH(0x0003),
        BCCH_DL_SCH_BR(0x0004),
        BCCH_DL_SCH_MBMS(0x0005),
        MCCH(0x0006),
        PCCH(0x0007),
        DL_CCCH(0x0008),
        DL_DCCH(0x0009),
        UL_CCCH(0x000a),
        UL_DCCH(0x000b),
        SC_MCCH(0x000c),
        SBCCH_SL_BCH(0x0101),
        SBCCH_SL_BCH_V2X(0x0102),
        BCCH_BCH_NB(0x0201),
        BCCH_BCH_TDD_NB(0x0202),
        BCCH_DL_SCH_NB(0x0203),
        PCCH_NB(0x0204),
        DL_CCCH_NB(0x0205),
        DL_DCCH_NB(0x0206),
        UL_CCCH_NB(0x0207),
        SC_MCCH_NB(0x0208),
        UL_DCCH_NB(0x0209);

        private final int index;
        private static final LteRRCSubType[] values = values();

        LteRRCSubType(int index) {
            this.index = index;
        }

        public static LteRRCSubType valueOf(int x) {
            for (LteRRCSubType type : values) {
                if (type.index == x) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    enum NrRRCSubType implements IGsmTapV3SubType {
        UNKNOWN(-1),
        BCCH_BCH(0x0001),
        BCCH_DL_SCH(0x0002),
        DL_CCCH(0x0003),
        DL_DCCH(0x0004),
        MCCH(0x0005),
        PCCH(0x0006),
        UL_CCCH(0x0007),
        UL_CCCH1(0x0008),
        UL_DCCH(0x0009),

        SBCCH_SL_BCH(0x0101),
        SCCH(0x0102);

        private final int index;
        private static final NrRRCSubType[] values = values();

        NrRRCSubType(int index) {
            this.index = index;
        }

        public static NrRRCSubType valueOf(int x) {
            for (NrRRCSubType type : values) {
                if (type.index == x) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }

    enum NASSubType implements IGsmTapV3SubType {
        UNKNOWN(-1),
        PLAIN(0x0000),
        SEC_HEADER(0x0001);

        private final int index;
        private static final NASSubType[] values = values();

        NASSubType(int index) {
            this.index = index;
        }

        public static NASSubType valueOf(int x) {
            for (NASSubType type : values) {
                if (type.index == x) {
                    return type;
                }
            }
            return UNKNOWN;
        }
    }
}
