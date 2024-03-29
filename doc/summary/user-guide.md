---
title: "CMPT 370 Group A3"
subtitle: "User Guide"
date: "November 18, 2015"
toc: true
---

-------------------------------------------------------------

## Getting started

(This guide assumes an executable jar has been distributed to the user)

Please start the provided software and you will see the following screen:

## Main Menu

![](MainMenu.png)\  

This menu presents you with 3 options:

- Watch Match: Watch the game being played out on the game board
- Instant Mode: Have the computer play the match instantly and display the results
- Test Bench: Manually run commands on specific robots to test their functionality

To watch a RoboSport match, please select the "Watch Match" option by clicking 
the appropriate button.

## Loading Teams

![](TeamLoadScreen.png)\  

After choosing "Watch Match" from the Main Menu you will arrive at a screen 
which allows you to load robots onto teams to participate in a match. For each 
of the 6 teams you see a "Load" button. Clicking this button for a team allows 
you to select a JSON encoded robot specification. 

It is possible to start a robot match with any number of robots up to a maximum 
of 4 per team. Notice the robots' names displayed in the text area as you 
import the robots.

Once you are satisfied with the robots on each team select the "confirm" option 
in the top-right corner.

## Viewing a Match

![](GameBoard.png)\  

On the Game board screen you'll notice two clear portions; the display area on 
the left and the control area on the right. In the display area you can see the 
positions of the robots on the board and watch them as they move around. In the 
control area you can find transport controls to alter the speed at which the 
match plays. To start the match, press the "start" button. At this point you 
can use the scrubber to change the speed that moves take place. You may use the 
"Stop" button to pause the match, then use "Play" to resume. The "Step" button 
will progress the match one robot's turn at a time.

## Using Test View

![](TestBench.png)\  

The test view reacts similarly to Match View, but has additional functionality 
to interact with the robots.

On the Test View screen, there is a terminal for user input. The following commands are supported:

### help
Prints out the list of available commands.

### list
Lists all robots that are on the game board.

### select (int)team (int)robot
Sets the currently selected robot to the given team and robot.

### kill
Kills the currently selected robot.

### move (int)x (int)y
Sets the currently selected robot's position to the given coordinates.
