package ctf.agent;

import java.util.Random;
import java.lang.Integer;
import java.lang.System;
import java.util.ArrayList;
import java.lang.Math;
import ctf.common.AgentAction;
import ctf.common.AgentEnvironment;
import ctf.agent.Agent;
import ctf.common.AgentAction;


public class Tharpoon5 extends Agent {

    enum obstacle {NONE, NORTH_WALL, EAST_WALL, SOUTH_WALL, WEST_WALL}

    private class Agent {
        public Integer number;
        public char name;
        public ArrayList<objective> objectives;
        public ArrayList<direction> directionsToGo;
        public ArrayList<obstacle> obstacles;
        public Coordinate spawn;
        public Coordinate position;
        public ArrayList<Coordinate> squaresTravelled;
        public ArrayList<Integer> movesMade;
        public boolean hasFlag;

        public Agent() {
            number = -1;
            objectives = new ArrayList<objective>();
            pushObjective(objective.INITIALIZE);
            directionsToGo = new ArrayList<direction>();
            pushDirectionToGo(direction.DONOTHING);
            obstacles = new ArrayList<obstacle>();
            ////

            
            spawn = new Coordinate();
            position = new Coordinate();
            squaresTravelled = new ArrayList<Coordinate>();
            movesMade = new ArrayList<Integer>();
            hasFlag = false;
        }

        public int getLastMoveMade() {
            return movesMade.get(movesMade.size() - 1);
        }

        public int getXMovesMadeBack(int x){
            return movesMade.get((movesMade.size() - 1) - x);
        }

        public int getLastNonDirMove(direction dir){
            int moveNumber = 1;
            int move = getAgentActionFromDirection(dir);
            while(getXMovesMadeBack(moveNumber) == move){
                moveNumber++;
            }
            return getXMovesMadeBack(moveNumber);
        }

        public int getLastNonDirMove(direction firstDir, direction secondDir){
            int moveNumber = 1;
            int firstMove = getAgentActionFromDirection(firstDir);
            int secondMove = getAgentActionFromDirection(secondDir);
            while(getXMovesMadeBack(moveNumber) == firstMove || getXMovesMadeBack(moveNumber) == secondMove){
                moveNumber++;
            }
            return getXMovesMadeBack(moveNumber);
        }

        public Agent pushMoveMade(int newMove) {
            movesMade.add(newMove);
            return this;
        }

        public Agent popMoveMade() {
            movesMade.remove(movesMade.size() - 1);
            return this;
        }

        public String getCurrentObstacleString(){

            String obstacleString = "";

            switch(getCurrentObstacle()){
                case NONE:
                    obstacleString = "none";
                    break;
                case NORTH_WALL:
                    obstacleString = "NORTH_WALL";
                    break;
                case EAST_WALL:
                    obstacleString = "EAST_WALL";
                    break;
                case SOUTH_WALL:
                    obstacleString = "SOUTH_WALL";
                    break;
                case WEST_WALL:
                    obstacleString = "WEST_WALL";
                    break;
            }
            return obstacleString;
        }

        public obstacle getCurrentObstacle() {
            return obstacles.get(obstacles.size() - 1);
        }

        public Agent setNewCurrentObjective(obstacle newObstacle) {
            popObstacle();
            pushObstacle(newObstacle);
            return this;
        }

        public Agent setNewBaseObstacle(obstacle newObstacle){
            obstacles.set(0, newObstacle);
            return this;
        }

        public obstacle getBaseObstacle(){
            return obstacles.get(0);
        }

        public Agent pushObstacle(obstacle newObstacle) {
            obstacles.add(newObstacle);
            return this;
        }

        public Agent popObstacle() {
            obstacles.remove(obstacles.size() - 1);
            return this;
        }

        public objective getXObstaclesBack(int x) {
            return objectives.get((objectives.size() - 1) - x);
        }

        public String getCurrentObjectiveString(){

            String objectiveString = "";

            switch(getCurrentObjective()){
                case INITIALIZE:
                    objectiveString = "initialize";
                    break;
                case SEEK_ENEMY_BASE:
                    objectiveString = "SEEK ENEMY BASE";
                    break;
                case SEEK_OUR_BASE:
                    objectiveString = "SEEK OUR BASE";
                    break;
                case RETRIEVE_FRIENDLY_FLAG:
                    objectiveString = "SEEK FRIENDLY FLAG";
                    break;
                case DEFEND_OUR_BASE:
                    objectiveString = "DEFEND OUR BASE";
                    break;
                case MOVE_AROUND_OBSTACLE:
                    objectiveString = "MOVE AROUND OBSTACLE";
                    break;
            }
            return objectiveString;
        }

        public objective getCurrentObjective() {
            return objectives.get(objectives.size() - 1);
        }

        public Agent setNewCurrentObjective(objective newObjective) {
            popObjective();
            pushObjective(newObjective);
            return this;
        }

        public Agent setNewBaseObjective(objective newObjective){
            objectives.set(0, newObjective);
            return this;
        }

        public objective getBaseObjective(){
            return objectives.get(0);
        }

        public Agent pushObjective(objective newObjective) {
            objectives.add(newObjective);
            return this;
        }

        public Agent popObjective() {
            objectives.remove(objectives.size() - 1);
            return this;
        }

        public objective getXObjectivesBack(int x) {
            return objectives.get((objectives.size() - 1) - x);
        }

        public direction getCurrentDirectionToGo() {
            return directionsToGo.get(directionsToGo.size() - 1);
        }

        public Agent setNewCurrentDirection(direction newDirection) {
            popDirectionToGo();
            pushDirectionToGo(newDirection);
            return this;
        }

        public Agent setNewBaseDirection(direction newDirection){
            directionsToGo.set(0, newDirection);
            return this;
        }

        public direction getBaseDirection(){
            return directionsToGo.get(0);
        }

        public Agent pushDirectionToGo(direction newDirection) {
            directionsToGo.add(newDirection);
            return this;
        }

        public Agent popDirectionToGo() {
            directionsToGo.remove(directionsToGo.size() - 1);
            return this;
        }

        public direction getXDirectionsBack(int x) {
            return directionsToGo.get((directionsToGo.size() - 1) - x);
        }

        public Coordinate oneNorth() {
            return position.oneNorth();
        }

        public Coordinate oneEast() {
            return position.oneEast();
        }

        public Coordinate oneSouth() {
            return position.oneSouth();
        }

        public Coordinate oneWest() {
            return position.oneWest();
        }
    }

    private class Place {
        String name;
        direction dir;
        Coordinate position;

        Place(String name) {
            this.name = name;
            position = new Coordinate();
        }
    }

    private class Coordinate{
        public Integer x;
        public Integer y;

        public Coordinate() {
        }

        public Coordinate(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public Coordinate(Coordinate otherCoordinate){
            this.x = otherCoordinate.x;
            this.y = otherCoordinate.y;
        }

        public void set(Integer x, Integer y) {
            this.x = x;
            this.y = y;
        }

        public void set(Coordinate otherCoordinate) {
            this.x = otherCoordinate.x;
            this.y = otherCoordinate.y;
        }

        public String toString() {
            return "(" + x + ", " + y + ")";
        }

        public boolean isEqualTo(Coordinate otherCoordinate){
            return ((this.x == otherCoordinate.x) && (this.y == otherCoordinate.y));
        }

        public Coordinate oneNorth(){
            return new Coordinate(this.x, this.y - 1);
        }

        public Coordinate oneSouth(){
            return new Coordinate(x, y + 1);
        }

        public Coordinate oneEast(){
            return new Coordinate(x + 1, y);
        }

        public Coordinate oneWest(){
            return new Coordinate(x - 1, y);
        }
    }

    //Agents
    private final static int MAX_NUM_AGENTS = 2;
    private static ArrayList<Agent> agents = new ArrayList<Agent>();
    private Agent me;
    private Agent otherAgent;

    private enum objective {INITIALIZE, SEEK_ENEMY_BASE, SEEK_OUR_BASE, RETRIEVE_FRIENDLY_FLAG, DEFEND_OUR_BASE, MOVE_AROUND_OBSTACLE}


    //Map
    private static char[][] map;
    private int[][] timesVisited;
    private int mapHeight = 10;         //it is known that mapHeight == mapLength
    Place ourBase;
    Place enemyBase;
    Place defenseWallCenter;
    ArrayList<Place> pointsOfInterest = new ArrayList<Place>();
    ArrayList<Coordinate> possibleMines = new ArrayList<Coordinate>();

    private enum direction {NORTH, EAST, SOUTH, WEST, SOUTHWEST, SOUTHEAST, NORTHEAST, NORTHWEST, DONOTHING}

    //Strategy
    boolean oneDefenderOneOffender = false;


    //Environment Variables
    private boolean obstNorth;
    private boolean obstSouth;
    private boolean obstEast;
    private boolean obstWest;
    private boolean obstNorthEast;
    private boolean obstSouthEast;
    private boolean obstNorthWest;
    private boolean obstSouthWest;
    private boolean obstNorthEastSouth;
    private boolean obstEastSouthWest;
    private boolean obstSouthWestNorth;
    private boolean obstWestNorthEast;
    private boolean obstEveryDirection;

    private boolean ourBaseNorth;
    private boolean ourBaseSouth;
    private boolean ourBaseWest;
    private boolean ourBaseEast;

    private boolean ourBaseNorthImmediate;
    private boolean ourBaseSouthImmediate;
    private boolean ourBaseWestImmediate;
    private boolean ourBaseEastImmediate;

    private boolean enemyBaseNorth;
    private boolean enemyBaseSouth;
    private boolean enemyBaseEast;
    private boolean enemyBaseWest;

    private boolean enemyBaseNorthImmediate;
    private boolean enemyBaseSouthImmediate;
    private boolean enemyBaseEastImmediate;
    private boolean enemyBaseWestImmediate;

    private boolean enemyNorthImmediate;
    private boolean enemyEastImmediate;
    private boolean enemySouthImmediate;
    private boolean enemyWestImmediate;

    private boolean enemyHasOurFlag;

    private boolean friendlyNorthImmediate;
    private boolean friendlyEastImmediate;
    private boolean friendlySouthImmediate;
    private boolean friendlyWestImmediate;

    private int moveNorth = AgentAction.MOVE_NORTH;
    private int moveEast = AgentAction.MOVE_EAST;
    private int moveSouth = AgentAction.MOVE_SOUTH;
    private int moveWest = AgentAction.MOVE_WEST;
    private int doNothing = AgentAction.DO_NOTHING;
    private int plantMine = AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE;

    Random rand;


    public Tharpoon5() {

        //Agent
        if (agents.size() == MAX_NUM_AGENTS) {
            agents.clear();
        }
        me = new Agent();
        agents.add(me);
        me.number = agents.size() - 1;
        me.name = (char) ('a' + me.number);
        System.out.println("added agent " + me.name + me.number);

        //Map
        makeEmptyMap();
        makeEmptyTimesVisited();
        ourBase = new Place("ourBase");
        enemyBase = new Place("enemyBase");
        defenseWallCenter = new Place("defenseWallCenter");
        pointsOfInterest.add(ourBase);
        pointsOfInterest.add(enemyBase);
        pointsOfInterest.add(defenseWallCenter);
    }

    public int getMove(AgentEnvironment inEnvironment) {

        int whatToDo = doNothing;

        //init
        queryEnvironment(inEnvironment);

        //some initilizations before we start seeking
        if (me.getCurrentObjective() == objective.INITIALIZE) {
            determineMySpawnAndInitialPosition();
            if (me.number == agents.size() - 1) {
                determineBasePositions();
            }
        }

        amIAtSpawn();
        addSurroundingObstaclesToMap(inEnvironment);
        if (me.hasFlag) {
            me.setNewBaseObjective(objective.SEEK_OUR_BASE);
        }else{
            me.setNewBaseObjective(objective.SEEK_ENEMY_BASE);
        }

        switch (me.getCurrentObjective()) {
            case SEEK_ENEMY_BASE:
                whatToDo = seek(enemyBase);
                break;
            case SEEK_OUR_BASE:
                whatToDo = seek(ourBase);
                break;
            case DEFEND_OUR_BASE:
                whatToDo = defend(ourBase);
                break;
            case MOVE_AROUND_OBSTACLE:
                if(me.getBaseObjective() == objective.SEEK_OUR_BASE){
                    whatToDo = goAroundObstacle(ourBase);
                }else if(me.getBaseObjective() == objective.SEEK_ENEMY_BASE){
                    whatToDo = goAroundObstacle(enemyBase);
                }
        }

        //print only after every complete move (ie, both agents have moved)
        if (me.number == 1) {
            printMap();
        }

        return whatToDo;

    }

    public int defend(Place place){
        determineDefenseWallCenterPositionAndDirection();

        int whatToDo = doNothing;

        if(inDefendingWallArea()){
            System.out.println("create wall");
            whatToDo = createDefendingWall();
        }else{
            System.out.println("seek wall center which is " + getDirectionName(defenseWallCenter.dir));
            whatToDo = seek(defenseWallCenter);
        }

        return whatToDo;
    }

    public boolean inDefendingWallArea(){
        return (me.position.isEqualTo(defenseWallCenter.position) ||
                me.position.isEqualTo(defenseWallCenter.position.oneNorth()) ||
                me.position.isEqualTo(defenseWallCenter.position.oneSouth()));
    }

    public int createDefendingWall(){

        boolean mineNorth = possibleMines.contains(me.position.oneNorth());
        boolean mineSouth = possibleMines.contains(me.position.oneSouth());
        boolean mineEast = possibleMines.contains(me.position.oneEast());
        boolean mineWest = possibleMines.contains(me.position.oneWest());
        boolean onMine = possibleMines.contains(me.position);

        boolean oneNorth = me.position.isEqualTo(defenseWallCenter.position.oneNorth());
        boolean oneSouth = me.position.isEqualTo(defenseWallCenter.position.oneSouth());
        boolean onCenter = me.position.isEqualTo(defenseWallCenter.position);

        int whatToDo = doNothing;

        //if I am on the wall center and there's not a mine or an obstacle south, move south
        if(onCenter && !mineSouth && !obstSouth) {
            whatToDo = moveSouth();

            //If I am one south of the center and I am not standing on a mine, plant one
        }else if(oneSouth && !onMine) {
            whatToDo = plantMine();

            //if I am one south of the center and I am standing on a mine, move north
        }else if(oneSouth && onMine){
            whatToDo = moveNorth();

            //if there is not a mine on defense wall center, put one there
        }else if (onCenter && !onMine){
            whatToDo = plantMine();

            //if I am on the defense wall center and I am standing on a mine, move north
        }else if(me.position.isEqualTo(defenseWallCenter.position) && onMine){
            whatToDo = moveNorth();

            //if I am one north of the defense wall center and I am not standing on a mine, plant one
        }else if(oneNorth && !onMine){
            whatToDo = plantMine();

            //if I am one north of the defense wall center and I am standing on a mine, seek enemy base because I have built the little wall
        }else if(oneNorth && onMine){
            me.setNewCurrentObjective(objective.SEEK_ENEMY_BASE);
            whatToDo = seek(enemyBase);
        }
        return whatToDo;
    }

    public int seek(Place place) {

        int whatToDo = doNothing;
        System.out.println("agent " + me.name + " has obj " + me.getCurrentObjectiveString());

        if (obstInThreeDirections()) {
            whatToDo = move(getOnlyNonObstDirection());
            return whatToDo;
        }
        if(blockedInThreeDirections()){
            whatToDo = move(getOnlyFreeDirection());
            return whatToDo;
        }

        direction directionToGo = place.dir;

        //if(oscillatingMoveCheck()){
        //    directionToGo = getDirectionFromAgentAction(getXMovesBack(2));
        //}

        //int tagEnemyWithOurFlag = checkForSurroundingEnemyAgentsWithOurFlag();
        //if(tagEnemyWithOurFlag != doNothing){
        //    return tagEnemyWithOurFlag;
        //}

        //if(smallCircleCheck()){
        //    return goRandomOpenDirection();
        //}

        switch (directionToGo) {
            case NORTH:
                if (okayToGoNorth()) {
                    whatToDo = moveNorth();
                }else{
                    me.pushObjective(objective.MOVE_AROUND_OBSTACLE);
                    me.pushObstacle(obstacle.NORTH_WALL);
                    whatToDo = goAroundObstacle(place);
                }
                break;
            case NORTHEAST:
                if(okayToGoNorth() && okayToGoEast()){
                    //whatToDo = preferInOrder(direction.EAST, direction.NORTH);
                    whatToDo = pickRandomlyBetweenAndMove(direction.NORTH, direction.EAST);
                }else if (okayToGoNorth() || okayToGoEast()){
                    direction dir = pickRandomlyBetween(direction.EAST, direction.NORTH);
                    if(okayToGo(dir)) {
                        whatToDo = move(dir);
                    }else{
                        me.pushObjective(objective.MOVE_AROUND_OBSTACLE);
                        me.pushObstacle(getObstacleDirectionFromDirection(dir));
                        whatToDo = goAroundObstacle(place);
                    }
                }
                break;
            case NORTHWEST:
                if(okayToGoNorth() && okayToGoWest()){
                    whatToDo = pickRandomlyBetweenAndMove(direction.NORTH, direction.WEST);
                    //whatToDo = preferInOrder(direction.WEST, direction.NORTH);
                }else if(okayToGoNorth() || okayToGoWest()){
                    direction dir = pickRandomlyBetween(direction.WEST, direction.NORTH);
                    if(okayToGo(dir)) {
                        whatToDo = move(dir);
                    }
                    else{
                        me.pushObjective(objective.MOVE_AROUND_OBSTACLE);
                        me.pushObstacle(getObstacleDirectionFromDirection(dir));
                        whatToDo = goAroundObstacle(place);
                    }
                }
                break;
            case SOUTH:
                if(okayToGoSouth()){
                    whatToDo = moveSouth();
                }else{
                        me.pushObjective(objective.MOVE_AROUND_OBSTACLE);
                        me.pushObstacle(obstacle.SOUTH_WALL);
                        whatToDo = goAroundObstacle(place);
                }
                break;
            case SOUTHEAST:
                if(okayToGoSouth() && okayToGoEast()){
                    whatToDo = pickRandomlyBetweenAndMove(direction.SOUTH, direction.EAST);
                }else if(okayToGoSouth() || okayToGoEast()) {
                    direction dir = pickRandomlyBetween(direction.SOUTH, direction.EAST);
                    if (okayToGo(dir)) {
                        whatToDo = move(dir);
                    } else{
                        me.pushObjective(objective.MOVE_AROUND_OBSTACLE);
                        me.pushObstacle(getObstacleDirectionFromDirection(dir));
                        whatToDo = goAroundObstacle(place);
                    }
                }
                break;
            case SOUTHWEST:
                if(okayToGoSouth() && okayToGoWest()){
                    whatToDo = pickRandomlyBetweenAndMove(direction.SOUTH, direction.WEST);
                }else if(okayToGoSouth() || okayToGoWest()){
                    direction dir = pickRandomlyBetween(direction.SOUTH, direction.WEST);
                    if(okayToGo(dir)) {
                        whatToDo = move(dir);
                    }
                    else{
                        me.pushObjective(objective.MOVE_AROUND_OBSTACLE);
                        me.pushObstacle(getObstacleDirectionFromDirection(dir));
                        whatToDo = goAroundObstacle(place);
                    }
                }
                break;
            case EAST:
                if(okayToGoEast()){
                    whatToDo = moveEast();
                }else{
                    me.pushObjective(objective.MOVE_AROUND_OBSTACLE);
                    me.pushObstacle(obstacle.EAST_WALL);
                    whatToDo = goAroundObstacle(place);
                }
                break;
            case WEST:
                if(okayToGoWest()){
                    whatToDo = moveWest();
                }else{
                    me.pushObjective(objective.MOVE_AROUND_OBSTACLE);
                    me.pushObstacle(obstacle.WEST_WALL);
                    whatToDo = goAroundObstacle(place);
                }
                break;
        }
        System.out.println("agent " + me.name + " " + getNameFromAgentAction(whatToDo) + "to position " + me.position.toString());
        return whatToDo;
    }

    public void determineDirectionToGoAroundWall(Place pointOfInterest){

        obstacle obst = me.getCurrentObstacle();
        int numMovesBack = 1;

        //WALLS

        //EAST WALL//EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL //EAST WALL
        //if obstEast and objective is south or southeast, keep going south until obstSouth or !obstEast
        if(obst == obstacle.EAST_WALL && (pointOfInterest.dir == direction.SOUTH || pointOfInterest.dir == direction.SOUTHEAST)){
            //if(okayToGoSouth()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.SOUTH) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.SOUTH);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.NORTH, direction.WEST);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.NORTH, direction.WEST);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}

        }

        //if obstEast and objective is north or northeast, keep going north until obstNorth or !obstEast
        if(obst == obstacle.EAST_WALL && (pointOfInterest.dir == direction.NORTH || pointOfInterest.dir == direction.NORTHEAST)){
            //if(okayToGoNorth()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.NORTH) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.NORTH);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.SOUTH, direction.WEST);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.SOUTH, direction.WEST);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.NORTH) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}

        }

        //if obstEast and objective is east,
        if(obst == obstacle.EAST_WALL && (pointOfInterest.dir == direction.EAST)){

            numMovesBack = 1;
            while(me.getXMovesMadeBack(numMovesBack) == moveEast){
                numMovesBack++;
            }
            int lastNonEastMove = me.getXMovesMadeBack(numMovesBack);

            System.out.println("agent " + me.name + " last move made was: " + getNameFromAgentAction(lastNonEastMove));

            //if last move was moveNorth keep going north until !obstEast or obstNorth
            if(lastNonEastMove == moveNorth){
                //if(okayToGoNorth()){
                System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.NORTH) + " around " + me.getCurrentObstacleString());
                me.pushDirectionToGo(direction.NORTH);
                return;
                //}else{
                //    direction dir = pickRandomlyBetween(direction.SOUTH, direction.WEST);
                //    while(!okayToGo(dir)){
                //        dir = pickRandomlyBetween(direction.SOUTH, direction.WEST);
                //    }
                //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
                //    me.pushDirectionToGo(dir);
                //    return;
                //}
            }

            //if last move was moveSouth keep going south until !obstEast or obstSouth
            if(lastNonEastMove == moveSouth) {
                //if(okayToGoSouth()){
                System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.SOUTH) + " around " + me.getCurrentObstacleString());
                me.pushDirectionToGo(direction.SOUTH);
                return;
                //}else{
                //    direction dir = pickRandomlyBetween(direction.NORTH, direction.WEST);
                //    while(!okayToGo(dir)){
                //        dir = pickRandomlyBetween(direction.NORTH, direction.WEST);
                //    }
                //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
                //    me.pushDirectionToGo(dir);
                //    return;
                //}
            }
        }

        //if obstEast and objective is west, go west
        if(obst == obstacle.EAST_WALL && (pointOfInterest.dir == direction.WEST || pointOfInterest.dir == direction.SOUTHWEST || pointOfInterest.dir == direction.NORTHWEST)) {
            //if (okayToGoWest()) {
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.WEST) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.WEST);
            return;
            //} else {
            //    direction dir = pickRandomlyBetween(direction.NORTH, direction.SOUTH);
            //    while (!okayToGo(dir)) {
            //        dir = pickRandomlyBetween(direction.NORTH, direction.SOUTH);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }


        //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL //WEST WALL
        //if obstWest and objective is south or southwest, keep going south until obstSouth or !obstWest
        if(obst == obstacle.WEST_WALL && (pointOfInterest.dir == direction.SOUTH || pointOfInterest.dir == direction.SOUTHWEST)){
            //if(okayToGoSouth()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.SOUTH) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.SOUTH);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.NORTH, direction.EAST);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.NORTH, direction.EAST);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }

        //if obstWest and objective is north or northwest, keep going north until obstNorth or !obstWest
        if(obst == obstacle.WEST_WALL && (pointOfInterest.dir == direction.NORTH || pointOfInterest.dir == direction.NORTHWEST)){
            //if(okayToGoNorth()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.NORTH) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.NORTH);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.SOUTH, direction.EAST);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.SOUTH, direction.EAST);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }

        //if obstWest and objective is west,
        if(obst == obstacle.WEST_WALL && (pointOfInterest.dir == direction.WEST)){

            numMovesBack = 1;
            while(me.getXMovesMadeBack(numMovesBack) == moveWest){
                numMovesBack++;
            }
            int lastNonWestMove = me.getXMovesMadeBack(numMovesBack);

            System.out.println("agent " + me.name + " last move made was: " + getNameFromAgentAction(lastNonWestMove));

            //if last move was moveNorth keep going north until !obstWest or obstNorth
            if(lastNonWestMove == moveNorth) {
                //if(okayToGoNorth()){
                System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.NORTH) + " around " + me.getCurrentObstacleString());
                me.pushDirectionToGo(direction.NORTH);
                return;
                //}else{
                //    direction dir = pickRandomlyBetween(direction.SOUTH, direction.EAST);
                //    while(!okayToGo(dir)){
                //        dir = pickRandomlyBetween(direction.SOUTH, direction.EAST);
                //    }
                //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
                //    me.pushDirectionToGo(dir);
                //    return;
                //}
            }

            //if last move was moveSouth keep going south until !obstWest or obstSouth
            if(lastNonWestMove == moveSouth) {
                //if(okayToGoSouth()){
                System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.SOUTH) + " around " + me.getCurrentObstacleString());
                me.pushDirectionToGo(direction.SOUTH);
                return;
                //}else{
                //    direction dir = pickRandomlyBetween(direction.NORTH, direction.EAST);
                //    while(!okayToGo(dir)){
                //        dir = pickRandomlyBetween(direction.NORTH, direction.EAST);
                //    }
                //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
                //    me.pushDirectionToGo(dir);
                //    return;
                //}
            }
        }

        //if obstWest and objective is east, go east
        if(obst == obstacle.WEST_WALL && (pointOfInterest.dir == direction.EAST || pointOfInterest.dir == direction.SOUTHEAST || pointOfInterest.dir == direction.NORTHEAST)){
            //if(okayToGoEast()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.EAST) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.EAST);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.NORTH, direction.SOUTH);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.NORTH, direction.SOUTH);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }

        //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL //NORTH WALL
        //if obstNorth and objective is east or northeast, keep going east until obstEast or !obstNorth
        if(obst == obstacle.NORTH_WALL && (pointOfInterest.dir == direction.EAST || pointOfInterest.dir == direction.NORTHEAST)){
            //if(okayToGoEast()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.EAST) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.EAST);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.WEST, direction.SOUTH);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.WEST, direction.SOUTH);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }

        //if obstNorth and objective is west or northwest, keep going west until obstWest or !obstNorth
        if(obst == obstacle.NORTH_WALL && (pointOfInterest.dir == direction.WEST || pointOfInterest.dir == direction.NORTHWEST)){
            //if(okayToGoWest()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.WEST) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.WEST);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.EAST, direction.SOUTH);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.EAST, direction.SOUTH);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }

        //if obstNorth and objective is north,
        if(obst == obstacle.NORTH_WALL && (pointOfInterest.dir == direction.NORTH)){

            numMovesBack = 1;
            while(me.getXMovesMadeBack(numMovesBack) == moveNorth){
                numMovesBack++;
            }
            int lastNonNorthMove = me.getXMovesMadeBack(numMovesBack);

            System.out.println("agent " + me.name + " last move made was: " + getNameFromAgentAction(lastNonNorthMove));

            //if last move was moveEast keep going east until !obstNorth or obstEast
            if(lastNonNorthMove == moveEast) {
                //    if(okayToGoEast()){
                System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.EAST) + " around " + me.getCurrentObstacleString());
                me.pushDirectionToGo(direction.EAST);
                return;
                //    }else{
                //        direction dir = pickRandomlyBetween(direction.WEST, direction.SOUTH);
                //        while(!okayToGo(dir)){
                //            dir = pickRandomlyBetween(direction.WEST, direction.SOUTH);
                //        }
                //        System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
                //        me.pushDirectionToGo(dir);
                //        return;
                //    }
            }

            //if last move was moveWest keep going west until !obstNorth or obstWest
            if(lastNonNorthMove == moveWest) {
                //if(okayToGoWest()){
                System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.WEST) + " around " + me.getCurrentObstacleString());
                me.pushDirectionToGo(direction.WEST);
                return;
                //}else{
                //    direction dir = pickRandomlyBetween(direction.EAST, direction.SOUTH);
                //    while(!okayToGo(dir)){
                //        dir = pickRandomlyBetween(direction.EAST, direction.SOUTH);
                //    }
                //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
                //    me.pushDirectionToGo(dir);
                //    return;
                //}
            }
        }


        //if obstNorth and objective is south, go south
        if(obst == obstacle.NORTH_WALL && (pointOfInterest.dir == direction.EAST || pointOfInterest.dir == direction.SOUTHEAST || pointOfInterest.dir == direction.NORTHEAST)){
            //if(okayToGoSouth()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.SOUTH) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.SOUTH);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.WEST, direction.EAST);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.WEST, direction.EAST);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }

        //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL //SOUTH WALL
        //if obstSouth and objective is east or southeast, keep going east until obstEast or !obstSouth
        if(obst == obstacle.SOUTH_WALL && (pointOfInterest.dir == direction.EAST || pointOfInterest.dir == direction.SOUTHEAST)){
            //if(okayToGoEast()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.EAST) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.EAST);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.WEST, direction.NORTH);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.WEST, direction.NORTH);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }

        //if obstSouth and objective is west or southwest, keep going west until obstWest or !obstSouth
        if(obst == obstacle.SOUTH_WALL && (pointOfInterest.dir == direction.WEST || pointOfInterest.dir == direction.SOUTHWEST)){
            //if(okayToGoWest()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.WEST) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.WEST);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.EAST, direction.NORTH);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.EAST, direction.NORTH);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }

        //if obstSouth and objective is south,
        if(obst == obstacle.SOUTH_WALL && (pointOfInterest.dir == direction.SOUTH)){

            numMovesBack = 1;
            while(me.getXMovesMadeBack(numMovesBack) == moveSouth){
                numMovesBack++;
            }
            int lastNonSouthMove = me.getXMovesMadeBack(numMovesBack);

            System.out.println("agent " + me.name + " last move made was: " + getNameFromAgentAction(lastNonSouthMove));

            //if last move was moveEast keep going east until !obstSouth or obstEast
            if(lastNonSouthMove == moveEast) {
                //if(okayToGoEast()){
                System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.EAST) + " around " + me.getCurrentObstacleString());
                me.pushDirectionToGo(direction.EAST);
                return;
                //}else{
                //    direction dir = pickRandomlyBetween(direction.WEST, direction.NORTH);
                //    while(!okayToGo(dir)){
                //        dir = pickRandomlyBetween(direction.WEST, direction.NORTH);
                //    }
                //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
                //    me.pushDirectionToGo(dir);
                //    return;
                //}
            }

            //if last move was moveWest keep going west until !obstSouth or obstWest
            if(lastNonSouthMove == moveWest) {
                //if(okayToGoWest()){
                System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.WEST) + " around " + me.getCurrentObstacleString());
                me.pushDirectionToGo(direction.WEST);
                return;
                //}else{
                //    direction dir = pickRandomlyBetween(direction.EAST, direction.NORTH);
                //    while(!okayToGo(dir)){
                //        dir = pickRandomlyBetween(direction.EAST, direction.NORTH);
                //    }
                //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
                //    me.pushDirectionToGo(dir);
                //    return;
                //}
            }
        }

        //if obstSouth and objective is north, go north
        if(obst == obstacle.SOUTH_WALL && (pointOfInterest.dir == direction.NORTH || pointOfInterest.dir == direction.NORTHEAST || pointOfInterest.dir == direction.NORTHWEST)){
            //if(okayToGoNorth()){
            System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(direction.NORTH) + " around " + me.getCurrentObstacleString());
            me.pushDirectionToGo(direction.NORTH);
            return;
            //}else{
            //    direction dir = pickRandomlyBetween(direction.WEST, direction.EAST);
            //    while(!okayToGo(dir)){
            //        dir = pickRandomlyBetween(direction.WEST, direction.EAST);
            //    }
            //    System.out.println("agent " + me.name + me.number + " decided to go " + getDirectionName(dir) + " around " + me.getCurrentObstacleString());
            //    me.pushDirectionToGo(dir);
            //    return;
            //}
        }
    }



    public void determineDirectionToGoAroundObstacle(Place pointOfInterest){
        //int lastNonEastMove = me.getLastNonDirMove(direction.EAST);
        //int lastNonSouthMove = me.getLastNonDirMove(direction.SOUTH);
        //int lastNonWestMove = me.getLastNonDirMove(direction.WEST);
        //int lastNonNorthMove = me.getLastNonDirMove(direction.NORTH);

        //int lastNonSouthOrWestMove = me.getLastNonDirMove(direction.WEST, direction.SOUTH);
        //int lastNonNorthOrWestMove = me.getLastNonDirMove(direction.WEST, direction.NORTH);

        obstacle currentObstacle = me.getCurrentObstacle();

        if(me.obstacles.size() == 1){
            //we are trying to get around a wall
            determineDirectionToGoAroundWall(pointOfInterest);
            return;
        }

        if(me.obstacles.size() == 2){
            //we are in a corner


            System.out.println("CORNER!!!");
            //CORNERS

            //if obstEast and obstNorth
            int lastNonNorthOrEastMove = me.getLastNonDirMove(direction.EAST, direction.NORTH);
            if(!okayToGoEast() && !okayToGoNorth()){
                if(pointOfInterest.dir == direction.NORTHEAST) {
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonNorthOrEastMove));
                }

                if(pointOfInterest.dir == direction.SOUTHEAST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(moveSouth));
                }

                if(pointOfInterest.dir == direction.EAST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonNorthOrEastMove));
                }

                if(pointOfInterest.dir == direction.WEST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(moveWest));
                }

                if(pointOfInterest.dir == direction.NORTHWEST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(moveWest));
                }

                if(pointOfInterest.dir == direction.SOUTHWEST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonNorthOrEastMove));
                }

                if(pointOfInterest.dir == direction.SOUTH){
                    me.pushDirectionToGo(getDirectionFromAgentAction(moveSouth));
                }

                if(pointOfInterest.dir == direction.NORTH){
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonNorthOrEastMove));
                }
            }

            int lastNonSouthOrEastMove = me.getLastNonDirMove(direction.EAST, direction.SOUTH);
            //if obstEast and obstSouth
            if(!okayToGoEast() && !okayToGoSouth()){

                if(pointOfInterest.dir == direction.NORTHEAST) {
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonSouthOrEastMove));
                }

                if(pointOfInterest.dir == direction.SOUTHEAST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonSouthOrEastMove));
                }

                if(pointOfInterest.dir == direction.EAST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonSouthOrEastMove));
                }

                if(pointOfInterest.dir == direction.WEST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(moveWest));
                }

                if(pointOfInterest.dir == direction.NORTHWEST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonSouthOrEastMove));
                }

                if(pointOfInterest.dir == direction.SOUTHWEST){
                    me.pushDirectionToGo(getDirectionFromAgentAction(moveWest));
                }

                if(pointOfInterest.dir == direction.NORTH){
                    me.pushDirectionToGo(getDirectionFromAgentAction(moveNorth));
                }

                if(pointOfInterest.dir == direction.SOUTH){
                    me.pushDirectionToGo(getDirectionFromAgentAction(lastNonSouthOrEastMove));
                }

                //if our last move was moveSouth, keep going west until !obstSouth or obstWest

                //if our last move was moveEast, keep going north until !obstEast or obstNorth
            }


            //if obstSouth and obstWest, we are in a corner, keep going north until !obstWest or obstNorth
                //if our last move was moveWest, keep going north until !obstWest or obstNorth

                //if our last move was moveSouth, keep going east until !obstSouth or obstEast

            //if obstWest and obstNorth, we are in a corner, keep going east until !obstNorth or obstEast
                //if our last move was moveWest, keep going south until !obstWest or obstSouth

                //if our last move was moveNorth, keep going east until !obstNorth or obstEast
        }

    }

    public int goAroundObstacle(Place pointOfInterest){

        //int whatToDo = doNothing;
        obstacle obst = me.getCurrentObstacle();

        if((me.getCurrentDirectionToGo() == direction.DONOTHING) || (me.obstacles.size() > 1)){
            determineDirectionToGoAroundObstacle(pointOfInterest);
        }

        direction currentDir = me.getCurrentDirectionToGo();
        direction obstDir = getDirectionFromObstacleDirection(obst);

        if(okayToGo(obstDir)){
            me.popObjective();
            me.popDirectionToGo();
            me.popObstacle();
            return move(obstDir);
        }else if(okayToGo(currentDir)){
            return move(currentDir);
        }
        else{
            //are in a corner
            //me.pushObstacle(obst);
            me.popDirectionToGo();
            me.pushObstacle(getObstacleDirectionFromDirection(currentDir));
            return goAroundObstacle(pointOfInterest);
        }
    }

    //MAP FUNCTIONS//MAP FUNCTIONS //MAP FUNCTIONS //MAP FUNCTIONS //MAP FUNCTIONS //MAP FUNCTIONS //MAP FUNCTIONS //MAP FUNCTIONS

    public ArrayList<direction> getLeastVisitedSurroundingSquares(){
        ArrayList<direction> leastVisited = new ArrayList<direction>();

        int lowest = 200;

        int numNorth = (okayToGoNorth())? (getNumTimesVisited(me.position.oneNorth())) : (200);

        int numEast = (okayToGoEast()) ? (getNumTimesVisited(me.position.oneEast())) : (200);

        int numSouth = (okayToGoSouth()) ? (getNumTimesVisited(me.position.oneSouth())) : (200);

        int numWest = (okayToGoWest()) ? (getNumTimesVisited(me.position.oneWest())) : (200);

        if(numNorth < lowest){
            lowest = numNorth;
        }
        if(numEast < lowest){
            lowest = numEast;
        }
        if(numSouth < lowest){
            lowest = numSouth;
        }
        if(numWest < lowest){
            lowest = numWest;
        }
        if(numNorth == lowest) {
            leastVisited.add(direction.NORTH);
        }
        if(numEast == lowest) {
            leastVisited.add(direction.EAST);
        }
        if(numSouth == lowest){
            leastVisited.add(direction.SOUTH);
        }
        if(numWest == lowest){
            leastVisited.add(direction.WEST);
        }
        return leastVisited;
    }

    public int getNumTimesVisited(Coordinate pos){
        return timesVisited[pos.x][pos.y];
    }

    public int checkForSurroundingEnemyAgentsWithOurFlag(){
        int whatToDo = doNothing;
        if(enemyNorthImmediate && enemyHasOurFlag){
            whatToDo = moveNorth;
        }else if(enemyEastImmediate && enemyHasOurFlag){
            whatToDo = moveEast;
        }
        else if(enemySouthImmediate && enemyHasOurFlag){
            whatToDo = moveSouth;
        }else if(enemyWestImmediate && enemyHasOurFlag) {
            whatToDo = moveWest;
        }
        return whatToDo;
    }

    public int goRandomOpenDirection(){
        ArrayList<direction> freeDirections = freeDirections();
        direction dir = direction.DONOTHING;

        int num = randomInt(1, freeDirections.size());
        for(int i = 0; i < freeDirections.size(); i++){
            if(num == i){
                dir = freeDirections.get(i);
                break;
            }
        }
        return move(dir);
    }

    public int moveRandomlyOneOfTheseDirectionsIfOpenOrElseDoNothing(direction firstDir, direction secondDir){
        direction dir = pickRandomlyBetween(firstDir, secondDir);

        if(okayToGo(dir)){
            return move(dir);
        }else if(dir == firstDir && okayToGo(secondDir)){
            return move(secondDir);
        }else{
            return doNothing;
        }
    }

    public int moveRandomlyOneOfTheseDirectionsIfOpenOrElseDoNothing(direction firstDir, direction secondDir, direction thirdDir){

        boolean firstDirBad = false;
        boolean secondDirBad = false;
        boolean thirdDirBad = false;

        while(true){
            direction dir = pickRandomlyBetween(firstDir, secondDir, thirdDir);
            if(okayToGo(dir)){
                return move(dir);
            }
            if(dir == firstDir){
                firstDirBad = true;
            }
            if(dir == secondDir){
                secondDirBad = true;
            }
            if(dir == thirdDir){
                thirdDirBad = true;
            }
            if(firstDirBad && secondDirBad && thirdDirBad){
                return doNothing;
            }
        }
    }

    public int moveRandomlyOneOfTheseDirectionsIfOpenOrElseDoNothing(direction firstDir, direction secondDir, direction thirdDir, direction fourthDir){

        boolean firstDirBad = false;
        boolean secondDirBad = false;
        boolean thirdDirBad = false;
        boolean fourthDirBad = false;

        while(true){
            direction dir = pickRandomlyBetween(firstDir, secondDir, thirdDir, fourthDir);
            if(okayToGo(dir)){
                return move(dir);
            }
            if(dir == firstDir){
                firstDirBad = true;
            }
            if(dir == secondDir){
                secondDirBad = true;
            }
            if(dir == thirdDir){
                thirdDirBad = true;
            }
            if(dir == fourthDir){
                fourthDirBad = true;
            }
            if(firstDirBad && secondDirBad && thirdDirBad && fourthDirBad){
                return doNothing;
            }
        }
    }

    public direction pickRandomlyBetween(direction firstDir, direction secondDir){
        direction dir = direction.DONOTHING;

        int num = randomInt(1, 2);
        return (num == 1) ? (firstDir) : (secondDir);
    }

    public direction pickRandomlyBetween(direction firstDir, direction secondDir, direction thirdDir){
        direction dir = direction.DONOTHING;

        int num = randomInt(1, 3);
        switch(num){
            case 1:
                return firstDir;
            case 2:
                return secondDir;
            case 3:
                return thirdDir;
        }
        return dir;
    }

    public direction pickRandomlyBetween(direction firstDir, direction secondDir, direction thirdDir, direction fourthDir){
        direction dir = direction.DONOTHING;

        int num = randomInt(1, 4);

        switch(num){
            case 1:
                return firstDir;
            case 2:
                return secondDir;
            case 3:
                return thirdDir;
            case 4:
                return fourthDir;
        }
        return dir;
    }

    public int pickRandomlyBetweenAndMove(direction firstDir, direction secondDir, direction thirdDir, direction fourthDir){
        direction dir = pickRandomlyBetween(firstDir, secondDir, thirdDir, fourthDir);
        return move(dir);
    }

    public int pickRandomlyBetweenAndMove(direction firstDir, direction secondDir){
        direction dir = pickRandomlyBetween(firstDir, secondDir);
        return move(dir);
    }

    public int randomInt(int min, int max){
        rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public int pickRandomlyBetweenDirectionsAndMove(ArrayList<direction> directions){
        direction dir = direction.DONOTHING;

        int num = randomInt(1, directions.size());
        for(int i = 0; i < directions.size(); i++){
            if(num == i){
                dir = directions.get(i);
                break;
            }
        }
        return move(dir);
    }

    public int moveToOneOfLeastVisitedSquares(){
        int whatToDo = pickRandomlyBetweenDirectionsAndMove(getLeastVisitedSurroundingSquares());
        return whatToDo;
    }

    public ArrayList<direction> freeDirections(){
        ArrayList<direction> freeDirections = new ArrayList<direction>();
        if(okayToGoNorth()){
            freeDirections.add(direction.NORTH);
        }
        if(okayToGoEast()) {
            freeDirections.add(direction.EAST);
        }
        if(okayToGoSouth()) {
            freeDirections.add(direction.SOUTH);
        }
        if(okayToGoWest()){
            freeDirections.add(direction.WEST);
        }
        return freeDirections;
    }

    public boolean directionsOkay(direction firstDirection, direction secondDirection){
        return (okayToGo(firstDirection) && okayToGo(secondDirection));
    }

    public boolean oscillatingMoveCheck(){
        if(me.squaresTravelled.size() < 3){
            return false;
        }else{
            return (getXPositionsBack(2).isEqualTo(me.position));
        }
    }

    public boolean smallCircleCheck(){
        boolean hasCircle = false;

        if(me.squaresTravelled.size() < 5){
            return false;
        }

        /*int d = dir.ordinal();
        for(int i = 0; i < 4; i++){
            if(getDirectionFromAgentAction(getXMovesBack(4)).ordinal() == d) {
                hasCircle = true;
            }
                d++;
        }*/
        Coordinate fourPositionsBack = getXPositionsBack(4);
        if(fourPositionsBack == me.position){
            //then I have gone in a small circle
            //System.out.println("mp: " + me.position.toString() + " 4pb: " + fourPositionsBack.toString());
            //System.out.println("CIRCLE!!!");
            hasCircle = true;
        }
        return hasCircle;
    }

    public Coordinate getLastPosition(){
        return me.squaresTravelled.get(me.squaresTravelled.size() - 2);
    }

    public Coordinate getXPositionsBack(Integer x){
        Coordinate xPosback = me.squaresTravelled.get(me.squaresTravelled.size() - (x + 1));
        return xPosback;
    }

    public int getLastMove(){
        return me.movesMade.get(me.movesMade.size() - 2);
    }

    public int getXMovesBack(Integer x){
        return me.movesMade.get(me.movesMade.size() - (x + 1));
    }

    public direction getOppositeDirection(direction dir){
        direction opposite = direction.DONOTHING;
        switch(dir){
            case NORTH:
                opposite = direction.SOUTH;
                break;
            case NORTHEAST:
                opposite = direction.SOUTHWEST;
                break;
            case EAST:
                opposite = direction.WEST;
                break;
            case SOUTHEAST:
                opposite = direction.NORTHWEST;
                break;
            case SOUTH:
                opposite = direction.NORTH;
                break;
            case SOUTHWEST:
                opposite = direction.NORTHEAST;
                break;
            case WEST:
                opposite = direction.EAST;
                break;
            case NORTHWEST:
                opposite = direction.SOUTHEAST;
                break;
        }
        return opposite;
    }

    public int preferSameDirectionAsPreviousMove(){
        if(getLastMove() == moveNorth && okayToGoNorth()){
            return moveNorth();
        }else if(getLastMove() == moveSouth && okayToGoSouth()){
            return moveSouth();
        }else if(getLastMove() == moveEast && okayToGoEast()){
            return moveEast();
        }else if(getLastMove() == moveWest && okayToGoWest()){
            return moveWest();
        }else{
            return doNothing;
        }
    }

    public int preferOppositeDirectionOfPreviousMove(){
        if(getLastMove() == moveNorth && okayToGoSouth()){
            return moveSouth();
        }else if(getLastMove() == moveSouth && okayToGoNorth()){
            return moveNorth();
        }else if (getLastMove() == moveEast && okayToGoWest()){
            return moveWest();
        }else if (getLastMove() == moveWest && okayToGoEast()){
            return moveEast();
        }else{
            return doNothing;
        }
    }

    public int plantMine(){
        changeMyPosition(me.position, me.position);  //I'm not moving, but call this function for bookkeeping.
        setMapSquare(me.position, 'm');
        me.movesMade.add(plantMine);
        possibleMines.add(me.position);
        return plantMine;
    }

    public int moveNorth(){
        Coordinate newPosition = new Coordinate(me.position.x, me.position.y - 1);
        changeMyPosition(me.position, newPosition);
        me.movesMade.add(moveNorth);
        return moveNorth;
    }

    public int moveEast(){
        Coordinate newPosition = new Coordinate(me.position.x + 1, me.position.y);
        changeMyPosition(me.position, newPosition);
        me.movesMade.add(moveEast);
        return moveEast;
    }

    public int moveSouth(){
        Coordinate newPosition = new Coordinate(me.position.x, me.position.y + 1);
        changeMyPosition(me.position, newPosition);
        me.movesMade.add(moveSouth);
        return moveSouth;
    }

    public int moveWest(){
        Coordinate newPosition = new Coordinate(me.position.x - 1, me.position.y);
        changeMyPosition(me.position, newPosition);
        me.movesMade.add(moveWest);
        return moveWest;
    }

    public int preferInOrder(direction firstDirection, direction secondDirection) {
        int whatToDo = doNothing;
        if (okayToGo(firstDirection)) {
            whatToDo = move(firstDirection);
        } else if (okayToGo(secondDirection)) {
            whatToDo = move(secondDirection);
        }
        return whatToDo;
    }

    public int preferInOrder(direction firstDirection, direction secondDirection, direction thirdDirection, direction fourthDirection){
        int whatToDo = doNothing;
        if(okayToGo(firstDirection)){
            whatToDo = move(firstDirection);
        }else if(okayToGo(secondDirection)){
            whatToDo = move(secondDirection);
        }else if(okayToGo(thirdDirection)){
            whatToDo = move(thirdDirection);
        }else if(okayToGo(fourthDirection)){
            whatToDo = move(fourthDirection);
        }else{
            whatToDo = doNothing;
        }
        return whatToDo;
    }

    public boolean okayToGo(direction dir){
        switch(dir){
            case NORTH:
                return okayToGoNorth();
            case EAST:
                return okayToGoEast();
            case SOUTH:
                return okayToGoSouth();
            case WEST:
                return okayToGoWest();
        }
        return false;
    }

    public boolean isObst(direction dir){
        switch(dir){
            case NORTH:
                return obstNorth;
            case EAST:
                return obstEast;
            case SOUTH:
                return obstSouth;
            case WEST:
                return obstWest;
        }
        return false;
    }

    int move(direction dir){
        switch(dir){
            case NORTH:
                return moveNorth();
            case EAST:
                return moveEast();
            case SOUTH:
                return moveSouth();
            case WEST:
                return moveWest();
        }
        return doNothing;
    }

    public void addSurroundingObstaclesToMap(AgentEnvironment inEnvironment) {

        //presence of surrounding obstacles
        if(me.position.y == 0){
            obstNorth = inEnvironment.isObstacleNorthImmediate();
        }else{
            obstNorth = (inEnvironment.isObstacleNorthImmediate() || (map[me.position.x][me.position.y - 1] == 'X'));
        }

        if(me.position.y == mapHeight - 1){
            obstSouth = inEnvironment.isObstacleSouthImmediate();
        }else{
            obstSouth = (inEnvironment.isObstacleSouthImmediate() || (map[me.position.x][me.position.y + 1] == 'X'));
        }

        if(me.position.x == mapHeight -1){
            obstEast  = inEnvironment.isObstacleEastImmediate();
        }else{
            obstEast  = (inEnvironment.isObstacleEastImmediate() || (map[me.position.x + 1][me.position.y] == 'X'));
        }

        if(me.position.x == 0){
            obstWest  = inEnvironment.isObstacleWestImmediate();
        }else{
            obstWest  = (inEnvironment.isObstacleWestImmediate() || (map[me.position.x - 1][me.position.y] == 'X'));
        }

        obstNorthEastSouth = (obstNorth && obstEast && obstSouth);
        obstEastSouthWest = (obstEast && obstSouth && obstWest);
        obstSouthWestNorth = (obstSouth && obstWest && obstNorth);
        obstWestNorthEast = (obstWest && obstNorth && obstEast);
        obstEveryDirection = (obstWest && obstNorth && obstEast && obstSouth);

        if (obstNorth && me.position.y != 0) {
            map[me.position.x][me.position.y - 1] = 'X';
        }
        if (obstEast && me.position.x != mapHeight - 1) {
            map[me.position.x + 1][me.position.y] = 'X';
        }
        if (obstSouth && me.position.y != mapHeight - 1) {
            map[me.position.x][me.position.y + 1] = 'X';
        }
        if (obstWest && me.position.x != 0) {
            map[me.position.x - 1][me.position.y] = 'X';
        }

        //if(obstInThreeDirections()){map[me.position.x][me.position.y] = 'X';}
    }

    public void determineMySpawnAndInitialPosition() {

        if (enemyBaseEast && ourBaseSouth) {
            me.spawn.set(0, 0);
        } else if (enemyBaseEast && ourBaseNorth) {
            me.spawn.set(0, mapHeight - 1);
        } else if (enemyBaseWest && ourBaseNorth) {
            me.spawn.set(mapHeight - 1, mapHeight - 1);
        } else if (enemyBaseWest && ourBaseSouth) {
            me.spawn.set(mapHeight - 1, 0);
        }
        me.position.set(me.spawn);
        //System.out.println("agent " + me.number + " init: " + me.position.toString());
    }

    public Boolean amIAtSpawn() {

        if ((ourBaseWest == false) && (ourBaseEast == false)) {
            if ((ourBaseSouth == true) && (obstNorth == true)) {
                if ((enemyBaseWest == true) && (me.spawn.x == mapHeight - 1) && (me.spawn.y == 0)) {
                    System.out.println("agent" + me.number + " has been respawned at (" + me.spawn.x + ", " + me.spawn.y + ")");
                    changeMyPosition(me.position, me.spawn);
                    makeEmptyTimesVisited();
                    return true;
                }
                if ((enemyBaseWest == false) && (me.spawn.x == 0) && (me.spawn.y == 0)) {
                    System.out.println("agent" + me.number + " has been respawned at (" + me.spawn.x + ", " + me.spawn.y + ")");
                    changeMyPosition(me.position, me.spawn);
                    makeEmptyTimesVisited();
                    return true;
                }
            } else if ((ourBaseNorth == true) && (obstSouth == true)) {
                if ((enemyBaseWest == true) && (me.spawn.x == mapHeight - 1) && (me.spawn.y == mapHeight - 1)) {
                    System.out.println("agent" + me.number + " has been respawned at (" + me.spawn.x + ", " + me.spawn.y + ")");
                    changeMyPosition(me.position, me.spawn);
                    makeEmptyTimesVisited();
                    return true;
                }
                if ((enemyBaseWest == false) && (me.spawn.x == 0) && (me.spawn.y == mapHeight - 1)) {
                    System.out.println("agent" + me.number + " has been respawned at (" + me.spawn.x + ", " + me.spawn.y + ")");
                    changeMyPosition(me.position, me.spawn);
                    makeEmptyTimesVisited();
                    return true;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return false;
    }

    public void determineBasePositions() {
        if (enemyBaseWest) {
            ourBase.position.set(9, 5);
            enemyBase.position.set(0, 5);
        } else if (enemyBaseEast) {
            ourBase.position.set(0, 5);
            enemyBase.position.set(9, 5);
        }
        System.out.println("enemy base: " + enemyBase.position.toString());
        System.out.println("ourBase: " + ourBase.position.toString());
    }

    public void determineDefenseWallCenterPositionAndDirection(){

        if(enemyBaseWest){
            defenseWallCenter.position.x = ourBase.position.x - 2;
        }else{
            defenseWallCenter.position.x = ourBase.position.x + 2;
        }
        defenseWallCenter.position.y = ourBase.position.y;
        System.out.println("determined defense wall center " + defenseWallCenter.position.toString());

        if(positionIsNorthEast(defenseWallCenter.position)){
            defenseWallCenter.dir = direction.NORTHEAST;
        }else if(positionIsNorthWest(defenseWallCenter.position)){
            defenseWallCenter.dir = direction.NORTHWEST;
        }else if(positionIsSouthEast(defenseWallCenter.position)){
            defenseWallCenter.dir = direction.SOUTHEAST;
        }else if(positionIsSouthWest(defenseWallCenter.position)){
            defenseWallCenter.dir = direction.SOUTHWEST;
        }else if(positionIsNorth(defenseWallCenter.position)){
            defenseWallCenter.dir = direction.NORTH;
        }else if(positionIsEast(defenseWallCenter.position)){
            defenseWallCenter.dir = direction.EAST;
        }else if(positionIsSouth(defenseWallCenter.position)){
            defenseWallCenter.dir = direction.SOUTH;
        }else{
            defenseWallCenter.dir = direction.WEST;
        }
    }

    public void setMapSquare(Coordinate pos, char c) {
        map[pos.x][pos.y] = c;
    }

    public void moveMeOnMap(Coordinate source, Coordinate destination) {
        if(obstInThreeDirections()){
            setMapSquare(source, 'X');
        }else {
            setMapSquare(source, ' ');
        }
        setMapSquare(destination, me.name);
    }

    public void updateTimesVisited(Coordinate pos){
        timesVisited[pos.x][pos.y]++;
    }

    public void changeMyPosition(Coordinate source, Coordinate destination) {
        me.squaresTravelled.add(new Coordinate(destination));
        //updateTimesVisited(destination);
        moveMeOnMap(source, destination);
        me.position.set(destination);
    }

    public void makeEmptyMap() {
        map = new char[mapHeight][mapHeight];
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapHeight; j++) {
                map[i][j] = '.';
            }
        }
    }

    public void makeEmptyTimesVisited(){
        timesVisited = new int[mapHeight][mapHeight];
        for(int i = 0; i < mapHeight; i++){
            for(int j = 0; j < mapHeight; j++){
                timesVisited[i][j] = 0;
            }
        }
    }

    public void printMap() {

        map[ourBase.position.x][ourBase.position.y] = 'O';
        map[enemyBase.position.x][enemyBase.position.y] = 'E';

        for (int i = 0; i < agents.size(); i++) {
            map[agents.get(i).position.x][agents.get(i).position.y] = agents.get(i).name;
        }

        System.out.println("----------");
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapHeight; j++) {
                System.out.print(map[j][i]);
            }
            System.out.println();
        }
        System.out.println("----------");
    }

    //ENVIRONMENT FUNCTIONS//ENVIRONMENT FUNCTIONS //ENVIRONMENT FUNCTIONS //ENVIRONMENT FUNCTIONS //ENVIRONMENT FUNCTIONS //ENVIRONMENT FUNCTIONS

    public boolean okayToGoNorth() {
        Coordinate oneNorth = new Coordinate(me.position.x, me.position.y - 1);
        boolean hasPossibleMine = possibleMines.contains(oneNorth);
        return ((me.hasFlag && enemyNorthImmediate) || friendlyNorthImmediate || obstNorth || (ourBaseNorthImmediate && !me.hasFlag) || hasPossibleMine) ? (false) : (true);
    }

    public boolean okayToGoEast() {
        Coordinate oneEast = new Coordinate(me.position.x + 1, me.position.y);
        boolean hasPossibleMine = possibleMines.contains(oneEast);
        return ((me.hasFlag && enemyEastImmediate) || friendlyEastImmediate || obstEast || (ourBaseEastImmediate && !me.hasFlag) || hasPossibleMine) ? (false) : (true);
    }

    public boolean okayToGoWest() {
        Coordinate oneWest = new Coordinate(me.position.x - 1, me.position.y);
        boolean hasPossibleMine = possibleMines.contains(oneWest);
        return ((me.hasFlag && enemyWestImmediate)|| friendlyWestImmediate || obstWest || (ourBaseWestImmediate && !me.hasFlag) || hasPossibleMine) ? (false) : (true);
    }

    public boolean okayToGoSouth() {
        Coordinate oneSouth = new Coordinate(me.position.x, me.position.y + 1);
        boolean hasPossibleMine = possibleMines.contains(oneSouth);
        return ((me.hasFlag && enemySouthImmediate) || friendlySouthImmediate || obstSouth || (ourBaseSouthImmediate && !me.hasFlag) || hasPossibleMine) ? (false) : (true);
    }

    public void queryEnvironment(AgentEnvironment inEnvironment) {

        //do I currently have the flag
        me.hasFlag = inEnvironment.hasFlag();
        enemyHasOurFlag = inEnvironment.hasFlag(inEnvironment.ENEMY_TEAM);

        //direction of our base
        ourBaseWest = inEnvironment.isBaseWest(inEnvironment.OUR_TEAM, false);
        ourBaseEast = inEnvironment.isBaseEast(inEnvironment.OUR_TEAM, false);
        ourBaseSouth = inEnvironment.isBaseSouth(inEnvironment.OUR_TEAM, false);
        ourBaseNorth = inEnvironment.isBaseNorth(inEnvironment.OUR_TEAM, false);

        //immediate presence of our base
        ourBaseWestImmediate = inEnvironment.isBaseWest(inEnvironment.OUR_TEAM, true);
        ourBaseEastImmediate = inEnvironment.isBaseEast(inEnvironment.OUR_TEAM, true);
        ourBaseSouthImmediate = inEnvironment.isBaseSouth(inEnvironment.OUR_TEAM, true);
        ourBaseNorthImmediate = inEnvironment.isBaseNorth(inEnvironment.OUR_TEAM, true);

        //direction of enemy base
        enemyBaseEast = inEnvironment.isBaseEast(inEnvironment.ENEMY_TEAM, false);
        enemyBaseWest = inEnvironment.isBaseWest(inEnvironment.ENEMY_TEAM, false);
        enemyBaseNorth = inEnvironment.isBaseNorth(inEnvironment.ENEMY_TEAM, false);
        enemyBaseSouth = inEnvironment.isBaseSouth(inEnvironment.ENEMY_TEAM, false);

        //immediate presence of enemy base
        enemyBaseEastImmediate = inEnvironment.isBaseEast(inEnvironment.ENEMY_TEAM, true);
        enemyBaseWestImmediate = inEnvironment.isBaseWest(inEnvironment.ENEMY_TEAM, true);
        enemyBaseNorthImmediate = inEnvironment.isBaseNorth(inEnvironment.ENEMY_TEAM, true);
        enemyBaseSouthImmediate = inEnvironment.isBaseNorth(inEnvironment.ENEMY_TEAM, true);

        //presence of surrounding obstacles
        obstNorth = inEnvironment.isObstacleNorthImmediate();
        obstSouth = inEnvironment.isObstacleSouthImmediate();
        obstEast  = inEnvironment.isObstacleEastImmediate();
        obstWest  = inEnvironment.isObstacleWestImmediate();

        obstNorthEast = (obstNorth && obstEast);
        obstSouthEast = (obstSouth && obstEast);
        obstNorthWest = (obstNorth && obstWest);
        obstSouthWest = (obstSouth && obstWest);

        //immediate presence of enemy
        enemyNorthImmediate = inEnvironment.isAgentNorth(inEnvironment.ENEMY_TEAM, true);
        enemyEastImmediate = inEnvironment.isAgentEast(inEnvironment.ENEMY_TEAM, true);
        enemySouthImmediate = inEnvironment.isAgentSouth(inEnvironment.ENEMY_TEAM, true);
        enemyWestImmediate = inEnvironment.isAgentWest(inEnvironment.ENEMY_TEAM, true);

        //immediate presence of friendly agent
        friendlyNorthImmediate = inEnvironment.isAgentNorth(inEnvironment.OUR_TEAM, true);
        friendlyEastImmediate = inEnvironment.isAgentEast(inEnvironment.OUR_TEAM, true);
        friendlySouthImmediate = inEnvironment.isAgentSouth(inEnvironment.OUR_TEAM, true);
        friendlyWestImmediate = inEnvironment.isAgentWest(inEnvironment.OUR_TEAM, true);

        //update the direction for all points of interest
        for (int i = 0; i < pointsOfInterest.size(); i++) {
            if (pointsOfInterest.get(i).name == "ourBase") {
                if (ourBaseNorth && ourBaseEast) {
                    pointsOfInterest.get(i).dir = direction.NORTHEAST;
                } else if (ourBaseEast && ourBaseSouth) {
                    pointsOfInterest.get(i).dir = direction.SOUTHEAST;
                } else if (ourBaseWest && ourBaseSouth) {
                    pointsOfInterest.get(i).dir = direction.SOUTHWEST;
                } else if (ourBaseWest && ourBaseNorth) {
                    pointsOfInterest.get(i).dir = direction.NORTHWEST;
                } else if (ourBaseNorth) {
                    pointsOfInterest.get(i).dir = direction.NORTH;
                } else if (ourBaseEast) {
                    pointsOfInterest.get(i).dir = direction.EAST;
                } else if (ourBaseWest) {
                    pointsOfInterest.get(i).dir = direction.WEST;
                } else if (ourBaseSouth) {
                    pointsOfInterest.get(i).dir = direction.SOUTH;
                }
            } else if (pointsOfInterest.get(i).name == "enemyBase") {
                if (enemyBaseNorth && enemyBaseEast) {
                    pointsOfInterest.get(i).dir = direction.NORTHEAST;
                } else if (enemyBaseEast && enemyBaseSouth) {
                    pointsOfInterest.get(i).dir = direction.SOUTHEAST;
                } else if (enemyBaseWest && enemyBaseSouth) {
                    pointsOfInterest.get(i).dir = direction.SOUTHWEST;
                } else if (enemyBaseWest && enemyBaseNorth) {
                    pointsOfInterest.get(i).dir = direction.NORTHWEST;
                } else if (enemyBaseNorth) {
                    pointsOfInterest.get(i).dir = direction.NORTH;
                } else if (enemyBaseEast) {
                    pointsOfInterest.get(i).dir = direction.EAST;
                } else if (enemyBaseWest) {
                    pointsOfInterest.get(i).dir = direction.WEST;
                } else if (enemyBaseSouth) {
                    pointsOfInterest.get(i).dir = direction.SOUTH;
                }
            }
        }
    }

    public boolean positionIsNorth(Coordinate pos){
        return (me.position.y > pos.y);
    }

    public boolean positionIsSouth(Coordinate pos){
        return (me.position.y < pos.y);
    }

    public boolean positionIsEast(Coordinate pos){
        return (me.position.x < pos.x);
    }

    public boolean positionIsWest(Coordinate pos){
        return (me.position.x > pos.x);
    }

    public boolean positionIsNorthEast(Coordinate pos){
        return (positionIsNorth(pos) && positionIsEast(pos));
    }

    public boolean positionIsSouthEast(Coordinate pos){
        return (positionIsSouth(pos) && positionIsEast(pos));
    }

    public boolean positionIsNorthWest(Coordinate pos) {
        return (positionIsNorth(pos) && positionIsWest(pos));
    }

    public boolean positionIsSouthWest(Coordinate pos){
        return (positionIsSouth(pos) && positionIsWest(pos));
    }

    public boolean obstInThreeDirections() {
        return (obstNorthEastSouth || obstEastSouthWest || obstSouthWestNorth || obstWestNorthEast);
    }

    public boolean blockedInThreeDirections(){
        return (blockedEastSouthWest() || blockedNorthEastSouth() || blockedSouthWestNorth() || blockedSouthWestNorth());
    }

    public boolean blockedNorthEastSouth(){
        return (!okayToGoNorth() && !okayToGoEast() && !okayToGoSouth());
    }

    public boolean blockedEastSouthWest(){
        return (!okayToGoEast() && !okayToGoSouth() && !okayToGoWest());
    }

    public boolean blockedSouthWestNorth(){
        return (!okayToGoSouth() && !okayToGoWest() && !okayToGoNorth());
    }

    public boolean blockedWestNorthEast(){
        return (!okayToGoWest() && !okayToGoNorth() && !okayToGoEast());
    }

    public direction getOnlyNonObstDirection() {
        direction freeDirection = direction.EAST;
        if (obstNorthEastSouth) {
            freeDirection = direction.WEST;
        } else if (obstEastSouthWest) {
            freeDirection = direction.NORTH;
        } else if (obstSouthWestNorth) {
            freeDirection = direction.EAST;
        } else if (obstWestNorthEast) {
            freeDirection = direction.SOUTH;
        }
        return freeDirection;
    }

    public direction getOnlyFreeDirection() {
        direction freeDirection = direction.DONOTHING;
        if (blockedNorthEastSouth()) {
            freeDirection = direction.WEST;
        } else if (blockedEastSouthWest()) {
            freeDirection = direction.NORTH;
        } else if (blockedSouthWestNorth()) {
            freeDirection = direction.EAST;
        } else if (blockedWestNorthEast()) {
            freeDirection = direction.SOUTH;
        }
        return freeDirection;
    }

    //STRING CONVERSION FUNCTIONS //STRING CONVERSION FUNCTIONS //STRING CONVERSION FUNCTIONS //STRING CONVERSION FUNCTIONS
    public String getNameFromAgentAction(int action) {
        String name = "does nothing";

        switch (action) {
            case AgentAction.MOVE_NORTH:
                name = "moves north";
                break;
            case AgentAction.MOVE_EAST:
                name = "moves east";
                break;
            case AgentAction.MOVE_SOUTH:
                name = "moves south";
                break;
            case AgentAction.MOVE_WEST:
                name = "moves west";
                break;
            case AgentAction.PLANT_HYPERDEADLY_PROXIMITY_MINE:
                name = "plants a mine";
                break;
        }
        return name;
    }

    public String getDirectionName(direction dir) {
        switch (dir) {
            case NORTH:
                return "north";
            case NORTHEAST:
                return "northeast";
            case EAST:
                return "east";
            case SOUTHEAST:
                return "southeast";
            case SOUTH:
                return "south";
            case SOUTHWEST:
                return "southwest";
            case WEST:
                return "west";
            case NORTHWEST:
                return "northwest";
        }
        return "error";
    }

    public int getAgentActionFromDirection(direction dir) {
        int action = doNothing;

        switch (dir) {
            case NORTH:
                action = moveNorth;
                break;
            case EAST:
            case NORTHEAST:
            case SOUTHEAST:
                action = moveEast;
                break;
            case WEST:
            case NORTHWEST:
            case SOUTHWEST:
                action = moveWest;
                break;
            case SOUTH:
                action = moveSouth;
                break;
        }
        return action;
    }

    public direction getDirectionFromAgentAction(int lastAction){
        direction dir = direction.DONOTHING;
        if(lastAction == moveNorth){
            dir = direction.NORTH;
        }else if(lastAction == moveEast){
            dir = direction.EAST;
        }else if(lastAction == moveWest){
            dir = direction.WEST;
        }else{
            dir = direction.SOUTH;
        }
        return dir;
    }

    public obstacle getObstacleDirectionFromDirection(direction dir){
        obstacle obst = obstacle.NONE;

        switch(dir){
            case NORTH:
                obst = obstacle.NORTH_WALL;
                break;
            case EAST:
                obst = obstacle.EAST_WALL;
                break;
            case SOUTH:
                obst = obstacle.SOUTH_WALL;
                break;
            case WEST:
                obst = obstacle.WEST_WALL;
                break;
        }
        return obst;
    }

    public direction getDirectionFromObstacleDirection(obstacle obst){
        direction dir = direction.DONOTHING;

        switch(obst){
            case NORTH_WALL:
                dir = direction.NORTH;
                break;
            case EAST_WALL:
                dir = direction.EAST;
                break;
            case SOUTH_WALL:
                dir = direction.SOUTH;
                break;
            case WEST_WALL:
                dir = direction.WEST;
                break;
        }
        return dir;
    }
}