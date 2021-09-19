
In order to run this application you need to setup a virtual device in android studio and make sure the SDK is setup properly.

A few disclaimers. I hope the settings all carry over after you import the project. One of the bigest challenges was dealing with all of the eros when initially setting everything up. I believe once you have the virtual device made you should be able to just run the application by pressing the green play button and selecting the device. 

The steps in order, assuming you dont have anything setup
1. Install android studio
2. Import Project, then choose the folder provided. It will be called FedEx Routing.
3. Make a new virtual device. 
This is done by going to Tools>AVD Manager>Create Virtual Device.
We use a pixel 3 on API 28 (Android 9.0)

4. Make sure your SDK is up to date by going to Tools>SDK Manager>SDK Tools
You need to have Google services installed. 
I have the following items checked and installed:
Anrdoid SDK Build tools
Android Emulator
Android SDK Platform-Tools
Android SDK Tools
Google Play APK Expansion Library
Google Play Instant Development SDK
Google Play Licensing Library
Google Play Services
Google USB Driver
Intel x86 Emulator Accelerator (HAXM installer)
Google Repository

ONCE THE EMULATOR IS WORKING- STEPS TO USE THE APPLICATION

VERY IMPORTANT- you need to upload the text file to a specific place for the application to work and read the file. If you dont the app will crash. 
In android studio all the way in the bottom right is a tab "Device File Explorer". Open that tab and run the emulator. Navigate to the folder 
Storage>Emulated>0 
and on the 0 folder, right click and click upload. This is where you will upload "file.txt" which is the raw data of the stops. This needs to be done for the application to work. This is the file where application gets the stop info from.

Now that you can run the application its time to use it.
1. You need login info. The one we use is 11111 and 'admin' for the password. There are others stored in the database is you look in the code.
2. Now that the app is logged in it should ask you for permissions such as media and location services. Click yes to both of these. If you dont the application wont work.
3. There is a small bug where the applcaition doesnt fully work at first. You simply need to close the application and start it again and then it will work from then on out. The problem is because the permissions are active yet the first time the app is ran. 
4. Now you can mess with the application. 

If you have any issues getting anything to work please contact Thomas at his email. tbridges@albany.edu
There were many thing to set up in the begining but hopefully this covers everything.

*****This application *does work when you put it on a phone directly hoewver you need to change where the file is located in the code. if you dont then the program crashes after the login since that is when the data is loaded.