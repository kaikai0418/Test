package com.usian.Listener;

import com.usian.pojo.ItemESVO;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
public class SearchIistener {
        /**
         * 监听者接收消息三要素：
         *  1、queue
         *  2、exchange
         *  3、routing key
         */
        @RabbitListener(bindings = @QueueBinding(
                value = @Queue(value="item_queue",durable = "true"),
                exchange = @Exchange(value="item_exchange",type= ExchangeTypes.TOPIC),
                key= {"item.insert"}
        ))
        public void listen(ItemESVO itemESVO){

        }
}
