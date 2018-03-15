package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;

public class PassLineMission implements MCRCommand {
	private MCRCommand command = new ParallelCommands(new CommandIntakeDeploy(), new CommandDrive(159));
	
	@Override
	public void run() {
		command.run();
	}

	@Override
	public boolean isFinished() {
		return command.isFinished();
	}

}
