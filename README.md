# WirelessControl
Android app for bluetooth control of robot

A robot was created for an Electronics Design Lab class at the University of Colorado Boulder. For our final project we
decided to control our robot via bluetooth using an Android mobile application. The code and UI for the application was created
from scratch with the use of resources such as the Android API guide. In addition the bluetooth module used for this project is
the HC-05 blutooth module. 

The mobile application contains 4 files:

-SplashScreen.java
  Creates the initial screen the user sees, lasts for 3 seconds. 

-DeviceList.java
  Allows the user to pick a bluetooth device from a list of sorrounding blutooth devices.

-ledControl.java
  Creates the widgets and sends commands to the Arduino connected to the robot in order to control movement, led's and speed.
  Also controls the connecting and disconnecting to bluetooth.

-MainActivity.java
  
  
