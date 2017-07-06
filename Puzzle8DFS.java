import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Stack;


public class Puzzle8DFS {
    Stack stack = new Stack();
    HashSet<String> depictedConfig = new HashSet<>();

    static final int NUMTILES = 3;
    static final int NEIGHBOURS[][] = {
            {-1,0},
            {1,0},
            {0,-1},
            {0,1}
    };

    String finalState = "123456780";
    String initialState;

    Scanner scanner = new Scanner(System.in);

    public void setInitialState(){
        System.out.print("Enter initial configuration : ");
        initialState = scanner.nextLine();
        stack.push(initialState);
    }

    public boolean checkGoal(String current){
        if(current.equals(finalState)){
            return true;
        }
        return false;
    }

    public boolean validateSwap(int nullPosition, int swapPosition){
        if(swapPosition >=0 && swapPosition<(NUMTILES*NUMTILES)){
            if((nullPosition/NUMTILES == swapPosition/NUMTILES)||(nullPosition%NUMTILES == swapPosition%NUMTILES))
                return true;
        }
        return false;
    }

    public String generateConfig(String currentConfig){
        int nullPosition, swapPosition;
        String newConfig = null;

        nullPosition = currentConfig.indexOf('0');

        for(int x[] : NEIGHBOURS){
            swapPosition = (x[0]*NUMTILES) + x[1]+nullPosition;

            if(validateSwap(nullPosition,swapPosition)){
                char [] config = currentConfig.toCharArray();
                config[nullPosition] = config[swapPosition];
                config[swapPosition] = '0';

                if(!depictedConfig.contains(new String(config))){
                    newConfig = new String(config);

                    System.out.print("swap("+nullPosition+", "+swapPosition+" )   config:");
                    printState(newConfig);
                    break;
                }
            }
        }
        return newConfig;
    }

    public void printState(String current){
        for(int i=0;i<(NUMTILES*NUMTILES);i++){
            if(i%NUMTILES == 0){
                System.out.println();
            }
            System.out.print(current.charAt(i));
        }
        System.out.println();
    }


    public static void main(String[] args){
        Puzzle8DFS puzzle = new Puzzle8DFS();
        puzzle.setInitialState();
        String current = null;
        while (!puzzle.stack.isEmpty()) {
            current = (String) puzzle.stack.peek();
            puzzle.depictedConfig.add(current);

            if(puzzle.checkGoal(current)){
                System.out.println("\n\n      Puzzle Solved!!!    config :");
                puzzle.printState(current);
                return;
            }

            System.out.print("current config : ");
            puzzle.printState(current);

            String newConfig = puzzle.generateConfig(current);
            if(newConfig == null){
                System.out.println("All possiblities are already processed");
                puzzle.stack.pop();
                continue;
            }

            puzzle.stack.push(newConfig);
        }
        System.out.println("Puzzle unsolvable !!!");

    }

}

