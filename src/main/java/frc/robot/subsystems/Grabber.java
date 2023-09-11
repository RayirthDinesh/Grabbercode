// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

public class Grabber extends SubsystemBase {
  /** Creates a new Grabber. */
  static CANSparkMax grabberMotor1 = new CANSparkMax(Constants.GRABBER_PORT_ONE, MotorType.kBrushless);
  static CANSparkMax grabberMotor2 = new CANSparkMax(Constants.GRABBER_PORT_TWO, MotorType.kBrushless);

  double outputCurrent = 0;
  GenericEntry OutputCurrentEntry = Shuffleboard.getTab("Grabber").add("Output Current", outputCurrent).getEntry();
  static Timer timer = new Timer(); 

  public Grabber() {
    grabberMotor2.follow(grabberMotor1, true);
  }

  public enum States{
    PULL_START,
    PULL_TIME_ELAPSING,
    PULLING,
    PULLED,
    PUSH_START,
    PUSHING_TIME_ELAPSING,
    PUSHING,
    PUSHED;

  }


  public static States state = States.PULL_START;

  public static void percentOutput(double output){
    grabberMotor1.getPIDController().setReference(output, ControlType.kDutyCycle);
  }
  // public static void percentOutputGrabber2(double output){
  //   grabberMotor2.getPIDController().setReference(output, ControlType.kDutyCycle);
  // }
  public static double getOutputCurrentGrabber1(){
    return grabberMotor1.getOutputCurrent();
  }

  public static void stopMotor(){
    grabberMotor1.getPIDController().setReference(0, ControlType.kDutyCycle);
  }


  public static void firstCurrentPass(){
    timer.start();
    if(timer.hasElapsed(0.5)){
      timer.stop();
      timer.reset();
      // state = States.CLOSING_CURRENT;
    } 
  }
  

  @Override
  public void periodic() {

    // This method will be called once per scheduler run
    System.out.println(grabberMotor2.getLastError());
    if(DriverStation.isEnabled()) {
      outputCurrent = getOutputCurrentGrabber1();
      OutputCurrentEntry.setDouble(outputCurrent);
      switch(state) {
        case PULL_START:
          percentOutput(-Constants.GRABBER_MOVE_GAME_PIECE_SPEED);
          state = States.PULL_TIME_ELAPSING;
          break;
        case PULL_TIME_ELAPSING:
          firstCurrentPass();
          state = States.PULLING;
          break;
        case PULLING:
          if (RobotContainer.coneMode.getAsBoolean()){
            if (Constants.CONE_OUTPUT_CURRENT == getOutputCurrentGrabber1()) {
              stopMotor();
              percentOutput(Constants.GRABBBER_CONSTANT_PERCENT_OUTPUT);
              state = States.PULLED;
            }
            
          }
          else{
            if (Constants.CUBE_OUTPUT_CURRENT == getOutputCurrentGrabber1()) {
              stopMotor();
              percentOutput(Constants.GRABBBER_CONSTANT_PERCENT_OUTPUT);
              state = States.PULLED;
            }
          }
          break;

        case PULLED:
          System.out.println("pulled");
          break;
        case PUSH_START:
          percentOutput(Constants.GRABBER_MOVE_GAME_PIECE_SPEED);
          state = States.PUSHING_TIME_ELAPSING;
          break;
        case PUSHING_TIME_ELAPSING:
          firstCurrentPass();
          state = States.PUSHING;
          break;
        case PUSHING:
          if (Constants.GRABBER_EMPTY_OUTPUT_MAX  > getOutputCurrentGrabber1() &&  Constants.GRABBER_EMPTY_OUTPUT_MIN < getOutputCurrentGrabber1() ){
            stopMotor();
            state = States.PUSHED;
          }
          break;
        case PUSHED:
          System.out.println("Pushed");
          break;
        default:
          System.out.print("something went wrong");
          break;
          
      }
  }
 }
}