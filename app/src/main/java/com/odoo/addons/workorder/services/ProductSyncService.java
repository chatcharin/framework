package com.odoo.addons.workorder.services;

import android.content.Context;
import android.os.Bundle;

import com.odoo.addons.workorder.models.Product;
import com.odoo.addons.workorder.models.Workcenter;
import com.odoo.core.service.OSyncAdapter;
import com.odoo.core.service.OSyncService;
import com.odoo.core.support.OUser;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class ProductSyncService extends OSyncService {
    public static final String TAG = ProductSyncService.class.getSimpleName();

    @Override
    public OSyncAdapter getSyncAdapter(OSyncService service, Context context) {
        return new OSyncAdapter(getApplicationContext(), Product.class, this, true);
    }

    @Override
    public void performDataSync(OSyncAdapter adapter, Bundle extras, OUser user) {
        adapter.syncDataLimit(80);
    }
}
