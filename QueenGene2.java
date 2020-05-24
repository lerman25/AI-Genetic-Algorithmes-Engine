import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.awt.Point;

public class QueenGene2 extends Gene {
	// This constructor is only for the initial.
	public QueenGene2(ArrayList<Point> queens, boolean _aging) {
		super(queens, 0, _aging);
		// TODO Auto-generated constructor stub
		fitness_calculation();
	}

	@Override
	public Gene mate(Gene gene) {
		// TODO Auto-generated method stub
		return super.mate(gene);
	}

	@Override
	public void mutate() {
		// TODO Auto-generated method sayList<Point>)tub
		ArrayList<Point> queens = (ArrayList<Point>) this.value;
		int nqueens = queens.size();
		int i = 0;
		Random r = new Random();
		int queen = r.nextInt(nqueens);
		int y;
		int x;
		Point newPoint;
		do {
			y = r.nextInt(nqueens);
			x = r.nextInt(nqueens);
			newPoint = new Point(x, y);
		} while (queens.contains(newPoint));
		queens.get(queen).setLocation(x, y);
		fitness_calculation();
	}

	@Override
	public Gene getRandom() {
		// TODO Auto-generated method stub
		ArrayList<Point> queens = (ArrayList<Point>) this.value;
		int nqueens = queens.size();
		ArrayList<Point> rand_queens = new ArrayList<Point>(nqueens);
		int i = 0;
		Random r = new Random();
		while (i < nqueens) {
			int y = r.nextInt(nqueens);
			int x = r.nextInt(nqueens);
			Point newPoint = new Point(x, y);
			if (!rand_queens.contains(newPoint)) {
				rand_queens.add(newPoint);
				i++;
			} else
				;
		}
		rand_queens.sort(Comparator.comparing(Point::getX));
		QueenGene randQg = new QueenGene(rand_queens, this.aging);
		randQg.setDeathAge(this.getDeathAge());
		randQg.setMatureAge(this.getMatureAge());
		return randQg;
	}

	@Override
	public Gene mate(Gene gene, Crossover option) {
		// TODO Auto-generated method stub
		ArrayList<Point> queens = (ArrayList<Point>) this.value;
		int nqueens = queens.size();
		ArrayList<Point> mate_queens = (ArrayList<Point>) gene.value;
		QueenGene newSG = null;

		switch (option) {
		case DEFULAT: {
			newSG = new QueenGene(singlePointCO(queens, mate_queens), this.aging);
			break;
		}
		case SINGLEPOINT: {
			newSG = new QueenGene(singlePointCO(queens, mate_queens), this.aging);
			break;
		}
		case TWOPOINT: {
			newSG = new QueenGene(twoPointCO(queens, mate_queens), this.aging);
			break;
		}
		case UINFORM: {
			newSG = new QueenGene(uniformCO(queens, mate_queens), this.aging);
			break;
		}
		default: {
			System.out.println("Error - Choose Crossover Option");
			break;
		}
		}
		if (newSG == null) {
			System.out.println("Error");
			System.exit(1);
		}
		newSG.setDeathAge(this.getDeathAge());
		newSG.setMatureAge(this.getMatureAge());
		return newSG;
	}

	@Override
	public void fitness_calculation() {
		// TODO Auto-generated method stub
		ArrayList<Point> queens = (ArrayList<Point>) this.value;
		int nqueens = queens.size();
		this.fitness = nqueens * nqueens;
		for (int i = 0; i < nqueens; i++) {
			// add here call to is safe queen
			if(isPlaceSafe(queens.get(i).getLocation()))
			{
					fitness-=nqueens;
			}
		}
	}

	public ArrayList<Point> twoPointCO(ArrayList<Point> queens, ArrayList<Point> mate_queens) {
		int nqueens = queens.size();
		Random r = new Random();
		int i1 = r.nextInt(nqueens);
		int i2 = r.nextInt(nqueens - i1) + i1;
		int spos = r.nextInt(nqueens);
		ArrayList<Point> newQueens = new ArrayList<Point>(nqueens);
		for (int i = 0; i < i1; i++) {
			newQueens.add(i, queens.get(i));

		}
		for (int i = i1; i < i2; i++) {
			newQueens.add(i, mate_queens.get(i));

		}
		for (int i = i2; i < nqueens; i++) {
			newQueens.add(i, queens.get(i));

		}
		return newQueens;
	}

	public ArrayList<Point> singlePointCO(ArrayList<Point> queens, ArrayList<Point> mate_queens) {
		Random r = new Random();
		int nqueens = queens.size();
		int spos = r.nextInt(nqueens);
		ArrayList<Point> newQueens = new ArrayList<Point>(nqueens);
		for (int i = 0; i < nqueens; i++) {
			if (i < spos)
				newQueens.add(i, queens.get(i));
			else
				newQueens.add(i, mate_queens.get(i));

		}
		return newQueens;
	}

	public ArrayList<Point> uniformCO(ArrayList<Point> queens, ArrayList<Point> mate_queens) {
		Random r = new Random();
		int nqueens = queens.size();
		ArrayList<Point> newQueens = new ArrayList<Point>(nqueens);
		for (int i = 0; i < nqueens; i++) {
			if (r.nextInt(2) == 1) // either 1 or 0 - 50% chance for each
			{
				newQueens.add(i, queens.get(i));
			} else {
				newQueens.add(i, mate_queens.get(i));

			}
		}
		return newQueens;
	}

	private boolean isPlaceSafe(Point place) {
		ArrayList<Point> queens = (ArrayList<Point>) this.value;
		int nqueens = queens.size();
		/* Check this row on left side */
		for(int i=0;i<nqueens;i++)
		{
			Point current = queens.get(i);
			//check row 
			if(place.getX()==current.getX())
				return false;
			// check column
			if(place.getY()==current.getY())
				return false;
			//check diagonal
			if((Math.abs(place.getY()-current.getY()))==(Math.abs(place.getX()-current.getX())))
				return false;
		}
		System.out.println("Safe");
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		print_board();
		return super.toString();
	}

	public void print_board() {
		System.out.print("\n\n");
		ArrayList<Point> queens = (ArrayList<Point>) this.value;
		int nqueens = queens.size();
		for (int i = 0; i < nqueens; i++) {
			System.out.print("*****   ");
			for (int j = 0; j < nqueens; j++) {
				Point current = new Point(j, i);
				if(queens.contains(current))
					System.out.print("Q");
				else
				System.out.print("O");
			}
			System.out.println("");
		}
		System.out.print("\n\n");
	}
}
