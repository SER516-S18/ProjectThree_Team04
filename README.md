# ASU SER516 Spring 2018 - Project Two (Team 04)
> Client/server application

## Team members
>
- David Henderson ([@david-henderson](https://github.com/david-henderson) | dchende2 | davidhenderson@asu.edu)
- Shreyas Hosahalli Govindaraja ([@shreyashg027](https://github.com/shreyashg027) | shosahal | shosahal@asu.edu)
- Drishty Kapoor ([@Drishtykapoor](https://github.com/Drishtykapoor) | dkapoor3 | dkapoor3@asu.edu)
- Yiru Hu ([@yiruhu](https://github.com/yiruhu) | yiruhu | yiruhu@asu.edu)
- Paulo Jaimer ([@pjaimeSchool](https://github.com/pjaimeSchool) | pjaime | pjaime@asu.edu)
- Saheb Johar ([@sahebjohar92](https://github.com/sahebjohar92) | ssjohar | ssjohar@asu.edu)
- Jeb Johnson ([@jajohn64](https://github.com/jajohn64) | jajohn64 | jajohn64@asu.edu)
- Nagarjuna Kalluri ([@nkalluri11](https://github.com/nkalluri11) | nkalluri | nkalluri@asu.edu)
- Sai Saran Kandimalla ([@saran1856](https://github.com/saran1856) | skandim2 | skandim2@asu.edu)
- Vineesha Kasam ([@VineeshaKasam](https://github.com/VineeshaKasam) | vkasam | vkasam@asu.edu)
- Venkata Sai Shirisha Kakarla ([@kvsshirisha](https://github.com/kvsshirisha) | vkakarla | vkakarla@asu.edu)

## Running our Project Two - Team 04 Client and Server
Compile all classes (and packages) with the two jar dependencies with:
```
javac -cp ".;./lib/jcommon-1.0.23.jar;./lib/jfreechart-1.0.19.jar" src/team04/project2/*.java src/team04/project2/constants/*.java src/team04/project2/listeners/*.java src/team04/project2/util/*.java src/team04/project2/ui/*.java src/team04/project2/model/*.java src/team04/project2/model/client/*.java src/team04/project2/model/server/*.java
```

After compiling, run the Client with:
```
java -cp ".;./src/;./lib/jcommon-1.0.23.jar;./lib/jfreechart-1.0.19.jar" team04.project2.ClientApp
```

After compiling, run the Server with:
```
java -cp ".;./src/;./lib/jcommon-1.0.23.jar;./lib/jfreechart-1.0.19.jar" team04.project2.ServerApp
```

Switch between the client and server application without rebooting by typing "init server" or "init client" in the console's input field.

Also, create a .jar file for the code and run with the following command,
'java -jar ProjectTwo_Team04.jar -init  client;' for client and 'java -jar ProjectTwo_Team04.jar -init  server;' for server
