package takbaeyo;

import takbaeyo.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;

@Service
public class PolicyHandler{
    @Autowired
    PointRepository pointRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverDelivered_GetPointPol(@Payload Delivered delivered){

        if(delivered.isMe()){
            //LJK

            Iterator<Point> iterator = pointRepository.findAll().iterator();
            while(iterator.hasNext()){
                Point pointTmp = iterator.next();
                if(pointTmp.getMemberId() == delivered.getMemberId() && delivered.getStatus().equals("Finish")){
                    Optional<Point> PointOptional = pointRepository.findById(pointTmp.getId());
                    Point point = PointOptional.get();
                    point.setPoint(point.getPoint()+1000);
                    pointRepository.save(point);

                }
            }
            //LJK

            System.out.println("##### listener GetPointPol : " + delivered.toJson());
        }
    }

}
