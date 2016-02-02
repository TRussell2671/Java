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
        if (redCanWin() > -1)
        {
            moveOnColumn(redCanWin());
        }
        else if(yellowCanWin() > -1)
        {
            moveOnColumn(yellowCanWin());
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
        for(int i = 0; i < column.getRowCount(); i++)
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
    public int redCanWin()
    {
        boolean iAmRed = true;
        for(int col = 0; col < myGame.getColumnCount(); col++)
        {
            int blankSlot = getLowestEmptyIndex(myGame.getColumn(col));
            if(myGame.getColumn(blankSlot) != null) {
                if(checkHorizontalWin(iAmRed, col, blankSlot) > -1)
                {
                    return checkHorizontalWin(iAmRed, col, blankSlot);
                }
                if(checkVerticalWin(iAmRed, col, blankSlot) > -1)
                {
                    return checkVerticalWin(iAmRed, col, blankSlot);
                }
                if(checkDiagonalWin(iAmRed, col, blankSlot) > -1)
                {
                    return checkDiagonalWin(iAmRed, col, blankSlot);
                }
            }
        }
        return -1;
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
    public int yellowCanWin()
    {
        boolean iAmRed = false;
        for(int col = 0; col < myGame.getColumnCount(); col++)
        {
            int blankSlot = getLowestEmptyIndex(myGame.getColumn(col));
            if(blankSlot < 0)
            {
                return -1;
            }
            if(checkHorizontalWin(iAmRed, col, blankSlot) > -1)
            {
                return checkHorizontalWin(iAmRed, col, blankSlot);
            }
            if(checkVerticalWin(iAmRed, col, blankSlot) > -1)
            {
                return checkVerticalWin(iAmRed, col, blankSlot);
            }
            if(checkDiagonalWin(iAmRed, col, blankSlot) > -1)
            {
                return checkDiagonalWin(iAmRed, col, blankSlot);
            }
        }
        return -1;
    }

    public int checkHorizontalWin(boolean iAmRed, int column, int slot)
    {
        if(iAmRed)
        {
            if(column >= 3) // RRRB
            {
                if(myGame.getColumn(column - 3).getSlot(slot).getIsRed() && myGame.getColumn(column - 3).getSlot(slot).getIsFilled() && myGame.getColumn(column - 2).getSlot(slot).getIsRed() && myGame.getColumn(column - 2).getSlot(slot).getIsFilled() && myGame.getColumn(column - 1).getSlot(slot).getIsRed() && myGame.getColumn(column - 1).getSlot(slot).getIsFilled())
                {
                    //System.out.println("Checking RRRB at column " + column);
                    return column;
                }
            }
            if(column <= myGame.getColumnCount() - 4) // BRRR
            {
                if(myGame.getColumn(column + 3).getSlot(slot).getIsRed() && myGame.getColumn(column + 3).getSlot(slot).getIsFilled() && myGame.getColumn(column + 2).getSlot(slot).getIsRed() && myGame.getColumn(column + 2).getSlot(slot).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot).getIsRed() && myGame.getColumn(column + 1).getSlot(slot).getIsFilled())
                {
                    //System.out.println("Checking BRRR at column " + column);
                    return column;
                }
            }
            if(column >= 1 && column <= myGame.getColumnCount() - 3) // RBRR
            {
                if(myGame.getColumn(column - 1).getSlot(slot).getIsRed() && myGame.getColumn(column - 1).getSlot(slot).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot).getIsRed() && myGame.getColumn(column + 1).getSlot(slot).getIsFilled() && myGame.getColumn(column + 2).getSlot(slot).getIsRed() && myGame.getColumn(column + 2).getSlot(slot).getIsFilled())
                {
                    //System.out.println("Checking RBRR at column " + column);
                    return column;
                }
            }
            if(column >= 2 && column <= myGame.getColumnCount() - 2) // RRBR
            {
                if(myGame.getColumn(column - 2).getSlot(slot).getIsRed() && myGame.getColumn(column - 2).getSlot(slot).getIsFilled() && myGame.getColumn(column - 1).getSlot(slot).getIsRed() && myGame.getColumn(column - 1).getSlot(slot).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot).getIsRed() && myGame.getColumn(column + 1).getSlot(slot).getIsFilled())
                {
                    //System.out.println("Checking RRBR at column " + column);
                    return column;
                }
            }
        } 
        if(!iAmRed)
        {
            if(column >= 3) // YYYB
            {
                if(!myGame.getColumn(column - 3).getSlot(slot).getIsRed() && myGame.getColumn(column - 3).getSlot(slot).getIsFilled() && !myGame.getColumn(column - 2).getSlot(slot).getIsRed() && myGame.getColumn(column - 2).getSlot(slot).getIsFilled() && !myGame.getColumn(column - 1).getSlot(slot).getIsRed() && myGame.getColumn(column - 1).getSlot(slot).getIsFilled())
                {
                    return column;
                }
            }
            if(column <= myGame.getColumnCount() - 4) // BYYY
            {
                if(!myGame.getColumn(column + 3).getSlot(slot).getIsRed() && myGame.getColumn(column + 3).getSlot(slot).getIsFilled() && !myGame.getColumn(column + 2).getSlot(slot).getIsRed() && myGame.getColumn(column + 2).getSlot(slot).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot).getIsRed() && myGame.getColumn(column + 1).getSlot(slot).getIsFilled())
                {
                    return column;
                }
            }
            if(column >= 1 && column <= myGame.getColumnCount() - 3) // YBYY
            {
                if(!myGame.getColumn(column - 1).getSlot(slot).getIsRed() && myGame.getColumn(column - 1).getSlot(slot).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot).getIsRed() && myGame.getColumn(column + 1).getSlot(slot).getIsFilled() && !myGame.getColumn(column + 2).getSlot(slot).getIsRed() && myGame.getColumn(column + 2).getSlot(slot).getIsFilled())
                {
                    return column;
                }
            }
            if(column >= 2 && column <= myGame.getColumnCount() - 2) // YYBY
            {
                if(!myGame.getColumn(column - 2).getSlot(slot).getIsRed() && myGame.getColumn(column - 2).getSlot(slot).getIsFilled() && !myGame.getColumn(column - 1).getSlot(slot).getIsRed() && myGame.getColumn(column - 1).getSlot(slot).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot).getIsRed() && myGame.getColumn(column + 1).getSlot(slot).getIsFilled())
                {
                    return column;
                }
            }
        }
        return -1;
    }
    
    public int checkVerticalWin(boolean iAmRed, int column, int slot) 
    {
        if(iAmRed) // check red
        {
            //System.out.println("Vertical red is reached " + slot);
            if(slot <= myGame.getRowCount() - 4) // Must already have 3 vertical, so only check valid rows for vertical win
            {
                if(myGame.getColumn(column).getSlot(slot + 1).getIsRed() && myGame.getColumn(column).getSlot(slot + 2).getIsRed() && myGame.getColumn(column).getSlot(slot + 3).getIsRed())
                {
                    //System.out.println("Red Vertical");
                    return column;
                }
            }
        }
        if(!iAmRed) // check yellow
        {
            //System.out.println("Vertical yellow is reached");
            if(slot <= myGame.getRowCount() - 4)
            {
                if(!myGame.getColumn(column).getSlot(slot + 1).getIsRed() && myGame.getColumn(column).getSlot(slot + 1).getIsFilled() && !myGame.getColumn(column).getSlot(slot + 2).getIsRed() && myGame.getColumn(column).getSlot(slot + 2).getIsFilled() && !myGame.getColumn(column).getSlot(slot + 3).getIsRed() && myGame.getColumn(column).getSlot(slot + 3).getIsFilled())
                {
                    //System.out.println("Yellow Vertical");
                    return column;
                }
            }
        }
        return -1;
    }
    
    public int checkDiagonalWin(boolean iAmRed, int column, int slot)
    {
        if(iAmRed)
        {
            if(column >= 3 && slot <= myGame.getRowCount() - 4) // RRRB top down
            {
                if(myGame.getColumn(column - 3).getSlot(slot + 3).getIsRed() && myGame.getColumn(column - 3).getSlot(slot + 3).getIsFilled() && myGame.getColumn(column - 2).getSlot(slot + 2).getIsRed() && myGame.getColumn(column - 2).getSlot(slot + 2).getIsFilled() && myGame.getColumn(column - 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot + 1).getIsFilled())
                {
                    //System.out.println("Checking RRRB top down diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 3 && slot >= myGame.getRowCount() - 3) // RRRB bottom up
            {
                if(myGame.getColumn(column - 3).getSlot(slot - 3).getIsRed() && myGame.getColumn(column - 3).getSlot(slot - 3).getIsFilled() && myGame.getColumn(column - 2).getSlot(slot - 2).getIsRed() && myGame.getColumn(column - 2).getSlot(slot - 2).getIsFilled() && myGame.getColumn(column - 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot - 1).getIsFilled())
                {
                    //System.out.println("Checking RRRB bottom up diagonal at column " + column);
                    return column;
                }
            }
            if(column <= myGame.getColumnCount() - 4 && slot <= myGame.getRowCount() - 4) // BRRR from top down
            {
                if(myGame.getColumn(column + 3).getSlot(slot + 3).getIsRed() && myGame.getColumn(column + 3).getSlot(slot + 3).getIsFilled() && myGame.getColumn(column + 2).getSlot(slot + 2).getIsRed() && myGame.getColumn(column + 2).getSlot(slot + 2).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsFilled())
                {
                    //System.out.println("Checking BRRR top down diagonal at column " + column);
                    return column;
                }
            }
            if(column <= myGame.getColumnCount() - 4 && slot >= myGame.getRowCount() - 3) // BRRR from bottom up
            {
                if(myGame.getColumn(column + 3).getSlot(slot - 3).getIsRed() && myGame.getColumn(column + 3).getSlot(slot - 3).getIsFilled() && myGame.getColumn(column + 2).getSlot(slot - 2).getIsRed() && myGame.getColumn(column + 2).getSlot(slot - 2).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsFilled())
                {
                    //System.out.println("Checking BRRR bottom up diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 1 && column <= myGame.getColumnCount() - 3 && slot >= 1 && slot <= myGame.getRowCount() - 3 ) // RBRR top down
            {
                if(myGame.getColumn(column - 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot - 1).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsFilled() && myGame.getColumn(column + 2).getSlot(slot + 2).getIsRed() && myGame.getColumn(column + 2).getSlot(slot + 2).getIsFilled())
                {
                    //System.out.println("Checking RBRR top down diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 1 && column <= myGame.getColumnCount() - 3 && slot >= myGame.getRowCount() - 4 && slot <= myGame.getRowCount() - 2) // RBRR bottom up
            {
                if(myGame.getColumn(column - 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot + 1).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsFilled() && myGame.getColumn(column + 2).getSlot(slot - 2).getIsRed() && myGame.getColumn(column + 2).getSlot(slot - 2).getIsFilled())
                {
                    //System.out.println("Checking RBRR bottom up diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 2 && column <= myGame.getColumnCount() - 2 && slot >= myGame.getRowCount() - 4 && slot <= myGame.getRowCount() - 2) // RRBR top down
            {
                if(myGame.getColumn(column - 2).getSlot(slot - 2).getIsRed() && myGame.getColumn(column - 2).getSlot(slot - 2).getIsFilled() && myGame.getColumn(column - 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot - 1).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsFilled())
                {
                    //System.out.println("Checking RRBR diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 2 && column <= myGame.getColumnCount() - 2 && slot >= myGame.getRowCount() - 4 && slot <= myGame.getRowCount() - 3) // RRBR bottom up
            {
                if(myGame.getColumn(column - 2).getSlot(slot + 2).getIsRed() && myGame.getColumn(column - 2).getSlot(slot + 2).getIsFilled() && myGame.getColumn(column - 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot + 1).getIsFilled() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsFilled())
                {
                    //System.out.println("Checking RRBR diagonal at column " + column);
                    return column;
                }
            }
        } 
        if(!iAmRed)
        {
            if(column >= 3 && slot <= myGame.getRowCount() - 4) // YYYB top down
            {
                if(!myGame.getColumn(column - 3).getSlot(slot + 3).getIsRed() && myGame.getColumn(column - 3).getSlot(slot + 3).getIsFilled() && !myGame.getColumn(column - 2).getSlot(slot + 2).getIsRed() && myGame.getColumn(column - 2).getSlot(slot + 2).getIsFilled() && !myGame.getColumn(column - 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot + 1).getIsFilled())
                {
                    //System.out.println("Checking YYYB top down diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 3 && slot >= myGame.getRowCount() - 3) // YYYB bottom up
            {
                if(!myGame.getColumn(column - 3).getSlot(slot - 3).getIsRed() && myGame.getColumn(column - 3).getSlot(slot - 3).getIsFilled() && !myGame.getColumn(column - 2).getSlot(slot - 2).getIsRed() && myGame.getColumn(column - 2).getSlot(slot - 2).getIsFilled() && !myGame.getColumn(column - 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot - 1).getIsFilled())
                {
                    //System.out.println("Checking YYYB bottom up diagonal at column " + column);
                    return column;
                }
            }
            if(column <= myGame.getColumnCount() - 4 && slot <= myGame.getRowCount() - 4) // BYYY from top down
            {
                if(!myGame.getColumn(column + 3).getSlot(slot + 3).getIsRed() && myGame.getColumn(column + 3).getSlot(slot + 3).getIsFilled() && !myGame.getColumn(column + 2).getSlot(slot + 2).getIsRed() && myGame.getColumn(column + 2).getSlot(slot + 2).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsFilled())
                {
                    //System.out.println("Checking BYYY top down diagonal at column " + column);
                    return column;
                }
            }
            if(column <= myGame.getColumnCount() - 4 && slot >= myGame.getRowCount() - 3) // BYYY from bottom up
            {
                if(!myGame.getColumn(column + 3).getSlot(slot - 3).getIsRed() && myGame.getColumn(column + 3).getSlot(slot - 3).getIsFilled() && !myGame.getColumn(column + 2).getSlot(slot - 2).getIsRed() && myGame.getColumn(column + 2).getSlot(slot - 2).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsFilled())
                {
                    //System.out.println("Checking BYYY bottom up diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 1 && column <= myGame.getColumnCount() - 3 && slot >= 1 && slot <= myGame.getRowCount() - 3) // YBYY top down
            {
                if(!myGame.getColumn(column - 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot - 1).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsFilled() && !myGame.getColumn(column + 2).getSlot(slot + 2).getIsRed() && myGame.getColumn(column + 2).getSlot(slot + 2).getIsFilled())
                {
                    //System.out.println("Checking YBYY top down diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 1 && column <= myGame.getColumnCount() - 3 && slot >= myGame.getRowCount() - 4 && slot <= myGame.getRowCount() - 2) // YBYY bottom up
            {
                if(!myGame.getColumn(column - 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot + 1).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsFilled() && !myGame.getColumn(column + 2).getSlot(slot - 2).getIsRed() && myGame.getColumn(column + 2).getSlot(slot - 2).getIsFilled())
                {
                    //System.out.println("Checking YBYY bottom up diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 2 && column <= myGame.getColumnCount() - 2 && slot >= myGame.getRowCount() - 4 && slot <= myGame.getRowCount() - 2) // YYBY top down
            {
                if(!myGame.getColumn(column - 2).getSlot(slot - 2).getIsRed() && myGame.getColumn(column - 2).getSlot(slot - 2).getIsFilled() && !myGame.getColumn(column - 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot - 1).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot + 1).getIsFilled())
                {
                    //System.out.println("Checking YYBY diagonal at column " + column);
                    return column;
                }
            }
            if(column >= 2 && column <= myGame.getColumnCount() - 2 && slot >= myGame.getRowCount() - 4 && slot <= myGame.getRowCount() - 3) // YYBY bottom up
            {
                if(!myGame.getColumn(column - 2).getSlot(slot + 2).getIsRed() && myGame.getColumn(column - 2).getSlot(slot + 2).getIsFilled() && !myGame.getColumn(column - 1).getSlot(slot + 1).getIsRed() && myGame.getColumn(column - 1).getSlot(slot + 1).getIsFilled() && !myGame.getColumn(column + 1).getSlot(slot - 1).getIsRed() && myGame.getColumn(column + 1).getSlot(slot - 1).getIsFilled())
                {
                    //System.out.println("Checking YYBY diagonal at column " + column);
                    return column;
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
