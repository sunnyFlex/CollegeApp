package com.sunnyflex.collegeapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NoticeListAdapter extends BaseAdapter {

    private Context context;
    private List<Notice> noticeList;

    public NoticeListAdapter(Context context, List<Notice> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    // * 갯수는 noticeList의 size로 리턴
    @Override
    public int getCount() {
        return noticeList.size();
    }

    // * Item의 위치는 noticeList의 위치 i로 리턴
    @Override
    public Object getItem(int i) {
        return noticeList.get(i);
    }

    // * item의 id는 i 위치값 그대로 리턴
    @Override
    public long getItemId(int i) {
        return i;
    }

    // * View는 notice리스트가 들어갈 layout를 가져와서 각각의 view정의 *
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // notice list가 출력될 view 연결
        View v = View.inflate(context, R.layout.notice, null);
        // 변수와 layout연결
        TextView noticeText = v.findViewById(R.id.notice_text);
        TextView nameText = v.findViewById(R.id.name_text);
        TextView dateText = v.findViewById(R.id.date_text);

        // 각각의 textView에 Notice.java class 입력값을 가져와서 출력한다.
        noticeText.setText(noticeList.get(i).getNotice());
        nameText.setText(noticeList.get(i).getName());
        dateText.setText(noticeList.get(i).getDate());

        // 공지사항을 view의 tag로 저장한다.
        v.setTag(noticeList.get(i).getNotice());
        return v;
    }

}
