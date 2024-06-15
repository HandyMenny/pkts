package io.pkts.packet.gsmtap;

/**
 * GSMTAP V3 Types from <a
 * href="https://gitea.osmocom.org/peremen/gsmtapv3/src/branch/master/include/osmocom/core/gsmtapv3.h">GSMTAP
 * V3 Proposal</a>
 */
public enum GsmTapV3Type {
    UNKNOWN(-1),

    /* 0x00, 0x01: Common and non-3GPP protocols */
    OSMOCORE_LOG(0x0000), /* libosmocore logging */
    SIM(0x0001), /* ISO 7816 smartcard interface */
    BASEBAND_DIAG(0x0002), /* Baseband diagnostic data */
    SIGNAL_STATUS_REPORT(0x0003), /* Radio signal status report, exact data TBD */
    TETRA_I1(0x0004), /* TETRA air interface */
    TETRA_I1_BURST(0x0005), /* TETRA air interface */
    GMR1_UM(0x0006), /* GMR-1 L2 packets */
    E1T1(0x0007), /* E1/T1 Lines */
    WMX_BURST(0x0008), /* WiMAX burst, shall we deprecate? */

    /* 0x02: GSM */
    UM(0x0200),
    UM_BURST(0x0201), /* raw burst bits */
    GB_RLCMAC(0x0202), /* GPRS Gb interface: RLC/MAC */
    GB_LLC(0x0203), /* GPRS Gb interface: LLC */
    GB_SNDCP(0x0204), /* GPRS Gb interface: SNDCP */
    ABIS(0x0205),
    RLP(0x0206), /* GSM RLP frames, as per 3GPP TS 24.022 */

    /* 0x03: UMTS/WCDMA */
    UMTS_MAC(0x0300), /* UMTS MAC PDU with context, as per 3GPP TS 25.321 */
    UMTS_RLC(0x0301), /* UMTS RLC PDU with context, as per 3GPP TS 25.322 */
    UMTS_PDCP(0x0302), /* UMTS PDCP PDU with context, as per 3GPP TS 25.323 */
    UMTS_RRC(0x0303), /* UMTS RRC PDU, as per 3GPP TS 25.331 */

    /* 0x04: LTE */
    LTE_MAC(0x0400), /* LTE MAC PDU with context, as per 3GPP TS 36.321 */
    LTE_RLC(0x0401), /* LTE RLC PDU with context, as per 3GPP TS 36.322 */
    LTE_PDCP(0x0402), /* LTE PDCP PDU with context, as per 3GPP TS 36.323 */
    LTE_RRC(0x0403), /* LTE RRC PDU, as per 3GPP TS 36.331 */
    NAS_EPS(0x0404), /* EPS Non-Access Stratum, as per 3GPP TS 24.301 */

    /* 0x05: NR */
    NR_MAC(0x0500), /* NR MAC PDU with context, as per 3GPP TS 38.321 */
    NR_RLC(0x0501), /* NR RLC PDU with context, as per 3GPP TS 38.322 */
    NR_PDCP(0x0502), /* NR PDCP PDU with context, as per 3GPP TS 38.323 */
    NR_RRC(0x0503), /* NR RRC PDU, as per 3GPP TS 38.331 */
    NAS_5GS(0x0504); /* 5GS Non-Access Stratum, as per 3GPP TS 24.501 */

    private final int index;
    private static final GsmTapV3Type[] values = values();

    GsmTapV3Type(int index) {
        this.index = index;
    }

    public static GsmTapV3Type valueOf(int x) {
        for (GsmTapV3Type type : values) {
            if (type.index == x) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
