
# stdf2xls4
stdf2xls4 is a java 1.8 program for converting STDF files into a spreadsheet.
It is licensed under the GPL Version 3.

The source code is implemented as an eclipse project, so if you want to 
compile the source code you will need to import it into eclipse as a git project.

# usage:
You must have java 1.8 or later installed on your computer.
The jar file is int the dist directory.  The user manual is in the doc directory.
To run it use the following command:

java -jar path_to_jar_file -x spreadsheet.xlsx \*.stdf

See the user manual for and explanation of all the options
and other features.  To get a quick overview of the options
you can use the command:

java -jar path_to_jar_file --help

Since java is a memory hog, large sets of stdf files may require
more than the default amount of memory used by the java virtual machine.
In that case use the following:

java -mx8G -jar path_to_jar_file ...

