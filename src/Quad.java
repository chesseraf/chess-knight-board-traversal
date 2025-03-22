public  class Quad
    {
        int r, c;
        public Quad(int r, int c)
        {
            this.r = r;
            this.c = c;
        }
        public int getR()
        {
            return r;
        }
        public int getC()
        {
            return c;
        }
        public boolean hasCoord(Coordinate co)
        {
            return this.equals(getQuad(co));
        }
        public static Quad getQuad(Coordinate co)
        {
            return new Quad(co.getRows()/4, co.getCols()/4);
        }
        public boolean equals(Quad other)
        {
            return r == other.r && c == other.c;
        }
        public Quad move(Side s)
        {
            int rows= r;
            int cols = c;
            switch (s) {
                case LEFT -> cols--;
                case RIGHT -> cols++;
                case UP -> rows--;
                case DOWN -> rows++;
            }
            return new Quad(rows, cols);
        }
    }