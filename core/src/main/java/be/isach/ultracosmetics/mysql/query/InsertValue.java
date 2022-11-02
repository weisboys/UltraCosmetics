package be.isach.ultracosmetics.mysql.query;

import java.util.List;
import java.util.UUID;

public class InsertValue implements InsertItem {
    private final Object value;
    private final boolean executable;

    public InsertValue(Object value, boolean executable) {
        this.value = value;
        this.executable = executable;
    }

    public InsertValue(Object value) {
        this(value, false);
    }

    public InsertValue(UUID uuid) {
        this("x'" + uuid.toString().replace("-", "") + "'", true);
    }

    @Override
    public String toSQL(List<Object> objects) {
        if (executable) {
            return value.toString();
        }
        objects.add(value);
        return "?";
    }
}
