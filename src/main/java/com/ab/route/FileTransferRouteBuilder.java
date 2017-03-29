package com.ab.route;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Created by Arghya Baishnob on 2/12/2016.
 */
@Component
public class FileTransferRouteBuilder extends RouteBuilder {

    private static final Logger logger = Logger.getLogger(FileTransferRouteBuilder.class);

    @Override
    public void configure() {
        from("file:D:/workspace/projects/ab-camel-demo/files/input?noop=true")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) {
                    final Message in = exchange.getIn();
                    logger.info("Processing: " + in.getBody(String.class));
                }
            }).process(new Processor() {
                @Override
                public void process(Exchange exchange) {
                    final Message in = exchange.getIn();
                    final String upperValue = in.getBody(String.class).toUpperCase();
                    exchange.getIn().setBody(upperValue);
                }
        }).to("file:D:/workspace/projects/ab-camel-demo/files/output");
    }
}
