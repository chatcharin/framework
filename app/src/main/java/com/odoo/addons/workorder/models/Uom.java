package com.odoo.addons.workorder.models;

import android.content.Context;
import android.net.Uri;

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

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }
}
