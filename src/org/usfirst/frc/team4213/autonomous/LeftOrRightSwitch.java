package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class LeftOrRightSwitch extends Mission {
	private enum MissionStates {
		data, left, right, ldriving, larrived, lturning, lturned, lreaching, lreached, ldeploying, ldeployed, lejecting, lejected, rwaiting, rdriving, rarrived, rdeploying, rdeployed, rejecting, rejected, done
	}

	private MissionStates curState = MissionStates.data;

	private AutoDrive driveStep;
	private AutoDrive driveDegrees;
	private AutoDrive driveToWall;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case data:
			if (onMySide(Hand.kRight)) {
				curState = MissionStates.right;
			} else {
				onMySide(Hand.kLeft);
				curState = MissionStates.left;
			}
		case left: // like a firstTime
			driveStep = new DriveWithEncoder(159.5);
			// driveStep = new DriveWithEncoder(12);
			driveDegrees = new TurnDegrees(90);
			driveToWall = new DriveToWall(13);
			intake.deploy();
			// elevator.moveToSetPosition(SetPositions.switchWall);
			System.out.println("waiting");
			curState = MissionStates.ldriving;
			break;
		case ldriving:
			driveStep.run();
			if (driveStep.isFinished())
				curState = MissionStates.larrived;
			System.out.println("driving");
			break;
		case larrived:
			System.out.println("arrived");
			curState = MissionStates.lturning;
			break;
		case lturning:
			driveDegrees.run();
			if (driveDegrees.isFinished())
				curState = MissionStates.lturned;
			System.out.println("turning");
			break;
		case lturned:
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
		case rwaiting: // like a firstTime
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
			// if (SetPositions.switchWall == elevator.getCurrentSetPostion()) {
			curState = MissionStates.rdeployed;
			// }
			break;
		case rdeployed:
			if (onMySide(Hand.kRight)) {
				intake.autoEject();
				System.out.println("ejecting");
				curState = MissionStates.rejecting;
			} else {
				curState = MissionStates.done;
			}
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