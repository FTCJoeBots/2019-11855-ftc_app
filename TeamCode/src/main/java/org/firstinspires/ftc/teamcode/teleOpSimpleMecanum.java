package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 *import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
 *import com.qualcomm.robotcore.hardware.DcMotor;
 *
 *
 */

/**
 *Notes For this TeleOp Code. This code is for Comp and all proggramers should review over this
 *code and understand this code for the possibility that a question may be asked related to TeleOp and
 *you should be able to explain in good detail everything in this code.
 *11/16/17-> Changed all gamepad's in code to correct gamepad (i.e some gamepad1's to gamepad2)
 ***11/18/17-> Competition Notes below
 *Notes-> Autonomous is incorrect, Not much was wrong from a software sandpoint but hardware issues were fixed
 *Autonomous issues included: Incorrect spinning causing us to move out of destination,
 *To much time on the down motion of the clamp and arm.
 *These issues are still not resolved
 * Recomendation for autonomous issues(Not Offical):Fine tune the timer on the clamp
 * Fine tune the movements and LOWER the TIME OF MOVEMENT in autonomous.
 * List of issues at Comp(1)-> https://docs.google.com/a/stjoebears.com/spreadsheets/d/1r_liipKBU7GHfONdxq9E6d4f7zikcCuXwDL2bsQfwm0/edit?usp=sharing
 *G-Sheet of time VS Heading for autonomous -> https://docs.google.com/a/stjoebears.com/spreadsheets/d/1pqv0iN94fFd5KvX1YIWP7z39HgpURXsscn0zPujs1q4/edit?usp=sharing
*/
@TeleOp(name="Simple Mecanum Drive", group="TeleOp")

public class teleOpSimpleMecanum extends LinearOpMode {

    double forward;
    double clockwise;
    double right;
    double k;
    double power0;
    double power1;
    double power2;
    double power3;
    double liftpower;
    double max;

    boolean currStateA;
    boolean prevStateA = false;

    boolean currStateB;
    boolean prevStateB = false;

    boolean bCurrStateY;
    boolean bPrevStateY = false;

    boolean bWristPanicCurr = false;
    boolean isbWristPanicPrev = false;

    boolean currState1A;
    boolean prevState1A = false;

    boolean currState2dpadUp;
    boolean prevState2dpadUp = false;
    boolean currState2dpadDown;
    boolean prevState2dpadDown = false;
    boolean currState2dpadLeft;
    boolean prevState2dpadLeft = false;
    boolean currState2dpadRight;
    boolean prevState2dpadRight = false;

    int shoulderPositionIncrement = 50;
    double powerIncrement = 0.1;
    double shoulderTargetPower = 0.2;
    double wristTargetPower = 0.2;

    HardwareJoeBot2019 robot = new HardwareJoeBot2019();

    @Override
    public void runOpMode() throws InterruptedException {




        robot.init(hardwareMap, this);

        robot.wristInit();

        waitForStart();



        //start of loop
        while (opModeIsActive()) {



            // Set SPEED LIMIT
            currState1A = gamepad1.a;
            if (currState1A && currState1A != prevState1A) {

                // This is the toggle for the Speed Limit
                // Check to see if robot.speedLimitEnabled is true

                if (robot.speedLimitEnabled) {
                    // Speed limit is enabled. Let's disable it.
                    robot.speedLimitEnabled = false;
                } else {
                    // Speed Limit is not enabled. Let's enable it.
                    robot.speedLimitEnabled = true;
                }

            }
            prevState1A = currState1A;


            //Drive Via "Analog Sticks" (Not Toggle)
            //Set initial motion parameters to Gamepad1 Inputs
            forward = -gamepad1.left_stick_y;
            //right = gamepad1.left_stick_x;
            right = -gamepad1.left_trigger + gamepad1.right_trigger;
            clockwise = gamepad1.right_stick_x;

            // Add a tuning constant "K" to tune rotate axis sensitivity
            k = .6;
            clockwise = clockwise * k; //Make sure the "= Clockwise" is "= -clockwise"


            robot.moveRobot(forward, right, clockwise);

            /*

            COMMENTING OUT THIS SECTION -- WILL USE moveRobot() instead

            // Calculate motor power
            power0 = forward + clockwise + right;
            power1 = forward - clockwise - right;
            power2 = forward + clockwise - right;
            power3 = forward - clockwise + right;

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

            robot.motor0.setPower(power0);
            robot.motor1.setPower(power1);
            robot.motor2.setPower(power2);
            robot.motor3.setPower(power3);


             */

            // Because of the gearing, positive motor power rotates the turret clockwise, but decreases the
            // encoder counts. So our "right" (clockwise) motion will decrease encoder counts

            if (gamepad2.left_bumper) {
                // move turret left

                robot.rotateTurret(robot.turretMotor.getCurrentPosition() + 20);

            }
            if (gamepad2.right_bumper) {
                //move turret right

                robot.rotateTurret(robot.turretMotor.getCurrentPosition() - 20);
            }

            currStateA = gamepad2.a;
            if (currStateA && currStateA != prevStateA) {

                if (robot.grabberState == 1) {
                    robot.midClamp();
                } else if (robot.grabberState == 2) {
                    robot.closeClamp();
                } else {
                    robot.midClamp();
                }

            }
            prevStateA = currStateA;

            currStateB = gamepad2.b;
            if (currStateB && currStateB != prevStateB ) {

                robot.openClamp();

            }
            prevStateB = currStateB;

            // Manually move wrist and shoulder based on dpad
            if (gamepad2.dpad_up) {
                robot.moveShoulder(robot.shoulderMotor.getCurrentPosition() + shoulderPositionIncrement);
            } else if (gamepad2.dpad_down) {
                robot.moveShoulder(robot.shoulderMotor.getCurrentPosition() - shoulderPositionIncrement);
            }



            // USE DPAD TO ADJUST SHOULDER/WRIST SPEED

            currState2dpadDown = gamepad2.dpad_down;
            if (currState2dpadDown && currState2dpadDown != prevState2dpadDown) {
                // decrease Shoulder power
                shoulderTargetPower = shoulderTargetPower - powerIncrement;
                robot.shoulderMotor.setPower(shoulderTargetPower);
                robot.wristMotor.setPower(shoulderTargetPower);
            }
            prevState2dpadDown = currState2dpadDown;

            currState2dpadUp = gamepad2.dpad_up;
            if (currState2dpadUp && currState2dpadUp != prevState2dpadUp) {
                // increase Shoulder power
                shoulderTargetPower = shoulderTargetPower + powerIncrement;
                robot.shoulderMotor.setPower(shoulderTargetPower);
                robot.wristMotor.setPower(shoulderTargetPower);
            }
            prevState2dpadUp = currState2dpadUp;

            currState2dpadLeft = gamepad2.dpad_left;
            if (currState2dpadLeft && currState2dpadLeft != prevState2dpadLeft) {
                // decrease Shoulder increment
                shoulderPositionIncrement = shoulderPositionIncrement - 10;
            }
            prevState2dpadLeft = currState2dpadLeft;

            currState2dpadRight = gamepad2.dpad_right;
            if (currState2dpadRight && currState2dpadRight != prevState2dpadRight) {
                // increase Shoulder increment
                shoulderPositionIncrement = shoulderPositionIncrement + 10;
            }
            prevState2dpadLeft = currState2dpadLeft;


            bCurrStateY = gamepad2.y;
            if (bCurrStateY && bCurrStateY != bPrevStateY) {

                if (robot.bFoundationClosed) {
                    robot.releaseFoundation();
                } else {
                    robot.grabFoundation();
                }

            }
            bPrevStateY = bCurrStateY;


            // WRIST PANIC CODE: This code will move the wrist up and re-zero the encoder
            // Note that the wrist will keep moving as long as both "stick" buttons are pressed
            // when the sticks are released, it will reinitialize the wrist and move it back to the
            // horizontal position

            if (gamepad2.left_stick_button && gamepad2.right_stick_button) {
                bWristPanicCurr = true;
                robot.moveWrist(0.5);
            } else {
                if (bWristPanicCurr) {
                    // We were in a wrist panic mode, but we're not now. call wrist init
                    robot.wristInit();
                    bWristPanicCurr = false;

                }

            }


            /*
            if (gamepad2.right_trigger > 0) {
                // open clamp

                robot.servoOpen();

            }

            if (gamepad2.left_trigger == -1) {
                // close clamp

                robot.servoClose();

            }
            */

            // Update Telemetry
            telemetry.addData(">", "Press Stop to end test.");

            if (robot.speedLimitEnabled) {
                telemetry.addLine("Speed Limit is ENABLED");
            } else {
                telemetry.addLine("Speed Limit is DISABLED");
            }
            telemetry.addData("Shoulder Power: ", "%1.2f", shoulderTargetPower);
            telemetry.addData("Shoulder Increment: ", "%3d", shoulderPositionIncrement);
            telemetry.addData("Turret Target:", robot.turretMotor.getTargetPosition());
            telemetry.addData("Turret Motor Position: ", robot.turretMotor.getCurrentPosition());
            telemetry.addData("Wrist Position: ", robot.wristMotor.getCurrentPosition());
            telemetry.addData("Shoulder Position: ", robot.shoulderMotor.getCurrentPosition());


            telemetry.update();
            idle();



        }




    }
}
