package com.odoo.addons.workorder.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.workorder.models.Product;
import com.odoo.addons.workorder.models.Uom;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

import odoo.helper.ODomain;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class UomSyncService extends OSyncService {
    public static final String TAG = UomSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(getApplicationContext(), Uom.class, this, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        adapter.syncDataLimit(80);
    }
}
