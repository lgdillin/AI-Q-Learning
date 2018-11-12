
class State {
  double[][] world;

  State() {
    world = new double[20][10];

    for(int i = 0; i < 20; ++i) {
      world[i][5] = -1;
    }
  }
}

class QLearner {
  double learningRate;
  double discountRate = 0.9;

  QLearner() {

  }

  double learn() {

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
