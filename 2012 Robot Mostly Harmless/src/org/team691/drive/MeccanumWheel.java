package org.team691.drive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;

/**
 * A wheel for the Meccanum drive system.
 * 
 * @author Akira, Gerard
 */
public class MeccanumWheel {
    public static final int FR_WHEEL = 1;
    public static final int FL_WHEEL = 2;
    public static final int BL_WHEEL = 3;
    public static final int BR_WHEEL = 4;
    
    PIDControlledVelocityMotor wheelMotor = null;
    int wheelNum;
    double mag    = 0.0;
    double scaler = 1;

    /**
     * Creates a new MeccanumWheel object.
     * @param motorIn   The motor-encoder pair that this object controls
     * @param wheel     The wheel position. Use {@code Meccanum.FR, .FL, .BR, .BL}
     */
    public MeccanumWheel(PIDControlledVelocityMotor motorIn, int wheel){
        wheelMotor = motorIn;
        wheelNum   = wheel;
    }

    /**
     * Constructor for the MeccanumWheel object. This method is used for
     * backwards compatability only.
     * 
     * @param cRIOSlot      The slot for the cRIO.
     * @param channelNum    Channel number of the SpeedController for the wheel.
     * @param enc           The encoder for the wheel.
     * @param wheel The wheel number (1=Front Right, 2=Front Left, 3=Back
     * Left, 4=Back Right).
     */
    public MeccanumWheel(int cRIOSlot, int channelNum, Encoder enc, int wheel){
        this.wheelNum = wheel;

        Jaguar WheelJaguar = new Jaguar(cRIOSlot, channelNum );

        wheelMotor = new PIDControlledVelocityMotor( WheelJaguar, enc, scaler );
    }

    /**
     * Update the magnitude needed to move this wheel.
     * @param F Value for forward movement.
     * @param R Value for strafing.
     * @param C Value for turning clockwise.
     */
    public double update(double F, double R, double C){
        // setMotor is called in the MeccanumDrive class after the values are
        // modified.
        return (calc( F, R, C ));
    }

    /**
     * Calculate the magnitude for each wheel.
     * @param F (Forward) The value from the left joystick's Y-Axis from
     * -1.0 to 1.0.
     * @param R (Right) The value from the left joystick's X-Axis from
     * -1.0 to 1.0.
     * @param C (Clockwise) The value from the right joysticks' X-Axis from
     * -1.0 to 1.0.
     * @return The magnitude the wheel's motor should have from -1.0 to 1.0.
     */
    public double calc(double F, double R, double C){
        switch(wheelNum){
            case 1 :
                mag = 1 * ( F - R - C );
                break; // front right wheel
            case 2 :
                mag = 1 * ( F + R + C );
                break; // front left wheel
            case 3 :
                mag = 1 * ( F - R + C );
                break; // back left wheel
            case 4 :
                mag = 1 * ( F + R - C );
                break; // back right wheel
            default:
                mag = 0;
                System.out.println("Error: Bad wheel");
        }

         //Jaguars can't accept values outside of (-1,1)
        if (mag >  1){mag =  1;}
        else if (mag < -1){mag = -1;}

        return mag;
    }

    /**
     * Sets the motor speed.
     * @param FMag Final magnitude of the motor from -1.0 to 1.0.
     */
    public void setMotor(double FMag)
    {
        wheelMotor.setTargetVelocity( FMag );
        wheelMotor.update();
    }

    /**
     * Gets the magnitude.
     * @return The magnitude from -1.0 to 1.0.
     */
    public double getMagnitude()
    {
        return mag;
    }

    /**
     * Provides a way to read the encoder speed of the wheel.
     * @return The encoder speed of this wheel in a human
     * readable string.
     */
    public String toString()
    {
        return "" + wheelMotor.getEncoderSpeed();
    }
    
    public void enable()
    {
        wheelMotor.enable();
    }
    public void disable()
    {
        wheelMotor.disable();
    }
} 

//FIRST FRC team 691 2012 competition code
