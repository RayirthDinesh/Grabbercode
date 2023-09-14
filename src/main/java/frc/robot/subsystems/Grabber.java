// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
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
  GenericEntry flag = Shuffleboard.getTab("Grabber").add("Flag", 0).getEntry();
  static Timer timer = new Timer(); 

  public Grabber() {
    grabberMotor2.follow(grabberMotor1, true);
    grabberMotor1.setIdleMode(IdleMode.kCoast);
  }

  public enum States{
    NOTHING,
    PULL_START,
    PULL_TIME_ELAPSING,
    PULLING,
    PULLED,
    PUSH_START,
    PUSHING_TIME_ELAPSING,
    PUSHING,
    PUSHED,
    BUTTON_RELEASED;

  }


  public static States state = States.NOTHING;
  GenericEntry StateEntry = Shuffleboard.getTab("Grabber").add("State", 0).getEntry();

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
    if(timer.hasElapsed(3)){
      timer.stop();
      timer.reset();
    
      if (state == States.PULL_TIME_ELAPSING)
    {
      state = States.PULLING;
    }
    else if (state == States.PUSHING_TIME_ELAPSING){
      state = States.PUSHING;
      // state = States.CLOSING_CURRENT;

    }
  }
}

  @Override
  public void periodic() {

    // This method will be called once per scheduler run
   
    if(DriverStation.isEnabled()) {
      outputCurrent = getOutputCurrentGrabber1();
      OutputCurrentEntry.setDouble(outputCurrent);
      switch(state) {
        case NOTHING:
          StateEntry.setDouble(0);
          break;
        case PULL_START:
          StateEntry.setDouble(1);
          percentOutput(Constants.GRABBER_MOVE_GAME_PIECE_SPEED);
          state = States.PULL_TIME_ELAPSING;
          System.out.println("Pull Start");
          break;
        case PULL_TIME_ELAPSING:
          StateEntry.setDouble(2);
          System.out.println("Pull elapse");
          firstCurrentPass();

          break;
        case PULLING:
        System.out.println("Pulling");

          StateEntry.setDouble(3);
          if (RobotContainer.coneMode.getAsBoolean()){
            if (Constants.CONE_OUTPUT_CURRENT_MAX <= getOutputCurrentGrabber1()) {
              flag.setDouble(1);
              state = States.PULLED;
              
            }
          }
          else{
            if (Constants.CUBE_OUTPUT_CURRENT_MAX <=  getOutputCurrentGrabber1()) {
              state = States.PULLED;
            }
          }
          break;

        case PULLED:
          System.out.println("Pulled");

          StateEntry.setDouble(4);
          grabberMotor1.setIdleMode(IdleMode.kBrake);

          stopMotor();
          //percentOutput(Constants.GRABBBER_CONSTANT_PERCENT_OUTPUT);
          break;
        case PUSH_START:
          System.out.println("Push Start");

          StateEntry.setDouble(5);
          percentOutput(-Constants.GRABBER_MOVE_GAME_PIECE_SPEED);
          state = States.PUSHING_TIME_ELAPSING;
          break;
        case PUSHING_TIME_ELAPSING:
          System.out.println("Push elapse");

          firstCurrentPass();
          StateEntry.setDouble(6);
         break;
        case PUSHING:
          System.out.println("Pushing");

          StateEntry.setDouble(7);
          if (Constants.GRABBER_EMPTY_OUTPUT_MAX  <= getOutputCurrentGrabber1() ){
            state = States.PUSHED;
            flag.setDouble(1);
          }
          break;
        case PUSHED:
          System.out.println("Pushed");

          StateEntry.setDouble(8);
          grabberMotor1.setIdleMode(IdleMode.kBrake);
          stopMotor();
          break;
        case BUTTON_RELEASED:
          StateEntry.setDouble(8);
          stopMotor();
          break;
        default:
          StateEntry.setDouble(9);
          System.out.print("something went wrong");
          break;
          
      }
  }
 }
}