package testcases;


public class TestClassInsCrea {
	RefClass ref1=new RefClass();
	RefClass ref2=new RefClass(3);
	Person p = new Person() {
        public void eat() {
        }
    };
}
abstract class Person {
    public abstract void eat();
}
