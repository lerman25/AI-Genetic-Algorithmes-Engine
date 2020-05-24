import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.awt.Point;

public class QueenGene extends Gene {
	// This constructor is only for the initial.
	public QueenGene(int queens[], boolean _aging) {
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
	public void mutate(Mutation mutation) {
		// TODO Auto-generated method sayList<Point>)tub
		int[] queens = (int[]) this.value;
		int nqueens = queens.length;
		Random r = new Random();
		switch(mutation)
		{
		case DEFUALT:
		{
			int index1 = r.nextInt(nqueens);
			int index2 = r.nextInt(nqueens);
			int temp  = queens[index1];
			queens[index1]=queens[index2];
			queens[index2]=temp;
			break;
		}
		case INSERTION:
		{
			ArrayList<Integer> queenList = new ArrayList<Integer>();
			for(int i=0;i<nqueens;i++)
				queenList.add(i,queens[i]);
			int index1 = r.nextInt(nqueens);
			int index2 = r.nextInt(nqueens);
			int queen = queens[index1];
			queenList.remove(index1);
			queenList.add(index2, queen);
			for(int i=0;i<nqueens;i++)
				queens[i]=queenList.get(i);
			break;
		}
		case SWAP:
		{
			int index1 = r.nextInt(nqueens);
			int index2 = r.nextInt(nqueens);
			int temp  = queens[index1];
			queens[index1]=queens[index2];
			queens[index2]=temp;
			break;
		}
		default:
		{
			break;
		}
		
		}
		fitness_calculation();
	}

	@Override
	public Gene getRandom() {
		// TODO Auto-generated method stub
		int[] queens = (int[]) this.value;
		int nqueens = queens.length;
		int[] rand_queens = new int[nqueens];
		int i = 0;
		Random r = new Random();
		for(i=0;i<nqueens;i++)
			rand_queens[i]=r.nextInt(nqueens);
		QueenGene randQg = new QueenGene(rand_queens, this.aging);
		randQg.setDeathAge(this.getDeathAge());
		randQg.setMatureAge(this.getMatureAge());
		return randQg;
	}

	@Override
	public Gene mate(Gene gene, Crossover option) {
		// TODO Auto-generated method stub
		int[] queens = (int[]) this.value;
		int nqueens = queens.length;
		int[] mate_queens = (int[]) gene.value;
		QueenGene newSG = null;

		switch (option) {
		case OX: {
			newSG = new QueenGene(orderCrossOver(queens, mate_queens), this.aging);
			break;
		}
		case ER: {
			newSG = new QueenGene(edgeRecombination(queens, mate_queens), this.aging);
			break;
		}
		case DEFULAT:
		{
			newSG = new QueenGene(orderCrossOver(queens, mate_queens), this.aging);
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
		int[] queens = (int[]) this.value;
		int nqueens = queens.length;
		this.fitness = nqueens;
		for (int i = 0; i < nqueens; i++) {
			// add here call to is safe quee
			Point place = new Point(i,queens[i]);
			if(isPlaceSafe(place))
			{
					fitness--;
			}
		}
	}

//	public int[] twoPointCO(int[] queens, int[] mate_queens) {
//		int nqueens = queens.length;
//		Random r = new Random();
//		int i1 = r.nextInt(nqueens);
//		int i2 = r.nextInt(nqueens - i1) + i1;
//		int spos = r.nextInt(nqueens);
//		int[] newQueens = new int[nqueens];
//		for (int i = 0; i < i1; i++) {
//			newQueens.add(i, queens.get(i));
//
//		}
//		for (int i = i1; i < i2; i++) {
//			newQueens.add(i, mate_queens.get(i));
//
//		}
//		for (int i = i2; i < nqueens; i++) {
//			newQueens.add(i, queens.get(i));
//
//		}
//		return newQueens;
//	}
//
//	public int[] singlePointCO(int[] queens, int[] mate_queens) {
//		Random r = new Random();
//		int nqueens = queens.length;
//		int spos = r.nextInt(nqueens);
//		int[] newQueens = new int[](nqueens);
//		for (int i = 0; i < nqueens; i++) {
//			if (i < spos)
//				newQueens.add(i, queens.get(i));
//			else
//				newQueens.add(i, mate_queens.get(i));
//
//		}
//		return newQueens;
//	}
//
//	public int[] uniformCO(int[] queens, int[] mate_queens) {
//		Random r = new Random();
//		int nqueens = queens.length;
//		int[] newQueens = new int[](nqueens);
//		for (int i = 0; i < nqueens; i++) {
//			if (r.nextInt(2) == 1) // either 1 or 0 - 50% chance for each
//			{
//				newQueens.add(i, queens.get(i));
//			} else {
//				newQueens.add(i, mate_queens.get(i));
//
//			}
//		}
//		return newQueens;
//	}
public int[] orderCrossOver(int[] queens, int[] mate_queens)
{
	int nqueens = queens.length;
	Random r = new Random();
	int[] newQueens = new int[nqueens];
	Arrays.fill(newQueens,-1);
	PriorityQueue<Integer> indexes = new PriorityQueue<Integer>();
	for(int i=0;i<nqueens;i++)
		indexes.add(i);
	for(int i=0;i<(nqueens/2);i++)
	{
		int index = r.nextInt(nqueens);
		if(newQueens[index]==-1)
		{
			newQueens[index]=queens[index];
			indexes.remove(index);
		}
		else
			i++;
	}
	for(int i=0;i<nqueens;i++)
	{
		if(newQueens[i]==-1)
		{
			newQueens[i]=mate_queens[indexes.poll()];
		}
	}
	return newQueens;
	
}
public int[] edgeRecombination(int[] queens, int[] mate_queens)
{
	int nqueens = queens.length;
	ArrayList<ArrayList<Integer>> graph = new ArrayList<ArrayList<Integer>>(nqueens);
	for(int i=0;i<nqueens;i++)
		graph.add(i,new ArrayList<Integer>());
	ArrayList<Integer> queenList = new ArrayList<Integer>();
	Random r = new Random();
	for(int i=0;i<nqueens;i++)
	{
		int index = i;
		if(index==0)
			index=nqueens;
		if(!graph.get(queens[i]).contains(queens[index-1]))
			graph.get(queens[i]).add(queens[index-1]);
		index=i;
		if(index==(nqueens-1))
			index=-1;
		if(!graph.get(queens[i]).contains(queens[index+1]))
			graph.get(queens[i]).add(queens[index+1]);
	}
	for(int i=0;i<nqueens;i++)
	{
		int index = i;
		if(index==0)
			index=nqueens;
		if(!graph.get(mate_queens[i]).contains(mate_queens[index-1]))
			graph.get(mate_queens[i]).add(mate_queens[index-1]);
		index=i;
		if(index==(nqueens-1))
			index=-1;
		if(!graph.get(mate_queens[i]).contains(mate_queens[index+1]))
			graph.get(mate_queens[i]).add(mate_queens[index+1]);
	}
	int start = r.nextInt(nqueens);
	int i=0;
	while(queenList.size()<nqueens-1)
	{
		queenList.add(i,start);
		for(int j=0;j<nqueens;j++)
			graph.get(j).remove(Integer.valueOf(start));
		int next = -1;
		if(graph.get(start).isEmpty())
		{
			do
			{
				start = r.nextInt(nqueens);
			}
			while(queenList.contains(start));
			i++;
		}
		else
		{
			int mindeg=nqueens;
			int mindegval=-1;
			for(int j=0;j<graph.get(start).size();j++)
			{
				int current = graph.get(start).get(j);
				int index= current;
				int currentDegree  = graph.get(index).size();
				if(currentDegree<mindeg)
				{
					mindeg=currentDegree;
					mindegval=current;
				}
			}
			start = mindegval;
			i++;
		}	
	}
	queenList.add(i,start);
	int[] newQueens = new int[nqueens];
	for(int j=0;j<nqueens;j++)
		newQueens[j]=queenList.get(j);
	return newQueens;
}

	private boolean isPlaceSafe(Point place) {
		int[] queens = (int[]) this.value;
		int nqueens = queens.length;
		for(int i=0;i<nqueens;i++)
		{

				Point current = new Point(i,queens[i]);
				if(!(current.distance(place)==0))
				{
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
		}
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
		int[] queens = (int[]) this.value;
		int nqueens = queens.length;
		for (int i = 0; i < nqueens; i++) {
			for (int j = 0; j < nqueens; j++) {
				Point current = new Point(i,j);
				if(queens[i]==j)
				{
					String qprint=" q";
					if(!isPlaceSafe(current))
						qprint=" Q";
					System.out.print(qprint);

				}
				else
				System.out.print(" O");
			}
			System.out.println("");
		}
		System.out.print("\n\n");
	}
}
