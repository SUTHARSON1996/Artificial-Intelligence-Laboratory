import java.util.*;

public class TSP_SimulatedAnnealing {

    int numCities;
    int sourceCity;
    int tourDistance;
    double avgStepCost;
    double currentEnergy;
    double nextEnergy;
    double temperature;
    String solutioinSpace;
    HashSet<Integer> visitedCities = new HashSet<>();
    HashMap<Integer,ArrayList<String>> adjacencyList = new HashMap<>();
    Queue<Integer> queue = new LinkedList<>();

    public void initializeGraph(){
        this.avgStepCost = 0;
        this.tourDistance = 0;
        this.solutioinSpace = "";
        String input="";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter temperature value : ");
        this.temperature = scanner.nextInt();
        System.out.println("Enter number of cities : ");
        this.numCities = scanner.nextInt();
        System.out.println("Enter the Source city : range( 0 -"+(this.numCities-1)+" )");
        this.sourceCity = scanner.nextInt();
        this.queue.add(sourceCity);
        System.out.println("Enter the edge Distances as follows(Type 'exit' to finish reading)\n<Source> <Destination> <Distance>");
        input = scanner.nextLine();
        input = scanner.nextLine();
        while (!input.equals("exit")){
            String[] parse = input.split("\\s+");

            int source = Integer.parseInt(parse[0]);
            int destination = Integer.parseInt(parse[1]);


            if(!this.adjacencyList.containsKey(source)){
                ArrayList<String> list = new ArrayList<>();
                list.add(parse[1]+" "+parse[2]);
                this.adjacencyList.put(source,list);
            }else{
                ArrayList<String> list = this.adjacencyList.get(source);
                list.add(parse[1]+" "+parse[2]);
                this.adjacencyList.put(source,list);

            }
            if(!this.adjacencyList.containsKey(destination)){
                ArrayList<String> list = new ArrayList<>();
                list.add(parse[0]+" "+parse[2]);
                this.adjacencyList.put(destination,list);
            }else{
                ArrayList<String> list = this.adjacencyList.get(destination);
                list.add(parse[0]+" "+parse[2]);
                this.adjacencyList.put(destination,list);
            }
            input = scanner.nextLine();
        }

    }


    public void setAvgStepCost(){
        int edgeCount=0;
        for(int i=0; i<this.numCities && this.adjacencyList.containsKey(i); i++){
            ArrayList<String> adjacency = this.adjacencyList.get(i);
            for(String x:adjacency){
                this.avgStepCost =this.avgStepCost +  Double.parseDouble(x.split("\\s+")[1]);
                edgeCount++;
            }
        }
        this.avgStepCost = this.avgStepCost/edgeCount;
        System.out.println("Average Step Cost = "+this.avgStepCost);
    }

    public void setInitialEnergy(){
        this.currentEnergy = this.avgStepCost * this.numCities;
        System.out.println("Initial Energy = "+this.currentEnergy);
    }

    public double generateSigmoid(int stepCost){
        double proability = 0;
        int citiesYetToCover=this.numCities-this.visitedCities.size();
        double currentHeuristics = this.currentEnergy;
        double nextHeuristics = (this.tourDistance + stepCost) +(citiesYetToCover*this.avgStepCost);
        this.nextEnergy = nextHeuristics;
        proability = 1/(1+(Math.exp((nextHeuristics - currentHeuristics)/this.temperature)));
        System.out.print(" current energy = "+currentHeuristics+", next energy = "+nextHeuristics+", sigmoid = "+proability);
        return proability;

    }


    public boolean explorePath(int currentCity){
        ArrayList<String> adjacency = this.adjacencyList.get(currentCity);
        ArrayList<String> unvisitedCities = new ArrayList<>();
        boolean flag = false;

        Random random = new Random();
        int index = -1;
        for(String x:adjacency){
            if(!this.visitedCities.contains(Integer.parseInt(x.split("\\s+")[0]))){
                unvisitedCities.add(x);
            }
        }
        if(unvisitedCities.size() == 0){
            return flag;
        }

        while(flag!=true){
            index = random.nextInt(unvisitedCities.size());
            String x = unvisitedCities.get(index);
            System.out.print(currentCity+"->"+x.split("\\s+")[0]);
            int distance = Integer.parseInt(x.split("\\s+")[1]);
            double probability = this.generateSigmoid(distance);
            double randProb = random.nextDouble();
            System.out.println(", random probability = "+randProb);
            if(randProb < probability){
                this.tourDistance = this.tourDistance+distance;
                this.temperature = this.temperature - 1;
                this.currentEnergy = this.nextEnergy;
                this.solutioinSpace = this.solutioinSpace + "--("+distance+")-->";
                this.queue.add(Integer.parseInt(x.split("\\s+")[0]));
                flag = true;
                break;
            }

        }
        return flag;
    }



    public boolean returnToSourceCity(int currentCity){
        boolean flag = false;
        ArrayList<String > adjacency = this.adjacencyList.get(currentCity);
        int distance = 0;
        for(String x:adjacency){
            if(Integer.parseInt(x.split("\\s+")[0]) == this.sourceCity){
                distance = Integer.parseInt(x.split("\\s+")[1]);
                flag = true;
                break;
            }
        }
        if(flag){
            Random rand = new Random();
            while(true) {
                System.out.print(currentCity+"->"+sourceCity);
                double prob = this.generateSigmoid(distance);
                double randProb = rand.nextDouble();
                System.out.println(", random probability = "+randProb);
                if(randProb<prob){
                    solutioinSpace = solutioinSpace+"--("+distance+")-->"+sourceCity;
                    this.tourDistance = this.tourDistance + distance;
                    break;
                }
            }
        }
        return flag;
    }




    public static void main(String[] args){
        TSP_SimulatedAnnealing tsp = new TSP_SimulatedAnnealing();
        tsp.initializeGraph();
        tsp.setAvgStepCost();
        int currentCity=-1;
        while(!tsp.queue.isEmpty()){
            currentCity = tsp.queue.remove();
            tsp.visitedCities.add(currentCity);
            tsp.solutioinSpace=tsp.solutioinSpace+currentCity;
            if(!tsp.explorePath(currentCity)){
                break;
            }
        }

        if(tsp.visitedCities.size()==tsp.numCities){
            if(tsp.returnToSourceCity(currentCity)) {
                System.out.println("\n\n------------Simulated Annealing Result------------");
                System.out.println(tsp.solutioinSpace);
                System.out.println("TSP Tour Distance = "+tsp.tourDistance);
                return;
            }
        }
        if(tsp.visitedCities.size()<tsp.numCities){
            System.out.println("Reached Local minima");
        }else{
            System.out.println("Graph does not contain Hamiltonian cycle");
        }

    }
}