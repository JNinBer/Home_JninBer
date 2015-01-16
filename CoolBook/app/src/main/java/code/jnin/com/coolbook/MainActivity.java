package code.jnin.com.coolbook;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.drakeet.materialdialog.MaterialDialog;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        if (id==R.id.dialog){
            createDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void createDialog(){

        final MaterialDialog dialog =new MaterialDialog(this);
        dialog.setTitle("提示");
        dialog.setMessage("这就是个掩饰的对话框 ，就是个掩饰的对话框 请不要当真这就是个掩饰的对话框 ，请不要当真这就是个掩饰的对话框 ，请不要当真这就是个掩饰的对话框 ，请不要当真这就是个掩饰的对话框 ，请不要当真这就是个掩饰的对话框 ，请不要当真");
        dialog.setNegativeButton("取消",new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        dialog.setPositiveButton("确定",new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        /**
         * rootView
         */
        @InjectView(R.id.list_string)
        ListView mListView;
        @InjectView(R.id.refresh_layout)
        SwipeRefreshLayout swipeRefreshLayout;
        private Handler handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                swipeRefreshLayout.setRefreshing(false);
                super.handleMessage(msg);
            }
        };

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.inject(this,rootView);
            initView();
            return rootView;
        }
        private void initView(){
            String[] list=this.getActivity().getResources().getStringArray(R.array.list);
            mListView.setAdapter(new SimAdapter<String>(this.getActivity(),list));
            swipeRefreshLayout.setColorSchemeColors(R.color.red,R.color.orange,R.color.amber,R.color.blue_grey);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                  new Thread(new Runnable() {
                      @Override
                      public void run() {
                          try {
                              Thread.sleep(5000);
                              handler.sendEmptyMessage(100);
                          } catch (InterruptedException e) {
                              e.printStackTrace();
                          }

                      }
                  }).start();
                }
            });
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    public static class SimAdapter<T> extends BaseAdapter {
        private Context context;
        private T[] list;
        private LayoutInflater inflater;

        public SimAdapter(Context context, T[] list) {
            this.list = list;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.length;
        }

        @Override
        public T getItem(int i) {
            return list[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view != null) {
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.list_item, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            }
            TextDrawable drawable = TextDrawable.builder().buildRound(getIndexStr(list[i].toString()), getColor());
            viewHolder.mImageIcon.setImageDrawable(drawable);
            viewHolder.mTextTitle.setText(getItem(i).toString());
            return view;
        }

        private String getIndexStr(String str) {
            if (TextUtils.isEmpty(str)) {
                return "";
            } else {
                return str.substring(0, 1);
            }
        }


        private int getColor() {
            int x = (int) (Math.random() * 5);
            int[] color = {R.color.red,R.color.blue,R.color.blue_grey
                    ,R.color.yellow,R.color.lime,R.color.teal
                    ,R.color.grey,R.color.light_green,R.color.light_blue
                    ,R.color.orange,R.color.deep_orange,R.color.inigo
                    ,R.color.pink,R.color.purple,R.color.deer_purple
            };
            return color[x];
        }

        class ViewHolder {
            @InjectView(R.id.im_text_icon)
            ImageView mImageIcon;
            @InjectView(R.id.tc_title)
            TextView mTextTitle;

            public ViewHolder(View view) {
                ButterKnife.inject(this,view);
            }
        }
    }

}
