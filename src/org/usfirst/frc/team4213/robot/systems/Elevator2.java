package org.usfirst.frc.team4213.robot.systems;

import edu.wpi.first.wpilibj.CounterBase;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import org.usfirst.frc.team4213.lib14.MCR_SRX;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.controllers.MasterControls;
import org.usfirst.frc.team4213.lib14.PDController;
import org.usfirst.frc.team4213.lib14.UtilityMethods;

import java.util.logging.Logger;
public class Elevator2 {
    private static final Elevator2 instance = new Elevator2();
    private static final Logger logger = Logger.getLogger(Elevator.class.getName());

    private static final MasterControls controller = MasterControls.getInstance();
    private static final MCR_SRX ELEVATOR_MOTOR1 = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL1);
    private static final MCR_SRX ELEVATOR_MOTOR2 = new MCR_SRX(RobotMap.Elevator.ELEVATOR_CHANNEL2);
    private static final edu.wpi.first.wpilibj.SpeedController ELEVATOR_MOTOR = new MCR_SRX(1);
    private static SpeedControllerGroup ElevatorSpeedControllerGroup = new SpeedControllerGroup (ELEVATOR_MOTOR1, ELEVATOR_MOTOR2);

    private static final Encoder elevatorEncoder = new Encoder(RobotMap.Elevator.ELEVATOR_ENCODER_1, RobotMap.Elevator.ELEVATOR_ENCODER_2, false, CounterBase.EncodingType.k4X);

    DigitalInput topLimit;// = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_TOP);
    DigitalInput bottomLimit;// = new DigitalInput(RobotMap.Elevator.LIMIT_SWITCH_BOTTOM);

    private boolean holding = false;
    private PDController holdPID;

    //to RobotMap
    private final double safeSpeed = .5;
    private final double safetyZone = .5;
    private final double topTics = .5;
    private final double bottomTics = .5;
    double kP = .2;
    double kD = 0;
    double range = 7000-0;
    private final double tolerance = 10;
    private final double maxAdjustment = .5;

    private Elevator2() {
        // Singleton Pattern
    }

    public static Elevator2 getInstance() {
        return instance;
    }

    public void execute() {
        if (holding && isStopped(controller.getElevatorThrottle())) {
            adjustHoldPID();
        } else {
            setElevatorSpeed(controller.getElevatorThrottle());
        }
    }

    public void setElevatorSpeed(double speed) {
        if (isMovingUp(speed) && isElevatorAtTop())
            hold(topTics);
        else if (isMovingDown(speed) && isElevatorAtBottom())
            stop();
        else if (0 == speed)
            hold(getEncoderTics());
        else
            ELEVATOR_MOTOR.set(maxSpeed(speed));
    }

    public void stop() {
        ElevatorSpeedControllerGroup.stopMotor();
    }

    public void hold(double currentPosition) {
        holdPID = new PDController(currentPosition / range, kP, kD);
    }

    private boolean isMovingUp(double speed) {
        return speed > 0;
    }

    private boolean isMovingDown(double speed) {
        return speed < 0;
    }

    private boolean isStopped(double speed) {
        return 0 == speed;
    }

    private boolean isElevatorAtTop() {
        return !topLimit.get(); //For some reason this is inverted in the hardware, correcting here in software
    }

    private boolean isElevatorAtBottom() {
        return !bottomLimit.get(); //for some reason this is inverted in hardware, correcting here in software
    }

    private double maxSpeed(double requestedSpeed) {
        if (isMovingUp(requestedSpeed) && inUpperSafteyZone()) {
            return Math.min(requestedSpeed, safeSpeed);  //safe speed from RobotMap
        } else if (isMovingDown(requestedSpeed) && inLowerSafteyZone()) {
            return Math.max(requestedSpeed, -safeSpeed);  //negate the safe speed to move down
        }
        return requestedSpeed;
    }

    private boolean inUpperSafteyZone() {
        return getEncoderTics() > (topTics - safetyZone);
    }

    private boolean inLowerSafteyZone() {
        return getEncoderTics() < (bottomTics + safetyZone);
    }

    private double getEncoderTics() {
        return elevatorEncoder.getDistance();
    }

    private void adjustHoldPID() {
        double currentError = (Math.abs(getEncoderTics()) > tolerance) ? getEncoderTics() : 0;
        double correction = holdPID.calculateAdjustment(currentError / range);
        setElevatorSpeed(maxCorrection(correction));
    }

    private double maxCorrection(double correction) {
        if (Math.abs(correction) > maxAdjustment)
            return correction;
        else
            return UtilityMethods.copySign(correction, maxAdjustment);
    }

}