package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class FarRightSwitch extends Mission {
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
			driveStep = new DriveWithEncoder(159.5);
			//driveStep = new DriveWithEncoder(12);
			driveDegrees = new TurnDegrees(-90);
			driveToWall = new DriveToWall(13);
			intake.autoDeploy();
			// elevator.moveToSetPosition(SetPositions.switchWall);
			setCurState(MissionStates.driving);
			break;
		case driving:
			driveStep.run();
			if (driveStep.isFinished()) {
				setCurState(MissionStates.arrived);
			}
			break;
		case arrived:
			setCurState(MissionStates.turning);
			break;
		case turning:
			driveDegrees.run();
			if (driveDegrees.isFinished())
			setCurState(MissionStates.turned);
			break;
		case turned:
			setCurState(MissionStates.deploying);
			break;
		case deploying:
			// if (SetPositions.switchWall == elevator.getCurrentSetPostion()) {
			setCurState(MissionStates.deployed);
			// }
			break;
		case deployed:
			setCurState(MissionStates.reaching);
			break;
		case reaching:
			driveToWall.run();
			if (driveToWall.isFinished()) {
				setCurState(MissionStates.reached);
			}
			break;
		case reached:
			if (onMySwitchSide(Hand.kLeft)) {
				intake.autoEject();
				setCurState(MissionStates.ejecting);
			} else {
				setCurState(MissionStates.done);
			}
			break;
		case ejecting:
			//intake.execute();
			if (!intake.isIntakeRunning()) {
				setCurState(MissionStates.ejected);
			}
			break;
		case ejected:
			// could do a secondary mission
			setCurState(MissionStates.done);
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		}
	}

	private void setCurState(MissionStates curState) {
		this.curState = curState;
		logger.info("FarRightSwitch state change to:" + curState.name());
	}
	
}
