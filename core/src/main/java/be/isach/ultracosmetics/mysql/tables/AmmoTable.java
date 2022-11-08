package be.isach.ultracosmetics.mysql.tables;

import be.isach.ultracosmetics.cosmetics.type.GadgetType;
import be.isach.ultracosmetics.mysql.column.Column;
import be.isach.ultracosmetics.mysql.column.ForeignKeyConstraint;
import be.isach.ultracosmetics.mysql.column.UUIDColumn;
import be.isach.ultracosmetics.mysql.column.UniqueConstraint;
import be.isach.ultracosmetics.mysql.column.VirtualUUIDColumn;
import be.isach.ultracosmetics.mysql.query.InnerJoin;
import be.isach.ultracosmetics.mysql.query.InsertQuery;
import be.isach.ultracosmetics.mysql.query.InsertValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.sql.DataSource;

public class AmmoTable extends Table {
    private final PlayerDataTable playerData;
    private final CosmeticTable cosmeticTable;

    public AmmoTable(DataSource dataSource, String name, PlayerDataTable playerData, CosmeticTable cosmeticTable) {
        super(dataSource, name);
        this.playerData = playerData;
        this.cosmeticTable = cosmeticTable;
    }

    @Override
    protected void setupTableInfo() {
        tableInfo.add(new UUIDColumn());
        tableInfo.add(new VirtualUUIDColumn());
        tableInfo.add(new Column<>("id", "INTEGER NOT NULL", Integer.class));
        tableInfo.add(new Column<>("ammo", "INTEGER NOT NULL DEFAULT 0", Integer.class));
        tableInfo.add(new ForeignKeyConstraint("uuid", playerData.getWrappedName(), "uuid"));
        tableInfo.add(new ForeignKeyConstraint("id", cosmeticTable.getWrappedName(), "id"));
        tableInfo.add(new UniqueConstraint("uuid", "id"));
    }

    public int getAmmo(UUID uuid, GadgetType type) {
        return select("ammo").uuid(uuid).where(cosmeticTable.subqueryFor(type, false)).asInt();
    }

    public Map<GadgetType,Integer> getAllAmmo(UUID uuid) {
        return select("ammo, type").uuid(uuid).innerJoin(new InnerJoin(cosmeticTable.getWrappedName(), "id")).getResults(r -> {
            Map<GadgetType,Integer> ammo = new HashMap<>();
            while (!r.isAfterLast()) {
                ammo.put(GadgetType.valueOf(r.getString("type")), r.getInt("ammo"));
                r.next();
            }
            return ammo;
        });
    }

    public void setAmmo(UUID uuid, GadgetType type, int amount) {
        insert("uuid", "id", "ammo").insert(new InsertValue(hexUUID(uuid)), cosmeticTable.subqueryFor(type, true), new InsertValue(amount))
                .updateOnDuplicate().execute();
    }

    public void setAllAmmo(UUID uuid, Map<GadgetType,Integer> ammo) {
        InsertQuery query = insert("uuid", "id", "ammo");
        InsertValue uuidVal = insertUUID(uuid);
        for (Entry<GadgetType,Integer> entry : ammo.entrySet()) {
            query.insert(uuidVal, cosmeticTable.subqueryFor(entry.getKey(), true), new InsertValue(entry.getValue()));
        }
        query.updateOnDuplicate().execute();
    }
}
