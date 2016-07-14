package com.odoo.addons.workorder.providers;

import com.odoo.addons.workorder.models.Product;
import com.odoo.core.orm.provider.BaseModelProvider;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class ProductSyncProvider extends BaseModelProvider {
    public static final String TAG = ProductSyncProvider.class.getSimpleName();

    @Override
    public String authority() {
        return Product.AUTHORITY;
    }
}
