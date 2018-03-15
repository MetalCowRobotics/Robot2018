package org.usfirst.frc.team4213.autonomous;

import java.util.ArrayList;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.lib14.SequentialCommands;
import org.usfirst.frc.team4213.robot.RobotMap;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class RightSideToLeftSwitchMission implements MCRCommand {
	private MCRCommand command;
	
	public RightSideToLeftSwitchMission(Hand switchSide) {
		ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
		missionSteps.add(new CommandIntakeDeploy());
		missionSteps.add(new CommandDrive(12));
		missionSteps.add(new CommandTurn(-90));
		missionSteps.add(new CommandDrive(84));
		missionSteps.add(new CommandTurn(90));
		if (Hand.kLeft==switchSide) {
			missionSteps.add(new ParallelCommands(
					new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT),
					new CommandDriveToObject(13))); 
			missionSteps.add(new CommandEjectCube());
		} else {
			missionSteps.add(new CommandDrive(48));
		}
		MCRCommand[] a = new MCRCommand[missionSteps.size()];
		command = new SequentialCommands((MCRCommand[]) missionSteps.toArray(a));
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
