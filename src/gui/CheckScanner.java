package gui;

import pieces.Piece;

public class CheckScanner {
    private Board board;

    public CheckScanner(Board board) {
        this.board = board;
    }

    /**
     * Returns true if making the given move would leave the moving side's king in check.
     */
    public boolean isKingChecked(Move move) {
        // locate king for moving side
        Piece king = board.findKing(move.piece.isWhite);
        if (king == null) return true;

        // save state
        int oldCol = move.piece.col;
        int oldRow = move.piece.row;
        Piece captured = board.getPiece(move.newCol, move.newRow);

        // apply move
        if (captured != null) board.pieceList.remove(captured);
        move.piece.col = move.newCol;
        move.piece.row = move.newRow;

        // determine king's square after move
        int kingCol = king.col;
        int kingRow = king.row;
        if (move.piece == king) {
            kingCol = move.newCol;
            kingRow = move.newRow;
        }

        // check if attacked by opponent
        boolean inCheck = isSquareAttacked(kingCol, kingRow, !move.piece.isWhite);

        // undo move
        move.piece.col = oldCol;
        move.piece.row = oldRow;
        if (captured != null) board.pieceList.add(captured);

        return inCheck;
    }

    /**
     * Checks if a given square is attacked by the specified side.
     */
    public boolean isSquareAttacked(int col, int row, boolean byWhite) {
        // orthogonal (rook/queen)
        int[][] orth = {{1,0},{-1,0},{0,1},{0,-1}};
        for (int[] d : orth) {
            for (int dist = 1; dist < 8; dist++) {
                int c = col + d[0] * dist;
                int r = row + d[1] * dist;
                if (c < 0 || c > 7 || r < 0 || r > 7) break;
                Piece p = board.getPiece(c, r);
                if (p != null) {
                    if (p.isWhite == byWhite && (p.name.equals("Rook") || p.name.equals("Queen"))) {
                        return true;
                    }
                    break;
                }
            }
        }
        // diagonal (bishop/queen)
        int[][] diag = {{1,1},{1,-1},{-1,1},{-1,-1}};
        for (int[] d : diag) {
            for (int dist = 1; dist < 8; dist++) {
                int c = col + d[0] * dist;
                int r = row + d[1] * dist;
                if (c < 0 || c > 7 || r < 0 || r > 7) break;
                Piece p = board.getPiece(c, r);
                if (p != null) {
                    if (p.isWhite == byWhite && (p.name.equals("Bishop") || p.name.equals("Queen"))) {
                        return true;
                    }
                    break;
                }
            }
        }
        // knight
        int[][] knights = {{2,1},{2,-1},{-2,1},{-2,-1},{1,2},{1,-2},{-1,2},{-1,-2}};
        for (int[] k : knights) {
            int c = col + k[0];
            int r = row + k[1];
            if (c < 0 || c > 7 || r < 0 || r > 7) continue;
            Piece p = board.getPiece(c, r);
            if (p != null && p.isWhite == byWhite && p.name.equals("Knight")) {
                return true;
            }
        }
        // pawn attacks
        int pawnDir = byWhite ? 1 : -1;
        for (int dc : new int[]{-1,1}) {
            int c = col + dc;
            int r = row + pawnDir;
            if (c < 0 || c > 7 || r < 0 || r > 7) continue;
            Piece p = board.getPiece(c, r);
            if (p != null && p.isWhite == byWhite && p.name.equals("Pawn")) {
                return true;
            }
        }
        // king adjacency
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int c = col + dx;
                int r = row + dy;
                if (c < 0 || c > 7 || r < 0 || r > 7) continue;
                Piece p = board.getPiece(c, r);
                if (p != null && p.isWhite == byWhite && p.name.equals("King")) {
                    return true;
                }
            }
        }
        return false;
    }
}
