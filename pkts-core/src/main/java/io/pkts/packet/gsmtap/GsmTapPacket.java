package io.pkts.packet.gsmtap;

import io.pkts.buffer.Buffer;
import io.pkts.packet.impl.ApplicationPacket;
import java.io.IOException;

public interface GsmTapPacket extends ApplicationPacket {
    int UDP_PORT = 4729;

    Buffer getHeaders();

    int getVersion() throws IOException;

    Type getType() throws IOException;

    SubType getSubType() throws IOException;

    int getArfcn();

    enum Type {
        UNKNOWN,
        UM,
        ABIS,
        UM_BURST,
        SIM,
        TETRA_I1,
        TETRA_I1_BURST,
        WMX_BURST,
        GB_LLC,
        GB_SNDCP,
        GMR1_UM,
        UMTS_RLC_MAC,
        UMTS_RRC,
        LTE_RRC,
        LTE_MAC,
        LTE_MAC_FRAMED,
        OSMOCORE_LOG,
        QC_DIAG,
        LTE_NAS,
        E1T1,
        GSM_RLP;

        private static final Type[] values = values();

        public static Type valueOf(int x) {
            try {
                return values[x];
            } catch (Exception ignored) {
                return UNKNOWN;
            }
        }
    }

    enum ChannelSubType implements SubType {
        UNKNOWN,
        BCCH,
        CCCH,
        RACH,
        AGCH,
        PCH,
        SDCCH,
        SDCCH4,
        SDCCH8,
        FACCH_F,
        FACCH_H,
        PACCH,
        CBCH52,
        PDTCH,
        PTCCH,
        CBCH51,
        VOICE_F,
        VOICE_H;

        private static final ChannelSubType[] values = values();

        public static ChannelSubType valueOf(int x) {
            try {
                return values[x];
            } catch (Exception ignored) {
                return UNKNOWN;
            }
        }
    }

    enum UmtsRRCSubType implements SubType {
        DL_DCCH,
        UL_DCCH,
        DL_CCCH,
        UL_CCCH,
        PCCH,
        DL_SHCCH,
        UL_SHCCH,
        BCCH_FACH,
        BCCH_BCH,
        MCCH,
        MSCH,
        HandoverToUTRANCommand,
        InterRATHandoverInfo,
        SystemInformation_BCH,
        System_Information_Container,
        UE_RadioAccessCapabilityInfo,
        MasterInformationBlock,
        SysInfoType1,
        SysInfoType2,
        SysInfoType3,
        SysInfoType4,
        SysInfoType5,
        SysInfoType5bis,
        SysInfoType6,
        SysInfoType7,
        SysInfoType8,
        SysInfoType9,
        SysInfoType10,
        SysInfoType11,
        SysInfoType11bis,
        SysInfoType12,
        SysInfoType13,
        SysInfoType13_1,
        SysInfoType13_2,
        SysInfoType13_3,
        SysInfoType13_4,
        SysInfoType14,
        SysInfoType15,
        SysInfoType15bis,
        SysInfoType15_1,
        SysInfoType15_1bis,
        SysInfoType15_2,
        SysInfoType15_2bis,
        SysInfoType15_2ter,
        SysInfoType15_3,
        SysInfoType15_3bis,
        SysInfoType15_4,
        SysInfoType15_5,
        SysInfoType15_6,
        SysInfoType15_7,
        SysInfoType15_8,
        SysInfoType16,
        SysInfoType17,
        SysInfoType18,
        SysInfoType19,
        SysInfoType20,
        SysInfoType21,
        SysInfoType22,
        SysInfoTypeSB1,
        SysInfoTypeSB2,
        ToTargetRNC_Container,
        TargetRNC_ToSourceRNC_Container,
        UNKNOWN;

        private static final UmtsRRCSubType[] values = values();

        public static UmtsRRCSubType valueOf(int x) {
            try {
                return values[x];
            } catch (Exception ignored) {
                return UNKNOWN;
            }
        }
    }

    enum LteRRCSubType implements SubType {
        DL_CCCH,
        DL_DCCH,
        UL_CCCH,
        UL_DCCH,
        BCCH_BCH,
        BCCH_DL_SCH,
        PCCH,
        MCCH,
        BCCH_BCH_MBMS,
        BCCH_DL_SCH_BR,
        BCCH_DL_SCH_MBMS,
        SC_MCCH,
        SBCCH_SL_BCH,
        SBCCH_SL_BCH_V2X,
        DL_CCCH_NB,
        DL_DCCH_NB,
        UL_CCCH_NB,
        UL_DCCH_NB,
        BCCH_BCH_NB,
        BCCH_BCH_TDD_NB,
        BCCH_DL_SCH_NB,
        PCCH_NB,
        SC_MCCH_NB,
        UNKNOWN;

        private static final LteRRCSubType[] values = LteRRCSubType.values();

        public static LteRRCSubType valueOf(int x) {
            try {
                return values[x];
            } catch (Exception ignored) {
                return UNKNOWN;
            }
        }
    }

    enum LteNASSubType implements SubType {
        PLAIN,
        SEC_HEADER,
        UNKNOWN;

        private static final LteNASSubType[] values = LteNASSubType.values();

        public static LteNASSubType valueOf(int x) {
            try {
                return values[x];
            } catch (Exception ignored) {
                return UNKNOWN;
            }
        }
    }

    enum UnknownSubType implements SubType {
        UNKNOWN
    }

    interface SubType {
        static SubType valueOf(Type type, int subType) {
            switch (type) {
                case UM:
                    return ChannelSubType.valueOf(subType);
                case UMTS_RRC:
                    return UmtsRRCSubType.valueOf(subType);
                case LTE_RRC:
                    return LteRRCSubType.valueOf(subType);
                case LTE_NAS:
                    return LteNASSubType.valueOf(subType);
            }
            return UnknownSubType.UNKNOWN;
        }
    }
}
