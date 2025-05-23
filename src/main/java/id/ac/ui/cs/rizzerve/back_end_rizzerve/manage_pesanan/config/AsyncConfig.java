package id.ac.ui.cs.rizzerve.back_end_rizzerve.manage_pesanan.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Konfigurasi thread pool
        executor.setCorePoolSize(10);         // Thread inti yang selalu aktif
        executor.setMaxPoolSize(50);          // Maksimum thread yang bisa dibuat
        executor.setQueueCapacity(100);       // Kapasitas antrian task
        executor.setThreadNamePrefix("AsyncCart-");  // Prefix nama thread
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}