import java.util.*;

public class TSP_HillClimbing {
    int numCities;
    int sourceCity;
    int tourDistance;
    double avgStepCost;
    double currentHeuristics;
    double nextHeuristics;
    String solutionSpace;

    Queue<Integer> queue= new LinkedList<>();
    HashSet<Integer> visitedCities = new HashSet<>();
    HashMap<Integer,ArrayList<String>> adjacencyList = new HashMap<>();

    public void initializeGraph(){
        String input="";
        this.tourDistance=0;
        this.solutionSpace= "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of cities : ");
        this.numCities = scanner.nextInt();
        System.out.println("Enter the Source city : range( 0 -"+(this.numCities-1)+" )");
        this.sourceCity = scanner.nextInt();
        this.queue.add(this.sourceCity);
        System.out.println("Enter the edge Distances as follows(Type 'exit' to finish reading)\n<Source> <Destination> <Distance>");
        input = scanner.nextLine();
        input = scanner.nextLine();
        while (!input.equals("exit")){
            String[] parse = input.split("\\s+");

            int source = Integer.parseInt(parse[0]);
            int destination = Integer.parseInt(parse[1]);
            int distance = Integer.parseInt(parse[2]);


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


    public void setCurrentHeuristics(){
        this.currentHeuristics = this.avgStepCost * this.numCities;
        System.out.println("Initial heuristics = "+this.currentHeuristics);
    }


    public void generateNextHeuristics(int curDis){
        int citiesYetToVisit = (this.numCities - (this.visitedCities.size()));// this 1 is for including the source city
        this.nextHeuristics = (this.tourDistance+curDis) + (citiesYetToVisit * this.avgStepCost);
    }

    public boolean explorePath(int currentCity){
        ArrayList<String> adjacency = this.adjacencyList.get(currentCity);
        int minDis = 32768;
        int nextCity = -1;

        for(String x:adjacency){
            int dest = Integer.parseInt(x.split("\\s+")[0]);
            int distance = Integer.parseInt(x.split("\\s+")[1]);
            if ((!this.visitedCities.contains(dest)) && (distance < minDis)) {
                minDis = distance;
                nextCity = dest;
            }
        }
        if(nextCity!=-1){
            this.generateNextHeuristics(minDis);
            System.out.println("from city "+currentCity+" to city "+nextCity);
            System.out.println("current heuristics = "+this.currentHeuristics+" next heuristics = "+this.nextHeuristics);
            if(this.nextHeuristics < this.currentHeuristics){
                this.solutionSpace = this.solutionSpace +"--("+minDis+")-->";
                this.tourDistance = this.tourDistance + minDis;
                this.queue.add(nextCity);
                this.currentHeuristics = this.nextHeuristics;
                return true;
            }
        }
        return false;
    }


    public boolean returnToSourceCity(int currentCity){
        ArrayList<String> adjacency = this.adjacencyList.get(currentCity);
        boolean flag = false;
        for(String x:adjacency){
            if(Integer.parseInt(x.split("\\s+")[0]) == this.sourceCity){
                int distance = Integer.parseInt(x.split("\\s+")[1]);
                this.generateNextHeuristics(distance);
                System.out.println("from city "+currentCity+" to city "+this.sourceCity);
                System.out.println("current heuristics = "+this.currentHeuristics+" next heuristics = "+this.nextHeuristics);
                if(this.currentHeuristics > this.nextHeuristics){
                    this.tourDistance = this.tourDistance + distance;
                    this.solutionSpace = this.solutionSpace+"--("+distance+")-->"+this.sourceCity;
                    flag=true;
                }
                break;
            }
        }
        return flag;
    }

    public static void main(String[] args){
        TSP_HillClimbing tsp = new TSP_HillClimbing();
        tsp.initializeGraph();
        tsp.setAvgStepCost();
        tsp.setCurrentHeuristics();
        int currentCity=0;

        while (!tsp.queue.isEmpty()){
            currentCity = tsp.queue.remove();
            if(!tsp.visitedCities.contains(currentCity)){
                tsp.solutionSpace = tsp.solutionSpace + currentCity+"--";
                tsp.visitedCities.add(currentCity);
                if(!tsp.explorePath(currentCity)){
                    break;
                }
            }
        }
        if(tsp.visitedCities.size() == tsp.numCities){
            if(tsp.returnToSourceCity(currentCity)){
                System.out.println("\n\n\n"+tsp.solutionSpace);
                System.out.println("\n\nHill Climbing Tour Distance = "+tsp.tourDistance);
                return;
            }
        }
        if(tsp.currentHeuristics < tsp.nextHeuristics){
            System.out.println("Stuck at Local Minima");
        }else{
            System.out.println("Graph Does not contain Hamiltonian cycle");
        }


    }
}