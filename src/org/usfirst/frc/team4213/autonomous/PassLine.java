package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;

public class PassLine extends Mission {
	private enum MissionStates {
		waiting, driving, arrived, done
	}

	private MissionStates curState = MissionStates.waiting;

	private AutoDrive driveStep;

	public void execute() {
		switch (curState) {
		case waiting: // like a firstTime
			driveStep = new DriveWithEncoder(159.5);
			intake.deploy();
			System.out.println("waiting");
			curState = MissionStates.driving;
			break;
		case driving:
			driveStep.run();
			if (driveStep.isFinished())
				curState = MissionStates.arrived;
			System.out.println("driving");
			break;
		case arrived:
			driveStep = null;
			System.out.println("arrived");
			curState = MissionStates.done;
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		}
	}
}