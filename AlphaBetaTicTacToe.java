import java.util.*;


class PuzzleBoard{
    public static int NUMTILES;
    public static int COMPUTER_SIGN;
    public static int USER_SIGN;

    private int[][] puzzle;

    public PuzzleBoard(int[][] puzzle){
        this.puzzle = puzzle;

    }

    public boolean isPlayerWon(int[][] puzzle, int userSign,boolean printResult){

        if(rowWiseMiniMax(puzzle,userSign,true) == -1
           || columnWiseMiniMax(puzzle,userSign,true) == -1
           || diagonalWiseMiniMax(puzzle,userSign,true) == -1
        ){


            if(userSign == 1 && printResult == true){
                System.out.println("-----------------Game Ended-----------------");
                System.out.println("Player 'X' won the game!!!");
            }else if(userSign == -1 && printResult == true){
                System.out.println("-----------------Game Ended-----------------");
                System.out.println("Player 'O' won the game!!!");
            }
            return true;
        }
        return false;
    }

    public boolean isMovesPossible(int[][] puzzle) {
        for (int i = 0; i < PuzzleBoard.NUMTILES; i++) {
            for (int j = 0; j < PuzzleBoard.NUMTILES; j++) {
                if(puzzle[i][j] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean setUserMove(int x,int y){
        if(this.puzzle[x][y] == 0 && x<PuzzleBoard.NUMTILES && y<PuzzleBoard.NUMTILES){
            this.puzzle[x][y] = PuzzleBoard.USER_SIGN;
            return true;
        }
        System.out.println("Enter a valid move");
        System.exit(0);
        return false;
    }

    public int rowWiseMiniMax(int[][] puzzle,int userSign,boolean winCheck){
        int value = 0;
        int i=0,j=0;
        for(i=0;i<PuzzleBoard.NUMTILES;i++){

            for(j=0;j<PuzzleBoard.NUMTILES;j++){
                if(puzzle[i][j] != userSign && (puzzle[i][j]!=0||winCheck)){
                    break;
                }
            }
            if(j==PuzzleBoard.NUMTILES){
                value = value + 1;
            }
            if(winCheck && value>0){
                return -1;//indicating it is a win for the userSign
            }
        }
        return value;
    }



    public int columnWiseMiniMax(int[][] puzzle,int userSign,boolean winCheck){
        int value = 0;
        int i=0,j=0;
        for(j=0;j<PuzzleBoard.NUMTILES;j++){

            for(i=0;i<PuzzleBoard.NUMTILES;i++){
                if(puzzle[i][j] != userSign && (puzzle[i][j]!=0||winCheck)){
                    break;
                }
            }
            if(i==PuzzleBoard.NUMTILES){
                value = value + 1;
            }
            if(winCheck && value>0){
                return -1;//indicating it is a win for the userSign
            }

        }
        return value;
    }

    public int diagonalWiseMiniMax(int[][] puzzle,int userSign,boolean winCheck){
        int value = 0;
        int i=0,j=0;

        //forward diagonal
        for(i=0;i<PuzzleBoard.NUMTILES;i++) {
            if (puzzle[i][i] != userSign && (puzzle[i][i]!=0||winCheck)) {
                break;
            }
        }
        if(i==PuzzleBoard.NUMTILES){
            value = value + 1;
        }

        //backward diagonal
        for (i=0;i<PuzzleBoard.NUMTILES;i++){
            if(puzzle[i][PuzzleBoard.NUMTILES-1-i] != userSign && (puzzle[i][PuzzleBoard.NUMTILES-1-i] != 0||winCheck)){
                break;
            }
        }
        if(i==PuzzleBoard.NUMTILES){
            value = value + 1;
        }

        if(winCheck && value>0){
            return -1;//indicating it is a win for the userSign
        }


        return value;
    }

    public int findMiniMaxValue(int[][] puzzle){
        int miniMaxValue = ( rowWiseMiniMax(puzzle,COMPUTER_SIGN,false)
                + columnWiseMiniMax(puzzle,COMPUTER_SIGN,false)
                + diagonalWiseMiniMax(puzzle,COMPUTER_SIGN,false) )
                                -
                (rowWiseMiniMax(puzzle,USER_SIGN,false)
                + columnWiseMiniMax(puzzle,USER_SIGN,false)
                + diagonalWiseMiniMax(puzzle,USER_SIGN,false))
        ;

        return miniMaxValue;
    }


    public int getBoardValue(){
        return findMiniMaxValue(this.puzzle);
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public void printPuzzle(){
        for (int i=0;i<PuzzleBoard.NUMTILES;i++){
            for (int j=0;j<PuzzleBoard.NUMTILES;j++){
                if(puzzle[i][j]==1)
                    System.out.print("X  ");
                else if(puzzle[i][j] == -1)
                    System.out.print("O  ");
                else
                    System.out.print("-  ");
            }
            System.out.println();
        }
    }
}


class GameTree{
    public static int alpha;
    public static int beta;
    public static boolean overRidePruningValue;
    private static PuzzleBoard alphaNode;
    private static PuzzleBoard currentRootNode;

    Queue<PuzzleBoard> maxLevel = new LinkedList<>();


    public int[][] copyArray(int[][] src){
        int[][] result = new int[PuzzleBoard.NUMTILES][PuzzleBoard.NUMTILES];

        for(int i=0;i<PuzzleBoard.NUMTILES;i++){
            for(int j=0;j<PuzzleBoard.NUMTILES;j++){
                result[i][j] = src[i][j];
            }
        }
        return result;
    }


    public void overRidePruning(int i,int j){
        int[][] config = copyArray( GameTree.currentRootNode.getPuzzle());
        config[i][j] = PuzzleBoard.COMPUTER_SIGN;
        PuzzleBoard temp = new PuzzleBoard(config);
        GameTree.alphaNode = temp;
        return;
    }


    public boolean generatePossibleMaxMoves(PuzzleBoard puzzleBoard){
        boolean flag = false;
        for(int i=0;i<PuzzleBoard.NUMTILES;i++){
            for (int j=0;j<PuzzleBoard.NUMTILES;j++){
                if(puzzleBoard.getPuzzle()[i][j]==0){
                    flag = true;
                    int[][] currentPuzzle = copyArray(puzzleBoard.getPuzzle());
                    currentPuzzle[i][j] = PuzzleBoard.COMPUTER_SIGN;
                    PuzzleBoard newconfig = new PuzzleBoard(currentPuzzle);
                    maxLevel.add(newconfig);
                }
            }
        }
        return flag;
    }


    public boolean generatePossibleMinMove(PuzzleBoard currentBoard,int parentNumber){
        int childNumber = 0;
        GameTree.beta = 100;//assumed infinity

        if(parentNumber == 1){
            GameTree.alpha = -100; //assumed infinity
        }


        PuzzleBoard newconfig=null;
        for(int i=0;i<PuzzleBoard.NUMTILES;i++){
            for (int j=0;j<PuzzleBoard.NUMTILES;j++) {

                if (currentBoard.getPuzzle()[i][j] == 0) {
                    int[][] currentPuzzle = copyArray(currentBoard.getPuzzle());
                    currentPuzzle[i][j] = PuzzleBoard.USER_SIGN;
                    newconfig = new PuzzleBoard(currentPuzzle);
                    childNumber++;
                }
                if(childNumber>0 && newconfig.isPlayerWon(newconfig.getPuzzle(),PuzzleBoard.USER_SIGN,false)){
                    int[][] currentPuzzle = copyArray(currentBoard.getPuzzle());
                    GameTree.alphaNode = new PuzzleBoard(currentPuzzle);
                    overRidePruning(i,j);
                    GameTree.overRidePruningValue = true;
                    return true;
                }

                if(childNumber>0 && GameTree.beta > newconfig.getBoardValue()){
                    GameTree.beta = newconfig.getBoardValue();
                    //System.out.println("new beta = "+GameTree.beta+" child = "+childNumber+" parent = "+parentNumber);
                    //newconfig.printPuzzle();
                }

                if(parentNumber!=1 && GameTree.beta<=GameTree.alpha){
                    //System.out.println("alpha cut-off");
                    return false;  //alpha cut-off
                }
            }
        }
        if(parentNumber == 1){
            GameTree.alpha = GameTree.beta;
            GameTree.alphaNode = currentBoard;
        }else if (GameTree.beta >= GameTree.alpha){
            GameTree.alpha = GameTree.beta;
            GameTree.alphaNode = currentBoard;
        }

        return true;//no alpha cut-off
    }

    public PuzzleBoard exploreTree(PuzzleBoard currentRoot){
        GameTree.alphaNode = null;
        GameTree.overRidePruningValue=false;
        GameTree.currentRootNode = currentRoot;
        generatePossibleMaxMoves(currentRoot);
        int configNumber = 0;
        while (!maxLevel.isEmpty()){
            configNumber++;
            PuzzleBoard currentConfig = maxLevel.remove();
            if(currentConfig.isPlayerWon(currentConfig.getPuzzle(),PuzzleBoard.COMPUTER_SIGN,false))
                return currentConfig;

            if(!GameTree.overRidePruningValue) {
                generatePossibleMinMove(currentConfig, configNumber);
            }
        }
        return GameTree.alphaNode;
    }

}



public class AlphaBetaTicTacToe {


    public static void setUserSigns(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose Player : \n1>'X'\n2>'O'");
        int choice = scanner.nextInt();
        if(choice == 1){
            PuzzleBoard.COMPUTER_SIGN = -1;
            PuzzleBoard.USER_SIGN = 1;
        }else if(choice == 2){
            PuzzleBoard.COMPUTER_SIGN = 1;
            PuzzleBoard.USER_SIGN = -1;
        }else {
            System.out.println("Invalid Choice!!!");
            System.exit(0);
        }

    }


    public static void main(String[] args){
        GameTree gameTree = new GameTree();
        PuzzleBoard currentConfig = null;
        PuzzleBoard.NUMTILES = 3;
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        boolean isComputerMove = random.nextBoolean();
        currentConfig = new PuzzleBoard(new int[PuzzleBoard.NUMTILES][PuzzleBoard.NUMTILES]);
        setUserSigns();

        while (!currentConfig.isPlayerWon(currentConfig.getPuzzle(),PuzzleBoard.USER_SIGN,true)
                &&
                !currentConfig.isPlayerWon(currentConfig.getPuzzle(),PuzzleBoard.COMPUTER_SIGN,true)
                &&
                currentConfig.isMovesPossible(currentConfig.getPuzzle())
         ){
            if(isComputerMove) {
                currentConfig = gameTree.exploreTree(currentConfig);
                System.out.println("Computer's Move :");
                currentConfig.printPuzzle();
                isComputerMove = false;
            }else {
                System.out.print("Choose Your Move ( x  y ) : ");
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                currentConfig.setUserMove(x, y);
                System.out.println("Your Move :");
                currentConfig.printPuzzle();
                isComputerMove = true;
            }
        }

        if(!currentConfig.isMovesPossible(currentConfig.getPuzzle())){
            System.out.println("\n\n---------Game Draw!!!----------------");

        }

    }
}

