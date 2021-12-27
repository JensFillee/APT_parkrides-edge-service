package fact.it.apt_parkridesedgeservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import fact.it.apt_parkridesedgeservice.model.Attraction;
import fact.it.apt_parkridesedgeservice.model.Themepark;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class FilledThemeparkControllerUnitTests {

    @Value("${themeparkservice.baseurl}")
    private String themeparkServiceBaseUrl;

    @Value("${attractionservice.baseurl}")
    private String attractionServiceBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;
    private ObjectMapper mapper = new ObjectMapper();

    private Themepark themepark1 = new Themepark("Themepark1","straatlaan 1", 5000,"156555");
    private Themepark themepark2 =  new Themepark("Themepark2","straatlaan 2", 8000,"156444");

    private Attraction attractionType1Themepark1 = new Attraction( "Attractie 1", "Duistere krachten in Land of Legends wekten een eeuwige Typhoon op. Spring in de koets van de Skyriders, vlieg met hen mee en duik 25 meter het diepe in. Help jij heks Wayra in te tomen?", 125, 1, "156545", "A001");
    private Attraction attractionType2Themepark1 = new Attraction( "Attractie 2", "Neem met de hele familie plaats in een boomstammetje en ontdek de jungle in Indiana River. Trotseer de vele bergen en dalen en bereid je voor om nat te worden!", 100, 2, "156545", "A002");
    private Attraction attractionType1Themepark2 = new Attraction( "Attractie 3", "Nietsvermoedend 77 m kaarsrecht omhoog, genietend van de frisse lucht, van steil gesproken... Alles rondom jou wordt steeds kleiner", 120, 1, "156554", "A003");


    private List<Attraction> allAttractionFromType1 = Arrays.asList(attractionType1Themepark1, attractionType1Themepark2);
    private List<Attraction> allAttractionsForThemepark1 = Arrays.asList(attractionType1Themepark1, attractionType2Themepark1);
    private List<Attraction> allAttractionsForThemepark2 = Arrays.asList(attractionType1Themepark2);
    private List<Themepark> allThemeparks = Arrays.asList(themepark1, themepark2);

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

//    @Test
//    public void whenGetAttractionByTypeId_thenReturnFilledThemeparksJson() throws Exception {
//
//        // GET all attractions from Type 1
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/reviews/user/1")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(allReviewsFromUser1))
//                );
//
//        // GET Themepark 2 info
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://" + bookInfoServiceBaseUrl + "/books/ISBN2")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(book2))
//                );
//
//        // GET Themepark 1 info
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://" + bookInfoServiceBaseUrl + "/books/ISBN1")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(book1))
//                );
//
//        mockMvc.perform(get("/rankings/user/{userId}", 1))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].bookTitle", is("Book2")))
//                .andExpect(jsonPath("$[0].isbn", is("ISBN2")))
//                .andExpect(jsonPath("$[0].userScores[0].userId", is(1)))
//                .andExpect(jsonPath("$[0].userScores[0].scoreNumber", is(2)))
//                .andExpect(jsonPath("$[1].bookTitle", is("Book1")))
//                .andExpect(jsonPath("$[1].isbn", is("ISBN1")))
//                .andExpect(jsonPath("$[1].userScores[0].userId", is(1)))
//                .andExpect(jsonPath("$[1].userScores[0].scoreNumber", is(1)));
//    }





}
