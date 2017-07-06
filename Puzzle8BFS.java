import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Puzzle8BFS {
    static int NUMTILES = 3;
    static final int NEIGHBOURS[][] = {
            {-1,0},
            {1,0 },
            {0,-1},
            {0,1 }
    };

    Queue<String> queue = new LinkedList<>();
    HashSet<String> depictedConfig = new HashSet<>();

    String finalState= "123456780";
    String initialState="";
    Scanner scanner = new Scanner(System.in);


    public void setInitialState(){
        System.out.println("Enter the initial state : ");
        String temp = scanner.nextLine();
        queue.add(temp);
    }

    public boolean checkState(String currentState){

        if(currentState.equals(finalState)){
            return true;
        }else{
            return false;
        }

    }

    public boolean validateSwap(int nullPosition,int swapPosition){
        if(swapPosition>=0 && swapPosition<(NUMTILES*NUMTILES)){
            if((swapPosition/NUMTILES)==(nullPosition/NUMTILES) || (swapPosition%NUMTILES)==(nullPosition%NUMTILES)){
                return true;
            }
        }
        return false;
    }

    public void generateConfig(String currentState){
        int nullPosition=0,swapPosition=0;
        nullPosition = currentState.indexOf('0');

        for(int x[] : NEIGHBOURS){
            swapPosition = (x[0]*NUMTILES) + x[1] + nullPosition;

            if(validateSwap(nullPosition,swapPosition)){
                char[] config = currentState.toCharArray();
                config[nullPosition]=config[swapPosition];
                config[swapPosition] = '0';
                String newConfig = new String(config);

                if(!depictedConfig.contains(newConfig)){
                    queue.add(newConfig);
                    System.out.print("swap ("+nullPosition+" , "+swapPosition+")"+ " -> config : ");
                    printState(newConfig);
                }else{
                    System.out.println("swap ("+nullPosition+" , "+swapPosition+")"+ " -> Already processed");
                }
            }
        }


    }


    public void printState(String current){
        for(int i=0;i<NUMTILES*NUMTILES;i++){
            if(i%NUMTILES == 0){
                System.out.println();
            }
            System.out.print(current.charAt(i));
        }
        System.out.println();
    }

    public static void main(String[] args){
        Puzzle8BFS puzzle8BFS = new Puzzle8BFS();
        puzzle8BFS.setInitialState();
        while(!puzzle8BFS.queue.isEmpty()){
            String current = puzzle8BFS.queue.remove();


            if(puzzle8BFS.checkState(current)){
                System.out.print("\n\n    Puzzle Solved!!!     config :");
                puzzle8BFS.printState(current);
                return;
            }
            if(!puzzle8BFS.depictedConfig.contains(current)){
                System.out.println("current state = "+current+" -> Possible solution :");
                puzzle8BFS.depictedConfig.add(current);
                puzzle8BFS.generateConfig(current);
            }else{
                System.out.println("current state = "+current+" -> Already Solved");
            }

        }
        System.out.println("Puzzle Unsolvable");
    }

}