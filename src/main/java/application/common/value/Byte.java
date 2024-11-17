package application.common.value;

import lombok.Getter;

@Getter
public abstract class Byte {

    private final int value;

    public Byte(int value) {
        this.value = value;
    }
}
