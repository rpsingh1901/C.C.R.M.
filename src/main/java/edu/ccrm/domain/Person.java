package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

// Abstraction: abstract base class
public abstract class Person {
    private final UUID id;
    private Name fullName; // encapsulation via private + getters/setters
    private String email;
    private boolean active;
    private final LocalDateTime createdAt;

    protected Person(Name fullName, String email, boolean active) {
        this.id = UUID.randomUUID();
        assert fullName != null : "Name cannot be null"; // Assertion example
        this.fullName = fullName;
        this.email = email;
        this.active = active;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public Name getFullName() { return fullName; }
    public void setFullName(Name fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public abstract String getDisplayId(); // different per subtype

    @Override public String toString() { return "Person{"+"id="+id+", name="+fullName+", email="+email+", active="+active+"}"; }
    @Override public int hashCode() { return Objects.hash(id); }
    @Override public boolean equals(Object o) { return (o instanceof Person) && Objects.equals(id, ((Person)o).id); }
}
