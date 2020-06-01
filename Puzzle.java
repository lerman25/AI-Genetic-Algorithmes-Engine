import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Puzzle {
	private char[][] board;
	private double DistanceToStart;
	private int xExit;
	private int yExit;
	private int redDis;
	private int bCars;
	private Car redCar;
	private float alpha;
	private float beta;
	public int size = 6;
	private String filePath = "C:\\Users\\user\\nov - eclipse-workspace\\lab1-AI\\src\\boards.txt";
	private ArrayList<Car> cars;

	public Puzzle() {
		board = new char[size][size];
		setxExit(5);
		setyExit(2);
		cars = new ArrayList<Car>();
		alpha = 7;
		beta = 3;
	}

	public Puzzle(int i, int j) {

		board = new char[size][size];
		setxExit(j);
		setyExit(i);
		cars = new ArrayList<Car>();
		alpha = 7;
		beta = 3;
	}

	public Puzzle(int i, int j, int _size) {
		size = _size;
		board = new char[size][size];
		setxExit(j);
		setyExit(i);
		cars = new ArrayList<Car>();
		alpha = 7;
		beta = 3;
	}

	public Puzzle(Puzzle current) {
		size = current.size;
		board = new char[current.size][current.size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++)
				board[i][j] = current.getBoard()[i][j];
		}
		setxExit(current.getxExit());
		setyExit(current.getyExit());
		cars = new ArrayList<Car>();
		for (int i = 0; i < current.getCars().size(); i++)
			cars.add(new Car(current.getCars().get(i)));
		bCars = current.getbCars();
		redDis = current.getRedDis();
		redCar = current.getRedCar();
		DistanceToStart = current.getDistanceToStart();
		alpha = current.getAlpha();
		beta = current.getBeta();
		// TODO Auto-generated constructor stub
	}

	public void print_board() {
		System.out.print("\n\n");
		for (int i = 0; i < size; i++) {
			System.out.print("*****   ");
			String exitSym = "*****";
			for (int j = 0; j < size; j++) {
				if (i == yExit && j == xExit)
					exitSym = "====>";
				System.out.print(board[i][j]);
			}
			System.out.print("   " + exitSym + "   \n");
		}
		System.out.print("\n\n");
	}

	// checks if the move input is possible
	public boolean isPossibleMove(Car car, int steps, boolean dir) {
		if (car.isHor_orient()) {
			if (dir) {
				int xCord = car.getLastX()+steps;
				if(xCord>=this.size) //out of bounds
					return false;
				for(int j=car.getLastX()+1;j<=xCord;j++)
				{
					if(board[car.getY()][j]!='.')
						return false;;
				}
			} else {
				// the car wants to go left;
				int xCord = car.getX()-steps;
				if(xCord<0) //out of bounds
					return false;;
				for(int j=car.getX()-1;j>=xCord;j--)
				{
					if(board[car.getY()][j]!='.')
						return false;;
				}
			}
		} else {
			if (dir) {
				// the car wants to go down
				int yCord = car.getLastY()+steps;
				if(yCord>=this.size) //out of bounds
					return false;
				for(int i=car.getLastY()+1;i<=yCord;i++)
				{
					if(board[i][car.getX()]!='.')
						return false;
				}
			} else {
				// the car wants to go up
				int yCord = car.getY()-steps;
				if(yCord<0) //out of bounds
					return false;
				for(int i=car.getY()-1;i>=yCord;i--)
				{
					if(board[i][car.getX()]!='.')
						return false;
				}
			}
		}
		return true;
	}
    // return a board in which the move is made , while calculating it's h function properties
	public Puzzle moveCar(Car car, int steps, boolean dir) {
		if (isPossibleMove(car, steps, dir)) {
			Puzzle newBoard = new Puzzle(this);
			Car move = newBoard.retCar(car.getKey());
			if(car.isHor_orient())
			{
				if(dir)
				{
					//the car need to be moved right
					int x = move.getX();
					// erasing the car
					for(int i=x;i<x+move.getLength();i++)
					{
						newBoard.getBoard()[car.getY()][i]='.'; 
					}
					//inserting the car in the right place
					int xDest = move.getLastX() + steps;
					for(int i=xDest;i>xDest-move.getLength();i--)
					{
						newBoard.getBoard()[car.getY()][i]=move.getKey(); 
					}
					move.setLastX(xDest);
					move.setX(xDest-move.getLength()+1);
					if(move.getKey()=='X')
					{
						newBoard.setRedCar(move);
						newBoard.setRedDis(xExit-redCar.getLastX());
					}
					return newBoard;
				}
				else
				{
					//the car need to be moved left
					int x = car.getLastX();
					//erasing the car
					for(int i = x;i>x-move.getLength();i--)
					{
						newBoard.getBoard()[car.getY()][i]='.'; 
					}
					//inserting the car in the right place
					int xDest = move.getX()-steps;
					for(int i=xDest;i<xDest+move.getLength();i++)
					{
						newBoard.getBoard()[car.getY()][i]=move.getKey(); 
					}
					move.setLastX(xDest+move.getLength()-1);
					move.setX(xDest);
					if(move.getKey()=='X')
					{
						newBoard.setRedCar(move);
						newBoard.setRedDis(xExit-redCar.getLastX());
					}
					return newBoard;
				}
			}
			else
			{
				if(dir)
				{
					//the car need to be moved down
					int y = car.getY();
					//erasing the car
					int blocking = newBoard.getbCars();
					if(newBoard.isOnRedPath(move))
						blocking--;
					for(int i = y;i<y+move.getLength();i++)
					{
						newBoard.getBoard()[i][move.getLastX()]='.'; 
					}
					//inserting the car in the right place
					int yDest = car.getLastY() + steps;
					for(int i=yDest;i>yDest-move.getLength();i--)
					{
						newBoard.getBoard()[i][move.getLastX()]=move.getKey();
					}
					move.setLastY(yDest);
					move.setY(yDest-move.getLength()+1);
					if(newBoard.isOnRedPath(move))
						blocking++;
					newBoard.setbCars(blocking);
					return newBoard;
				}
				else
				{
					//the car need to be moved up
					int y = car.getLastY();
					int blocking = this.getbCars();
					if(newBoard.isOnRedPath(move))
						blocking--;
					//erasing the car
					for(int i = y;i>y-move.getLength();i--)
					{
						newBoard.getBoard()[i][move.getLastX()]='.'; 
					}
					//inserting the car in the right place
					int yDest = move.getY()-steps;
					for(int i=yDest;i<yDest+move.getLength();i++)
					{
						newBoard.getBoard()[i][move.getLastX()]=move.getKey(); 
					}
					move.setLastY(yDest+move.getLength()-1);
					move.setY(yDest);
					if(newBoard.isOnRedPath(move))
						blocking++;
					newBoard.setbCars(blocking);
					return newBoard;
				}
			}
		
		}
		else
		{
		return null;
		}
	}

	public float h() {
		return (float) beta * redDis + alpha * (float) bCars;
	}

	public void readBoard_file(int line) throws FileNotFoundException {
		File boardFile = new File(filePath);
		java.util.Scanner reader = new java.util.Scanner(boardFile);
		String boardLine = "";
		for (int i = 1; i <= line; i++) {
			boardLine = reader.nextLine();
		}
		readBoard_line(boardLine);
		cars = Car.getCarsFromBoard(this);

	}

	public void readBoard_scan() {
		Scanner scan = new Scanner(System.in); // Create a Scanner object
		System.out.println("Enter Puzzle Line");
		String line = scan.nextLine(); // Read user input
		line.trim();
		readBoard_line(line);
		cars = Car.getCarsFromBoard(this);

	}

	public void readBoard_string(String line) {
		line.trim();
		readBoard_line(line);
		cars = Car.getCarsFromBoard(this);
	}

	public void readBoard_line(String line) {
		char[] array = line.toCharArray();
		int y = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = array[y];
				y++;
			}
		}
	}

	public void print_cars() {
		for (int i = 0; i < cars.size(); i++) {
			Car current = cars.get(i);
			System.out.println(current.toString());
		}
	}

	public int getXCarDis() {
		Car Xcar = retCar('X');
		return xExit - Xcar.getLastX();
	}

	public int getXcarBlock() {
		Car Xcar = retCar('X');
		int counter = 0;
		for (int j = Xcar.getLastX() + 1; j < size; j++) {
			if (board[Xcar.getY()][j] != '.')
				counter++;
		}
		return counter;
	}

	public int getxExit() {
		return xExit;
	}

	public Car retCar(char c) {
		for (int i = 0; i < cars.size(); i++) {
			Car car = cars.get(i);
			if (car.getKey() == c)
				return car;
		}
		return null;
	}

	public void setxExit(int xExit) {
		this.xExit = xExit;
	}

	public int getyExit() {
		return yExit;
	}

	public void setyExit(int yExit) {
		this.yExit = yExit;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public char[][] getBoard() {
		return board;
	}

	public void setBoard(char[][] board) {
		this.board = board;
	}

//	public ArrayList<Car> getCars() {
//		return cars;
//	}
//	public void setCars(ArrayList<Car> cars) {
//		this.cars = cars;
//	}
	public int getRedDis() {
		return redDis;
	}

	public void setRedDis(int redDis) {
		this.redDis = redDis;
	}

	public int getbCars() {
		return bCars;
	}

	public void setbCars(int bCars) {
		this.bCars = bCars;
	}

	public Car getRedCar() {
		return redCar;
	}

	public void setRedCar(Car redCar) {
		this.redCar = redCar;
	}

	public void removeCar(Car remove) {
		cars.remove(remove);
	}

	public void addCar(Car add) {
		cars.add(add);
		if (add.getKey() == 'X') {
			setRedCar(add);
			redDis = (xExit - add.getLastX());
		}
	}

	public boolean isOnRedPath(Car c) {
		if ((xExit - c.getLastX()) < getRedDis()) {
			if ((c.getLastY() - c.getLength()) < yExit)
				return true;
		}
		return false;
	}

	public boolean isTargetBoard() {
		return (redCar.getLastX() == xExit);
	}

	public void print_h() {
		System.out.println("Red Distance : " + redDis);
		System.out.println("#Blocking Cars : " + bCars);
		System.out.println("h : " + h());
	}

	public void print_full_stats() {
		print_board();
		print_cars();
		print_h();
		System.out.println("is Targe Puzzle : " + isTargetBoard());
	}

	public double getDistanceToStart() {
		return DistanceToStart;
	}

	public void setDistanceToStart(double distanceToStart) {
		DistanceToStart = distanceToStart;
	}

	public ArrayList<Car> getCars() {
		return cars;
	}

	public void setCars(ArrayList<Car> cars) {
		this.cars = cars;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(DistanceToStart);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + bCars;
		result = prime * result + Arrays.deepHashCode(board);
		result = prime * result + ((cars == null) ? 0 : cars.hashCode());
		result = prime * result + ((filePath == null) ? 0 : filePath.hashCode());
		result = prime * result + ((redCar == null) ? 0 : redCar.hashCode());
		result = prime * result + redDis;
		result = prime * result + size;
		result = prime * result + xExit;
		result = prime * result + yExit;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Puzzle other = (Puzzle) obj;
		if (other.size != size)
			return false;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (this.board[i][j] != other.getBoard()[i][j])
					return false;
			}
		}
		return true;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public float getBeta() {
		return beta;
	}

	public void setBeta(float beta) {
		this.beta = beta;
	}

}
