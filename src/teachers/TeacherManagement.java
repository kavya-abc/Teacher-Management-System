package teachers;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
class Teacher 
{
    private String name;
    private int age;
    private Date dateOfBirth; 
    private int classesTaught; 

    public Teacher(String name, int age, Date dateOfBirth, int classesTaught) 
    {
        this.name = name;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.classesTaught = classesTaught;
    }

    public String getName() 
    {
        return name;
    }

    public int getAge()
    {
        return age;
    }

    public Date getDateOfBirth() 
    {
        return dateOfBirth;
    }

    public int getClassesTaught() 
    {
        return classesTaught;
    }

    public void setName(String name) 
    {
        this.name = name;
    }

    public void setAge(int age) 
    {
        this.age = age;
    }

    public void setDateOfBirth(Date dateOfBirth) 
    {
        this.dateOfBirth = dateOfBirth;
    }

    public void setClassesTaught(int classesTaught) 
    {
        this.classesTaught = classesTaught;
    }

    @Override
    public String toString() 
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
        String dobString = sdf.format(dateOfBirth);
        return "Name: " + name + ", Age: " + age + ", Date of Birth: " + dobString + ", Classes Taught: " + classesTaught;
    }
}

class TeacherManagementSystem 
{
    private static final String FILE_PATH = System.getProperty("user.home") + "\\Desktop\\teachers.txt";
    public void addTeacher(Teacher teacher) 
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dobString = sdf.format(teacher.getDateOfBirth());
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true)))
        {
            bw.write(teacher.getName() + ", " + teacher.getAge() + ", " + dobString + ", " + teacher.getClassesTaught());
            bw.newLine();
            System.out.println("Teacher added successfully!");
        } catch (IOException e)
        {
            System.out.println("Error writing to file!");
        }
    }

    public List<Teacher> getAllTeachers() 
    {
        List<Teacher> teachers = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) 
        {
            System.out.println("File does not exist!");
            return teachers;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH)))
        {
            String line;
            while ((line = br.readLine()) != null) 
            {
                String[] data = line.split(",");
                String name = data[0].trim();
                int age = Integer.parseInt(data[1].trim());
                Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse(data[2].trim()); // Parsing dateOfBirth correctly
                int classesTaught = Integer.parseInt(data[3].trim());
                Teacher teacher = new Teacher(name, age, dateOfBirth, classesTaught);
                teachers.add(teacher);
            }
        } catch (IOException | NumberFormatException | ParseException e) 
        {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return teachers;
    }
    
    public List<Teacher> filterTeachersByAge(int age)
    {
        List<Teacher> filteredTeachers = new ArrayList<>();
        List<Teacher> allTeachers = getAllTeachers();
        for (Teacher teacher : allTeachers) 
        {
            if (teacher.getAge() == age) 
            {
                filteredTeachers.add(teacher);
            }
        }
        return filteredTeachers;
    }

    public List<Teacher> filterTeachersByClasses(int classes) 
    {
        List<Teacher> filteredTeachers = new ArrayList<>();
        List<Teacher> allTeachers = getAllTeachers();
        for (Teacher teacher : allTeachers) 
        {
            if (teacher.getClassesTaught() == classes) 
            {
                filteredTeachers.add(teacher);
            }
        }
        return filteredTeachers;
    }

    public Teacher searchTeacherByName(String name) 
    {
        List<Teacher> allTeachers = getAllTeachers();
        for (Teacher teacher : allTeachers)
        {
            if (teacher.getName().equalsIgnoreCase(name)) 
            {
                return teacher;
            }
        }
        return null;
    }

    public void updateTeacher(String name, Teacher newTeacher)
    {
        List<Teacher> allTeachers = getAllTeachers();
        boolean found = false;
        for (int i = 0; i < allTeachers.size(); i++) 
        {
            if (allTeachers.get(i).getName().equalsIgnoreCase(name))
            {
                allTeachers.set(i, newTeacher);
                updateFile(allTeachers);
                found = true;
                break;
            }
        }
        if (!found) 
        {
            System.out.println("Teacher not found!");
        } else
        {
            System.out.println("Teacher record updated successfully!");
        }
    }

    public void deleteTeacher(String name)
    {
        List<Teacher> allTeachers = getAllTeachers();
        Iterator<Teacher> iterator = allTeachers.iterator();
        boolean found = false;
        while (iterator.hasNext())
        {
            if (iterator.next().getName().equalsIgnoreCase(name)) 
            {
                iterator.remove();
                updateFile(allTeachers);
                found = true;
                break;
            }
        }
        if (!found) 
        {
            System.out.println("Teacher not found!");
        } else 
        {
            System.out.println("Teacher deleted successfully!");
        }
    }

    private void updateFile(List<Teacher> teachers) 
    {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH)))
        {
            for (Teacher teacher : teachers) 
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dobString = sdf.format(teacher.getDateOfBirth());
                bw.write(teacher.getName() + ", " + teacher.getAge() + ", " + dobString + ", " + teacher.getClassesTaught());
                bw.newLine();
            }
        } 
        catch (IOException e) 
        {
            System.out.println("Error writing to file!");
        }
    }
}

public class TeacherManagement
{
    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        TeacherManagementSystem tms = new TeacherManagementSystem();
        while (true) 
        {
            System.out.println("\n***** WELCOME TO TEACHER MANAGEMENT SYSTEM *****");
            System.out.println("1. Add a Teacher");
            System.out.println("2. Show All Teachers");
            System.out.println("3. Filter Teachers by Age");
            System.out.println("4. Filter Teachers by the number of classes");
            System.out.println("5. Search for a Teacher");
            System.out.println("6. Update a Teacher's Record");
            System.out.println("7. Delete a Teacher");
            System.out.println("8. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); 

            switch (choice) 
            {
                case 1:
                    System.out.print("Enter teacher name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter teacher age: ");
                    int age = sc.nextInt();
                    sc.nextLine(); 
                    System.out.print("Enter teacher date of birth (yyyy-MM-dd): ");
                    String dobStr = sc.nextLine();
                    Date dob = null;
                    try 
                    {
                        dob = new SimpleDateFormat("yyyy-MM-dd").parse(dobStr);
                    } 
                    catch (ParseException e) {
                        System.out.println("Invalid date format! Please enter date in yyyy-MM-dd format.");
                        continue;
                    }
                    System.out.print("Enter number of classes taught: ");
                    int classesTaught = sc.nextInt();
                    sc.nextLine(); 
                    Teacher teacher = new Teacher(name, age, dob, classesTaught);
                    tms.addTeacher(teacher);
                    break;
                case 2:
                    List<Teacher> allTeachers = tms.getAllTeachers();
                    if (allTeachers.isEmpty())
                    {
                        System.out.println("No teachers found!");
                    } else 
                    {
                        System.out.println("All Teachers:");
                        for (Teacher t : allTeachers)
                        {
                            System.out.println(t);
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter age to filter teachers: ");
                    int filterAge = sc.nextInt();
                    sc.nextLine(); 
                    List<Teacher> ageFilteredTeachers = tms.filterTeachersByAge(filterAge);
                    if (ageFilteredTeachers.isEmpty())
                    {
                        System.out.println("No teachers found for the given age!");
                    } 
                    else 
                    {
                        System.out.println("Teachers filtered by age:");
                        for (Teacher t : ageFilteredTeachers)
                        {
                            System.out.println(t);
                        }
                    }
                    break;
                case 4:
                    System.out.print("Enter number of classes to filter teachers: ");
                    int filterClasses = sc.nextInt();
                    sc.nextLine(); 
                    List<Teacher> classesFilteredTeachers = tms.filterTeachersByClasses(filterClasses);
                    if (classesFilteredTeachers.isEmpty()) 
                    {
                        System.out.println("No teachers found for the given number of classes!");
                    } 
                    else 
                    {
                        System.out.println("Teachers filtered by number of classes taught:");
                        for (Teacher t : classesFilteredTeachers) {
                            System.out.println(t);
                        }
                    }
                    break;
                case 5:
                    System.out.print("Enter the name of the teacher to search: ");
                    String searchName = sc.nextLine();
                    Teacher searchedTeacher = tms.searchTeacherByName(searchName);
                    if (searchedTeacher != null) 
                    {
                        System.out.println("Teacher found:");
                        System.out.println(searchedTeacher);
                    } 
                    else 
                    {
                        System.out.println("Teacher not found!");
                    }
                    break;
                case 6:
                    System.out.print("Enter the name of the teacher to update: ");
                    String updateName = sc.nextLine();
                    Teacher updateTeacher = tms.searchTeacherByName(updateName);
                    if (updateTeacher != null) 
                    {
                        System.out.print("Enter updated teacher name: ");
                        String updatedName = sc.nextLine();
                        System.out.print("Enter updated teacher age: ");
                        int updatedAge = sc.nextInt();
                        sc.nextLine(); 
                        System.out.print("Enter updated teacher date of birth (yyyy-MM-dd): ");
                        String updatedDobStr = sc.nextLine();
                        Date updatedDob = null;
                        try 
                        {
                            updatedDob = new SimpleDateFormat("yyyy-MM-dd").parse(updatedDobStr);
                        } 
                        catch (ParseException e)
                        {
                            System.out.println("Invalid date format! Please enter date in yyyy-MM-dd format.");
                            continue;
                        }
                        System.out.print("Enter updated number of classes taught: ");
                        int updatedClassesTaught = sc.nextInt();
                        sc.nextLine(); 
                        Teacher updatedTeacher = new Teacher(updatedName, updatedAge, updatedDob, updatedClassesTaught);
                        tms.updateTeacher(updateName, updatedTeacher);
                    } 
                    else 
                    {
                        System.out.println("Teacher not found!");
                    }
                    break;
                case 7:
                    System.out.print("Enter the name of the teacher to delete: ");
                    String deleteName = sc.nextLine();
                    tms.deleteTeacher(deleteName);
                    break;
                case 8:
                    System.out.println("Exiting program...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice! Please enter a valid choice.");
            }
        }
    }
}
