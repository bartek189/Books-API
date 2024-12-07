package x.none.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class StatusInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long jobId;
    private String status;
    private String duration;

    private String userName;


    public StatusInfo(Long jobId, String status, String duration, String username) {
        this.jobId = jobId;
        this.status = status;
        this.duration = duration;
        this.userName = username;

    }
}