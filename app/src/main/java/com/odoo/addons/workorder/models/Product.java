package com.odoo.addons.workorder.models;

import android.content.Context;
import android.net.Uri;

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

    OColumn name = new OColumn("Name", OVarchar.class).setSize(100);

    public Product(Context context, OUser user) {
        super(context, "product.product", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }

}
