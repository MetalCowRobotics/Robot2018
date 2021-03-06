package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class LeftSideScale extends Mission{
	private enum MissionStates {
		waiting, driving, arrived, turning, turned, reaching, reached, deploying, deployed, ejecting, ejected, done
	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep;
	private AutoDrive driveDegrees;
	private AutoDrive driveToWall;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			driveStep = new DriveWithEncoder(319);
			//driveStep = new DriveWithEncoder(12);
			driveDegrees = new TurnDegrees(90);
			driveToWall = new DriveToWall(13);
			intake.autoDeploy();
			// elevator.moveToSetPosition(SetPositions.switchWall);
			curState = MissionStates.driving;
			break;
		case driving:
			driveStep.run();
			if (driveStep.isFinished())
				curState = MissionStates.arrived;
			break;
		case arrived:
			curState = MissionStates.turning;
			break;
		case turning:
			driveDegrees.run();
			if (driveDegrees.isFinished())
				curState = MissionStates.turned;
			break;
		case turned:
			curState = MissionStates.deploying;
			break;
		case deploying:
			// if (SetPositions.switchWall == elevator.getCurrentSetPostion()) {
			curState = MissionStates.deployed;
			// }
			break;
		case deployed:
			curState = MissionStates.reaching;
			break;
		case reaching:
			driveToWall.run();
			if (driveToWall.isFinished()) {
				curState = MissionStates.reached;
			}
			break;
		case reached:
			if (onMySwitchSide(Hand.kLeft)) {
				intake.autoEject();
				curState = MissionStates.ejecting;
			} else {
				curState = MissionStates.done;
			}
			break;
		case ejecting:
			//intake.execute();
			if (!intake.isIntakeRunning()) {
				curState = MissionStates.ejected;
			}
			break;
		case ejected:
			// could do a secondary mission
			curState = MissionStates.done;
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		}
	}

}
