package gui;


import pieces.Knight;
import pieces.Pawn;
import pieces.Piece;
import pieces.Queen;
import pieces.Rook;
import pieces.Bishop;
import pieces.King;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class Board extends JPanel {
    public int tilesize = 80;
    int cols = 8;
    int rows = 8;

    ArrayList<Piece> pieceList = new ArrayList<>();

    public Piece selectedPiece;

    public int enPassantTile = -1;

    Input input = new Input(this);

    public Board() {
        this.setPreferredSize(new Dimension(cols * tilesize, rows * tilesize));
        this.setBackground(Color.BLUE);

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    public Piece getPiece(int col, int row) {
        for (Piece piece : pieceList) {
            if (piece.col == col && piece.row == row) {
                return piece;
            }
        }
        return null;
    }

    public int getTileNum(int col, int row) {

        return row * rows + col;
    }

    /** replace pawn with a queen on the promotion square */
    private void promotePawn(Move move) {
        // 1) remove the pawn itself
        pieceList.remove(move.piece);
        // 2) add the new queen
        pieceList.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
    }

    private void movePawn(Move move) {
        int dir = move.piece.isWhite ? -1 : 1;     // white goes up (-1), black down (+1)

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow - dir);
        }

        if (Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.piece.row + dir);
        } else {
            enPassantTile = -1;
        }

        move.piece.col   = move.newCol;
        move.piece.row   = move.newRow;
        move.piece.xPos  = move.newCol * tilesize;
        move.piece.yPos  = move.newRow * tilesize;
        move.piece.isFirstMove = false;
        capture(move);
        // promotion
        int promotionRow = move.piece.isWhite ? 0 : 7;
        if (move.newRow == promotionRow) {
            promotePawn(move);
        }

    }

    public void makeMove(Move move) {
        if (move.piece.name.equals("Pawn")) {
            movePawn(move);
        } else {
            // non-pawn moves
            move.piece.col       = move.newCol;
            move.piece.row       = move.newRow;
            move.piece.xPos      = move.newCol * tilesize;
            move.piece.yPos      = move.newRow * tilesize;
            move.piece.isFirstMove = false;
            capture(move);
        }
    }

    public void capture(Move move) {
        pieceList.remove(move.capture);
    }

    public boolean isValidMove(Move move) {
        if (sameTeam(move.piece, move.capture)) {
            return false;
        }

        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }

        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }

        return true;
    }

    private boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public void addPieces() {
        pieceList.add(new Rook(this, 0, 0, false));
        pieceList.add(new Knight(this, 1, 0, false));
        pieceList.add(new Bishop(this, 2, 0, false));
        pieceList.add(new Queen(this, 3, 0, false));
        pieceList.add(new King(this, 4, 0, false));
        pieceList.add(new Bishop(this, 5, 0, false));
        pieceList.add(new Knight(this, 6, 0, false));
        pieceList.add(new Rook(this, 7, 0, false));
        // Black pawns
        for (int x = 0; x < cols; x++) {
            pieceList.add(new Pawn(this, x, 1, false));
        }
        // White pawns
        for (int x = 0; x < cols; x++) {
            pieceList.add(new Pawn(this, x, 6, true));
        }
        // White back‐rank
        pieceList.add(new Rook(this, 0, 7, true));
        pieceList.add(new Knight(this, 1, 7, true));
        pieceList.add(new Bishop(this, 2, 7, true));
        pieceList.add(new Queen(this, 3, 7, true));
        pieceList.add(new King(this, 4, 7, true));
        pieceList.add(new Bishop(this, 5, 7, true));
        pieceList.add(new Knight(this, 6, 7, true));
        pieceList.add(new Rook(this, 7, 7, true));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g2d.setColor((i + j) % 2 == 0
                        ? new Color(240, 217, 181)
                        : new Color(181, 136, 99));
                g2d.fillRect(j * tilesize, i * tilesize, tilesize, tilesize);
            }
        }
        // move highlighting
        if (selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(68, 180, 57, 190));
                        g2d.fillRect(c * tilesize, r * tilesize, tilesize, tilesize);
                    }
                }
            }
        }
        // pieces
        for (Piece piece : pieceList) {
            piece.paint(g2d);
        }
    }

}
