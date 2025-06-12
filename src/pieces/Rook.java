package pieces;

import gui.Board;

import java.awt.image.BufferedImage;

public class Rook extends Piece {
    public Rook(Board board, int col, int row, boolean isWhite    ) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tilesize;
        this.yPos = row * board.tilesize;
        this.name = "Rook";
        this.sprite = sheet.getSubimage(4* sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(sheetScale, sheetScale, BufferedImage.SCALE_SMOOTH);

    }

    public boolean isValidMovement(int col, int row) {
        return this.col == col || this.row == row;
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        if (this.col == col) {
            int stepR = Integer.signum(row - this.row);
            for (int r = this.row + stepR; r != row; r += stepR) {
                if (board.getPiece(col, r) != null) {
                    return true;
                }
            }
        } else if (this.row == row) {
            int stepC = Integer.signum(col - this.col);
            for (int c = this.col + stepC; c != col; c += stepC) {
                if (board.getPiece(c, row) != null) {
                    return true;
                }
            }
        }
        return false;
    }

}
