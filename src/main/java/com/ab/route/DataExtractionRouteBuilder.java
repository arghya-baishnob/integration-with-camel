package com.ab.route;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Arghya Baishnob on 2/12/2016.
 */
@Component
public class DataExtractionRouteBuilder extends RouteBuilder  {

    private String usageQuery ;
    private String queryA = "select * from student where student_name IN (#accountId#) order by student_id desc";

    @Override
    public void configure() throws Exception {
        from("timer://dataExtractionStartUp/source?repeatCount=1")
            .routeId("dataExtraction.source")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    String value = "'Arghya', 'Debu'";
                    buildQuery(value);
                    exchange.getIn().setHeader("myQuery", usageQuery);
                }
            })
            //.to("sql:{{sql.student.select.all}}?dataSource=db.arg")
            .recipientList(simple("sql:${header.myQuery}?dataSource=db.arg"))
            .split(body())
            .log(LoggingLevel.INFO, body().convertToString().toString())
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    final Map<String, Object> dbRow = (Map<String, Object>) exchange.getIn().getBody();
                    final String studentId = String.valueOf(dbRow.get("student_id").toString());
                    final String studentName = String.valueOf(dbRow.get("student_name").toString());
                   /* exchange.getIn().setHeader("id", "1");
                    exchange.getIn().setHeader("name", studentName);*/
                }
             });
             //.to("sql:{{sql.insert.student}}?dataSource=db.arg");
    }

    private void buildQuery(String id) {
        usageQuery = "select * from student where student_name IN (" + id + ") order by student_id desc";
    }
}
