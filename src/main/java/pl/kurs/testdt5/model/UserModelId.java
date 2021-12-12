package pl.kurs.testdt5.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kurs.testdt5.validation.IdExists;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModelId extends UserModel {
    @NotNull
    @IdExists
    private int id;

    public UserModelId(String name, String lastname, String email, String login, String password, int id) {
        super(name, lastname, email, login, password);
        this.id = id;
    }
}
