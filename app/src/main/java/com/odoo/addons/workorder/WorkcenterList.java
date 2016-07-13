package com.odoo.addons.workorder;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.odoo.R;
import com.odoo.addons.workorder.models.Workcenter;
import com.odoo.core.orm.ODataRow;
import com.odoo.core.support.addons.fragment.BaseFragment;
import com.odoo.core.support.addons.fragment.ISyncStatusObserverListener;
import com.odoo.core.support.drawer.ODrawerItem;
import com.odoo.core.support.list.OCursorListAdapter;
import com.odoo.core.utils.OControls;
import com.odoo.core.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tititabs on 7/13/2016 AD.
 */
public class WorkcenterList extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>,
        ISyncStatusObserverListener, SwipeRefreshLayout.OnRefreshListener,
        OCursorListAdapter.OnViewBindListener{

    public static final String TAG = WorkcenterList.class.getSimpleName();

    private View mView;
    private ListView listView;
    private OCursorListAdapter listAdapter;


    @Override
    public List<ODrawerItem> drawerMenus(Context context) {
        List<ODrawerItem> menu = new ArrayList<>();
        menu.add(new ODrawerItem(TAG).setTitle("Workcenters").setInstance(new WorkcenterList()));
        return menu;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.workcenter_list, container, false);
        //return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mView = view;
        listView = (ListView) mView.findViewById(R.id.workcenterList);
        //listAdapter = new OCursorListAdapter(getActivity(), null, android.R.layout.simple_list_item_1);
        listAdapter = new OCursorListAdapter(getActivity(), null, R.layout.workcenter_list_item);
        listView.setAdapter(listAdapter);

        listAdapter.setOnViewBindListener(this);
        setHasSyncStatusObserver(TAG, this, db());

        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public Class<Workcenter> database() {
        return Workcenter.class;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), db().uri(), null, null, null, "name");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        listAdapter.changeCursor(data);
        if (data.getCount() > 0) {
            OControls.setGone(mView, R.id.loadingProgress);
            OControls.setVisible(mView, R.id.swipe_container);
            //OControls.setGone(mView, R.id.no_items);
            setHasSwipeRefreshView(mView, R.id.swipe_container, this);
        } else {
            OControls.setGone(mView, R.id.loadingProgress);
            OControls.setGone(mView, R.id.swipe_container);
            //OControls.setVisible(mView, R.id.no_items);
            setHasSwipeRefreshView(mView, R.id.swipe_container, this);
            //OControls.setText(mView, R.id.title, "No Tasks found");
            //OControls.setText(mView, R.id.subTitle, "Swipe to check new tasks");
        }
        if (db().isEmptyTable()) {
            // Request for sync
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        if (inNetwork()) {
            parent().sync().requestSync(Workcenter.AUTHORITY);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listAdapter.changeCursor(null);
    }

    @Override
    public void onStatusChange(Boolean refreshing) {
        if(refreshing){
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onViewBind(View view, Cursor cursor, ODataRow row) {
        //OControls.setText(view, android.R.id.text1, row.getString("name"));
        OControls.setText(view, R.id.workcenter_name, row.getString("name"));
        //OControls.setText(view, R.id.message, StringUtils.htmlToString(row.getString("message")));

    }
}
