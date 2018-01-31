package org.usfirst.frc.team4213.lib14;

import edu.wpi.first.wpilibj.AnalogInput;

public class MaxBotixRangeFinder extends AnalogInput{
	
	public MaxBotixRangeFinder(int channel) {
		super(channel);
	}
	
	
	public double getDistanceInches() {
		double voltage = getVoltage();
		double distance =  Math.round (voltage * 10000.0) / 10000.0;
		//ToDo: fix Math. Sorry :(
		return distance;
	}

	
	

}
