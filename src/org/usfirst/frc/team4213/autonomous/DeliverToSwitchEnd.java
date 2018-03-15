package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class DeliverToSwitchEnd extends Mission {

	private enum MissionStates {
		waiting, driving, turning, reaching, reached, deploying, ejecting, done
	}

	private MissionStates curState = MissionStates.waiting;
	private Hand mySide;
	private double elevatorHeight = RobotMap.Elevator.SWITCHWALL_HEIGHT;
	private AutoDrive driveForward;
	private AutoDrive turnDegrees;
	private AutoDrive driveToWall;

	public DeliverToSwitchEnd(Hand side) {
		mySide = side;
		driveForward = new DriveWithEncoder(159.5);
		if (side.equals(Hand.kLeft)) {
			turnDegrees = new TurnDegrees(90);
		} else {
			turnDegrees = new TurnDegrees(-90);
		}
		driveToWall = new DriveToWall(13);
	}

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			intake.autoDeploy();
			curState = MissionStates.driving;
			break;
		case driving:
			driveForward.run();
			if (driveForward.isFinished())
				elevator.setPosition(elevatorHeight);
			curState = MissionStates.turning;
			break;
		case turning:
			turnDegrees.run();
			if (turnDegrees.isFinished())
				curState = MissionStates.deploying;
			break;
		case deploying:
			if (elevator.isAtHeight(elevatorHeight)) {
				curState = MissionStates.reaching;
			}
			break;
		case reaching:
			driveToWall.run();
			if (driveToWall.isFinished()) {
				curState = MissionStates.reached;
			}
			break;
		case reached:
			if (onMySwitchSide(mySide)) {
				intake.autoEject();
				curState = MissionStates.ejecting;
			} else {
				curState = MissionStates.done;
			}
			break;
		case ejecting:
			if (!intake.isIntakeRunning()) {
				curState = MissionStates.done;
			}
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		}
	}

}