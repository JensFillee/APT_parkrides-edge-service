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
@RequestMapping("/api")
public class FilledThemeparkController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${themeparkservice.baseurl}")
    private String themeparkServiceBaseUrl;

    @Value("${attractionservice.baseurl}")
    private String attractionServiceBaseUrl;

    //    geeft één themepark terug met een lijst van zijn attracties
    @GetMapping("/rides/themepark/{themeparkCode}")
    public FilledThemepark getThemeparkByCode(@PathVariable String themeparkCode) {
        Themepark themepark =
                restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                        Themepark.class, themeparkCode);

        ResponseEntity<List<Attraction>> responseEntityAttractions =
                restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions/themepark/{themeparkCode}",
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                        }, themeparkCode);

        return new FilledThemepark(themepark, responseEntityAttractions.getBody());
    }


    //geeft lijst van themeparks met een bepaalde naam samen met hun attracties
    @GetMapping("/rides/themepark/name/{name}")
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
                    restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions/themepark/{themeparkCode}",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                            }, themepark.getThemeparkCode());

            returnList.add(new FilledThemepark(themepark, responseEntityAttractions.getBody()));
        }
        return returnList;
    }


        //geeft het themepark met alle attracties die een bepaalde naam hebben
        @GetMapping("/rides/themepark/{themeparkCode}/attractions/name/{attractionName}")
        public FilledThemepark getRidesByParkIdAndRideName (@PathVariable String themeparkCode, @PathVariable String
        attractionName){

            Themepark themepark =
                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                            Themepark.class, themeparkCode);

            ResponseEntity<List<Attraction>> responseEntityAttractions =
                    restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions/name/" + attractionName + "/themepark/" + themeparkCode,
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                            }, attractionName, themeparkCode);

            return new FilledThemepark(themepark, responseEntityAttractions.getBody());
        }


        //geeft lijst van alle attracties van een bepaald type in een bepaald park
        @GetMapping("/rides/themepark/{themeparkCode}/attractions/type/{typeId}")
        public FilledThemepark getRidesByParkIdAndTypeId(@PathVariable String themeparkCode, @PathVariable Integer typeId){

            Themepark themepark =
                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                            Themepark.class, themeparkCode);

            ResponseEntity<List<Attraction>> responseEntityAttractions =
                    restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions/type/{typeId}/themepark/{themeparkCode}",
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<Attraction>>() {
                            }, typeId, themeparkCode);

            return new FilledThemepark(themepark, responseEntityAttractions.getBody());
        }

        @PostMapping("/rides")
        public FilledThemepark addRide(@RequestBody Attraction newAttraction) {

            Attraction attraction =
                    restTemplate.postForObject("http://" + attractionServiceBaseUrl + "/attractions",
                            newAttraction, Attraction.class);

            String themeparkCode = newAttraction.getThemeparkCode();

            Themepark themepark =
                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/" + themeparkCode,
                            Themepark.class, themeparkCode);

            return new FilledThemepark(themepark, attraction);
        }

//        @PutMapping("/rides")
//        public FilledThemepark updateRide(@RequestParam String attractionCode, @RequestParam String themeparkCode, @RequestParam String name, @RequestParam Integer typeId, @RequestParam Integer minHeight, @RequestParam String description){
//
//            Attraction attraction =
//                    restTemplate.getForObject("http://" + attractionServiceBaseUrl + "/attractions/" + attractionCode,
//                            Attraction.class);
//            attraction.setThemeparkCode(themeparkCode);
//            attraction.setDescription(description);
//            attraction.setName(name);
//            attraction.setMinHeight(minHeight);
//            attraction.setTypeId(typeId);
//
//            ResponseEntity<Attraction> responseEntityAttraction =
//                    restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions",
//                            HttpMethod.PUT, new HttpEntity<>(attraction), Attraction.class);
//
//            Attraction retrievedAttraction = responseEntityAttraction.getBody();
//
//            Themepark themepark =
//                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
//                            Themepark.class, themeparkCode);
//
//            return new FilledThemepark(themepark, retrievedAttraction);
//        }

        @PutMapping("/rides")
        public FilledThemepark updateRide(@RequestBody Attraction editAttraction) {

            Attraction attraction =
                    restTemplate.getForObject("http://" + attractionServiceBaseUrl + "/attractions/" + editAttraction.getAttractionCode(),
                            Attraction.class);

            attraction.setThemeparkCode(editAttraction.getThemeparkCode());
            attraction.setDescription(editAttraction.getDescription());
            attraction.setName(editAttraction.getName());
            attraction.setMinHeight(editAttraction.getMinHeight());
            attraction.setTypeId(editAttraction.getTypeId());

            ResponseEntity<Attraction> responseEntityAttraction =
                    restTemplate.exchange("http://" + attractionServiceBaseUrl + "/attractions",
                            HttpMethod.PUT, new HttpEntity<>(attraction), Attraction.class);

            Attraction retrievedAttraction = responseEntityAttraction.getBody();

            String themeparkCode = editAttraction.getThemeparkCode();

            Themepark themepark =
                    restTemplate.getForObject("http://" + themeparkServiceBaseUrl + "/themeparks/{themeparkCode}",
                            Themepark.class, themeparkCode);

            return new FilledThemepark(themepark, attraction);
        }

        //delete a specific attraction
        @DeleteMapping("/rides/attraction/{attractionCode}")
        public ResponseEntity deleteRide(@PathVariable String attractionCode){

            restTemplate.delete("http://" + attractionServiceBaseUrl + "/attractions/" + attractionCode );

            return ResponseEntity.ok().build();
        }

}
