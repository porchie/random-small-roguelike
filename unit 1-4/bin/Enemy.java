public class Enemy extends MovingEnt{
 
 private static String enemyChar = 
    "<span bgcolor= white>"              + 
    "<font color='purple'>"              + 
    "E"                                  +
    "</span>"                            +
    "</font>"; 
                    
   

  public Enemy(int EposRow, int EposCol, int hp, int dmg) 
  {
    setChar(enemyChar);
    setRow(EposRow);
    setCol(EposCol);
    setDmg(dmg);
    setHp(hp);
  }

  public Enemy(int EposRow, int EposCol)
  {
    setChar(enemyChar);
    setRow(EposRow);
    setCol(EposCol);
    setDmg(1);
    setHp(5);
  }

  public static String enemyString()
  {
    return enemyChar;
  }

  



 
}




