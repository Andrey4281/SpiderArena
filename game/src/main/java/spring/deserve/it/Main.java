package spring.deserve.it;

import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import spring.deserve.starter.spider.Spider;


@SpringBootApplication
@EnableAspectJAutoProxy
public class Main {


    @Bean
    public Map<String,Integer> playersMap(List<Spider> spiders){
       return spiders.stream().collect(toMap(Spider::getOwner,spider -> 0,(e1,e2)->e1));
    }



    public static void main(String[] args) {
        System.out.println("Starting game");


        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);
        context.getBean(GameMasterNew.class).fight();

    }

}
