import java.util.Random;


class State {
  byte x, y;

  State() {
  }

  State(byte x, byte y) {
    if(x < 0) x = 0;
    if(x > 9) x = 9;
    if(y < 0) y = 0;
    if(y > 19) y = 19;

    this.x = x;
    this.y = y;
  }

  static void validate(State s) {
    if(s.x < 0) s.x = 0;
    if(s.x > 9) s.x = 9;
    if(s.y < 0) s.y = 0;
    if(s.y > 19) s.y = 19;
  }
}

class QLearner {
  Random rand;
  int xRange, yRange, aRange;
  double learningRate = 0.1;
  double discountRate = 0.97;
  double[] qTable;

  QLearner(Random r) {
    xRange = 10;
    yRange = 20;
    aRange = 4;

    rand = r;
    qTable = new double[xRange * yRange * aRange];
    for(int i = 0; i < qTable.length; ++i) {
      qTable[i] = 0;
    }
  }

  State learn(State i) {
    double epsilon = 0.05;

    int action;
    if(rand.nextDouble() < epsilon) {
      // Explore (pick a random action)
      action = rand.nextInt(4);
    } else {
      // Exploit (pick the best action)
      action = 0;
      for(int candidate = 0; candidate < 4; ++candidate) {
        if(q(i, candidate) > q(i, action))
          action = candidate;
      }

      if(q(i, action) == 0.0)
        action = rand.nextInt(4);
    }

    // do the action
    State j = doAction(i, action);

    //learn from the experience
    updateQTable(i, j, action);

    //reset
    if(j.x == 0 && j.y == 19) { // goal state
      j.x = 9;
      j.y = 0;
    }
    return j;
  }

  void updateQTable(State i, State j, int action) {
    int index = (xRange * action) + i.x + (xRange * aRange * i.y);

    //double calculatedReward = reward(j);
    double max = -100000;
    for(int bAction = 0; bAction < 4; ++bAction) {
      max = Math.max(max, q(j, bAction));
    }

    double partialCalulation = reward(j) + discountRate * max;
    double value = (1 - learningRate) * q(i, action) + learningRate * partialCalulation;
  }

  // Receives an index and returns the Q-value at that location
  double q(State i, int action) {
    //int index = (xRange * action) + i.x + (xRange * aRange * i.y);
    int index = action + (aRange * i.y) + (aRange * yRange * i.x);
    return qTable[index];
  }

  // Receives a state, modifies it with the action and returns a new modified state
  State doAction(State i, int action) {
    // 0: right; 1: up; 2: left; 4: down
    State j = new State(i.x, i.y);
    if(action == 0) ++j.x;
    else if(action == 1) ++j.y;
    else if(action == 2) --j.x;
    else --j.y;
    State.validate(j);
    return j;
  }

  // Calculates a reward based upon the location of the state
  double reward(State j) {
    if(j.y == 11) {
      if(j.x <= 5 || j.x >= 6)
      return -1;
    }

    if(j.x == 0 && j.y == 19)
      return 10;
    return 0;
  }

  int bestMove(int x, int y) {
    int startIndex = (aRange * y) + (aRange * yRange * x);

    double max = -10000;
    double current;
    int iMax = 0;
    for(int i = 0; i < aRange; ++i) {
      current = qTable[startIndex + i];
      if(current > max) {
        max = current;
        iMax = i;
      }
    }
    return iMax;
  }

  //double reward(State i, int action, State j) { return 1; }
}

class Main {
  char[][] world;

  Main() {
    world = new char[10][20];

    for(int i = 0; i < 10; ++i) {
      for(int j = 0; j < 20; ++j) {
        world[i][j] = '.';
      }
    }

    for(int i = 0; i < 10; ++i) {
      if(i == 5 || i == 6) { }
      else world[i][11] = '#';
    }

    world[9][0] = 'S';
    world[0][19] = 'G';
  }

  void placeMoves(QLearner ql) {
    for(int i = 0; i < 10; ++i) {
      for(int j = 0; j < 20; ++j) {
        if(world[i][j] == 'S')
          continue;
        else if(world[i][j] == 'G')
          continue;
        else if(world[i][j] == '#')
          continue;

        int x = ql.bestMove(i, j);
        if(x == 0)
          world[i][j] = '>';
        else if(x == 1)
          world[i][j] = '^';
        else if(x == 2)
          world[i][j] = '<';
        else
          world[i][j] = 'v';
      }
    }
  }

  void print() {
    for(int i = 0; i < 10; ++i) {
      for(int j = 0; j < 20; ++j) {
        System.out.print("" + world[i][j] + ' ');
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    Random r = new Random(123456);

    Main m = new Main();

    QLearner ql = new QLearner(r);
    State s = new State((byte)9, (byte)0);
    for(int i = 0; i < 100000; ++i) {
      ql.learn(s);
    }
    m.placeMoves(ql);
    m.print();
  }
}
