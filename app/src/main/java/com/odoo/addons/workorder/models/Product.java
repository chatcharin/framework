package com.odoo.addons.workorder.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class Product extends OModel {
    public static final String TAG = Workcenter.class.getSimpleName();
    public static final String AUTHORITY = "com.odoo.addons.workorder.product";

    OColumn name = new OColumn("Name", OVarchar.class);
    OColumn uom_id = new OColumn("Uom", Uom.class, OColumn.RelationType.ManyToOne);

    public Product(Context context, OUser user) {
        super(context, "product.product", user);
    }

    public static String getName(Context context, int row_id) {
        Product data = new Product(context, null);
        ODataRow row = data.browse(row_id);
        return (row != null) ? row.getString("name") : "" ;
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }

}
