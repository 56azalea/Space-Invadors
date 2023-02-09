// Interface for an one-argument function-object with signature [T -> U]
interface IFunc<T, U> {
  U apply(T t);
}

// Interface for two-argument function-objects with signature [A, B -> C]
interface IFunc2<A, B, C> {
  C apply(A a, B b);
}

// Interface for a predicate with a generic argument
interface IPredicate<T> {
  boolean test(T t);
}