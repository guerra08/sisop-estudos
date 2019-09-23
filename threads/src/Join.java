import java.time.LocalTime;
import java.util.concurrent.Semaphore;

public class Join {

    public static int QTD_TD = 10;static volatile int aux = 20;

    public static void main(String[] args) throws InterruptedException {
        Semaphore mutex = new Semaphore(1);

        for (int i = 1; i <= QTD_TD; i++) {
            Thread t = new Thread(new T(i,mutex));
            t.start();
            t.join();
        }

    }

    static class T implements Runnable{
        private int tid;
        Semaphore mutex;

        public T(int tid, Semaphore mutex) {
            this.tid = tid;
            this.mutex = mutex;
        }

        @Override
        public void run(){
            while (true && aux > 0){
                try{
                    mutex.acquire();
                    aux --;
                    System.out.println("TID " + tid + " - agora são " + LocalTime.now().toString());
                    Thread.sleep(100);
                    mutex.release();
                }catch (Exception e){
                    return;
                }
            }
        }
    }

}
