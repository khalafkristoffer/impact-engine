package pieces;

import gui.Board;

import java.awt.image.BufferedImage;

public class King extends Piece {
    public King(Board board, int col, int row, boolean isWhite    ) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tilesize;
        this.yPos = row * board.tilesize;
        this.name = "King";
        this.sprite = sheet.getSubimage(0* sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(sheetScale, sheetScale, BufferedImage.SCALE_SMOOTH);

    }

    @Override
    public boolean isValidMovement(int col, int row) {
        int dc = Math.abs(col - this.col);
        int dr = Math.abs(row - this.row);
        return (dc <= 1 && dr <= 1) && (dc + dr != 0);
    }
}
