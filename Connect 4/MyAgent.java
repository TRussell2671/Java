import java.util.Random;

public class MyAgent extends Agent
{
    Random r;

    /**
     * Constructs a new agent, giving it the game and telling it whether it is Red or Yellow.
     * 
     * @param game The game the agent will be playing.
     * @param iAmRed True if the agent is Red, False if the agent is Yellow.
     */
    public MyAgent(Connect4Game game, boolean iAmRed)
    {
        super(game, iAmRed);
        r = new Random();
    }

    /**
     * The move method is run every time it is this agent's turn in the game. You may assume that
     * when move() is called, the game has at least one open slot for a token, and the game has not
     * already been won.
     * 
     * By the end of the move method, the agent should have placed one token into the game at some
     * point.
     * 
     * After the move() method is called, the game engine will check to make sure the move was
     * valid. A move might be invalid if:
     * - No token was place into the game.
     * - More than one token was placed into the game.
     * - A previous token was removed from the game.
     * - The color of a previous token was changed.
     * - There are empty spaces below where the token was placed.
     * 
     * If an invalid move is made, the game engine will announce it and the game will be ended.
     * 
     */
    public void move()
    {
        if (iCanWin() > -1)
        {
            moveOnColumn(iCanWin());
            System.out.println("I can win");
        }
        else if(theyCanWin() > -1)
        {
            moveOnColumn(theyCanWin());
            System.out.println("They can win");
        }
        else
        {
            moveOnColumn(randomMove()); 
            // add proximity algorithm instead of randomness 
        }
    }
   
    
    /**
     * Drops a token into a particular column so that it will fall to the bottom of the column.
     * If the column is already full, nothing will change.
     * 
     * @param columnNumber The column into which to drop the token.
     */
    public void moveOnColumn(int columnNumber)
    {
        int lowestEmptySlotIndex = getLowestEmptyIndex(myGame.getColumn(columnNumber));   // Find the top empty slot in the column
                                                                                                  // If the column is full, lowestEmptySlot will be -1
        if (lowestEmptySlotIndex > -1)  // if the column is not full
        {
            Connect4Slot lowestEmptySlot = myGame.getColumn(columnNumber).getSlot(lowestEmptySlotIndex);  // get the slot in this column at this index
            if (iAmRed) // If the current agent is the Red player...
            {
                lowestEmptySlot.addRed(); // Place a red token into the empty slot
            }
            else // If the current agent is the Yellow player (not the Red player)...
            {
                lowestEmptySlot.addYellow(); // Place a yellow token into the empty slot
            }
        }
    }

    /**
     * Returns the index of the top empty slot in a particular column.
     * 
     * @param column The column to check.
     * @return the index of the top empty slot in a particular column; -1 if the column is already full.
     */
    public int getLowestEmptyIndex(Connect4Column column) {
        int lowestEmptySlot = -1;
        for  (int i = 0; i < column.getRowCount(); i++)
        {
            if (!column.getSlot(i).getIsFilled())
            {
                lowestEmptySlot = i;
            }
        }
        return lowestEmptySlot;
    }

    /**
     * Returns a random valid move. If your agent doesn't know what to do, making a random move
     * can allow the game to go on anyway.
     * 
     * @return a random valid move.
     */
    public int randomMove()
    {
        int i = r.nextInt(myGame.getColumnCount());
        while (getLowestEmptyIndex(myGame.getColumn(i)) == -1)
        {
            i = r.nextInt(myGame.getColumnCount());
        }
        return i;
    }

    /**
     * Returns the column that would allow the agent to win.
     * 
     * You might want your agent to check to see if it has a winning move available to it so that
     * it can go ahead and make that move. Implement this method to return what column would
     * allow the agent to win.
     *
     * @return the column that would allow the agent to win.
     */
    public int iCanWin()
    {
        int pos = -1;
        for (int i = 0; i < myGame.getColumnCount(); i++)
        {
            for (int j = 5; j >= 0; j--)
            {
                if(myGame.getColumn(i).getSlot(j).getIsRed())
                {
                    if(i <= myGame.getColumnCount() / 2 && j >= myGame.getRowCount() / 2) 
                    {
                        if(checkRight(i, j, true) > -1)
                        {
                            pos = checkRight(i, j, true);
                        }
                    }
//                     if(checkVerticalWin(i, j) > -1)
//                     {
//                         pos = i;
//                     }
//                     else if(checkHorizontalWin(i, j, true) > -1)
//                     {
//                         pos = checkHorizontalWin(i, j, true);
//                     }
//                     else if(checkDiagonalWin(i, j, true) > -1)
//                     {
//                         pos = checkDiagonalWin(i, j, true);
//                     }
                }
            }
        }
        return pos;
    }

    /**
     * Returns the column that would allow the opponent to win.
     * 
     * You might want your agent to check to see if the opponent would have any winning moves
     * available so your agent can block them. Implement this method to return what column should
     * be blocked to prevent the opponent from winning.
     *
     * @return the column that would allow the opponent to win.
     */
    public int theyCanWin()
    {
        int pos = -1;
        for (int i = 0; i < myGame.getColumnCount(); i++)
        {
            for (int j = 0; j < myGame.getRowCount(); j++)
            {
                if(myGame.getColumn(i).getSlot(j).getIsFilled() &&
                   !myGame.getColumn(i).getSlot(j).getIsRed())
                {
                    if(checkVerticalWin(i, j) > -1)
                    {
                        pos = i;
                    }
                    else if(checkHorizontalWin(i, j, false) > -1)
                    {
                        pos = checkHorizontalWin(i, j, false);
                    }
                    else if(checkDiagonalWin(i, j, false) > -1)
                    {
                        pos = checkDiagonalWin(i, j, false);
                    }
                }
            }
        }
        return pos;
    }
    
    public int checkRight(int column, int row, boolean iAmRed) 
    {
        char[][] board = myGame.getBoardMatrix();
        int countH = 0;
        int countV = 0;
        int countD = 0;
        for(int i = 0; i <= myGame.getColumnCount() / 2; i++) 
        {
           if(board[row][column + i] == 'R')  // Check Horizontal
           {
               countH++;
               if(countH > 2) 
               {
                   System.out.println("Looking Horizontal");
                   return column;
               }
           }
           else if(board[row - i][column] == 'R') // Check Vertical
           {
               countV++;
               if(countV > 2) 
               {
                   System.out.println("Looking vertical");
                   return column;
               }
           }
           else if(board[row - i][column + i] == 'R')  // check Diagonal
           {
               countD++;
               if(countD > 2) 
               {
                   System.out.println("Looking diagonal");
                   return column;
               }
           }
        }
        return -1;
    }
    
     /**
     * Checks to see if there is a possible vertical win
     * 
     * @param column is the column for the position being checked
     * @param row is the row for the position being checked
     * @return int value of the position to be filled
     */
    public int checkVerticalWin(int column, int row)
    {
        int limit = 3;
        int countRed = 0;
        int countYellow = 0;
        Connect4Column checkCol = myGame.getColumn(column);
        for(int i = 0; i < limit; i++)
        {
            if(checkCol.getSlot(row - limit) != null &&       // Check for a red chip and make sure that
               checkCol.getSlot(row - i).getIsRed())          // a possible combination is in bounds.  
            {
                countRed++;
            }
            else if(checkCol.getSlot(row - limit) != null &&  // Check for a yellow chip and make sure
                    !checkCol.getSlot(row - i).getIsRed() &&  // that a winning combo is in bounds.
                    checkCol.getSlot(row - i).getIsFilled())
            {
                countYellow++;
            }
        }
        if((countRed > 2 || countYellow > 2) &&               // If there is 3 chips in a row, agent will fill
           !checkCol.getSlot(row - limit).getIsFilled())      // that column. If blocked by opposite color, agent
        {                                                     // will continue without dropping chip in column.  
            return column;
        }
        return -1;
    }
    
    /**
     * Checks for a horizontal win
     * 
     * @param column is the column for the position being checked
     * @param row is the row for the position being checked
     * @param iAmRed determines if the current chip is Red or Yellow
     * @return int > 1 if horizontal victory.
     */
    public int checkHorizontalWin(int column, int row, boolean iAmRed) throws NullPointerException
    {
        int limit = 3;
        int countRed = 0;
        int countYellow = 0;
        boolean inBounds = myGame.getColumn(column + limit) != null;
        /* If the chip being checked is a red chip, scan the next four spaces for 
         * any other red chips. If there are chips, then add them to a red counter.
         * The same applies for yellow chips.
         */
        if(inBounds)
        {
            try // check from -1 -> 3 for possible combinations of _XXX / X_XX / XX_X / XXX_
            {
                for(int i = 0; i <= limit; i++)
                {
                    Connect4Slot slot = myGame.getColumn(i + column).getSlot(row);
                    boolean red = slot.getIsRed() && iAmRed;
                    boolean yellow = !slot.getIsRed() && slot.getIsFilled() && !iAmRed;
                    if(red)           
                    {
                        countRed++;
                    }
                    else if(yellow)
                    {
                        countYellow++;
                    }
                }
            }
            catch(NullPointerException n) // if column is out of bounds (ie: column 0)
            {
                for(int i = 0; i <= limit; i++)
                {
                    Connect4Slot slot = myGame.getColumn(i + column).getSlot(row);
                    boolean red = slot.getIsRed() && iAmRed;
                    boolean yellow = !slot.getIsRed() && slot.getIsFilled() && !iAmRed;
                    if(red)           
                    {
                        countRed++;
                    }
                    else if(yellow)
                    {
                        countYellow++;
                    }
                }
            }  
        }
        /* if a scan of 4 spaces yeilds 3 of the same color, scan that sequence and
         * find the empty slot.
         */
        if(countRed > 2 || countYellow > 2)
        {
            try // Column 1 - end
            {
                for(int i = -1; i <= limit; i++)
                {
                    Connect4Slot slot = myGame.getColumn(i + column).getSlot(row);
                    int lowestIndex = getLowestEmptyIndex(myGame.getColumn(i + column));
                    boolean blockYellow =  slot.getIsFilled() && !slot.getIsRed() && iAmRed; // Yellow blocks red
                    boolean blockRed = slot.getIsFilled() && slot.getIsRed() && !iAmRed; // Red blocks yellow
                    if(!slot.getIsFilled() && lowestIndex == row)
                    {
                        return i + column; 
                    }
                    else if(blockYellow)
                    {
                        i++;
                    }
                    else if(blockRed)
                    {
                        i++;
                    }
                }
            }
            catch(NullPointerException n) // column 0 error exception
            {
               for(int i = 0; i <= limit; i++)
               {
                    Connect4Slot slot = myGame.getColumn(i + column).getSlot(row);
                    int lowestIndex = getLowestEmptyIndex(myGame.getColumn(i + column));
                    boolean blockYellow =  slot.getIsFilled() && !slot.getIsRed() && iAmRed; // Yellow blocks red
                    boolean blockRed = slot.getIsFilled() && slot.getIsRed() && !iAmRed; // Red blocks yellow
                    if(!slot.getIsFilled() && lowestIndex == row)
                    {
                        return i + column; 
                    }
                    else if(blockYellow)
                    {
                        i++;
                    }
                    else if(blockRed)
                    {
                        i++;
                    }
               } 
            }
        }
        return -1;
    }
    
    /**
     * Checks for a diagonal win
     * 
     * @param column is the column for the position being checked
     * @param row is the row for the position being checked
     * @param iAmRed determines if the current chip is Red or Yellow
     * @return int value if there is a possible diagonal win
     */
    public int checkDiagonalWin(int column, int row, boolean iAmRed) throws NullPointerException
    {
        int limit = 3;
        int countRed = 0;
        int countYellow = 0;
        boolean leftBounds = myGame.getColumn(column + limit) != null && myGame.getColumn(column + limit).getSlot(row - limit) != null;
        boolean rightBounds = myGame.getColumn(column - limit) != null && myGame.getColumn(column - limit).getSlot(row - limit) != null;
       // int[][] pos = new int[column][row];
        if(leftBounds)
        {
           for(int i = 0; i <= limit; i++)
           {
               Connect4Slot slot = myGame.getColumn(column + i).getSlot(row - i);
               boolean red = slot.getIsRed() && iAmRed;
               boolean yellow = !slot.getIsRed() && slot.getIsFilled() && !iAmRed;
               if(red)
               {
                   countRed++;
               }
               else if(yellow)
               {
                   countYellow++;
               }
           }
        }
        else if(rightBounds)
        {
           for(int i = -3; i <= 0; i++)
           {
               Connect4Slot slot = myGame.getColumn(column + i).getSlot(row + i);
               boolean red = slot.getIsRed() && iAmRed;
               boolean yellow = !slot.getIsRed() && slot.getIsFilled() && !iAmRed;
               if(red)
               {
                   countRed++;
               }
               else if(yellow)
               {
                   countYellow++;
               }
           } 
        }
        if(countRed > 2 || countYellow > 2)
        {
            if(leftBounds)
            {
                for(int i = 0; i <= limit; i++)
                {
                    Connect4Slot slot = myGame.getColumn(column + i).getSlot(row - i);
                    int lowestIndex = getLowestEmptyIndex(myGame.getColumn(i + column));
                    boolean blockYellow =  slot.getIsFilled() && !slot.getIsRed() && iAmRed; // Yellow blocks red
                    boolean blockRed = slot.getIsFilled() && slot.getIsRed() && !iAmRed; // Red blocks yellow
                    if(!slot.getIsFilled() && lowestIndex == row)
                    {
                        return i + column; 
                    }
                    else if(blockYellow)
                    {
                        i++;
                    }
                    else if(blockRed)
                    {
                        i++;
                    }
                }
            }
            else if(rightBounds)
            {
                for(int i = -3; i <= 0; i++)
                {
                    Connect4Slot slot = myGame.getColumn(column + i).getSlot(row + i);
                    int lowestIndex = getLowestEmptyIndex(myGame.getColumn(i + column));
                    boolean blockYellow =  slot.getIsFilled() && !slot.getIsRed() && iAmRed; // Yellow blocks red
                    boolean blockRed = slot.getIsFilled() && slot.getIsRed() && !iAmRed; // Red blocks yellow
                    if(!slot.getIsFilled() && lowestIndex == row)
                    {
                        return i + column; 
                    }
                    else if(blockYellow)
                    {
                        i++;
                    }
                    else if(blockRed)
                    {
                        i++;
                    }
                }
            }
        }
        return -1;
    }
    
    /**
     * Returns the name of this agent.
     *
     * @return the agent's name
     */
    public String getName()
    {
        return "My Agent";
    }
}
