import java.util.concurrent.Semaphore;

public class Correio {

    public static int QTD_CARTAS            = 20;
    public static int QTD_PESSOAS           = 10;
    public static volatile int countCartas  = 0;

    public static void main(String[] args) {
        Semaphore mutex             = new Semaphore(1);
        Semaphore controlaCartas    = new Semaphore(QTD_CARTAS+1);
        Semaphore bloqueiaCarteiro  = new Semaphore(0);
        Semaphore bloqueiaPessoa    = new Semaphore(0);

        Thread tCarteiro = new Thread(new Carteiro(controlaCartas, bloqueiaCarteiro, bloqueiaPessoa));
        tCarteiro.start();

        for (int i = 1; i <= QTD_PESSOAS; i++) {
            Thread tPessoa = new Thread(new Pessoa(i, mutex, controlaCartas, bloqueiaCarteiro, bloqueiaPessoa));
            tPessoa.start();
        }
    }

    static public class Pessoa implements Runnable{

        private int tid;
        Semaphore mutex;
        Semaphore cCartas;
        Semaphore bCarteiro;
        Semaphore bPessoa;

        public Pessoa(int tid, Semaphore mutex, Semaphore cCartas, Semaphore bCarteiro, Semaphore bPessoa) {
            this.tid        = tid;
            this.mutex      = mutex;
            this.cCartas    = cCartas;
            this.bCarteiro  = bCarteiro;
            this.bPessoa    = bPessoa;
        }

        @Override
        public void run(){
            while (true){
                try{
                    cCartas.acquire();
                    mutex.acquire();
                    if(countCartas == QTD_CARTAS){
                        System.out.println("Acordando o carteiro!");
                        bCarteiro.release();
                        bPessoa.acquire();
                    }
                    countCartas++;
                    System.out.println("Pessoa posta carta! São " + countCartas + " cartas no momento.");
                    Thread.sleep(200);
                    mutex.release();
                }
                catch (Exception e){
                    return;
                }
            }
        }

    }

    static public class Carteiro implements Runnable{

        Semaphore cCartas;
        Semaphore bCarteiro;
        Semaphore bPessoa;

        public Carteiro( Semaphore cCartas, Semaphore bCarteiro, Semaphore bPessoa) {
            this.cCartas    = cCartas;
            this.bCarteiro  = bCarteiro;
            this.bPessoa    = bPessoa;
        }

        @Override
        public void run(){
            while (true){
                try{
                    bCarteiro.acquire();
                    countCartas = 0;
                    System.out.println("Carteiro vai entregar!");
                    for (int i = 0; i < QTD_CARTAS; i++) {
                        cCartas.release();
                    }
                    bPessoa.release();
                }
                catch (Exception e){
                    return;
                }
            }
        }

    }
}
