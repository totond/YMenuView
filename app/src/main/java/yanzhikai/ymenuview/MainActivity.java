package yanzhikai.ymenuview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yanzhikai.ymenuview.YMenuView;

public class MainActivity extends AppCompatActivity implements YMenuView.OnOptionsClickListener{
    private YMenuView mYMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mYMenuView = (YMenuView) findViewById(R.id.ymv);
        mYMenuView.setOnOptionsClickListener(this);
//        mYMenuView.setBanArray(0,2,6);
//              mYMenuView.setOptionDrawableIds(R.drawable.zero,R.drawable.one,R.drawable.two
//                ,R.drawable.three,R.drawable.four);
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
        }
    }


    private void makeToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
