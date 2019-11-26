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
 * This is sample code used to explain how to write an autonomous code
 *
 */

@Autonomous(name="Blue Foundation", group="Pushbot")
//@Disabled
public class blueFoundationParking extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareJoeBot2019      robot   = new HardwareJoeBot2019();   // Use a Pushbot's hardware
    Image_Recognition    V = new Image_Recognition();
    private ElapsedTime     runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        telemetry.addLine("Press > to Start");
        telemetry.update();

        robot.init1(hardwareMap,this);
        waitForStart();

        //move to foundation
        robot.moveInches(22.5,1, 10);
        sleep(750);
        robot.strafeSeconds(540,-0.7);
        //grab foundation
        robot.grabFoundation();

        sleep(500);
        //drive into building site
        robot.moveInches(-80, 0.4,15);
       /*
        robot.strafeSeconds(500, 0.5);
        robot.moveInches(-1, 0.25, 10);
        */
        //release grabber
        robot.releaseFoundation();
        sleep(500);

        //back up under skybridge
        robot.moveInches(22,1,10);

        sleep(200);
        //strafe left of the skybridge
        robot.strafeSeconds(450, -1);

        sleep(500);
        //moves foward
        robot.moveInches(17,1,10);
        telemetry.addLine("done");
        telemetry.update();
        sleep(1000);

        //rotate left
        robot.rotateDegrees(-90,0.5);

        //moves back
        robot.moveInches(-17,1,10);

        //moves wrist later


        //moves foward
        robot.moveInches(22,1,10);

        //moves back
        robot.moveInches(-17,1,10);

        //rotate
        robot.rotateDegrees(-90,1);

        //moves foward
        robot.moveInches(78,1,10);

       //srafe left
       robot.strafeSeconds(500,1);

        // arm goes up here


        //drops block here


        //wrist goes back up here


        //srafe right
        robot.strafeSeconds(500,-1);

        //goes back under the bridge
        robot.moveInches(42,1,15);



        telemetry.addLine("We're done. Press stop.");
        telemetry.update();

    }

}