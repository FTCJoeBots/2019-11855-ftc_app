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

@Autonomous(name="Skystone and Blue Foundation", group="Pushbot")
//@Disabled
public class blueFoundationParkingAndSkystone extends LinearOpMode {

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



       // robot.moveInches(24,0.45,10);

        //strafe intil skystone goes here




        //move to foundation
        robot.moveInches(29,0.5, 10);
        sleep(1000);
        robot.strafeSeconds(640,-0.7);
        //grab foundation
        robot.grabFoundation();

        sleep(1000);
        //drive into building site
        robot.moveInches(-65, 0.35,15);
        //robot.strafeSeconds(1200, .75);
        //robot.moveInches(-10, 0.5, 10);

        //release grabber
        robot.releaseFoundation();
        sleep(1000);

        //back up under skybridge
        robot.moveInches(44,0.5,10);






        telemetry.addLine("We're done. Press stop.");
        telemetry.update();

    }

}