package pl.kurs.testdt5.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "log_request")
public class LogRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long time;
    private String ip;
    private String method;
    private String body;
    private String parameters;
    private String attributes;
    private String cookies;
    private String responseCode;
    private String responseBody;
    private String headers;
}
