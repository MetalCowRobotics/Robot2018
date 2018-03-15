package org.usfirst.frc.team4213.commands;

import java.util.ArrayList;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.lib14.SequentialCommands;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.RobotMap.Autonomous;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class SwitchEndMission implements MCRCommand {
	private MCRCommand command;

	public SwitchEndMission(Hand mySide, Hand switchSide) {
		ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
		missionSteps.add(new CommandIntakeDeploy());
		missionSteps.add(new CommandDriveStraight(Autonomous.middleSwitchDistance));
		if (mySide.equals(switchSide)) {
			missionSteps.add(new CommandTurn(turnDirection(mySide)));
			missionSteps.add(new ParallelCommands(new CommandDriveToObject(Autonomous.wallBackOff),
					// new CommandDrive(Autonomous.distanceToSwitchEnd),
					new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT)));
			missionSteps.add(new CommandEjectCube());
			MCRCommand[] a = new MCRCommand[missionSteps.size()];
			command = new SequentialCommands((MCRCommand[]) missionSteps.toArray(a));
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

	private double turnDirection(Hand side) {
		return Hand.kRight.equals(side) ? Autonomous.leftTurn : Autonomous.rightTurn;
	}
}