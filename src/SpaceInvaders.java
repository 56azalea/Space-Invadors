import java.awt.Color;
import java.util.Random;

import javalib.funworld.World;
import javalib.funworld.WorldScene;
import javalib.worldimages.FontStyle;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.TextImage;
import tester.Tester;

// a world class animating the spaceship and the invaders
class SpaceInvaders extends World {
  IUnit ship;
  IList<IUnit> invaders;
  IList<IUnit> shipBullets;
  IList<IUnit> invadersBullets;

  SpaceInvaders(IUnit ship, IList<IUnit> invaders) {
    this.ship = ship;
    this.invaders = invaders;
    this.shipBullets = new MtList<IUnit>();
    this.invadersBullets = new MtList<IUnit>();
  }

  SpaceInvaders(IUnit ship, IList<IUnit> invaders, IList<IUnit> shipBullets,
      IList<IUnit> invadersBullets) {
    this.ship = ship;
    this.invaders = invaders;
    this.shipBullets = shipBullets;
    this.invadersBullets = invadersBullets;
  }

  // draws the spaceship and the invaders onto the background (empty scene)
  public WorldScene makeScene() {
    IList<IUnit> targets = this.invaders.append(shipBullets).append(invadersBullets).cons(ship);
    return targets.foldr(new IUnitToScene(), new WorldScene(600, 600));
  }

  // the world after every tick of the clock
  public World onTick() {
    // when the spaceship has been hit by an invader bullet
    if (this.invadersBullets.ormap(new IUnitCollide(this.ship))) {
      this.endOfWorld("You lost!");
    }
    // when all invaders have been eliminated
    else if (this.invaders.length() == 0) {
      this.endOfWorld("You won!");
    }
    // invaders left after the collision
    IList<IUnit> invaders = this.invaders.filter(new IUnitLeftList(this.shipBullets));
    IList<IUnit> shipBullets = this.shipBullets.filter(new IUnitLeftList(this.invaders));
    // manages bullets
    shipBullets = shipBullets.map(new IUnitAct()).filter(new IUnitExist());
    IList<IUnit> invadersBullets = this.invadersBullets.map(new IUnitAct())
        .filter(new IUnitExist());
    Random random = new Random();
    if (this.invadersBullets.length() < 10) {
      // manages the difficulty of the game
      if (random.nextDouble() < 0.3) {
        // chooses a number of invaders getting to fire bullets randomly
        IUnit choosed = this.invaders.choose(random.nextInt(this.invaders.length()));
        IUnit bullet = choosed.fire();
        return new SpaceInvaders(this.ship.act(), invaders, shipBullets,
            invadersBullets.cons(bullet));
      }
    }
    return new SpaceInvaders(this.ship.act(), invaders, shipBullets, invadersBullets);
  }

  // fires bullets when the space bar is pressed by the user
  public World onKeyReleased(String key) {
    if (key.equals(" ")) {
      if (this.shipBullets.length() < 3) {
        IUnit bullet = this.ship.fire();
        return new SpaceInvaders(this.ship, this.invaders, this.shipBullets.cons(bullet),
            this.invadersBullets);
      }
    }
    return this;
  }

  // allows the user to move the spaceship left and right
  public World onKeyEvent(String key) {
    if (key.equals("left")) {
      return new SpaceInvaders(new SpaceShip(this.ship.getPos(), -10), this.invaders,
          this.shipBullets, this.invadersBullets);
    }
    else if (key.equals("right")) {
      return new SpaceInvaders(new SpaceShip(this.ship.getPos(), 10), this.invaders,
          this.shipBullets, this.invadersBullets);
    }
    else {
      return this;
    }
  }

  // the last scene before the world is ended
  public WorldScene lastScene(String message) {
    return new WorldScene(600, 600)
        .placeImageXY(new TextImage("Game Over", 36, FontStyle.BOLD, Color.red), 600 / 2, 600 / 2);
  }
}

// examples of Space Invaders Game
class ExamplesSpaceInvaders {
  ExamplesSpaceInvaders() {
  }

  CartPt cp00 = new CartPt(0, 0);
  CartPt cp11 = new CartPt(1, 1);
  CartPt cp22 = new CartPt(2, 2);
  IList<CartPt> mt = new MtList<CartPt>();
  IList<CartPt> list1 = new MtList<CartPt>().cons(this.cp00).cons(this.cp11);
  IList<CartPt> list2 = new MtList<CartPt>().cons(this.cp00).cons(this.cp11).cons(this.cp22);
  Utils utility = new Utils();
  IUnit ship = new SpaceShip(new CartPt(300, 570), Color.black, 50, 20, 0);
  IList<IUnit> invaders = utility.buildInvaderList(4, 9);
  SpaceInvaders world = new SpaceInvaders(this.ship, this.invaders);

  // tests the method getX
  boolean testGetX(Tester t) {
    return t.checkExpect(this.cp00.getX(), 0) && t.checkExpect(this.cp11.getX(), 1)
        && t.checkExpect(this.cp22.getX(), 2);
  }

  // tests the method getY
  boolean testGetY(Tester t) {
    return t.checkExpect(this.cp00.getY(), 0) && t.checkExpect(this.cp11.getY(), 1)
        && t.checkExpect(this.cp22.getY(), 2);
  }

  // tests the method equal
  boolean testEqual(Tester t) {
    return t.checkExpect(this.cp00.equal(this.cp00), true)
        && t.checkExpect(this.cp00.equal(this.cp11), false)
        && t.checkExpect(this.cp11.equal(this.cp00), false)
        && t.checkExpect(this.cp11.equal(this.cp11), true)
        && t.checkExpect(this.cp11.equal(this.cp22), false);
  }

  // tests the method move
  boolean testMove(Tester t) {
    return t.checkExpect(this.cp00.move(1, 1), this.cp11)
        && t.checkExpect(this.cp11.move(5, 6), new CartPt(6, 7))
        && t.checkExpect(this.cp22.move(4, -1), new CartPt(6, 1));
  }

  // tests the method test
  boolean testTest(Tester t) {
    return t.checkExpect(new IsSameCart(this.cp00).test(cp00), true)
        && t.checkExpect(new IsSameCart(this.cp11).test(cp00), false)
        && t.checkExpect(new IsSameCart(this.cp00).test(cp11), false)
        && t.checkExpect(new IsSameCart(this.cp11).test(cp22), false)
        && t.checkExpect(new CartPtPredicate(this.mt).test(this.cp00), false)
        && t.checkExpect(new CartPtPredicate(this.mt).test(this.cp11), false)
        && t.checkExpect(new CartPtPredicate(this.list1).test(this.cp00), true)
        && t.checkExpect(new CartPtPredicate(this.list1).test(this.cp22), false);
  }

  // tests the method cons
  boolean testCons(Tester t) {
    return t.checkExpect(this.mt.cons(this.cp00), new MtList<CartPt>().cons(this.cp00))
        && t.checkExpect(this.list1.cons(this.cp22), this.list2)
        && t.checkExpect(this.list2.cons(new CartPt(3, 3)), this.list2.cons(new CartPt(3, 3)));
  }

  // tests the method append
  boolean testAppend(Tester t) {
    return t.checkExpect(this.mt.append(this.list1), this.list1)
        && t.checkExpect(this.mt.append(this.list2), this.list2);
  }

  // tests the method length
  boolean testLength(Tester t) {
    return t.checkExpect(this.mt.length(), 0) && t.checkExpect(this.list1.length(), 2);
  }

  // tests the method choose
  boolean testChoose(Tester t) {
    return t.checkExpect(this.list1.choose(0), this.cp11)
        && t.checkExpect(this.list2.choose(2), this.cp00);
  }

  // tests the method getPos
  boolean testGetPos(Tester t) {
    return t.checkExpect(this.cp00.getPos(), this.cp00)
        && t.checkExpect(this.cp11.getPos(), this.cp11)
        && t.checkExpect(this.ship.getPos(), new CartPt(300, 570));
  }

  // tests the method getWidth
  boolean testGetWidth(Tester t) {
    return t.checkExpect(this.ship.getWidth(), 50) && t
        .checkExpect(new RectangleImage(10, 30, OutlineMode.SOLID, Color.pink).getWidth(), 10.0);
  }

  // tests the method getHeight
  boolean testGetHeight(Tester t) {
    return t.checkExpect(this.ship.getHeight(), 20) && t
        .checkExpect(new RectangleImage(10, 30, OutlineMode.SOLID, Color.pink).getHeight(), 30.0);
  }

  // tests the method fire
  boolean testFire(Tester t) {
    return t.checkExpect(this.ship.fire(), new Bullet(new CartPt(300, 570), Color.black, -10))
        && t.checkExpect(new Invader().fire(), new Bullet(new CartPt(0, 0), Color.red, 10));
  }

  // tests the method act
  boolean testAct(Tester t) {
    return t.checkExpect(this.ship.act(), this.ship)
        && t.checkExpect(new Invader().act(), new Bullet(new CartPt(0, 0), Color.red, 10))
        && t.checkExpect(new Bullet(new CartPt(5, 6), Color.yellow, 4),
            new Bullet(new CartPt(5, 6), Color.yellow, 4));
  }

  // tests the method collideWith
  boolean testCollideWith(Tester t) {
    return t.checkExpect(this.ship.collideWith(new Invader()), false)
        && t.checkExpect(this.ship.collideWith(this.ship), true);
  }

  // tests the method draw
  boolean testDraw(Tester t) {
    return t.checkExpect(this.ship.draw(),
        new RectangleImage(50, 20, OutlineMode.SOLID, Color.black))
        && t.checkExpect(new Invader().draw(),
            new RectangleImage(20, 20, OutlineMode.SOLID, Color.red))
        && t.checkExpect(new Bullet(new CartPt(5, 10), Color.yellow, 4).draw(),
            new RectangleImage(5, 5, OutlineMode.SOLID, Color.yellow));
  }

  // tests the method drawToScene
  boolean testDrawToScene(Tester t) {
    WorldScene world1 = new WorldScene(600, 600);
    WorldScene world2 = new WorldScene(300, 400);
    return t.checkExpect(this.ship.drawToScene(world1),
        world1.placeImageXY(this.ship.draw(), 300, 570))
        && t.checkExpect(this.ship.drawToScene(world2),
            world2.placeImageXY(this.ship.draw(), 300, 570))
        && t.checkExpect(new Invader().drawToScene(world1),
            world1.placeImageXY(new Invader().draw(), 0, 0));
  }

  // tests the method makeScene
  boolean testMakeScene(Tester t) {
    return t.checkExpect(this.world.makeScene(),
        this.invaders.cons(this.ship).foldr(new IUnitToScene(), new WorldScene(600, 600)));
  }

  // tests the method onKeyReleased
  boolean testOnKeyReleased(Tester t) {
    return t.checkExpect(this.world.onKeyReleased("left"), this.world)
        && t.checkExpect(this.world.onKeyReleased(" "), new SpaceInvaders(this.ship, this.invaders,
            new MtList<IUnit>().cons(this.ship.fire()), new MtList<IUnit>()));
  }

  // tests the method onKeyEvent
  boolean testOnKeyEvent(Tester t) {
    return t.checkExpect(this.world.onKeyEvent(" "), this.world)
        && t.checkExpect(this.world.onKeyEvent("left"),
            new SpaceInvaders(new SpaceShip(this.ship.getPos(), -10), this.invaders,
                new MtList<IUnit>(), new MtList<IUnit>()))
        && t.checkExpect(this.world.onKeyEvent("right"),
            new SpaceInvaders(new SpaceShip(this.ship.getPos(), 10), this.invaders,
                new MtList<IUnit>(), new MtList<IUnit>()));
  }

  // tests the method lastScene
  boolean testLastScene(Tester t) {
    return t.checkExpect(this.world.lastScene("Game Over"), new WorldScene(600, 600)
        .placeImageXY(new TextImage("Game Over", 36, FontStyle.BOLD, Color.red), 600 / 2, 600 / 2));
  }

  boolean testBigBang(Tester t) {
    int worldWidth = 600;
    int worldHeight = 600;
    double tickRate = .1;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }
}