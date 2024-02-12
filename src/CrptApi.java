import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CrptApi {

    private final Semaphore semaphore;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.semaphore = new Semaphore(requestLimit);
        // Устанавливаем ограничение на количество запросов в определенный интервал времени
        long intervalMillis = timeUnit.toMillis(1);
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(intervalMillis);
                    semaphore.release(requestLimit - semaphore.availablePermits());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void createDocument(Object document, String signature) {
        try {
            semaphore.acquire();
            // Здесь можно симулировать обработку создания документа
            System.out.println("Document created successfully.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        // Пример использования
        CrptApi crptApi = new CrptApi(TimeUnit.SECONDS, 10);
        Object document = new Object(); // Здесь должен быть ваш объект документа
        String signature = "signature_string";
        crptApi.createDocument(document, signature);
    }
}
