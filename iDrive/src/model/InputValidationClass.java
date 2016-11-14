package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidationClass {
	
	
	public boolean dateInputValidation(String date){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date dateThreeDaysFromNow = new Date();
		calendar.setTime(dateThreeDaysFromNow);
		calendar.add(Calendar.DATE, 3);
		dateThreeDaysFromNow = calendar.getTime();
		if(date == null){
			return false;
		} else {
			
			String regex = "[^0-9-]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(date);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(date.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				System.out.println("all valid characters");
				try {
					Date threeDaysFromNow = dateFormat.parse(dateFormat.format(dateThreeDaysFromNow));
					Date inputDate = dateFormat.parse(dateFormat.format(dateFormat.parse(date)));
					
						if(inputDate.equals(threeDaysFromNow) || inputDate.after(threeDaysFromNow)){
							System.out.println("Correct Date, you entered " + inputDate.toString());
							return true;
						} else {
							System.out.println("Wrong date, you entered " + inputDate.toString() + " must be on or before this date: " + threeDaysFromNow.toString());
							return false;
						}
						
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
	}
	
	public boolean timeInputValidation(String hours, String minutes, String timeOfDay){
	
		if(hours == null || minutes == null || timeOfDay == null){
			return false;
		} else {
			
			String regex = "[^0-9]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(hours);
			Matcher matcher1 = pattern.matcher(minutes);
			
			if(matcher.find() == true || matcher1.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(hours.trim().isEmpty() == true || minutes.trim().isEmpty() == true || timeOfDay.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				System.out.println("all valid characters");
				int intHours = Integer.parseInt(hours);
				int intMinutes = Integer.parseInt(minutes);
				
				if(intHours < 1 || intHours > 12){
					return false;
				}
				
				if(intMinutes < 0 || intMinutes > 60){
					return false;
				}
				
				if(timeOfDay.equals("AM") == false && timeOfDay.equals("PM") == false){
					return false;
				}
				return true;
			}

		}
	}
	
	public boolean destinationInputValidation(String text){
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			String regex = "[^A-Za-z0-9 ]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else if(text.length()>50){
				System.out.println("greater than destination length");
				return false;
			}else{
				System.out.println("all valid characters");
				return true;
			}
		}
	}
	
	public boolean purposeInputValidation(String text){
		if(text == null){
			System.out.println("null text");
			return false;
		} else {
			String regex = "[^A-Za-z0-9 .!?,$&-]";
			Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			
			if(matcher.find() == true){
				
				System.out.println("special character found.");
				return false;
				
			} else if(text.trim().isEmpty() == true){
				
				System.out.println("all whitespace");
				return false;
				
			} else {
				System.out.println("all valid characters");
				return true;
			}
		}
	}
	
	public boolean numberInputValidation(String text){
		if(text == null){
			return false;
		} else {
			char check[] = text.toCharArray();
			
			for(int i = 0; i < text.length(); i++){
				if(Character.isDigit(check[i]) == true){
					
				} else {
					return false;
				}
			}
			
			if(Integer.parseInt(text) < 0 || Integer.parseInt(text) > 2){
				return false;
			}
			
			return true;
		}
	}
	
	public boolean passengerInputValidation(String text){
		if(text == null){
			return true;
		} else {
			String regex = "[^A-Za-z .,]";
            Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(text);

            if(matcher.find() == true){

                System.out.println("special character found.");
                return false;

            } else if(text.trim().isEmpty() == true){

                System.out.println("all whitespace");
                return true;

            } else {
                System.out.println("all valid characters");
                return true;
            }
		}
	}
}