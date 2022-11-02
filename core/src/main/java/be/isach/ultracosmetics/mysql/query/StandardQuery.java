package be.isach.ultracosmetics.mysql.query;

import be.isach.ultracosmetics.UltraCosmeticsData;
import be.isach.ultracosmetics.mysql.column.Column;
import be.isach.ultracosmetics.mysql.tables.Table;
import be.isach.ultracosmetics.mysql.tables.TableInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.UUID;

public class StandardQuery {
    protected final Table table;
    protected final String command;
    protected final List<ClauseItem> whereItems = new ArrayList<>();
    protected final List<ClauseItem> setItems = new ArrayList<>();
    protected InnerJoin innerJoin;

    public StandardQuery(Table table, String command) {
        this.table = table;
        this.command = command + " " + table.getWrappedName();
    }

    public StandardQuery where(ClauseItem clauseItem) {
        whereItems.add(clauseItem);
        return this;
    }

    public StandardQuery where(String key, Object value) {
        return where(new ClauseItemLiteral(key, value));
    }

    // convenience since we use it so often
    public StandardQuery uuid(UUID uuid) {
        return where(new ClauseItemLiteral("uuid", Table.hexUUID(uuid), true));
    }

    public StandardQuery set(ClauseItem clauseItem) {
        setItems.add(clauseItem);
        return this;
    }

    public StandardQuery set(String key, Object value) {
        return set(new ClauseItemLiteral(key, value));
    }

    public StandardQuery innerJoin(InnerJoin join) {
        this.innerJoin = join;
        return this;
    }

    /**
     * Weird and janky because when a PreparedStatement is closed,
     * its ResultSet is also closed. To work around this, you must
     * pass a Function as a parameter that returns whatever the
     * getResults() method as a whole should return (type parameters ensure same type)
     *
     * @param <T>           Return type, determined by function passed in.
     * @param processResult Function to process ResultSet.
     * @return Whatever processResult() returns.
     */
    public <T> T getResults(ResultGetter<T> processResult) {
        if (whereItems.size() == 0) {
            throw new IllegalStateException("Should not execute non-INSERT query without WHERE clause");
        }
        StringBuilder sql = new StringBuilder(command);
        List<Object> objects = new ArrayList<>();
        addClause(sql, "SET", ", ", setItems, objects);
        if (innerJoin != null) {
            sql.append(" ").append(innerJoin.toSQL(table.getWrappedName()));
        }
        addClause(sql, "WHERE", " AND ", whereItems, objects);
        if (UltraCosmeticsData.get().getPlugin().getMySqlConnectionManager().isDebug()) {
            String plaintext = sql.toString();
            for (Object obj : objects) {
                plaintext = plaintext.replaceFirst("\\?", obj == null ? "NULL" : obj.toString());
            }
            UltraCosmeticsData.get().getPlugin().getSmartLogger().write("Executing SQL: " + plaintext);
        }
        try (Connection connection = table.getConnection(); PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < objects.size(); i++) {
                statement.setObject(i + 1, objects.get(i));
            }
            if (processResult == null) {
                statement.executeUpdate();
                return null;
            }
            ResultSet result = statement.executeQuery();
            // yes, this is required
            result.next();
            return processResult.process(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void execute() {
        getResults(null);
    }

    public boolean exists() {
        return getResults(r -> r.next());
    }

    public int asInt() {
        return getResults(r -> r.getInt(1));
    }

    public boolean asBool() {
        return getResults(r -> r.getBoolean(1));
    }

    public String asString() {
        return getResults(r -> r.getString(1));
    }

    public Map<String,Object> getAll() {
        return getResults(r -> {
            Map<String,Object> values = new HashMap<>();
            for (TableInfo info : table.getTableInfo()) {
                if (info instanceof Column<?>) {
                    values.put(((Column<?>) info).getName(), ((Column<?>) info).getValue(r));
                }
            }
            return values;
        });
    }

    private void addClause(StringBuilder sb, String clause, String joiner, List<ClauseItem> items, List<Object> objects) {
        if (items.size() == 0) return;
        sb.append(" " + clause + " ");
        StringJoiner sj = new StringJoiner(joiner);
        for (ClauseItem item : items) {
            sj.add(item.toSQL(objects));
        }
        sb.append(sj.toString());
    }
}
