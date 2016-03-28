#Written by Anton von Weltzien 2015
#This script is used to control the motor and the camera. It has the function
# oneStep which moves the motor one of its 512 steps. rotateNSteps() which can move the motor
#any number of steps and creates a sound. BetterPictureQuality which improves the settings of
#the camera. takePicture() which takes a pic and moves it. picsAndRotation() which executes
#the required methods to get the whished number of pictures


from time import sleep
import time
import sys
import RPi.GPIO as GPIO
import picamera
from fractions import Fraction
import os

sleep_substeps= 0.001 #At the moment this has no use
sleep_step = 0 #At the moment this has no use
steps_rotation = 512 #With this motor 512 steps always equal one full rotation
pics = int(sys.argv[1]) #usable Values for pics so far are 16, 32 and 64. Possible is 2^n
cameraSpeed = 220000 #Depending on the object it needs to be changed to get a white line
digestTime = 0.01
#Until proven wrong, this is good. Is explained later

GPIO.setmode(GPIO.BCM)
GPIO.setwarnings(False)
"""here we have selected blow pins which start after the first ground pin on the right on the other side and skip on ground pin """
port_list = [4,17,27,22]
GPIO.setup(port_list, GPIO.OUT)

def oneStep():
    #the function goes through required the different ticks for a complet step
    #during the process the pins 4 to 7 are opened in the required order for this motor
    #This way of moving the steppermotor is called halfstepping.
    #tick1 -- 4
    GPIO.output(port_list, (GPIO.HIGH, GPIO.LOW, GPIO.LOW, GPIO.LOW))
    sleep(sleep_substeps)
    GPIO.output(port_list, (GPIO.HIGH, GPIO.HIGH, GPIO.LOW, GPIO.LOW))
    sleep(sleep_substeps)
    #tick3 -- 5
    GPIO.output(port_list, (GPIO.LOW, GPIO.HIGH, GPIO.LOW, GPIO.LOW))
    sleep(sleep_substeps)
    #tick4 -- 5 + 6
    GPIO.output(port_list, (GPIO.LOW, GPIO.HIGH, GPIO.HIGH, GPIO.LOW))
    sleep(sleep_substeps)
    #tick5 -- 6
    GPIO.output(port_list, (GPIO.LOW, GPIO.LOW, GPIO.HIGH, GPIO.LOW))
    sleep(sleep_substeps);
    #tick6 -- 6 + 7
    GPIO.output(port_list, (GPIO.LOW, GPIO.LOW, GPIO.HIGH, GPIO.HIGH))
    sleep(sleep_substeps)
    #tick7 -- 7
    GPIO.output(port_list, (GPIO.LOW, GPIO.LOW, GPIO.LOW, GPIO.HIGH))
    sleep(sleep_substeps)
    #tick8 -- 7 + 4
    GPIO.output(port_list, (GPIO.HIGH, GPIO.LOW, GPIO.LOW, GPIO.HIGH))
    sleep(sleep_substeps)
    #The complet step is finshed here so 4 and 7 are closed
    GPIO.output(port_list, GPIO.LOW)
    sleep(sleep_substeps)

def rotateNSteps(N_amount):
    for i in range(N_amount):
        oneStep()
	
def betterPictureQuality():
     #This method sets better Settings for the camera. To make the pictures easier to process
    #All the pictures will have the same quality/structure
    #camera.resolution = (imageWidth, imageHeight)
    #Set framerate to lower then 1/shutter speed, but it needs to be set first.
    #camera.zoom = (0.0, 0.08, 0.9, 0.7)
    #camera.resolution = (2333, 800)
    camera.resolution = (2592, 1944)
    camera.zoom = (0.2, 0.15, 0.55, 0.55)
    #camera.framerate = Fraction(1, (cameraSpeed/10000))
    camera.framerate = Fraction(5, 2)
    camera.shutter_speed = cameraSpeed
    #Here the white balance is set to on standart so colors dont change
    g = camera.awb_gains
    camera.awb_mode = 'off'
    camera.awb_gains = g
    camera.iso = 500
    #gain for red and blue. This is good. Dont change! Sets a standart white
    #camera.awb_gains = (1, 1.8)
    #Here exposure is set to the above values and kept this way for ever.
    #camera.exposure_mode = 'off'
    #Sets light sensetivty. Keep it low to reduce noise.
    #camera.iso = 100
    #Maximum contrast makes the bright parts white, which is easier to detect
    #camera.contrast = 100
    #camera.zoom = (0.0, 0.08, 0.9, 0.7)
    #camera.sharpness = 100
    
def takePicture(number):
    
    name = str(number) + ".jpg"
    camera.capture("/home/pi/Desktop/Driver/Java/"+ name)
    #Here we need to set a digest/wait time, befor the Photo is moved so client.jar
    #doesnt copy an unfinished photo, which WILL RESULT IN AN ERROR!
    sleep(digestTime)
    #The Photo is moved after it hopefully has been completly written, to a place
    #where the client.jar can find it.
    os.rename("/home/pi/Desktop/Driver/Java/" + name, "/home/pi/Desktop/Driver/Kamera/" + name)
    
def picsAndRotation():
    #This function puts everything together. This is what is called to make a full rotation whith pics
    steps = int(steps_rotation/pics)
    for i in range(pics):
        takePicture(i)
        """print("Bild ", i)
        print (camera.framerate)
        print (camera.awb_mode)
        print (camera.iso)
        print (camera.exposure_mode)
        print (camera.awb_gains)
        print (camera.exposure_speed)"""
        sleep(0.1)#4
        rotateNSteps(steps)
        sleep(0.1)#2

def einBild():
    #This Method takes just one picture. It will be used for calibration
    takePicture(1)
    print (camera.exposure_speed)

#Here the camera object is created.
camera = picamera.PiCamera()
sleep(3)
betterPictureQuality()
#All the methods above are called here. Because conections are created try/finally is used
#this is the part of the script that is actually executed.
try:
    if pics == 1:
        einBild()
    else:
        picsAndRotation()
finally:
    #closes everything
    GPIO.cleanup()
    camera.close()




