import java.util.Random;
import java.util.Scanner;

public class Labrinth
{

	private int numRows = 6;    
	private int numColumns = 5;
	private int minEmpty = 15;

	private Coordinate start;

    private Cell[][] data = new Cell[numRows][numColumns]; // the actual= labrinth data as 2D array 

    private Random random_number_generator;
    private Scanner scan;

    public Labrinth() {

    	System.out.println("\tinit");

    	scan = new Scanner(System.in);
        // fill the entire labrinth with walls (place a 1 into each cell)
    	for(int i = 0; i < numRows; i++) {
    		for(int j = 0; j < numColumns; j++) {
    			data[i][j] = new Cell("1");
    		}
    	} 

        // pick a random start point on the first row (row 0) of the array
    	random_number_generator = new Random(); 

		// there are numColumns number of slots in the first row, we use that number - 1 make a suitable range
		// so that we can pick a random array index to be our "Start" cell of the maze
    	int column_start = random_number_generator.nextInt(numColumns);

        // and mark that node of the labrinth with an "S"	
    	data[0][column_start].set("S");
    	start = new Coordinate(0, column_start);
    }

    public Labrinth(DecisionTree tree) {

    	System.out.println("\tinit");

    	scan = new Scanner(System.in);
        // fill the entire labrinth with walls (place a 1 into each cell)
    	for(int i = 0; i < numRows; i++) {
    		for(int j = 0; j < numColumns; j++) {
    			data[i][j] = new Cell("1");
    		}
    	}

    	DecisionTreeNode currentNode = tree.root();
    	if(currentNode == null) {
    		// pick a random start point on the first row (row 0) of the array
	    	random_number_generator = new Random();

			// there are numColumns number of slots in the first row, we use that number - 1 make a suitable range
			// so that we can pick a random array index to be our "Start" cell of the maze
	    	int column_start = random_number_generator.nextInt(numColumns - 1);

	        // and mark that node of the labrinth with an "S"	
	    	data[0][column_start].set("S");
	    	start = new Coordinate(0, column_start);
    	} else {
    		set(currentNode.coordinate(), "S");	
    	}

	
		boolean isNull = currentNode.child() == null;

		int num = 0;
		while(true) {
			
			currentNode = currentNode.child();
			if(currentNode == null) {
				return;
			}
			if(currentNode.child() == null && lastRow(currentNode.coordinate().row)) {
				set(currentNode.coordinate(), "E");
			} else {
				set(currentNode.coordinate(), "0");	
			}
			
			
		}
    }

    public Labrinth(DecisionTree tree, Labrinth labrinth) {

    	tree.print();

    	labrinth.print();

    	System.out.println("\tinitboop");

    	scan = new Scanner(System.in);

    	System.out.println("numRows: " + labrinth.numRows);
    	System.out.println("numColumns: " + labrinth.numColumns);
    	for(int i = 0; i < labrinth.numRows; i++) {
    		for(int j = 0; j < labrinth.numColumns; j++) {
    			// System.out.print("i: " + i + " j: " + j);
    			data[i][j] = new Cell(labrinth.at(i, j));
    		}
    		// System.out.println();
    	}

    	DecisionTreeNode currentNode = tree.root();
	
		boolean isNull = currentNode.child() == null;

		int num = 0;
		while(true) {
			
			currentNode = currentNode.child();
			if(currentNode == null) {
				return;
			}
			if(currentNode.child() == null) {
				set(currentNode.coordinate(), "E");
			} else {
				set(currentNode.coordinate(), "*");	
			}
		}
    }

    public Coordinate random() {
    	return new Coordinate(randomRow(), randomCol());
    }

    public int randomRow() {
    	random_number_generator = new Random(); 
    	return random_number_generator.nextInt(numRows - 1);
    }

    public int randomCol() {
    	random_number_generator = new Random(); 
    	return random_number_generator.nextInt(numRows - 1);
    }

	public int rows() {
		return numRows;
	}

	public int cols() {
		return numColumns;
	}

	public int minEmpty() {
		return minEmpty;
	}

	public int numEmpty() {
		int numEmpty = 0;
		for(int i = 0; i < numRows; i++) {
    		for(int j = 0; j < numColumns; j++) {
    			if(data[i][j].get() == "0") {
    				numEmpty++;
    			}
    		}
    	}
    	return numEmpty;
	}

	public String at(Coordinate _coordinate) {
		if (!canGoHere(_coordinate)) {
			System.out.println("Error: invalid coordinate!! " + _coordinate.toString());
			return "";
		} else {
			return data[_coordinate.row][_coordinate.col].get();
		}
	}

	public String at(int _row, int _col) {
		if (!canGoHere(new Coordinate(_row, _col))) {
			System.out.println("Error: invalid coordinate!!");
			return "";
		} else {
			return data[_row][_col].get();
		}
	}

	public void set(Coordinate _coordinate, String _data) {
		data[_coordinate.row][_coordinate.col].set(_data);
	}

	public boolean lastRow(int _row) {
		return _row == numRows - 1;
	}

	public boolean validRow(int _row) {
		boolean valid = _row > -1 && (_row < numRows);
		// System.out.println("\tvalidRow(): row: " + _row + " " + valid);
		return valid;
	}

	public boolean validCol(int _col) {
		boolean valid = _col > -1 && (_col < numColumns);
		// System.out.println("\tvalidCol: col: " + _col + " " + valid);
		return valid;
	}

	public boolean canGoHere(Coordinate _coordinate) {
		boolean canGo = validRow(_coordinate.row) && validCol(_coordinate.col);

		// System.out.println("\tcanGoHere: " + canGo + " coord: " + _coordinate.toString());
		return validRow(_coordinate.row) && validCol(_coordinate.col);
	} 

	public boolean isWall(Coordinate _coordinate) {
		//System.out.println("isWall(): " + data[_coordinate.row][_coordinate.col].get() + " " + data[_coordinate.row][_coordinate.col].get() == "1");
		return data[_coordinate.row][_coordinate.col].get() == "1";
	}

	public boolean isEmpty(Coordinate _coordinate) {
		return data[_coordinate.row][_coordinate.col].get() == "0";
	}

	public Coordinate start() {
		return start;
	}

	public boolean isStart(Coordinate _coordinate) {
		return _coordinate.row == start.row && _coordinate.col == start.col;
	}

	public boolean canEndPath(Coordinate _coordinate) {
		//System.out.println("canEndPath: " + _coordinate.toString() + " lastRow: " + (numRows - 1) );
		return _coordinate.row == numRows - 1;
	}

	public boolean isEnd(Coordinate _coordinate) {
		return at(_coordinate) == "E";
	}

	public boolean isValid(int _decision, Coordinate _coordinate) {
		// System.out.println("\t\tisValid(): decision: " + _decision + " coordinate: " + _coordinate.toString());

		if(_decision == 0) {
			boolean canBreakNorth = canBreakNorth(_coordinate);
			// System.out.println("\t\tcanBreakNorth: " + canBreakNorth);
			return canBreakNorth;
		} else if(_decision == 1) {
			boolean canBreakEast = canBreakEast(_coordinate);
			// System.out.println("\t\tcanBreakEast: " + canBreakEast);
			return canBreakEast;
		} else if(_decision == 2) {
			boolean canBreakSouth = canBreakSouth(_coordinate);
			// System.out.println("\t\tcanBreakSouth: " + canBreakSouth);
			return canBreakSouth;
		} else if(_decision == 3) {
			boolean canBreakWest = canBreakWest(_coordinate);
			// System.out.println("\t\tcanBreakWest: " + canBreakWest);
			return canBreakWest;
		} else {
			return false;
		}
	}

	boolean canWalk(int _decision, Coordinate _coordinate) {
		if(_decision == 0) {
			boolean canWalkNorth = canWalkNorth(_coordinate);
			System.out.println("\t\tcanWalkNorth: " + canWalkNorth);
			return canWalkNorth;
		} else if(_decision == 1) {
			boolean canWalkEast = canWalkEast(_coordinate);
			System.out.println("\t\tcanWalkEast: " + canWalkEast);
			return canWalkEast;
		} else if(_decision == 2) {
			boolean canWalkSouth = canWalkSouth(_coordinate);
			System.out.println("\t\tcanWalkSouth: " + canWalkSouth);
			return canWalkSouth;
		} else if(_decision == 3) {
			boolean canWalkWest = canWalkWest(_coordinate);
			System.out.println("\t\tcanWalkWest: " + canWalkWest);
			return canWalkWest;
		} else {
			return false;
		}	
	}

	public boolean canBreakNorth(Coordinate _coordinate) {

		Coordinate newCoord = new Coordinate(_coordinate.row - 1, _coordinate.col);
		boolean canGoHere = canGoHere(newCoord);
		
		// System.out.println("canGoHere: " + canGoHere);
		if(canGoHere) {
			boolean isWall = isWall(newCoord);
			// System.out.println("isWall: " + isWall);	
		}
		return canGoHere(newCoord) && isWall(newCoord);
	}

	public boolean canWalkNorth(Coordinate _coordinate) {

		Coordinate newCoord = new Coordinate(_coordinate.row - 1, _coordinate.col);
		boolean canGoHere = canGoHere(newCoord);
		
		// System.out.println("canGoHere: " + canGoHere);
		if(canGoHere) {
			boolean isWall = isEmpty(newCoord);
			// System.out.println("isWall: " + isWall);	
		}
		return canGoHere(newCoord) && (isEmpty(newCoord) || isEnd(newCoord));
	}

	public Coordinate getNorth(Coordinate _coordinate) {
		return new Coordinate(_coordinate.row - 1, _coordinate.col);
	}

	public boolean canBreakEast(Coordinate _coordinate) {

		Coordinate newCoord = new Coordinate(_coordinate.row, _coordinate.col + 1);
		boolean canGoHere = canGoHere(newCoord);
		
		//System.out.println("canGoHere: " + canGoHere);
		if(canGoHere) {
			boolean isWall = isWall(newCoord);
			// System.out.println("isWall: " + isWall);	
		}
		
		return canGoHere(newCoord) && isWall(newCoord);
	}

	public boolean canWalkEast(Coordinate _coordinate) {

		Coordinate newCoord = new Coordinate(_coordinate.row, _coordinate.col + 1);
		boolean canGoHere = canGoHere(newCoord);
		
		//System.out.println("canGoHere: " + canGoHere);
		if(canGoHere) {
			boolean isWall = isEmpty(newCoord);
			// System.out.println("isWall: " + isWall);	
		}
		
		return canGoHere(newCoord) && (isEmpty(newCoord) || isEnd(newCoord));
	}

	public Coordinate getEast(Coordinate _coordinate) {
		return new Coordinate(_coordinate.row, _coordinate.col + 1);	
	}

	public boolean canBreakSouth(Coordinate _coordinate) {
		Coordinate newCoord = new Coordinate(_coordinate.row + 1, _coordinate.col);
		boolean canGoHere = canGoHere(newCoord);
		
		// System.out.println("canGoHere: " + canGoHere);
		if(canGoHere) {
			boolean isWall = isWall(newCoord);
			// System.out.println("isWall: " + isWall);	
		}
		return canGoHere(newCoord) && isWall(newCoord);
	}

	public boolean canWalkSouth(Coordinate _coordinate) {
		Coordinate newCoord = new Coordinate(_coordinate.row + 1, _coordinate.col);
		boolean canGoHere = canGoHere(newCoord);
		
		// System.out.println("canGoHere: " + canGoHere);
		if(canGoHere) {
			boolean isWall = isEmpty(newCoord);
			// System.out.println("isWall: " + isWall);	
		}
		return canGoHere(newCoord) && (isEmpty(newCoord) || isEnd(newCoord));
	}

	public Coordinate getSouth(Coordinate _coordinate) {
		return new Coordinate(_coordinate.row + 1, _coordinate.col);	
	}

	public boolean canBreakWest(Coordinate _coordinate) {
		Coordinate newCoord = new Coordinate(_coordinate.row, _coordinate.col -1);
		boolean canGoHere = canGoHere(newCoord);
		
		// System.out.println("canGoHere: " + canGoHere);
		if(canGoHere) {
			boolean isWall = isWall(newCoord);
			//System.out.println("isWall: " + isWall);	
		}
		return canGoHere(newCoord) && isWall(newCoord);
	}

	public boolean canWalkWest(Coordinate _coordinate) {
		Coordinate newCoord = new Coordinate(_coordinate.row, _coordinate.col -1);
		boolean canGoHere = canGoHere(newCoord);
		
		// System.out.println("canGoHere: " + canGoHere);
		if(canGoHere) {
			boolean isWall = isEmpty(newCoord);
			//System.out.println("isWall: " + isWall);	
		}
		return canGoHere(newCoord) && (isEmpty(newCoord) || isEnd(newCoord));
	}

	public Coordinate getWest(Coordinate _coordinate) {
		return new Coordinate(_coordinate.row, _coordinate.col - 1);	
	}

	public boolean canBreakAWall(Coordinate _coordinate) {
		return  canBreakNorth(_coordinate) || 
				canBreakEast(_coordinate)  || 
				canBreakSouth(_coordinate) ||
				canBreakWest(_coordinate);
	}

	public Coordinate nextChoiceAsCoordinate(int _decision, Coordinate _coordinate) {

		//     // _decision is a number such that:
		//     // 0 = NORTH
		//     // 1 = EAST
		//     // 2 = SOUTH
		//     // 3 = WEST

		if(_decision == 0) {        // NORTH
			return getNorth(_coordinate);
		} else if(_decision == 1) { // EAST
				return getEast(_coordinate);
		} else if(_decision == 2) { // SOUTH
			return getSouth(_coordinate);
		} else { 				   // WEST
			return getWest(_coordinate);
		}
    
	}

	public void endPath(Coordinate _coordinate) {
		data[_coordinate.row][_coordinate.col].set("E");
	}

	public void print() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				System.out.print(data[i][j].get());
			}
			System.out.println();
		}
		System.out.println();
	}

} 


