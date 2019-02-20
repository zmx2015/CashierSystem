package com.zmx.mian.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zmx.mian.bean.Goods;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GOODS".
*/
public class GoodsDao extends AbstractDao<Goods, Void> {

    public static final String TABLENAME = "GOODS";

    /**
     * Properties of entity Goods.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property G_id = new Property(0, String.class, "g_id", false, "G_ID");
        public final static Property G_img = new Property(1, String.class, "g_img", false, "G_IMG");
        public final static Property G_price = new Property(2, String.class, "g_price", false, "G_PRICE");
        public final static Property G_name = new Property(3, String.class, "g_name", false, "G_NAME");
        public final static Property Cp_name = new Property(4, String.class, "cp_name", false, "CP_NAME");
        public final static Property Cp_group = new Property(5, String.class, "cp_group", false, "CP_GROUP");
        public final static Property RowCount = new Property(6, String.class, "rowCount", false, "ROW_COUNT");
        public final static Property Vip_g_price = new Property(7, String.class, "vip_g_price", false, "VIP_G_PRICE");
        public final static Property Mall_state = new Property(8, String.class, "mall_state", false, "MALL_STATE");
        public final static Property Store_state = new Property(9, String.class, "store_state", false, "STORE_STATE");
        public final static Property Mall_id = new Property(10, String.class, "mall_id", false, "MALL_ID");
        public final static Property Bar_code = new Property(11, String.class, "bar_code", false, "BAR_CODE");
    }


    public GoodsDao(DaoConfig config) {
        super(config);
    }
    
    public GoodsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GOODS\" (" + //
                "\"G_ID\" TEXT UNIQUE ," + // 0: g_id
                "\"G_IMG\" TEXT," + // 1: g_img
                "\"G_PRICE\" TEXT," + // 2: g_price
                "\"G_NAME\" TEXT," + // 3: g_name
                "\"CP_NAME\" TEXT," + // 4: cp_name
                "\"CP_GROUP\" TEXT," + // 5: cp_group
                "\"ROW_COUNT\" TEXT," + // 6: rowCount
                "\"VIP_G_PRICE\" TEXT," + // 7: vip_g_price
                "\"MALL_STATE\" TEXT," + // 8: mall_state
                "\"STORE_STATE\" TEXT," + // 9: store_state
                "\"MALL_ID\" TEXT," + // 10: mall_id
                "\"BAR_CODE\" TEXT);"); // 11: bar_code
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GOODS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Goods entity) {
        stmt.clearBindings();
 
        String g_id = entity.getG_id();
        if (g_id != null) {
            stmt.bindString(1, g_id);
        }
 
        String g_img = entity.getG_img();
        if (g_img != null) {
            stmt.bindString(2, g_img);
        }
 
        String g_price = entity.getG_price();
        if (g_price != null) {
            stmt.bindString(3, g_price);
        }
 
        String g_name = entity.getG_name();
        if (g_name != null) {
            stmt.bindString(4, g_name);
        }
 
        String cp_name = entity.getCp_name();
        if (cp_name != null) {
            stmt.bindString(5, cp_name);
        }
 
        String cp_group = entity.getCp_group();
        if (cp_group != null) {
            stmt.bindString(6, cp_group);
        }
 
        String rowCount = entity.getRowCount();
        if (rowCount != null) {
            stmt.bindString(7, rowCount);
        }
 
        String vip_g_price = entity.getVip_g_price();
        if (vip_g_price != null) {
            stmt.bindString(8, vip_g_price);
        }
 
        String mall_state = entity.getMall_state();
        if (mall_state != null) {
            stmt.bindString(9, mall_state);
        }
 
        String store_state = entity.getStore_state();
        if (store_state != null) {
            stmt.bindString(10, store_state);
        }
 
        String mall_id = entity.getMall_id();
        if (mall_id != null) {
            stmt.bindString(11, mall_id);
        }
 
        String bar_code = entity.getBar_code();
        if (bar_code != null) {
            stmt.bindString(12, bar_code);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Goods entity) {
        stmt.clearBindings();
 
        String g_id = entity.getG_id();
        if (g_id != null) {
            stmt.bindString(1, g_id);
        }
 
        String g_img = entity.getG_img();
        if (g_img != null) {
            stmt.bindString(2, g_img);
        }
 
        String g_price = entity.getG_price();
        if (g_price != null) {
            stmt.bindString(3, g_price);
        }
 
        String g_name = entity.getG_name();
        if (g_name != null) {
            stmt.bindString(4, g_name);
        }
 
        String cp_name = entity.getCp_name();
        if (cp_name != null) {
            stmt.bindString(5, cp_name);
        }
 
        String cp_group = entity.getCp_group();
        if (cp_group != null) {
            stmt.bindString(6, cp_group);
        }
 
        String rowCount = entity.getRowCount();
        if (rowCount != null) {
            stmt.bindString(7, rowCount);
        }
 
        String vip_g_price = entity.getVip_g_price();
        if (vip_g_price != null) {
            stmt.bindString(8, vip_g_price);
        }
 
        String mall_state = entity.getMall_state();
        if (mall_state != null) {
            stmt.bindString(9, mall_state);
        }
 
        String store_state = entity.getStore_state();
        if (store_state != null) {
            stmt.bindString(10, store_state);
        }
 
        String mall_id = entity.getMall_id();
        if (mall_id != null) {
            stmt.bindString(11, mall_id);
        }
 
        String bar_code = entity.getBar_code();
        if (bar_code != null) {
            stmt.bindString(12, bar_code);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public Goods readEntity(Cursor cursor, int offset) {
        Goods entity = new Goods( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // g_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // g_img
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // g_price
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // g_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // cp_name
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // cp_group
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // rowCount
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // vip_g_price
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // mall_state
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // store_state
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // mall_id
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // bar_code
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Goods entity, int offset) {
        entity.setG_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setG_img(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setG_price(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setG_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCp_name(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCp_group(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setRowCount(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setVip_g_price(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setMall_state(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setStore_state(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setMall_id(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setBar_code(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(Goods entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(Goods entity) {
        return null;
    }

    @Override
    public boolean hasKey(Goods entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
