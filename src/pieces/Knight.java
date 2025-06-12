package pieces;

import gui.Board;

import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;

public class Knight extends Piece {
    public Knight(Board board, int col, int row, boolean isWhite    ) {
        super(board);
        this.col = col;
        this.row = row;
        this.isWhite = isWhite;
        this.xPos = col * board.tilesize;
        this.yPos = row * board.tilesize;
        this.name = "Knight";
        this.sprite = sheet.getSubimage(3* sheetScale, isWhite ? 0 : sheetScale, sheetScale, sheetScale).getScaledInstance(sheetScale, sheetScale, BufferedImage.SCALE_AREA_AVERAGING);

    }

    public boolean isValidMovement(int col, int row) {
        return Math.abs(col - this.col) * Math.abs(row - this.row) == 2;
    }
}
