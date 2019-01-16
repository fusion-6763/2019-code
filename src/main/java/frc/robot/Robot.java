/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private static final String TANK_DRIVE = "Tank Drive";
    private static final String MECANUM_DRIVE = "Mecanum Drive";
    private String driveSelected;
    private final SendableChooser<String> m_chooser = new SendableChooser<>();
    private XboxController controller = new XboxController(1);
    private Talon frontLeftMotor = new Talon(0);
    private Talon frontRightMotor = new Talon(1);
    private Talon rearRightMotor = new Talon(2);
    private Talon rearLeftMotor = new Talon(3);
    private MecanumDrive mecanumDrive = new MecanumDrive(frontLeftMotor, rearLeftMotor, frontRightMotor,
            rearRightMotor);
    private Timer timer = new Timer();
    private AHRS navx = new AHRS(SPI.Port.kMXP);
    private DifferentialDrive tankDrive = new DifferentialDrive(frontLeftMotor, frontRightMotor);

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        m_chooser.setDefaultOption("Mecanum Drive", MECANUM_DRIVE);
        m_chooser.addOption("Tank Drive", TANK_DRIVE);
        SmartDashboard.putData("Drive choices", m_chooser);

        navx.reset();
        timer.stop();
        timer.reset();
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
        System.out.println("gyro angle = " + navx.getAngle() );
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
        driveSelected = m_chooser.getSelected();
        timer.start();
        System.out.println("Auto selected: " + driveSelected);
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        switch (driveSelected) {
        case MECANUM_DRIVE:
            if (timer.get() < 1) {
                // its suposed to go forward
                mecanumDrive.drivePolar(0.8, 0, 0);
            } 
            else if (timer.get() < 2) {
                // its suposed to go left
                mecanumDrive.drivePolar(0.8, -90, 0);
            } 
            else if (timer.get() < 3) {
                // its supposed to go right
                mecanumDrive.drivePolar(0.8, 90, 0);
            } 
            else if (timer.get() < 4) {
                // supposed to go backward
                mecanumDrive.drivePolar(0.8, 180, 0);
            } 
            else {
                mecanumDrive.stopMotor();
            }
            break;
        case TANK_DRIVE:
        default:
            // Put default auto code here
            break;
        }
    }

    /**
     * This function is called at the start of the operator control period.
     */
    @Override
    public void teleopInit() {
        super.teleopInit();

        driveSelected = m_chooser.getSelected();
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        if (driveSelected.equals(MECANUM_DRIVE)) {

            // To use polar drive, uncomment this line and comment out the driveCartesian line.
            mecanumDrive.drivePolar(getJoystickMagnitude(), getJoystickAngle(Hand.kLeft), getJoystickAngle(Hand.kRight));

            // To use cartesian drive, uncomment this line and comment out the drivePolar line.
            //mecanumDrive.driveCartesian(-controller.getY(Hand.kLeft), controller.getX(Hand.kLeft), getJoystickAngle(Hand.kRight));
        } else {
            tankDrive.tankDrive(-controller.getY(Hand.kLeft), controller.getY(Hand.kRight));
        }
    }

    private double getJoystickMagnitude() {
        double absoluteY = Math.abs(controller.getY(Hand.kLeft));
        double absoluteX = Math.abs(controller.getX(Hand.kLeft));
        
        return Math.sqrt(Math.pow(absoluteY, 2) + Math.pow(absoluteX, 2));
    } 

    private double getJoystickAngle(final Hand hand) {

        // This assumes the angle is from the postive side of the X axis.
        return Math.atan2(-controller.getY(hand), controller.getX(hand));
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
