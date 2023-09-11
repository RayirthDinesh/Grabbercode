// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Grabber;
import frc.robot.subsystems.Grabber.States;

public class grabberPush extends CommandBase {
 
  public grabberPush() {
    addRequirements(RobotContainer.m_grabber);
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(RobotContainer.m_grabber);
  }

  Timer timer = new Timer();

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    timer.reset();
    if(Grabber.state != States.PUSHED){
      System.out.println("Pushing");
      Grabber.state = States.PUSH_START;
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
