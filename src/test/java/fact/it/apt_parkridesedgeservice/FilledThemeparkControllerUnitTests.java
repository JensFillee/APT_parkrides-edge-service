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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private Themepark themepark1 = new Themepark("Themepark1","straatlaan 1", 5000,"TP1");
    private Themepark themepark2 =  new Themepark("Themepark2","straatlaan 2", 8000,"TP2");

    private Attraction attractionType1Themepark1 = new Attraction( "Attractie 1", "test descriptie attractie 1", 125, 1, "TP1", "A001");
    private Attraction attractionType2Themepark1 = new Attraction( "Attractie 2", "test descriptie attractie 2", 100, 2, "TP1", "A002");
    private Attraction attractionType1Themepark2 = new Attraction( "Attractie 3", "test descriptie attractie 3", 120, 1, "TP2", "A003");


    private List<Attraction> allAttractionFromType1 = Arrays.asList(attractionType1Themepark1, attractionType1Themepark2);
    private List<Attraction> allAttractionsForThemepark1 = Arrays.asList(attractionType1Themepark1, attractionType2Themepark1);
    private List<Attraction> allAttractionsForThemepark2 = Arrays.asList(attractionType1Themepark2);
    private List<Themepark> allThemeparks = Arrays.asList(themepark1, themepark2);

    @BeforeEach
    public void initializeMockserver() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    //get attractions by typeId
    @Test
    public void whenGetAttractionByTypeId_thenReturnFilledThemeparksJson() throws Exception {

        // GET all attractions from Type 1
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions/type/1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allAttractionFromType1))
                );

        // GET Themepark 1 info
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + themeparkServiceBaseUrl + "/themeparks/TP1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(themepark1))
                );

        // GET Themepark 2 info
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + themeparkServiceBaseUrl + "/themeparks/TP2")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(themepark2))
                );

        mockMvc.perform(get("/api/rides/attractions/type/{typeId}", 1))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].themeparkName",is("Themepark1")))
                .andExpect(jsonPath("$[0].themeparkCode",is("TP1")))
                .andExpect(jsonPath("$[0].themeparkAddress",is("straatlaan 1")))
                .andExpect(jsonPath("$[0].themeparkCapacity",is(5000)))
                .andExpect(jsonPath("$[0].rideDetails[0].attractionCode", is("A001")))
                .andExpect(jsonPath("$[0].rideDetails[0].name", is("Attractie 1")))
                .andExpect(jsonPath("$[0].rideDetails[0].minHeight", is(125)))
                .andExpect(jsonPath("$[1].themeparkName",is("Themepark2")))
                .andExpect(jsonPath("$[1].themeparkCode",is("TP2")))
                .andExpect(jsonPath("$[1].themeparkAddress",is("straatlaan 2")))
                .andExpect(jsonPath("$[1].themeparkCapacity",is(8000)))
                .andExpect(jsonPath("$[1].rideDetails[0].attractionCode", is("A003")))
                .andExpect(jsonPath("$[1].rideDetails[0].name", is("Attractie 3")))
                .andExpect(jsonPath("$[1].rideDetails[0].minHeight", is(120)));
    }

    //get Rides by themeparkName
    @Test
    public void whenGetRidesByThemeparkName_thenReturnFilledThemeparkJson() throws Exception {

        // GET Themeparks by Name 'Themepark'
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + themeparkServiceBaseUrl + "/themeparks/name/Themepark")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allThemeparks))
                );

        // GET all attractions for Themepark 1
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions/themepark/TP1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allAttractionsForThemepark1))
                );

        // GET all attractions for Themepark 2
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions/themepark/TP2")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allAttractionsForThemepark2))
                );

        mockMvc.perform(get("/api/rides/themepark/name/{name}", "Themepark"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].themeparkName",is("Themepark1")))
                .andExpect(jsonPath("$[0].themeparkCode",is("TP1")))
                .andExpect(jsonPath("$[0].themeparkAddress",is("straatlaan 1")))
                .andExpect(jsonPath("$[0].themeparkCapacity",is(5000)))
                .andExpect(jsonPath("$[0].rideDetails[0].attractionCode", is("A001")))
                .andExpect(jsonPath("$[0].rideDetails[0].name", is("Attractie 1")))
                .andExpect(jsonPath("$[0].rideDetails[0].minHeight", is(125)))
                .andExpect(jsonPath("$[1].themeparkName",is("Themepark2")))
                .andExpect(jsonPath("$[1].themeparkCode",is("TP2")))
                .andExpect(jsonPath("$[1].themeparkAddress",is("straatlaan 2")))
                .andExpect(jsonPath("$[1].themeparkCapacity",is(8000)))
                .andExpect(jsonPath("$[1].rideDetails[0].attractionCode", is("A003")))
                .andExpect(jsonPath("$[1].rideDetails[0].name", is("Attractie 3")))
                .andExpect(jsonPath("$[1].rideDetails[0].minHeight", is(120)));

    }

    //get Rides by themeparkCode
    @Test
    public void whenGetRidesByThemeparkCode_thenReturnFilledThemeparksJson() throws Exception {

        // GET Themepark 1 info
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + themeparkServiceBaseUrl + "/themeparks/TP1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(themepark1))
                );


        // GET all attractions for themepark 1
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions/themepark/TP1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allAttractionsForThemepark1))
                );


        mockMvc.perform(get("/api/rides/themepark/{themeparkCode}", "TP1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.themeparkName",is("Themepark1")))
                .andExpect(jsonPath("$.themeparkCode",is("TP1")))
                .andExpect(jsonPath("$.themeparkAddress",is("straatlaan 1")))
                .andExpect(jsonPath("$.themeparkCapacity",is(5000)))
                .andExpect(jsonPath("$.rideDetails[0].attractionCode", is("A001")))
                .andExpect(jsonPath("$.rideDetails[0].name", is("Attractie 1")))
                .andExpect(jsonPath("$.rideDetails[0].minHeight", is(125)));
    }

    //Get rides by ThemeparkCode and attraction Name
    @Test
    public void whenGetRidesByThemeparkCodeAndAttractionName_thenReturnFilledThemeparksJson() throws Exception {

        // GET Themepark 1 info
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + themeparkServiceBaseUrl + "/themeparks/TP1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(themepark1))
                );

        // GET attractions with name containing "Attractie" of Themepark 1
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions/name/Attractie/themepark/TP1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(allAttractionsForThemepark1))
                );

        mockMvc.perform(get("/api/rides/themepark/{themeparkCode}/attractions/name/{name}",  "TP1","Attractie"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.themeparkName",is("Themepark1")))
                .andExpect(jsonPath("$.themeparkCode",is("TP1")))
                .andExpect(jsonPath("$.themeparkAddress",is("straatlaan 1")))
                .andExpect(jsonPath("$.themeparkCapacity",is(5000)))
                .andExpect(jsonPath("$.rideDetails[0].attractionCode", is("A001")))
                .andExpect(jsonPath("$.rideDetails[0].name", is("Attractie 1")))
                .andExpect(jsonPath("$.rideDetails[0].minHeight", is(125)))
                .andExpect(jsonPath("$.rideDetails[1].attractionCode", is("A002")))
                .andExpect(jsonPath("$.rideDetails[1].name", is("Attractie 2")))
                .andExpect(jsonPath("$.rideDetails[1].minHeight", is(100)));
    }

    //Get rides by Themeparkcode and TypeId
//    @Test
//    public void whenGetRidesByThemeparkCodeAndTypeId_thenReturnFilledThemeparksJson() throws Exception {
//
//        // GET Themepark 1 info
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://" + themeparkServiceBaseUrl + "/themeparks/TP1")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(themepark1))
//                );
//
//        // GET attraction from Type 1 of Themepark 1
//        mockServer.expect(ExpectedCount.once(),
//                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions/type/1/themepark/TP1")))
//                .andExpect(method(HttpMethod.GET))
//                .andRespond(withStatus(HttpStatus.OK)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .body(mapper.writeValueAsString(attractionType1Themepark1))
//                );
//
//        mockMvc.perform(get("/api/rides/themepark/{themeparkCode}/attractions/type/{typeId}",  "TP1",1))
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.themeparkName",is("Themepark1")))
//                .andExpect(jsonPath("$.themeparkCode",is("TP1")))
//                .andExpect(jsonPath("$.themeparkAddress",is("straatlaan 1")))
//                .andExpect(jsonPath("$.themeparkCapacity",is(5000)))
//                .andExpect(jsonPath("$.rideDetails[0].attractionCode", is("A001")))
//                .andExpect(jsonPath("$.rideDetails[0].name", is("Attractie 1")))
//                .andExpect(jsonPath("$.rideDetails[0].minHeight", is(125)));
//    }

    @Test
    public void whenAddRide_thenReturnFilledThemeparkJson() throws Exception {

        Attraction attractionType3Themepark1 =  new Attraction( "Attractie 4", "test descriptie attractie 4", 100, 3, "TP1", "A004");

        // POST attraction for themepark 1 from Type 3
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(attractionType3Themepark1))
                );

        // GET Themepark 1 info
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + themeparkServiceBaseUrl + "/themeparks/TP1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(themepark1))
                );

        mockMvc.perform(post("/api/rides")
                    .content(mapper.writeValueAsString(attractionType3Themepark1))
                    .contentType(MediaType.APPLICATION_JSON))
                //expect:
                //JSON format
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // HTTP 200
                .andExpect(status().isOk())
                // Results matching the values of attractionType3Themepark1
                .andExpect(jsonPath("$.themeparkName",is("Themepark1")))
                .andExpect(jsonPath("$.themeparkCode",is("TP1")))
                .andExpect(jsonPath("$.themeparkAddress",is("straatlaan 1")))
                .andExpect(jsonPath("$.themeparkCapacity",is(5000)))
                .andExpect(jsonPath("$.rideDetails[0].attractionCode", is("A004")))
                .andExpect(jsonPath("$.rideDetails[0].name", is("Attractie 4")))
                .andExpect(jsonPath("$.rideDetails[0].typeId", is(3)))
                .andExpect(jsonPath("$.rideDetails[0].minHeight", is(100)))
                .andExpect(jsonPath("$.rideDetails[0].description", is("test descriptie attractie 4")));

    }

    @Test
    public void whenUpdateRide_thenReturnFilledThemeparkJson() throws Exception {

        Attraction updatedAttractionType1Themepark1 = new Attraction( "Attractie 1", "test descriptie attractie 1", 85, 1, "TP1", "A001");

        // GET attraction A001
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions/A001")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(attractionType1Themepark1))
                );

        // PUT attraction from Type 1 for Themepark 1 with new min height 85
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + attractionServiceBaseUrl + "/attractions")))
                .andExpect(method(HttpMethod.PUT))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(updatedAttractionType1Themepark1))
                );

        // GET Themepark 1 info
        mockServer.expect(ExpectedCount.once(),
                        requestTo(new URI("http://" + themeparkServiceBaseUrl + "/themeparks/TP1")))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(mapper.writeValueAsString(themepark1))
                );

        mockMvc.perform(put("/api/rides")
                        .content(mapper.writeValueAsString(updatedAttractionType1Themepark1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.themeparkName",is("Themepark1")))
                .andExpect(jsonPath("$.themeparkCode",is("TP1")))
                .andExpect(jsonPath("$.themeparkAddress",is("straatlaan 1")))
                .andExpect(jsonPath("$.themeparkCapacity",is(5000)))
                .andExpect(jsonPath("$.rideDetails[0].attractionCode", is("A001")))
                .andExpect(jsonPath("$.rideDetails[0].name", is("Attractie 1")))
                .andExpect(jsonPath("$.rideDetails[0].typeId", is(1)))
                .andExpect(jsonPath("$.rideDetails[0].minHeight", is(85)))
                .andExpect(jsonPath("$.rideDetails[0].description", is("test descriptie attractie 1")));

    }



}
