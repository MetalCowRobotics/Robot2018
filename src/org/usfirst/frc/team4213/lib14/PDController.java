package org.usfirst.frc.team4213.lib14;

// calculate the error
// Error = Setpoint Value - Current Value
// determine the adjusted speeds of the motors.
// MotorSpeed = Kp * Error + Kd * ( Error - LastError );
// LastError = Error;
// RightMotorSpeed = RightBaseSpeed + MotorSpeed;
// LeftMotorSpeed = LeftBaseSpeed - MotorSpeed;
public class PDController {

	private final double maxAdjustment = .4;

	// must experiment to get these right
	private double Kp = .4;
	private double Kd = .1;

	// control variables
	private double setPoint;
	private double previousError;

	public PDController(double setPoint) {
		this.setPoint = setPoint;
	}

	public PDController(double setPoint, double Kp, double Kd) {
		this.setPoint = setPoint;
		this.Kd = Kd;
		this.Kp = Kp;
	}

	public double calculateAdjustment(double currentAngle) {
		// currentAngle = getGyroAngle();
		double currentError = calaculateError(setPoint, currentAngle);
		double motorAdjustment = determineAdjustment(currentError, previousError);
		previousError = currentError;
		return maxAdjustment(motorAdjustment);
	}

	private double calaculateError(double setPoint, double curPosition) {
		return setPoint - curPosition;
	}

	private double determineAdjustment(double currentError, double previousError) {
		return Kp * currentError + Kd * (currentError - previousError);
	}

	private double maxAdjustment(double speed) {
		if (Math.abs(speed) > Math.abs(maxAdjustment))
			if(speed<0)
				return -maxAdjustment;
			else
				return maxAdjustment;
		return speed;
	}

}
