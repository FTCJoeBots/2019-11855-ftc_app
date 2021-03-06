package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
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
import static java.lang.Thread.sleep;

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

public class HardwareJoeBot2019 {
    /* Public OpMode members. */

    // Declare Motors
    public DcMotor motor0 = null; // Left Front
    public DcMotor motor1 = null; // Right Front
    public DcMotor motor2 = null; // Left Rear
    public DcMotor motor3 = null; // Right Rear

    public DcMotor turretMotor = null;
    public DcMotor shoulderMotor = null;
    public DcMotor wristMotor = null;

    // Declare Limit Switches
    public DigitalChannel lsWristUp;
    public DigitalChannel lsWristDown;


    //Declare Servo

    public Servo clampServo = null;
    public Servo foundationClamp = null;

    public int grabberState = 1; //1=open; 2=mid; 3=close
    public boolean bFoundationClosed = false;

    // Declare Speed Limit Tracker
    public boolean speedLimitEnabled = false;

    public Servo capstoneServo = null;


    // Declare Sensors
    public BNO055IMU imu;                  // The IMU sensor object

    // Variables used for IMU tracking...
    public Orientation angles;
    public Acceleration gravity;
    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    // Private Members
    private LinearOpMode myOpMode;
    private ElapsedTime runtime = new ElapsedTime();
    private Orientation lastImuAngles = new Orientation();
    private double globalAngle;

    ////////////////////////////////////////////////// ADDED THIS FOR TENSOR FLOW  ////////////////////////////////
    //vuforia key
    private static final String VUFORIA_KEY = "AWlN79T/////AAABmWr9OM0/rkyCv9xvArgzsFQAk+1QECSzNLLooRyXl4SJEguYmtuWqkOyEfk1XbyxiVq95BuSeuD5kgCMFUxvoDZBSrGA05GCbpvavBkmw8wpZDi5ffhERuoFtbbdJR8N6n3ddLfL19Ei+xljlb0it+9ukBP+Q4qCaZwpbTqupaZJGzlCsLPBIjKVUhTa8vEmbs1X8dEzHcIRZ9DIcBEkybCybflhpztnmCnaJ8s5qUd6qJxmgFv7Ei/zCchZm2eLZtjJ7OaQykPBOjb54DLgA34s/Lybr0JrKXL/vPrh0pTIDXd3v1aERMydeZpKNz1oGBBJaVZgU9yID7yRnaO+VHsGNOMgjMHjCbYLMpQKrdGx";

    //vufoia locolizer
    public VuforiaLocalizer vuforia;

    //tenser flow detector
    public TFObjectDetector tfod;

    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Servo Consents
    static final double SERVO_MIN = 0.02;
    static final double SERVO_MAX = 0.99;


    // Declare Static members for calculations
    //static final double COUNTS_PER_MOTOR_REV    = 1120;
    static final double CLAMP_MAX_POSITION = 0.99;
    static final double CLAMP_SERVO_MIN = 0.02;
    static final double COUNTS_PER_MOTOR_REV = 780;

    static final double DRIVE_GEAR_REDUCTION = 1;
    static final double WHEEL_DIAMETER_INCHES = 4.0;
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.14159);
    static final double LIFT_THREADS_PER_INCH = 0.948;
    static final double LIFT_GEAR_REDUCTION = 1;
    static final double LIFT_COUNTS_PER_MOTOR_REV = 4.0;
    static final double LIFT_COUNTS_PER_INCH = (LIFT_THREADS_PER_INCH * LIFT_GEAR_REDUCTION * LIFT_COUNTS_PER_MOTOR_REV);

    static final double CAPSTONE_OPEN = 0.2;
    static final double CAPSTONE_CLOSE = 0;


    static final double FOUNDATION_DOWN = 1.0;
    static final double FOUNDATION_UP = 0.2;

    static final double GRABBER_CLAMP_OPEN = 0.9;
    static final double GRABBER_CLAMP_CLOSE = 0.5;
    static final double GRABBER_CLAMP_MID = 0.75;
  
    static final int SHOULDER_MIN_POS = 0;
    static final int SHOULDER_MAX_POS = 4200;

    static final int WRIST_START_POS = 0;
    public static final int WRIST_MIN_POS = -180;
    public static final int WRIST_MAX_POS = -350;

    static final int TURRET_MAX = -4050;
    static final int TURRET_MIN = 10;

    static final double SPEED_LIMIT = 0.3;

    static final double     ARM_LOW_SPEED = 0.25;
    static final int        ARM_LOW_INCREMENT = 50;
    static final double     ARM_HIGH_SPEED = 0.75;
    static final int     ARM_HIGH_INCREMENT = 150;

    private int armIncrement = ARM_HIGH_INCREMENT;

    public boolean bArmSpeedLimitEnabled = false;




    /* Constructor */
    public HardwareJoeBot2019() {

    }



    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap, LinearOpMode opMode) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        myOpMode = opMode;

        // Define and Initialize Motors
        motor0 = hwMap.dcMotor.get("motor0");
        motor1 = hwMap.dcMotor.get("motor1");
        motor2 = hwMap.dcMotor.get("motor2");
        motor3 = hwMap.dcMotor.get("motor3");

        turretMotor = hwMap.dcMotor.get("turretMotor");
        shoulderMotor = hwMap.dcMotor.get("shoulderMotor");
        wristMotor = hwMap.dcMotor.get("wristMotor");


        // Define and Initialize Servos
        clampServo = hwMap.servo.get("clampServo");
        clampServo.setPosition(CLAMP_MAX_POSITION);

        foundationClamp = hwMap.servo.get("foundationServo");
        releaseFoundation();

        capstoneServo = hwMap.servo.get("capstoneServo");


        // Map Limit Switches
        lsWristDown = hwMap.get(DigitalChannel.class, "lsWristDown");
        lsWristUp   = hwMap.get(DigitalChannel.class, "lsWristUp");


        //liftBucketMotor = hwMap.dcMotor.get("liftBucketMotor");
        //mainBucketMotor = hwMap.dcMotor.get("mainBucketMotor");
        //intakeMotor = hwMap.dcMotor.get("intakeMotor");

        // Set Default Motor Directions
        motor0.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        motor1.setDirection(DcMotor.Direction.FORWARD); // Set to FORWARD if using AndyMark motors
        motor2.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        motor3.setDirection(DcMotor.Direction.FORWARD); // Set to FORWARD if using AndyMark motors

        turretMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shoulderMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        wristMotor.setDirection(DcMotorSimple.Direction.REVERSE);



        // Set all motors to zero power
        motor0.setPower(0);
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);

        turretMotor.setPower(0);
        shoulderMotor.setPower(0);
        wristMotor.setPower(0);

        myOpMode.telemetry.addLine("initialized motor power to zero");
        myOpMode.telemetry.update();

        myOpMode.telemetry.addLine("initialized other motor power to zero");
        myOpMode.telemetry.update();




        // Set all drive motors to run without encoders.
        // May want to switch to  RUN_USING_ENCODERS during autonomous
        motor0.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor3.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shoulderMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulderMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        wristMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        wristInit();


        shoulderMotor.setTargetPosition(0);
        shoulderMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        shoulderMotor.setPower(.2);

        // We don't want to call moveShoulder yet, because we need to make sure the wrist is okay
        // moveShoulder(SHOULDER_MIN_POS);



        //Set TurrentMotor to start Position
        turretMotor.setTargetPosition(TURRET_MIN);
        turretMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretMotor.setPower(0.2);




        // IMU Initializaiton
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);

        //////////////////////////////////  INITIALIZE VUFORIA AND TENSOR FLOW OBJECT DETECTOR
        //initialize vuforia here because vuforia takes a while to init
        //initVuforia();
        //if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
        //    initTfod();
        //} else {
        //    myOpMode.telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        //}
        ////////////////////////////////////////////////////////////////////////////////////////


    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long remaining = periodMs - (long) period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }


    /***
     * void setMode(DcMotor.RunMode mode ) Set all drive motors to same mode.
     * @param mode    Desired Motor mode.
     */
    public void setMode(DcMotor.RunMode mode) {
        motor0.setMode(mode);
        motor1.setMode(mode);
        motor2.setMode(mode);
        motor3.setMode(mode);
    }

    public void strafeSeconds(long milliseconds, double power) {

        //Make the robot strafe for a given number of seconds

            moveRobot(0, power,0);
            myOpMode.sleep(milliseconds);
            stop();

        }

    /**
     * void moveRobot(double forward, double right, double clockwise)
     *
     * Calculates power settings for Mecanum drive for JoeBots
     *
     * @param forward
     * @param right
     * @param clockwise
     */
    public void moveRobot(double forward, double right, double clockwise) {

        // Declare Variables to hold calculated power values for each motor
        double power0;
        double power1;
        double power2;
        double power3;
        double liftPower;
        double mainPower;

        double max;

        // gcf - inverting clockwise variables for testing

        power0 = forward + clockwise + right;
        power1 = forward - clockwise - right;
        power2 = forward + clockwise - right;
        power3 = forward - clockwise + right;

        if (speedLimitEnabled) {
            power0 = power0 * SPEED_LIMIT;
            power1 = power1 * SPEED_LIMIT;
            power2 = power2 * SPEED_LIMIT;
            power3 = power3 * SPEED_LIMIT;
        }

        // Normalize Wheel speeds so that no speed exceeds 1.0
        max = Math.abs(power0);
        if (Math.abs(power1) > max) {
            max = Math.abs(power1);
        }
        if (Math.abs(power2) > max) {
            max = Math.abs(power2);
        }
        if (Math.abs(power3) > max) {
            max = Math.abs(power3);
        }

        if (max > 1) {
            power0 /= max;
            power1 /= max;
            power2 /= max;
            power3 /= max;
        }

        motor0.setPower(power0);
        motor1.setPower(power1);
        motor2.setPower(power2);
        motor3.setPower(power3);


    }

    /**
     * stop()
     * <p>
     * method to set all motor powers to zero
     */

    public void stop() {

        motor0.setPower(0);
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);

        myOpMode.telemetry.addLine("initialized motor power to zero");
        myOpMode.telemetry.update();

    }

    /**
     * moveInches(double inches, double power)
     * <p>
     * method to drive forward (only) for a set # of inches at a set power
     *
     * @param inches
     * @param power
     * @param timeoutSec
     */

    public void moveInches(double inches, double power, int timeoutSec) {

        // method will accept a value in inches and a power setting and calculate the number of
        // rotations required to drive the given distance.

        // Tell Telemetry what we're starting
        //myOpMode.telemetry.log().add("Starting moveInches method");

        // Declare needed variables
        int newmotor0Target;
        int newmotor1Target;
        int newmotor2Target;
        int newmotor3Target;

        // Check to make sure the OpMode is still active; If it isn't don't run the method
        if (myOpMode.opModeIsActive()) {

            // Determine new target positions for each wheel
            newmotor0Target = motor0.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            newmotor1Target = motor1.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            newmotor2Target = motor2.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);
            newmotor3Target = motor3.getCurrentPosition() + (int) (inches * COUNTS_PER_INCH);

            // Send target Positions to motors
            motor0.setTargetPosition(newmotor0Target);
            motor1.setTargetPosition(newmotor1Target);
            motor2.setTargetPosition(newmotor2Target);
            motor3.setTargetPosition(newmotor3Target);

            // Set Robot to RUN_TO_POSITION mode
            setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // Reset the runtime
            runtime.reset();

            // Start moving the robot
            moveRobot(power, 0, 0);

            // Keep looping (wait) until the motors are finished or timeout is reached.
            while (myOpMode.opModeIsActive() && (runtime.seconds() < timeoutSec) &&
                    (motor0.isBusy() && motor1.isBusy() && motor2.isBusy() && motor3.isBusy())) {


                //Compose Telemetry message
          //      myOpMode.telemetry.addLine("> Waiting for robot to reach target");
            //    myOpMode.telemetry.addLine("Curr. Pos. |")
              //          .addData("0:", motor0.getCurrentPosition())
                //        .addData("1:", motor1.getCurrentPosition())
                  //      .addData("2:", motor2.getCurrentPosition())
                    //    .addData("3:", motor3.getCurrentPosition());
                //myOpMode.telemetry.addLine("Target | ")
                //        .addData("0:", newmotor0Target)
                //        .addData("1:", newmotor1Target)
                //        .addData("2:", newmotor2Target)
                //        .addData("3:", newmotor3Target);
                //myOpMode.telemetry.addData("Power: ", power);
                //myOpMode.telemetry.update();

                myOpMode.idle();
            }

            // Stop the motors
            stop();

            // Update telemetry log
            //myOpMode.telemetry.log().add("Ending moveInches method");

            // Set the motors back to standard mode
            setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        }

    }


    public void liftAndScore() {
        /*
        shoulder up
        elbow up
        intake mostly up
        */
        // backwardToggle();

    }


    //methods a lpenty.
    //no longer intake

    /**
     * resetImuAngle()
     * <p>
     * Method to grab the current reading from the IMU and set the cumulative angle tracking
     * to 0
     */

    private void resetAngle() {

        // Grab reading from IMU and store it in lastImuAngles
        lastImuAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        // Set globalAngle to zero
        globalAngle = 0;

    }

    /**
     * getAngle()
     * <p>
     * Gets the current cumulative angle rotation from last reset.
     *
     * @return Angle in degrees (+ left, - right)
     */

    private double getAngle() {

        // Grab the current IMU Angle reading
        Orientation currAngles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        // Determine the difference between the current Angle reading and the last reset
        double deltaAngle = currAngles.firstAngle - lastImuAngles.firstAngle;
        deltaAngle = -deltaAngle; // Fixing sign on deltaAngle
        // Since the Rev IMU measures in Euler angles (-180 <-> +180), we need to detect this
        if (deltaAngle < -180) deltaAngle += 360;
        else if (deltaAngle > 180) deltaAngle -= 360;

        globalAngle += deltaAngle;

        lastImuAngles = currAngles;

        return globalAngle;

    }

    /**
     * rotate(int degrees, double power)
     * <p>
     * Does not support turning more than 180 degrees.
     *
     * @param degrees
     * @param power
     */

    public void rotate(int degrees, double power) {

        myOpMode.telemetry.log().add("Starting rotate method");

        // Restart IMU movement tracking
        resetAngle();

        // getAngle returns + when rotating clockwise and - when rotating counter clockwise
        // set power (speed) negative when turning left
        if (degrees < 0) power = -power;

        // start robot turning
        moveRobot(0, 0, power);

        // stop turning when getAngle() returns a value greater or less than intended degrees
        if (degrees > 0) {
            // Expect this to be a right turn
            // On a right turn, since we start on zero, we have to get off zero first

            while (myOpMode.opModeIsActive() && getAngle() == 0) {
                myOpMode.telemetry.addLine(">getAngle() returned 0");
                myOpMode.telemetry.addLine(">>")
                        .addData("Cur: ", getAngle())
                        .addData("Tar: ", degrees);
                myOpMode.telemetry.update();
            }

            while (myOpMode.opModeIsActive() && getAngle() < degrees) {
                myOpMode.telemetry.addLine(">getAngle() returned >0");
                myOpMode.telemetry.addLine(">>")
                        .addData("Cur: ", getAngle())
                        .addData("Tar: ", degrees);
                myOpMode.telemetry.update();
            }
        } else {
            // left turn

            while (myOpMode.opModeIsActive() && getAngle() > degrees) {
                myOpMode.telemetry.addLine(">getAngle() returned <0");
                myOpMode.telemetry.addLine(">>")
                        .addData("Cur: ", getAngle())
                        .addData("Tar: ", degrees);
                myOpMode.telemetry.update();
            }

        }


        //Stop the motors
        stop();

        // reset IMU tracking
        resetAngle();


    }


    public void rotateSeconds(double SecondsToTurn, double power) {

        double currentHeading = getAngle ();

        double targetHeading = currentHeading + SecondsToTurn;

        double error = targetHeading - currentHeading;

        double closeEnough = 0.5;

        double targetPower = 0;

        double maxPower = power;

        double minPower = .1;


        while(myOpMode.opModeIsActive() && abs(error)>closeEnough) {

            if (abs(error) > 50) {
                targetPower = maxPower;
            } else if (abs(error) < 10) {
                targetPower = minPower;
            } else {
                targetPower = (maxPower - minPower) / 2;
            }
            if (error < 0) {
                targetPower = -targetPower;
            }

            moveRobot(0,0,targetPower);

            currentHeading = getAngle();
            error = targetHeading-currentHeading;

            myOpMode.telemetry.addData("targetHeading: ", targetHeading);
            myOpMode.telemetry.addData("currentHeading: ", currentHeading);
            myOpMode.telemetry.addData("targetPower: ", targetPower);
            myOpMode.telemetry.update();

        }

        myOpMode.telemetry.addData("targetHeading: ", targetHeading);
        myOpMode.telemetry.addData("currentHeading: ", currentHeading);
        myOpMode.telemetry.addData("targetPower: ", targetPower);
        myOpMode.telemetry.update();

    }








    public void rotateDegrees(double degreesToTurn, double power) {

        double currentHeading = getAngle();

        double targetHeading = currentHeading + degreesToTurn;

        double error = targetHeading - currentHeading;

        double closeEnough = 0.5;

        double targetPower = 0;

        double maxPower = .6;

        double minPower = .2;

        while(myOpMode.opModeIsActive() && abs(error)>closeEnough){

            if(abs(error) > 50) {
                targetPower = maxPower;
            } else if (abs(error) < 10) {
                targetPower = minPower;
            } else {
                targetPower = (maxPower-minPower)/2;
            }
            if(error < 0){
                targetPower = -targetPower;
            }

            moveRobot(0,0,targetPower);

            currentHeading = getAngle();
            error = targetHeading-currentHeading;

            myOpMode.telemetry.addData("targetHeading: ", targetHeading);
            myOpMode.telemetry.addData("currentHeading: ", currentHeading);
            myOpMode.telemetry.addData("targetPower: ", targetPower);
            myOpMode.telemetry.update();
        }

        myOpMode.telemetry.addData("targetHeading: ", targetHeading);
        myOpMode.telemetry.addData("currentHeading: ", currentHeading);
        myOpMode.telemetry.addData("targetPower: ", targetPower);
        myOpMode.telemetry.update();


    }


    /////////////////////////////////     Added this method:
    ///    tflocate  -  look at the leftmost two minerals because we can't see all three
    ///              -   if this sees, two silver minerals, gold is on the right, return 2 (right)
    ///              -   if this sees a gold on the right, return 1 (center)
    ///              -   if this sees a gold on the left, return 0 (left)
    ////             -   If this is in an undetermined state, it function returns -1
    ///
    ///    CAUTION!!!! -   Make sure the phone is oriented properly (camera toward middle) or this
    //                     will confuse left and center positions

    public int tflocate()
    {


        /** Activate Tensor Flow Object Detection. */
        if (tfod != null) {
            tfod.activate();
        }
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                myOpMode.telemetry.addData("# Object Detected", updatedRecognitions.size());
                if (updatedRecognitions.size() == 2) {
                    int goldMineralX = -1;
                    int silverMineral1X = -1;
                    int silverMineral2X = -1;
                    for (Recognition recognition : updatedRecognitions) {
                        if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                            goldMineralX = (int) recognition.getTop();
                            myOpMode.telemetry.addData("Left Edge:",recognition.getTop());
                        }
                        if (recognition.getLabel().equals(LABEL_SILVER_MINERAL)) {
                            silverMineral1X = (int) recognition.getTop();
                            myOpMode.telemetry.addData("Left Edge:",recognition.getTop());
                        }




                    }

                    if (goldMineralX == -1 )
                    {
                        myOpMode.telemetry.addLine("Right");
                        return 2;
                    }
                    else if (goldMineralX > silverMineral1X)
                    {
                        myOpMode.telemetry.addLine("Center");
                        myOpMode.telemetry.addData("gold mineral X", goldMineralX);
                        myOpMode.telemetry.addData("silver mineral x", silverMineral1X);
                        return 1;
                    }

                    else if (goldMineralX < silverMineral1X)
                    {
                        myOpMode.telemetry.addLine("Left");
                        myOpMode.telemetry.addData("gold mineral X", goldMineralX);
                        myOpMode.telemetry.addData("silver mineral x", silverMineral1X);
                        return 0;
                    }

                    else
                    {
                        myOpMode.telemetry.addLine("Left");
                        return 0;
                    }

                } else {
                    myOpMode.telemetry.addData("Objects detected:", updatedRecognitions.size());

                }
                myOpMode.telemetry.update();
            }
        }

        return -1;
    }
    /////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////  Initialize tensor flow object detector/////////////////////
    public void initTfod() {
        int tfodMonitorViewId = hwMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hwMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////Initialize Vuforia//////////////////////////////////////////////
    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////

    // Servo open and close method


    public void rotateTurret(int targetPos) {
        // move turret to target position

        if (targetPos < TURRET_MAX) {
            targetPos = TURRET_MAX;
        }

        if (targetPos > TURRET_MIN) {
            targetPos = TURRET_MIN;
        }

        turretMotor.setPower(1);
        turretMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretMotor.setTargetPosition(targetPos);


    }

    public void stopTurret() {

        turretMotor.setPower(0);

    }


    // grabs the foundation
    public void grabFoundation() {

        foundationClamp.setPosition(FOUNDATION_DOWN);
        bFoundationClosed = true;

    }


    public void capstoneOpen() {

        capstoneServo.setPosition(CAPSTONE_OPEN);
    }


    public void capstoneClose() {

        capstoneServo.setPosition(CAPSTONE_CLOSE);
    }


    // releases the foundation
    public void releaseFoundation () {

        foundationClamp.setPosition(FOUNDATION_UP);
        bFoundationClosed = false;

    }

    // opens servo for clamp
    public void openClamp(){

        clampServo.setPosition(GRABBER_CLAMP_OPEN);
        grabberState = 1;

    }

    // closes servo for clamp
    public void closeClamp() {
        clampServo.setPosition(GRABBER_CLAMP_CLOSE);
        grabberState = 3;

    }

    public void midClamp() {
        clampServo.setPosition(GRABBER_CLAMP_MID);
        grabberState = 2;

    }

    public void moveWrist (double power) {

        // This method will move the wrist with no regard to the minimum stop and start positions.
        // Positive power will increase the wrist encoder targetValue; negative power will decrease
        // the wrist encoder target value.

        int newWristTarget = 0;

        if (power > 0) {
            // Move the wrist up
            newWristTarget = wristMotor.getCurrentPosition() + 25;
        } else if (power < 0) {
            // Move the wrist down
            newWristTarget = wristMotor.getCurrentPosition() - 25;
        }

        wristMotor.setTargetPosition(newWristTarget);

    }



    public void moveShoulderUp(){

        int shoulderTargetPosition;

        shoulderTargetPosition = shoulderMotor.getTargetPosition() + armIncrement;

        moveShoulder(shoulderTargetPosition);

    }

    public void moveShoulderDown(){

        int shoulderTargetPosition;

        shoulderTargetPosition = shoulderMotor.getTargetPosition() - armIncrement;

        moveShoulder(shoulderTargetPosition);

    }

    public void moveShoulder(int targetPos) {

        int shoulderPos; //shoulder max position=4200)
        int wristPos;

        shoulderPos = targetPos;

        // TODO: Add error/bounds checking
        // if shoulder position is greater than 4200, then it stays at 4200

        if (shoulderPos > SHOULDER_MAX_POS) {
            shoulderPos = SHOULDER_MAX_POS;
        }

        if (shoulderPos < SHOULDER_MIN_POS)  {
            shoulderPos = SHOULDER_MIN_POS;
        }

        shoulderMotor.setTargetPosition(shoulderPos);

    }

    public void alignWrist() {

        // This method will align the wrist based on the current shoulderMotor Position

        int wristTargetPosition = (-150 - (shoulderMotor.getCurrentPosition()/32));

        if (wristTargetPosition > WRIST_MIN_POS) {
            wristTargetPosition = WRIST_MIN_POS;
        }

        if (wristTargetPosition < WRIST_MAX_POS) {
            wristTargetPosition = WRIST_MAX_POS;
        }

        wristMotor.setTargetPosition(wristTargetPosition);


    }

    public void toggleArmSpeedLimit() {

        if (bArmSpeedLimitEnabled) {

            // Robot already has the arm speed limit enabled. We want to disable the speed limit

            armIncrement = ARM_HIGH_INCREMENT;
            shoulderMotor.setPower(ARM_HIGH_SPEED);
            wristMotor.setPower((ARM_HIGH_SPEED));

            bArmSpeedLimitEnabled = false;

        } else {

            // Speed limit is not enabled. Enable the speed limit

            armIncrement = ARM_LOW_INCREMENT;
            shoulderMotor.setPower(ARM_LOW_SPEED);
            wristMotor.setPower(ARM_LOW_SPEED);

            bArmSpeedLimitEnabled = true;

        }


    }

    public void wristInit() {

        // This method assumes the wrist is already defined in the main robot init. It will move the
        // wrist "up" until the limit switch is pressed. Once up, it will reset the encoder to zero.

        wristMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        while (lsWristUp.getState() == true) {

            // Move the wrist up
            wristMotor.setPower(0.2);

            myOpMode.telemetry.addLine("Still in WristInit mode");
            myOpMode.telemetry.update();

        }


        if (lsWristDown.getState() == true) {
            myOpMode.telemetry.addData("Wrist Down Sensor >> ", "Is Not Pressed");
        } else {
            myOpMode.telemetry.addData("Wrist Down Sensor >> ", "Is Pressed");
        }

        if (lsWristUp.getState() == true) {
            myOpMode.telemetry.addData("Wrist Up Sensor >> ", "Is Not Pressed");
        } else {
            myOpMode.telemetry.addData("Wrist Up Sensor >> ", "Is Pressed");
        }

        myOpMode.telemetry.update();
        wristMotor.setPower(0);

        wristMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        wristMotor.setTargetPosition(0);
        wristMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        toggleArmSpeedLimit();

    }

    /* Initialize standard Hardware interfaces */
    public void init1(HardwareMap ahwMap, LinearOpMode opMode) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        myOpMode = opMode;

        // Define and Initialize Motors
        motor0 = hwMap.dcMotor.get("motor0");
        motor1 = hwMap.dcMotor.get("motor1");
        motor2 = hwMap.dcMotor.get("motor2");
        motor3 = hwMap.dcMotor.get("motor3");

        turretMotor = hwMap.dcMotor.get("turretMotor");
        shoulderMotor = hwMap.dcMotor.get("shoulderMotor");
        wristMotor = hwMap.dcMotor.get("wristMotor");



        // Define and Initialize Servos
        //clampServo = hwMap.servo.get("clampServo");
        //clampServo.setPosition(CLAMP_MAX_POSITION);

        foundationClamp = hwMap.servo.get("foundationServo");
        releaseFoundation();



        //liftBucketMotor = hwMap.dcMotor.get("liftBucketMotor");
        //mainBucketMotor = hwMap.dcMotor.get("mainBucketMotor");
        //intakeMotor = hwMap.dcMotor.get("intakeMotor");

        // Set Default Motor Directions
        motor0.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        motor1.setDirection(DcMotor.Direction.FORWARD); // Set to FORWARD if using AndyMark motors
        motor2.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        motor3.setDirection(DcMotor.Direction.FORWARD); // Set to FORWARD if using AndyMark motors

        turretMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shoulderMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        wristMotor.setDirection(DcMotorSimple.Direction.REVERSE);




        // Set all motors to zero power
        motor0.setPower(0);
        motor1.setPower(0);
        motor2.setPower(0);
        motor3.setPower(0);

        turretMotor.setPower(0);
        shoulderMotor.setPower(0);
        wristMotor.setPower(0);

        myOpMode.telemetry.addLine("initialized motor power to zero");
        myOpMode.telemetry.update();

        myOpMode.telemetry.addLine("initialized other motor power to zero");
        myOpMode.telemetry.update();



    }

}










