// This creates a function named Foo(); but we can use Foo as an object if
// we put a new in the call, as with foo1 below. Notice how "this" changes from
// an object to the Window instance depending on the caller's use of new or not:
function Foo(x) {
    this.value = x;
    console.log("Foo() - this.value: "+this.value)
    console.log("Foo() - this: "+this);
}
Foo.prototype.getValue = function () {
  return this.value;
};

// Here we are using Foo as an object:
foo1 = new Foo(2);
console.log("foo1: "+JSON.stringify(foo1));
console.log("foo1.value: "+foo1.value);
console.log("foo1.getValue(): "+foo1.getValue())

// We can legally use Foo() as a function,
// but we're not going to get the same results
// because we skipped over "new " this time:
foo2 = Foo(3);
console.log("foo2: "+JSON.stringify(foo2));
