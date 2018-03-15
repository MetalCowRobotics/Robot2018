package org.usfirst.frc.team4213.commands;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.robot.RobotMap;

public class PassLineMission implements MCRCommand {
	private MCRCommand command = new ParallelCommands(new CommandIntakeDeploy(),
			new CommandDriveStraight(RobotMap.Autonomous.passLineDistance));

	@Override
	public void run() {
		command.run();
	}

	@Override
	public boolean isFinished() {
		return command.isFinished();
	}

}
