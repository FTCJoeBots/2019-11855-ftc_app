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

    HardwareJoeBot2019 robot = new HardwareJoeBot2019();

    @Override
    public void runOpMode() throws InterruptedException {




        robot.init(hardwareMap, this);

        robot.wristInit();

        waitForStart();



        //start of loop
        while (opModeIsActive()) {


            //Drive Via "Analog Sticks" (Not Toggle)
            //Set initial motion parameters to Gamepad1 Inputs
            forward = -gamepad1.left_stick_y;
            //right = gamepad1.left_stick_x;
            right = -gamepad1.left_trigger + gamepad1.right_trigger;
            clockwise = gamepad1.right_stick_x;

            // Add a tuning constant "K" to tune rotate axis sensitivity
            k = .6;
            clockwise = clockwise * k; //Make sure the "= Clockwise" is "= -clockwise"


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
                robot.moveShoulder(robot.shoulderMotor.getCurrentPosition() + 100);
            } else if (gamepad2.dpad_down) {
                robot.moveShoulder(robot.shoulderMotor.getCurrentPosition() - 100);
            }

            if (gamepad2.dpad_left) {

            } else if (gamepad2.dpad_right) {

            }

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
            telemetry.addData("Turret Target:", robot.turretMotor.getTargetPosition());
            telemetry.addData("Turret Motor Position: ", robot.turretMotor.getCurrentPosition());
            telemetry.addData("Wrist Position: ", robot.wristMotor.getCurrentPosition());
            telemetry.addData("Shoulder Position: ", robot.shoulderMotor.getCurrentPosition());


            telemetry.update();
            idle();



        }




    }
}
