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

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.HardwareJoeBot2019;
import org.firstinspires.ftc.teamcode.Image_Recognition;

/**
 * This is sample code used to explain how to write an autonomous code
 *
 */

@Autonomous(name="Red Random Block No Stack", group="Pushbot")
//@Disabled
public class RedFoundationParkingAndRandomBlockAndNoStack extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareJoeBot2019      robot   = new HardwareJoeBot2019();   // Use a Pushbot's hardware
    Image_Recognition    V = new Image_Recognition();
    private ElapsedTime     runtime = new ElapsedTime();


    @Override
    public void runOpMode() {

        telemetry.addLine("Press > to Start");
        telemetry.update();

        V.init(hardwareMap,this);
        robot.init(hardwareMap,this);
        robot.wristMotor.setTargetPosition(robot.WRIST_MIN_POS);
/*
        //Start Looking for Skystone
        double coords[] = {777,777};
        int skystonePosition = 0; // 1= left; 2=center; 3=right

        runtime.reset();

        int i = 0;
        while(runtime.seconds() < 10 && coords[1]!=777){
            coords = V.skystone_cooridinates();
            i= i + 1;
            sleep(80);
        }

        if (coords[0] < 0) {
            skystonePosition = 1;
        } else if (coords[0] == 777) {
            // we couldn't find the skystone, so we assume position 3
            skystonePosition = 3;
        } else {
            skystonePosition = 2;
        }
*/
        waitForStart();

        //move to range of skystone
        //robot.moveInches(6.3,0.42, 10);
        //sleep(300);

        /*
        // Figure out where Skystone is
        double coords[] = {777,777};

        int skystonePosition = 0; // 1= left; 2=center; 3=right
        int i = 0;
        while(i < 25){
            coords = V.skystone_cooridinates();
            i= i + 1;
            sleep(80);
        }



        if (coords[1] < 0) {
            skystonePosition = 1;
        } else if (coords[1] != 777) {
            // we couldn't find the skystone, so we assume position 3
            skystonePosition = 3;
        } else {
            skystonePosition = 2;
        }

        telemetry.addData("Skystone Location: ", skystonePosition);
        telemetry.addData("X Coord: ", coords[1]);
        telemetry.update();

        */
/*
        // Drive forward
        robot.moveInches(25,.2,10);

        sleep(200);
*/
        // Decide left center or right

       // robot.wristMotor.setTargetPosition(robot.WRIST_MIN_POS);


        // Drive forward
        robot.moveInches(21,.25,10);
sleep(600);
        // close clamp
        robot.closeClamp();
sleep(600);
        // raise arm
        robot.moveShoulder(700);

        // drive backwards
        robot.moveInches(-4,.25,10);

        // rotate 90-degrees CCW
        robot.rotateDegrees(87,.2);


        // Drive Straight (Decide how far based on left center right)
       robot.moveInches(32,.25,10);

        // rotate 90
        robot.rotateDegrees(-87,.2);
sleep(500);
        //robot.moveInches(-2,.25,10);
        // drop block
        robot.openClamp();
sleep(500);
        // rotate 90 counterclockwise
        //robot.moveInches(2,0.25,10);
        robot.moveInches(-1,.25,10);
        robot.rotateDegrees(87,.2);
sleep(500);
        robot.moveShoulder(0);
sleep(500);
        // drive backwards to parking spot
        robot.moveInches(-20,.2,10);







/*
        if(coords[1] < 0){
        robot.strafeSeconds(-50,.5);
        robot.moveInches(35,.75 ,10);
        robot.closeClamp();
        robot.moveInches(-13,.75,10);
        robot.rotateDegrees(-90,.5);
        robot.moveInches(45,1,10);
        robot.rotateDegrees(90,.5);
        robot.moveInches(25,1,10);
        robot.openClamp();
        }else if(coords[1] != 777){

            robot.strafeSeconds(-300,1);
            robot.moveInches(35,.5,10);
            robot.closeClamp();
            robot.moveInches(-13,.75,10);
            robot.strafeSeconds(900,1);
            robot.moveInches(25,1,10);
            robot.openClamp();

        }else{

            robot.strafeSeconds(600,1);
            robot.moveInches(35,.5,10);
            robot.closeClamp();
            robot.moveInches(-13,.75,10);
            robot.strafeSeconds(900,1);
            robot.moveInches(25,1,10);
            robot.openClamp();

        }

*/





/*
        //move to foundation
       // robot.moveInches(29,0.5, 10);
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
        robot.moveInches(18,1,10);
        robot.strafeSeconds(500,1);
        robot.moveInches(23,1,10);
*/



        telemetry.addLine("We're done. Press stop.");
        telemetry.update();

    }

}