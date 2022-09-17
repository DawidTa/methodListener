package pl.methodListener.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "log_request")
public class LogRequestEntity {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private long time;
    private String ip;
    private String method;
    @Lob
    private String body;
    private String parameters;
    @Lob
    private String attributes;
    private String cookies;
    private String responseCode;
    @Lob
    private String responseBody;
    @Lob
    private String headers;

}
