/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Spark;

/**
 * Add your docs here.
 */
public class Elevator {
    Spark motor1;
    Spark motor2;

    public Elevator(int port1, int port2){
        motor1 = new Spark(port1);
        motor2 = new Spark(port2);

        motor2.setInverted(true);
    }

    public void moveUp(){
        motor1.set(1.0);
        motor2.set(1.0);
    }

    public void moveDown(){
        motor1.set(-1.0);
        motor2.set(-1.0);
    }

    public void stop(){
        motor1.stopMotor();
        motor2.stopMotor();
    }

    public void set(double value){
        motor1.set(value);
        motor2.set(value);
    }
}
