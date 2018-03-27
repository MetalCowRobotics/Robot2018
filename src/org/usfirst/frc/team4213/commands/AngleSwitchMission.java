package org.usfirst.frc.team4213.commands;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.usfirst.frc.team4213.autonomous.Mission;
import org.usfirst.frc.team4213.lib14.MCRCommand;
import org.usfirst.frc.team4213.lib14.ParallelCommands;
import org.usfirst.frc.team4213.lib14.SequentialCommands;
import org.usfirst.frc.team4213.robot.HamburgerDashboard;
import org.usfirst.frc.team4213.robot.RobotMap;
import org.usfirst.frc.team4213.robot.RobotMap.Autonomous;

import edu.wpi.first.wpilibj.GenericHID.Hand;

public class AngleSwitchMission implements MCRCommand {
	private static final Logger logger = Logger.getLogger(Mission.class.getName());
	private ArrayList<MCRCommand> missionSteps = new ArrayList<MCRCommand>();
	private MCRCommand mission;

	public AngleSwitchMission(Hand mySwitchSide) {
		logger.setLevel(RobotMap.LogLevels.missionClass);
		missionSteps.add(new CommandIntakeDeploy());
		if (Hand.kRight.equals(mySwitchSide)) {
			missionSteps.add(new ParallelCommands(
					new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT),
					new CommandTurn(Autonomous.rightAngleTurn, 2.0)));
			// missionSteps.add(new CommandTurn(Autonomous.angleTurn));
			missionSteps.add(new CommandDriveStraight(RobotMap.Autonomous.switchWallDistance, 1.8));
		} else {
			missionSteps.add(new CommandDriveStraight(Autonomous.rightSideToLeftSideAngleDistance, 3.3));
			missionSteps.add(new ParallelCommands(
					new CommandTurn(Autonomous.angleTurn, 2.0),
					new CommandRaiseElevator(RobotMap.Elevator.SWITCHWALL_HEIGHT)));
			missionSteps.add(new CommandDriveStraight(24, 1.7));
		}
		missionSteps.add(new CommandEjectCube());
		//do a secondary command set to position for a second power cube
		if (HamburgerDashboard.getInstance().doSecondaryMission()) {
			logger.info("Doing secondary mission!");
			missionSteps.add(PositionForSecondPowerCube.getCommandsFromSwitchFront(mySwitchSide));
		}
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
