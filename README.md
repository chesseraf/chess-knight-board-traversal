# Knight's tour solver
By Rafael Pashkov
March 2025
Last updated - October 2025

## Overview
The knight's tour is a fun chess problem. The goal is to take a chess knight, and move it on an 8x8 chess board repeatedly such that the knight visits all squares exactly once. This program can create solutions on boards of any dimensions as long as the width and height are each divisible by 4. The most interesting part is that the starting point and the ending point can be specified by the user.

In the example solution below, the knight starts in the top left corner, labeled with a 1. Then it moves to the spot labeled 2, and so on until 64. 
<pre>
| 1  | 58 | 11 | 16 | 53 | 56 | 29 | 8  |  
| 12 | 17 | 2  | 57 | 30 | 9  | 52 | 55 |  
| 59 | 4  | 15 | 10 | 41 | 54 | 7  | 28 |  
| 18 | 13 | 40 | 3  | 6  | 31 | 46 | 51 |  
| 39 | 60 | 5  | 14 | 47 | 42 | 27 | 32 |  
| 22 | 19 | 38 | 63 | 36 | 33 | 50 | 45 |  
| 61 | 64 | 21 | 24 | 43 | 48 | 35 | 26 |  
| 20 | 23 | 62 | 37 | 34 | 25 | 44 | 49 |  
</pre>

A knights move follows an L shape. It move 2 spaces in any of the 4 directions, and 1 space in a perpendicular direction. For example, a knight can move from the spot labeled 1 to the spot labeled 2 in the example above.

## Running the program
To run the program:
1) clone the repository with
``` bash
git clone https://github.com/chesseraf/chess-knight-board-traversal.git
```
2) This step is recomended but the program may still run if it is skipped. It recompiles the program in case any changes were made.
``` bash
javac -d .\bin\ .\src\KnightPackage\*.java
```
3) Run the program:
``` bash
java -cp bin KnightPackage.App
```

## Description
There are 4 different solvers for the chess knight's tour problem that the user can choose between. They all contain randomness so they will find a large number of different solutions. Different solvers create solutions with different properties.

1) Lines:
No restrictions. The start and end point of the solution can be anywhere on the board.
2) Loops:
The result will be a loop, meaning the end point is adjacent (by a knight move) to the starting point.
3) Specified endpoints:
The user sets the coordinates for both the first point and the last point.
4) Startpoint:
The user specifies the first point of the path.

When running the program, the user will be prompted all of the options. The program does not take any parameters.

The user can specify the size of the board. The number of rows and columns must be divisible by 4, and be at least 8.
If the user provides an option that was not offered, either a default value or a similar value will be used instead. Also, choosing a 4x4 board size will show solutions for a 4x8 board since there are no solution for a 4x4 board.

## Solver statistics
All solvers can make 100k different solutions for 8x8 boards in under 20 seconds.
All solvers can solve a 1000 x 1000 board in 25 seconds.
The solvers without prespecified endpoints can solve a 1000 x 1000 board in 10 seconds

There is an option to add extra randomness which should preserve similar statistics. There is also an option for higher randomness, but it takes longer to find solutions so the statistics may slightly differ for it.
The minimum board size is 4x8. If one of the dimensions is 4, then there are less solver options given.

## Folder Structure
All of the Java code is in `src/KnightPackage`
The compiled output files will be generated in the `bin` folder by default.

## Using the jar file
To generate another .jar file, run the following command from the `bin` folder.
Replace the X in VX with a version number.
```bash
jar -cf ../knightSolverVX.jar .
```
The latest .jar file for this project is KnightSolverV5.jar and is available in the project. It is in the root directory of the project. 

The package name is KnightPackage. After adding the jar file, for example, use:
```Java
import KnightPackage.*;
```

## Core algorithm
1) Start by creating several loops on the board, such that each square is a part of exactly one loop.
A loop is a sequence of cells where each one is adjacent to the next one via a knight move, and the first one is adjacent to the last one.
One method to do this is to split the board into 4x4 sections. Each such section can be split into loops as follows:
 <pre>
| <i>1</i>  | 2  | 3  | 4  |  
| 3  | 4  | <i>1</i>  | 2  |  
| 2  | <i>1</i>  | 4  | 3  |  
| 4  | 3  | 2  | <i>1</i>  |  
</pre>
In the example above, a loop can be formed from the cells labled 1, as well as with 2, 3, and 4.
<pre>
| 1  |    |    |    |  
|    |    | 2  |    |  
|    | 4  |    |    |  
|    |    |    | 3  |  
</pre>
This is how ordering the cells labled 1 can form a loop

Another method for splitting the board into loops works similarily, but splits each loop from the previous version into 2. In one of 2 ways. For example, the loop formed by the cells labled 1 in the previous example can be split into 2 loops in either of the following 2 ways:
<pre>
| 1  |    |    |    |  
|    |    | 2  |    |  
|    | 1  |    |    |  
|    |    |    | 2  |  
OR
| 1  |    |    |    |  
|    |    | 1  |    |  
|    | 2  |    |    |  
|    |    |    | 2  |  
</pre>
Even though there is only 2 squares, it is still a loop.

When the user is asked about using extra randomness, yes results in this 2nd method being used while no results in the first method.
The 2nd method is able to find a larger amount of different solutions because there is a larger number of loops to start with. 

The 3rd method makes loops by first creating a solution in another method. Then, it takes consecutive pairs of squares in that solution and makes each pair into a loop. This set of loops of size 2 is then used as the starting point for the merging process.

2) Merge the loops with each other until only 1 loop remains. This loop contains all of the squares, so it is an answer. 

Example how 2 loops are merged into one. There is one loop in the upper 4x4 section and another in the lower. In the example below it, the 2 loops have been merger. There is just one loop with the same cells.


```
 Unmerged
| 1  |    |    |    |  
|    |    | 2  |    |  
|    | 4  |    |    |  
|    |    |    | 3  |  
| 1  |    |    |    |  
|    |    | 2  |    |  
|    | 4  |    |    |  
|    |    |    | 3  |  

 Merged
| 1  |    |    |    |  
|    |    | 2  |    |  
|    | 8  |    |    |  
|    |    |    | 3  |  
| 7  |    |    |    |  
|    |    | 4  |    |  
|    | 6  |    |    |  
|    |    |    | 5  |  
```

Note that the 1 and the 8 are adjacent.
To merge, choose a random line. Go through all consecutive pairs of squares in that line. For each pair, create another pair of squares by moving them with a knight move in any of the 8 possible directions. If the new pair of squares are consecutive squares in another loop, then the 2 loops can be merged by connecting the two pairs of squares. 

In the example above, the upper Loops Coordinates labelled 3 and 4 are considered. When moved 2 down and 1 left (knight move), they land on the lower Loops Coordinates labelled 1 and 2. Since they are consecutive squares in that loop, the 2 loops can be merged. 