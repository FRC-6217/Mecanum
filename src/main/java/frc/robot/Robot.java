// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

/** This is a demo program showing how to use Mecanum control with the MecanumDrive class. */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 32;
  private static final int kRearLeftChannel = 33;
  private static final int kFrontRightChannel = 31;
  private static final int kRearRightChannel = 30;

  private static final int kJoystickChannel = 0;

  private MecanumDrive m_robotDrive;
  private Joystick m_stick;
  private final Timer m_timer = new Timer();

  @Override
  public void robotInit() {
    CANSparkMax frontLeft = new CANSparkMax(kFrontLeftChannel, MotorType.kBrushless);
    frontLeft.setIdleMode(IdleMode.kBrake);
    CANSparkMax rearLeft = new CANSparkMax(kRearLeftChannel, MotorType.kBrushless);
    rearLeft.setIdleMode(IdleMode.kBrake);
    CANSparkMax frontRight = new CANSparkMax(kFrontRightChannel, MotorType.kBrushless);
    frontRight.setIdleMode(IdleMode.kBrake);
    CANSparkMax rearRight = new CANSparkMax(kRearRightChannel, MotorType.kBrushless);
    rearRight.setIdleMode(IdleMode.kBrake);

    // Invert the right side motors to run the motors in the same direction
    frontRight.setInverted(true);
    rearRight.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick = new Joystick(kJoystickChannel);
  }

  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
  }

  @Override
  public void autonomousPeriodic() {
    // Drive around
    if (m_timer.get() < 4.0) {
      m_robotDrive.driveCartesian(0.1, 0.0, 0.0); // drive forwards
    }
    else if (m_timer.get() > 5.0 && m_timer.get() < 9.0) {
      m_robotDrive.driveCartesian(0.0, 0.1, 0.0); // drive sideways
    }
    else if (m_timer.get() > 10.0 && m_timer.get() < 14.0) {
      m_robotDrive.driveCartesian(0.0, 0.0, 0.1); // spin
    }
    else {
      m_robotDrive.stopMotor(); // stop robot
    }
  }

  @Override
  public void teleopPeriodic() {
    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    double gov = (1-m_stick.getRawAxis(3))/2;
    double xSpeed = (Math.abs(m_stick.getRawAxis(0)) < 0.3) ? 0.0 : (-m_stick.getRawAxis(0) * gov);
    double ySpeed = (Math.abs(m_stick.getRawAxis(1)) < 0.3) ? 0.0 : (-m_stick.getRawAxis(1) * gov);
    double zSpeed = (Math.abs(m_stick.getRawAxis(2)) < 0.3) ? 0.0 : (m_stick.getRawAxis(2) * gov);
    m_robotDrive.driveCartesian(ySpeed, xSpeed, zSpeed, 0.0);
    
  }
}
