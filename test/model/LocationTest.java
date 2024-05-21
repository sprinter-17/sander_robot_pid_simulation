package model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocationTest {
    private final Location location = new Location(new Position(50, 50), 45);

    @Test
    public void testLimit() {
        assertThat(location.limit(40, 60).position()).isEqualTo(new Position(50, 50));
        assertThat(location.limit(55, 60).position()).isEqualTo(new Position(55, 55));
        assertThat(location.limit(40, 45).position()).isEqualTo(new Position(45, 45));
    }


}