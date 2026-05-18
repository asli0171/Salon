package dk.salon.salon.dto;

public class BookingRequestDTO {

    private Long slotId;

    public BookingRequestDTO() {}

    public BookingRequestDTO(Long slotId) {
        this.slotId = slotId;
    }

    public Long getSlotId() { return slotId; }
    public void setSlotId(Long slotId) { this.slotId = slotId; }
}
