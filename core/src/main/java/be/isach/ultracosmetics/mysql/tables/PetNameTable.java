package be.isach.ultracosmetics.mysql.tables;

import be.isach.ultracosmetics.cosmetics.type.PetType;
import be.isach.ultracosmetics.mysql.column.Column;
import be.isach.ultracosmetics.mysql.column.ForeignKeyConstraint;
import be.isach.ultracosmetics.mysql.column.StringColumn;
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

public class PetNameTable extends Table {
    private final PlayerDataTable playerData;
    private final CosmeticTable cosmeticTable;

    public PetNameTable(DataSource dataSource, String name, PlayerDataTable playerData, CosmeticTable cosmeticTable) {
        super(dataSource, name);
        this.playerData = playerData;
        this.cosmeticTable = cosmeticTable;
    }

    @Override
    protected void setupTableInfo() {
        tableInfo.add(new UUIDColumn());
        tableInfo.add(new VirtualUUIDColumn());
        tableInfo.add(new Column<>("id", "INTEGER NOT NULL", Integer.class));
        tableInfo.add(new StringColumn("name", 256, false, true));
        tableInfo.add(new ForeignKeyConstraint("uuid", playerData.getWrappedName(), "uuid"));
        tableInfo.add(new ForeignKeyConstraint("id", cosmeticTable.getWrappedName(), "id"));
        tableInfo.add(new UniqueConstraint("uuid", "id"));
    }

    public String getPetName(UUID uuid, PetType type) {
        return select("name").uuid(uuid).where(cosmeticTable.subqueryFor(type, false)).asString();
    }

    public Map<PetType,String> getAllPetNames(UUID uuid) {
        return select("name, type").uuid(uuid).innerJoin(new InnerJoin(cosmeticTable.getWrappedName(), "id")).getResults(r -> {
            Map<PetType,String> names = new HashMap<>();
            while (!r.isAfterLast()) {
                names.put(PetType.valueOf(r.getString("type")), r.getString("name"));
                r.next();
            }
            return names;
        });
    }

    public void setPetName(UUID uuid, PetType type, String name) {
        insert("uuid", "id", "name").insert(new InsertValue(hexUUID(uuid)), cosmeticTable.subqueryFor(type, true), new InsertValue(name))
                .updateOnDuplicate().execute();
    }

    public void setAllPetNames(UUID uuid, Map<PetType,String> names) {
        InsertQuery query = insert("uuid", "id", "name");
        InsertValue uuidVal = insertUUID(uuid);
        for (Entry<PetType,String> entry : names.entrySet()) {
            query.insert(uuidVal, cosmeticTable.subqueryFor(entry.getKey(), true), new InsertValue(entry.getValue()));
        }
        query.updateOnDuplicate().execute();
    }
}
