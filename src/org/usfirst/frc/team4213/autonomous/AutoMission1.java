package org.usfirst.frc.team4213.autonomous;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoMission1 extends Mission {

	public void execute() {
		stepOne();
	}

	private void stepOne() {
		double angle = driveTrain.getAngle();
		// double Kp = 0.15;

		double Kp = SmartDashboard.getNumber("Kp", .15);
		System.out.println("Drive angle: " + (angle));
		driveTrain.autoDrive(0, -angle * Kp);
	}

}
