package likelion.hamahama.user.entity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.io.IOException;

import java.util.List;

@Slf4j
@Convert
public class StringListConverter implements AttributeConverter<List<String>, String>{

//    private static final String SPLIT_CHAR = ",";
//
//    @Override
//    public String convertToDatabaseColumn(List<String> attribute) {
//        return attribute.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));
//    }
//
//    @Override
//    public List<String> convertToEntityAttribute(String dbData) {
//        return Arrays.stream(dbData.split(SPLIT_CHAR))
//                .map(String::toString)
//                .collect(Collectors.toList());
//
//    }

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
            .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);

    // DB에 저장 될 때 사용
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            log.debug("StringListConverter.convertToDatabaseColumn exception occur attribute: {}", attribute.toString());
            new NullPointerException("null");
        }
        return null;
    }

    // DB의 데이터를 Object로 매핑할 때 사용
    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, List.class);
        } catch (IOException e) {
            log.debug("StringListConverter.convertToEntityAttribute exception occur dbData: {}", dbData);
            new NullPointerException("null");
        }
        return null;
    }

}