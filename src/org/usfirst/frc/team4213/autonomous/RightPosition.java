package org.usfirst.frc.team4213.autonomous;


import edu.wpi.first.wpilibj.GenericHID.Hand;

public class RightPosition extends Mission {
	private enum MissionStates {
		waiting, scale, Switch, passLine, done
	}

	private MissionStates curState = MissionStates.waiting;
	
	private Mission rightSideOfScale;
	private Mission farRightSideSwitch;
	private Mission passLine;

	// The Go Straight For X Feet Mission

	public void execute() {
		switch (curState) {
		case waiting:
			if (onMySwitchSide(Hand.kLeft)) {
				farRightSideSwitch = new FarRightSwitch();
				curState = MissionStates.Switch;
			} else if (onMyScaleSide(Hand.kLeft)) {
				rightSideOfScale = new FarRightScale();
				curState = MissionStates.scale;
			} else {
				passLine = new PassLine();
				curState = MissionStates.passLine;
			}
				
			break;
				
		case Switch: // like a firstTime
			farRightSideSwitch.execute();
			break;
		case scale: // like a firstTime
			rightSideOfScale.execute();
			break;
		case passLine:
			passLine.execute();
			break;
		
		case done:
			// turn stuff off an prepare for teleop
			break;
		default:
			break;
		}
	}
}