package org.usfirst.frc.team4213.robot.systems;

import org.usfirst.frc.team4213.robot.controllers.MasterControls;

public class Elevator {
	private MasterControls controller;
	
	//TODO: Motors
	
	//TODO: Sensors
	
	private enum ElevatorState { BOTTOM, MOVINGUP, MOVINGDOWN, TOP}
	ElevatorState elevatorState = ElevatorState.BOTTOM;

	
	public Elevator(MasterControls controller) {
		this.controller = controller;
	}
	
	public void execute() {

	}
	
	private void elevatorUp() {

	}

	private void elevatorDown() {
		
	}
	
}

