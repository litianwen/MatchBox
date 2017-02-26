package com.example.administrator.matchbox.ui.fragment.tab.message;

import android.content.Intent;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.matchbox.R;
import com.example.administrator.matchbox.adapter.ConversationAdapter;
import com.example.administrator.matchbox.base.BaseFragment;
import com.example.administrator.matchbox.bean.UserBean;
import com.example.administrator.matchbox.ui.activity.ChatActivity;
import com.example.administrator.matchbox.utils.EmUserUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.domain.EaseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/14.
 */

public class PrivateFragment extends BaseFragment {
    List<EMConversation> conversationList = new ArrayList<>();
    @BindView(R.id.lv_content)
    ListView lvContent;

    ConversationAdapter adapter;


    @Override
    protected void initView() {
        adapter = new ConversationAdapter(getContext(), conversationList);
        lvContent.setAdapter(adapter);
        lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser easeUser = EmUserUtils.getInstance().getUser(conversationList.get(position).getUserName());
                UserBean userBean = new UserBean();
                userBean.setUserId(Integer.parseInt(conversationList.get(position).getUserName()));
                userBean.setUrl(easeUser.getAvatar());
                userBean.setUserName(easeUser.getNickname());
                startActivity(new Intent(getContext(), ChatActivity.class).putExtra("userBean", userBean));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        adapter.notifyDataSetChanged();
    }

    private void initData() {
        conversationList.clear();
        conversationList.addAll(loadConversationList());

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_private;
    }


    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

}
