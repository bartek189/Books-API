package x.none.model.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class StatusResponseDto {
    private String status;
    private Integer processedRows;
    private Long durationInMs;

}

