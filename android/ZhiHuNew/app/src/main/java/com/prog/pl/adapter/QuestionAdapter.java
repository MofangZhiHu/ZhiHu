package com.prog.pl.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.prog.pl.bean.Question;
import com.prog.pl.zhihunew.R;

/**
 *
 */
public class QuestionAdapter extends BaseAdapter {

    private boolean checkVisi;
    // 填充数据的list
    private List<Question> list;
    // 上下文
    private Context context;
    // 用来导入布局
    private LayoutInflater inflater = null;
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    public QuestionAdapter(Context context, List<Question> queList) {
        this.context = context;
        this.list = queList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Question getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        // View view = null;
        final ViewHolder holder;
        if (convertView == null) {
            // 获得ViewHolder对象
            holder = new ViewHolder();
            // 导入布局并赋值给convertview
            convertView = inflater.inflate(R.layout.item_question, parent,
                    false);

            holder.sourceTextView = (TextView) convertView
                    .findViewById(R.id.text_questionitem_source);
            holder.questionTextView = (TextView) convertView
                    .findViewById(R.id.text_questionitem_question);
            holder.answerTextView = (TextView) convertView
                    .findViewById(R.id.text_questionitem_answer);
            holder.lastTextView = (TextView) convertView
                    .findViewById(R.id.text_questionitem_last);

            // 为view设置标签
            convertView.setTag(holder);
        } else {
            // 取出holder
            holder = (ViewHolder) convertView.getTag();
        }

        // 设置list中TextView的显示
        holder.sourceTextView.setText(list.get(position).getQuestionuser());
        holder.questionTextView.setText(list.get(position).getQuestion());
        holder.answerTextView.setText(list.get(position).getAnswer());
        holder.lastTextView.setText(list.get(position).getSupport()+"评论");
        return convertView;
    }


    public class ViewHolder {
        public TextView getSourceTextView() {
            return sourceTextView;
        }

        public void setSourceTextView(TextView sourceTextView) {
            this.sourceTextView = sourceTextView;
        }

        TextView sourceTextView;

        public TextView getQuestionTextView() {
            return questionTextView;
        }

        public void setQuestionTextView(TextView questionTextView) {
            this.questionTextView = questionTextView;
        }

        TextView questionTextView;

        public TextView getAnswerTextView() {
            return answerTextView;
        }

        public void setAnswerTextView(TextView answerTextView) {
            this.answerTextView = answerTextView;
        }

        TextView answerTextView;

        public TextView getTextView() {
            return TextView;
        }

        public void setTextView(TextView textView) {
            TextView = textView;
        }

        TextView TextView;

        public TextView getLastTextView() {
            return lastTextView;
        }

        public void setLastTextView(TextView lastTextView) {
            this.lastTextView = lastTextView;
        }

        TextView lastTextView;

    }
}