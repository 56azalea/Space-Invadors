// a cartesian point
class CartPt {
  int x;
  int y;

  CartPt(int x, int y) {
    this.x = x;
    this.y = y;
  }

  // the x position
  public int getX() {
    return this.x;
  }

  // the y position
  public int getY() {
    return this.y;
  }

  // the x y position of the unit
  CartPt getPos() {
    return new CartPt(this.x, this.y);
  }

  // determines if two cartesian points represent the same point
  public boolean equal(CartPt that) {
    return this.x == that.x && this.y == that.y;
  }

  // shifts the cartesian point by x and y
  CartPt move(int x, int y) {
    return new CartPt(this.x + x, this.y + y);
  }
}

// compares two cartesian points
class IsSameCart implements IPredicate<CartPt> {
  CartPt target;

  IsSameCart(CartPt target) {
    this.target = target;
  }

  // returns true if the given CartPt is contained within the target and false
  // otherwise
  public boolean test(CartPt that) {
    return that.equal(this.target);
  }
}

// compares the lists of cartesian points
class CartPtPredicate implements IPredicate<CartPt> {
  IList<CartPt> points;

  CartPtPredicate(IList<CartPt> points) {
    this.points = points;
  }

  // returns true if the given CartPt is contained within points and false
  // otherwise
  public boolean test(CartPt pos) {
    return this.points.ormap(new IsSameCart(pos));
  }
}