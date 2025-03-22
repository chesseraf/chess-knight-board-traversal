public interface BoardCreator {
    abstract Board createBoard(int r, int c);
    class AccurateBoardCreator implements BoardCreator
    {
        @Override
        public Board createBoard(int r, int c)
        {
            return Board.make4x4Board(r, c);
        }
    }
    class RandLessAccurateCreator implements BoardCreator
    {
        @Override
        public Board createBoard(int r, int c)
        {
            return Board.makeMiniLoop(r, c);
        }
    }
    
}
