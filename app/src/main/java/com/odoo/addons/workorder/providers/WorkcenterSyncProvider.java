package com.odoo.addons.workorder.providers;

import com.odoo.addons.workorder.models.Workcenter;
import com.odoo.core.orm.provider.BaseModelProvider;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class WorkcenterSyncProvider extends BaseModelProvider {
    public static final String TAG = WorkcenterSyncProvider.class.getSimpleName();

    @Override
    public String authority() {
        return Workcenter.AUTHORITY;
    }
}
