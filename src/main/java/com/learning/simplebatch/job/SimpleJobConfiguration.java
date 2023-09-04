package com.learning.simplebatch.job;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleJobConfiguration {

    @Bean
    public Job simpleJob(JobRepository jobRepository, Step myStep) {
        return new JobBuilder("simpleJob01", jobRepository)
                .start(myStep)
                .build();
    }

    @Bean
    public Step simpleStep(JobRepository jobRepository, Tasklet myTasklet, PlatformTransactionManager transactionManager) {
        return new StepBuilder("simpleStep01", jobRepository)
                .tasklet(myTasklet, transactionManager)
                .build();
    }

    @Bean
    public Tasklet simpleTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("this is simpleTasklet01");
                return RepeatStatus.FINISHED;
            }
        };
    }

}

//    // v4 까지는 아래처럼 썼었으나 코드만 봐서는 Builder 에서 JobRepository 가 생성되고 설정된다는 사실이 드러나지 않는다.
//    // 따라서 JobRepository 를 명시적으로 파라미터에 넣어주는 방식으로 바꼈다.
//    public Job myJob(Step step) {
//        return this.jobBuilderFactory.get("myJob")
//                .start(step)
//                .build();
//    }
//      // step builder 에서 transactionManager 파라미터도 마찬가지
//    public Step myStep() {
    // tasklet 방식
//        return this.stepBuilderFactory.get("myStep")
//                .tasklet((StepContribution contribution, ChunkContext chunkContext)->{})
//                .build();
//    }

    //    @Bean
//    public Step simpleStep01() {
    // chunk 방식
//        return new StepBuilder("simpleStep01", this.jobRepository)
//                .<String, String>chunk(10, transactionManager)
//                .reader(reader())
//                .processor(processor())
//                .writer(writer())
//                .taskExecutor(taskExecutor())
//                .build();
//    }
//
//
// application.yml 파일 만들고 나서 아래 에러 나옴.
// deprecated gradle features were used in this build making it into incompatible with gradle 9.0
// IDE 의 build tool 을 바꿔주면 됨.
// IntelliJ > settings > build, execution, deployment > build tools > gradle > build and run using/run test using > gradle 로 돼 있으면 IntelliJ 로 바꾸기.
//
// application.yml
// 파일 안에 job.name 에다가 우리가 생성하는 job 의 이름을 넣어줌.
// JobBuilder("myJob") 에 넣어주는 이름이랑 job: name: 에 넣어주는 이름이랑 맞춰주기.
//spring:
//  batch:
//    job:
//      name: ${job.name:simpleJob01}