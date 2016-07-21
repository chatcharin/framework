package com.odoo.addons.workorder.models;

import android.content.Context;
import android.net.Uri;

import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OModel;
import com.odoo.core.orm.OValues;
import com.odoo.core.orm.annotation.Odoo;
import com.odoo.core.orm.fields.OColumn;
import com.odoo.core.orm.fields.types.ODateTime;
import com.odoo.core.orm.fields.types.OFloat;
import com.odoo.core.orm.fields.types.OInteger;
import com.odoo.core.orm.fields.types.OVarchar;
import com.odoo.core.support.OUser;
import com.odoo.core.utils.OControls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tititab on 7/15/16 AD.
 */
public class WorkOrder extends OModel {
    public static final String TAG = WorkOrder.class.getSimpleName();
    public static final String AUTHORITY = "com.odoo.addons.workorder.workorder";

    OColumn name = new OColumn("Name", OVarchar.class) ;
    OColumn product_id = new OColumn("Product", Product.class, OColumn.RelationType.ManyToOne);
    @Odoo.Functional(store = true, depends = {"product_id"}, method = "storeProduct")
    OColumn product_name = new OColumn("Product Name", OVarchar.class)
            .setLocalColumn();

    OColumn production_id = new OColumn("Production", Production.class, OColumn.RelationType.ManyToOne);
    @Odoo.Functional(store = true, depends = {"production_id"}, method = "storeProduction")
    OColumn production_name = new OColumn("Production Name", OVarchar.class).setSize(100)
            .setLocalColumn();
    OColumn workcenter_id = new OColumn("Workcenter", Workcenter.class, OColumn.RelationType.ManyToOne);
    @Odoo.Functional(store = true, depends = {"workcenter_id"}, method = "storeWorkcenter")
    OColumn workcenter_name = new OColumn("Workcenter Name", OVarchar.class).setSize(100)
            .setLocalColumn();
    OColumn sequence = new OColumn("Sequence", OInteger.class);
    OColumn state = new OColumn("State", OVarchar.class);
    OColumn cycle = new OColumn("Cycle", OFloat.class);
    OColumn qty = new OColumn("Qty", OFloat.class);
    OColumn date_start = new OColumn("Date Start", ODateTime.class);
    OColumn date_finished = new OColumn("Date Finished", ODateTime.class);

    OColumn name_full = new OColumn("Name Fully", OVarchar.class) ;

    public String storeWorkcenter(OValues value) {
        try {
            if (!value.getString("workcenter_id").equals("false")) {
                List<Object> workcenter_id = (ArrayList<Object>) value.get("workcenter_id");
                return workcenter_id.get(1) + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String storeProduction(OValues value) {
        try {
            if (!value.getString("production_id").equals("false")) {
                List<Object> data = (ArrayList<Object>) value.get("production_id");
                return data.get(1) + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String storeProduct(OValues value) {
        try {
            if (!value.getString("product_id").equals("false")) {
                List<Object> data = (ArrayList<Object>) value.get("product_id");
                return data.get(1) + "";
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return "";
    }

    public WorkOrder(Context context, OUser user) {
        super(context, "mrp.production.workcenter.line", user);
    }

    @Override
    public Uri uri() {
        return buildURI(AUTHORITY);
    }

    public static String getName(Context context, int row_id) {
        WorkOrder data = new WorkOrder(context, null);
        ODataRow row = data.browse(row_id);
        return (row != null) ? row.getString("name") : "" ;
    }
}
