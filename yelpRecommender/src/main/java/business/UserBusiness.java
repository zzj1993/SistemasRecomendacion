package business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import recommender.utils.RecommendersInformation;
import entity.User;

public class UserBusiness {
	
	private final RecommendersInformation recommendersInformation;
	
	public UserBusiness(RecommendersInformation recommendersInformation){
		this.recommendersInformation = recommendersInformation;
	}
	
	public List<User> getUsers(){
		List<String> userIds = recommendersInformation.getRandomUsers(0.3);
		List<User> users = new ArrayList<User>(userIds.size());
		for(String u : userIds){
			users.add(new User(u, recommendersInformation.getUserName(u)));
		}
		Collections.sort(users);
		return users;
	}
}