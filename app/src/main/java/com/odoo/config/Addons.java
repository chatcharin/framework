/**
 * Odoo, Open Source Management Solution
 * Copyright (C) 2012-today Odoo SA (<http:www.odoo.com>)
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details
 * <p/>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:www.gnu.org/licenses/>
 * <p/>
 * Created on 30/12/14 3:11 PM
 */
package com.odoo.config;

import com.odoo.addons.customers.Customers;
import com.odoo.addons.workorder.ProductList;
import com.odoo.addons.workorder.ProductionList;
import com.odoo.addons.workorder.UomList;
import com.odoo.addons.workorder.WorkcenterList;
import com.odoo.addons.workorder.models.Product;
import com.odoo.addons.workorder.models.Workcenter;
import com.odoo.core.support.addons.AddonsHelper;
import com.odoo.core.support.addons.OAddon;

public class Addons extends AddonsHelper {
    OAddon workcenters = new OAddon(WorkcenterList.class).setDefault();
    OAddon products = new OAddon(ProductList.class) ;
    OAddon uoms = new OAddon(UomList.class);
    OAddon productions = new OAddon(ProductionList.class);
}
