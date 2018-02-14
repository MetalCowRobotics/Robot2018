package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class LeftOrRightSwitch extends Mission {
	private enum MissionStates {
		data, left, right, ldriving1, ldriving2, larrived1, larrived2, lturning1, lturning2, lturned1, lturned2, lreaching, lreached, ldeploying, ldeployed, lejecting, lejected, rdriving, rarrived, rdeploying, rdeployed, rejecting, rejected, done
	}

	private MissionStates curState = MissionStates.data;
	
	
	private AutoDrive driveStep;
	private AutoDrive driveStep1;
	private AutoDrive driveStep2;
	private AutoDrive driveDegrees1;
	private AutoDrive driveDegrees2;
	private AutoDrive driveToWall;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case data:
			if (onMySwitchSide(Hand.kRight)) {
				curState = MissionStates.right;
			} else {
				onMySwitchSide(Hand.kLeft);
				curState = MissionStates.left;
			}
		break;
		case left: // like a firstTime
			driveStep1 = new DriveWithEncoder(60);
			// driveStep = new DriveWithEncoder(12);
			driveDegrees1 = new TurnDegrees(-90);
			driveStep2 = new DriveWithEncoder(84);
			driveDegrees2 = new TurnDegrees(90);
			driveToWall = new DriveToWall(13);
			intake.deploy();
			// elevator.moveToSetPosition(SetPositions.switchWall);
			System.out.println("waiting");
			curState = MissionStates.ldriving1;
			break;
		case ldriving1:
			driveStep1.run();
			if (driveStep1.isFinished())
				curState = MissionStates.larrived1;
			System.out.println("firstStraight");
			break;
		case larrived1:
			System.out.println("done with firstStraight");
			curState = MissionStates.lturning1;
			break;
		case lturning1:
			driveDegrees1.run();
			if (driveDegrees1.isFinished())
				curState = MissionStates.lturned1;
			System.out.println("firstTurn");
			break;
		case lturned1:
			curState = MissionStates.ldriving2;
			break;
		case ldriving2:
			driveStep2.run();
			if (driveStep2.isFinished())
				curState = MissionStates.larrived2;
			System.out.println("secondStraight");
			break;
		case larrived2:
			System.out.println("done with secondStraight");
			curState = MissionStates.lturning2;
			break;
		case lturning2:
			driveDegrees2.run();
			if (driveDegrees2.isFinished())
				curState = MissionStates.lturned2;	
			break;
		case lturned2: 
			curState = MissionStates.ldeploying;
			break;
		case ldeploying:
			// if (SetPositions.switchWall == elevator.getCurrentSetPostion()) {
			System.out.println("deploying");
			curState = MissionStates.ldeployed;
			// }
			break;
		case ldeployed:
			curState = MissionStates.lreaching;
			break;
		case lreaching:
			driveToWall.run();
			if (driveToWall.isFinished())
				curState = MissionStates.lreached;
			break;
		case lreached:
			System.out.println("ejecting");
			intake.autoEject();
			curState = MissionStates.lejecting;
			break;
		case lejecting:
			System.out.println("checking eject time");
			intake.execute();
			if (!intake.isIntakeRunning()) {
				curState = MissionStates.lejected;
			}
			break;
		case lejected:
			// could do a secondary mission
			curState = MissionStates.done;
			break;
		case right: // like a firstTime
			// elevator.moveToSetPosition(SetPositions.switchWall);
			driveStep = new DriveToWall(13);
			intake.deploy();
			curState = MissionStates.rdriving;
			break;
		case rdriving:
			driveStep.run();
			if (driveStep.isFinished()) {
				curState = MissionStates.rdeploying;
			}
			break;
		// case arrived:
		// curState = MissionStates.deploying;
		// break;
		case rdeploying:
			curState = MissionStates.rdeployed;
			break;
		case rdeployed:
				intake.autoEject();
				System.out.println("ejecting");
				curState = MissionStates.rejecting;
			break;
		case rejecting:
			System.out.println("checking eject time");
			intake.execute();
			if (!intake.isIntakeRunning()) {
				curState = MissionStates.rejected;
			}
			break;
		case rejected:
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