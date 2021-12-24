package fact.it.apt_parkridesedgeservice.controller;

import fact.it.apt_parkridesedgeservice.model.Attraction;
import fact.it.apt_parkridesedgeservice.model.FilledThemepark;
import fact.it.apt_parkridesedgeservice.model.Themepark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilledThemeparkController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${themeparkservice}")
    private String themeparkServiceBaseUrl;

    @Value("${attractionservice}")
    private String attractionServiceBaseUrl;

    //    geeft één themepark terug met een lijst van zijn attracties
    @GetMapping("/rides/themepark/{themeparkCode}")
    public FilledThemepark getThemeparkByCode(@PathVariable String parkCode) {
        Themepark themepark =
                restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                        Themepark.class, parkCode);

        ResponseEntity<List<Attraction>> responseEntityAttractions =
                restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions/themepark/{themeparkCode}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                        }, parkCode);

        return new FilledThemepark(themepark, responseEntityAttractions.getBody());
    }


    //geeft lijst van themeparks met een bepaalde naam samen met hun attracties
    @GetMapping("/rides/themepark/{name}")
    public List<FilledThemepark> getThemeparksByName(@PathVariable String name) {

        List<FilledThemepark> returnList = new ArrayList<>();

        ResponseEntity<List<Themepark>> responseEntityThemeparks =
                restTemplate.exchange("http://" + themeparkServiceBaseUrl + "/themeparks/name/{name}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Themepark>>() {
                        }, name);

        List<Themepark> themeparks = responseEntityThemeparks.getBody();

        for (Themepark themepark :
                themeparks) {
            ResponseEntity<List<Attraction>> responseEntityAttractions =
                    restTemplate.exchange("http://" + attractionServiceBaseUrl + "/themepark/{themeparkCode}",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                            }, themepark.getThemeparkCode());

            returnList.add(new FilledThemepark(themepark, responseEntityAttractions.getBody()));
        }
        return returnList;
    }


        //geeft het themepark met alle attracties die een bepaalde naam hebben
        @GetMapping("/rides/themepark/{themeparkCode}/attractions/name/{name}")
        public FilledThemepark getRidesByParkIdAndRideName (@PathVariable String parkCode, @PathVariable String
        attractionName){

            Themepark themepark =
                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                            Themepark.class, parkCode);

            Attraction attraction =
                    restTemplate.getForObject("http://" + attractionServiceBaseUrl + "/attractions/name/" + attractionName + "/themepark/" + parkCode,
                            Attraction.class);

            return new FilledThemepark(themepark, attraction);
        }


        //geeft lijst van alle attracties van een bepaald type in een bepaald park
        @GetMapping("/rides/themepark/{themeparkCode}/attractions/type/{typeId}")
        public FilledThemepark getRidesByParkIdAndTypeId(@PathVariable String parkCode, @PathVariable Integer typeId){

            Themepark themepark =
                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                            Themepark.class, parkCode);

            ResponseEntity<List<Attraction>> responseEntityAttractions =
                    restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions/type/{typeId}/themepark/{themeparkCode}",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                            }, typeId,parkCode);

            return new FilledThemepark(themepark, responseEntityAttractions.getBody());
        }

        @PostMapping("/rides")
        public FilledThemepark addRide(@RequestParam String attractionCode, @RequestParam String parkCode, @RequestParam String name, @RequestParam Integer typeId, @RequestParam Integer minHeight, @RequestParam String description){

            Attraction attraction =
                    restTemplate.postForObject("http://" + attractionServiceBaseUrl + "/attractions",
                            new Attraction(name,minHeight,typeId,description,parkCode,attractionCode),Attraction.class);

            Themepark themepark =
                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                            Themepark.class,parkCode);

            return new FilledThemepark(themepark, attraction);
        }

        @PutMapping("/rides")
        public FilledThemepark updateRide(@RequestParam String attractionCode, @RequestParam String parkCode, @RequestParam String name, @RequestParam Integer typeId, @RequestParam Integer minHeight, @RequestParam String description){

            Attraction attraction =
                    restTemplate.getForObject("http://" + attractionServiceBaseUrl + "/attractions/" + attractionCode,
                            Attraction.class);
            attraction.setThemeparkCode(parkCode);
            attraction.setDescription(description);
            attraction.setName(name);
            attraction.setMinHeight(minHeight);
            attraction.setTypeId(typeId);

            ResponseEntity<Attraction> responseEntityAttraction =
                    restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions",
                            HttpMethod.PUT, new HttpEntity<>(attraction), Attraction.class);

            Attraction retrievedAttraction = responseEntityAttraction.getBody();

            Themepark themepark =
                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                            Themepark.class,parkCode);

            return new FilledThemepark(themepark, retrievedAttraction);
        }

        //delete a specific attraction
        @DeleteMapping("/rides/attraction/{attractionCode}")
        public ResponseEntity deleteRide(@PathVariable String attractionCode){

            restTemplate.delete("http://" + attractionServiceBaseUrl + "/attractions/" + attractionCode );

            return ResponseEntity.ok().build();
        }

}
