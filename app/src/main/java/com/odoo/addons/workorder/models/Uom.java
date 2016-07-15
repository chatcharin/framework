package com.odoo.addons.workorder.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.base.addons.res.ResCurrency;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;

/**
 * Created by tititab on 7/14/16 AD.
 */
public class Uom extends OModel {
    public static final String TAG = Uom.class.getSimpleName();
    public static final String AUTHORITY = "com.odoo.addons.workorder.uom";

    OColumn name = new OColumn("Name", OVarchar.class).setSize(100);

    public Uom(Context context, OUser user) {
        super(context, "product.uom", user);
    }

    public static String getName(Context context, int row_id) {
        Uom uom = new Uom(context, null);
        ODataRow row = uom.browse(row_id);
        return (row != null) ? row.getString("name") : "" ;
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }
}
