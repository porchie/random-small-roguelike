public class MovingEnt
{
  private int posRow;
  private int posCol;
  private String entChar;
  private int hitPoints;
  private int damage;


  public String getChar()
  {
    return entChar;
  }

  public void setChar(String set)
  {
    entChar = set;
  }

  public void moveUp()
  {
    posRow--;
  }

  public void moveDown()
  {
    posRow++;
  }

  public void moveLeft()
  {
    posCol--;
  }
  
  public void moveRight()
  {
    posCol++;
  }

  public int getRow()
  {
    return posRow;
  }

  public int getCol()
  {
    return posCol;
  }

  public void setRow(int row)
  {
    posRow = row;
  }

  public void setCol(int col)
  {
    posCol = col;
  }

  public boolean isDead()
  {
    if (hitPoints < 1)
    {
      return true;
    }
    return false;
  }

  public void setDmg(int dmg)
  {
    damage = dmg;
  }

  public void setHp(int hp)
  {
    hitPoints = hp;
  }

  public int getHp()
  {
    return hitPoints;
  }

  public int getDmg()
  {
    return damage;
  }
  public void takeDmg(int dmg)
  {
    hitPoints -= dmg;
  }
}