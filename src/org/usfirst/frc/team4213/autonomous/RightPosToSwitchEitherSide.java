package org.usfirst.frc.team4213.autonomous;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class RightPosToSwitchEitherSide extends Mission {
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
				rightSideSwitch = new RightPosToRigthSwitch();
				curState = MissionStates.right;
			} else if (onMySwitchSide(Hand.kLeft)) {
				rightSideToLeftSwitch = new RightSideToLeftSwitch();
				curState = MissionStates.left;
			}
			break;
		case left:
			rightSideToLeftSwitch.execute();
			break;
		case right:
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