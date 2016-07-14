package com.odoo.addons.workorder.providers;

import com.odoo.addons.workorder.models.Product;
import com.odoo.addons.workorder.models.Uom;
import com.odoo.core.orm.provider.BaseModelProvider;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class UomSyncProvider extends BaseModelProvider {
    public static final String TAG = UomSyncProvider.class.getSimpleName();

    @Override
    public String authority() {
        return Uom.AUTHORITY;
    }
}
