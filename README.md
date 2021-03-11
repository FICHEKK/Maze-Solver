![](doc/images/header.png)

# ❓ What is it?
Maze solver is an application which allows the user to:
1. Generate mazes using various maze generation algorithms.
2. Edit maze using tools such as a brush or a fill-bucket.
3. Run selected search algorithm to find a path from start to finish cells.

# 🕹️ Control panel
Maze solver's user interface is very simple, containing only a single window of controls on the right side of the screen - called *control panel*. Control panel contains 4 sections:
* **Generate** - used to generate mazes.
* **Edit** - used to edit mazes.
* **Search** - used to search mazes.
* **Save/Load** - used to save and load mazes to/from files.

![](doc/images/solved-maze.png)

## ➕ Generate

Generate section is used to generate mazes and offers the user following options:
* **Generator** - option that allows the user to select the maze generation algorithm. Possible options are:
  * *Recursive backtracker*
  * *Binary tree*
  * *Sidewinder*
  * *Prim*
* **Width** - defines the width of the maze.
* **Height** - defines the height of the maze.
* **Wall density** - defines the percentage that the wall cell **won't** be deleted after the mazes has been generated.

**NOTE:** The actual number of cells in width = **Width** * 2 + 1. The actual number of cells in height = **Height** * 2 + 1. This is made so that the number of cells in any dimension is odd, which allows for a better looking maze generation.

---

## 🖌️ Edit

Edit section is used to edit mazes and offers the user following options:
* **Toolbox** - option that allows the user to select the edit tool. Possible options are:
  * *Brush* - paints the maze terrain with selected cell type.
  * *Bucket* - flood-fills area with selected cell type.
  * *Start marker* - defines the start cell position.
  * *Finish marker* - defines the finish cell position.

* **Brush** - allows the user to select the cell type used for editing the maze terrain.
* **Indestructible** - list of cell types which cannot be edited by the brush tool.
* **Radius** - radius used by the brush tool.
* **Density** - density used by the brush tool.
* **Outside** - cell type used to paint area outside the of the maze.

---

## 🔍 Search

Search section is used to search mazes using the selected search algorithm. It offers the following options:
* **Algorithm** - algorithm used for the search. Possible algorithms include:
  * *A\* Search Algorithm*
  * *Dijkstra's Algorithm*
  * *Greedy Best-First Search*
  * *Breadth-First Search*
  * *Depth-First Search*

* **Step duration** - defines the search animation speed.

---

## 💾 Save/Load

Mazes can be saved to and loaded from files.

**Saving the maze**
1. Click on the **Save** button - the save dialog should open.
2. Define the save file name - extension *.maze* will be automatically added to the file name.
3. Select save destination by navigating the file system.
4. Click on the **Save** button in the save dialog to save the maze to the specified location.

**Loading the maze**
1. Click on the **Load** button - the load dialog should open.
2. Locate and select the file which contains the maze information.
3. Click on the **Load** button in the load dialog to load the maze from the selected file.

# 🗺️ Examples

## 🌵 *enclosed-desert.maze*
![](doc/gifs/enclosed-desert.gif)

## 🏝️ *tropical-islands.maze*
![](doc/gifs/tropical-islands.gif)

## 🌋 *cracked-volcano.maze*
![](doc/gifs/cracked-volcano.gif)

## ❄️ *frozen-lakes.maze*
![](doc/gifs/frozen-lakes.gif)

## 🌳 *forest-walk.maze*
![](doc/gifs/forest-walk.gif)

And that is all! Have fun playing with the maze solver! 🙂
