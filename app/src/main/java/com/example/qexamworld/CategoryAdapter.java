package com.example.qexamworld;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.qexamworld.Models.CategoryModel;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<CategoryModel> cate_list;

    public CategoryAdapter(List<CategoryModel> cate_list) {
        this.cate_list = cate_list;
    }

    @Override
    public int getCount() {
        return cate_list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        View myview;

        if(view == null){

            myview= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);

        }else {

            myview = view;
        }

        myview.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                DbQuery.g_selected = i;

                Intent intent = new Intent(view.getContext(), TestActivity.class);

                view.getContext().startActivity(intent);
            }
        });

        TextView catName = myview.findViewById(R.id.catname);
        TextView noTest = myview.findViewById(R.id.notest);

        catName.setText(cate_list.get(i).getName());
        noTest.setText(String.valueOf(cate_list.get(i).getNoOfTests()) + " Tests");

        return myview;
    }
}
