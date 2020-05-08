package datasource;

/**
 * This stores the enums for the questions 
 * @author tl9649
 *
 */
public enum QuestionsEnum {
	//enums for the questions
	/**
	 * 
	 */
	Q1("Question 1"), /**
	 * 
	 */
	Q2("Question 2");
	
	private final String q;
	
	/**
	 * Constructor will get a question and assign it to the instance variable
	 * @param q
	 */
	private QuestionsEnum(String q)
	{
		this.q = q;
	}
	
	/**
	 * This will return a question
	 * @return q
	 */
	public String getQuestion()
	{
		return q;
	}
}
