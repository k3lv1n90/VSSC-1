README.TXT FOR VSSC
===================================================================

PROJECT TITLE: VIDEO STREAMING SYSTEM - CLIENT
PURPOSE OF PROJECT: A CLIENT PROGRAM FOR VSS DESCRIBED IN DCS ASSIGNMENT-2
VERSION or DATE: v1.0.1741
AUTHORS: ADK
HOW TO START THIS PROJECT: START CLASS

RECOMMENDATIONS FOR NEXT RELEASE:
===================================================================
- SERVER PROGRAM WILL IMPLEMENT AN "ARE YOU ONLINE?" CHECKING
  THIS NEEDS TO BE HANDLED IN SERVER LISTENER
  PLEASE SEE VSSS README FOR DETAILS
  
- TIMEOUT MAY BE ADDED TO CONNECTIONS WHICH REQUIRES REPLY
  SEE SOCKETTIMEOUTEXCEPTION AND SETSOTIMEOUT CLASSES
  
- CLIENTLISTENER THREAD STOPPED BY CLOSING SOCKET IN CLIENT CLASS
  THAT WILL CAUSE CLIENTLISTER.RUN() THROW IOEXCEPTION TO STOP
  MORE SUITABLE WAY CAN BE FOUND TO STOP THREAD

- A BETTER CONSOLE SCREEN STATING USERS, VIDEOS AND NOTIFICATIONS
  LAST EVENTS CAN BE DISPLAYED IN EVENTS FEED
  A CONSOLESCREEN CLASS MAY BE REQUIRED TO DO THIS
  WHICH TAKES ALL PRINT STATEMENTS AND REARRANGES THE CONSOLE AND
  RE-PRINTS EVERYTING AT EACHTIME
  
- EXAMPLE CONSOLE UI
===================================================================
*******************************************************************
************************** VSS CLIENT *****************************
*******************************************************************
===================================================================
STATUS:                         NO OF ONLINE USERS:
TIME:                           NO OF ONLINE VIDEOS:
===================================================================
15:25 : CONNECTED TO SERVER
15:25 : VIDEO LIST RECEIVED
15:28 : JOHN REQUESTED A VIDEO
===================================================================
[1] Connect to server
[2] Display online video list
[3] Download a video
[4] Refresh local video list
[5] Display local video list
[6] Add file to local video list
[7] Remove file from local video list
[8] Disconnect from server
[0] Exit
===================================================================
Select option : 
===================================================================