package be.isach.ultracosmetics.mysql.column;

public class ForeignKeyConstraint extends Constraint {

    public ForeignKeyConstraint(String column, String foreignTable, String foreignColumn) {
        super("FOREIGN KEY (" + column + ") REFERENCES " + foreignTable + "(" + foreignColumn + ")");
    }

}
