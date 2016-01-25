#Welcome to the 3DScanner Project realised with a Raspberry Pi!

#Navigation

The main Folders of our Project are: **Core** and **Desktop**.
In Core are two different Packages. One Package **polygonviewer** is for the Computer that should be responsible for the calculations and the other one **Client** is for the raspberry pi to control the movement of the motor and the camera.
In Desktop we impelemnted mostly of the UI.

#How to start

You just have to do one step: Go into the .java File Exec and set the IP Adress of your raspberry Pi in your network. Be sure that your network allows ssh connections and then start the programm over the main method.

##Pi
Before you start the main-method you should setup the raspberry. Turn it and the laser on, set the camera and laser into the right posiiton. Furthermore the Folder Core/Client **necessarily** placed into the raspberry Pi on the Desktop in a Folderstructure with the Top Folder Scanner, two second Folder, Driver and Kamera. The .java file must stay in the Driver folder. The .py also must stay in the driver Folder.


