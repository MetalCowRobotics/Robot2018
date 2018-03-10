package org.usfirst.frc.team4213.autonomous;

import org.usfirst.frc.team4213.robot.systems.AutoDrive;
import org.usfirst.frc.team4213.robot.systems.DriveToWall;
import org.usfirst.frc.team4213.robot.systems.DriveWithEncoder;
import org.usfirst.frc.team4213.robot.systems.TurnDegrees;
import org.usfirst.frc.team4213.robot.RobotMap;

public class AngledRight extends Mission {
	private enum MissionStates {
		waiting, turning, reaching, reached, deploying, ejecting, done
	}

	private MissionStates curState = MissionStates.waiting;
	
	private AutoDrive driveDegrees;
	private AutoDrive driveStep;
	private AutoDrive driveToWall;

	public void execute() {
		switch (curState) {
		case waiting:
			driveDegrees = new TurnDegrees(45);
			driveToWall = new DriveToWall(13);
			intake.autoDeploy();
			curState = MissionStates.turning;
			break;
		case turning:
			driveDegrees.run();
			logger.info("Run Turn");
			if (driveDegrees.isFinished())
				curState = MissionStates.reaching;
			break;
		case deploying:
		if (elevator.isAtHeight(RobotMap.Elevator.SWITCHWALL_HEIGHT)) {
			curState = MissionStates.reaching;
		}
			break;
		case reaching:
			driveToWall.run();
			if (driveToWall.isFinished())
				curState = MissionStates.reached;
			break;
		case reached:
			intake.autoEject();
			curState = MissionStates.ejecting;
			break;
		case ejecting:
			//intake.execute();
			if (!intake.isIntakeRunning()) {
				curState = MissionStates.done;
			}
			break;
		case done:
			// turn stuff off an prepare for teleop
			break;
		default:
			break;
		}
	}
}