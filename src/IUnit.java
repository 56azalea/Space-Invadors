import java.awt.Color;

import javalib.funworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

// Interface for a unit
interface IUnit {
  // the x position of the unit
  int getX();

  // the y position of the unit
  int getY();

  // the x y position of the unit
  CartPt getPos();

  // the width of the unit
  int getWidth();

  // the height of the unit
  int getHeight();

  // fires bullets from the unit
  IUnit fire();

  // acts the unit
  IUnit act();

  // the collision of the unit with other
  boolean collideWith(IUnit other);

  // draws the unit
  WorldImage draw();

  // draws the unit to the WorldScene, acc
  WorldScene drawToScene(WorldScene acc);

  // returns the result of applying the given visitor to this unit (one argument)
  <R> R accept(IUnitVisitor<R> visitor);

  // returns the result of applying the given visitor to this unit (two arguments)
  <T, R> R accept2(IUnitVisitor2<T, R> visitor, T acc);
}

// Abstract class for a unit
abstract class AUnit implements IUnit {
  CartPt pos;
  Color color;
  int width;
  int height;

  AUnit(CartPt pos, Color color, int width, int height) {
    this.pos = pos;
    this.color = color;
    this.width = width;
    this.height = height;
  }

  // the x position of the unit
  public int getX() {
    return this.pos.getX();
  }

  // the y position of the unit
  public int getY() {
    return this.pos.getY();
  }

  // the x y position of the unit
  public CartPt getPos() {
    return this.pos;
  }

  // the width of the unit
  public int getWidth() {
    return this.width;
  }

  // the height of the unit
  public int getHeight() {
    return this.height;
  }

  // the collision of the unit with other
  public boolean collideWith(IUnit other) {
    int xDist = this.getX() - other.getX();
    int yDist = this.getY() - other.getY();
    int xSpace = (this.getWidth() + other.getWidth()) / 2;
    int ySpace = (this.getHeight() + other.getHeight()) / 2;
    return -xSpace <= xDist && xDist <= xSpace && -ySpace <= yDist && yDist <= ySpace;
  }

  // draws the unit with the shape of a rectangle
  public WorldImage draw() {
    return new RectangleImage(this.width, this.height, OutlineMode.SOLID, this.color);
  }

  // draws the unit to the scene
  public WorldScene drawToScene(WorldScene acc) {
    return acc.placeImageXY(this.draw(), getX(), getY());
  }
}

// a spaceship
class SpaceShip extends AUnit {
  int dx;

  SpaceShip(CartPt pos, Color color, int width, int height, int dx) {
    super(pos, color, width, height);
    this.dx = dx;
  }

  SpaceShip(CartPt pos, int dx) {
    this(pos, Color.black, 50, 20, dx);
  }

  // fires bullets from the spaceship
  public IUnit fire() {
    return new Bullet(this.pos, this.color, -10);
  }

  // acts the spaceship
  public IUnit act() {
    SpaceShip moved = this.move(this.dx, 0);
    int leftEdge = moved.getX() - moved.width / 2;
    int rightEdge = moved.getX() + moved.width / 2;
    if (leftEdge < 0) {
      return this;
    }
    else if (rightEdge > 600) {
      return this;
    }
    else {
      return moved;
    }
  }

  // shifts the spaceship by x and y
  SpaceShip move(int x, int y) {
    return new SpaceShip(this.pos.move(x, y), Color.black, 50, 20, this.dx);
  }

  // returns the result of applying the given visitor to this spaceship (one
  // argument)
  public <R> R accept(IUnitVisitor<R> visitor) {
    return visitor.visit(this);
  }

  // returns the result of applying the given visitor to this spaceship (two
  // arguments)
  public <T, R> R accept2(IUnitVisitor2<T, R> visitor, T acc) {
    return visitor.visit(this, acc);
  }
}

// an invader
class Invader extends AUnit {

  Invader(CartPt pos, Color color, int width, int height) {
    super(pos, color, width, height);
  }

  Invader() {
    this(new CartPt(0, 0), Color.red, 20, 20);
  }

  // fires bullets from the invader
  public IUnit fire() {
    return new Bullet(this.pos, this.color, 10);
  }

  // acts the invader
  public IUnit act() {
    return this.fire();
  }

  // shifts the invader by x and y
  Invader move(int x, int y) {
    return new Invader(this.pos.move(x, y), Color.red, 20, 20);
  }

  // returns the result of applying the given visitor to this invader (one
  // argument)
  public <R> R accept(IUnitVisitor<R> visitor) {
    return visitor.visit(this);
  }

  // returns the result of applying the given visitor to this invader (two
  // arguments)
  public <T, R> R accept2(IUnitVisitor2<T, R> visitor, T acc) {
    return visitor.visit(this, acc);
  }
}

// a bullet
class Bullet extends AUnit {
  int dy;

  Bullet(CartPt pos, Color color, int width, int height, int dy) {
    super(pos, color, width, height);
    this.dy = dy;
  }

  Bullet(CartPt pos, Color color, int dy) {
    this(pos, color, 5, 5, dy);
  }

  // fires bullets from the bullet
  public IUnit fire() {
    throw new IllegalArgumentException("Invalid fire");
  }

  // acts the bullet
  public IUnit act() {
    return this.move(0, this.dy);
  }

  // shifts the bullet by x and y
  Bullet move(int x, int y) {
    return new Bullet(this.pos.move(x, y), this.color, this.dy);
  }

  // returns the result of applying the given visitor to this bullet (one
  // argument)
  public <R> R accept(IUnitVisitor<R> visitor) {
    return visitor.visit(this);
  }

  // returns the result of applying the given visitor to this bullet (two
  // arguments)
  public <T, R> R accept2(IUnitVisitor2<T, R> visitor, T acc) {
    return visitor.visit(this, acc);
  }
}