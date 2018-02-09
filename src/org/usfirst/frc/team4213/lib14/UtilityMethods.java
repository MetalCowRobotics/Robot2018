package org.usfirst.frc.team4213.lib14;

public class UtilityMethods {
	public static double copySign(double source, double target) {
		if (0 < source)
			return Math.abs(target);
		else
			return -1 * Math.abs(target);
	}

	// determine if value is between the lower and upper inclusive of the endpoint
	public static boolean between(double value, double lower, double upper) {
		return value >= lower && value <= upper;
	}

	public static double round(double number, int precision) {
		return (double) Math.round(number * precision) / precision;
	}
}
