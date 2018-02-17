package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class MiddlePosition extends Mission {
	private enum MissionStates {
		waiting, left, right, done
	}

	private MissionStates curState = MissionStates.waiting;
	
	private Mission rightSideSwitch;
	private Mission rightSideToLeftSwitch;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting:
			if (onMySwitchSide(Hand.kRight)) {
				rightSideSwitch = new RightSideSwitch();
				curState = MissionStates.right;
			} else if (onMySwitchSide(Hand.kLeft)) {
				rightSideToLeftSwitch = new RightSideToLeftSwitch();
				curState = MissionStates.left;
			}
			break;
				
		case left: // like a firstTime
			rightSideToLeftSwitch.execute();
			break;
		case right: // like a firstTime
			rightSideSwitch.execute();
			break;
		
		case done:
			// turn stuff off an prepare for teleop
			break;
		default:
			break;
		}
	}
}