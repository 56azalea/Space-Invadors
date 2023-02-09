import javalib.funworld.WorldScene;

// the collision of the unit
class IUnitCollide implements IPredicate<IUnit> {
  IUnit unit;

  IUnitCollide(IUnit unit) {
    this.unit = unit;
  }

  // does this unit collide with other?
  public boolean test(IUnit other) {
    return this.unit.collideWith(other);
  }
}

// the list of units left after the collision
class IUnitLeftList implements IPredicate<IUnit> {
  IList<IUnit> units;

  IUnitLeftList(IList<IUnit> units) {
    this.units = units;
  }

  // units left after the collision with other
  public boolean test(IUnit other) {
    return !this.units.ormap(new IUnitCollide(other));
  }
}

// the existence of the unit in the canvas
class IUnitExist implements IPredicate<IUnit> {

  // does this unit exist in the canvas?
  public boolean test(IUnit t) {
    return 0 < t.getX() && t.getX() < 600 && 0 < t.getY() && t.getY() < 600;
  }
}

// implements a function over the unit, returning a value of type R (one argument)
interface IUnitVisitor<R> extends IFunc<IUnit, R> {
  // for the spaceship
  R visit(SpaceShip ship);

  // for the invader
  R visit(Invader invader);

  // for the bullet
  R visit(Bullet bullet);
}

// moves the unit
class IUnitMove implements IUnitVisitor<IUnit> {
  int x;
  int y;

  IUnitMove(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // the unit
  public IUnit apply(IUnit unit) {
    return unit.accept(this);
  }

  // moves the spaceship
  public IUnit visit(SpaceShip ship) {
    return ship.move(x, y);
  }

  // moves the invader
  public IUnit visit(Invader invader) {
    return invader.move(x, y);
  }

  // moves the bullet
  public IUnit visit(Bullet bullet) {
    return bullet.move(x, y);
  }
}

// acts the unit
class IUnitAct implements IUnitVisitor<IUnit> {

  // the unit
  public IUnit apply(IUnit unit) {
    return unit.accept(this);
  }

  // acts the spaceship
  public IUnit visit(SpaceShip ship) {
    return ship.act();
  }

  // acts the invader
  public IUnit visit(Invader invader) {
    return invader.act();
  }

  // acts the bullet
  public IUnit visit(Bullet bullet) {
    return bullet.act();
  }
}

// implements a function over the unit, returning a value of type R (two arguments)
interface IUnitVisitor2<T, R> extends IFunc2<IUnit, T, R> {
  // for the spaceship
  R visit(SpaceShip ship, T acc);

  // for the invader
  R visit(Invader invader, T acc);

  // for the bullet
  R visit(Bullet bullet, T t);
}

// draws the unit to the WorldScene
class IUnitToScene implements IUnitVisitor2<WorldScene, WorldScene> {

  // the WorldScene, acc
  public WorldScene apply(IUnit unit, WorldScene acc) {
    return unit.accept2(this, acc);
  }

  // draws the spaceship to the acc
  public WorldScene visit(SpaceShip ship, WorldScene acc) {
    return ship.drawToScene(acc);
  }

  // draws the invader to the acc
  public WorldScene visit(Invader invader, WorldScene acc) {
    return invader.drawToScene(acc);
  }

  // draws the bullet to the acc
  public WorldScene visit(Bullet bullet, WorldScene acc) {
    return bullet.drawToScene(acc);
  }
}