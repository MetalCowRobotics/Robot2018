package org.usfirst.frc.team4213.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HamburgerDashboard {
    private static org.usfirst.frc.team4213.robot.HamburgerDashboard ourInstance = new org.usfirst.frc.team4213.robot.HamburgerDashboard();

    public static org.usfirst.frc.team4213.robot.HamburgerDashboard getInstance() {
        return ourInstance;
    }

    private HamburgerDashboard() {
    }

    // Define Autonomous Missions
    private final String rightSide = "RightSide";
    private final String leftSide = "LeftSide";
    private final String passLine = "PassLine";
    private final String eitherSide = "eitherSide";
    private final String leftSideOfScale = "leftSideOfScale";
    private final String leftPosition = "LeftPosition";
    private final String rightPosition = "RightPosition";
    private final String middlePosition = "MiddlePosition";
    private SendableChooser<String> autoChooser = new SendableChooser<>();
    private String autoSelected = passLine;

    private PowerDistributionPanel pdp;
    private DriverStation driverStation;

    public void initializeDashboard() {
        pdp = new PowerDistributionPanel();
        driverStation = edu.wpi.first.wpilibj.DriverStation.getInstance();
        //CameraServer.getInstance().startAutomaticCapture();
    }

    public void pushAutonomousMissions() {
        // Load available Autonomous missions to the driverstation
        autoSelected = rightSide;
        autoChooser.addObject("RightSideSwitch", rightSide);
        autoChooser.addObject("LeftSideSwitch", leftSide);
        autoChooser.addDefault("PassLine", passLine);
        autoChooser.addObject("eitherSide", eitherSide);
        autoChooser.addObject("leftScale", leftSideOfScale );
        autoChooser.addObject("RightPosition", rightPosition);
        autoChooser.addObject("LeftPosition", leftPosition);
        autoChooser.addObject("MiddlePosition", middlePosition);

        SmartDashboard.putData("Auto choices", autoChooser);
     }

     public String getSelectedMision() {
         // autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
         return autoChooser.getSelected();
     }



}
