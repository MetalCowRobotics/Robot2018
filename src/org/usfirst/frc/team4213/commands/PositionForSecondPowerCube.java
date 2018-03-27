package org.usfirst.frc.team4213.commands;

import java.util.ArrayList;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.lib14.SequentialCommands;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.RobotMap.Autonomous;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class PositionForSecondPowerCube {
	public static MCRCommand getCommandsFromSwitchFront(Hand lastPositionSide) {
		ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
		missionSteps.add(new CommandDriveBackwards(-12, 1.5));
		missionSteps.add(new ParallelCommands(
				new CommandTurn(turnDirection(lastPositionSide), 1.5),
				new CommandRaiseElevator(RobotMap.Elevator.EXCHANGE_HEIGHT)));
		missionSteps.add(new CommandDriveBackwards(-48, 3));
		missionSteps.add(new CommandTurn(turnDirection(lastPositionSide), 1.5));
		missionSteps.add(new CommandDriveBackwards(-60, 4));
		MCRCommand[] a = new MCRCommand[missionSteps.size()];
		return new SequentialCommands((MCRCommand[]) missionSteps.toArray(a));
	}

	public static MCRCommand getCommandsFromSwitchEnd(Hand lastPositionSide) {
		ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
		missionSteps.add(new CommandDriveBackwards(-12, 1.5));
		missionSteps.add(new ParallelCommands(
				new CommandTurn(turnDirection(lastPositionSide), 1.5),
				new CommandRaiseElevator(RobotMap.Elevator.EXCHANGE_HEIGHT)));
		missionSteps.add(new CommandDriveBackwards(-30, 3));
		MCRCommand[] a = new MCRCommand[missionSteps.size()];
		return new SequentialCommands((MCRCommand[]) missionSteps.toArray(a));
	}
	
	private static double turnDirection(Hand side) {
		return Hand.kRight.equals(side) ? Autonomous.leftTurn : Autonomous.rightTurn;
	}

}
