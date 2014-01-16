package tr.edu.iyte.ceng389.finalexamstuff;

public class Student
{
	public enum Departments
	{
		CENG, ME, MBG;
	}
	
	private long id;
	private String name;
	private int number;
	private Departments department;
	
	public Student(String name, int number, Departments department)
	{
		this(-1, name, number, department);
	}
	
	public Student(long id, String name, int number, Departments department)
	{
		setId(id);
		setName(name);
		setNumber(number);
		setDepartment(department);
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	/**
	 * @return the department
	 */
	public Departments getDepartment() {
		return department;
	}
	/**
	 * @param department the department to set
	 */
	public void setDepartment(Departments department) {
		this.department = department;
	}
}