import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class World {

	private int rows, cols, initRows, initCols, offsetX, offsetY;
	private ArrayList<ArrayList<Boolean>> valuesList = new ArrayList<ArrayList<Boolean>>();
	private ArrayList<Coordinates> coordinatesList = new ArrayList<>();

	public World(int rows, int cols, String filename) {
		this.rows = rows;
		this.cols = cols;
		this.initRows = rows;
		this.initCols = cols;
		offsetX = 0;
		offsetY = 0;
		// initialize values list
		for (int i = 0; i < cols; i++) {
			valuesList.add(new ArrayList<Boolean>());
			for (int j = 0; j < rows; j++) {
				valuesList.get(i).add(false);
			}
		}
		readSeed(filename);
		initializeCoordinatesList();
	}

	// TODO: expand world to accommodate for infinite directions:
	public void expandWorld(String direction, ArrayList<ArrayList<Boolean>> cellList) {
		System.out.println(rows+" X "+cols);
		switch (direction.toLowerCase()) {
		case "north":
			for (ArrayList<Boolean> list : cellList) {
				list.add(0, false);
			}
			rows++;
			offsetY++;
			break;
		case "south":
			for (ArrayList<Boolean> list : cellList) {
				list.add(false);
			}
			rows++;
			break;
		case "east":
			cellList.add(addNewList());
			cols++;
			break;
		case "west":
			// insert new false value to the beginning of every arraylist in valuesList
			cellList.add(0, addNewList());
			cols++;
			offsetX++;
			break;
		}
	}

	// helper method for expandWorld, for expanding east and west borders
	private ArrayList<Boolean> addNewList() {
		ArrayList<Boolean> newList = new ArrayList<Boolean>(Collections.nCopies(rows, false));
		return newList;
	}

	// TODO: read seed for loading different starting positions of game
	public void readSeed(String filename) {
		try {
			Scanner scan = new Scanner(new File(filename));
			int row = 0;
			while (scan.hasNextLine()) {
				row++;
				if (row >= row - 1) {
					expandWorld("south", valuesList);
				}
				int col = 0;
				while (scan.hasNextInt()) {
					col++;
					if (col >= cols - 1) {
						expandWorld("east", valuesList);
					}
					scan.nextInt();
				}
				scan.nextLine();
			}
			scan.close();
			Scanner scan2 = new Scanner(new File(filename));
			while (scan2.hasNext()) {
				for (int y = 0; y < rows; y++) {
					for (int x = 0; x < cols; x++) {
						if (scan2.hasNextInt()) {
							valuesList.get(x).set(y, scan2.nextInt() == 1);
						} else {
							break;
						}
					}
					if (scan2.hasNextLine()) {
						scan2.nextLine();
					} else {
						break;
					}
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initializeCoordinatesList() {
		// loop through valuesList and find true values
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				if (valuesList.get(i).get(j) == true)
					// create new Coordinates object of x and y position of the true value
					coordinatesList.add(new Coordinates(i - offsetX, j - offsetY));
			}
		}
	}

	// custom classes
	public void update() {
		ArrayList<ArrayList<Boolean>> temp = new ArrayList<ArrayList<Boolean>>();
		for (int i = 0; i < cols; i++) {
			temp.add(new ArrayList<Boolean>());
			for (int j = 0; j < rows; j++) {
				temp.get(i).add(false);
				int cellNumber = returnLiveNeighbours(i, j);
				boolean value = valuesList.get(i).get(j);
				// rule #1: if cell is alive and has 2 or 3 surrounding live neighbours
				if (value == true && (cellNumber == 3 || cellNumber == 2))
					temp.get(i).set(j, true);
				// rule #2: if cell is dead but has 3 surrounding live neighbours
				else if (value == false && cellNumber == 3)
					temp.get(i).set(j, true);
				// rule #3: if cell does not fulfill the conditions for survival
				else
					temp.get(i).set(j, false);
			}
		}
		checkExpansion(temp);
		valuesList = temp;
		// clear existing Coordinates arraylist and initialize new ones
		this.coordinatesList.clear();
		initializeCoordinatesList();
	}

	public void checkExpansion(ArrayList<ArrayList<Boolean>> cellList) {
		for (int x = 0; x < cols - 2; x++) {
			if (cellList.get(x).get(0)) {
				if (cellList.get(x + 1).get(0) && cellList.get(x + 2).get(0)) {
					expandWorld("north", cellList);
					break;
				}
			}
		}
		for (int x = 0; x < cols - 2; x++) {
			if (cellList.get(x).get(rows - 1)) {
				if (cellList.get(x + 1).get(rows - 1) && cellList.get(x + 2).get(rows - 1)) {
					expandWorld("south", cellList);
					break;
				}
			}
		}
		for (int y = 0; y < rows - 2; y++) {
			if (cellList.get(0).get(y)) {
				if (cellList.get(0).get(y + 1) && cellList.get(0).get(y + 2)) {
					expandWorld("west", cellList);
					break;
				}
			}
		}
		for (int y = 0; y < rows - 2; y++) {
			if (cellList.get(valuesList.size() - 1).get(y)) {
				if (cellList.get(cols - 1).get(y + 1) && cellList.get(cols - 1).get(y + 2)) {
					expandWorld("east", cellList);
					break;
				}
			}
		}
	}

	// take coordinates x, y and return number of live neighboring cells

	// TODO: include new expandWorld methods to expand the world borders if needed
	// alternative: new method for checking borders independent of
	// returnLiveNeighbours
	public int returnLiveNeighbours(int x, int y) {
		int counter = 0;
		if (y > 0) {
			// check north
			if (valuesList.get(x).get(y - 1)) {
				counter++;
			}
			if (x < cols - 1) {
				// check northeast
				if (valuesList.get(x + 1).get(y - 1)) {
					counter++;
				}
			}
			if (x > 0) {
				// check northwest
				if (valuesList.get(x - 1).get(y - 1)) {
					counter++;
				}
			}
		}
		if (y < rows - 1) {
			// check south
			if (valuesList.get(x).get(y + 1)) {
				counter++;
			}
			if (x < cols - 1) {
				// check southeast
				if (valuesList.get(x + 1).get(y + 1)) {
					counter++;
				}
			}
			if (x > 0) {
				// check southwest
				if (valuesList.get(x - 1).get(y + 1)) {
					counter++;
				}
			}
		}
		if (x < cols - 1) {
			// check east
			if (valuesList.get(x + 1).get(y)) {
				counter++;
			}
		}
		if (x > 0) {
			// check west
			if (valuesList.get(x - 1).get(y)) {
				counter++;
			}
		}

		return counter;
	}

	// getters and setters

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getCols() {
		return cols;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public ArrayList<ArrayList<Boolean>> getValuesList() {
		return valuesList;
	}

	public void setValuesList(ArrayList<ArrayList<Boolean>> valuesList) {
		this.valuesList = valuesList;
	}

	public ArrayList<Coordinates> getCoordinatesList() {
		return coordinatesList;
	}

	public void setCoordinatesList(ArrayList<Coordinates> coordinatesList) {
		this.coordinatesList = coordinatesList;
	}

}
