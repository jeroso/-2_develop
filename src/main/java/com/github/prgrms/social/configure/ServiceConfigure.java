package com.github.prgrms.social.configure;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import com.github.prgrms.social.security.Jwt;
import com.github.prgrms.social.util.MessageUtils;
import com.zaxxer.hikari.HikariDataSource;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ServiceConfigure {

  @Bean
  @Profile("test")
  public DataSource testDataSource() {
    DataSourceBuilder<? extends DataSource> factory = DataSourceBuilder
      .create()
      .driverClassName("org.h2.Driver")
      .url("jdbc:h2:mem:test_social_server;MODE=MYSQL;DB_CLOSE_DELAY=-1");
    HikariDataSource dataSource = (HikariDataSource) factory.build();
    dataSource.setPoolName("TEST_H2_DB");
    dataSource.setMinimumIdle(1);
    dataSource.setMaximumPoolSize(1);
    return new Log4jdbcProxyDataSource(dataSource);
  }

  @Bean
  public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
    MessageSourceAccessor messageSourceAccessor = new MessageSourceAccessor(messageSource);
    MessageUtils.setMessageSourceAccessor(messageSourceAccessor);
    return messageSourceAccessor;
  }

  @Bean
  public Jwt jwt(JwtTokenConfigure jwtTokenConfigure) {
    return new Jwt(jwtTokenConfigure.getIssuer(), jwtTokenConfigure.getClientSecret(), jwtTokenConfigure.getExpirySeconds());
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
    // Jackson 설정 처리
    return builder -> {
      AfterburnerModule abm = new AfterburnerModule();
      JavaTimeModule jtm = new JavaTimeModule();
      jtm.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));

      builder.visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
      builder.visibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
      builder.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
      builder.serializationInclusion(JsonInclude.Include.NON_NULL);
      builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      builder.modulesToInstall(abm, jtm);
    };
  }

}