#Introduction to a 3DScanner - created with a Raspberry-PI

##Setup the Software

###Navigation

The main Folders of our Project are: **Core** and **Desktop**.
In Core are two different Packages. One Package **polygonviewer** is for the Computer that should be responsible for the calculations and the other one **Client** is for the raspberry pi to control the movement of the motor and the camera.
In Desktop we impelemnted mostly of the UI.

###How to start

You just have to do one step: Go into the .java File Exec and set the IP Adress of your raspberry Pi in your network. Be sure that your network allows ssh connections and then start the programm over the main method.

##Setup the Hardware

This 3DScanner is based on a RaspberryPI(its not important which one), a normal 5MP Raspberry Kamera, a Line Laser, a Steppermotor and a some creativity.
You need to montage those Parts as displayed in the Image below.
addImage
The Laser should project a vertical Line on the center of the Motor.
Before you start the main-method you should setup the raspberry. Turn it and the laser on, set the camera and laser into the right posiiton. Furthermore the Folder Core/Client **necessarily** placed into the raspberry Pi on the Desktop in a Folderstructure with the Top Folder Scanner, two second Folder, Driver and Kamera. The .java file must stay in the Driver folder. The .py also must stay in the driver Folder.


