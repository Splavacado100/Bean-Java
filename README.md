# Bean-Java
Java interpreter for the custom language of Bean

## TO RUN .BEAN FILES

#### Running a text file as a Bean program
Save your program in a text file with any name, for example "myProgram.txt". When the text file is in the same directory as all the .java files, run the command `java BeanInterpreter myProgram.txt` from that directory. **It is important to have .txt at the end,** otherwise the interpreter will look for myProgram.bean

#### Running a Bean program from Geany-Mac
In the folder "Setup Files" you'll find another folder named "Geany-Mac". In it will be a configuration file named "filetypes.Bean.conf". This file creates the syntax highlight for Bean as well as the build commands to run it. Move filetypes.Bean.conf to the folder /Applications/Geany.app/Contents/Resources/share/geany/filedefs. To get to this folder, you'll have to view package contents of your Geany application. In the folder right above (/Applications/Geany.app/Contents/Resources/share/geany), there is a file name "filetype_extensions.conf". Open it, and add the line `Bean=*.bean;` to the file right after the line `Batch=*.bat;*.cmd;*.nt;`. This addition is to help Geany recognize the .bean file as a Bean program. Now scroll down to the end, you should see a line that looks something like: `Script=Graphviz;`. Add `Bean;` to the end of this line (the whole line should look like `Script=Graphviz;Bean;`), this adds Bean to the scripting language category. For these changes to take effect, you have to quit Geany and open it back up again. After that, everything should work! Run your first Bean program by pressing `fn+f5`!

## SYNTAX

Printing is done a la Python but a touch of Java, i.e. print(expression);
print() function does carry a newline, in the future a formatted print statement will come without newline, i.e. printf()

Current primitive data types are: (char)acter, (str)ing, (int)eger, double, and (bool)ean
Current binary operators are: +, -, *, /, and %

Much like Java, chars are treated as integers when operated on with other integers. String operaters have been created,
with '+' tacking on the literal representation of the type.

Variables do exist, they be declared, assigned, and changed. The names must start with a letter,
and can contain letters or '_'. To be honest, I've been pushing off numbers in variable names to focus on more higher up
things, but I'll get to it.

### SAMPLE PROGRAM:
```
int foo = 4 + 12;
print(foo * 6);

foo = foo / 8;

double foobar = 5.0;
foo = foo * (int)foobar;
int c = 99;

print("" + (char)c + true + foo);
```
### OUTPUT:
```
96.0
ctrue10
```

### Things to work on:
```
-Comparison, logical, and bitwise operators

   -It'll expand more of the options the programmer has
 
-If, for, and while loops

   -A crucial part of making the coding not just linear
 
-Functions

   -Brings the high-level structure of most modern languages, i.e. not goto
 
-Arrays, lists, stacks, and queues

   -I havent decided if I want the "list" type to be bounded of unbounded
 
   -I know I at least want same type and passed by reference
 
   -It's easy, so stacks and queues are going to become built-in data types
```
If you have any questions, comments, or suggestions about any of the code, email me at splavacado100@gmail.com

Happy coding!
