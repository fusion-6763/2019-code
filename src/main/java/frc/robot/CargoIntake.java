/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Spark;

/**
 * Class for interfacing our 2019 cargo intake.
 */
public class CargoIntake {

    Spark motor;

    /**
     * Create a cargo intake by providing a Spark PWN port.
     * @param motorChannel
     */
    public CargoIntake(int motorChannel){
        motor = new Spark(motorChannel);
        motor.setInverted(true);
    }

    /**
     * Intake the ball. (Full speed)
     */
    public void intake(){
        motor.set(-1.0);
    }

    /**
     * Spit out the ball. (Full speed)
     */
    public void outtake(){
        motor.set(1.0);
    }

    /**
     * Manually set the speed.
     */
    public void set(double value){
        motor.set(value);
    }

    /**
     * Starts slow inward rotation.
     */
    public void normal(){
        motor.set(-0.2);
    }
}
