// a list of a generic argument
interface IList<T> {
  // adds a generic argument to the list
  IList<T> cons(T t);

  // appends the lists
  IList<T> append(IList<T> list);

  // counts the number of items in the list
  int length();

  // chooses an item in the list
  T choose(int n);

  // constructs a list by applying func to each item on the list
  <U> IList<U> map(IFunc<T, U> func);

  // applies func from right to left to each item in the list and acc
  <U> U foldr(IFunc2<T, U, U> func, U acc);

  // produces a list from those items on the list for which pred holds
  IList<T> filter(IPredicate<T> pred);

  // determines whether pred holds for every item on the list
  boolean andmap(IPredicate<T> pred);

  // determines whether pred holds for at least one item on the list
  boolean ormap(IPredicate<T> pred);
}

// an empty list of a generic argument
class MtList<T> implements IList<T> {
  // adds a generic argument to the empty list
  public IList<T> cons(T t) {
    return new ConsList<T>(t, this);
  }

  // appends the list to the empty list
  public IList<T> append(IList<T> list) {
    return list;
  }

  // counts the number of items in the empty list
  public int length() {
    return 0;
  }

  // chooses an item in the empty list
  public T choose(int n) {
    throw new IllegalArgumentException("Cannot choose an element from an empty list");
  }

  // constructs a list by applying func to each item on the empty list
  public <U> IList<U> map(IFunc<T, U> func) {
    return new MtList<U>();
  }

  // applies func from right to left to each item in the empty list and acc
  public <U> U foldr(IFunc2<T, U, U> func, U acc) {
    return acc;
  }

  // produces a list from those items on the empty list for which pred holds
  public IList<T> filter(IPredicate<T> pred) {
    return this;
  }

  // determines whether pred holds for every item on the empty list
  public boolean andmap(IPredicate<T> pred) {
    return true;
  }

  // determines whether pred holds for at least one item on the empty list
  public boolean ormap(IPredicate<T> pred) {
    return false;
  }
}

// a non-empty list of a generic argument
class ConsList<T> implements IList<T> {
  T first;
  IList<T> rest;

  ConsList(T first, IList<T> rest) {
    this.first = first;
    this.rest = rest;
  }

  // adds a generic argument to the non-empty list
  public IList<T> cons(T t) {
    return new ConsList<T>(t, this);
  }

  // appends the list to the non-empty list
  public IList<T> append(IList<T> list) {
    return this.rest.append(list).cons(this.first);
  }

  // counts the number of items in the non-empty list
  public int length() {
    return 1 + this.rest.length();
  }

  // chooses an item in the non-empty list
  public T choose(int n) {
    if (n == 0) {
      return this.first;
    }
    else {
      return this.rest.choose(n - 1);
    }
  }

  // constructs a list by applying func to each item on the non-empty list
  public <U> IList<U> map(IFunc<T, U> func) {
    return new ConsList<U>(func.apply(this.first), this.rest.map(func));
  }

  // applies func from right to left to each item in the non-empty list and acc
  public <U> U foldr(IFunc2<T, U, U> func, U acc) {
    return func.apply(this.first, this.rest.foldr(func, acc));
  }

  // produces a list from those items on the non-empty list for which pred holds
  public IList<T> filter(IPredicate<T> pred) {
    if (pred.test(this.first)) {
      return new ConsList<T>(this.first, this.rest.filter(pred));
    }
    else {
      return this.rest.filter(pred);
    }
  }

  // determines whether pred holds for every item on non-empty the list
  public boolean andmap(IPredicate<T> pred) {
    return pred.test(this.first) && this.rest.andmap(pred);
  }

  // determines whether pred holds for at least one item on non-empty the list
  public boolean ormap(IPredicate<T> pred) {
    return pred.test(this.first) || this.rest.ormap(pred);
  }
}