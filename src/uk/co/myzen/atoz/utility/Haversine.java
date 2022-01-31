/**
 * 
 */
package uk.co.myzen.atoz.utility;

/**
 * @author howard
 *
 *         Utility functions to calculate distance and bearing between locations
 *         Uses Haversine formula for great-circle distance and bearing
 *         calculations involving latitude/longitude See formulae at
 *         https://www.movable-type.co.uk/scripts/latlong.html Also see
 *         http://tchester.org/sgm/analysis/peaks/how_to_get_view_params.html
 *
 */
public class Haversine {

	public static final double APPROX_RADIUS_EARTH_MILES = 3959.87433; // Had we assumed WGS84 ellipsoid (semi-major
																		// axis = 6378137m) value would be 3963.190592
																		// miles
																		// However many Haversine formula
																		// examples, not based on WGS84 ellipsoid,
																		// assume a mean Earth radius of 3959.87433
																		// miles

	public static final double APPROX_FEET_IN_NAUTICAL_MILE = 6076.12; // modern convention for definition of nautical
																		// mile is equivalent to 1852 metres
																		// In contrast there are 1609.344 meters in
																		// an imperial mile
	public static final double NAUTICAL_MILES_IN_A_MILE = 0.868976;

	public static final double MILES_IN_A_NAUTICAL_MILE = 1.15078;

	public static final double FEET_IN_METRE = 3.28084;

	public static final double FEET_IN_MILE = 5280; // because precisely 1760 yards in an imperial mile

	public static final double METRES_IN_IMPERIAL_MILE = 1609.344;

	public static final double DEGREES_TO_RADIANS = 0.017453292519943295;

	public static final double RADIANS_TO_DEGREES = 57.29577951308232;

	private final double milesEarthRadiusApprox;

	public Haversine(double metresEarthRadiusApprox) {

		if (metresEarthRadiusApprox < 6000000) {

			// just return angularDistance by setting R == 1

			milesEarthRadiusApprox = 1;

		} else {

			// convert supplied metres into miles
			milesEarthRadiusApprox = metresEarthRadiusApprox * FEET_IN_METRE / FEET_IN_MILE;
		}
	}

	public Haversine() {

		milesEarthRadiusApprox = APPROX_RADIUS_EARTH_MILES;
	}

	public static double milesToNauticalMiles(double distanceMiles) {

		return NAUTICAL_MILES_IN_A_MILE * distanceMiles;
	}

	public static double nauticalMilesToMiles(double distanceNauticalMiles) {

		return MILES_IN_A_NAUTICAL_MILE * distanceNauticalMiles;
	}

	public static Double toRadians(double value) {

		return value * DEGREES_TO_RADIANS;
	}

	public static Double toDegrees(double value) {

		return (double) RADIANS_TO_DEGREES * value;
	}

	public double calculateFeetHeightFromAngle(double angle, double distanceMiles) {

		double ftDistance = distanceMiles * FEET_IN_MILE;

		double radAngularDrop = distanceMiles / (2 * APPROX_RADIUS_EARTH_MILES);

		double tangent = Math.tan(toRadians(angle) + radAngularDrop);

		double height = tangent * ftDistance;

		return height;
	}

	/**
	 * This complex method takes account of a spherical earth.
	 * 
	 * @param distanceMiles
	 * @param ftLocalHeight
	 * @param ftRemoteHeight
	 * @return
	 */
	public double calculateElevationAngle(double distanceMiles, double ftLocalHeight, double ftRemoteHeight) {

		// this assumes R=3956.87433 miles d=distance in feet & uses a formula:

		// (180/pi) * atan{ (elev2 - elev1) / d - ( d/2*R ) }

		double ftDistance = distanceMiles * FEET_IN_MILE;

		double radAngularDrop = distanceMiles / (2 * APPROX_RADIUS_EARTH_MILES);

		double ftDiff = ftRemoteHeight - ftLocalHeight;

		double radians = ftDistance <= 0 ? 0 : ftDiff / ftDistance;

		double angle = toDegrees(Math.atan(radians - radAngularDrop));

		return angle;
	}

	public double calculateRemoteLatitude(double bearing, double distance, double localLat) {

		final double angularDistance = distance / milesEarthRadiusApprox;

		double radBearing = toRadians(bearing);
		double radLocalLat = toRadians(localLat);

		double a = Math.sin(radLocalLat) * Math.cos(angularDistance)
				+ Math.cos(radLocalLat) * Math.sin(angularDistance) * Math.cos(radBearing);

		double remoteLat = toDegrees(Math.asin(a));

		return remoteLat;
	}

	public double calculateRemoteLongitude(double bearing, double distance, double localLat, double localLon,
			double remoteLat) {

		final double angularDistance = distance / milesEarthRadiusApprox;

		double radBearing = toRadians(bearing);

		double radLocalLat = toRadians(localLat);
		double radLocalLon = toRadians(localLon);
		double radRemoteLat = toRadians(remoteLat);

		double y = Math.sin(radBearing) * Math.sin(angularDistance) * Math.cos(radLocalLat);
		double x = Math.cos(angularDistance) - Math.sin(radLocalLat) * Math.sin(radRemoteLat);

		double remoteLon = toDegrees(radLocalLon + Math.atan2(y, x));

		// normalise to -180...+180
		return (540 + remoteLon) % 360 - 180;
	}

	public double calculateDistance(double localLat, double localLon, double remoteLat, double remoteLon) {

		Double latDistance = toRadians(remoteLat - localLat);
		Double lonDistance = toRadians(remoteLon - localLon);

		double latSq = Math.pow(Math.sin(latDistance / 2), 2); // just squaring the sin of half the distance
		double lonSq = Math.pow(Math.sin(lonDistance / 2), 2);

		Double a = latSq + Math.cos(toRadians(localLat)) * Math.cos(toRadians(remoteLat)) * lonSq;

		Double angularDistance = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		Double distance = milesEarthRadiusApprox * angularDistance;

		return distance;
	}

	public double calculateBearing(double localLat, double localLon, double remoteLat, double remoteLon) {

		double radLatLocal = Math.toRadians(localLat);
		double radLatRemote = Math.toRadians(remoteLat);

		double lonDiff = Math.toRadians(remoteLon - localLon);

		double y = Math.sin(lonDiff) * Math.cos(radLatRemote);

		double x = Math.cos(radLatLocal) * Math.sin(radLatRemote)
				- Math.sin(radLatLocal) * Math.cos(radLatRemote) * Math.cos(lonDiff);

		double bearing = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;

		return bearing;
	}

}
