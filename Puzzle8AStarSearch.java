import java.util.*;

public class Puzzle8AStarSearch {

    int[][] NEIGHBOURS = {
            {-1,0},
            {1,0},
            {0,-1},
            {0,1}
    };



    Comparator<Puzzle> puzzleComparator = new Comparator<Puzzle>() {
        @Override
        public int compare(Puzzle lhs, Puzzle rhs) {
            if(lhs.getAStarHeuristics() < rhs.getAStarHeuristics()){
                return -1;
            }
            if(lhs.getAStarHeuristics() > rhs.getAStarHeuristics()){
                return 1;
            }
            return 0;
        }
    };

    Queue<Puzzle> queue = new PriorityQueue<>(puzzleComparator);
    Set<List<Integer> > processedConfig = new HashSet<>();


    public void getInput(){
        List<Integer> temp = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Puzzle Size 'n' in (nxn) : ");
        Puzzle.NUMTILES = scanner.nextInt();
        System.out.println("enter the current config : ");
        for(int i=0;i<Puzzle.NUMTILES*Puzzle.NUMTILES;i++){
            temp.add(scanner.nextInt());
        }
        Puzzle current = new Puzzle(temp,0,null);
        queue.add(current);
    }

    public int findNullPosition(List<Integer> puzzle){
        int i;
        for(i=0;i<Puzzle.NUMTILES*Puzzle.NUMTILES;i++){
            if(puzzle.get(i)==-1){
                break;
            }
        }

        return i;
    }

    public boolean validateSwap(int nullPosition,int swapPosition){
        if(swapPosition>=0 && swapPosition<(Puzzle.NUMTILES*Puzzle.NUMTILES)) {

            if (
                    (nullPosition / Puzzle.NUMTILES == swapPosition / Puzzle.NUMTILES)
                            ||
                    (nullPosition % Puzzle.NUMTILES == swapPosition % Puzzle.NUMTILES)
                ) {
                return true;
            }

        }
        return false;
    }

    public boolean generateConfig(Puzzle current){
        int nullPosition = this.findNullPosition(current.puzzle);
        int swapPosition;

        for (int x[]:NEIGHBOURS){
            swapPosition = nullPosition + (x[0]*Puzzle.NUMTILES) + x[1];

            if(this.validateSwap(nullPosition,swapPosition)){
                List<Integer> currentState = new ArrayList<>(current.puzzle);
                currentState.set(nullPosition,currentState.get(swapPosition));
                currentState.set(swapPosition,-1);
                if(!processedConfig.contains(currentState)){
                    Puzzle newConfig = new Puzzle(currentState,current.stepCost+1,current);
                    this.queue.add(newConfig);
                }
            }

        }
        return true;
    }


    public void printPuzzleConfig(List<Integer> puzzle){
        for(int i=0;i<Puzzle.NUMTILES;i++){
            for (int j=0;j<Puzzle.NUMTILES;j++){
                System.out.print(puzzle.get((i*Puzzle.NUMTILES)+j)+" ");
            }
            System.out.println();
        }
        System.out.println("\n");
    }


    public void printStateSpace(Puzzle current){
        List<Puzzle> solutionSpace = new ArrayList<>();

        while (current.previousBoard != null){
            solutionSpace.add(current);
            current = current.previousBoard;
        }
        solutionSpace.add(current);
        Collections.reverse(solutionSpace);
        for (Puzzle x:solutionSpace){
            System.out.println("Step cost = "+x.stepCost+" Manhattan = "+x.manhattan);
            this.printPuzzleConfig(x.puzzle);
        }


    }




    public static void main(String[] args){
        Puzzle current=null;
        Puzzle8AStarSearch game = new Puzzle8AStarSearch();

        game.getInput();

        while (!game.queue.isEmpty()){
            current = game.queue.remove();
            game.processedConfig.add(current.puzzle);

            if(current.getManhattan() == 0){
                break;
            }

            game.generateConfig(current);
        }


        System.out.println("\n\n----------------A*  Solution----------------\n\n");
        game.printStateSpace(current);

    }

}



class Puzzle{
    static int NUMTILES;

    List<Integer> puzzle = new ArrayList<>();
    Puzzle previousBoard = null;

    int stepCost;
    int manhattan;


    public Puzzle(List<Integer> puzzle,int stepCost,Puzzle previousBoard){
        this.puzzle = puzzle;
        this.stepCost = stepCost;
        this.previousBoard = previousBoard;
        this.manhattan = computeManhattan(puzzle);
    }


    public int getManhattan(){
        return manhattan;
    }

    public int getAStarHeuristics(){
        return (this.stepCost + this.manhattan);
    }


    private int computeManhattan(List<Integer> puzzle){
        int manhattan = 0;
        for(int i=0;i<(Puzzle.NUMTILES*Puzzle.NUMTILES);i++){

            if(puzzle.get(i)!=i && puzzle.get(i)!=-1){
                int rowOffset = Math.abs((i/Puzzle.NUMTILES) - (puzzle.get(i)/Puzzle.NUMTILES));
                int colOffset = Math.abs((i%Puzzle.NUMTILES) - (puzzle.get(i)%Puzzle.NUMTILES));
                manhattan = manhattan + rowOffset + colOffset;
            }

        }
        return manhattan;
    }


}