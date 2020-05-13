package com.stackroute.newz.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.stackroute.newz.model.News;
import com.stackroute.newz.model.UserNews;
import com.stackroute.newz.repository.NewsRepository;
import com.stackroute.newz.util.exception.NewsNotFoundException;

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
public class NewsServiceImpl implements NewsService {

	/*
	 * Autowiring should be implemented for the NewsDao and MongoOperation.
	 * (Use Constructor-based autowiring) Please note that we should not create any
	 * object using the new keyword.
	 */
	private NewsRepository rep;

	@Autowired
	public NewsServiceImpl(NewsRepository rep) {
		this.rep = rep;
	}

	/*
	 * This method should be used to save a new news.
	 */
	@Override
	public boolean addNews(News news) {

		Optional<UserNews> opt = this.rep.findById(news.getAuthor());
		List<News> list = new LinkedList<>();
		UserNews newsAdded = null;
		if (opt.isPresent()) {
			int newsId = news.getNewsId();
			list = opt.get().getNewslist();
			long n = list.stream().filter(curNews -> curNews.getNewsId() == newsId).count();
			if (n > 0) {
				return false;
			}
		}
		UserNews userNews = new UserNews();
		list.add(news);

		userNews.setNewslist(list);
		userNews.setUserId(news.getAuthor());
		newsAdded = rep.insert(userNews);
		if (newsAdded == null) {
			return false;
		}
		return true;
	}

	/* This method should be used to delete an existing news. */
	
	public boolean deleteNews(String userId, int newsId) {
		Optional<UserNews> opt = this.rep.findById(userId);
		List<News> list = new LinkedList<>();
		if (opt.isPresent()) {
			list = opt.get().getNewslist();
			long count = list.stream().filter(curNews -> curNews.getNewsId() == newsId).count();
			if (count > 0) {
				list = list.stream().filter(curNews -> curNews.getNewsId() != newsId).collect(Collectors.toList());
				UserNews userNews = new UserNews();
				userNews.setNewslist(list);
				userNews.setUserId(userId);

				rep.save(userNews);
				return true;
			}
		}
		return false;
	}

	/* This method should be used to delete all news for a  specific userId. */
	
	public boolean deleteAllNews(String userId) throws NewsNotFoundException {
		try {
			UserNews nl = rep.findById(userId).get();
			nl.setNewslist(null);
			return true;
		} catch (Exception e) {
			throw new NewsNotFoundException("NOT_FOUND");
		}

	}

	/*
	 * This method should be used to update a existing news.
	 */

	public News updateNews(News news, int newsId, String userId) throws NewsNotFoundException {
		try {
			News n = getNewsByNewsId(userId, newsId);
			UserNews usernews = rep.findById(userId).get();
			List<News> nl = usernews.getNewslist();

			nl.remove(n);
			nl.add(news);
			usernews.setNewslist(nl);
			rep.save(usernews);
		} catch (Exception e) {
			throw new NewsNotFoundException("NOT_FOUND");
		}
		return news;
	}

	/*
	 * This method should be used to get a news by newsId created by specific user
	 */

	public News getNewsByNewsId(String userId, int newsId) throws NewsNotFoundException {
		try {
			List<News> nl = rep.findById(userId).get().getNewslist();
			for (News n : nl) {
				if (n.getNewsId() == newsId) {
					return n;
				}
			}
		} catch (Exception e) {

		}
		throw new NewsNotFoundException("NOT_FOUND");
	}

	/*
	 * This method should be used to get all news for a specific userId.
	 */

	public List<News> getAllNewsByUserId(String userId) {
		return rep.findById(userId).get().getNewslist();
	}

}
