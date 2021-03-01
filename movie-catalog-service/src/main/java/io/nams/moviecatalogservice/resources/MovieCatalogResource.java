package io.nams.moviecatalogservice.resources;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.nams.moviecatalogservice.models.Rating;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import io.nams.moviecatalogservice.models.CatalogItem;
import io.nams.moviecatalogservice.models.Movie;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
	
	//creating Resttemplate
@Autowired
private RestTemplate restTemplate ;	

@Autowired
private WebClient.Builder webClientBuilder;
	
@RequestMapping("/{userId}")	
public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
	

	
	//get all rated movie ids
	List<Rating> ratings = Arrays.asList(
			new Rating(1,2),
			new Rating(2,4)
			);
	
	
	return ratings.stream().map(rating -> {
		//make call to movie API using movie id from ratings data
		//Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);
		
		//calling Api using webclient builder
		Movie movie = webClientBuilder.build()
				.get()
				.uri("http://localhost:8082/movies/")
				.retrieve()
				.bodyToMono(Movie.class)
				.block();
		
		return new CatalogItem(movie.getMovieName(), "test", rating.getRating());
	})
	.collect(Collectors.toList());
	//for each movie id , call movie info service and get details
	//put them together
	
//	return Collections.singletonList(new CatalogItem("Transformers", "SciFi Movie", 4));
}
}
