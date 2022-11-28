# TCP Client-Router-Server LAN Threaded File-Streaming

## You do NOT have permission to use this code for any schoolwork purposes under any circumstances. 

## You do NOT have permission to use this code for any commercial purposes without speaking to me to work out a deal.

This program creates a client-router-server TCP LAN connection, requiring 3 different machines. The client streams a file to the router ("server-router"), and the router forwards the file to the server. The router facilitates the client-server streaming using threading, allowing asynchronicity. Any client or server that connects is immediately threaded, allowing multiple pairs of clients and servers to connect. The program outputs the total message size sent by the client, forwarded by the server-router, and received by the server, allowing for verification that the total file was transmitted. Finally, if the file is not a .txt file, the program attempts to open and play the file in JavaFX on both the client and server machines, to verify transmission.

## THIS PROGRAM REQUIRES JAVAFX TO RUN
### To run WITHOUT JavaFX, or if playing video/audio is not wanted/required:
1) Delete ServerApp.java and ClientApp.java
2) Comment out or delete lines 101-105 in TCPClient.java
3) Comment out or delete lines 80-84 in TCPServer.java

### To run WITH JavaFX, if wanted: Guide to Install JavaFX in VSCode, if JavaFX is not already installed:
1) Download JavaFX from https://gluonhq.com/products/javafx/ for the proper operating system on both the Client and the Server machines. 
2) Unzip the file. 
3) In VSCode, under “Java Projects” in the bottom left corner, click “Referenced Libraries,” click the “+” symbol, navigate to the lib folder in the unzipped file, highlight all of the files, and hit enter. 
4) Go to “Run,” press “Add Configurations,” and in the “launch.json” file, at the bottom after the final “projectName,” add a “,” to the end of the line, press enter, and copy-paste or type the following: "vmArgs": "--module-path \"C:/Users/XXX/XXX/javafx-sdk-17.0.2/lib\" --add-modules javafx.controls,javafx.fxml,javafx.media"
5) Replace the path above with the path to the location of the downloaded JavaFX lib folder.

### To execute:

1) Place ClientApp.java (if using JavaFX) and TCPClient.java on one computer.
2) Place ServerApp.java (if using JavaFX) and TCPServer.java on a second computer that is on the same Wi-Fi network.
3) Place SThread.java and TCPServerRouter.java on a third computer that is on the same Wi-Fi network.
4) Edit TCPClient.java line 13: put the IP of the machine with SThread.java and TCPServerRouter.java on it. 
   #### Note: IP can be found on the other machine in command prompt by typing “ipconfig” and retrieving the IPv4 Address from the section titled “Wireless LAN adapter Wi-Fi.”
6) Edit TCPClient.java line 37: put the file path to the file that you wish for the client to transmit here. This file will be sent to the server.
7) Edit TCPClient.java line 43: put the IP of the machine with ServerApp.java and TCPServer.java on it.
8) Edit TCPServer.java line 13: put the IP of the machine with ClientApp.java and TCPClient.java on it.
9) Edit TCPServer.java line 38: put the file path to where the server should save the file received from the client.
10) Edit TCPClient.java line 40: put the IP of the machine with ClientApp.java and TCPClient.java on it.
11) Run TCPServerRouter.java first.
12) Run TCPServer.java second.
13) WAIT for server to write that it is connected to the router.
14) Run TCPClient.java.
