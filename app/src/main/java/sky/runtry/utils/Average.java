package sky.runtry.utils;

/**
 * Created by pruthvishpatel on 11/9/17.
 */

public class Average {

    private short count = 0;
    private short limit = 0;
    private float sum = 0;

    public Average(short limit) {
        this.limit = limit;
    }

    public float add(float value){
        count++;

        this.sum += value;

        float ret = (sum/count);

        if(this.count == this.limit)
            reset(ret);

        return ret;
    }

    public float get(){
        return sum/count;
    }

    private void reset(float average){
        this.count = 1;
        this.sum = average;
    }

}
