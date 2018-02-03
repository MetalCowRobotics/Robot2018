package org.usfirst.frc.team4213.lib14;

// calculate the error
// Error = Setpoint Value - Current Value
// determine the adjusted speeds of the motors.
// MotorSpeed = Kp * Error + Kd * ( Error - LastError );
// LastError = Error;
// RightMotorSpeed = RightBaseSpeed + MotorSpeed;
// LeftMotorSpeed = LeftBaseSpeed - MotorSpeed;
public class PDController {


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
		double currentError = calaculateError(setPoint, currentAngle);
		double motorAdjustment = determineAdjustment(currentError, previousError);
		previousError = currentError;
		return motorAdjustment;
	}

	private double calaculateError(double setPoint, double curPosition) {
		return setPoint - curPosition;
	}

	private double determineAdjustment(double currentError, double previousError) {
		return Kp * currentError + Kd * (currentError - previousError);
	}



}
