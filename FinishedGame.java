   import java.util.*;
public class FinishedGame
{
	public static void main(String[]args)
	{
		//Variables
			Scanner reader = new Scanner(System.in);
			String direction = "";
			int[][] board = {{0,0,0,0},
							 {0,0,0,0},
							 {0,0,0,0},
							 {0,0,0,0}};
			int[][]board2;
			int score = 0;


		//Start Game
			System.out.println("This is 2048. The board is below. Use WASD to shift around. Other inputs invalid");
			addNum(board);
			addNum(board);

		//Run Game
			while(checkOpen(board))	//Run until board is full
			{
				//Output Board
				System.out.println("Score: " + score);
				printBoard(board);

				//Obtain direction
				direction = reader.nextLine();

				//Check Input
				while(!(direction.equals("w") || direction.equals("a") || direction.equals("s") || direction.equals("d")))
				{
					direction = reader.nextLine();
				}


				//Copy array
				board2 = copyArray(board);

				//Move Board Accordingly and increment score
				if(direction.equals("w"))
					score+=moveUp(board);
				else if(direction.equals("a"))
					score+=moveLeft(board);
				else if(direction.equals("d"))
					score+=moveRight(board);
				else if(direction.equals("s"))
					score+=moveDown(board);
				else
					System.out.println("invalid input");



				//Add New Number if the board moved
				if(checkSame(board, board2))
					System.out.println("no board movement");
				else
					addNum(board);



			}//Check Open Loop End

			//Lost Game Message
			System.out.println("\n\nGame over\nFinal Score: " + score);
			printBoard(board);

	}//End Main
//Methods

static int[][] copyArray(int[][]board)
{
	//Method will copy the contents of an array into a new array
	int[][] board2 = new int[4][4];

	for(int i = 0; i<4; i++)
	{
		for(int x = 0; x<4; x++)
		{
			board2[i][x] = board[i][x];
		}
	}

	//Return Array
	return board2;
}//copyArayEnd


static void addNum(int[][]board)
{
	//Method will add a number to a random open square
		//variables
			int randRow;
			int randCol;
			Random rand = new Random();
			int addedNumber = 2;
			int placeHolder;

		//Calculate what number to add
			placeHolder = rand.nextInt(10);
			if(placeHolder == 9)
				addedNumber = 4;

		//Loop through board until empty spot is filled
			while(checkOpen(board))
			{

				randRow = rand.nextInt(4);
				randCol = rand.nextInt(4);

				if(board[randRow][randCol] == 0)
				{
					board[randRow][randCol] = addedNumber;
					break;
				}
			}
}//add num end


static boolean checkOpen(int[][]board)
{
	//Method will check if there is an open space, return true if there is one
		for(int i = 0; i<4; i++)
		{
			for(int x = 0; x<4; x++)
			{
				if(board[i][x] == 0)
					return true;
			}
		}

		//If no open space, check if there are combinations that could be made
		for(int i = 0; i<3; i++)
		{
			for(int x = 0; x<4; x++)
			{
				if(board[i][x]==board[i+1][x])
					return true;
			}
		}
		for(int i = 3; i>0; i--)
		{
			for(int x = 0; x<4; x++)
			{
				if(board[i][x]==board[i-1][x])
					return true;
			}
		}

		for(int i = 0; i<4; i++)	//Column
		{
			for(int x = 0; x<3; x++)	//Row
			{
				if(board[i][x+1] == board[i][x])
				return true;
			}
		}

		for(int i = 0; i<4; i++)	//Column
		{
			for(int x = 3; x>0; x--)	//Row
			{
				if(board[i][x-1] == board[i][x])
				return true;
			}
		}

		//If the method has gotten to this point, there is no open space or matches
		return false;
}//check open end


//Notes for the shifting methods
/*
	Each seperate method moves the numbers in a different direction. It will move the numbers if there is an open spaces.
		If there is not an open space, the method will compare the numbers next to each other and see if they could be
		added together.

	The methods work by starting on the "highest" number for that method (up method is the first number of a column,
		down is the bottom-most number, right is rightmost, etc.) shifting the numbers below it closer, if there are spaces
		or if there can be additions. The method works through the same row or colum multiple times to account for
		shifting of numbers.


	In 2048, there are a limit of additions in one move. For example, a column containing all 2's should combine to
		two 4's, not one 8, not one 4 and two 2's. To make this happen, if a tile is added into, the value of the tile is
		set to neative. The addition part of the method will check to see if the numbers have the same sign, and will not add
		if they are different signs. This is done as it will prevent a given tile from being added into more than once.

*/

static int moveLeft(int[][]board)
{
	//Moves numbers left if space, adds numbers if they are the same, adds to score if needed
	int score = 0;

	//Enter large loop, repeated three times to make sure all numbers are moved
	for(int z=0; z<3; z++)
	{
		for(int i = 0; i<4; i++)	//Column
		{
			for(int x = 0; x<3; x++)	//Row
			{
				if(board[i][x] == 0)
				{
					if(board[i][x+1] != 0)	//If open space and the number to the right is zero, shift left
					{
						board[i][x]=board[i][x+1];
						board[i][x+1] = 0;
					}
				}
				else if(board[i][x+1] == board[i][x])	//If the numbers are the same, attempt to add them
				{
					if(board[i][x]>0)	//Add numbers unless there have been more additions than allowed
					{
						board[i][x] = -(board[i][x+1]+board[i][x]);	//Set the tile to negative so there will not be another addition here
						board[i][x+1] = 0;
						score = score + Math.abs(board[i][x]);	//Increment score
					}
				}
			}//Row
		}//Column
	}//Large loop

	//Make all values positive
	for(int i = 0; i<4; i++)
	{
		for(int x = 0; x<4; x++)
		{
			board[i][x] = Math.abs(board[i][x]);
		}
	}

	//Return Score
	return score;
}//Left end


static int moveRight(int[][]board)
{
	//Moves numbers right if space, adds numbers if they are the same, adds to score if needed
	int score = 0;

	//Enter large loop, repeated three times to make sure all numbers are moved
	for(int z=0; z<3; z++)
	{
		for(int i = 0; i<4; i++)//Column
		{
			for(int x = 3; x>+0; x--)	//Row
			{
				if(board[i][x] == 0)
				{
					if(board[i][x-1] != 0)	//If open space and the number to the left is zero, shift right
					{
						board[i][x]=board[i][x-1];
						board[i][x-1] = 0;
					}
				}
				else if(board[i][x-1] == board[i][x])	//If the numbers are the same, attempt to add them
				{
					if(board[i][x]>0)	//Add numbers unless there have been more additions than allowed
					{
						board[i][x] = -(board[i][x-1]+board[i][x]); //Set the tile to negative so there will not be another addition here
						board[i][x-1] = 0;
						score = score + Math.abs(board[i][x]);	//Increment score
					}
				}
			}//Row
		}//Column
	}//Large loop


	//Make all values positive
	for(int i = 0; i<4; i++)
	{
		for(int x = 0; x<4; x++)
		{
			board[i][x] = Math.abs(board[i][x]);
		}
	}
	//Return Score
	return score;
}//right end



static int moveDown(int[][]board)
{
	//Moves numbers down if space, adds numbers if they are the same, adds to score if needed
	int score = 0;

	//Enter large loop
	for(int z=0; z<3; z++)
	{
		for(int i = 3; i>0; i--)	//column
		{
			for(int x = 0; x<4; x++)	//row
			{
				if(board[i][x] == 0)
				{
					if(board[i-1][x] != 0)	//If open space and the number below is zero, shift down
					{
						board[i][x]=board[i-1][x];
						board[i-1][x] = 0;
					}
				}
				else if(board[i-1][x] == board[i][x])	//If the numbers are the same, attempt to add them
				{
					if(board[i][x]>0)	//Add numbers unless there have been more additions than allowed
					{
						board[i][x] = -(board[i-1][x]+board[i][x]);	//Set the tile to negative so there will not be another addition here
						board[i-1][x] = 0;
						score = score + Math.abs(board[i][x]);	//Increment score
					}
				}
			}//row
		}//column
	}//large loop

	//Make all values positive
	for(int i = 0; i<4; i++)
	{

		for(int x = 0; x<4; x++)
		{
			board[i][x] = Math.abs(board[i][x]);
		}
	}

	//Return Score
	return score;
}//down end

static int moveUp(int[][]board)
{
	//Moves numbers up if space, adds numbers if they are the same, adds to score if needed
	int score = 0;


	//Enter large loop, repeated three times to make sure all numbers are moved
	for(int z=0; z<3; z++)
	{
		for(int i = 0; i<3; i++)	//Column
		{
			for(int x = 0; x<4; x++)	//Row
			{
				if(board[i][x] == 0)
				{
					if(board[i+1][x] != 0)	//If open space and the number below is zero, shift up
					{
						board[i][x]=board[i+1][x];
						board[i+1][x] = 0;
					}
				}
				else if(board[i+1][x] == board[i][x])	//If the numbers are the same, attempt to add them
				{

					if(board[i][x]>0)	//Add numbers unless there have been more additions than allowed
					{
						board[i][x] = -(board[i][x]+board[i+1][x]);	//Set the tile to negative so there will not be another addition here
						board[i+1][x] = 0;
						score = score + Math.abs(board[i][x]);	//Increment score
					}
				}
			}//Row loop end
		}//Column loop end
	}//Large loop end

	//Make all values positive
	for(int i = 0; i<4; i++)
	{

		for(int x = 0; x<4; x++)
		{
			board[i][x] = Math.abs(board[i][x]);
		}
	}

	//Return Score
	return score;
}//up end

static boolean checkSame(int[][]board, int[][]board2)
{
	//This method will return a value depicting if the board has changed at all
	for(int i = 0; i<4; i++)
	{
		for(int x = 0; x<4; x++)
		{
			//Return false if they are not the same
			if(board[i][x] != board2[i][x])
				return false;
		}
	}

	//return true if same
	return true;

}//checkSame end



static void printBoard(int[][]board)
{
	//Variables
		int length;
		int diff;


	//Print board
	for(int i = 0; i<4; i++)
	{
		System.out.println();
		for(int x = 0; x<4; x++)
		{
			length = String.valueOf(board[i][x]).length();
			diff = 4-length;
			if(diff == 4)
				System.out.print("0000" + board[i][x] + "\t");
			else if(diff == 3)
				System.out.print("000" + board[i][x] + "\t");
			else if(diff == 2)
				System.out.print("00" + board[i][x] + "\t");
			else if(diff == 1)
				System.out.print("0" + board[i][x] + "\t");
			else
				System.out.print(board[i][x] + "\t");

		}
	}
}//print board end


/*
Notes:

*/

}//End Program