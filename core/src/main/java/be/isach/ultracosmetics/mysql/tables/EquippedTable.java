package be.isach.ultracosmetics.mysql.tables;

import be.isach.ultracosmetics.cosmetics.Category;
import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.mysql.column.Column;
import be.isach.ultracosmetics.mysql.column.ForeignKeyConstraint;
import be.isach.ultracosmetics.mysql.column.StringColumn;
import be.isach.ultracosmetics.mysql.column.UUIDColumn;
import be.isach.ultracosmetics.mysql.column.UniqueConstraint;
import be.isach.ultracosmetics.mysql.column.VirtualUUIDColumn;
import be.isach.ultracosmetics.mysql.query.InsertValue;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

public class EquippedTable extends Table {
    private final PlayerDataTable playerData;
    private final CosmeticTable cosmeticTable;

    public EquippedTable(DataSource dataSource, String name, PlayerDataTable playerData, CosmeticTable cosmeticTable) {
        super(dataSource, name);
        this.playerData = playerData;
        this.cosmeticTable = cosmeticTable;
    }

    @Override
    protected void setupTableInfo() {
        tableInfo.add(new UUIDColumn());
        tableInfo.add(new VirtualUUIDColumn());
        tableInfo.add(new Column<>("id", "INTEGER NOT NULL", Integer.class));
        tableInfo.add(new StringColumn("category", 32, true));
        tableInfo.add(new ForeignKeyConstraint("uuid", playerData.getWrappedName(), "uuid"));
        tableInfo.add(new ForeignKeyConstraint("id", cosmeticTable.getWrappedName(), "id"));
        tableInfo.add(new ForeignKeyConstraint("category", cosmeticTable.getWrappedName(), "category"));
        tableInfo.add(new UniqueConstraint("uuid", "category"));
    }

    public Map<Category,CosmeticType<?>> getEquipped(UUID uuid) {
        return select("category, id").uuid(uuid).getResults(r -> {
            Map<Category,CosmeticType<?>> equipped = new HashMap<>();
            while (!r.isAfterLast()) {
                Category cat = Category.valueOf(r.getString("category"));
                CosmeticType<?> type = cat.valueOfType(r.getString("id"));
                equipped.put(cat, type);
            }
            return equipped;
        });
    }

    public void setEquipped(UUID uuid, CosmeticType<?> type) {
        insert("uuid", "id", "category").insert(insertUUID(uuid), cosmeticTable.subqueryFor(type, true), new InsertValue(getCategoryName(type)))
                .updateOnDuplicate().execute();
    }
}
