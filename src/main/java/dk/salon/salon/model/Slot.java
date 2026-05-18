package dk.salon.salon.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "slot")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hairdresser_id")
    private Hairdresser hairdresser;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isAvailable = true;

    public Slot() {}

    public Slot(Hairdresser hairdresser, Service service,
                LocalDateTime startTime, LocalDateTime endTime) {
        this.hairdresser = hairdresser;
        this.service = service;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Hairdresser getHairdresser() { return hairdresser; }
    public void setHairdresser(Hairdresser hairdresser) { this.hairdresser = hairdresser; }
    public Service getService() { return service; }
    public void setService(Service service) { this.service = service; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
}
