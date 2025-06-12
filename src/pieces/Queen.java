package pieces;

import gui.Board;

import java.awt.image.BufferedImage;

public class Queen extends Piece {
    public Queen(Board board, int col, int row, boolean isWhite    ) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tilesize;
        this.yPos = row * board.tilesize;
        this.name = "Queen";
        this.sprite = sheet.getSubimage(sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(sheetScale, sheetScale, BufferedImage.SCALE_SMOOTH);
    }

    @Override
    public boolean isValidMovement(int col, int row) {
        int dc = col - this.col;
        int dr = row - this.row;
        // straight lines or perfect diagonals
        return (dc == 0 && dr != 0)
            || (dr == 0 && dc != 0)
            || (Math.abs(dc) == Math.abs(dr));
    }

    @Override
    public boolean moveCollidesWithPiece(int col, int row) {
        int dc = col - this.col;
        int dr = row - this.row;
        int stepC = Integer.signum(dc);
        int stepR = Integer.signum(dr);

        // vertical move
        if (dc == 0 && dr != 0) {
            for (int r = this.row + stepR; r != row; r += stepR) {
                if (board.getPiece(col, r) != null) {
                    return true;
                }
            }
        }
        // horizontal move
        else if (dr == 0 && dc != 0) {
            for (int c = this.col + stepC; c != col; c += stepC) {
                if (board.getPiece(c, row) != null) {
                    return true;
                }
            }
        }
        // diagonal move
        else if (Math.abs(dc) == Math.abs(dr)) {
            for (int i = 1; i < Math.abs(dc); i++) {
                int c = this.col + i * stepC;
                int r = this.row + i * stepR;
                if (board.getPiece(c, r) != null) {
                    return true;
                }
            }
        }
        return false;
    }
}
