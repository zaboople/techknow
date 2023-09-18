import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

ArrayBlockingQueue abq = new ArrayBlockingQueue(125);
Integer maxCount = 10000;
Set waiting = (1..maxCount).toSet();
Thread th = new Thread() {
    public void run() {
        List found = [];
        Integer jj = -1000;
        while (jj!=null) {
            jj=abq.poll(2, TimeUnit.SECONDS);
            if (jj!=null) {
                waiting.remove(jj);
                found.add(jj);
                System.out.print(jj + " ");
            }
        }
        Integer last=0;
        println("\n\nWaiting: "+waiting.size());
        println("Found: "+found.size());
    }
}
th.start();
Random random = new Random(System.currentTimeMillis());
for (int i=0; i<maxCount; i++) {
    //int r = random.nextInt(10);
    //Thread.sleep(1);
    abq.offer(i+1, 999, TimeUnit.NANOSECONDS);
}
System.out.println("\n\nFinished submitting...\n\n");