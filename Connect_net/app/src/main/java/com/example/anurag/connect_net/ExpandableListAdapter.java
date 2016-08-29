package com.example.anurag.connect_net;

/**
 * Created by kapil on 26/6/16.
 */
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
private  boolean FAQlist=false;
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private    HashMap<String,List<genericList<String,Integer>>> _listDataChild;
private Fragment fragment;
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String,List<genericList<String,Integer>>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String,List<genericList<String,Integer>>> listChildData,Fragment fragment) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.fragment=fragment;
      if(  fragment instanceof FAQExpandableListFragment)
      {FAQlist=true;}
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon).first;
    }
    private  int getLectureId(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosititon).second;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(FAQlist)
                convertView = infalInflater.inflate(R.layout.faq_child_list_view, null);
            else
            convertView = infalInflater.inflate(R.layout.child_list_view, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //here to change view on click
                if(FAQlist) {
                    ((Faqs) ((FAQExpandableListFragment) fragment).getParentFragment())
                            .onLectureSelection(getLectureId(groupPosition, childPosition));
                }else
                {
                    ((Learning) ((ExpandableListFragment) fragment).getParentFragment())
                            .onLectureSelection(getLectureId(groupPosition, childPosition));

                }
            }
        });

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(FAQlist)
                convertView = infalInflater.inflate(R.layout.faq_list_items, null);
            else
            convertView = infalInflater.inflate(R.layout.list_items, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}