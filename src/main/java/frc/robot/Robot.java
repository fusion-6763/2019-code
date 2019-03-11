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
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.HatchIntake.HatchState;
import edu.wpi.first.wpilibj.Joystick;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.cameraserver.*;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;

import edu.wpi.first.wpilibj.Compressor;

import edu.wpi.first.networktables.NetworkTableEntry;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    //private final SendableChooser<String> m_chooser = new SendableChooser<>();
    Joystick driver = new Joystick(0);
    Joystick controller = new Joystick(1);

    AHRS navx = new AHRS(SPI.Port.kMXP);

    DifferentialDrive myRobot = new DifferentialDrive(new Spark(0), new Spark(2));

    Elevator elevator = new Elevator(4, 6);
    CargoIntake cargoIntake = new CargoIntake(9);
    HatchIntake hatchIntake = new HatchIntake(0, 1);

    AnalogInput pixyAnalog = new AnalogInput(3);
    DigitalInput pixyDigital = new DigitalInput(0);

    Compressor pressorOfCom = new Compressor();

    HatchState hatchState = HatchState.IN;

    double value;
    double finalXSpeed;
    double currentSpeed = 0.0;

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        // SmartDashboard.putData("Drive choices", m_chooser);
        navx.reset();
        CameraServer.getInstance().startAutomaticCapture();

        SmartDashboard.putBoolean("Hatch out?", false);
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
        pressorOfCom.start();

        double joystick = -driver.getY();
        if(joystick > currentSpeed + 0.1 || joystick < currentSpeed - 0.1){
            if(joystick < currentSpeed){
                currentSpeed -= 0.1;
            }
            else{
                currentSpeed += 0.1;
            }
            System.out.println("Apples of pines.");
        }
        System.out.println(currentSpeed);
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
        hatchState = HatchState.IN;
        hatchIntake.set(hatchState);
    }
    
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
        if(driver.getRawButton(1)){
            if(pixyDigital.get()){
                myRobot.arcadeDrive(-driver.getY(), -finalXSpeed);
            }
            else{
                myRobot.tankDrive(0.0, 0.0);
            }
        }
        else{
            myRobot.arcadeDrive(-driver.getRawAxis(5), driver.getRawAxis(4));
        }

        // Elevator

        elevator.set(-controller.getY());

        // Cargo intake

        if(controller.getRawButton(1)){
            cargoIntake.outtake();
        }
        else if(controller.getRawButton(2)){
            cargoIntake.intake();
        }
        else{
            cargoIntake.normal();
        }

        // Hatch intake

        if(controller.getRawButtonPressed(3) || controller.getRawButtonPressed(4)){
            hatchState = hatchState == HatchState.IN ? HatchState.OUT : HatchState.IN;
            hatchIntake.set(hatchState);
            //System.out.println("Pressed");
        }
        /* if(controller.getRawButton(3) || controller.getRawButton(4)){
            hatchState = HatchState.OUT;
        }
        else{
            hatchState = HatchState.IN;
        }
        hatchIntake.set(hatchState); */

        // Driver displays

        System.out.println(hatchState);
        SmartDashboard.putBoolean("Hatch out?", hatchState == HatchState.OUT ? true : false);
    }

    /**
     * This function is called at the start of the operator control period.
     */
    @Override
    public void teleopInit() {
        hatchState = HatchState.IN;
        hatchIntake.set(hatchState);
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
        if(driver.getRawButton(1)){
            if(pixyDigital.get()){
                myRobot.arcadeDrive(-driver.getY(), -finalXSpeed);
            }
            else{
                myRobot.tankDrive(0.0, 0.0);
            }
        }
        else{
            //myRobot.arcadeDrive(-driver.getRawAxis(5), driver.getRawAxis(4));
        }

        // Elevator

        elevator.set(-controller.getY());

        // Cargo intake

        if(controller.getRawButton(1)){
            cargoIntake.outtake();
        }
        else if(controller.getRawButton(2)){
            cargoIntake.intake();
        }
        else{
            cargoIntake.normal();
        }

        // Hatch intake

        if(controller.getRawButtonPressed(3) || controller.getRawButtonPressed(4)){
            hatchState = hatchState == HatchState.IN ? HatchState.OUT : HatchState.IN;
            hatchIntake.set(hatchState);
            //System.out.println("Pressed");
        }
        /* if(controller.getRawButton(3) || controller.getRawButton(4)){
            hatchState = HatchState.OUT;
        }
        else{
            hatchState = HatchState.IN;
        }
        hatchIntake.set(hatchState); */

        // Driver displays

        System.out.println(hatchState);
        SmartDashboard.putBoolean("Hatch out?", hatchState == HatchState.OUT ? true : false);

        double joystick = driver.getY();
        if(Math.abs(joystick) > Math.abs(currentSpeed)){
            currentSpeed += joystick < currentSpeed ? -0.1 : 0.1;
        }
        System.out.println(currentSpeed);
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}