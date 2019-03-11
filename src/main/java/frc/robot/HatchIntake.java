/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/**
 * Class for interfacing our 2019 hatch intake.
 */
public class HatchIntake {

    DoubleSolenoid hatchSolenoid;

    /**
     * Create a hatch intake by providing the two PCM ports for a double solenoid.
     * @param solenoidPort1
     * @param solenoidPort2
     */
    public HatchIntake(int solenoidPort1, int solenoidPort2){
        hatchSolenoid = new DoubleSolenoid(solenoidPort1, solenoidPort2);
    }

    /**
     * Move the intake (HatchState.IN or HatchState.OUT).
     * @param hatchState
     */
    public void set(HatchState hatchState){
        hatchSolenoid.set(hatchState == HatchState.IN ? Value.kReverse : Value.kForward);
    }

    /**
     * Enum for holding possible hatch intake states.
     */
    public enum HatchState {
        OUT,
        IN;
    }
}
