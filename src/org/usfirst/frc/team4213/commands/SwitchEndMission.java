package org.usfirst.frc.team4213.commands;

import java.util.ArrayList;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.lib14.SequentialCommands;
import org.usfirst.frc.team4213.robot.HamburgerDashboard;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.RobotMap.Autonomous;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class SwitchEndMission implements MCRCommand {
	private MCRCommand command;

	public SwitchEndMission(Hand mySide, Hand switchSide) {
		ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
		missionSteps.add(new CommandIntakeDeploy());
		missionSteps.add(new CommandDriveStraight(Autonomous.middleSwitchDistance, 3));
		if (mySide.equals(switchSide)) {
			missionSteps.add(new ParallelCommands(
					new CommandTurn(turnDirection(mySide), 1.5),
					new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT)));
//			missionSteps.add(new CommandDriveToObject(Autonomous.wallBackOff));
			missionSteps.add(new CommandDriveStraight(Autonomous.distanceToSwitchEnd, 2));
			missionSteps.add(new CommandEjectCube());
			//do a secondary command set to position for a second power cube
			if (HamburgerDashboard.getInstance().doSecondaryMission()) {
				missionSteps.add(PositionForSecondPowerCube.getCommandsFromSwitchEnd(switchSide));
			}
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
