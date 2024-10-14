package labaSolvd.JacoLuna.Classes;

import java.util.Objects;

public class BoardingPass {
    private long idFlight;
    private long idPassenger;
    private long idClass;
    private String boardingTime;
    private int gateNumber;
    private int boardingOrder;

    public BoardingPass(long idPassenger, int idClass, String boardingTime, int gateNumber, int boardingOrder) {
        this.idPassenger = idPassenger;
        this.idClass = idClass;
        this.boardingTime = boardingTime;
        this.gateNumber = gateNumber;
        this.boardingOrder = boardingOrder;
    }

    public long getIdFlight() {
        return idFlight;
    }

    public long getIdPassenger() {
        return idPassenger;
    }

    public long getIdClass() {
        return idClass;
    }

    public String getBoardingTime() {
        return boardingTime;
    }

    public int getGateNumber() {
        return gateNumber;
    }

    public int getBoardingOrder() {
        return boardingOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardingPass that = (BoardingPass) o;
        return idFlight == that.idFlight && idPassenger == that.idPassenger && idClass == that.idClass && gateNumber == that.gateNumber && boardingOrder == that.boardingOrder && Objects.equals(boardingTime, that.boardingTime);
    }

    @Override
    public int hashCode() {
        return 21 + (int)idFlight + (int)idPassenger + (int)idClass;
    }
}
