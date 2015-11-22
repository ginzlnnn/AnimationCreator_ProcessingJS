Acontroller c;

class Amaker {
  int widthX, heightY;
  int size[][];
  int frameNum;
  int frame[][][];
  Amaker(int w, int h) {
    widthX = w;
    heightY = h;
    size = new int[heightY][widthX];
    frame = new int[0][heightY][widthX];
    frameNum = 0;
  }

  int[][] get_size() {
    return size;
  }
  void set_size(int[][] current) {
    size = current;
  }
  int[][][] get_frame() {
    return frame;
  }
  void set_frame(int[][][] current) {
    frame = current;
  }
  int get_frameNum() {
    return frameNum;
  }
  void setFrame(int set) {
    frameNum+=set;
  }
  int get_w() {
    return widthX;
  }
  int get_h() {
    return heightY;
  }
}
class Acontroller {
  Amaker m;
  Aviewer v;
  int move = 0;
  int x1;
  int y1;
  Acontroller(Amaker maker, Aviewer viewer) {
    m = maker;
    v = viewer;
  }
  void add_tile(int x, int y) { 
    int[][] current = m.get_size();
    current[y][x] = 1;
    m.set_size(current);
  }
  void remove_tile(int x, int y) { 
    int[][] current = m.get_size();
    current[y][x] = 0;
    m.set_size(current);
  }
  void move_tile(int x1, int y1, int x2, int y2) {
    int[][] current = m.get_size();
    current[y2][x2] = 1;
    current[y1][x1] = 0;
    m.set_size(current);
    print("!Moved Frame!");
  }
  void add_frame() {
    int[][] currentF;
    currentF = new int[m.get_h()][m.get_w()];
    for (int i = 0; i < m.get_h(); i++) {
      for (int j = 0; j < m.get_size()[i].length; j++) {
        currentF[i][j] = m.get_size()[i][j];
      }
    }
    int[][][]addedFrame = (int[][][])append(m.get_frame(), currentF);
    m.set_frame(addedFrame);
    m.setFrame(1);
    println("!Add Frame!");
  }
  void remove_frame() {
    int[][][] totalFrame = m.get_frame();
    int[][][] emptyFrame = new int[0][m.get_h()][m.get_w()];
    if (m.get_frame().length > 0 ) {
      totalFrame = (int[][][])shorten(totalFrame);
      m.set_frame(totalFrame);
      m.setFrame(-1);
    } else {
      m.set_frame(emptyFrame);
    }
    println("!Remove Frame!");
  }

  void tileDisplay() {
    v.display();
  }
  void controlDisplay() { 
    controlButton(10, 25, 90, 40, 4, #008800, "Add Frame", 255);
    controlButton(110, 25, 90, 40, 4, #FF0000, "Remove Frame", 255);
  }

  void clicked() {    
    for (int i = 0; i<m.get_size().length; i++) {
      for (int j = 0; j<m.get_size()[i].length; j++) {
        if (mouseButton == LEFT && m.get_size()[i][j] != 1 && move != 1 && buttonPosition((j*26)+v.get_viewX(), (i*26)+v.get_viewY(), 25, 25)) {
          add_tile(j, i);
        } else if (mouseButton == RIGHT && buttonPosition(j*26+v.get_viewX(), i*26+v.get_viewY(), 25, 25)) {
          remove_tile(j, i);
          move = 0;   
          println("\nDisplay All Frame");
          for (int k = 0; k<m.get_frameNum(); k++) {
            for (int n = 0; n < m.get_size().length; n++) {
              for (int l = 0; l < m.get_size()[i].length; l++) {
                print(m.get_frame()[k][n][l]);
              }
              println();
            }
            println("------------------");
          }
        } else if (mouseButton == LEFT && move == 0 && m.get_size()[i][j] == 1 &&  buttonPosition(j*26+v.get_viewX(), i*26+v.get_viewY(), 25, 25)) {
          x1 = j;
          y1 = i;
          int[][] current = m.get_size();
          if (j-1 >= 0 && current[i][j-1] != 1) {
            current[i][j-1] = 2;
          }
          if (j+1 <= current[0].length-1 && current[i][j+1] != 1) {
            current[i][j+1] = 2;
          }
          if (i-1 >= 0 && current[i-1][j] != 1) {
            current[i-1][j] = 2;
          }
          if (i+1 <= current.length-1 && current[i+1][j] != 1) {
            current[i+1][j] = 2;
          }
          m.set_size(current);
          move = 1;
        } else if (mouseButton == LEFT && move == 1 && m.get_size()[i][j] == 2 && buttonPosition(j*26+v.get_viewX(), i*26+v.get_viewY(), 25, 25)) {
          move_tile(x1, y1, j, i);
          int[][] current = m.get_size();
          for (int n = 0; n < m.get_size().length; n++) {
            for (int l = 0; l < m.get_size()[i].length; l++) {
              if (current[n][l] == 2) {
                current[n][l] = 0;
              }
            }
          }
          move = 0;
        }
      }
    }

    if (buttonPosition(20, 25, 90, 40)) {
      add_frame();
    } else if (buttonPosition(110, 25, 90, 40)) {
      remove_frame();
    }
  }
}

class Aviewer {
  Amaker m;
  int x;
  int y;
  Aviewer(Amaker maker) {
    m = maker;
    x = 210;
    y = 20;
  }
  void display() {
    int tileSize = 25;
    int posY = y;
    for (int i = 0; i < m.get_size().length; i++) {
      int posX = x;
      for (int j = 0; j < m.get_size()[i].length; j++) {
        if (m.get_size()[i][j] == 0) {
          fill(#FFFFFF);
        } else if (m.get_size()[i][j] == 1) {
          fill(#FF0000);
        } else if (m.get_size()[i][j] == 2) {
          fill(#33FF33);
        }
        rect(posX, posY, tileSize, tileSize);
        posX += tileSize+1;
      }
      posY += tileSize+1;
    }
  }

  int get_viewX() {
    return x;
  }
  int get_viewY() {
    return y;
  }
}

void setup() {
  size(700, 350);
  Amaker m = new Amaker(18, 10);
  Aviewer v = new Aviewer(m);
  c = new Acontroller(m, v);
}
void draw() {
  background(#DDEEFF);
  c.controlDisplay();
  c.tileDisplay();
}

void mousePressed() {
  c.clicked();
}


void controlButton(int x, int y, int buttonWidth, int buttonHeight, int corner, int buttonColor, String textButton, int textColor) {
  fill(buttonColor);
  rect(x, y, buttonWidth, buttonHeight, corner);
  fill(textColor);
  textAlign(CENTER, CENTER);
  text(textButton, x+(buttonWidth/2), y+(buttonHeight/2.5));
}

boolean buttonPosition(int x, int y, int buttonWidth, int buttonHeight) {
  if (mouseX >= x && mouseX <= x+buttonWidth && mouseY >= y && mouseY <= y+buttonHeight) {
    return true;
  } else {
    return false;
  }
}