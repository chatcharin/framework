package com.odoo.addons.workorder.providers;

import com.odoo.addons.workorder.models.Production;
import com.odoo.addons.workorder.models.WorkOrder;
import com.odoo.core.orm.provider.BaseModelProvider;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class WorkOrderSyncProvider extends BaseModelProvider {
    public static final String TAG = WorkOrderSyncProvider.class.getSimpleName();

    @Override
    public String authority() {
        return WorkOrder.AUTHORITY;
    }
}
