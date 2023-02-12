package com.tech;

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

    private final JdbcTemplate jdbcTemplate;

    @Value("${h2.port}")
    private String hTwoPort;


    @PostConstruct
    public void init () throws Exception {

        String[] sqls = {"CREATE TABLE if not exists rules (\n" +
                " rule_namespace varchar(256) not null, \n" +
                " rule_id varchar(512) not null, \n" +
                " condition varchar(2000), \n" +
                " action varchar(2000), \n" +
                " priority integer, \n" +
                " description varchar(1000), \n" +
                " PRIMARY KEY(rule_namespace, rule_id)\n" +
                ")",
                "INSERT INTO rules \n" +
                " (rule_namespace , rule_id, condition, \n" +
                " action, priority, description) \n" +
                "VALUES (\n" +
                " 'LOAN',\n" +
                " '1',\n" +
                " 'input.monthlySalary >= 50000 && input.creditScore >= 800 && input.requestedLoanAmount < 4000000 && $(bank.target_done) == false', \n" +
                " 'output.setApprovalStatus(true);output.setSanctionedPercentage(90);output.setProcessingFees(8000);', \n" +
                " '1', \n" +
                " 'A person is eligible for Home loan?'\n" +
                ")",
                "INSERT INTO rules \n" +
                        " (rule_namespace , rule_id, condition, \n" +
                        " action, priority, description) \n" +
                        "VALUES (\n" +
                        " 'LOAN',\n" +
                        " '2',\n" +
                        " 'input.monthlySalary >= 35000 && input.monthlySalary <= 50000 && input.creditScore <= 500 && input.requestedLoanAmount < 2000000 && $(bank.target_done) == false',\n" +
                        "'output.setApprovalStatus(true);output.setSanctionedPercentage(60);output.setProcessingFees(2000);', \n" +
                        " '2', \n" +
                        " 'A person is eligible for Home loan?'\n" +
                        ")"};

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
