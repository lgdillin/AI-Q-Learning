import java.util.Random;


class State {
  byte x, y;

  State() {

  }

  State(byte x, byte y) {
    if(x < 0) x = 0;
    if(x > 19) x = 19;
    if(y < 0) y = 0;
    if(y > 9) y = 9;

    this.x = x;
    this.y = y;
  }
}

class QLearner {
  Random rand;
  double learningRate = 0.1;
  double discountRate = 0.97;

  int[] qTable;

  QLearner(Random r) {
    rand = r;
    qTable = new int[20 * 10 * 4];
  }

  double learn() {
    State i = new State(); // temp
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
    //learn from the experience
    //reset

  }

  int q(State i, int action) {
    // index = (a+1)(10y + x)
  }

  int doAction(int action) {

  }

  double reward(State i, int action, State j) {

  }
}

class Main {

  Main() {

  }

  public static void main(String[] args) {

  }
}
