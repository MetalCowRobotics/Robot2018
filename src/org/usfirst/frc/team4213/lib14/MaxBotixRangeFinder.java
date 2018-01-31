package org.usfirst.frc.team4213.lib14;

import edu.wpi.first.wpilibj.AnalogInput;

public class MaxBotixRangeFinder extends AnalogInput {

	public MaxBotixRangeFinder(int channel) {
		super(channel);
	}

	public double getDistanceInches() {
		double voltage = getVoltage();
		double distance = round((voltage * 100) / 2.54, 10);
		return distance;
	}

	private double round(double number, int precision) {
		return (double) Math.round(number * precision) / precision;
	}

}
