/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  WPI_TalonFX frontFX;
  WPI_TalonFX middleFX;
  WPI_TalonFX backFX;
  DigitalInput sensor1;
  DigitalInput sensor2;
  DigitalInput sensor3;
  DigitalInput sensor4;
  XboxController ctrl;
  boolean frontCheck = false;
  boolean backCheck = false;
  boolean middleCheck = false;
  boolean go = false;

  double defaultValue = 0.3;

  Double frontPower = 0.0;
  Double backPower = 0.0;
  Double midPower = 0.0;

  boolean firstBallCheck = false;
  boolean middleBallCheck = false;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    frontFX = new WPI_TalonFX(11);
    middleFX = new WPI_TalonFX(12);
    backFX = new WPI_TalonFX(13);
    sensor1 = new DigitalInput(0);
    sensor2 = new DigitalInput(2);
    sensor3 = new DigitalInput(3);
    ctrl = new XboxController(0);
    SmartDashboard.putNumber("motor speed", defaultValue);
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
    SmartDashboard.putBoolean("Front On/Off", frontCheck);
    SmartDashboard.putBoolean("Middle On/Off", middleCheck);
    SmartDashboard.putBoolean("Back On/Off", backCheck);
    SmartDashboard.putBoolean("Sequence On/Off", go);
    defaultValue = SmartDashboard.getNumber("motor speed", 0.3);

    if (!sensor1.get()) {
      SmartDashboard.putString("Sensor 1", "Object Detected");
    } else {
      SmartDashboard.putString("Sensor 1", "NO OBJECT");
    }
    if (!sensor2.get()) {
      SmartDashboard.putString("Sensor 2", "Object Detected");
    } else {
      SmartDashboard.putString("Sensor 2", "NO OBJECT");
    }
    if (!sensor3.get()) {
      SmartDashboard.putString("Sensor 3", "Object Detected");
    } else {
      SmartDashboard.putString("Sensor 3", "NO OBJECT");
    }
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

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    if (ctrl.getAButtonPressed()) {
      go = !go;
    }

    if (go) {
      if (sensor3.get()) {
        if (!sensor1.get()) {
          allMotorsSet(defaultValue);
        } else if (sensor1.get()) {
          allMotorsSet(0);
        }
      } else if (!sensor3.get()) {
        allMotorsSet(0);
      }

    } else {
      allMotorsSet(0);
    }

    //Shoot
    if (ctrl.getBButton()) {
      allMotorsSet(defaultValue);
    }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    if (ctrl.getXButton()) {
      frontCheck = true;
      frontPower = -defaultValue;
      frontFX.set(ControlMode.PercentOutput, frontPower);
    } else if (ctrl.getAButton()) {
      middleCheck = true;
      midPower = defaultValue;
      middleFX.set(ControlMode.PercentOutput, midPower);
    } else if (ctrl.getBButton()) {
      backCheck = true;
      backPower = -defaultValue;
      backFX.set(ControlMode.PercentOutput, backPower);
    } else {
      frontCheck = false;
      middleCheck = false;
      backCheck = false;
      frontPower = 0.0;
      backPower = 0.0;
      midPower = 0.0;
      frontFX.set(ControlMode.PercentOutput, ctrl.getX(Hand.kRight) * -defaultValue);
      backFX.set(ControlMode.PercentOutput, ctrl.getX(Hand.kRight) * -defaultValue);
      middleFX.set(ControlMode.PercentOutput, ctrl.getX(Hand.kRight) * defaultValue);
    }
  }

  public void allMotorsSet(double s) {
    frontFX.set(-s);
    backFX.set(-s);
    middleFX.set(s);

  }
}
