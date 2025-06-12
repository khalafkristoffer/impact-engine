package pieces;

import gui.Board;

import java.awt.image.BufferedImage;

public class Bishop extends Piece {
    public Bishop(Board board, int col, int row, boolean isWhite    ) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tilesize;
        this.yPos = row * board.tilesize;
        this.name = "Bishop";
        this.sprite = sheet.getSubimage(2* sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(sheetScale, sheetScale, BufferedImage.SCALE_SMOOTH);

    }

    public boolean isValidMovement(int col, int row) {
        return Math.abs(this.col - col) == Math.abs(this.row - row);
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        int dc = col - this.col;
        int dr = row - this.row;
        if (Math.abs(dc) != Math.abs(dr)) {
            return false;
        }
        int stepC = Integer.signum(dc);
        int stepR = Integer.signum(dr);
        int distance = Math.abs(dc);
        for (int i = 1; i < distance; i++) {
            int c = this.col + i * stepC;
            int r = this.row + i * stepR;
            if (board.getPiece(c, r) != null) {
                return true;
            }
        }
        return false;
    }
}
