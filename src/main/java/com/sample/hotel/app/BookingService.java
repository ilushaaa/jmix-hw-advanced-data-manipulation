package com.sample.hotel.app;

import com.sample.hotel.entity.Booking;
import com.sample.hotel.entity.BookingStatus;
import com.sample.hotel.entity.Room;
import com.sample.hotel.entity.RoomReservation;
import io.jmix.core.DataManager;
import io.jmix.core.FetchPlan;
import io.jmix.core.FetchPlans;
import io.jmix.data.PersistenceHints;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
public class BookingService {
    private final DataManager dataManager;
    @PersistenceContext
    private EntityManager entityManager;

    private final FetchPlans fetchPlans;

    public BookingService(FetchPlans fetchPlans, DataManager dataManager) {
        this.fetchPlans = fetchPlans;
        this.dataManager = dataManager;
    }

    /**
     * Check if given room is suitable for the booking.
     * 1) Check that sleeping places is enough to fit numberOfGuests.
     * 2) Check that there are no reservations for this room at the same range of dates.
     * Use javax.persistence.EntityManager and JPQL query for querying database.
     *
     * @param booking booking
     * @param room room
     * @return true if checks are passed successfully
     */
    public boolean isSuitable(Booking booking, Room room) {
        if (booking.getNumberOfGuests() > room.getSleepingPlaces()) {
            return false;
        }

        FetchPlan fetchPlan = fetchPlans.builder(Booking.class)
                .add("arrivalDate")
                .add("departureDate")
                .partial()
                .build();

        List<Booking> intersectBookings = entityManager.createQuery(
                "select b from Booking b join RoomReservation r on b = r.booking " +
                        "where r.room.id = :roomId and " +
                        "b.arrivalDate < :departureDate and " +
                        "b.departureDate > :arrivalDate", Booking.class)
                .setParameter("roomId", room.getId())
                .setParameter("arrivalDate", booking.getArrivalDate())
                .setParameter("departureDate", booking.getDepartureDate())
                .setHint(PersistenceHints.FETCH_PLAN, fetchPlan)
                .getResultList();

        return intersectBookings.isEmpty();
    }

    /**
     * Check that room is suitable for the booking, and create a reservation for this room.
     * @param room room to reserve
     * @param booking hotel booking
     * Wrap operation into a transaction (declarative or manual).
     *
     * @return created reservation object, or null if room is not suitable
     */
    @Transactional
    public RoomReservation reserveRoom(Booking booking, Room room) {
        if (isSuitable(booking, room)) {
            RoomReservation roomReservation = dataManager.create(RoomReservation.class);
            roomReservation.setRoom(room);
            roomReservation.setBooking(booking);
            dataManager.save(roomReservation);
            return roomReservation;
        }
        return null;
    }

    @Transactional
    public void cancelReservation(Booking booking) {
        List<RoomReservation> roomReservations = dataManager.load(RoomReservation.class)
                .query("select r from RoomReservation r where r.booking.id = :bookingId")
                .parameter("bookingId", booking.getId())
                .list();
        if (roomReservations.isEmpty()) {
            return;
        }

        dataManager.remove(roomReservations.get(0));

        booking.setStatus(BookingStatus.CANCELLED);
        dataManager.save(booking);
    }
}