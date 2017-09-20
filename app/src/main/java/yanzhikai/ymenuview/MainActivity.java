package yanzhikai.ymenuview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.yanzhikai.ymenuview.YMenu;

public class MainActivity extends AppCompatActivity implements YMenu.OnOptionsClickListener{
    private YMenu mYMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mYMenu = (YMenu) findViewById(R.id.ymv);
        mYMenu.setOnOptionsClickListener(this);
        mYMenu.setBanArray(3,4,7,5,6);
        mYMenu.setOptionDrawableIds(R.drawable.zero,R.drawable.one,R.drawable.two
                ,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six
                ,R.drawable.seven,R.drawable.eight);
//        mYMenu.setMenuOpenAnimation(null);
//        mYMenu.setMenuCloseAnimation(null);
    }


    @Override
    public void onOptionsClick(int index) {
        switch (index){
            case 0:
                makeToast("0");
                break;
            case 1:
                makeToast("1");
                break;
            case 2:
                makeToast("2");
                break;
            case 3:
                makeToast("3");
                break;
            case 4:
                makeToast("4");
                break;
            case 5:
                makeToast("5");
                break;
            case 6:
                makeToast("6");
                break;
            case 7:
                makeToast("7");
                break;
            case 8:
                makeToast("8");
                break;

        }
    }


    private void makeToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
