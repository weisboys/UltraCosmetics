package be.isach.ultracosmetics.mysql.query;

import java.util.List;

public class ClauseItemLiteral implements ClauseItem {
    private final String column;
    private final Object value;
    private final boolean executable;

    public ClauseItemLiteral(String column, Object value, boolean executable) {
        this.column = column;
        this.value = value;
        this.executable = executable;
    }

    public ClauseItemLiteral(String column, Object value) {
        this(column, value, false);
    }

    public String getColumn() {
        return column;
    }

    public Object getValue() {
        return value;
    }

    public boolean isExecutable() {
        return executable;
    }

    @Override
    public String toSQL(List<Object> objects) {
        String prefix = (column == null ? "" : column + " = ");
        if (executable) {
            return prefix + value;
        }
        objects.add(value);
        return prefix + "?";
    }
}
