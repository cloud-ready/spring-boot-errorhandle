package top.infra.errorhandle.api;

import static lombok.AccessLevel.PRIVATE;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by zhanghaolun on 16/7/3.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY) // for Jackson 2.x
@Builder(builderMethodName = "validationErrorBuilder")
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Setter(PRIVATE)
@Data
public class ValidationError implements Serializable {

    private String field;
    private String rejected;
    private String message;
}
