package be.isach.ultracosmetics.mysql.column;

public class StringColumn extends Column<String> {
    private final int size;

    public StringColumn(String name, int size, boolean ascii, boolean nullable) {
        super(name, "VARCHAR(" + size + ") " + (nullable ? "" : "NOT NULL ") + "CHARACTER SET " + (ascii ? "latin1" : "utf8mb4"), String.class);
        this.size = size;
    }

    public StringColumn(String name, int size, boolean ascii) {
        this(name, size, ascii, false);
    }

    public int getSize() {
        return size;
    }
}
