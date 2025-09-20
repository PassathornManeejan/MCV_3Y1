package app.model.entity;

public enum Grade {
    A, BPLUS, B, CPLUS, C, DPLUS, D, F;

    public static Grade fromString(String s) {
        if (s == null || s.isBlank()) return null; // not graded yet
        s = s.trim().toUpperCase();
        return switch (s) {
            case "A" -> A;
            case "B+" -> BPLUS;
            case "B" -> B;
            case "C+" -> CPLUS;
            case "C" -> C;
            case "D+" -> DPLUS;
            case "D" -> D;
            case "F" -> F;
            default -> null;
        };
    }

    @Override public String toString() {
        return switch (this) {
            case A -> "A"; case BPLUS -> "B+"; case B -> "B";
            case CPLUS -> "C+"; case C -> "C"; case DPLUS -> "D+"; case D -> "D"; case F -> "F";
        };
    }
}
