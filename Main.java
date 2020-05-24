import java.awt.Point;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StringGene sg1 = new StringGene("Hello Worle!", "Hello World!",false);
		sg1.setDeathAge(10);
		sg1.setMatureAge(3);
		StringGene sg2 = new StringGene("Hello Worde!", "Hello World!",false);
		int[] queens = new int[1024];
		int[][] newKnapSack = new int[4][24]; 
		newKnapSack[0]=Arrays.asList(135,
				139,
				149,
				150,
				156,
				163,
				173,
				184,
				192,
				201,
				210,
				214,
				221,
				229,
				240).stream().mapToInt(i->i).toArray();
		newKnapSack[1]=Arrays.asList( 70,
				 73,
				 77,
				 80,
				 82,
				 87,
				 90,
				 94,
				 98,
				106,
				110,
				113,
				115,
				118,
				120).stream().mapToInt(i->i).toArray();;
		Arrays.fill(newKnapSack[2],0);
		newKnapSack[3][0]=750;
		SackGene skg = new SackGene(newKnapSack, false);
		QueenGene qg1 = new QueenGene(queens,false);
		Population pop = new Population();
//		pop.setPopSize(16000);
		pop.setOption(Crossover.DEFULAT);
		pop.pop_init(skg);
		pop.setMaxIter(10000);
//		pop.setMutateRate(0.5);
//		pop.setMaxIter(Integer.MAX_VALUE);
//		pop.setMutOption(Mutation.INSERTION);
		// Implemented as Thread because java supports cpu time only with threads.
		Thread convergenceThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
				 threadMXBean.setThreadCpuTimeEnabled(true);
				if(!threadMXBean.isThreadCpuTimeSupported())
				{
					System.out.println("CPU time is not suported for this JVM");
				}
				

				long convergenceStart = System.nanoTime(); // timer for Convergence elapsed start
				long convergenceCpu = threadMXBean.getCurrentThreadCpuTime();
				for (int i = 0; i < pop.getMaxIter(); i++) {
					long genStart = System.nanoTime();; // timer for generation elapsed start
					long genCpu= threadMXBean.getCurrentThreadCpuTime();
					pop.fitness_sort();
					System.out.println(pop.getPopulation().get(0).toString()+"Fitness: "+pop.getPopulation().get(0).getFitness());
					pop.print_fitness_stats();
					pop.mate();
					if(pop.isAging())
						pop.new_year();
					pop.swap();
					long genElapsed = (System.nanoTime()-genStart)/1000; // timer for generation elapsed start
					long genCpuTime= (threadMXBean.getCurrentThreadCpuTime()-genCpu)/1000;
					System.out.println(" Cpu : "+genCpuTime +" Elapsed "+genElapsed);
				}
				long elapsedTime = (System.nanoTime()-convergenceStart)/1000; 
				long time2 = (threadMXBean.getCurrentThreadCpuTime()-convergenceCpu)/1000;
				System.out.println("Cpu : "+time2 +" Elapsed "+elapsedTime);
				System.out.println(pop.rwscounter);
			}
				
			
		});
		convergenceThread.start();
	
	}
}