package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class SwitchOrScaleMySideMission implements MCRCommand {
	private MCRCommand command;

	public SwitchOrScaleMySideMission(Hand mySide, Hand switchSide, Hand scaleSide) {
		if (mySide.equals(switchSide)) {
			command = new SwitchEndMission(mySide, switchSide);
		} else if (mySide.equals(scaleSide)) {
			command = new ScaleEndMission(mySide, scaleSide);
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
