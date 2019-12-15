# Bean-Java
Java interpreter for the custom language of Bean

## TO RUN .BEAN FILES

Right now, the .bean file needs to be in the same directory as all the other files. I am still learning the syntax of Java,
if anyone knows how to set up the files to create a bean run command from any directory, let me know.

The run command for Mac:
  java BeanInterpreter program

This will run the file program.bean, to run a different file, change the filename in the command

## SYNTAX

Printing is done a la Python but a touch of Java, i.e. print(expression);
print() function does carry a newline, in the future a formatted print statement will come without newline, i.e. printf()

Current primitive data types are: (char)acter, (str)ing, (int)eger, double, and (bool)ean
Current binary operators are: '+', '-', '*', '/', '%', and '**'(exponent)

Much like Java, chars are treated as integers when operated on with other integers. String operaters have been created,
with '+' tacking on the literal representation of the type, as well as 'INT * STR' creating a repeated string.

Variables do exist, they be declared, assigned, and changed. The names must start with a letter,
and can contain letters of '_'. To be honest, I've been pushing off numbers in variable names to focus on more higher up
things, but I'll get to it. Typecasting isn't a thing yet, so right now there is no way to turn a double into an integer.

### SAMPLE PROGRAM:
'''
int foo = 4 + 12;
print(foo * 6);

foo = foo / 8;

double foobar = 5.0;
foo = foo ** foobar;
print("" + 'c' + true + foo);
'''
### OUTPUT:
'''
96
ctrue32.0
'''

Things to work on:
  -Typecasting: This will allow doubles to be converted to integer types
  -Comparison, logical, and bitwise operators: It'll expand more of the options the programmer has
  -If, for, and while loops: A crucial part of making the coding not just linear
  -Functions: Brings the high-level structure of most modern languages, i.e. not goto
  -Arrays, lists, stacks, and queues
    -I havent decided if I want the "list" type to be bounded of unbounded
    -I know I at least want same type and passed by reference
    -It's easy, so stacks and queues are going to become built-in data types

If you have any questions, comments, or suggestions about any of the code, email me at splavacado100@gmail.com

Happy coding!
