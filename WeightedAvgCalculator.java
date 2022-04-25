//Copyright (c) Anudev Gill

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class WeightedAvgCalculator{

    //build 8 double ArrayLists (making them static so they can be used in terminalInput() and fileInput() method), one for each course, which store the user's past weighted average output history for average comparison purposes
    static ArrayList<Double> bioMarksList = new ArrayList<Double>(); //for Biology
    static ArrayList<Double> compSciMarksList = new ArrayList<Double>(); //for Computer Science         
    static ArrayList<Double> chemMarksList = new ArrayList<Double>(); //for Chemistry
    static ArrayList<Double> presSpeakingMarksList = new ArrayList<Double>(); //for Presentations and Speaking
    static ArrayList<Double> frenchMarksList = new ArrayList<Double>(); //for French
    static ArrayList<Double> physicsMarksList = new ArrayList<Double>(); //for Physics
    static ArrayList<Double> functionsMarksList = new ArrayList<Double>(); //for Functions
    static ArrayList<Double> engMarksList = new ArrayList<Double>(); //for AP English
        
    //build variables needed for program, making them static so they can be used in multiple methods
    static Scanner userInput = new Scanner(System.in);
    static DecimalFormat df = new DecimalFormat("0.00"); //DecimalFormat df used to round averages to 2 decimal places
    static int courseChoice = 0;
    static double mark = 0;
    static double numerator = 0;
    static double denominator = 0;
    static double weightedAvg = 0;
    static int marksListSize = 0;
    static double lastAvg = 0;
    static double difference = 0;

    //build 8 int[] arrays that contain Knowledge and Understanding, Thinking, Communication, and Application weightings for each course, in that order
    static int[] bioWeighting = {20, 17, 15, 18}; //for Biology
    static int[] compSciWeighting = {15, 15, 20, 20}; //for Computer Science 
    static int[] chemWeighting = {20, 17, 15, 18}; //for Chemistry
    static int[] presSpeakingWeighting = {15, 15, 20, 20}; //for Presentations and Speaking
    static int[] frenchWeighting = {18, 17, 18, 17}; //for French
    static int[] physicsWeighting = {20, 15, 15, 20}; //for Physics
    static int[] functionsWeighting = {25, 15, 15, 15}; //for Functions
    static int[] engWeighting = {15, 20, 20, 15}; //for AP English
    //build double[] array inputMarks to store user's input for 4 marks (K/U, T, C, A)
    static double[] inputMarks = new double[4];

    //method that stalls program to provide the user time to read instructions; takes parameter int milliseconds that determines how long to stall for 
    public static void stall(int milliseconds){
		
		//try branch to make processor sleep for specified number of milliseconds
		try{
		
			Thread.sleep(milliseconds);
		}
		//catch branch to handle any exceptions
		catch(Exception e){
		
			System.out.println("\nAn error occurred. Please re-run the program, ensuring your input is valid.\n");
		}
    }
    
    //try-catch method to foolproof user's choice for type of input (input into terminal or input into file)
	public static String foolproofInputChoice(){
	
		//build variable called input to store scanner value 
		String input;
		
		//try branch obtains input and returns it
		try{
		
			input = userInput.next();
			
			//if-else branches check if input is either "t" or "f" and if not, print error message and force valid input by calling method again
			//if branch for valid input, returns input value
			if(input.equalsIgnoreCase("t") || input.equalsIgnoreCase("f")){
			
				return input;
			}
			//else branch for invalid input, prints error message and forces valid input by calling method again	
			else{
			
				System.out.println("\nInvalid input. Please re-enter your input, ensuring it is either T\n(for input into the terminal) or F (for input into the file):");
				
				return foolproofInputChoice();
			}
		}
		//catch branch handles exceptions by printing error message, advancing scanner, and forcing valid input by calling method again
		catch(Exception e){
		
			System.out.println("\nInvalid input. Please re-enter your input, ensuring it is either T\n(for input into the terminal) or F (for input into the file):");
			userInput.next();
			
			return foolproofInputChoice();
		}
	}

    //try-catch method to foolproof user's course choice for terminal input
    public static int foolproofCourseChoice(){

        //initialize variable called secondInput to store scanner value
        int secondInput = 0;

        //try branch obtains input and returns it
        try{

            secondInput = userInput.nextInt();
            
            //if-else branches check if input is an integer between 1 and 8 and if not, print error message and force valid input by calling the method again
            //if branch for valid input, returns input value
            if(secondInput >= 1 && secondInput <= 8){

                return secondInput;
            }
            //else branch for invalid input, prints error message and forces valid input by calling method again
            else{

                System.out.println("\nInvalid input. Please re-enter your input, ensuring it is a positive integer\nbetween 1 and 8 (inclusive):\n");
                
                return foolproofCourseChoice();
            }
        }
        //catch branch handles exceptions by printing error message, advancing scanner, and forcing valid input by calling method again 
        catch(Exception e){

            System.out.println("\nInvalid input. Please re-enter your input, ensuring it is a positive integer\nbetween 1 and 8 (inclusive):\n");
            userInput.next();

            return foolproofCourseChoice();
        }
    }

    //try-catch method to foolproof user's mark input for each category for terminal input
    public static double foolproofMarksInput(){

        //initialize variable called thirdInput to store scanner value
        double thirdInput = 0;

        //try branch obtains input and returns it
        try{

            thirdInput = userInput.nextDouble();
            
            //if-else branches check if input is a number between 0 and 100 (inclusive) OR -1 and if not, print error message and force valid input by calling the method again
            //if branch for valid input, returns input value
            if((thirdInput >= 0 && thirdInput <= 100) || thirdInput == -1){

                return thirdInput;
            }
            //else branch for invalid input, prints error message and forces valid input by calling method again
            else{

                System.out.println("\nInvalid input. Please re-enter your input, ensuring it is either a percentage\n(omit the % sign) between 0 and 100 (inclusive) (e.g. 97) or type -1 if the\ncategory was not present:\n");
                
                return foolproofMarksInput();
            }
        }
        //catch branch handles exceptions by printing error message, advancing scanner, and forcing valid input by calling method again
        catch(Exception e){

            System.out.println("\nInvalid input. Please re-enter your input, ensuring it is either a percentage\n(omit the % sign) between 0 and 100 (inclusive) (e.g. 97) or type -1 if the\ncategory was not present:\n");
            userInput.next();

            return foolproofMarksInput();
        }
    }

    //method for input into terminal
    public static void terminalInput() throws IOException{    

        //build variables needed for reading/writing to files for each of the 8 courses in order to create weighted average histories for each course, for average comparison purposes
        
        //variables for reading/writing to Biology file
        File bioMarks = new File("BiologyMarks.txt");
        Scanner bioScan = new Scanner(bioMarks);
        FileWriter bioFileWriter = new FileWriter(bioMarks, true);
        PrintWriter bioOutput = new PrintWriter(bioFileWriter);

        //variables for reading/writing to Computer Science file
        File compSciMarks = new File("ComputerScienceMarks.txt");
        Scanner compSciScan = new Scanner(compSciMarks);
        FileWriter compSciFileWriter = new FileWriter(compSciMarks, true);
        PrintWriter compSciOutput = new PrintWriter(compSciFileWriter);

        //variables for reading/writing to Chemistry file
        File chemMarks = new File("ChemistryMarks.txt");
        Scanner chemScan = new Scanner(chemMarks);
        FileWriter chemFileWriter = new FileWriter(chemMarks, true);
        PrintWriter chemOutput = new PrintWriter(chemFileWriter);

        //variables for reading/writing to Presentations and Speaking file
        File presSpeakingMarks = new File("PresAndSpeakingMarks.txt");
        Scanner presSpeakingScan = new Scanner(presSpeakingMarks);
        FileWriter presSpeakingFileWriter = new FileWriter(presSpeakingMarks, true);
        PrintWriter presSpeakingOutput = new PrintWriter(presSpeakingFileWriter);

        //variables for reading/writing to French file
        File frenchMarks = new File("FrenchMarks.txt");
        Scanner frenchScan = new Scanner(frenchMarks);
        FileWriter frenchFileWriter = new FileWriter(frenchMarks, true);
        PrintWriter frenchOutput = new PrintWriter(frenchFileWriter);

        //variables for reading/writing to Physics file
        File physicsMarks = new File("PhysicsMarks.txt");
        Scanner physicsScan = new Scanner(physicsMarks);
        FileWriter physicsFileWriter = new FileWriter(physicsMarks, true);
        PrintWriter physicsOutput = new PrintWriter(physicsFileWriter);

        //variables for reading/writing to Functions file
        File functionsMarks = new File("FunctionsMarks.txt");
        Scanner functionsScan = new Scanner(functionsMarks);
        FileWriter functionsFileWriter = new FileWriter(functionsMarks, true);
        PrintWriter functionsOutput = new PrintWriter(functionsFileWriter);

        //variables for reading/writing to AP English file
        File engMarks = new File("EnglishMarks.txt");
        Scanner engScan = new Scanner(engMarks);
        FileWriter engFileWriter = new FileWriter(engMarks, true);
        PrintWriter engOutput = new PrintWriter(engFileWriter);

        //clear console screen
        System.out.print("\033[H\033[2J");
        
        //ask user to choose a course to calculate a weighted average on an assessment for and print options
        System.out.println("\nBegin by choosing a course from the below list to calculate a weighted average\nfor by typing the corresponding number (e.g. 1 for Biology, 2 for Computer\nScience, etc.):\n");
        System.out.println("1. Biology (SBI3U)");
        System.out.println("2. Computer Science (ICS3U)");
        System.out.println("3. Chemistry (SCH3U)");
        System.out.println("4. Presentations and Speaking (EPS3O)");
        System.out.println("5. French (FSF3U)");
        System.out.println("6. Physics (SPH3U))");
        System.out.println("7. Functions (MCR3U)");
        System.out.println("8. AP English (ENG3UA)");
        System.out.println("");

        //call foolproofCourseChoice() method to obtain user's input for course choice and store in courseChoice
        courseChoice = foolproofCourseChoice();

        //explain to user how to enter the marks he/she obtained on each category of the assessment
        System.out.println("\nNext, you will be prompted for the percentage you obtained in each category on\nthe assessment. Type in each number as a percentage (omit the % sign),\nbetween 0 and 100 (inclusive). If one or more categories was not present on the\nassessment, type -1 when prompted for that category.");

        //call stall(milliseconds) method and pass in 7000 to stall the program for 7 seconds, providing the user time to read the above instructions
        stall(7000);

        //for loop loops 4 times and takes user's input for each category and stores it in array inputMarks[]
        for(int i = 0; i < 4; i++){

            //if branch checks if first time looping through program and asks user for their K/U mark
            if (i == 0){

                System.out.println("\nEnter the percentage (omit the % sign) you recevied on the KNOWLEDGE AND\nUNDERSTANDING category (e.g. 97.5). If this cateogry was not present on the\nassessment, type -1 instead of a percentage:\n");
            }
            //else if branch checks if second time looping through program and asks user for their T mark
            else if (i == 1){

                System.out.println("\nEnter the percentage (omit the % sign) you recevied on the THINKING category\n(e.g. 85). If this cateogry was not present on the assessment, type -1 instead\nof a percentage:\n");
            }
            //else if branch checks if third time looping through program and asks user for their C mark
            else if (i == 2){

                System.out.println("\nEnter the percentage (omit the % sign) you recevied on the COMMUNICATION\ncategory (e.g. 90). If this cateogry was not present on the assessment, type -1\ninstead of a percentage:\n");
            }
            //else if branch checks if fourth time looping through program and asks user for their A mark
            else if (i == 3){

                System.out.println("\nEnter the percentage (omit the % sign) you recevied on the APPLICATION category (e.g. 100). If this cateogry was not present on the assessment, type -1 instead\nof a percentage:\n");
            }
            //else branch handles errors by printing error message 
            else{

                System.out.println("\nAn error occurred. Please re-run the program, ensuring your input is valid.\n");
            }

            //call foolproofMarksInput() method to obtain user's input for each mark and store in mark
            mark = foolproofMarksInput();
            
            //add mark to inputMarks[i]
            inputMarks[i] = mark;    
        }  

        //if branch checks if user chose Biology for course choice  
        if(courseChoice == 1){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * bioWeighting[i]; //multiply inputMarks[i] by bioWeighting[i] to weight the mark, and add this product to numerator
                    denominator += bioWeighting[i]; //add bioWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg, using df to round the value to 2 decimal places
            System.out.println("\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in BiologyMarks file to bioMarksList
            while(bioScan.hasNextDouble()){

                bioMarksList.add(bioScan.nextDouble());
            }

            //find size of bioMarksList and store in marksListSize
            marksListSize = bioMarksList.size();
            //find last value in bioMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = bioMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                System.out.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Biology average\n");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                System.out.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Biology average\n");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message stating this 
            else{

                System.out.println("\nThis is the SAME AS your LAST Biology average\n");
            }

            //add value of weightedAvg to BiologyMarks file and then close PrintWriter
            bioOutput.println(weightedAvg);
            bioOutput.close();
        }
        //else if branch checks if user chose Computer Science for course choice
        else if(courseChoice == 2){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * compSciWeighting[i]; //multiply inputMarks[i] by compSciWeighting[i] to weight the mark, and add this product to numerator
                    denominator += compSciWeighting[i]; //add compSciWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg, using df to round the value to 2 decimal places
            System.out.println("\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in ComputerScienceMarks file to compSciMarksList
            while(compSciScan.hasNextDouble()){

                compSciMarksList.add(compSciScan.nextDouble());
            }

            //find size of compSciMarksList and store in marksListSize
            marksListSize = compSciMarksList.size();
            //find last value in compSciMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = compSciMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                System.out.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Computer Science average\n");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                System.out.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Computer Science average\n");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message stating this 
            else{

                System.out.println("\nThis is the SAME AS your LAST Computer Science average\n");
            }

            //add value of weightedAvg to ComputerScienceMarks file and then close PrintWriter
            compSciOutput.println(weightedAvg);
            compSciOutput.close();
        }
        //else if branch checks if user chose Chemistry for course choice
        else if(courseChoice == 3){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * chemWeighting[i]; //multiply inputMarks[i] by chemWeighting[i] to weight the mark, and add this product to numerator
                    denominator += chemWeighting[i]; //add chemWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg, using df to round the value to 2 decimal places
            System.out.println("\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in ChemistryMarks file to chemMarksList
            while(chemScan.hasNextDouble()){

                chemMarksList.add(chemScan.nextDouble());
            }

            //find size of chemMarksList and store in marksListSize
            marksListSize = chemMarksList.size();
            //find last value in chemMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = chemMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                System.out.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Chemistry average\n");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                System.out.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Chemistry average\n");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message stating this
            else{

                System.out.println("\nThis is the SAME AS your LAST Chemistry average\n");
            }

            //add value of weightedAvg to ChemistryMarks file and then close PrintWriter
            chemOutput.println(weightedAvg);
            chemOutput.close();
        }
        //else if branch checks if user chose Presentaions and Speaking for course choice
        else if(courseChoice == 4){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * presSpeakingWeighting[i]; //multiply inputMarks[i] by presSpeakingWeighting[i] to weight the mark, and add this product to numerator
                    denominator += presSpeakingWeighting[i]; //add presSpeakingWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg, using df to round the value to 2 decimal places
            System.out.println("\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in PresAndSpeakingMarks file to presSpeakingMarksList
            while(presSpeakingScan.hasNextDouble()){

                presSpeakingMarksList.add(presSpeakingScan.nextDouble());
            }

            //find size of presSpeakingMarksList and store in marksListSize
            marksListSize = presSpeakingMarksList.size();
            //find last value in presSpeakingMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = presSpeakingMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                System.out.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Presentations and Speaking average\n");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                System.out.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Presentations and Speaking average\n");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message stating this 
            else{

                System.out.println("\nThis is the SAME AS your LAST Presentations and Speaking average\n");
            }

            //add value of weightedAvg to PresAndSpeakingMarks file and then close PrintWriter
            presSpeakingOutput.println(weightedAvg);
            presSpeakingOutput.close();
        }
        //else if branch checks if user chose French for course choice
        else if(courseChoice == 5){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * frenchWeighting[i]; //multiply inputMarks[i] by frenchWeighting[i] to weight the mark, and add this product to numerator
                    denominator += frenchWeighting[i]; //add frenchWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg, using df to round the value to 2 decimal places
            System.out.println("\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in FrenchMarks file to frenchMarksList
            while(frenchScan.hasNextDouble()){

                frenchMarksList.add(frenchScan.nextDouble());
            }

            //find size of frenchMarksList and store in marksListSize
            marksListSize = frenchMarksList.size();
            //find last value in frenchMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = frenchMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                System.out.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST French average\n");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                System.out.println("\nThis is " + df.format(difference) + "% LOWER than your LAST French average\n");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message stating this
            else{

                System.out.println("\nThis is the SAME AS your LAST French average\n");
            }

            //add value of weightedAvg to FrenchMarks file and then close PrintWriter
            frenchOutput.println(weightedAvg);
            frenchOutput.close();
        }
        //else if branch checks if user chose Physics for course choice
        else if(courseChoice == 6){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * physicsWeighting[i]; //multiply inputMarks[i] by physicsWeighting[i] to weight the mark, and add this product to numerator
                    denominator += physicsWeighting[i]; //add physicsWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg, using df to round the value to 2 decimal places
            System.out.println("\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in PhysicsMarks file to physicsMarksList
            while(physicsScan.hasNextDouble()){

                physicsMarksList.add(physicsScan.nextDouble());
            }

            //find size of physicsMarksList and store in marksListSize
            marksListSize = physicsMarksList.size();
            //find last value in physicsMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = physicsMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                System.out.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Physics average\n");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                System.out.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Physics average\n");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message stating this
            else{

                System.out.println("\nThis is the SAME AS your LAST Physics average\n");
            }

            //add value of weightedAvg to PhysicsMarks file and then close PrintWriter
            physicsOutput.println(weightedAvg);
            physicsOutput.close();
        }
        //else if branch checks if user chose Functions for course choice
        else if(courseChoice == 7){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * functionsWeighting[i]; //multiply inputMarks[i] by functionsWeighting[i] to weight the mark, and add this product to numerator
                    denominator += functionsWeighting[i]; //add functionsWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg, using df to round the value to 2 decimal places
            System.out.println("\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in FunctionsMarks file to functionsMarksList
            while(functionsScan.hasNextDouble()){

                functionsMarksList.add(functionsScan.nextDouble());
            }

            //find size of functionsMarksList and store in marksListSize
            marksListSize = functionsMarksList.size();
            //find last value in functionshMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = functionsMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                System.out.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Functions average\n");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                System.out.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Functions average\n");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message stating this
            else{

                System.out.println("\nThis is the SAME AS your LAST Functions average\n");
            }

            //add value of weightedAvg to FunctionsMarks file and then close PrintWriter
            functionsOutput.println(weightedAvg);
            functionsOutput.close();
        }
        //else if branch checks if user chose AP English for course choice
        else if(courseChoice == 8){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * engWeighting[i]; //multiply inputMarks[i] by engWeighting[i] to weight the mark, and add this product to numerator 
                    denominator += engWeighting[i]; //add engWeighting[i] to denominator in order to be able to calculate a weighted average    
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg, using df to round the value to 2 decimal places
            System.out.println("\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in EnglishMarks file to engMarksList
            while(engScan.hasNextDouble()){

                engMarksList.add(engScan.nextDouble());
            }

            //find size of engMarksList and store in marksListSize
            marksListSize = engMarksList.size();
            //find last value in engMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = engMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                System.out.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST AP English average\n");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it out, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                System.out.println("\nThis is " + df.format(difference) + "% LOWER than your LAST AP English average\n");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message stating this
            else{

                System.out.println("\nThis is the SAME AS your LAST AP English average\n");
            }

            //add value of weightedAvg to EnglishMarks file and then close PrintWriter
            engOutput.println(weightedAvg);
            engOutput.close();
        }
        //else branch handles errors by printing error message
        else{

            System.out.println("\nAn error occurred. Please re-run the program, ensuring your input is valid.\n");
        }
    }

    //method for input into file, has return type int so that it can be exited prematurely if an error occurs or if user types invalid input
    public static int fileInput() throws IOException{

        //build variables needed for reading/writing to files for each of the 8 courses in order to create weighted average histories for each course, for average comparison purposes

        //variables for reading/writing to Biology file
        File bioMarks = new File("BiologyMarks.txt");
        Scanner bioScan = new Scanner(bioMarks);
        FileWriter bioFileWriter = new FileWriter(bioMarks, true);
        PrintWriter bioOutput = new PrintWriter(bioFileWriter);

        //variables for reading/writing to Computer Science file
        File compSciMarks = new File("ComputerScienceMarks.txt");
        Scanner compSciScan = new Scanner(compSciMarks);
        FileWriter compSciFileWriter = new FileWriter(compSciMarks, true);
        PrintWriter compSciOutput = new PrintWriter(compSciFileWriter);

        //variables for reading/writing to Chemistry file
        File chemMarks = new File("ChemistryMarks.txt");
        Scanner chemScan = new Scanner(chemMarks);
        FileWriter chemFileWriter = new FileWriter(chemMarks, true);
        PrintWriter chemOutput = new PrintWriter(chemFileWriter);

        //variables for reading/writing to Presentations and Speaking file
        File presSpeakingMarks = new File("PresAndSpeakingMarks.txt");
        Scanner presSpeakingScan = new Scanner(presSpeakingMarks);
        FileWriter presSpeakingFileWriter = new FileWriter(presSpeakingMarks, true);
        PrintWriter presSpeakingOutput = new PrintWriter(presSpeakingFileWriter);

        //variables for reading/writing to French file
        File frenchMarks = new File("FrenchMarks.txt");
        Scanner frenchScan = new Scanner(frenchMarks);
        FileWriter frenchFileWriter = new FileWriter(frenchMarks, true);
        PrintWriter frenchOutput = new PrintWriter(frenchFileWriter);

        //variables for reading/writing to Physics file
        File physicsMarks = new File("PhysicsMarks.txt");
        Scanner physicsScan = new Scanner(physicsMarks);
        FileWriter physicsFileWriter = new FileWriter(physicsMarks, true);
        PrintWriter physicsOutput = new PrintWriter(physicsFileWriter);

        //variables for reading/writing to Functions file
        File functionsMarks = new File("FunctionsMarks.txt");
        Scanner functionsScan = new Scanner(functionsMarks);
        FileWriter functionsFileWriter = new FileWriter(functionsMarks, true);
        PrintWriter functionsOutput = new PrintWriter(functionsFileWriter);

        //variables for reading/writing to AP English file
        File engMarks = new File("EnglishMarks.txt");
        Scanner engScan = new Scanner(engMarks);
        FileWriter engFileWriter = new FileWriter(engMarks, true);
        PrintWriter engOutput = new PrintWriter(engFileWriter);

        //variables for reading/writing to MarksInputData file (to take user's input and to output results)
        File marksInputData = new File("MarksInputData.txt");
        Scanner marksInputScan = new Scanner(marksInputData);
        FileWriter marksFileWriter = new FileWriter(marksInputData, true);
        PrintWriter marksOutput = new PrintWriter(marksFileWriter);

        //variables for reading from RunCount file (to determine if user's first time running program)
        File programRunCount = new File("RunCount.txt");
        Scanner runCountScan = new Scanner(programRunCount);

        //initialize variable called courseChoiceTester to temporarily store user's input for course choice
        int courseChoiceTester = 0;

        //try branch obtains input for user's course choice and stores it in courseChoice
        try{

            //obtain user's input for course choice and store in courseChoiceTester
            courseChoiceTester = marksInputScan.nextInt();

            //if-else branches check if input is an integer between 1 and 8, and if not, print error message to file, close PrintWriter, and return 1
            //if branch for valid input, sets courseChoice equal to courseChoiceTester
            if(courseChoiceTester >= 1 && courseChoiceTester <= 8){

                courseChoice = courseChoiceTester;
            }
            //else branch for invalid input, prints error message to MarksInputData file, closes PrintWriter, and returns 1
            else{

                marksOutput.println("\n\nInvalid input for course choice (line 1). Please re-enter your input, ensuring it is a positive integer\nbetween 1 and 8 (inclusive). Then, delete this error message, save the file, and re-run the program.");
                
                marksOutput.close();
                return 1;
            }
        }
        //catch branch handles exceptions
        catch(Exception e){

            //if branch checks if it is user's first time running program (in which case an error is bound to occur becuase MarksInputData file is empty) and delete the contents of the file (by creating a PrintWriter to write to RunCount file and then closing it) to signify that the first time running the program is over; then, return 1
            if(runCountScan.hasNextInt()){

                //build FileWriter runCountOutput to write to RunCount file
                FileWriter runCountOutput = new FileWriter(programRunCount, false);
                
                //close FileWriter to delete contents of file
                runCountOutput.close();
                //return 1 to exit method
                return 1;
            }
            //else branch if it is not user's first time running program, prints error message to MarksInputData file, closes PrintWriter, and returns 1
            else{
                
                marksOutput.println("\n\nInvalid input for course choice (line 1). Please re-enter your input, ensuring it is a positive integer\nbetween 1 and 8 (inclusive). Then, delete this error message, save the file, and re-run the program.");
           
                marksOutput.close();
                return 1;
            }    
        }

        //for loop loops 4 times and takes user's input for each category from MarksInputData file and stores it in array inputMarks[]
        for(int i = 0; i < 4; i++){

            //try branch obtains input and stores it in inputMarks[i]
            try{
            
                mark = marksInputScan.nextDouble();

                //if-else branches check if input is a number between 0 and 100 (inclusive) OR -1 and if not, prints error message to file, closes PrintWriter, and returns 1
                //if branch for valid input, stores input in inputMarks[i]
                if((mark >= 0 && mark <= 100) || mark == -1){

                    inputMarks[i] = mark;
                }
                //else branch for invalid input, prints error message to MarksInputData file, closes PrintWriter, and returns 1
                else{

                    marksOutput.println("\n\nInvalid input for marks (lines 2, 3, 4, and/or 5). Please re-enter your input, ensuring it is either a percentage\n(omit the % sign) between 0 and 100 (inclusive) (e.g. 97) or type -1 if the\ncategory was not present. Then, delete this error message, save the file, and re-run the program.");
                    
                    marksOutput.close();
                    return 1;
                }
            }
            //catch branch handles exceptions by printing error message to MarksInputData file, closing PrintWriter, and returning 1
            catch(Exception e){

                marksOutput.println("\n\nInvalid input for marks (lines 2, 3, 4, and/or 5). Please re-enter your input, ensuring it is either a percentage\n(omit the % sign) between 0 and 100 (inclusive) (e.g. 97) or type -1 if the\ncategory was not present. Then, delete this error message, save the file, and re-run the program.");
                
                marksOutput.close();
                return 1;
            }   
        }  

        //if branch checks if user chose Biology for course choice
        if(courseChoice == 1){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                 //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                 if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * bioWeighting[i]; //multiply inputMarks[i] by bioWeighting[i] to weight the mark, and add this product to numerator
                    denominator += bioWeighting[i]; //add bioWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg to MarksInputData file, using df to round the value to 2 decimal places
            marksOutput.println("\n\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in BiologyMarks file to bioMarksList
            while(bioScan.hasNextDouble()){

                bioMarksList.add(bioScan.nextDouble());
            }

            //find size of bioMarksList and store in marksListSize
            marksListSize = bioMarksList.size();
            //find last value in bioMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = bioMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Biology average");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Biology average");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message to MarksInputData file stating this
            else{

                marksOutput.println("\nThis is the SAME AS your LAST Biology average");
            }

            //add value of weightedAvg to BiologyMarks file and then close PrintWriter
            bioOutput.println(weightedAvg);
            bioOutput.close();
        }
        //else if branch checks if user chose Computer Science for course choice
        else if(courseChoice == 2){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * compSciWeighting[i]; //multiply inputMarks[i] by compSciWeighting[i] to weight the mark, and add this product to numerator
                    denominator += compSciWeighting[i]; //add compSciWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg to MarksInputData file, using df to round the value to 2 decimal places
            marksOutput.println("\n\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in ComputerScienceMarks file to compSciMarksList
            while(compSciScan.hasNextDouble()){

                compSciMarksList.add(compSciScan.nextDouble());
            }

            //find size of compSciMarksList and store in marksListSize
            marksListSize = compSciMarksList.size();
            //find last value in compSciMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = compSciMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Computer Science average");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Computer Science average");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message to MarksInputData file stating this 
            else{

                marksOutput.println("\nThis is the SAME AS your LAST Computer Science average");
            }

            //add value of weightedAvg to ComputerScienceMarks file and then close PrintWriter
            compSciOutput.println(weightedAvg);
            compSciOutput.close();
        }
        //else if branch checks if user chose Chemistry for course choice
        else if(courseChoice == 3){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * chemWeighting[i]; //multiply inputMarks[i] by chemWeighting[i] to weight the mark, and add this product to numerator
                    denominator += chemWeighting[i]; //add chemWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg to MarksInputData file, using df to round the value to 2 decimal places
            marksOutput.println("\n\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in ChemistryMarks file to chemMarksList
            while(chemScan.hasNextDouble()){

                chemMarksList.add(chemScan.nextDouble());
            }

            //find size of chemMarksList and store in marksListSize
            marksListSize = chemMarksList.size();
            //find last value in chemMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = chemMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Chemistry average");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Chemistry average");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message to MarksInputData file stating this
            else{

                marksOutput.println("\nThis is the SAME AS your LAST Chemistry average");
            }

            //add value of weightedAvg to ChemistryMarks file and then close PrintWriter
            chemOutput.println(weightedAvg);
            chemOutput.close();
        }
        //else if branch checks if user chose Presentaions and Speaking for course choice
        else if(courseChoice == 4){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * presSpeakingWeighting[i]; //multiply inputMarks[i] by presSpeakingWeighting[i] to weight the mark, and add this product to numerator
                    denominator += presSpeakingWeighting[i]; //add presSpeakingWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg to MarksInputData file, using df to round the value to 2 decimal places
            marksOutput.println("\n\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in PresAndSpeakingMarks file to presSpeakingMarksList
            while(presSpeakingScan.hasNextDouble()){

                presSpeakingMarksList.add(presSpeakingScan.nextDouble());
            }

            //find size of presSpeakingMarksList and store in marksListSize
            marksListSize = presSpeakingMarksList.size();
            //find last value in presSpeakingMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = presSpeakingMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Presentations and Speaking average");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Presentations and Speaking average");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message to MarksInputData file stating this 
            else{

                marksOutput.println("\nThis is the SAME AS your LAST Presentations and Speaking average");
            }

            //add value of weightedAvg to PresAndSpeakingMarks file and then close PrintWriter
            presSpeakingOutput.println(weightedAvg);
            presSpeakingOutput.close();
        }
        //else if branch checks if user chose French for course choice
        else if(courseChoice == 5){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * frenchWeighting[i]; //multiply inputMarks[i] by frenchWeighting[i] to weight the mark, and add this product to numerator
                    denominator += frenchWeighting[i]; //add frenchWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg to MarksInputData file, using df to round the value to 2 decimal places
            marksOutput.println("\n\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in FrenchMarks file to frenchMarksList
            while(frenchScan.hasNextDouble()){

                frenchMarksList.add(frenchScan.nextDouble());
            }

            //find size of frenchMarksList and store in marksListSize
            marksListSize = frenchMarksList.size();
            //find last value in frenchMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = frenchMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST French average");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% LOWER than your LAST French average");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message to MarksInputData file stating this
            else{

                marksOutput.println("\nThis is the SAME AS your LAST French average");
            }

            //add value of weightedAvg to FrenchMarks file and then close PrintWriter
            frenchOutput.println(weightedAvg);
            frenchOutput.close();
        }
        //else if branch checks if user chose Physics for course choice
        else if(courseChoice == 6){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * physicsWeighting[i]; //multiply inputMarks[i] by physicsWeighting[i] to weight the mark, and add this product to numerator
                    denominator += physicsWeighting[i]; //add physicsWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg to MarksInputData file, using df to round the value to 2 decimal places
            marksOutput.println("\n\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in PhysicsMarks file to physicsMarksList
            while(physicsScan.hasNextDouble()){

                physicsMarksList.add(physicsScan.nextDouble());
            }

            //find size of physicsMarksList and store in marksListSize
            marksListSize = physicsMarksList.size();
            //find last value in physicsMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = physicsMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Physics average");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Physics average");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message to MarksInputData file stating this
            else{

                marksOutput.println("\nThis is the SAME AS your LAST Physics average");
            }

            //add value of weightedAvg to PhysicsMarks file and then close PrintWriter
            physicsOutput.println(weightedAvg);
            physicsOutput.close();
        }
        //else if branch checks if user chose Functions for course choice
        else if(courseChoice == 7){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * functionsWeighting[i]; //multiply inputMarks[i] by functionsWeighting[i] to weight the mark, and add this product to numerator
                    denominator += functionsWeighting[i]; //add functionsWeighting[i] to denominator in order to be able to calculate a weighted average
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg to MarksInputData file, using df to round the value to 2 decimal places
            marksOutput.println("\n\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in FunctionsMarks file to functionsMarksList
            while(functionsScan.hasNextDouble()){

                functionsMarksList.add(functionsScan.nextDouble());
            }

            //find size of functionsMarksList and store in marksListSize
            marksListSize = functionsMarksList.size();
            //find last value in functionshMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = functionsMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST Functions average");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% LOWER than your LAST Functions average");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message to MarksInputData file stating this
            else{

                marksOutput.println("\nThis is the SAME AS your LAST Functions average");
            }

            //add value of weightedAvg to FunctionsMarks file and then close PrintWriter
            functionsOutput.println(weightedAvg);
            functionsOutput.close();
        }
        //else if branch checks if user chose AP English for course choice
        else if(courseChoice == 8){

            //for loop loops through each element in inputMarks (e.g. loops 4 times) to find numerator and denominator from which to calculate a weighted average
            for(int i = 0; i < 4; i++){

                //if branch checks if inputMarks[i] is not equal to -1 and if so, calculates a numerator and denominator becuase it means the category was not absent on the assessment
                if(inputMarks[i] != -1){

                    numerator += inputMarks[i] * engWeighting[i]; //multiply inputMarks[i] by engWeighting[i] to weight the mark, and add this product to numerator 
                    denominator += engWeighting[i]; //add engWeighting[i] to denominator in order to be able to calculate a weighted average    
                }
            }

            //calculate weighted average by dividing numerator by denominator and store in weightedAvg
            weightedAvg = numerator / denominator;
            //print weightedAvg to MarksInputData file, using df to round the value to 2 decimal places
            marksOutput.println("\n\nYour WEIGHTED AVERAGE (to 2 decimal places) is: " + df.format(weightedAvg) + "%");

            //while loop adds each value in EnglishMarks file to engMarksList
            while(engScan.hasNextDouble()){

                engMarksList.add(engScan.nextDouble());
            }

            //find size of engMarksList and store in marksListSize
            marksListSize = engMarksList.size();
            //find last value in engMarksList using marksListSize - 1 and store in lastAvg
            lastAvg = engMarksList.get(marksListSize - 1);
            
            //if branch checks if current weighted average is higher than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            if(weightedAvg > lastAvg){

                difference = weightedAvg - lastAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% HIGHER than your LAST AP English average");
            }
            //else if branch checks if current weighted average is lower than last weighted average, and if so, calculates the difference and prints it to MarksInputData file, using df to round to 2 decimal places
            else if(weightedAvg < lastAvg){

                difference = lastAvg - weightedAvg;
                marksOutput.println("\nThis is " + df.format(difference) + "% LOWER than your LAST AP English average");
            }
            //else branch checks if current weighted average is same as last weighted average, and if so, prints a message to MarksInputData file stating this
            else{

                marksOutput.println("\nThis is the SAME AS your LAST AP English average");
            }

            //add value of weightedAvg to EnglishMarks file and then close PrintWriter
            engOutput.println(weightedAvg);
            engOutput.close();
        }
        //else branch handles errors by printing error message to MarksInputData file, closing PrintWriter, and returning 1
        else{

            marksOutput.println("\n\nAn error occurred. Please re-run the program, ensuring your input is valid.");

            marksOutput.close();
            return 1;
        }

        //once all results are output to MarksInputData file, close PrintWriter and return 0
        marksOutput.close();
        return 0;
    }

    //method to obtain user's input for type of input (input into terminal or input into file) and run corresponding method (e.g. terminalInput() or fileInput())
    public static void inputChoice() throws IOException{

        //build variable called choice to store user's choice for input type
        String choice;

        //clear console creen
        System.out.print("\033[H\033[2J");

        //welcome user and introduce program
        System.out.println("\nWelcome to Weighted Average Calculator! This program calculates weighted\naverages on tests, quizzes, assignments, and other assessments.");
        //ask user how they would like to enter input
        System.out.println("\nWould you like to enter input directly into the terminal or input it into the\ntext file 'MarksInputData.txt'?");
        System.out.println("\nPress T for input into the terminal and F for input into the file.");
        
        //call foolproofInputChoice() method to obtain user's input for type of input (file or terminal) and store in choice 
		choice = foolproofInputChoice();
		
		//if branch if user chooses input from terminal
		if(choice.equalsIgnoreCase("t")){
		
			//clear console screen
			System.out.print("\033[H\033[2J");
			
			//run terminalInput() method
			terminalInput();
		}
		//else if branch if user chooses input from file
		else if(choice.equalsIgnoreCase("f")){
		
			//clear console screen
			System.out.print("\033[H\033[2J");
			
			//explain to user how to enter data from a file
            System.out.println("\nOpen the file 'MarksInputData.txt'. Inside, you will find 5 blank lines\n(lines 1, 2, 3, 4, and 5).");
            
            System.out.println("\nChoose a course from the below list to calculate a weighted average for\nby typing the corresponding number (e.g. 1 for Biology, 2 for Computer\nScience, etc.) on line 1 of the data file.\n");
            System.out.println("1. Biology (SBI3U)");
            System.out.println("2. Computer Science (ICS3U)");
            System.out.println("3. Chemistry (SCH3U)");
            System.out.println("4. Presentations and Speaking (EPS3O)");
            System.out.println("5. French (FSF3U)");
            System.out.println("6. Physics (SPH3U))");
            System.out.println("7. Functions (MCR3U)");
            System.out.println("8. AP English (ENG3UA)");

            System.out.println("\nThen, on lines 2, 3, 4, and 5, type your Knowledge and Understanding, Thinking,\nCommunication, and Application marks respectively (one mark per line). Ensure\nyou enter the marks as a percentage (omit the % sign) between 0 and 100\n(inclusive) (e.g. 97) OR, in the event that one or more categories was absent on\nthe assessment, type -1 on the appropriate line instead of a percentage.");
            
            //call stall(milliseconds) method and pass in 7000 to stall the program for 7 seconds, providing the user time to read the above instructions
            stall(7000);

            System.out.println("\nSave the file, and re-run this program, ensuring you press F for input into the\nfile when prompted once again. Wait for the terminal output (what you're\ncurrently reading) to finish printing. When you open the file again, you will\nfind your output inside it.");
            System.out.println("\nIf you require further help, please reference the provided user manual.\n");
		
			//run fileInput() method
			fileInput();
		}
		//else branch for invalid input, prints an error message
		else{
		
			System.out.println("\nInvalid input. Please re-run the program, ensuring you enter either T or F next time.\n");
		}
    }

    public static void main(String[] args) throws IOException{    

        //call inputChoice() method to run the program
        inputChoice();
    }
}