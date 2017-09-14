import java.util.*;
import java.util.function.Supplier;

public class TSP_AStarSearch{
    int numCities;
    double tourDistance;
    int sourceCity;
    double avgStepCost;
    String solutionSpace;


    Comparator<City> queueComparator = new Comparator<City>() {
        @Override
        public int compare(City o1, City o2) {
            double currentHeuristics = o1.getHeuristicFn();
            double prevHeuristics = o2.getHeuristicFn();
            if(currentHeuristics < prevHeuristics){
                return -1;
            }
            if(currentHeuristics>prevHeuristics){
                return 1;
            }
            return 0;
        }
    };


    HashSet<Integer> visitedCities = new HashSet<>();
    HashMap<Integer,ArrayList<String>> adjacencyList = new HashMap<>();
    PriorityQueue<City> queue = new PriorityQueue<>(queueComparator);

    public void initializeGraph(){
        String input="";
        this.tourDistance=0;
        this.solutionSpace= "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of cities : ");
        this.numCities = scanner.nextInt();
        System.out.println("Enter the Source city : range( 0 -"+(this.numCities-1)+" )");
        this.sourceCity = scanner.nextInt();
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


    public void initializeQueue(){
        City source = new City(null,sourceCity,0,0,(numCities*avgStepCost));
        this.queue.add(source);
    }

    public double generatePriority(City current,int distance){
        double fn = (current.getGn() + distance) + ((this.numCities - current.getCitiesCovered())*this.avgStepCost);
        return fn;
    }



    public void exploreGraph(City current){
        boolean flag = false;
        int currentCity = current.getCurrentCity();
        double Gn = current.getGn();
        int citiesCovered = current.getCitiesCovered() + 1;
        double Hn = (this.numCities - citiesCovered)*this.avgStepCost;
        ArrayList<String> adjacency = this.adjacencyList.get(currentCity);

        for(String x:adjacency){
            String temp[] = x.split("\\s+");
            int destination = Integer.parseInt(temp[0]);
            if(!this.visitedCities.contains(destination)) {
                int distance = Integer.parseInt(temp[1]);
                City city = new City(current, destination, citiesCovered, (Gn + distance), Hn);
                this.queue.add(city);
            }
        }
    }

    public void returnToSourceCity(City current){
        double Gn = current.getGn();
        this.tourDistance = Gn;
        int currentCity = current.getCurrentCity();
        ArrayList<String> adjacency = this.adjacencyList.get(currentCity);
        for(String x:adjacency){
            String[] temp = x.split("\\s+");
            if(Integer.parseInt(temp[0]) == this.sourceCity){
                this.tourDistance = this.tourDistance + Integer.parseInt(temp[1]);
                break;
            }
        }
        ArrayList<Integer> cityPath = new ArrayList<>();
        City temp = current;
        while(temp!=null){
            cityPath.add(temp.getCurrentCity());
            System.out.println(temp.getCurrentCity());
            temp = temp.getPreviousCity();
        }
        Collections.reverse(cityPath);

        System.out.print("---------------A* result------------\n\nTour Path =>  ");
        for (Integer x:cityPath){
            System.out.print(x+"-->");
        }
        System.out.println(sourceCity);
        System.out.println("\nTour Distance = "+this.tourDistance);


    }


    public static void main(String[] args){
        TSP_AStarSearch tsp = new TSP_AStarSearch();
        City current;
        tsp.initializeGraph();
        tsp.setAvgStepCost();
        tsp.initializeQueue();

        while(!tsp.queue.isEmpty()){
            current = tsp.queue.remove();
            tsp.visitedCities.add(current.getCurrentCity());
            //System.out.print(current.getCurrentCity()+"--");
            if(current.getCitiesCovered() == tsp.numCities-1){
                tsp.returnToSourceCity(current);
                break;
            }
            tsp.exploreGraph(current);
        }
    }
}

class City{
    City previousCity;
    int currentCity;
    int citiesCovered;
    double Fn;
    double Gn;
    double Hn;
    public City(City parent,int city,int citiesCovered,double Gn,double Hn){
        this.previousCity = parent;
        this.currentCity = city;
        this.citiesCovered = citiesCovered;
        this.Gn = Gn;
        this.Hn =Hn;
        this.Fn = this.Gn + this.Hn;
    }
    public int getCurrentCity(){
        return currentCity;
    }
    public City getPreviousCity(){
        return previousCity;
    }

    public int getCitiesCovered() {
        return citiesCovered;
    }

    public double getGn(){
        return Gn;
    }

    public double getHn() {
        return Hn;
    }

    public double getHeuristicFn(){
        return Fn;
    }

}