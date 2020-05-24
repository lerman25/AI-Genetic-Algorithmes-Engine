import java.util.Random;


public class StringGene extends Gene {
	private boolean bullsEye;

	public StringGene(String str, String target, boolean aging) {
		// TODO Auto-generated constructor stub
		super(str, target, aging);
		bullsEye = false;
		fitness_calculation();
	}

	@Override
	public void fitness_calculation() {
		// fitness Calulation
		int fitness = 0;
		if (bullsEye) {
			fitness = bullsEye();
		} else {
			String targetS = (String) this.getTarget();
			String val = (String) this.value;
			char[] valC = val.toCharArray();
			char[] tarC = targetS.toCharArray();
			for (int i = 0; i < valC.length; i++) {
				fitness += Math.abs(valC[i] - tarC[i]);
			}
		}
		this.setFitness(fitness);
	}

	@Override
	public void mutate(Mutation option) {
		Random r = new Random();

		String val = (String) this.value;
		String tarS = (String) this.target;
		char[] valC = val.toCharArray();
		int ipos = r.nextInt(tarS.length());
		int delta = r.nextInt(90) + 32;
		valC[ipos] = (char) ((valC[ipos] + delta) % 122);
		this.value = String.copyValueOf(valC);
		fitness_calculation();
	}

	@Override
	public Gene mate(Gene gene, Crossover option) {
		String val = (String) this.value;
		String target = (String) this.target;
		String mateS = (String) gene.getValue();
		StringGene newSG =null;

		switch (option) {
		case DEFULAT: {
			newSG = new StringGene(singlePointCO(val, mateS), target, this.aging);
			break;
		}
		case SINGLEPOINT: {
			newSG = new StringGene(singlePointCO(val, mateS), target, this.aging);
			break;
		}
		case TWOPOINT: {
			newSG = new StringGene(twoPointCO(val, mateS), target, this.aging);
			break;
		}
		case UINFORM: {
			newSG = new StringGene(uniformCO(val, mateS), target, this.aging);
			break;
		}
		default: {
			System.out.println("Error - Choose Crossover Option");
			break;
		}
		}
		if(newSG==null)
		{
			System.out.println("Error");
			System.exit(1);
		}
		newSG.setDeathAge(this.getDeathAge());
		newSG.setMatureAge(this.getMatureAge());
		return newSG;
	}

	public String twoPointCO(String string1, String string2) {
		Random r = new Random();
		String target = (String) this.target;
		int i1 = r.nextInt(target.length());
		int i2 = r.nextInt(target.length() - i1) + i1;
		String crossed = "";
		crossed = string1.substring(0, i1);
		crossed += string2.substring(i1, i2);
		crossed += string1.substring(i2, target.length());
		return crossed;
	}

	public String singlePointCO(String string1, String string2) {
		Random r = new Random();
		String target = (String) this.target;
		int spos = r.nextInt(target.length());
		String newStr = string1.substring(0, spos);
		newStr += string2.substring(spos, target.length());
		return newStr;
	}

	public String uniformCO(String string1, String string2) {
		Random r = new Random();
		String target = (String) this.target;
		String newStr = "";
		for (int i = 0; i < target.length(); i++) {
			char newChar;
			if (r.nextInt(2) == 1) // either 1 or 0 - 50% chance for each
			{
				newChar = string1.charAt(i);
			} else {
				newChar = string2.charAt(i);

			}
			newStr += newChar;
		}
		return newStr;
	}

	@Override
	public Gene getRandom() {
		String tarS = (String) this.target;
		char[] valC = new char[tarS.length()];
		Random r = new Random();
		for (int i = 0; i < tarS.length(); i++) {
			valC[i] = (char) (r.nextInt(90) + 32);
		}
		StringGene randSg = new StringGene(String.copyValueOf(valC), tarS, this.aging);
		randSg.setDeathAge(this.getDeathAge());
		randSg.setMatureAge(this.getMatureAge());
		return randSg;
	}

	@Override
	public String toString() {
		String val = (String) this.value;
		return val;
	}

	public int bullsEye() {
		String tarS = (String) this.target;
		String val = (String) this.value;
		char[] valC = val.toCharArray();
		int fitness = 122 * 12;
		for (int i = 0; i < valC.length; i++) {
			int index = tarS.indexOf(valC[i]);
			int cindex = val.indexOf(valC[i]);
			// the case in which their are more then one specific char in the string
			while (cindex != i) {
				index = tarS.indexOf(valC[i], index + 1);
				cindex = val.indexOf(valC[i], cindex + 1);
			}
			if (index != i)
				fitness -= 12;
			else
				fitness -= 122;
		}
		return fitness;
	}

	public boolean isBullsEye() {
		return bullsEye;
	}

	public void setBullsEye(boolean bullsEye) {
		this.bullsEye = bullsEye;
	}

}
