import java.util.*;

public class TSP_BestFirstSearch {
    int numCities;
    int sourceCity;
    int tourDistance;
    HashSet<Integer> visitedCities = new HashSet<>();
    HashMap<Integer,ArrayList<String>> adjacencyList = new HashMap<>();
    Queue<Integer> queue = new LinkedList<>();


    public void initializeGraph(){
        String input=null;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of cities : ");
        this.numCities = scanner.nextInt();
        System.out.println("Enter source city : range( 0 - "+(numCities-1)+" )");
        this.sourceCity = scanner.nextInt();
        this.queue.add(this.sourceCity);
        this.tourDistance=0;
        System.out.println("Enter the edge Distances as follows(Type 'exit' to finish reading)\n<Source> <Destination> <Distance>");
        input = scanner.nextLine();
        input = scanner.nextLine();

        while(!input.equals("exit")){
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


    public boolean explorePath(int currentCity){
        if(this.adjacencyList.containsKey(currentCity)){
            ArrayList<String > list = this.adjacencyList.get(currentCity);
            int distance=32767;  //assumed infinity
            int nextCity=-1;
            for(String x:list){
                int tempDis = Integer.parseInt(x.split("\\s+")[1]);
                int tempNext = Integer.parseInt(x.split("\\s+")[0]);

                if(!visitedCities.contains(tempNext) && tempDis<distance){
                    distance = tempDis;
                    nextCity = tempNext;
                }
            }
            if(nextCity != -1) {
                this.queue.add(nextCity);
                this.tourDistance = this.tourDistance + distance;
                System.out.print("--("+distance+")--> ");
                return true;
            }
        }
        return false;
    }

    public void returnToSourceCity(int currentCity){
        ArrayList<String > list = this.adjacencyList.get(currentCity);
        for (String x:list){
            if(Integer.parseInt(x.split("\\s+")[0])==this.sourceCity){
                this.tourDistance = this.tourDistance+Integer.parseInt(x.split("\\s+")[1]);
                System.out.println("--("+x.split("\\s+")[1]+")-->"+this.sourceCity);
                break;
            }
        }
    }

    public boolean checkVisitedNodes(){
        int x;
        for (x=0;x<this.numCities;x++){
            if(!this.visitedCities.contains(x)){
                break;
            }
        }
        if(x==this.numCities){
            return true;
        }else {
            return false;
        }
    }

    public static void main(String[] args){
        int currentCity=-1;
        TSP_BestFirstSearch tsp = new TSP_BestFirstSearch();
        tsp.initializeGraph();

        while(!tsp.queue.isEmpty()){
            currentCity = tsp.queue.remove();
            System.out.print(currentCity);
            if(!tsp.visitedCities.contains(currentCity)) {
                tsp.visitedCities.add(currentCity);
                if(!tsp.explorePath(currentCity)){
                    break;
                }
            }
        }
        if(tsp.checkVisitedNodes()){
            tsp.returnToSourceCity(currentCity);
            System.out.println("greedy best first search => tour distance = "+tsp.tourDistance);
        }else{
            System.out.print("end\nTour cannot be continued as it does not have a hamiltonian cycle!!!");
        }
    }
}
