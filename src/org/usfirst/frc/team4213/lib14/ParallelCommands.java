package org.usfirst.frc.team4213.lib14;

import java.util.ArrayList;

public class ParallelCommands implements MCRCommand {
	private ArrayList<MCRCommand> parallelCommands = new ArrayList<MCRCommand>();
	
	public ParallelCommands(MCRCommand... commands) {
		for(MCRCommand command : commands) {
			parallelCommands.add(command);
		}
	}

	@Override
	public void run() {
		int numTimes =0;
		for (MCRCommand command : parallelCommands) {
//			if (!command.isFinished()) {
				command.run();
				System.out.println("NUMBER OF TIMES : "+(numTimes+=1));				
			//}
		}
	}

	@Override
	public boolean isFinished() {
		for (MCRCommand command : parallelCommands) {
			if (!command.isFinished()) {
				return false;
			}
		}
		return true;
	}

}