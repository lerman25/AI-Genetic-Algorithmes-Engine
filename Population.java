import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.TreeMap;

public class Population {
	private ArrayList<Gene> population;
	private ArrayList<Gene> buffer;
	private int popSize = 2048;
	private int maxIter = 16384;
	private double elitRate = 0.1;
	private double mutateRate = 0.25;
	private int mutateSize = (int) (Integer.MAX_VALUE * mutateRate);
	public int rwscounter = 0;
	private boolean aging;
	private Object Target;
	private Crossover option = Crossover.DEFULAT;
	private Mutation mutOption = Mutation.DEFUALT;
	public Population() {
		// TODO Auto-generated constructor stub
		population = new ArrayList<Gene>();
		buffer = new ArrayList<Gene>();
	}

	public void pop_init(Gene type) {
		setAging(type.aging);
		for (int i = 0; i < popSize; i++) {
			Gene randGene = type.getRandom();
			population.add(randGene);

		}
	}

	public void elitism(int esize) {
		buffer = new ArrayList<Gene>();
		for (int i = 0; i < esize; i++) {
			buffer.add(population.get(i));
		}
	}

	public void mate() {
		int esize = (int) (popSize * elitRate);
		int i1, i2, spos;
		elitism(esize);
		Random r = new Random();
		ArrayList<Gene> matingPop;
//		 top population
		for(int i=esize;i<population.size();i++)
		{
			i1 =r.nextInt(popSize/2);
			i2 = r.nextInt(popSize/2);
			Gene mate = (population.get(i1)).mate(population.get(i2),option);
			if(r.nextInt(Integer.MAX_VALUE)<mutateSize)
				mate.mutate(mutOption);
			buffer.add(i,mate);
		}
		// aging
//		matingPop = this.maturePop();
		// rws population
//		matingPop = rws(population,esize);
//		this is relevent for rws and aging.
//		for (int i = 0; i < matingPop.size(); i++) {
//			i1 = r.nextInt(matingPop.size());
//			i2 = r.nextInt(matingPop.size());
//			Gene mate = (matingPop.get(i1)).mate(matingPop.get(i2));
//			if (r.nextInt(Integer.MAX_VALUE) < mutateSize)
//				mate.mutate();
//			buffer.add(mate);
//		}
//		matingPop = tournament(4, esize);
//		for(int i=0;i<matingPop.size();i=i+2)
//		{
//			Gene mate = (matingPop.get(i)).mate(matingPop.get(i+1),option);
//			if (r.nextInt(Integer.MAX_VALUE) < mutateSize)
//				mate.mutate();
//			buffer.add(mate);
//		}

	}

	/**
	 * @param population - sorted by fitness roulette wheel selection
	 * @return population-size
	 */
	public ArrayList<Gene> rws(ArrayList<Gene> population, int k) {
		ArrayList<Gene> rws = new ArrayList<Gene>();
		int sumOfFitness = 0;
		TreeMap<Double, Gene> geneTree = new TreeMap<Double, Gene>();
		double accRatio = 0;
		for (int i = 0; i < population.size(); i++) {
			Gene current = population.get(i);
			sumOfFitness += current.getFitness();
		}
		for (int i = 0; i < population.size(); i++) {
			Gene current = population.get(i);
			accRatio += ((double) current.getFitness() / (double) sumOfFitness);
			geneTree.put(accRatio, current);
		}

		// spin
		for (int i = 0; i < k; i++) {
			Entry<Double, Gene> selected = geneTree.ceilingEntry(Math.random());
			if (selected != null) {
				Gene selectedGene = selected.getValue();
				rws.add(selectedGene);
			}
			// try again...
			else {
				i--;
				// debugging purpose
				rwscounter++;
			}

		}
		return rws;
	}

	public ArrayList<Gene> maturePop() {
		ArrayList<Gene> maturePop = new ArrayList<Gene>(population.size());
		for (int i = 0; i < population.size(); i++) {
			Gene g = population.get(i);
			if (g.isMature())
				maturePop.add(g);
		}
		return maturePop;
	}

	public ArrayList<Gene> survivors() {
		ArrayList<Gene> survivePop = new ArrayList<Gene>();
		for (int i = 0; i < population.size(); i++) {
			Gene g = population.get(i);
			if (g.getAge() < g.getDeathAge())
				survivePop.add(g);
		}
		return survivePop;
	}

	public ArrayList<Gene> survivors(ArrayList<Gene> buffer) {
		ArrayList<Gene> survivePop = new ArrayList<Gene>();
		for (int i = 0; i < buffer.size(); i++) {
			Gene g = buffer.get(i);
			if (g.getAge() < g.getDeathAge())
				survivePop.add(g);
		}
		return survivePop;
	}

	public void fitness_sort() {
		Collections.sort(population, Gene.compare());
	}

	public void new_year() {
		for (Gene g : population)
			g.birthday();
	}

	public void swap() {
		ArrayList<Gene> temp = new ArrayList<Gene>(population);
		if (aging)
			population = new ArrayList<Gene>(survivors(buffer));
		else
			population = new ArrayList<Gene>(buffer);
		buffer = new ArrayList<Gene>(temp);
//		buffer= new ArrayList<Gene>();
	}

	public void print_fitness_stats() {
		double sum = 0;
		for (int i = 0; i < population.size(); i++) {
			sum += population.get(i).getFitness();
		}
		double average = sum / population.size();
		sum = 0;
		for (int i = 0; i < population.size(); i++) {
			sum += Math.pow(population.get(i).getFitness() - average, 2);
		}
		sum /= population.size();
		double variance = Math.sqrt(sum);
		DecimalFormat formatter = new DecimalFormat("#0.000");
		System.out.println("Avreage fitness for generation: " + formatter.format(average) + " Variance  :"
				+ formatter.format(variance));
	}

	public ArrayList<Gene> tournament(int k, int esize) {
		ArrayList<Gene> winners = new ArrayList<Gene>();
		PriorityQueue<Integer> indexes = new PriorityQueue<Integer>();
		for (int j = 0; j < esize; j++) {
			Random r = new Random();
			for (int i = 0; i < k; i++) {
				indexes.add(r.nextInt(population.size()));
			}
			winners.add(population.get(indexes.poll()));
		}

		return winners;
	}

	// *
	// *
	// Setters and Getters nothing important here *
	// *
	// *
	// *
	public ArrayList<Gene> getPopulation() {
		return population;
	}

	public void setPopulation(ArrayList<Gene> population) {
		this.population = population;
	}

	public int getPopSize() {
		return popSize;
	}

	public void setPopSize(int popSize) {
		this.popSize = popSize;
	}

	public int getMaxIter() {
		return maxIter;
	}

	public void setMaxIter(int maxIter) {
		this.maxIter = maxIter;
	}

	public double getElitRate() {
		return elitRate;
	}

	public void setElitRate(double elitRate) {
		this.elitRate = elitRate;
	}

	public double getMutateRate() {
		return mutateRate;
	}

	public void setMutateRate(double mutateRate) {
		this.mutateRate = mutateRate;
	}

	public Object getTarget() {
		return Target;
	}

	public void setTarget(Object target) {
		Target = target;
	}

	public boolean isAging() {
		return aging;
	}

	public void setAging(boolean aging) {
		this.aging = aging;
	}

	public Crossover getOption() {
		return option;
	}

	public void setOption(Crossover option) {
		this.option = option;
	}

	public int getMutateSize() {
		return mutateSize;
	}

	public void setMutateSize(int mutateSize) {
		this.mutateSize = mutateSize;
	}

	public Mutation getMutOption() {
		return mutOption;
	}

	public void setMutOption(Mutation mutOption) {
		this.mutOption = mutOption;
	}

}
