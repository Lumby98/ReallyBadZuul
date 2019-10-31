
import java.util.Set;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.07.31
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room cell, r1, r2, r3, r4, r5,
        r6, r7, r8, r9, r10, r11, r12,
        r13, r14, r15, r16;
      
        // create the rooms
        cell = new Room("in your holding cell");
        r1 = new Room("in a room full of mirrors");
        r2 = new Room("in an empty room");
        r3 = new Room("in an small library");
        r4 = new Room("in another holding cell");
        r5 = new Room("in a long hallway");
        r6 = new Room("in a room filled with weapons");
        r7 = new Room("in a room with a giant hole in the center");
        r8 = new Room("in a shallow room");
        r9 = new Room("in a dining room");
        r10 = new Room("in a room with an well");
        r11 = new Room("in a animale holding");
        r12 = new Room("in a storage room");
        r13 = new Room("in a kitchen");
        r14 = new Room("in a small room");
        r15 = new Room("in a room with jackets laying around");
        r16 = new Room("in a room with a staircase leading up");
        
        Room m1;
        m1 = new Room("in a room with a staircase leading down");
        
        // initialise room exits
       cell.setExits("south", r2);
       
       r1.setExits("east", r2);
       r1.setExits("south", r9);
       
       r2.setExits("north", cell);
       r2.setExits("east", r3);
       r2.setExits("south", r5);
       r2.setExits("west", r1);
       
       r3.setExits("west", r2);
       
       r4.setExits("south", r8);
       
       r5.setExits("north", r2);
       r5.setExits("east", r6);
       r5.setExits("south", r10);
       
       r6.setExits("east", r7);
       r6.setExits("west", r5);
       
       r7.setExits("south", r11);
       r7.setExits("west", r6);
       
       r8.setExits("north", r4);
       r8.setExits("south", r12);
       
       r9.setExits("north", r1);
       r9.setExits("east", r10);
       r9.setExits("south", r13);
       
       r10.setExits("north", r5);
       r10.setExits("south", r14);
       r10.setExits("west", r9);
       
       r11.setExits("north", r7);
       r11.setExits("east", r12);
       r11.setExits("south", r16);
       
       r12.setExits("north", r8);
       r12.setExits("west", r11);
       
       r13.setExits("north", r9);
       r13.setExits("east", r14);
       
       r14.setExits("south", r15);
       r14.setExits("west", r13);
       
       r15.setExits("north", r14);
       r15.setExits("east", r16);
       
       r16.setExits("north", r11);
       r16.setExits("west", r15);
       r16.setExits("up", m1);
       
       m1.setExits("down", r16);
        
       
        
        

        currentRoom = cell;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        printLocationInfo();
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   go quit help");
    }
    
    /**
     * Prints details of current exits
     */
    private void printLocationInfo()
    {
        System.out.println("You are " + currentRoom.getLongDescription());              
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            currentRoom = nextRoom;
           printLocationInfo();
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
