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
import org.springframework.web.bind.annotation.RestController;

import com.stackroute.newz.model.News;
import com.stackroute.newz.service.NewsService;
import com.stackroute.newz.util.exception.NewsNotFoundException;

/*
 * As in this assignment, we are working with creating RESTful web service, hence annotate
 * the class with @RestController annotation.A class annotated with @Controller annotation
 * has handler methods which returns a view. However, if we use @ResponseBody annotation along
 * with @Controller annotation, it will return the data directly in a serialized 
 * format. Starting from Spring 4 and above, we can use @RestController annotation which 
 * is equivalent to using @Controller and @ResposeBody annotation
 */
@RestController
public class NewsController {

	/*
	 * Autowiring should be implemented for the NewsService. (Use Constructor-based
	 * autowiring) Please note that we should not create any object using the new
	 * keyword
	 */
	private NewsService service;

	@Autowired
	public NewsController(NewsService service) {
		this.service = service;
	}

	/*
	 * Define a handler method which will create a specific news by reading the
	 * Serialized object from request body and save the news details in the
	 * database.This handler method should return any one of the status messages
	 * basis on different situations: 1. 201(CREATED) - If the news created
	 * successfully. 2. 409(CONFLICT) - If the newsId conflicts with any existing
	 * user.
	 * 
	 * This handler method should map to the URL "/api/v1/news" using HTTP POST
	 * method
	 */
	@PostMapping("/api/v1/news")
	public ResponseEntity<?> saveNews(@RequestBody News news) {
		ResponseEntity<?> entity;

		boolean news2 = service.addNews(news);

		if (news2) {

			entity = new ResponseEntity<>(news2, HttpStatus.CREATED);
		} else {

			entity = new ResponseEntity<>(news2, HttpStatus.CONFLICT);
		}
		return entity;
	}

	/*
	 * Define a handler method which will delete a news from a database. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the news deleted successfully from
	 * database. 2. 404(NOT FOUND) - If the news with specified newsId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/news/{userId}/{newsId}"
	 * using HTTP Delete method where "userId" should be replaced by a valid userId
	 * without {} and "newsId" should be replaced by a valid newsId without {}.
	 * 
	 */
	@DeleteMapping("/api/v1/news/{userId}/{newsId}")
	public ResponseEntity<?> deleteNews(@PathVariable("userId") String userId, @PathVariable("newsId") int newsId) {

		ResponseEntity<?> entity;

		Boolean flag = service.deleteNews(userId, newsId);

		if (flag) {

			entity = new ResponseEntity<>("Deleted Successfully ", HttpStatus.OK);
		} else {
			entity = new ResponseEntity<>("News not found for this newsId", HttpStatus.NOT_FOUND);
		}

		return entity;
	}

	/*
	 * Define a handler method which will delete all the news of a specific user
	 * from a database. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the newsId deleted
	 * successfully from database. 2. 404(NOT FOUND) - If the note with specified
	 * newsId is not found.
	 *
	 * This handler method should map to the URL "/api/v1/news/{userId}" using HTTP
	 * Delete method where "userId" should be replaced by a valid userId without {}
	 * and "newsid" should be replaced by a valid newsId without {}.
	 * 
	 */
	@DeleteMapping("/api/v1/news/{userId}")
	public ResponseEntity<?> deleteAllNews(@PathVariable("userId") String userId) {
		ResponseEntity<?> entity;
		try {

			Boolean flag = service.deleteAllNews(userId);

			if (flag) {

				entity = new ResponseEntity<>("All news deleted successfully ", HttpStatus.OK);

			} else {

				entity = new ResponseEntity<>("News not found for this userId", HttpStatus.NOT_FOUND);

			}
		} catch (NewsNotFoundException e) {

			entity = new ResponseEntity<>("News not found for this userId", HttpStatus.NOT_FOUND);
		}

		return entity;

	}

	/*
	 * Define a handler method which will update a specific news by reading the
	 * Serialized object from request body and save the updated news details in a
	 * database. This handler method should return any one of the status messages
	 * basis on different situations: 1. 200(OK) - If the news updated successfully.
	 * 2. 404(NOT FOUND) - If the news with specified newsId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/news/{userId}/{newsId}"
	 * using HTTP PUT method where "userId" should be replaced by a valid userId
	 * without {} and "newsid" should be replaced by a valid newsId without {}.
	 * 
	 */
	@PutMapping("/api/v1/news/{userId}/{newsId}")
	public ResponseEntity<?> updateNews(@RequestBody News news, @PathVariable("userId") String userId,
			@PathVariable("newsId") int newsId) {

		try {

			return new ResponseEntity<>(service.updateNews(news, newsId, userId), HttpStatus.OK);

		} catch (NewsNotFoundException e) {

			return new ResponseEntity<>("News not found...", HttpStatus.NOT_FOUND);

		}
	}

	/*
	 * Define a handler method which will get us the specific news by a userId. This
	 * handler method should return any one of the status messages basis on
	 * different situations: 1. 200(OK) - If the news found successfully. 2. 404(NOT
	 * FOUND) - If the news with specified newsId is not found.
	 * 
	 * This handler method should map to the URL "/api/v1/news/{userId}/{newsId}"
	 * using HTTP GET method where "userId" should be replaced by a valid userId
	 * without {} and "newsid" should be replaced by a valid newsId without {}.
	 * 
	 */
	@GetMapping("/api/v1/news/{userId}/{newsId}")
	public ResponseEntity<?> getAllNews(@PathVariable("userId") String userId, @PathVariable("newsId") int newsId) {
		try {

			return new ResponseEntity<>(service.getNewsByNewsId(userId, newsId), HttpStatus.OK);

		} catch (NewsNotFoundException e) {
			return new ResponseEntity<>("News NOT FOUND for this userId and newsId", HttpStatus.NOT_FOUND);

		}
	}

	/*
	 * Define a handler method which will show details of all news created by
	 * specific user. This handler method should return any one of the status
	 * messages basis on different situations: 1. 200(OK) - If the news found
	 * successfully. 2. 404(NOT FOUND) - If the news with specified newsId is not
	 * found. This handler method should map to the URL "/api/v1/news/{userId}"
	 * using HTTP GET method where "userId" should be replaced by a valid userId
	 * without {}.
	 * 
	 */
	@GetMapping("/api/v1/news/{userId}")
	public ResponseEntity<?> getAllNews(@PathVariable("userId") String userId) {

		List<News> list = service.getAllNewsByUserId(userId);
		if (list != null) {

			return new ResponseEntity<>(list, HttpStatus.OK);

		} else {
			return new ResponseEntity<>("News not found for this user Id", HttpStatus.NOT_FOUND);
		}

	}
}
