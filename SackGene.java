import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SackGene extends Gene{

	public SackGene(Object val, boolean _aging) {
		super(val, 0, _aging);
		// TODO Auto-generated constructor stub
		fitness_calculation();

	}

	@Override
	public void fitness_calculation() {
		// TODO Auto-generated method stub
		int[][] knapSack = (int[][])this.value;
		int[] price = knapSack[0];
		int[] weight = knapSack[1];
		int[] inSack = knapSack[2];
		int max = knapSack[3][0];
		int totalSackWeight=0;
		for(int i=0;i<weight.length;i++)
		{
			totalSackWeight+=(weight[i]*inSack[i]);
		}
		if(totalSackWeight>max)
		{
			this.fitness=Integer.MAX_VALUE;
		}
		else
		{
			int fitSum=0;
			for(int i=0;i<price.length;i++)
			{
				fitSum-=(price[i]*inSack[i]);
			}
			fitness=fitSum;
		}
		
	}

	@Override
	public Gene getRandom() {
		// TODO Auto-generated method stub
		int[][] knapSack = (int[][])this.value;
		int[] price = knapSack[0];
		int[] weight = knapSack[1];
		int[] newInSack = new int[price.length];
		int[][] newKnapSack = new int[4][price.length];
		newKnapSack[0]=Arrays.copyOf(price, price.length);
		newKnapSack[1]=Arrays.copyOf(weight, weight.length);
		newKnapSack[3][0]=knapSack[3][0];

		int max = knapSack[3][0];
		Random r = new Random();
		for(int i=0;i<newInSack.length;i++)
		{
			newInSack[i] = r.nextInt(2);
		}
		SackGene randSkg = new SackGene(newKnapSack, this.aging);
		randSkg.setDeathAge(this.getDeathAge());
		randSkg.setMatureAge(this.getMatureAge());
		return randSkg;
	}

	@Override
	public Gene mate(Gene gene, Crossover option) {
		// TODO Auto-generated method stub
		int[][] knapSack = (int[][])this.value;
		int[] inSack = knapSack[2];
		int[][] mateSack = (int[][])gene.value;
		int[] mateInSack = mateSack[2];

		int max = knapSack[3][0];
		SackGene newSkG = null;
		switch (option) {
		case DEFULAT: {
			newSkG = new SackGene(singlePointCO(knapSack, mateSack), this.aging);
			break;
		}
		case SINGLEPOINT: {
			newSkG = new SackGene(singlePointCO(knapSack, mateSack), this.aging);
			break;
		}
		case TWOPOINT: {
			newSkG = new SackGene(twoPointCO(knapSack, mateSack), this.aging);
			break;
		}
		case UINFORM: {
			newSkG = new SackGene(uniformCO(knapSack, mateSack), this.aging);
			break;
		}
		default: {
			System.out.println("Error - Choose Crossover Option");
			break;
		}
		}
		if(newSkG==null)
		{
			System.out.println("Error");
			System.exit(1);
		}
		newSkG.setDeathAge(this.getDeathAge());
		newSkG.setMatureAge(this.getMatureAge());
		return newSkG;	
		}
	public int[][]  twoPointCO(int[][] knapSack1, int[][] knapSack2) {
		Random r = new Random();
		int[] inSack1 = knapSack1[2];
		int[] inSack2 = knapSack2[2];

		int i1 = r.nextInt(inSack1.length);
		int i2 = r.nextInt(inSack1.length - i1) + i1;
		int[] crossed = new int[inSack1.length];
		for (int i = 0; i < i1; i++) {
			crossed[i]=inSack1[i];

		}
		for (int i = i1; i < i2; i++) {
			crossed[i]=inSack2[i];

		}
		for (int i = i2; i < inSack1.length; i++) {
			crossed[i]=inSack1[i];

		}
		int[][] newKnapSack = new int[4][inSack1.length];
		newKnapSack[0]=Arrays.copyOf(knapSack1[0], inSack1.length);
		newKnapSack[1]=Arrays.copyOf(knapSack1[1], inSack1.length);
		newKnapSack[2]=Arrays.copyOf(crossed,inSack1.length);
		newKnapSack[3][0]=knapSack1[3][0];
		return newKnapSack;
	}

	public int[][] singlePointCO(int[][] knapSack1, int[][] knapSack2) {
		Random r = new Random();
		int[] inSack1 = knapSack1[2];
		int[] inSack2 = knapSack2[2];
		int i1 = r.nextInt(inSack1.length);
		int[] crossed = new int[inSack1.length];
		for (int i = 0; i < i1; i++) {
			crossed[i]=inSack1[i];

		}
		for (int i = i1; i <inSack1.length; i++) {
			crossed[i]=inSack2[i];

		}
		int[][] newKnapSack = new int[4][inSack1.length];
		newKnapSack[0]=Arrays.copyOf(knapSack1[0], inSack1.length);
		newKnapSack[1]=Arrays.copyOf(knapSack1[1], inSack1.length);
		newKnapSack[2]=Arrays.copyOf(crossed,inSack1.length);
		newKnapSack[3][0]=knapSack1[3][0];
		return newKnapSack;
	}

	public int[][] uniformCO(int[][] knapSack1, int[][] knapSack2) {
		Random r = new Random();
		int[] inSack1 = knapSack1[2];
		int[] inSack2 = knapSack2[2];
		int[] crossed = new int[inSack1.length];

		for (int i = 0; i <inSack1.length; i++) {
			if (r.nextInt(2) == 1) // either 1 or 0 - 50% chance for each
			{
				crossed[i]=inSack1[i];
			} else {
				crossed[i]=inSack2[i];
			}
		}
		int[][] newKnapSack = new int[4][inSack1.length];
		newKnapSack[0]=Arrays.copyOf(knapSack1[0], inSack1.length);
		newKnapSack[1]=Arrays.copyOf(knapSack1[1], inSack1.length);
		newKnapSack[2]=Arrays.copyOf(crossed,inSack1.length);
		newKnapSack[3][0]=knapSack1[3][0];
		return newKnapSack;
	}
	@Override
	public void mutate(Mutation mutation) {
		int[][] knapSack = (int[][])this.value;
		int[] price = knapSack[0];
		int[] weight = knapSack[1];
		int[] inSack = knapSack[2];
		int max = knapSack[3][0];
		Random r = new Random();
		inSack[r.nextInt(inSack.length)]=1-inSack[r.nextInt(inSack.length)];
		fitness_calculation();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		int[][] knapSack = (int[][])this.value;
		int[] price = knapSack[0];
		int[] weight = knapSack[1];
		int[] inSack = knapSack[2];
		for(int i=0;i<inSack.length;i++)
			System.out.print(inSack[i]+"\n");
		return super.toString();
	}
	

}
