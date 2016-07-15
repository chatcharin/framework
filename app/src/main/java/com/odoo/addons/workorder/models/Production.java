package com.odoo.addons.workorder.models;

import android.content.Context;

import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OInteger;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

/**
 * Created by tititab on 7/15/16 AD.
 */
public class Production extends OModel {

    public static final String TAG = Production.class.getSimpleName();
    public static final String AUTHORITY = "com.odoo.addons.workorder.production";

    OColumn name = new OColumn("Name", OVarchar.class) ;
    OColumn state = new OColumn("State", OVarchar.class) ;
    OColumn product_id = new OColumn("Product", Product.class, OColumn.RelationType.ManyToOne) ;
    OColumn product_qty = new OColumn("Product Qty", OInteger.class) ;
    OColumn product_uom = new OColumn("UOM", Uom.class, OColumn.RelationType.ManyToOne) ;

    public Production(Context context, OUser user) {
        super(context, "mrp.production", user);
    }

    public static String getName(Context context, int row_id) {
        Production data = new Production(context, null);
        ODataRow row = data.browse(row_id);
        return (row != null) ? row.getString("name") : "" ;
    }

    public static String getProductName(Context context, int row_id) {
        Production data = new Production(context, null);
        ODataRow row = data.browse(row_id);
        String productName = "" ;
        if (!row.getInt("product_id").equals(false)){
            Product product = new Product(context, null);
            productName = product.getName(row.getInt("product_id")) ;
        }
        return (row != null) ? productName : "" ;
    }

    public static String getUomName(Context context, int row_id) {
        Production data = new Production(context, null);
        ODataRow row = data.browse(row_id);
        String uomName = "" ;
        if (!row.getInt("product_uom").equals(false)){
            Uom uom = new Uom(context, null);
            uomName = uom.getName(row.getInt("product_uom")) ;
        }
        return (row != null) ? uomName : "" ;
    }

}
