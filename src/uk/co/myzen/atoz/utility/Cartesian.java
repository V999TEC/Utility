package uk.co.myzen.atoz.utility;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class Cartesian {

	private double x;
	private double y;
	private double z;

	private int altitude;

	public Cartesian(double x, double y, double z, int altitude) {

		this.x = x;
		this.y = y;
		this.z = z;
		this.altitude = altitude;
	}

	public Cartesian(double latitude, double longitude, int altitude) {

		double radLat = Haversine.toRadians(latitude);
		double radLong = Haversine.toRadians(longitude);

		double w = Math.cos(radLat);

		x = w * Math.cos(radLong);

		y = w * Math.sin(radLong);

		z = Math.sin(radLat);

		this.altitude = altitude;
	}

	public int getAltitude() {
		return altitude;
	}

	public void setAltitude(int altitude) {
		this.altitude = altitude;
	}

	public double toLongitude() {

		double radLong = Math.atan2(y, x);

		return Haversine.toDegrees(radLong);
	}

	public double toLatitude() {

		double w = Math.sqrt((x * x) + (y * y));

		double radLat = Math.atan2(z, w);

		return Haversine.toDegrees(radLat);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public static Cartesian getMean(List<Cartesian> cartesians) {

		Cartesian result = null;

		int size = null == cartesians ? 0 : cartesians.size();

		if (size > 0) {

			double xSum = 0;
			double ySum = 0;
			double zSum = 0;

			int altSum = 0;

			for (Cartesian c : cartesians) {

				xSum += c.getX();
				ySum += c.getY();
				zSum += c.getZ();
				altSum += c.getAltitude();
			}

			result = new Cartesian(xSum / size, ySum / size, zSum / size, altSum / size);
		}

		return result;
	}

	public String toRoundAltitude() {

		return String.valueOf(toRoundInt(getAltitude()));
	}

	public String toRoundLatitude(int precision) {

		return toRoundDoubleAsString(toLatitude(), precision);
	}

	public String toRoundLongitude(int precision) {

		return toRoundDoubleAsString(toLongitude(), precision);
	}

	private static String toRoundDoubleAsString(double number, int precision) {

		String formatString = "0.######################";

		String text = String.valueOf(number);

		DecimalFormat df = new DecimalFormat(formatString.substring(0, precision + text.indexOf('.')));

		df.setRoundingMode(RoundingMode.CEILING);

		return df.format(number);
	}

	private static int toRoundInt(int number) {

		int calc = number * 10 + 5;

		return calc / 10;
	}
}
