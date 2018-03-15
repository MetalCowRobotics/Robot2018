//package org.usfirst.frc.team4213.autonomous;
//
//import edu.wpi.first.wpilibj.GenericHID.Hand;
//
//public class AngledAutonomous extends Mission {
//	private enum MissionStates {
//		waiting, angledLeft, angledRight, done
//	}
//
//	private MissionStates curState = MissionStates.waiting;
//
//	private Mission angledRight;
//	private Mission angledLeft;
//
//	// The Go Straight For X Feet Mission
//
//	public void execute() {
//		switch (curState) {
//		case waiting:
//			System.out.println("Running Angle autonomous");
//			if (onMySwitchSide(Hand.kRight)) {
//				angledRight = new AngledRight();
//				curState = MissionStates.angledRight;
//				System.out.println("Right Angle Auto");
//			} else if (onMySwitchSide(Hand.kLeft)) {
//				angledLeft = new AngledLeft();
//				curState = MissionStates.angledLeft;
//				System.out.println("Left Angle Auto");
//			}
//			break;
//		case angledLeft:
//			angledLeft.execute();
//			break;
//		case angledRight:
//			angledRight.execute();
//			break;
//		case done:
//			// turn stuff off an prepare for teleop
//			break;
//		default:
//			break;
//		}
//	}
//}

package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.Elevator;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class AngledAutonomous extends Mission {
	private enum MissionStates {
		waiting, left, right, done
	}

	private MissionStates curState = MissionStates.waiting;

	// private Mission rightSideSwitch;
	// private Mission rightSideToLeftSwitch;
	private AutoDrive driveStep;
	private AutoDrive driveDegrees;
	private AutoDrive driveToWall;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting:
			System.out.println("In Auto with Ifs");
			intake.autoDeploy();
			if(intake.isIntakeDown())
				Elevator.getInstance().setPosition(RobotMap.Elevator.SWITCHWALL_HEIGHT);
			driveToWall = new DriveToWall(13);
			driveDegrees = new TurnDegrees(45);
			driveStep = new DriveWithEncoder(80);// DriveWithEncoder(159.5);
			if (onMySwitchSide(Hand.kRight)) {
				driveDegrees.run();
				if (driveDegrees.isFinished())
					driveToWall.run();
				if (driveToWall.isFinished())
					intake.autoEject();
			} else if (onMySwitchSide(Hand.kLeft)) {
				driveStep.run();
				if (driveStep.isFinished())
					driveDegrees.run();
				if (driveDegrees.isFinished())
					driveToWall.run();
				if (driveToWall.isFinished())
					intake.autoEject();
				curState = MissionStates.left;
			}
		case done:// turn stuff off an prepare for teleop
			break;
		default:
			break;
		}
	}
}