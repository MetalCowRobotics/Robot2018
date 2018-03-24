package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.RobotMap;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class RightPosToRigthSwitch extends Mission {

	private enum MissionStates {
		waiting, driving, arrived, deploying, deployed, ejecting, ejected, done

		// deploys to scale on right side

	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			intake.autoDeploy();
			//driveStep = new DriveToWall(18);
			driveStep = new DriveWithEncoder(95);
			curState = MissionStates.driving;
			break;
		case deploying:
			if (intake.isIntakeDown()) {
				elevator.setPosition(RobotMap.Elevator.SWITCHWALL_HEIGHT);
				curState = MissionStates.driving;				
			}
			break;
		case driving:
			driveStep.run();
			System.out.println("Driving");
			if (driveStep.isFinished()) {
				System.out.println("Drive is done");
				curState = MissionStates.deployed;
			}
			break;
		case arrived:
			curState = MissionStates.deployed;
			break;
		case deployed:
			System.out.println("ON MY SIDE" +onMySwitchSide(Hand.kRight) );
			if (onMySwitchSide(Hand.kRight)) {
				intake.autoEject();
				curState = MissionStates.ejecting;
			} else {
				curState = MissionStates.done;
			}
			break;
		case ejecting:
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
		default:
			break;
		}
	}

}
