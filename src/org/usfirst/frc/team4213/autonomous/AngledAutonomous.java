package org.usfirst.frc.team4213.autonomous;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class AngledAutonomous extends Mission {
	private enum MissionStates {
		waiting, angledLeft, angledRight, done
	}

	private MissionStates curState = MissionStates.waiting;

	private Mission angledRight;
	private Mission angledLeft;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting:
			System.out.println("Running Angle autonomous");
			if (onMySwitchSide(Hand.kRight)) {
				angledRight = new AngledRight();
				curState = MissionStates.angledRight;
				System.out.println("Right Angle Auto");
			} else if (onMySwitchSide(Hand.kLeft)) {
				angledLeft = new AngledLeft();
				curState = MissionStates.angledLeft;
				System.out.println("Left Angle Auto");
			}
			break;
		case angledLeft:
			angledLeft.execute();
			break;
		case angledRight:
			angledRight.execute();
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		default:
			break;
		}
	}
}