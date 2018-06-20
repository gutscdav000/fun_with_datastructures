public class CountKs {
    int key;
    int count;

    public CountKs(int key){
        this.key = key;
        this.count = 0;
    }

    public CountKs(int key, int count){
        this.key = key;
        this.count = count;
    }

    public int getCount() {return this.count;}

    public void setCount(int s) {this.count = s;}

    public int getKey() {return this.key;}

    public void setKey(int key) {this.key = key;}

    public void incrementCount() {this.count += 1;}

    public int compareTo(CountKs that) {
        int c = that.getCount();
        if(this.count > c) return this.count - c;
        else if(this.count < c) return c - this.count;
        else return 0;
    }
}