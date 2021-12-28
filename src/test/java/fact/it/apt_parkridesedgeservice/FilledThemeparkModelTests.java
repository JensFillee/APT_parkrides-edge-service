package fact.it.apt_parkridesedgeservice;

import fact.it.apt_parkridesedgeservice.model.Attraction;
import fact.it.apt_parkridesedgeservice.model.FilledThemepark;
import fact.it.apt_parkridesedgeservice.model.Themepark;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilledThemeparkModelTests {

    @Autowired
    private Themepark themepark1 = new Themepark("Themepark1","straatlaan 1", 5000,"TP1");

    private Attraction attraction1 = new Attraction( "Attractie 1", "test descriptie attractie 1", 125, 1, "TP1", "A001");
    private Attraction attraction2 = new Attraction( "Attractie 2", "test descriptie attractie 2", 100, 2, "TP1", "A002");

    List<Attraction> attractions = Arrays.asList(attraction1, attraction2);

    @Test
    void testConstructor(){
        FilledThemepark filledThemepark = new FilledThemepark(themepark1,attraction1);
        assertEquals("Themepark1",filledThemepark.getThemeparkName());
        assertEquals("straatlaan 1",filledThemepark.getThemeparkAddress());
        assertEquals(5000,filledThemepark.getThemeparkCapacity());
        assertEquals("TP1",filledThemepark.getThemeparkCode());
        assertEquals(1,filledThemepark.getRideDetails().size());
    }

    @Test
    void testConstructorWithList(){
        FilledThemepark filledThemepark = new FilledThemepark(themepark1,attractions);
        assertEquals("Themepark1",filledThemepark.getThemeparkName());
        assertEquals("straatlaan 1",filledThemepark.getThemeparkAddress());
        assertEquals(5000,filledThemepark.getThemeparkCapacity());
        assertEquals("TP1",filledThemepark.getThemeparkCode());
        assertEquals(attractions.size(),filledThemepark.getRideDetails().size());
    }

    @Test
    void getThemeparkName_happy(){
        FilledThemepark filledThemepark = new FilledThemepark(themepark1,attractions);

        assertThat(filledThemepark).isNotNull();
        assertThat(filledThemepark.getThemeparkName()).isEqualTo("Themepark1");
    }

    @Test
    void getThemeparkAddress(){
        FilledThemepark filledThemepark = new FilledThemepark(themepark1,attractions);

        assertThat(filledThemepark).isNotNull();
        assertThat(filledThemepark.getThemeparkAddress()).isEqualTo("straatlaan 1");
    }

    @Test
    void getThemeparkCapacity(){
        FilledThemepark filledThemepark = new FilledThemepark(themepark1,attractions);

        assertThat(filledThemepark).isNotNull();
        assertThat(filledThemepark.getThemeparkCapacity()).isEqualTo(5000);
    }

    @Test
    void getThemeparkCode(){
        FilledThemepark filledThemepark = new FilledThemepark(themepark1,attractions);

        assertThat(filledThemepark).isNotNull();
        assertThat(filledThemepark.getThemeparkCode()).isEqualTo("TP1");
    }

    @Test
    void getRideDetails(){
        FilledThemepark filledThemepark = new FilledThemepark(themepark1,attractions);
        assertThat(filledThemepark).isNotNull();
        assertThat(filledThemepark.getRideDetails()).usingRecursiveComparison().ignoringCollectionOrder().isEqualTo(attractions);
    }


}
