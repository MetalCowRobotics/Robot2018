package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.lib14.MCRCommand;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class ScaleOrSwitchMission implements MCRCommand {
	private MCRCommand command;

	public ScaleOrSwitchMission(Hand mySide, Hand switchSide, Hand scaleSide) {
		if (mySide.equals(scaleSide)) {
			command = new SwitchEndMission(mySide, scaleSide);
		} else if (mySide.equals(switchSide)) {
			command = new SwitchEndMission(mySide, switchSide);
		} else {
			command = new PassLineMission();
		}
	}

	@Override
	public void run() {
		command.run();
	}

	@Override
	public boolean isFinished() {
		return command.isFinished();
	}

}
