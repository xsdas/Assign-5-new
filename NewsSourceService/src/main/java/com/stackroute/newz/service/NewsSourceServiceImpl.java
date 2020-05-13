package com.stackroute.newz.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stackroute.newz.model.NewsSource;
import com.stackroute.newz.repository.NewsSourceRepository;
import com.stackroute.newz.util.exception.NewsSourceNotFoundException;

/*
* Service classes are used here to implement additional business logic/validation 
* This class has to be annotated with @Service annotation.
* @Service - It is a specialization of the component annotation. It doesn't currently 
* provide any additional behavior over the @Component annotation, but it's a good idea 
* to use @Service over @Component in service-layer classes because it specifies intent 
* better. Additionally, tool support and additional behavior might rely on it in the 
* future.
* */

@Service
public class NewsSourceServiceImpl implements NewsSourceService {

	/*
	 * Autowiring should be implemented for the NewsDao and MongoOperation. (Use
	 * Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	private NewsSourceRepository repository;

	@Autowired
	public NewsSourceServiceImpl(NewsSourceRepository repository) {

		this.repository = repository;
	}
	/*
	 * This method should be used to save a newsSource.
	 */

	@Override
	public boolean addNewsSource(NewsSource newsSource) {
		if (repository.insert(newsSource) != null) {
			return true;
		}
		return false;
	}

	/* This method should be used to delete an existing newsSource. */

	@Override
	public boolean deleteNewsSource(int newsSourceId) {
		if (repository.findById(newsSourceId) != null) {
			repository.deleteById(newsSourceId);
			return true;
		}
		return false;
	}

	/* This method should be used to update an existing newsSource. */

	@Override
	public NewsSource updateNewsSource(NewsSource newsSource, int newsSourceId) throws NewsSourceNotFoundException {
		if (repository.findById(newsSourceId) != null) {
			repository.insert(newsSource);

		}
		return newsSource;
	}

	/* This method should be used to get a specific newsSource for an user. */

	@Override
	public NewsSource getNewsSourceById(String userId, int newsSourceId) throws NewsSourceNotFoundException {
		NewsSource as = null;
		try {
			List<NewsSource> news = getAllNewsSourceByUserId(userId);
			for (NewsSource ns : news) {
				if (ns.getNewsSourceId() == newsSourceId) {
					as = ns;
					break;
				}
			}
		} catch (Exception e) {

		}

		return as;
	}

	/* This method should be used to get all newsSource for a specific userId. */

	@Override
	public List<NewsSource> getAllNewsSourceByUserId(String createdBy) {
		return repository.findAllNewsSourceByNewsSourceCreatedBy(createdBy);
	}

}
