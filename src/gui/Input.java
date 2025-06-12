package gui;

import pieces.Piece;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Input extends MouseAdapter {
    Board board;

    public Input(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.tilesize;
        int row = e.getY() / board.tilesize;

        Piece pieceXY = board.getPiece(col, row);
        if (pieceXY != null) {
            board.selectedPiece = pieceXY;
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null) {
            board.selectedPiece.xPos = e.getX() - board.tilesize / 2;
            board.selectedPiece.yPos = e.getY() - board.tilesize / 2;

            board.repaint();
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int col = e.getX() / board.tilesize;
        int row = e.getY() / board.tilesize;
        if (board.selectedPiece != null) {
            Move move = new Move(board, board.selectedPiece, col, row);

            if (board.isValidMove(move)) {
                board.makeMove(move);
            } else {
                board.selectedPiece.xPos = board.selectedPiece.col * board.tilesize;
                board.selectedPiece.yPos = board.selectedPiece.row * board.tilesize;
            }
        }

        board.selectedPiece = null;
        board.repaint();

    }

}
