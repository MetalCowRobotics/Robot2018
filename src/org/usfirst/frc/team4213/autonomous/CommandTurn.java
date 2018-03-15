package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;

public class CommandTurn implements MCRCommand {
	private TurnDegrees thisCommand;

	public CommandTurn(double turnDegrees) {
		thisCommand = new TurnDegrees(turnDegrees);
	}

	@Override
	public void run() {
		thisCommand.run();
	}

	@Override
	public boolean isFinished() {
		return thisCommand.isFinished();
	}

}
