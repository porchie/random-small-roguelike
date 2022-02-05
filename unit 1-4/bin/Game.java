import java.util.ArrayList;


public class Game{
    private String[][] floorData;
    private String floorTile;
    private String wall;
    private int floorNum;
    private int[][] roomSeeds;
    private int roomNums;
    private int floorH;
    private int floorW;
    private Player player;
    private int curPlayerR;
    private int curPlayerC;
    private ArrayList<Enemy> enemyArrayList = new ArrayList<Enemy>();
    private ArrayList<int[]> enemyCoords = new ArrayList<int[]>();
    private int enemyNum;
    private String status;
    private String key;
    private String door;
    private String potion;
    private String strength;
    private boolean onDoor;

   

    public Game(int roomMax, int enemyNum)
    {
      roomNums = roomMax; 
      floorW = 60; 
      floorH = 40;
      floorNum = 0;

      this.enemyNum = enemyNum;
      
      

      floorTile =     
      "<span bgcolor= white>"              + 
      "<font color='white'>"                 + 
      "#"                                  +
      "</span>"                            +
      "</font>"; 
     
      wall = 
      "<span bgcolor = white>" +
      "#"                     +
      "</span>";

      key =     
      "<span bgcolor= white>"              + 
      "<font color='orange'>"              + 
      "K"                                  +
      "</span>"                            +
      "</font>"; 



      door =     
      "<span bgcolor= white>"              + 
      "<font color='lime'>"              + 
      "0"                                  +
      "</span>"                            +
      "</font>"; 
    
      potion =     
      "<span bgcolor= white>"              + 
      "<font color='red'>"                 + 
      "H"                                  +
      "</span>"                            +
      "</font>";
      
      strength =     
      "<span bgcolor= white>"              + 
      "<font color='red'>"                 + 
      "S"                                  +
      "</span>"                            +
      "</font>";

      status = "";
      player = new Player();
      newFloor();
    }
    





    

    public int getFloorNums()
    {
      return floorNum;
    }
   
     /****************** Updating related methods*****************/
    public void updateWorld()
    {
      
      // Updates player
      floorData[curPlayerR][curPlayerC] = floorTile;
      floorData[player.getRow()][player.getCol()] = player.getChar();
      curPlayerR = player.getRow();
      curPlayerC = player.getCol(); 
      
      // Door check
      if (onDoor)
      {
        newFloor();
        return;
      }


      // Enemy Death
      for (int i = 0; i < enemyArrayList.size(); i++)
      {
        Enemy enem = enemyArrayList.get(i);
        
        if (enem.isDead())
        {
          enemyKiller(enem, i);
          status += "Enemy is dead!<br>";
        }
      }

   
      // Enemy behavior
      for (int i = 0; i < enemyArrayList.size(); i++)
      {
        Enemy enem = enemyArrayList.get(i);
        if (canSeePlayer(enem))
        {
          // Can goes towards player and hit them
          aggressive(enem);

        }
        else
        {
          // cannot see player, wanders around 
          wander(enem); 
          
        }
        enemyCoords.get(i)[0] = enem.getRow();
        enemyCoords.get(i)[1] = enem.getCol();

       
      }
     
    }

    public boolean playerDead()
    {
      return player.isDead();
    }

    private void enemyKiller(Enemy E, int index)
    {
      // Random chance to drop health potion
       int rand = (int)(Math.random() * 9) + 1;
       if (rand == 1)
       {
        floorData[E.getRow()][E.getCol()] = potion;
       } 
       else
       {
        floorData[E.getRow()][E.getCol()] = floorTile;
       }


       enemyArrayList.remove(index);
       enemyCoords.remove(index);
    }

    

    


     /****************** Player related methods*****************/
    private void spawnPlayer()
    {
      curPlayerR = roomSeeds[0][0];
      curPlayerC = roomSeeds[0][1];
      player.setRow(curPlayerR);
      player.setCol(curPlayerC);
      floorData[curPlayerR][curPlayerC] = player.getChar();
  
    }

  
   //////////////////////////  Movement  ///////////////////////////


    public void moveUp()
    {
      int up = curPlayerR - 1;
      if (player.getRow() != 0 
      && floorData[up][curPlayerC].equals(floorTile))
      {
        player.moveUp();
      }
      else if(floorData[up][curPlayerC].equals(Enemy.enemyString()))
      {
        
        int col = curPlayerC;
        int index = findEnemy(up, col);
        enemyArrayList.get(index).takeDmg(player.getDmg());
        status += "Enemy took " + player.getDmg() + " damage!<br>";
        

      }
      else if(floorData[up][curPlayerC].equals(key))
      {
        floorData[up][curPlayerC] = floorTile;
        player.moveUp();
        player.keyCollected(true);
        status += "Key collected!<br>";
      }
      else if(floorData[up][curPlayerC].equals(door))
      {
        if (player.getKey())
        {
          onDoor = true;
        }
      }
      else if(floorData[up][curPlayerC].equals(potion))
      {
        floorData[up][curPlayerC] = floorTile;
        player.moveUp();
        player.addPot();
        status += "Health potion collected!<br>";
      }
      else if(floorData[up][curPlayerC].equals(strength))
      {
        floorData[up][curPlayerC] = floorTile;
        player.moveUp();
        player.setDmg(player.getDmg() + 1);
        status += "Strength potion collected!<br>";
      }
    }
    
    public void moveDown()
    {
      int down = curPlayerR + 1;
      if (player.getRow() != floorH - 1
      && floorData[down][curPlayerC].equals(floorTile))
      {
        player.moveDown();
      }
      else if(floorData[down][curPlayerC].equals(Enemy.enemyString()))
      {
        
        int col = curPlayerC;
        int index = findEnemy(down, col);
        enemyArrayList.get(index).takeDmg(player.getDmg());
        status += "Enemy took " + player.getDmg() + " damage!<br>";
   
      }
      else if(floorData[down][curPlayerC].equals(key))
      {
        floorData[down][curPlayerC] = floorTile;
        player.moveDown();
        player.keyCollected(true);
        status += "Key collected!<br>";
      }
      else if(floorData[down][curPlayerC].equals(door))
      {
        if (player.getKey())
        {
          onDoor = true;
        }
      }
      else if(floorData[down][curPlayerC].equals(potion))
      {
        floorData[down][curPlayerC] = floorTile;
        player.moveDown();
        player.addPot();
        status += "Health potion collected!<br>";
      }
      else if(floorData[down][curPlayerC].equals(strength))
      {
        floorData[down][curPlayerC] = floorTile;
        player.moveDown();
        player.setDmg(player.getDmg() + 1);
        status += "Strength potion collected!<br>";
      }
      
    }
    
    public void moveLeft()
    {
      int left = curPlayerC - 1;
      if (player.getCol() != 0
      && floorData[curPlayerR][left].equals(floorTile))
      {
        player.moveLeft();
      }
      else if(floorData[curPlayerR][left].equals(Enemy.enemyString()))
      {
        int row = curPlayerR ;
      
        int index = findEnemy(row, left);
        enemyArrayList.get(index).takeDmg(player.getDmg());
        
        status += "Enemy took " + player.getDmg() + " damage!<br>";
        

      }
      else if(floorData[curPlayerR][left].equals(key))
      {
        floorData[curPlayerR][left] = floorTile;
        player.moveLeft();
        player.keyCollected(true);
        status += "Key collected!<br>";
      }
      else if(floorData[curPlayerR][left].equals(door))
      {
        if (player.getKey())
        {
          onDoor = true;
        }
      }
      else if(floorData[curPlayerR][left].equals(potion))
      {
        floorData[curPlayerR][left] = floorTile;
        player.moveLeft();
        player.addPot();
        status += "Health potion collected!<br>";
      }
      else if(floorData[curPlayerR][left].equals(strength))
      {
        floorData[curPlayerR][left] = floorTile;
        player.moveLeft();
        player.setDmg(player.getDmg() + 1);
        status += "Strength potion collected!<br>";
      }

    }
    public void moveRight()
    {
      int right = curPlayerC + 1;
      if (player.getCol() != floorW - 1
      && floorData[curPlayerR][right].equals(floorTile))
      {
        player.moveRight();
      }
      else if(floorData[curPlayerR][right].equals(Enemy.enemyString()))
      {
        int row = curPlayerR ;
       
        int index = findEnemy(row, right);
        enemyArrayList.get(index).takeDmg(player.getDmg());
        status += "Enemy took " + player.getDmg() + " damage!<br>";
       

      }
      else if(floorData[curPlayerR][right].equals(key))
      {
        floorData[curPlayerR][right] = floorTile;
        player.moveRight();
        player.keyCollected(true);
        status += "Key collected!<br>";
      }
      else if(floorData[curPlayerR][right].equals(door))
      {
        if (player.getKey())
        {
          onDoor = true;
        }
      }
      else if(floorData[curPlayerR][right].equals(potion))
      {
        floorData[curPlayerR][right] = floorTile;
        player.moveRight();
        player.addPot();
        status += "Health potion collected!<br>";
      }
      else if(floorData[curPlayerR][right].equals(strength))
      {
        floorData[curPlayerR][right] = floorTile;
        player.moveRight();
        player.setDmg(player.getDmg() + 1);
        status += "Strength potion collected!<br>";
      }
    }
    ///////////////////////////////////////////////////////////



    // returns index of the enemy in enemyArrayList that the player hit
    private int findEnemy(int row, int col) 
    {
      for (int i = 0; i < enemyCoords.size(); i++)
      {
        if (enemyCoords.get(i)[0] == row && enemyCoords.get(i)[1] == col)
        {
          return i; 
        }
      }
      return 0; //THIS SHOULD NEVER RUN!!!!!!!!!!!!!
    }



    public int usePot()
    {
      if(player.getPots() != 0)
      {
        player.usePot();
        player.takeDmg(-20); // heal amount
        status += "Used a Health Potion<br>";
        return 1;
      }
      else
      {
        status += "You do not have a health potion<br>";
        return 0;
      }

    }




    /***************Enemy Related Methods **********************/
    
    //Populates world with Enemies
    private void populate(int numEnems)
    {
      int scaling = 1;
    
  
      if (floorNum % 15 == 0 && floorNum <= 60)
      {
        scaling++;
      }

      int hp = 4 + ((floorNum / 3) * (3 + scaling));
      int dmg = ((floorNum / 10) + (scaling));
   
    
      for (int i = 1; i <= numEnems; i++)
      {
        spawnEnemy(i,hp, dmg);
      }
    } 

    //Enemy sightline check
    private boolean canSeePlayer(Enemy E)
    { 
       
     int eyeSight = 7;

     int row = E.getRow();
     int col = E.getCol();
     //Enemy to player row distance
     int rDis = curPlayerR - row; 
     //Enemy to player column distance
     int cDis = curPlayerC - col;
     
      
     int rDir = (int)Math.signum((double)rDis);
       
     int cDir = (int)Math.signum((double)cDis);
       
     // Enemies have bad eyesight, Defines how far enemies can see
     if (Math.abs(rDis) >= eyeSight)
     {
       return false;
     }
     if (Math.abs(cDis) >= eyeSight)
     {
        return false;
     }

     //player walled off
     if (floorData[player.getRow() + (rDir * -1)][player.getCol()].equals(wall))
     {
        if 
        (floorData[player.getRow()][player.getCol() + (cDir * -1)].equals(wall))
        {
           return false;
        }
     }

     // Enemy walled off
     if(floorData[row + rDir][col].equals(wall))
     {
        if(floorData[row][col + cDir].equals(wall)) 
        {
          return false;
        }
     }



    //Same row 
     if (rDis == 0)
     {
        for(int i = 1; i <= cDis; i++)
        {
          if (floorData[row][col + (cDir * i) ].equals(wall)) 
          {
            return false;
          }
        }
        return true;
     }

    //Same column
     if (cDis == 0)
     {
        for(int i = 1; i <= rDis; i++)
        {
          if (floorData[row + (rDir * i)][col].equals(wall))
          {
            return false;
          }
        }
        return true;
     }





    // Checks the slope of player to enemy for walls
      int checkR = row + rDir;
      int checkC = col + cDir;

      if (Math.abs(rDis) < Math.abs(cDis))
      {
        double slope = rDis / cDis;
    
        for (int i = 0; i < Math.abs(cDis); i++)
        {
          int slopeVal = (int)(slope * i) * rDir; 
          if 
          (floorData[checkR + slopeVal][checkC + (cDir * i)].equals(wall))
          {
            return false;
          }
        }
      }

      else
      {
        double slope = cDis / rDis;
    
        for (int i = 0; i < Math.abs(rDis); i++)
        {
          int slopeVal = (int)(slope * i) * cDir; 
          if 
          (floorData[checkR + (rDir * i)][checkC + slopeVal].equals(wall))
          {
            return false;
          }
        }
        
      }



      // if can see, which means passed all checks
      return true;       
    }
      
      //random movement of enemy
    private void wander(Enemy E)
    {

      int r = (int)(Math.random() * 5) + 1;
      if (r == 1) // Up   
      {
        if(floorData[E.getRow() - 1][E.getCol()].equals(floorTile))
        {
          floorData[E.getRow() - 1][E.getCol()] = E.getChar();
          floorData[E.getRow()][E.getCol()] = floorTile;
          E.moveUp();
        }     
        else
        {
          r++;
        }
      }


      if (r == 2) // Down
      {
        if (floorData[E.getRow() + 1][E.getCol()].equals(floorTile))
        {
          floorData[E.getRow() + 1][E.getCol()] = E.getChar();
          floorData[E.getRow()][E.getCol()] = floorTile;
          E.moveDown();
        }    
        else
        {
          r++;
        }    
      }

      if (r == 3) // Left
      {
        if (floorData[E.getRow()][E.getCol() - 1].equals(floorTile))
        {
          floorData[E.getRow()][E.getCol() - 1] = E.getChar();
          floorData[E.getRow()][E.getCol()] = floorTile;
          E.moveLeft();
        }
        else
        {
          r++;
        }
      }

      if (r == 4)  // Right
      {
        if (floorData[E.getRow()][E.getCol() + 1].equals(floorTile))
        {
          floorData[E.getRow()][E.getCol() + 1] = E.getChar();
          floorData[E.getRow()][E.getCol()] = floorTile;
          E.moveRight();         
        }       
      }
    }

    // Enemy chases player
    private void aggressive(Enemy E)
    {
      int row = E.getRow();
      int col = E.getCol();
      int rDis = curPlayerR - row; 
      //Enemy to player row distance
      int cDis = curPlayerC - col;
      //Enemy to player column distance
      int rDir = (int)Math.signum((double)rDis); 
      int cDir = (int)Math.signum((double)cDis);

      //attacks player
      if (floorData[row][col + cDir].equals(player.getChar()) || floorData[row + rDir][col].equals(player.getChar()))
      {
        int rand = (int)(Math.random() * 101) + 1;
        if (rand < 70)
        {
          player.takeDmg(E.getDmg());
          status += "You took " + E.getDmg() + " damage<br>";
        }
        else
        {
          status += "Enemy missed! Lucky you!<br>";
        }
        return; //ends method for the enemy
      }  


      int rand = (int)(Math.random() * 2) + 1;

      if (rand == 1)  // first random choice
      {
        if (floorData[row][col + cDir].equals(floorTile))
        {
          if(cDir < 0)
          {
            floorData[E.getRow()][E.getCol()] = floorTile;
            E.moveLeft();
            floorData[E.getRow()][E.getCol()] = E.getChar();
          }
          else if(cDir > 0)
          {
            floorData[E.getRow()][E.getCol()] = floorTile;
            E.moveRight();
            floorData[E.getRow()][E.getCol()] = E.getChar();
          }
        }   
        else if (floorData[row + rDir][col].equals(floorTile))
        {
          if(rDir > 0)
          {
            floorData[E.getRow()][E.getCol()] = floorTile;
            E.moveDown();
            floorData[E.getRow()][E.getCol()] = E.getChar();
          }     
          else 
          { 
            floorData[E.getRow()][E.getCol()] = floorTile;
            E.moveUp();
            floorData[E.getRow()][E.getCol()] = E.getChar();
          } 
        }
      }
     
      


      else  //other random choice
      { 
        if (floorData[row + rDir][col].equals(floorTile))
        {
          if(rDir > 0)
          {
            floorData[E.getRow()][E.getCol()] = floorTile;
            E.moveDown();
            floorData[E.getRow()][E.getCol()] = E.getChar();
          }
          else if (rDir < 0)
          {
            floorData[E.getRow()][E.getCol()] = floorTile;
            E.moveUp();
            floorData[E.getRow()][E.getCol()] = E.getChar();
          } 
        } 
        else if (floorData[row][col + cDir].equals(floorTile))
        {
          if(cDir < 0)
          {
            floorData[E.getRow()][E.getCol()] = floorTile;
            E.moveLeft();
            floorData[E.getRow()][E.getCol()] = E.getChar();
          }
          else
          {
            floorData[E.getRow()][E.getCol()] = floorTile;
            E.moveRight();
            floorData[E.getRow()][E.getCol()] = E.getChar();
          } 
        }
      }     
    }  
      
     



    private void spawnEnemy(int index, int hp, int dmg)
    {
      int eneRow = roomSeeds[index][0]; 
      int eneCol = roomSeeds[index][1];
      if(floorNum < 5)
      {
        enemyArrayList.add(new Enemy(eneRow,eneCol)); //default weak enemy
      }
      else
      {
        enemyArrayList.add(new Enemy(eneRow,eneCol,hp,dmg));
      }
      enemyCoords.add(new int[]{eneRow, eneCol});
      floorData[eneRow][eneCol] = enemyArrayList.get(enemyArrayList.size()-1).getChar();   
    }

    

    

    
    /****************** World gen related  methods*************/

    private void newFloor()
    {
      floorNum++;
      enemyArrayList.clear();
      enemyCoords.clear();
      floorData = new String[floorH][floorW];
      onDoor = false;
      genWorld();
      
      enemyNum = (int)(Math.random() * roomNums - 6) + 6;
      if (floorNum > 20)
      {
        enemyNum = (int)(Math.random() * roomNums - 8) + 8;
      }
      populate(enemyNum);

      player.keyCollected(false);
      spawnPlayer();
      
      genKey();
      
      genDoor();

      if(floorNum % 10 == 0 || (int)(Math.random() * 101) + 1 > 85)
      {
        genStrength();
      }
      
      
      
      int rand = (int)(Math.random() * 4) + 1;
      for(int i = 0; i < rand; i++)
      {
        genPot();
      }

    }
    private void genStrength()
    {
      boolean strGenerated = false;
      while (!strGenerated)
      {
        int r = (int)(Math.random() * floorH - 1) + 1;
        int c = (int)(Math.random() * floorW - 1) + 1;
        if (floorData[r][c].equals(floorTile))
        {
          floorData[r][c] = strength;
          strGenerated = true;
        }
      }
    }

    private void genKey()
    {
      boolean keyGenerated = false;
      while (!keyGenerated)
      {
        int r = (int)(Math.random() * floorH - 1) + 1;
        int c = (int)(Math.random() * floorW - 1) + 1;
        if (floorData[r][c].equals(floorTile))
        {
          floorData[r][c] = key;
          keyGenerated = true;
        }
      }
    }

    
    private void genDoor()
    {
      boolean doorGenerated = false;
      while (!doorGenerated)
      {
        int r = (int)(Math.random() * floorH - 1) + 1;
        int c = (int)(Math.random() * floorW - 1) + 1;
        if (floorData[r][c].equals(floorTile))
        {
          floorData[r][c] = door;
          doorGenerated = true;
        }
      }
    }

    private void genPot()
    {
      boolean potGenerated = false;
      while (!potGenerated)
      {
        int r = (int)(Math.random() * floorH - 1) + 1;
        int c = (int)(Math.random() * floorW - 1) + 1;
        if (floorData[r][c].equals(floorTile))
        {
          floorData[r][c] = potion;
          potGenerated = true;
        }
      }
    }









   
   


    private void genWorld()
    {
     
     // 2D array initialization / generation
    
    
     for (int i = 0; i < floorH; i++)
     {
       for (int j = 0; j < floorW; j++)
       {
         floorData[i][j] = wall;  //wall spaces
       }
     } 

     //Seed generation
     roomSeeds = new int[roomNums][2];
     
     int[] randArr = uniqRand(roomNums);
     for (int i = 0; i < roomNums; i++)
     {
      
      int r = randArr[i];

       roomSeeds[i][0] = (r / floorW + 1) % (floorH - 1);
       if (roomSeeds[i][0] == 0)
       {
         roomSeeds[i][0]++;
       }
       roomSeeds[i][1] = (r) % (floorW - 1);
       if (roomSeeds[i][1] == 0)
       {
         roomSeeds[i][1]++;
       }
     }
     


     //Room generation
     for (int j = 0; j < roomNums; j++)
     {
       
       int row = roomSeeds[j][0];
       int col = roomSeeds[j][1];
       genRoom(row, col);
        
     }
     // connect rooms 
     genConnect(); 
    }


    private int[] uniqRand(int roomNums)
    {
      int[] uniq;
      uniq = new int[roomNums];
      for (int i = 0; i < roomNums; i++)
      {
        int r = (int)(Math.random() * ((floorW*floorH) - floorW + 1)) + 1;
        while (!(uniqInIntArr(uniq, r)))
        {
          r = (int)(Math.random() * ((floorW*floorH) - floorW + 1)) + 1;
        }
        uniq[i] = r;
      }
      return uniq;
    }

    private boolean uniqInIntArr(int[] arr, int r)
    {
      for (int i = 0; i < arr.length; i++)
      {
        if (r == arr[i])
        {
          return false;
        }
      }
      return true;
    }
    
    private void genRoom(int row, int col)
    {
     
     int c = col;
     int r = row;
     
     for (int i = (int)(Math.random() * 5) + 5; i > 0; i--)
     {
      
       for (int j = (int)(Math.random() * 9) + 5; j > 0; j--)
       {
        if (c < floorW - 1  && r < floorH - 1)
        {
          floorData[r][c] = floorTile;
          c++;
        }
       }
       if (r < floorH - 1 && c < floorW - 1)
       {
         floorData[r][c] = floorTile;
          r++;
       }
       c = col;
     }
    }




    private void genConnect()
    {
      
      for (int i = 0; i < roomNums - 1; i++)
      {
        
        int col1 = roomSeeds[i][1];
        int col2 = roomSeeds[i+1][1];
        int row1 = roomSeeds[i][0];
        int row2 = roomSeeds[i+1][0];
        
        int negPosCol = 0;
        int negPosRow = 0;
        if (col1 - col2 < 0)
        {
          negPosCol = 1;
        }
        else 
        {
          negPosCol = -1;
        }
        
         if (row1 - row2 < 0)
        {
          negPosRow = 1;
        }
        else 
        {
          negPosRow = -1;
        }
        
        
       
        
        int newCol = col1;
        int newRow = row1;

        int rand = (int)(Math.random() * 2) + 1;
        int r = (int)(Math.random() * 2) + 1;

        if (rand > 1)  // Randomized Room connection, column first or row first
        {
          for (int k = 0; k < Math.abs(row1 - row2); k++ )
          {
            newRow += negPosRow;
            
            if (!(newRow <= -1 || newRow >= floorH))
            {
              floorData[newRow][col2] = floorTile;
              if (col2 + negPosCol > 0 && col2 + negPosCol < floorW - 1 && r > 1)
                floorData[newRow][col2 + negPosCol] = floorTile;
              // 2 wide connection
            }        
          }

          for (int j = 0; j < Math.abs(col1 - col2); j++ )
          {
           newCol += negPosCol;
          
           if (!(newCol <= 0 || newCol >= floorW))
           {
             floorData[row1][newCol] = floorTile;
             if (row1 + negPosRow > 0 && row1 + negPosRow < floorH - 1 && r > 1)
              floorData[row1 + negPosRow][newCol] = floorTile;
             // 2 wide connection
           }
          }

        }


        else
        {
          for (int j = 0; j < Math.abs(col1 - col2); j++ )
          {
            newCol += negPosCol;
            
           if (!(newCol <= 0 || newCol >= floorW))
           {
             floorData[row1][newCol] = floorTile;
             if (row1 + negPosRow > 0 && row1 + negPosRow < floorW -1 && r > 1)
              floorData[row1 + negPosRow][newCol] = floorTile;
             // 2 wide connection
           }
          }


        
          for (int k = 0; k < Math.abs(row1 - row2); k++ )
          {
            newRow += negPosRow;
             if (!(newRow <= 0 || newRow >= floorH))
             {
               floorData[newRow][col2] = floorTile;
               if (col2 + negPosCol > 0 && col2 + negPosCol < floorH - 1 && r > 1)
                floorData[newRow][col2 + negPosCol] = floorTile;
               // 2 wide connection
             }        
          }
        }
      }
    }




    



    /****************** Display related methods*****************/
    public String status()
    {
      String str = "<html>";
      str += "Player Hp: " + player.getHp() + "<br>";
      str += "Player has key: " + player.getKey() + "<br>";
      str += "Health Potions: " + player.getPots() + "<br>";
      str += "Floor " + floorNum + "<br><br>";

      
      str += status;
      str += "</html>";


      status = "";
      return str;
    }
    
    public String toString()
    {
    
      String str = "<html><br>";
   

      int viewBox = 4; // player "eyesight" even worse
      int leftMost = player.getCol() - (viewBox * 2);
      if (leftMost < 0)
      {
        leftMost = 0;
      }
      int rightMost = player.getCol() + (viewBox * 2);
      if (rightMost > floorW - 1)
      {
        rightMost = floorW - 1;
      }
      int top = player.getRow() - viewBox;
      if (top < 0)
      {
        top = 0;
      }
      int bot = player.getRow() + viewBox;
      if (bot > floorH - 1)
      {
        bot = floorH - 1;
      }

      for (int i = top; i <= bot; i++)
      {
        
        for (int j = leftMost; j <= rightMost; j++)
        {
          
          str += floorData[i][j];
         
        }
        
        str += "<br>";  // <br> for html
      }
       
      str += "</html>";
      return str;
    }
  



}