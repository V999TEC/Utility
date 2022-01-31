package uk.co.myzen.atoz.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Airtime {

	private static Map<String, Airtime> airtimeInstances = new HashMap<String, Airtime>(15);

	private final SpreadingFactor spreadingFactor;
	private final Band band;
	private final BandWidth bandWidth;

	private final int[] airtime;

	public enum Band {

		EU868, US915,

	};

	public enum SpreadingFactor {

		SF7, SF8, SF9, SF10, SF11, SF12
	};

	public enum BandWidth {

		BW125, BW250, BW500
	};

	// US915
	// (uplink)
	private static final int[] data_US915_DR0_BW125_SF10_11 = { 2888, 3297, 3707, };

	private static final int[] data_US915_DR1_BW125_SF9_53 = { 1649, 1853, 2058, 2263, 2468, 2673, 2877, 3082, 3287,
			3492, 3697, 3901, };

	private static final int[] data_US915_DR2_BW125_SF8_125 = { 824, 927, 1029, 1132, 1234, 1336, 1439, 1541, 1644,
			1746, 1848, 1951, 2053, 2156, 2258, 2360, 2463, 2565, 2668, 2770, 2872, 2975, 3077, 3180, 3282, 3384, 3487,
			3589, 3692, 3794, 3896, 3999, };

	private static final int[] data_US915_DR3_BW125_SF7_242 = { 463, 515, 566, 617, 668, 719, 771, 822, 873, 924, 975,
			1027, 1078, 1129, 1180, 1231, 1283, 1334, 1385, 1436, 1487, 1539, 1590, 1641, 1692, 1743, 1795, 1846, 1897,
			1948, 1999, 2051, 2102, 2153, 2204, 2255, 2307, 2358, 2409, 2460, 2511, 2563, };

	private static final int[] data_US915_DR4_DR12_BW500_SF8_222 = { 206, 232, 257, 283, 308, 334, 360, 385, 411, 436,
			462, 488, 513, 539, 564, 590, 616, 641, 667, 692, 718, 744, 769, 795, 820, 846, 872, 897, 923, 948, 974,
			1000, 1025, 1051, 1076, 1102, 1128, 1153, 1179, 1204, 1230, 1256, 1281, 1307, 1332, 1358, 1384, 1409, 1435,
			1460, 1486, 1512, 1537, 1563, 1588, 1614, 1640, }; // 904.6Mhz only - actually DR4 and DR12 are the same

	// (downlink)
	private static final int[] data_US915_DR8_BW500_SF12_33 = { 2888, 3297, 3707, 4116, 4529, 4936, };

	private static final int[] data_US915_DR9_BW500_SF11_109 = { 1444, 1649, 1853, 2058, 2263, 2468, 2673, 2877, 3082,
			3287, 3492, 3697, 3901, 4106, 4311, 4516, 4721, 4925, 5130, 5335, 5540, };

	private static final int[] data_US915_DR10_BW500_SF10_222 = { 722, 824, 927, 1029, 1132, 1234, 1336, 1439, 1541,
			1644, 1746, 1848, 1951, 2053, 2156, 2258, 2360, 2463, 2565, 2668, 2770, 2872, 2975, 3077, 3180, 3282, 3384,
			3487, 3589, 3692, 3794, 3896, 3999, 4101, 4204, 4036, 4408, 4511, 4613, 4716, 4818, 4920, 5023, 5125, 5228,
			5330, };
	private static final int[] data_US915_DR11_BW500_SF9_222 = { 412, 463, 515, 566, 617, 668, 719, 771, 822, 873, 924,
			975, 1027, 1078, 1129, 1180, 1231, 1283, 1334, 1385, 1436, 1487, 1539, 1590, 1641, 1692, 1743, 1795, 1846,
			1897, 1948, 1999, 2051, 2102, 2153, 2204, 2255, 2307, 2358, 2409, 2460, 2511, 2563, 2614, 2665, 2716, 2767,
			2819, 2870, 2921, };

	private static final int[] data_US915_DR13_BW500_SF7_222 = { 116, 129, 141, 154, 167, 180, 193, 205, 218, 231, 244,
			257, 269, 282, 295, 308, 321, 333, 346, 359, 372, 385, 397, 410, 423, 436, 449, 461, 474, 487, 500, 513,
			525, 538, 551, 564, 577, 589, 602, 615, 628, 641, 653, 666, 679, 692, 705, 717, 730, 743, 756, 769, 781,
			794, 807, 820, 833, 845, 858, 871, 884, 897, 909, 922, };

	// EU868
	private static final int[] data_EU868_DR0_BW125_SF12_51 = { 11551, 13189, 14828, 16466, 18104, 19743, 21381, 23020,
			24658, 26296, 27935 };

	private static final int[] data_EU868_DR1_BW125_SF11_51 = { 5775, 6595, 7414, 8233, 9052, 9871, 10691, 11510, 12329,
			13148, 13967, 14787, 15606 };

	private static final int[] data_EU868_DR2_BW125_SF10_51 = { 2888, 3297, 3707, 4116, 4526, 4936, 5345, 5755, 6164,
			6574, 6984, };

	private static final int[] data_EU868_DR3_BW125_SF9_115 = { 1649, 1853, 2058, 2263, 2468, 2673, 2877, 3082, 3287,
			3492, 3697, 3901, 4106, 4311, 4516, 4721, 4925, 5130, 5335, 5540, 5745, 5949, 6154, 6359, 6564, 6769 };

	private static final int[] data_EU868_DR4_BW125_SF8_222 = { 824, 927, 1029, 1132, 1234, 1336, 1439, 1541, 1644,
			1746, 1848, 1951, 2053, 2156, 2258, 2360, 2463, 2565, 2668, 2770, 2872, 2975, 3077, 3180, 3282, 3384, 3487,
			3589, 3692, 3794, 3896, 3999, 4101, 4204, 4306, 4408, 4511, 4613, 4716, 4818, 4920, 5023, 5125, 5228, 5330,
			5432, 5535, 5637, 5740, 5842, 5944, 6047, 6149, 6252, 6354, 6456, 6559 };

	private static final int[] data_EU868_DR5_BW125_SF7_222 = { 463, 515, 566, 617, 668, 719, 771, 822, 873, 924, 975,
			1027, 1078, 1129, 1180, 1231, 1283, 1334, 1385, 1436, 1487, 1539, 1590, 1641, 1692, 1743, 1795, 1846, 1897,
			1948, 1999, 2051, 2102, 2153, 2204, 2255, 2307, 2358, 2409, 2460, 2511, 2563, 2614, 2665, 2716, 2767, 2819,
			2870, 2921, 2972, 3023, 3075, 3126, 3177, 3228, 3279, 3331, 3382, 3433, 3484, 3535, 3587, 3638, 3689 };

	private static final int[] data_EU868_DR6_BW250_SF7_222 = { 232, 257, 283, 308, 334, 360, 385, 411, 436, 462, 488,
			513, 539, 564, 590, 616, 641, 667, 692, 718, 744, 769, 795, 820, 846, 872, 897, 923, 948, 974, 1000, 1025,
			1051, 1076, 1102, 1128, 1153, 1179, 1204, 1230, 1256, 1281, 1307, 1332, 1358, 1384, 1409, 1435, 1460, 1486,
			1512, 1537, 1563, 1588, 1614, 1640, 1665, 1691, 1716, 1742, 1768, 1793, 1819, 1844 };

	private static int[] expandTable(int preamble, int countFirst, int countSecond, int maxTableSize, int[] data) {

		int[] result = new int[maxTableSize];

		List<Integer> intArray = new ArrayList<Integer>(maxTableSize);

		int count = preamble;

		int index = 0;

		boolean first = false;

		try {

			do {

				Integer value = Integer.valueOf(data[index]);

				for (int i = 0; i < count; i++) {

					if (intArray.size() >= maxTableSize) {

						return result;
					}

					intArray.add(value);
				}

				first = !first;

				count = first ? countFirst : countSecond;

				index++;

			} while (index < data.length);

			return result;

		} finally {

			for (int i = 0; i < intArray.size(); i++) {

				result[i] = intArray.get(i);
			}

		}

	}

	public static class AirtimeException extends Exception {

		private static final long serialVersionUID = 1399328105162716430L;

		private final Float context;

		public AirtimeException(String message, Float context) {

			super(message);

			this.context = context;
		}

		public Float getContext() {

			return context;
		}
	}

	public static Airtime getInstance(Band band, int bw, int sf) throws AirtimeException {

		Airtime instance = null;

		SpreadingFactor enumSpreadingFactor;
		BandWidth enumBandWidth;

		if (500000 == bw) {

			enumBandWidth = BandWidth.BW500;

		} else if (250000 == bw) {

			enumBandWidth = BandWidth.BW250;

		} else if (125000 == bw) {

			enumBandWidth = BandWidth.BW125;

		} else {

			throw new AirtimeException("Invalid BW " + bw, -1f);
		}

		switch (sf) {
		case 7:
			enumSpreadingFactor = SpreadingFactor.SF7;

			break;
		case 8:
			enumSpreadingFactor = SpreadingFactor.SF8;

			break;
		case 9:
			enumSpreadingFactor = SpreadingFactor.SF9;

			break;
		case 10:
			enumSpreadingFactor = SpreadingFactor.SF10;

			break;
		case 11:
			enumSpreadingFactor = SpreadingFactor.SF11;

			break;
		case 12:
			enumSpreadingFactor = SpreadingFactor.SF12;

			break;
		default:
			throw new AirtimeException("Invalid SF " + sf, -1f);
		}

		String key = band.name() + "_" + enumBandWidth.name() + "_" + enumSpreadingFactor.name();

		if (airtimeInstances.containsKey(key)) {

			instance = airtimeInstances.get(key);

		} else {

			instance = new Airtime(band, enumBandWidth, enumSpreadingFactor);

			airtimeInstances.put(key, instance);
		}

		return instance;
	}

	private Airtime(Band band, BandWidth bandWidth, SpreadingFactor spreadingFactor) throws AirtimeException {

		this.spreadingFactor = spreadingFactor;
		this.band = band;
		this.bandWidth = bandWidth;

		switch (band) {

		default:
			airtime = new int[0];
			break;

		case US915:
			if (BandWidth.BW500 == bandWidth) {

				if (SpreadingFactor.SF12 == spreadingFactor) {

					airtime = expandTable(6, 6, 6, 33, data_US915_DR8_BW500_SF12_33);
					break;
				}

				if (SpreadingFactor.SF11 == spreadingFactor) {

					airtime = expandTable(4, 6, 5, 109, data_US915_DR9_BW500_SF11_109);
					break;
				}

				if (SpreadingFactor.SF10 == spreadingFactor) {

					airtime = expandTable(2, 5, 5, 222, data_US915_DR10_BW500_SF10_222);
					break;
				}

				if (SpreadingFactor.SF9 == spreadingFactor) {

					airtime = expandTable(5, 4, 5, 222, data_US915_DR11_BW500_SF9_222);
					break;
				}

				if (SpreadingFactor.SF8 == spreadingFactor) {

					airtime = expandTable(2, 4, 4, 222, data_US915_DR4_DR12_BW500_SF8_222);
					break;
				}

				if (SpreadingFactor.SF7 == spreadingFactor) {

					airtime = expandTable(3, 4, 3, 222, data_US915_DR13_BW500_SF7_222);
					break;
				}

			} else if (BandWidth.BW125 == bandWidth) {

				if (SpreadingFactor.SF10 == spreadingFactor) {

					airtime = expandTable(2, 5, 5, 11, data_US915_DR0_BW125_SF10_11);
					break;
				}

				if (SpreadingFactor.SF9 == spreadingFactor) {

					airtime = expandTable(5, 4, 5, 53, data_US915_DR1_BW125_SF9_53);
					break;
				}

				if (SpreadingFactor.SF8 == spreadingFactor) {

					airtime = expandTable(2, 4, 4, 125, data_US915_DR2_BW125_SF8_125);
					break;
				}

				if (SpreadingFactor.SF7 == spreadingFactor) {

					airtime = expandTable(3, 4, 3, 242, data_US915_DR3_BW125_SF7_242);
					break;
				}
			}

			airtime = new int[0];
			break;

		case EU868:

			if (BandWidth.BW250 == bandWidth) {

				if (SpreadingFactor.SF7 == spreadingFactor) {

					airtime = expandTable(3, 4, 3, 223, data_EU868_DR6_BW250_SF7_222);
					break;
				}

			} else if (BandWidth.BW125 == bandWidth) {

				if (SpreadingFactor.SF7 == spreadingFactor) {

					airtime = expandTable(3, 4, 3, 223, data_EU868_DR5_BW125_SF7_222); // 0
					break;
				}

				if (SpreadingFactor.SF8 == spreadingFactor) {

					airtime = expandTable(2, 4, 4, 223, data_EU868_DR4_BW125_SF8_222);
					break;
				}

				if (SpreadingFactor.SF9 == spreadingFactor) {

					airtime = expandTable(5, 4, 5, 116, data_EU868_DR3_BW125_SF9_115);
					break;
				}

				if (SpreadingFactor.SF10 == spreadingFactor) {

					airtime = expandTable(2, 5, 5, 52, data_EU868_DR2_BW125_SF10_51);
					break;
				}

				if (SpreadingFactor.SF11 == spreadingFactor) {

					airtime = expandTable(1, 5, 4, 52, data_EU868_DR1_BW125_SF11_51);
					break;
				}

				if (SpreadingFactor.SF12 == spreadingFactor) {

					airtime = expandTable(3, 5, 5, 52, data_EU868_DR0_BW125_SF12_51);
					break;
				}
			}

			airtime = new int[0];
			break;
		}

		if (0 == airtime.length) {

			throw new AirtimeException("Unsupported combination: BandWidth " + bandWidth + " SpreadingFactor "
					+ spreadingFactor + " for " + band, -1f);
		}
	}

	public BandWidth getBandWidth() {

		return bandWidth;
	}

	public Band getBand() {

		return band;
	}

	public SpreadingFactor getSpreadingFactor() {

		return spreadingFactor;
	}

	public float msAirtimeEstimate(int payloadSize) throws AirtimeException {

		if (payloadSize > airtime.length - 1 || payloadSize < 0) {

			throw new AirtimeException("Invalid payload size " + payloadSize + " for " + band + " " + bandWidth + " "
					+ spreadingFactor + " Needs to be in range 0 to " + (airtime.length - 1),
					airtime(airtime.length - 1));
		}

		return airtime(payloadSize);
	}

	private float airtime(int payloadSize) {

		String value = String.valueOf(airtime[payloadSize]);

		String value1dp = value.substring(0, value.length() - 1) + "." + value.substring(value.length() - 1);

		Float result = Float.valueOf(value1dp);

		return result.floatValue();
	}

	public int getMaximumPayloadSize() {

		return airtime.length - 1;
	}

	public int getMinPayloadSizeForAirtime(float msAirtime) {

		int tableValue = Float.valueOf(10 * msAirtime).intValue();

		int index = 0;

		for (; index < airtime.length; index++) {

			if (tableValue <= airtime[index]) {

				break;
			}
		}

		return index;
	}

	public int getMaxPayloadSizeForAirtime(float msAirtime) throws AirtimeException {

		int index = getMinPayloadSizeForAirtime(msAirtime);

		int time = airtime[index];

		do {

			index++;

			if (index == airtime.length) {

				float maxAirtime = airtime(airtime.length - 1);

				throw new AirtimeException("Invalid airtime size " + msAirtime + " for " + band + " " + bandWidth + " "
						+ spreadingFactor + " The maximum allowed airtime is " + maxAirtime + " mS", maxAirtime);
			}

		} while (time == airtime[index]);

		return index;
	}
}
