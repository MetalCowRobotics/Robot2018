package org.usfirst.frc.team4213.commands;

import java.util.ArrayList;

import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.lib14.SequentialCommands;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.RobotMap.Autonomous;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class AngleSwitchMission implements MCRCommand {
	private ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
	private MCRCommand mission;

	public AngleSwitchMission(Hand mySwitchSide) {
		missionSteps.add(new CommandIntakeDeploy());
		if (Hand.kRight.equals(mySwitchSide)) {
			missionSteps.add(new CommandTurn(Autonomous.angleTurn));
			missionSteps.add(new ParallelCommands(
					new CommandDriveStraight(RobotMap.Autonomous.switchWallDistance, 4),
					new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT)));
		} else {
			missionSteps.add(new CommandDriveStraight(Autonomous.rightSideToLeftSideAngleDistance, 5));
			missionSteps.add(new ParallelCommands(
					new CommandTurn(Autonomous.angleTurn),
					new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT)));
			missionSteps.add(new CommandDriveStraight(12, 2));
		}
		missionSteps.add(new CommandEjectCube());
		MCRCommand[] a = new MCRCommand[missionSteps.size()];
		mission = new SequentialCommands((MCRCommand[]) missionSteps.toArray(a));
	}

	@Override
	public void run() {
		mission.run();
	}

	@Override
	public boolean isFinished() {
		return mission.isFinished();
	}
}
