// a container for the functions that don’t have any other class they really need to be part of
class Utils {
  Utils() {
  }

  int maxWidth = 600;
  int maxHeight = 600;
  int invaderWidth = 20;
  int invaderHeight = 20;
  int xMargin = 20;
  int yMargin = 10;

  // builds a row with the columnNum of invaders
  IList<IUnit> buildRow(int columnNum) {
    if (columnNum < 0) {
      throw new IllegalArgumentException("Invalid columNum");
    }
    else if (columnNum == 0) {
      return new MtList<IUnit>();
    }
    else {
      int xPos = (invaderWidth + xMargin) * (columnNum - 1);
      Invader newInvader = new Invader().move(xPos, 0);
      return buildRow(columnNum - 1).cons(newInvader);
    }
  }

  // builds a number of rows with the rowNum and the columnNum of invaders
  IList<IUnit> buildRows(int rowNum, int columnNum) {
    if (rowNum < 0) {
      throw new IllegalArgumentException("Invalid rowNum");
    }
    else if (rowNum == 0) {
      return new MtList<IUnit>();
    }
    else {
      int yPos = (invaderHeight + yMargin) * (rowNum - 1);
      IList<IUnit> row = buildRow(columnNum).map(new IUnitMove(0, yPos));
      return buildRows(rowNum - 1, columnNum).append(row);
    }
  }

  // builds a number of rows with the rowNum and the columnNum of invaders,
  // considering the starting points
  IList<IUnit> buildInvaderList(int rowNum, int columnNum) {
    int xAreaInvader = columnNum * invaderWidth + (columnNum - 1) * xMargin;
    int yAreaInvader = rowNum * invaderHeight + (rowNum - 1) * yMargin;
    int xAreaMargin = maxWidth - xAreaInvader;
    int startX = xAreaMargin / 2 + invaderWidth / 2;
    int startY = yMargin + invaderHeight;

    if (rowNum < 4) {
      throw new IllegalArgumentException("# of rows should be at least 4");
    }
    else if (columnNum < 9) {
      throw new IllegalArgumentException("Each row must have at least 9 invaders");
    }
    else if (xAreaInvader > maxWidth) {
      throw new IllegalArgumentException("Too many columns of invaders");
    }
    else if (yAreaInvader > maxHeight) {
      throw new IllegalArgumentException("Too many rows of invaders");
    }
    else {
      return buildRows(rowNum, columnNum).map(new IUnitMove(startX, startY));
    }
  }
}