package com.odoo.addons.workorder.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.workorder.models.Production;
import com.odoo.addons.workorder.models.WorkOrder;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

import java.util.ArrayList;
import java.util.List;

import odoo.helper.ODomain;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class WorkOrderSyncService extends OSyncService {
    public static final String TAG = WorkOrderSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(getApplicationContext(), WorkOrder.class, this, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        if (adapter.getModel().getModelName().equals("mrp.production.workcenter.line")) {
            List<String> stateIds = new ArrayList<>();
            stateIds.add("done");
            stateIds.add("cancel") ;
            ODomain domain = new ODomain();
            domain.add("state", "not in", stateIds);
            adapter.setDomain(domain).syncDataLimit(80);
        }
    }
}
