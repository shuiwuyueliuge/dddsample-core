package se.citerus.dddsample.domain.model.handling;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import se.citerus.dddsample.domain.model.DomainEvent;
import se.citerus.dddsample.domain.model.cargo.Cargo;
import se.citerus.dddsample.domain.model.carrier.CarrierMovement;
import se.citerus.dddsample.domain.model.location.Location;

import java.util.Comparator;
import java.util.Date;

/**
 * A HandlingEvent is used to register the event when, for instance,
 * a cargo is unloaded from a carrier at a some loacation at a given time.
 * <p/>
 * The HandlingEvent's are sent from different Incident Logging Applications
 * some time after the event occured and contain information about the
 * {@link se.citerus.dddsample.domain.model.cargo.TrackingId}, {@link se.citerus.dddsample.domain.model.location.Location}, timestamp of the completion of the event,
 * and possibly, if applicable a {@link se.citerus.dddsample.domain.model.carrier.CarrierMovement}.
 * <p/>
 * This class is the only member, and consequently the root, of the HandlingEvent aggregate. 
 * <p/>
 * HandlingEvent's could contain information about a {@link CarrierMovement} and if so,
 * the event type must be either {@link Type#LOAD} or {@link Type#UNLOAD}.
 * <p/>
 * All other events must be of {@link Type#RECEIVE}, {@link Type#CLAIM} or {@link Type#CUSTOMS}.
 */
public final class HandlingEvent implements DomainEvent<HandlingEvent> {

  /**
   * Comparator used to be able to sort HandlingEvents according to their completion time
   */
  public static final Comparator<HandlingEvent> BY_COMPLETION_TIME_COMPARATOR = new Comparator<HandlingEvent>() {
    public int compare(final HandlingEvent o1, final HandlingEvent o2) {
      return o1.completionTime().compareTo(o2.completionTime());
    }
  };

  private Type type;
  private CarrierMovement carrierMovement;
  private Location location;
  private Date completionTime;
  private Date registrationTime;
  private Cargo cargo;

  /**
   * Handling event type. Either requires or prohibits a carrier movement
   * association, it's never optional.
   */
  public enum Type {
    LOAD(true),
    UNLOAD(true),
    RECEIVE(false),
    CLAIM(false),
    CUSTOMS(false);

    private boolean carrierMovementRequired;

    /**
     * Private enum constructor.
     *
     * @param carrierMovementRequired whether or not a carrier movement is associated with this event type
     */
    private Type(final boolean carrierMovementRequired) {
      this.carrierMovementRequired = carrierMovementRequired;
    }

    /**
     * @return True if a carrier movement association is required for this event type.
     */
    public boolean requiresCarrierMovement() {
      return carrierMovementRequired;
    }

    /**
     * @return True if a carrier movement association is prohibited for this event type.
     */
    public boolean prohibitsCarrierMovement() {
      return !requiresCarrierMovement();
    }
  }

  /**
   * @param cargo            cargo
   * @param completionTime   completion time, the reported time that the event actually happened (e.g. the receive took place).
   * @param registrationTime registration time, the time the message is received
   * @param type             type of event. Legal values are LOAD and UNLOAD
   * @param location         where the event took place
   * @param carrierMovement  carrier movement.
   */
  public HandlingEvent(final Cargo cargo, final Date completionTime, final Date registrationTime, final Type type,
                       final Location location, final CarrierMovement carrierMovement) {
    Validate.noNullElements(new Object[]{cargo, completionTime, registrationTime, type, location});
    this.registrationTime = registrationTime;
    this.completionTime = completionTime;
    this.type = type;
    this.cargo = cargo;
    this.location = location;
    this.carrierMovement = carrierMovement;

    validateType();
  }

  public Type type() {
    return this.type;
  }

  public CarrierMovement carrierMovement() {
    return this.carrierMovement;
  }

  public Date completionTime() {
    return this.completionTime;
  }

  public Date registrationTime() {
    return this.registrationTime;
  }

  public Location location() {
    return this.location;
  }

  public Cargo cargo() {
    return this.cargo;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    final HandlingEvent event = (HandlingEvent) o;

    return sameEventAs(event);
  }

  public boolean sameEventAs(final HandlingEvent other) {
    return other != null && new EqualsBuilder().
      append(this.cargo, other.cargo).
      append(this.carrierMovement, other.carrierMovement).
      append(this.completionTime, other.completionTime).
      append(this.location, other.location).
      append(this.type, other.type).
      isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().
      append(cargo).
      append(carrierMovement).
      append(completionTime).
      append(location).
      append(type).
      toHashCode();
  }

  /**
   * Validate that the event type is compatible with the carrier movement value.
   * <p/>
   * Only certain types of events may be associated with a carrier movement.
   */
  private void validateType() {
    if (type.requiresCarrierMovement() && carrierMovement == null) {
      throw new IllegalArgumentException("Carrier movement is required for event type " + type);
    }
    if (type.prohibitsCarrierMovement() && carrierMovement != null) {
      throw new IllegalArgumentException("Carrier movement is not allowed with event type " + type);
    }
  }

  // Needed by Hibernate
  HandlingEvent() {
  }


  // Auto-generated surrogate key
  private Long id;

}
