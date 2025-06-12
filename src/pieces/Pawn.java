package pieces;

import gui.Board;

import java.awt.image.BufferedImage;

public class Pawn extends Piece {
    public Pawn(Board board, int col, int row, boolean isWhite    ) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tilesize;
        this.yPos = row * board.tilesize;
        this.name = "Pawn";
        this.sprite = sheet.getSubimage(5* sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(sheetScale, sheetScale, BufferedImage.SCALE_SMOOTH);

    }

    @Override
    public boolean isValidMovement(int col, int row) {
        int dir = isWhite ? -1 : 1;

        // forward one
        if (col == this.col
            && row - this.row == dir
            && board.getPiece(col, row) == null) {
            return true;
        }
        // forward two on first move
        if (isFirstMove
            && col == this.col
            && row - this.row == 2 * dir
            && board.getPiece(col, this.row + dir) == null
            && board.getPiece(col, row) == null) {
            return true;
        }
        // diagonal capture
        Piece target = board.getPiece(col, row);
        if (Math.abs(col - this.col) == 1
            && row - this.row == dir
            && target != null
            && target.isWhite != this.isWhite) {
            return true;
        }

        // en-passant
        if (Math.abs(col - this.col) == 1
            && row - this.row == dir
            && board.getTileNum(col, row) == board.enPassantTile) {
            return true;
        }

        return false;
    }
}
