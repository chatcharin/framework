package com.odoo.addons.workorder.providers;

import com.odoo.addons.workorder.models.Production;
import com.odoo.core.orm.provider.BaseModelProvider;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class ProductionSyncProvider extends BaseModelProvider {
    public static final String TAG = ProductionSyncProvider.class.getSimpleName();

    @Override
    public String authority() {
        return Production.AUTHORITY;
    }
}
