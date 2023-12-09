package ua.kpi.receiver;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MessageReceiverApplication {

  private final Runnable messageListener;

  public MessageReceiverApplication(Runnable messageListener) {
    this.messageListener = messageListener;
  }

  public static void main(String[] args) {
    SpringApplication.run(MessageReceiverApplication.class, args);
  }

  @Bean
  public CommandLineRunner schedulingRunner(
      @Qualifier("applicationTaskExecutor") TaskExecutor executor) {
    return args -> executor.execute(messageListener);
  }
}
