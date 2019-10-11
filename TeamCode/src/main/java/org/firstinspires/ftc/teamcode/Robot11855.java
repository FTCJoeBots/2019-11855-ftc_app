package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

import static java.lang.StrictMath.abs;

/**
 * This is NOT an opmode. This is a hardware class used to abstract the hardware config for the
 * 2018 JoeBots FTC Rover Ruckus challenge. This file has been generalized to work as a base for
 * all three JoeBots FTC teams (8513, 11855, and 13702). As the season progresses, this file may be
 * customized for each individual team in their own branch.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 *
 * motor0 (left front)
 * motor1 (right front)
 * motor2 (left rear)
 * motor3 (right rear)
 * imu - navigation features
 *
 * Note:  All names are lower case and some have single spaces between words.
 *
 */

public class Robot11855 {
    /* Public OpMode members. */

    // Declare Motors
    public DcMotor turrentMotor = null;
    public DcMotor shoulderMotor = null;
    public DcMotor ristMotor = null;

    // Declare Sensors
    public Servo clampServo = null;


    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    // Private Members
    private LinearOpMode myOpMode;
    private ElapsedTime runtime = new ElapsedTime();


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //Servo Consents
    static final double SERVO_MIN = 0.02;
    static final double SERVO_MAX = 0.99;

    // Declare Static members for calculations
    //static final double COUNTS_PER_MOTOR_REV    = 1120;
    static final double COUNTS_PER_MOTOR_REV = 780;

    static final double DRIVE_GEAR_REDUCTION = 1;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);
    static final double LIFT_THREADS_PER_INCH = 0.948;
    static final double LIFT_GEAR_REDUCTION = 1;
    static final double LIFT_COUNTS_PER_MOTOR_REV = 4.0;
    static final double LIFT_COUNTS_PER_INCH = (LIFT_THREADS_PER_INCH * LIFT_GEAR_REDUCTION * LIFT_COUNTS_PER_MOTOR_REV);

    /* Constructor */
    public Robot11855() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, LinearOpMode opMode) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        myOpMode = opMode;

        // Define and Initialize Motors
        turrentMotor = hwMap.dcMotor.get("motor0");
        shoulderMotor = hwMap.dcMotor.get("motor1");
        ristMotor = hwMap.dcMotor.get("motor2");

        //Define and Intialize Servos

        clampServo = hwMap.servo.get("clampServo");
        clampServo.setPosition(clampServo.MIN_POSITION);

        // Set Default Motor Directions
        turrentMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        shoulderMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        ristMotor.setDirection(DcMotorSimple.Direction.FORWARD);


        // Set all motors to zero power
        turrentMotor.setPower(0);
        shoulderMotor.setPower(0);
        ristMotor.setPower(0);

        myOpMode.telemetry.addLine("initialized motor power to zero");
        myOpMode.telemetry.update();

        myOpMode.telemetry.addLine("initialized other motor power to zero");
        myOpMode.telemetry.update();

        // Set all drive motors to run without encoders.
        // May want to switch to  RUN_USING_ENCODERS during autonomous

        turrentMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shoulderMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ristMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        ////////////////////////////////////////////////////////////////////////////////////////


    }


// Method to set all motors power to zero

    public void stop() {

        turrentMotor.setPower(0);
        shoulderMotor.setPower(0);
        ristMotor.setPower(0);


    }




    // Servo open and close method

    public void servoOpen() {
        clampServo.setPosition(Servo.MAX_POSITION);

    }




    public void servoClose ()
    {
        clampServo.setPosition(Servo.MIN_POSITION);


    }

}












