package pmf.master.platforma.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digitalna platforma za ocenjivanje predispitnih obaveza")
                        .version("1.0")
                        .description("All endpoints for the digital platform for grading student homework")
                        .termsOfService("http://swagger.io/terms/"));
    }
}
