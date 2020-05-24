import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

public abstract class Gene {


	protected Object value;
	protected int fitness;
	protected int age;
	protected int matureAge;
	protected int deathAge;
	protected boolean aging;
	protected boolean mature;
	protected Object target;
	public Gene(Object val, Object tar,boolean _aging) {
		// TODO Auto-generated constructor stub
		value=val;
		target=tar;
		aging=_aging;
		age=0;
		mature = false;
		matureAge=5;
		fitness_calculation();
	}
	public void birthday()
	{
		age++;
		if(age>=matureAge)
			mature = true;
	}
	public void fitness_calculation()//abstarct
	{
		System.out.println("Gene");
		fitness=0;
	}
	public Gene mate(Gene gene)//abstarct
	{
		return null;
	}
	public void mutate()//abstarct
	{
		return;
	}
	public Gene getRandom() //abstarct
	{
		return null;
	}
	public static Comparator<Gene> compare()
	{
		Comparator<Gene> comp= new Comparator<Gene>() {
			
			@Override
			public int compare(Gene o1, Gene o2) {
				// TODO Auto-generated method stub
				if(o1.getFitness()<o2.getFitness())
					return -1;
				if(o1.getFitness()>o2.getFitness())
					return 1;
				return 0;
			}
		};
		return comp;
	}
	//												*
	//												*
	//Setters and Getters nothing important here	*
	//												*
	//												*
	//												*
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}


	public Object getTarget() {
		return target;
	}

	public void setTarget(Object target) {
		this.target = target;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getMatureAge() {
		return matureAge;
	}
	public void setMatureAge(int matureAge) {
		this.matureAge = matureAge;
	}
	public boolean isAging() {
		return aging;
	}
	public void setAging(boolean aging) {
		this.aging = aging;
	}
	public boolean isMature()
	{
		return mature;
	}
	public int getDeathAge() {
		return deathAge;
	}
	public void setDeathAge(int deathAge) {
		this.deathAge = deathAge;
	}
	public Gene mate(Gene gene, Crossover option) {
		// TODO Auto-generated method stub
		return null;
	}
	public void mutate(Mutation mutation) {
		// TODO Auto-generated method stub
		
	}

}
