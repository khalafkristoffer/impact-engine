package gui;

import pieces.Piece;
import gui.Move;
import java.util.ArrayList;
import java.util.List;
import pieces.King;
import pieces.Rook;

public class MoveGenerator {
    private Board board;
    private CheckScanner scanner;

    public MoveGenerator(Board board) {
        this.board = board;
        this.scanner = new CheckScanner(board);
    }

    public boolean isLegalMove(Move move) {
        if (!isPseudoLegal(move)) return false;
        // for castling, pseudo-legal ensures path squares are safe
        if (move.piece instanceof King && Math.abs(move.newCol - move.oldCol) == 2) {
            return true;
        }
        return !scanner.isKingChecked(move);
    }

    public boolean isPseudoLegal(Move move) {
        Piece piece = move.piece;
        // castling special: king moves two squares horizontally
        if (piece instanceof King && piece.isFirstMove && move.newRow == move.oldRow) {
            int delta = move.newCol - move.oldCol;
            if (Math.abs(delta) == 2) {
                int rookCol = delta > 0 ? 7 : 0;
                Piece rook = board.getPiece(rookCol, piece.row);
                if (rook instanceof Rook && rook.isFirstMove) {
                    int step = delta > 0 ? 1 : -1;
                    // no pieces between king and rook
                    for (int c = piece.col + step; c != rookCol; c += step) {
                        if (board.getPiece(c, piece.row) != null) return false;
                    }
                    // temporarily remove the rook so it doesn't attack its own path
                    board.pieceList.remove(rook);
                    // check that king's start, transit, and end squares are not attacked
                    boolean pathSafe = true;
                    for (int c = move.oldCol; ; c += step) {
                        if (scanner.isSquareAttacked(c, piece.row, !piece.isWhite)) {
                            pathSafe = false;
                            break;
                        }
                        if (c == move.newCol) break;
                    }
                    // restore the rook
                    board.pieceList.add(rook);
                    if (!pathSafe) return false;
                    return true;
                }
            }
        }
        // basic movement checks
        if (!piece.isValidMovement(move.newCol, move.newRow)) return false;
        if (piece.moveCollidesWithPiece(move.newCol, move.newRow)) return false;
        Piece dest = board.getPiece(move.newCol, move.newRow);
        if (dest != null && dest.isWhite == piece.isWhite) return false;

        return true;
    }

    public List<Move> generateLegalMoves(Piece piece) {
        List<Move> moves = new ArrayList<>();
        for (int c = 0; c < 8; c++) {
            for (int r = 0; r < 8; r++) {
                Move move = new Move(board, piece, c, r);
                if (isLegalMove(move)) {
                    moves.add(move);
                }
            }
        }
        return moves;
    }

    public List<Move> generateAllLegalMoves(boolean isWhite) {
        List<Move> moves = new ArrayList<>();
        for (Piece p : new java.util.ArrayList<>(board.pieceList)) {
            if (p.isWhite == isWhite) {
                moves.addAll(generateLegalMoves(p));
            }
        }
        return moves;
    }
}
