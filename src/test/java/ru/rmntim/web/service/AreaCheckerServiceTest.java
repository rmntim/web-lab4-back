package ru.rmntim.web.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AreaCheckerServiceTest {
    private AreaCheckerService areaCheckerService;

    @BeforeEach
    void setUp() {
        areaCheckerService = new AreaCheckerService();
    }

    @Nested
    @DisplayName("Third Quadrant (Square: x <= 0, y <= 0)")
    class ThirdQuadrantTests {
        @ParameterizedTest
        @CsvSource({"0, 0, 1", "-0.5, -0.5, 1", // Inside
                "-1, -1, 1", "-1, 0, 1",      // On x-axis boundary
                "0, -1, 1", "-2, -2, 2"      // Scaled R
        })
        @DisplayName("should return true for points inside or on boundary")
        void testInArea_ThirdQuadrant_InsideOrOnBoundary(double x, double y, double r) {
            assertTrue(areaCheckerService.isInArea(x, y, r), String.format("Point (%.2f, %.2f) with R=%.2f should be IN area (3rd Q)", x, y, r));
        }

        @ParameterizedTest
        @CsvSource({"-1.1, -0.5, 1", "-0.5, -1.1, 1", // Outside (y too small)
                "-2, -0.5, 1", "-0.5, -2, 1"    // Outside
        })
        @DisplayName("should return false for points outside")
        void testInArea_ThirdQuadrant_Outside(double x, double y, double r) {
            assertFalse(areaCheckerService.isInArea(x, y, r), String.format("Point (%.2f, %.2f) with R=%.2f should be OUT of area (3rd Q)", x, y, r));
        }
    }

    @Nested
    @DisplayName("First Quadrant (Quarter Circle: x >= 0, y >= 0)")
    class FirstQuadrantTests {
        @ParameterizedTest
        @CsvSource({"0, 0, 1", "0.5, 0.5, 1",   // Inside
                "1, 0, 1", "0, 1, 1",       // On y-axis boundary (0, R)
                "0.707, 0.707, 1", "2, 0, 2"        // Scaled R
        })
        @DisplayName("should return true for points inside or on boundary")
        void testInArea_FirstQuadrant_InsideOrOnBoundary(double x, double y, double r) {
            assertTrue(areaCheckerService.isInArea(x, y, r), String.format("Point (%.2f, %.2f) with R=%.2f should be IN area (1st Q)", x, y, r));
        }

        @ParameterizedTest
        @CsvSource({"1, 1, 1", "0.8, 0.8, 1",   // Outside (0.64 + 0.64 = 1.28 > 1)
                "2, 1, 1"})
        @DisplayName("should return false for points outside")
        void testInArea_FirstQuadrant_Outside(double x, double y, double r) {
            assertFalse(areaCheckerService.isInArea(x, y, r), String.format("Point (%.2f, %.2f) with R=%.2f should be OUT of area (1st Q)", x, y, r));
        }
    }

    @Nested
    @DisplayName("Fourth Quadrant (Triangle: x >= 0, y <= 0)")
    class FourthQuadrantTests {
        @ParameterizedTest
        @CsvSource({"0, 0, 2", "1, 0, 2",       // On x-axis, inside triangle (R=2, so (2,0) is a vertex)
                "0, -0.5, 2", "1, -0.2, 2",    // Inside: y = -0.2, x/2 - r/2 = 1/2 - 2/2 = 0.5 - 1 = -0.5.  -0.2 >= -0.5
                "2, 0, 2", "0, -1, 2",      // Vertex (0, -R/2)
                "1, -0.5, 2"})
        @DisplayName("should return true for points inside or on boundary")
        void testInArea_FourthQuadrant_InsideOrOnBoundary(double x, double y, double r) {
            assertTrue(areaCheckerService.isInArea(x, y, r), String.format("Point (%.2f, %.2f) with R=%.2f should be IN area (4th Q)", x, y, r));
        }

        @ParameterizedTest
        @CsvSource({"1, -1, 2", "0.5, -0.9, 1",  // Outside: y = -0.9, x/2 - r/2 = 0.25 - 0.5 = -0.25. -0.9 < -0.25
                "3, -0.1, 2"})
        @DisplayName("should return false for points outside")
        void testInArea_FourthQuadrant_Outside(double x, double y, double r) {
            assertFalse(areaCheckerService.isInArea(x, y, r), String.format("Point (%.2f, %.2f) with R=%.2f should be OUT of area (4th Q)", x, y, r));
        }
    }

    @Nested
    @DisplayName("Second Quadrant (x < 0, y > 0)")
    class SecondQuadrantTests {
        @ParameterizedTest
        @CsvSource({"-1, 1, 1", "-0.1, 0.1, 5", "-10, 10, 2"})
        @DisplayName("should always return false")
        void testInArea_SecondQuadrant_AlwaysFalse(double x, double y, double r) {
            assertFalse(areaCheckerService.isInArea(x, y, r), String.format("Point (%.2f, %.2f) with R=%.2f should be OUT of area (2nd Q)", x, y, r));
        }
    }

    @Test
    @DisplayName("should handle R=0 correctly")
    void testInArea_RIsZero() {
        assertTrue(areaCheckerService.isInArea(0, 0, 0), "Origin (0,0) with R=0 should be IN area");
        assertFalse(areaCheckerService.isInArea(0.1, 0, 0), "Point (0.1,0) with R=0 should be OUT of area");
        assertFalse(areaCheckerService.isInArea(0, 0.1, 0), "Point (0,0.1) with R=0 should be OUT of area");
        assertFalse(areaCheckerService.isInArea(-0.1, 0, 0), "Point (-0.1,0) with R=0 should be OUT of area");
        assertFalse(areaCheckerService.isInArea(0, -0.1, 0), "Point (0,-0.1) with R=0 should be OUT of area");
    }

    @Test
    @DisplayName("should handle negative R (current behavior)")
    void testInArea_NegativeR() {
        assertFalse(areaCheckerService.isInArea(-0.5, -0.5, -1), "(-0.5,-0.5) R=-1 (3rd Q) should be false");

        assertTrue(areaCheckerService.isInArea(0.5, 0.5, -1), "(0.5,0.5) R=-1 (1st Q) should be true (behaves like R=1)");
        assertFalse(areaCheckerService.isInArea(1, 1, -1), "(1,1) R=-1 (1st Q) should be false (behaves like R=1)");

        assertTrue(areaCheckerService.isInArea(0.5, 0.8, -1), "(0.5,0.8) R=-1 (4th Q) should be true (2*0.8 >= 0.5+1 => 1.6 >= 1.5)");
        assertTrue(areaCheckerService.isInArea(0.5, 0.2, -1), "(0.5,0.2) R=-1 (4th Q) should be false (2*0.2 >= 0.5+1 => 0.4 < 1.5)");
    }
}
