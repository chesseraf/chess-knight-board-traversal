There are 3 different solvers for the chess knight's tour problem created here.
1) Solve the board randomly every time, not neccesarilly ending near the start.
2) Make a loop, ending adjacent to where you started.
3) Make solutions that start and end at specified coordinates.

The user can specify the size of the board. The number of rows and columns must be divisible by 4, and be at least 8.
All solvers can make 100k different solutions for 8x8 boards in under 20 seconds.
All solvers can solve a 1000 x 1000 board in 25 seconds.
The solvers without prespecified endpoints can solve a 1000 x 1000 board in 10 seconds

There is an option to add extra randomness which should preserve similar statistics.
The minimum board size is 4x8. If one of the dimensions is 4, then there are less solver options given.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Using the jar file
The .jar file for this project called KnightSolver.jar is available in the project. It is at the same level as the bin and src folders. 
The package name is KnightPackage. After adding the jar file, for example, use: import KnightPackage.*;
