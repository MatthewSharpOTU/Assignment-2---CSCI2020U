# Assignment-2---CSCI2020U
Assignment-2---CSCI2020U Assignment 2 Repository for System Development and Integration - CSCI2020U.
Group Members: `Matthew Sharp - 100748071` and `Andy Wang - 100751519`

This is a program that uses mutithreading and 
When running server the output should appear text:
![Alt text](/ServerIndication.png)

When running client first the client will see:
![Alt text](/LoginWindow.png)

Then after loging in they will have access to the server:
![Alt text](/ClientUi.png)

The simple FileViewer:
![Alt text](/FileViewer.png)

Improvements:
- UI: Custom style using css and fxml
- UI: Login Ui for user
- UI: Warn user when login has invalid values
- UI: Warn users when performating upload or download when nothing is selected from the correct menu
- Model: 

Instructions:
- Using Intellij to run program:

- Clone project to desired directory if getting files repository from GitHub
- Extract files to a desired directory if getting files from zip.

- now open up Intellij and click on the top left `File -> Open`
- navigate to the cloned repository Assignment-2---CSCI2020U/Assignment as the project

- Configure Intellij to run the server and client
1. Navigate to `Files -> Project Structures...` and see and make sure that the right sdk version 11+ and jdk version 11+ are installed an applied
2. Navigate to `Run -> Edit Configurations...` if you do not see an FileServerClient then press the + symbol Application. Under build and run select the correct jdk file and version 
then press `Modify Options -> Add VM Options` and type this into the VM options input bar: --module-path "The\Path\To\JDK\lib;out\production" --add-modules=javafx.controls,javafx.fxml
	For the main class to run. select FileServerClient as main
3. Navigate to `Run -> Edit Configurations...` if you do not see an FileServer then press the + symbol Application. Under build and run select the correct jdk file and version 
	then press `Modify Options -> Add VM Options` and type this into the VM options input bar: --module-path "The\Path\To\JDK\lib;out\production" --add-modules=javafx.controls,javafx.fxml
	For the main class to run. select FileServer as main
4. To test the project, Begin by running the FileServer
5. Then switch to FileServerClient and run. a client login menu should appear

- Username is just the user identifier, it can be anything

References:
- https://stackoverflow.com/
- https://docs.oracle.com/javase/8/docs/api/overview-summary.html
- https://www.w3schools.com/

GitHub Repository:
- https://github.com/MatthewSharpOTU/Assignment-2---CSCI2020U.git
