package org.usfirst.frc.team4213.commands;

import java.util.ArrayList;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.lib14.SequentialCommands;
import org.usfirst.frc.team4213.robot.RobotMap;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class RightSideToRightSwitchMission implements MCRCommand {
	private MCRCommand command;

	public RightSideToRightSwitchMission(Hand switchSide) {
		ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
		missionSteps.add(new CommandIntakeDeploy());
		if (Hand.kRight == switchSide) {
			missionSteps.add(new ParallelCommands(
					new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT),
					new CommandDriveStraight(RobotMap.Autonomous.switchWallDistance, 4)));
			missionSteps.add(new CommandEjectCube());
//			missionSteps.add(new CommandDriveToObject(RobotMap.Autonomous.wallBackOff));
//			missionSteps.add(new ParallelCommands(
//								new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT),
//								new CommandDriveToObject(RobotMap.Autonomous.wallBackOff)));
		} else {
			missionSteps.add(new CommandDriveStraight(RobotMap.Autonomous.switchWallDistance, 4));
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
