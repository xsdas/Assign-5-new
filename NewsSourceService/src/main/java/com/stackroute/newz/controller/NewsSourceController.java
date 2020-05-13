package com.stackroute.newz.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.newz.model.NewsSource;
import com.stackroute.newz.service.NewsSourceService;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
@RequestMapping("/api/v1/newssource")
public class NewsSourceController {

	/*
	 * Autowiring should be implemented for the NewsService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */

	private NewsSourceService service;

	@Autowired
	public NewsSourceController(NewsSourceService service) {
		this.service = service;
	}

	/*
	 * Define a handler method which will create a specific newssource by reading
	 * the Serialized object from request body and save the newssource details in
	 * the database.This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED) - If the newssource created
	 * successfully. 2. 409(CONFLICT) - If the newssourceId conflicts with any
	 * existing user.
	 * 
	 * This handler method should map to the URL "/api/v1/newssource" using HTTP
	 * POST method
	 */

	@PostMapping
	public ResponseEntity<?> createNewsSource(@RequestBody NewsSource newssource) {
		ResponseEntity<?> entity;
		boolean news2 = service.addNewsSource(newssource);
		System.out.println("news2 :" + news2);
		if (news2) {

			entity = new ResponseEntity<>(news2, HttpStatus.CREATED);
		} else {
			entity = new ResponseEntity<>(news2, HttpStatus.CONFLICT);
		}
		return entity;

	}

	/*
	 * Define a handler method which will delete a newssource from a database. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the newssource deleted successfully
	 * from database. 2. 404(NOT FOUND) - If the newssource with specified newsId is
	 * not found.
	 *
	 * This handler method should map to the URL "/api/v1/newssource/{newssourceId}"
	 * using HTTP Delete method where "userId" should be replaced by a valid userId
	 * without {} and "newssourceId" should be replaced by a valid newsId without
	 * {}.
	 * 
	 */
	@DeleteMapping("/{newssourceId}")
	public ResponseEntity<?> deleteNewssource(@PathVariable("newssourceId") int newsSourceId) {
		try {
			if (true == service.deleteNewsSource(newsSourceId)) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {

		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	/*
	 * Define a handler method which will update a specific newssource by reading
	 * the Serialized object from request body and save the updated newssource
	 * details in a database. This handler method should return any one of the
	 * status messages basis on different situations: 1. 200(OK) - If the newssource
	 * updated successfully. 2. 404(NOT FOUND) - If the newssource with specified
	 * newssourceId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/newssource/{newssourceId}"
	 * using HTTP PUT method where "newssourceId" should be replaced by a valid
	 * newssourceId without {}.
	 * 
	 */
	@PutMapping("/{newssourceId}")
	public ResponseEntity<?> updateNews(@PathVariable("newssourceId") int newssourceId,
			@RequestBody NewsSource newsSource) {
		try {
			service.getNewsSourceById(newsSource.getNewsSourceCreatedBy(), newsSource.getNewsSourceId());
			newsSource.setNewsSourceCreationDate();

			return new ResponseEntity<>(service.updateNewsSource(newsSource, newssourceId), HttpStatus.OK);

		} catch (Exception e) {

		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	/*
	 * Define a handler method which will get us the specific newssource by a
	 * userId. This handler method should return any one of the status messages
	 * basis on different situations: 1. 200(OK) - If the newssource found
	 * successfully. 2. 404(NOT FOUND) - If the newssource with specified newsId is
	 * not found.
	 * 
	 * This handler method should map to the URL
	 * "/api/v1/newssource/{userId}/{newssourceId}" using HTTP GET method where
	 * "userId" should be replaced by a valid userId without {} and "newssourceId"
	 * should be replaced by a valid newsId without {}.
	 * 
	 */
	@GetMapping("/{userId}/{newssourceId}")
	public ResponseEntity<?> getNewsSource(@PathVariable("userId") String userId,
			@PathVariable("newssourceId") int newssourceId) {
		try {
			return new ResponseEntity<>(service.getNewsSourceById(userId, newssourceId), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}

	/*
	 * Define a handler method which will show details of all newssource created by
	 * specific user. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the newssource found
	 * successfully. 2. 404(NOT FOUND) - If the newssource with specified newsId is
	 * not found. This handler method should map to the URL
	 * "/api/v1/newssource/{userId}" using HTTP GET method where "userId" should be
	 * replaced by a valid userId without {}.
	 * 
	 */
	@GetMapping("/{userId}")
	public ResponseEntity<?> getNewsSourceByUser(@PathVariable("userId") String userId,
			@RequestBody NewsSource newssource) {
		List<NewsSource> newsSourceList = service.getAllNewsSourceByUserId(userId);
		if (newsSourceList == null || newsSourceList.size() == 0) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(newsSourceList, HttpStatus.OK);

	}

}
