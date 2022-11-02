package be.isach.ultracosmetics.mysql.tables;

import be.isach.ultracosmetics.cosmetics.type.CosmeticType;
import be.isach.ultracosmetics.mysql.column.Column;
import be.isach.ultracosmetics.mysql.column.ForeignKeyConstraint;
import be.isach.ultracosmetics.mysql.column.UUIDColumn;
import be.isach.ultracosmetics.mysql.column.UniqueConstraint;
import be.isach.ultracosmetics.mysql.query.InsertValue;

import java.util.UUID;

import javax.sql.DataSource;

public class UnlockedTable extends Table {
    private final PlayerDataTable playerData;
    private final CosmeticTable cosmeticTable;

    public UnlockedTable(DataSource dataSource, String name, PlayerDataTable playerData, CosmeticTable cosmeticTable) {
        super(dataSource, name);
        this.playerData = playerData;
        this.cosmeticTable = cosmeticTable;
    }

    @Override
    protected void setupTableInfo() {
        tableInfo.add(new UUIDColumn());
        tableInfo.add(new Column<>("id", "INTEGER NOT NULL", Integer.class));
        tableInfo.add(new UniqueConstraint("uuid", "id"));
        tableInfo.add(new ForeignKeyConstraint("uuid", playerData.getWrappedName(), "uuid"));
        tableInfo.add(new ForeignKeyConstraint("id", cosmeticTable.getWrappedName(), "id"));
    }

    public boolean hasUnlocked(UUID uuid, CosmeticType<?> type) {
        return this.selectVoid().uuid(uuid).where(cosmeticTable.subqueryFor(type, false)).exists();
    }

    public void setUnlocked(UUID uuid, CosmeticType<?> type) {
        insertIgnore("uuid", "id").insert(new InsertValue(hexUUID(uuid)), cosmeticTable.subqueryFor(type, true)).execute();
    }

    public void unsetUnlocked(UUID uuid, CosmeticType<?> type) {
        delete().uuid(uuid).where(cosmeticTable.subqueryFor(type, false)).execute();
    }
}
