package com.iz.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class H2Config {

    public static final String ACTUAL_VS_CONTRACT_EXPR1 = """
            priceDifference = currentPrice - originalPrice; 
            status = priceDifference > 0 ? 'UnderCharged' : (priceDifference < 0 ? 'OverCharged' : 'NoChange')
            """;

    /**
     *
     * priceDifference > 0 AND priceDifference > 5 -> status = OVerCharged
     *
     *
     *
     *
     *
     */

    private final JdbcTemplate jdbcTemplate;

    @Value("${h2.port}")
    private String hTwoPort;


    @PostConstruct
    public void init () throws Exception {

        String[] sqls = {"""
                 CREATE TABLE if not exists calculations (
                     type varchar(256) not null, 
                     id varchar(512) not null, 
                     expression varchar(2000), 
                     action varchar(2000), 
                     priority integer, 
                     description varchar(1000), 
                     PRIMARY KEY(rule_namespace, rule_id)
                )""",
                """
                         INSERT INTO calculations 
                             (rule_namespace , rule_id, condition, action, priority, description) 
                        VALUES (
                             'ActualVsContract', '1',
                             'input.monthlySalary >= 50000 && input.creditScore >= 800 && input.requestedLoanAmount < 4000000 && $(bank.target_done) == false', 
                             'output.setApprovalStatus(true);output.setSanctionedPercentage(90);output.setProcessingFees(8000);', 
                             '1', 
                             'A person is eligible for Home loan?'
                        )""",
                """
                        INSERT INTO rules 
                         (rule_namespace , rule_id, condition, action, priority, description) 
                        VALUES (
                         'ActualVsContract', '2',
                         'input.monthlySalary >= 35000 && input.monthlySalary <= 50000 && input.creditScore <= 500 && input.requestedLoanAmount < 2000000 && $(bank.target_done) == false',
                          'output.setApprovalStatus(true);output.setSanctionedPercentage(60);output.setProcessingFees(2000);', 
                         '2', 
                         'A person is eligible for Home loan?'
                        )"""};

        Arrays.asList(sqls).forEach(string -> {
            log.info("SQL: {}", string);
            jdbcTemplate.execute(string);
        });
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public org.h2.tools.Server inMemDb() throws Exception {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", hTwoPort);
    }
}

