/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Joystick;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.cameraserver.*;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    //private final SendableChooser<String> m_chooser = new SendableChooser<>();
    Joystick controller = new Joystick(0);
    Joystick vroom = new Joystick(1);
    AHRS navx = new AHRS(SPI.Port.kMXP);
    DifferentialDrive myRobot = new DifferentialDrive(new Spark(0), new Spark(2));

    Elevator elevator = new Elevator(7, 8);

    AnalogInput pixyAnalog = new AnalogInput(3);
    DigitalInput pixyDigital = new DigitalInput(0);

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        // SmartDashboard.putData("Drive choices", m_chooser);
        navx.reset();
        CameraServer.getInstance().startAutomaticCapture();
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like diagnostics that you want ran during disabled, autonomous,
     * teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        //System.out.println("Gyro Angle: " + navx.getAngle());
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable chooser
     * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
     * remove all of the chooser code and uncomment the getString line to get the
     * auto name from the text box below the Gyro
     *
     * <p>
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the SendableChooser
     * make sure to add them to the chooser code above as well.
     */
    @Override
    public void autonomousInit() {

    }
    double value;
    double finalXSpeed;
    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        value = pixyAnalog.getVoltage() * 30.30;
    
        double difference = value - 50;
        double cookie = difference / 50;
        double pineapple = cookie * 0.6;

        if(pineapple < 0){
            finalXSpeed = pineapple - 0.4;
        }
        else if(pineapple > 0){
            finalXSpeed = pineapple + 0.4;
        }
        if(controller.getRawButton(1)){
            if(pixyDigital.get()){
                myRobot.arcadeDrive(-controller.getY(), -finalXSpeed);
            }
            else{
                myRobot.tankDrive(0.0, 0.0);
            }
        }
        else{
            myRobot.arcadeDrive(-controller.getY(), controller.getX());
        }
    }

    /**
     * This function is called at the start of the operator control period.
     */
    @Override
    public void teleopInit() {

    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        value = pixyAnalog.getVoltage() * 30.30;
    
        double difference = value - 50;
        double cookie = difference / 50;
        double pineapple = cookie * 0.6;

        if(pineapple < 0){
            finalXSpeed = pineapple - 0.4;
        }
        else if(pineapple > 0){
            finalXSpeed = pineapple + 0.4;
        }
        if(controller.getRawButton(1)){
            if(pixyDigital.get()){
                myRobot.arcadeDrive(-controller.getY(), -finalXSpeed);
            }
            else{
                myRobot.tankDrive(0.0, 0.0);
            }
        }
        else{
            myRobot.arcadeDrive(-controller.getY(), controller.getX());
        }


        //Outtake and intake motors
        if (vroom.getRawButton(1)) {
            //Outtake
        }
        else if (vroom.getRawButton(2)) {
            //Intake
        }
        else {
            //stop motor!
        }

        //Elevator motors: More code in the Elevator.java file
        //double leverage = 0.1; //Reminder: getY values range from -1 to 1
        /*if (vroom.getY() < 0 - leverage ) {
            elevator.moveUp();
        }
        else if (vroom.getY() > 0 + leverage) {
            elevator.moveDown();
        }
        else {
            elevator.stop();
        }*/
        elevator.set(vroom.getY());

        //Toggle the hatch-grabber piston
        boolean hatchToggle = false;
        if (vroom.getRawButtonPressed(3) || vroom.getRawButtonPressed(4)){
            hatchToggle = !hatchToggle;
        }
        if (hatchToggle) {
            //Extendo hatcho!
        }
        else{
            //unextendo hatcho y cookieso y vacas!
        }

    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
