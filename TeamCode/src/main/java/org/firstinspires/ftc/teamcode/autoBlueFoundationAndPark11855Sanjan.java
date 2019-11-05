/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This code gets foundation to BLUE build site and parks under bridge near the wall
 *
 */

@Autonomous(name="11855 Auto Move Foundation Blue", group="Pushbot")
//@Disabled
public class autoBlueFoundationAndPark11855Sanjan extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareJoeBot2019 robot = new HardwareJoeBot2019();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        telemetry.addLine("Press > to Start");
        telemetry.update();

        robot.init(hardwareMap, this);

        waitForStart();

        //Strafe to foundation

        telemetry.addLine("Starts strafing to foundation");
        robot.moveRobot(0, -13, 0);
        telemetry.update();

        //grab foundation code goes here

      /*  public void grabFoundation() {

            foundationServo.setPosition(FOUNDATION_DOWN);
        }

        // releases the foundation
        public void releaseFoundation () {

            foundationServo.setPosition(FOUNDATION_UP);

        }

        // opens servo for clamp
        public void openClamp(){

            clampServo.setPosition(CLAMP_OPEN);
        }

        // closes servo for clamp
        public void closeClamp(){

            clampServo.setPosition(CLAMP_CLOSE);
        }
    } */
        // Strafe foundation to build site
        telemetry.addLine("Starts strafing to build site");
        robot.moveRobot(0, 13, 0);
        telemetry.update();

        sleep(1000);
        telemetry.addLine("Ends strafing");
        telemetry.update();

        //grab clamp

        telemetry.addLine("Close Clamp");


        //move forward to alliance bridge

        robot.moveInches(21, .5, 10);
        sleep(1000);


        telemetry.addLine("We're done. Press stop.");
        telemetry.update();

    }

}
