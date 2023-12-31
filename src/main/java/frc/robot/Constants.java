// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  //motor ports
  public final static int GRABBER_PORT_ONE = 17;
  public final static int GRABBER_PORT_TWO = 18;

  

  //Output current 
  //needs testing

  //Amps
  public final static double CUBE_OUTPUT_CURRENT_MAX = 10;
  
  public final static double GRABBER_EMPTY_OUTPUT_MAX = 10;
  public final static double CONE_OUTPUT_CURRENT_MAX = 10;

  //percent output
  public final static double GRABBBER_CONSTANT_PERCENT_OUTPUT = 0.125;
  public final static double GRABBER_MOVE_GAME_PIECE_SPEED = 0.3;


  

}
