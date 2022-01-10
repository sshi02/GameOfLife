=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: sshi1
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays
  The 2D arrays will be used to store grid information about where/what cells
  are present on the grid. The array will be interated through and updated
  during celluar automation, displaying cells in the GUI, and calculating win/fail
  conditions. There are two arrays, one used that will represent
  visible cells, and a 'buffered' grid to achieve a desired behavior
  while running the model. The cells will be represented through integers,
  with 0 indicating dead cell and 1 indicating alive cell. Two special types,
  kill (3) and restricted (2) will be cells with special behaviors specific
  to the game.
  Was good to go from submission of proposal.

  2. File I/O
  File I/O is used to read/write the layouts of grids/buffered grids, as well as
  important level data that help determine win conditions or user constraints.
  The files read will compose of save states for sandbox mode, progress files for
  ongoing levels, layouts of levels, and level data.
  The game will write to save states and level progress. This data will be loaded
  in when necessary or called upon by the user.
  Was good to go from submission of proposal.

  3. Inheritance and Subtyping
  Two models of the game (classes level and sandbox) will extend the class Grid,
  which contains the original model/design of Game of Life. This is necessary as
  the two subclasses demand different methods and implemenations. For example,
  levels need to be able to process and control for specific win conditions and
  relevant files (saving progress, layouts, level data, etc.). In the case of
  sandbox, the model has no wincondition, needs additional controls since there are
  no limits compared to the level, and need to save/load from specific save/load
  files using their own methods.
  Was good to go from submission of proposal.

  4. Collections and Maps
  A LinkedList will be used to store the 'ticks' of a model or each generation
  that is produced. This LinkedList will store the relevant grid data pertaining
  to a generation (int[][]). The importance of a LinkedList is that it is ordered,
  and it doesn't really care about whether the data inside is the same or equal (since
  certain generations can produce duplicate grids).
  Methods properly use get() methods taking advantage of framing indexes as
  tick number. These ticks will be used in modes such as
  sandbox mode in order to help the user iterate through specific
  ticks if they want to figure out a desired behavior. These ticks
  also help store old grid/generational data that the Level class
  fetches to help reset the level efficiently.

  Acting on feedback:
  - Second implementation considered substantial enough, so most focus went
  into working with the specific collection and creating features aronud it.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  FileLineIterator (ripped from the hw), serves as a useful wrapper around
  BufferedReader, which implements Iterator. This will help read files for the game.

  GridParser serves to help parse files with grids into grid data for models
  to use.

  GridWriter serves to help take grid data and covert it into files with the
  specified filename (either saving/creating new files) for things such as save
  files, progress files, etc. for game models to use.

  Grid is the parent class/model for Level and Grid. It contains the original
  framework for John Conway's Game of Life, and it houses the methods
  that run the model and the data pertaining to the model when it is run.
  It also houses useful methods that can reset the grid to specific layouts
  and sizes (given either int, int[][], or file names of grid layouts). It will
  also tell you a lot about the model (such as number of cells still alive),
  and let you set cells on the grid.
  Basically, it's just the thing that happens when you click the title screen!

  Level is a subclass of Grid, and it not only contains the framework for
  John Conway's Game of Life but also is the actual model for a win-condition
  based environment. This will tell you if a model, under certain win conditions
  specified by a levelData[levelId] file, meets a win condition of a fail condition.
  It also houses methods that will save progress, level messages, hints, and other
  information such as number of modifications made to a layout (for restricting cell
  changes).

  Sandbox is a subclass of Grid, and it is a win-conditionless environment, armed with
  methods that allow the user to save and load their desired layouts to help them
  play around with the Game of Life.

  MenuView extends JPanel to implement basically the title screen which
  uses grid to do something real cool when clicked!

  LevelView extends JPanel to implement level into a GUI format, including
  all its visual components and housing important methods for the game run
  file (RunGameOfLife)

  SandboxView extends JPanel to implement sandbox into a GUI format, containing
  interesting methods that allow users to work through the model in the game
  run file visually.

  RunGameOfLife is where everything coalesces, and it serves as the front-end
  to the entire game, for levels and sandbox mode.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  A huge issue when figuring out how to display a running model
  of the game avoiding using a run-to-end solution, since it severally limits
  what the user can do when they're just watching to game run. It took a while
  to find a solution, which was a timer which would make it easier to not have the model
  stutter over something such as making threads sleep or wait. This can be fonud
  in the ____View classes.

  Another block was finding good behavior for the model. If cells were to run off-
  screen, I didn't want that to affect the model as if it had just disappeared.
  So I saved a "buffered" grid that would compute any cells that were outside
  the scope of the normal grid. This helped maintain the correct behavior, and can
  be found in Grid.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  All classes that are accessed by others use private instance variables, and only
  certain static information such as methods to help parse data are available
  as public. In terms of separation, it's decent since it adheres to the model,
  view, and front-end implementation discussed in class. In terms of refactoring,
  maybe eventually cleaning up some methods to be more efficient and
  using clearer methods names would help a lot.

  For the front-end things could always be prettier, and the changes in window sizes
  when you jump from screen to screen could be solved through implementing a panel or
  component that forces a window size and resizes everything to match.




========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  JavaDocs from Oracle, immensely. https://docs.oracle.com/en/java/javase/17/.

  Below is a Standford Archive on John Conway's Game of Life from
  the Oct 1970 edition of Scientific American.
  Gardner, M. (1970, October).
    The fantastic combinations of John  Conway's new solitaire game "life". Scientific American, (223), 120–123.
  Retrieved from https://web.stanford.edu/class/sts145/Library/life.pdf.
