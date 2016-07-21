package com.odoo.addons.workorder;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.odoo.OnSwipeTouchListener;
import com.odoo.R;
import com.odoo.addons.customers.CustomerDetails;
import com.odoo.addons.workorder.models.WorkOrder;
import com.odoo.addons.workorder.models.Workcenter;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.orm.OValues;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.support.addons.fragment.IOnSearchViewChangeListener;
import com.odoo.core.support.addons.fragment.ISyncStatusObserverListener;
import com.odoo.core.support.drawer.ODrawerItem;
import com.odoo.core.support.list.OCursorListAdapter;
import com.odoo.core.utils.IntentUtils;
import com.odoo.core.utils.OControls;
import com.odoo.core.utils.OCursorUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by tititab on 7/15/16 AD.
 */
public class WorkOrderList extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        ISyncStatusObserverListener, SwipeRefreshLayout.OnRefreshListener,
        OCursorListAdapter.OnViewBindListener, IOnSearchViewChangeListener,
        AdapterView.OnItemClickListener {

    public static final String TAG = WorkOrderList.class.getSimpleName();

    private View mView;
    private ListView listView;
    private OCursorListAdapter listAdapter;
    private String mFilter = null;
    private String mWorkcenterFilter = null;

    private List<String> workcenters = new ArrayList<>();
    OnSwipeTouchListener onSwipeTouchListener;
    ImageView button_start;

    @Override
    public List<ODrawerItem> drawerMenus(Context context) {
        List<ODrawerItem> menu = new ArrayList<>();
        menu.add(new ODrawerItem(TAG).setTitle("Work Orders").setInstance(new WorkOrderList()));
        return menu;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //onSwipeTouchListener = new OnSwipeTouchListener();
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.workorder_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_partners, menu);
        setHasSearchView(this, menu, R.id.menu_partner_search);
    }

    @Override
    public Class<WorkOrder> database() {
        return WorkOrder.class;
    }

    @Override
    public void onStatusChange(Boolean refreshing) {
        if(refreshing){
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String where = "";
        List<String> params = new ArrayList<>();
        if (mWorkcenterFilter != null) {
            //Log.i(TAG, "onCreateLoader: "+mWorkcenterFilter+" Values");
            where += "workcenter_name = ? " ;
            params.add(mWorkcenterFilter) ;
            where += " and state != ?" ;
            params.add("done") ;
            where += " and state != ?" ;
            params.add("cancel") ;
        }
        if (mFilter != null) {
            where += " and name like ? ";
            params.add("%" + mFilter + "%");
        }
        String selection = (params.size() > 0) ? where : null;
        String[] selectionArgs = (params.size() > 0) ? params.toArray(new String[params.size()]) : null;
        return new CursorLoader(getActivity(), db().uri(),
                null, selection, selectionArgs, "name");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listAdapter.changeCursor(data);
        if (data.getCount() > 0) {
            OControls.setGone(mView, R.id.loadingProgress);
            OControls.setVisible(mView, R.id.swipe_container);
            setHasSwipeRefreshView(mView, R.id.swipe_container, this);
        } else {
            OControls.setGone(mView, R.id.loadingProgress);
            OControls.setGone(mView, R.id.swipe_container);
            setHasSwipeRefreshView(mView, R.id.swipe_container, this);
        }
        if (db().isEmptyTable()) {
            onRefresh();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listAdapter.changeCursor(null);
    }

    @Override
    public void onRefresh() {
        if (inNetwork()) {
            parent().sync().requestSync(WorkOrder.AUTHORITY);
        }
    }

    @Override
    public void onViewBind(View view, Cursor cursor, ODataRow row) {
        OControls.setText(view, R.id.workorder_name, row.getString("name_full"));
        //OControls.setText(view, R.id.production_name, row.getString("production_name"));
        OControls.setText(view, R.id.production_qty, "Total : " + row.getString("cycle") +" Units" );
        ImageView button_start = (ImageView) view.findViewById(R.id.button_start) ;
        ImageView button_done = (ImageView) view.findViewById(R.id.button_done) ;
        button_start.setTag(row.getInt("_id")) ;
        button_done.setTag(row.getInt("_id")) ;
        button_start.setVisibility(View.INVISIBLE);
        button_done.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        workcenters.clear() ;
        List<ODataRow> workcenterRows = new Workcenter(getContext(), null).select() ;
        for (ODataRow row : workcenterRows ) {
            workcenters.add(row.getString("name"));
        }
        Spinner workcenterSpinner = (Spinner) mView.findViewById(R.id.workcenter_spinner) ;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, workcenters);
        workcenterSpinner.setAdapter(adapter);
        workcenterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Object item = parentView.getItemAtPosition(position);
                mWorkcenterFilter = item.toString();
                //Log.i(TAG, "onItemSelected: "+item.toString());
                getLoaderManager().restartLoader(0, null, WorkOrderList.this);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //WorkcenterFilter = null ;
            }
        });
        listView = (ListView) mView.findViewById(R.id.workorderList);
        listAdapter = new OCursorListAdapter(getActivity(), null, R.layout.workorder_list_item);
        listAdapter.setHasSectionIndexers(true, "name");
        listView.setAdapter(listAdapter);
        listAdapter.setOnViewBindListener(this);
        listView.setFastScrollAlwaysVisible(true);
        listView.setOnItemClickListener(this);
        setHasSyncStatusObserver(TAG, this, db());
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onSearchViewTextChange(String newFilter) {
        mFilter = newFilter;
        getLoaderManager().restartLoader(0, null, this);
        return true;
    }

    @Override
    public void onSearchViewClose() {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Log.i(TAG, "onItemClick: ");
        ODataRow row = OCursorUtils.toDatarow((Cursor) listAdapter.getItem(i));
        ImageView button_start = (ImageView)view.findViewById(R.id.button_start) ;
        ImageView button_done = (ImageView)view.findViewById(R.id.button_done) ;
        //Log.i(TAG, "onItemClick: "+row.getString("state"));
        if (row.getString("state").equals("draft")) {
            button_start.setVisibility(View.VISIBLE) ;
            button_done.setVisibility(View.INVISIBLE) ;
        } else {
            button_start.setVisibility(View.INVISIBLE) ;
            button_done.setVisibility(View.VISIBLE) ;
        } ;

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkOrder order = new WorkOrder(getContext(), null) ;
                OValues values = new OValues() ;
                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                DateFormat df = DateFormat.getTimeInstance();
                df.setTimeZone(TimeZone.getTimeZone("gmt"));
                String gmtTime = df.format(new Date());
                values.put("date_start", currentDateandTime+ " " +gmtTime);
                values.put("state","startworking") ;
                order.update((int)view.getTag(), values) ;
                view.setVisibility(View.INVISIBLE) ;
                getLoaderManager().restartLoader(0, null, WorkOrderList.this);
            }
        });
        button_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkOrder order = new WorkOrder(getContext(), null) ;
                OValues values = new OValues() ;
                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                DateFormat df = DateFormat.getTimeInstance();
                df.setTimeZone(TimeZone.getTimeZone("gmt"));
                String gmtTime = df.format(new Date());
                values.put("date_finished", currentDateandTime+ " " +gmtTime);
                values.put("state","done") ;
                order.update((int)view.getTag(), values) ;
                view.setVisibility(View.INVISIBLE) ;
                getLoaderManager().restartLoader(0, null, WorkOrderList.this);
            }
        });
    }
}