package dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Review;
import entity.User;

public class GeneralDAO {

	private final static String ID = "ID";
	private final static String NAME = "NAME";
	private final static String BUSINESS_ID = "BUSINESS_ID";
	private final static String USER_ID = "USER_ID";
	private final static String STARS = "STARS";
	private final static String COMPUTED_STARS = "COMPUTED_STARS";
	private final static String TEXT = "TEXT";

	public List<User> getAllUsers(Connection dbConnection){
		List<User> users = new ArrayList<User>();
		try{
			String sql = "SELECT * FROM USER u;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				String id = resultSet.getString(ID);;
				String name = resultSet.getString(NAME);
				users.add(new User(id, name));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return users;
	}
	
	public void addReview(Review review, Connection dbConnection){
		String sql = "INSERT INTO REVIEW (BUSINESS_ID, USER_ID, STARS) VALUES (?,?,?);";
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			prepareStatement.setString(1, review.getBusinessId());
			prepareStatement.setString(2, review.getUserId());
			prepareStatement.setInt(3, review.getStars());
			prepareStatement.executeUpdate();
			prepareStatement.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Review> getAllReviews(Connection dbConnection){
		List<Review> reviews = new ArrayList<Review>();
		try{
			String sql = "SELECT * FROM REVIEW r;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				String businessId = resultSet.getString(BUSINESS_ID);
				String userId = resultSet.getString(USER_ID);
				int stars = resultSet.getInt(STARS);
				int computedStars = 0;
				Clob text = resultSet.getClob(TEXT);
				reviews.add(new Review(businessId, userId, stars, computedStars, clobToString(text)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return reviews;
	}
	
	public int getStarsSumReviewsSublist(int size, Connection dbConnection){
		int result = -1;
		try{
			String sql = "SELECT SUM(STARS) AS STARS FROM (SELECT STARS AS STARS FROM REVIEW r LIMIT ?);";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			prepareStatement.setInt(1, size);
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				result = resultSet.getInt(STARS);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public List<String> getBusinessIdsFromReviews(Connection dbConnection){
		List<String> ids = new ArrayList<String>();
		try{
			String sql = "SELECT DISTINCT BUSINESS_ID FROM REVIEW r;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				String id = resultSet.getString(BUSINESS_ID);
				ids.add(id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ids;
	}
	
	public List<String> getUserIdsFromReviews(int size, Connection dbConnection){
		List<String> ids = new ArrayList<String>();
		try{
			String sql = "SELECT DISTINCT USER_ID FROM REVIEW r LIMIT ?;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			prepareStatement.setInt(1, size);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				String id = resultSet.getString(USER_ID);
				ids.add(id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return ids;
	}
	
	public List<Review> getUserReviews(String userId, Connection dbConnection){
		List<Review> reviews = new ArrayList<Review>();
		try{
			String sql = "SELECT * FROM REVIEW r WHERE USER_ID = ?;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			prepareStatement.setString(1, userId);
			ResultSet resultSet = prepareStatement.executeQuery();
			while(resultSet.next()){
				String businessId = resultSet.getString(BUSINESS_ID);
				int stars = resultSet.getInt(STARS);
				int computedStars = 0;
				Clob text = resultSet.getClob(TEXT);
				reviews.add(new Review(businessId, userId, stars, computedStars, clobToString(text)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return reviews;
	}
	
	public int getReviewsSize(Connection dbConnection){
		int result = -1;
		try{
			String sql = "SELECT COUNT(*) FROM REVIEW r;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				result = resultSet.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public int userReviewsContainsUser(String userId, Connection dbConnection){
		int result = -1;
		try{
			String sql = "SELECT COUNT(*) FROM REVIEW r WHERE USER_ID = ?;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			prepareStatement.setString(1, userId);
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				result = resultSet.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public int getUserMean(String userId, Connection dbConnection){
		int result = -1;
		try{
			String sql = "SELECT SUM(STARS)/COUNT(*) FROM REVIEW r WHERE USER_ID = ?;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			prepareStatement.setString(1, userId);
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				result = resultSet.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public int getBusinessMean(String businessId, Connection dbConnection){
		int result = -1;
		try{
			String sql = "SELECT SUM(STARS)/COUNT(*) FROM REVIEW r WHERE BUSINESS_ID = ?;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			prepareStatement.setString(1, businessId);
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				result = resultSet.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	public int getAllGoodBusinessSize(Connection dbConnection){
		int result = -1;
		try{
			String sql = "SELECT COUNT(*) FROM (SELECT BUSINESS_ID, SUM(STARS)/COUNT(*) AS MEAN FROM REVIEW r GROUP BY BUSINESS_ID) AS C WHERE C.MEAN >= 4;";
			PreparedStatement prepareStatement = dbConnection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery();
			if(resultSet.next()){
				result = resultSet.getInt(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	private String clobToString(Clob data) {
	    StringBuilder sb = new StringBuilder();
	    try {
	        Reader reader = data.getCharacterStream();
	        BufferedReader br = new BufferedReader(reader);
	        String line;
	        while(null != (line = br.readLine())) {
	            sb.append(line);
	        }
	        br.close();
	    } catch (SQLException e) {
	    } catch (IOException e) {
	    }
	    return sb.toString();
	}
}