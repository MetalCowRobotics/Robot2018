package org.usfirst.frc.team4213.commands;

import java.util.ArrayList;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.lib14.SequentialCommands;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.RobotMap.Autonomous;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class RightSideToLeftSwitchMission implements MCRCommand {
	private MCRCommand command;

	public RightSideToLeftSwitchMission(Hand switchSide) {
		ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
		missionSteps.add(new CommandIntakeDeploy());
		missionSteps.add(new CommandDriveStraight(Autonomous.clearExchangeDistance));
		missionSteps.add(new CommandTurn(Autonomous.leftTurn));
		missionSteps.add(new CommandDriveStraight(Autonomous.rightSideToLeftSideDistance));
		missionSteps.add(new CommandTurn(Autonomous.rightTurn));
		if (Hand.kLeft == switchSide) {
			missionSteps.add(new ParallelCommands(new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT),
					new CommandDriveToObject(Autonomous.wallBackOff)));
			missionSteps.add(new CommandEjectCube());
		} else {
			missionSteps.add(new CommandDriveStraight(Autonomous.switchWallDistance));
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
