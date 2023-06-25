package webschoolproject;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import static com.mongodb.client.model.Filters.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;


@SuppressWarnings("unchecked")
public class Backendw {
    //secret
    private static String uri = "mongodb+srv://admin:admin@yonadb.jii65y7.mongodb.net/test";
    private static MongoClientURI clientURI = new MongoClientURI(uri);
    private static MongoClient mongoClient = new MongoClient(clientURI);
    private static MongoDatabase mongoDatabase = mongoClient.getDatabase("yoavTest");
    private static MongoCollection<Document> collection = mongoDatabase.getCollection("avivim");

    static Gson gson = new Gson();

    private static List<String> subjects = Arrays.asList("english", "hebrew", "java", "math", "sport", "science", "history", "python", "assembly", "chemistry", "poetry", "physics");
    public static List<String> allClasses = Arrays.asList("1a", "2a", "3a", "1b", "2b", "3b", "1c","2c","3c","1d", "2d", "3d", "1e", "2e", "3e",
    "1f", "2f", "3f", "1g", "2g", "3g", "1h","2h","3h","1i", "2i", "3i", "1j", "2j", "3j", "1k", "2k", "3k", "1l", "2l", "3l");


    
    public Backendw () throws IOException {}


    //gets a filter and a school and returns the ObjectId (_id)
    public static Object getObjectIdOfUser(String documentName, String value, String school) {
        collection = mongoDatabase.getCollection(school);

        return (ObjectId) ((Document) collection.find(eq(documentName, value)).first()).getObjectId("_id");
    }





    //gets a name of an object, school and returns the documents ObjectId
    public static Object getObjectIdFromInfo(String documentName, String school) {
        collection = mongoDatabase.getCollection(school + "_info");//need to think if i ask school/collection

        return (ObjectId) ((Document) collection.find(eq(documentName, new Document("$exists", true))).first()).get("_id");
    }




    //gets an objectId, request key and value, collectionString and
    public static void changeData (Object objectId, User.Request req ,String key, Object value, String collectionString) {
        collection = mongoDatabase.getCollection(collectionString);
        Bson filter = eq("_id", objectId);
        BiFunction<String, Object, Bson> updateMethod;
        
        switch(req) {
            case add:
                updateMethod = Updates::push;
                break;
            case removeValue:
                updateMethod = Updates::pull;
                break;
            case replace:
                updateMethod = Updates::set;
                break;
            default:
                throw new IllegalArgumentException("Invalid request type: " + req);
        }
    
        collection.updateOne(filter, updateMethod.apply(key, value));
        //exampels
        // grades.math for inside student
        //from school_info: classes.0.1a.homework
        //schedule example classesSchedule1a.0.0

        // add data
        // collection.updateOne(
        // studentFilter,
        //     Updates.push("grades.math", newValue)
        // );


        //replace
        // collection.updateOne(
        //     filter,
        //     Updates.set("classes.0.1a.students.0", "amir mar")
        // );


        //delete one value
        // collection.updateOne(
        //     filter,
        //     Updates.pull("classes.0.1a.homework", "new homework")
        // );

        //scedule thingi
        // collection.updateOne(
        //     filter,
        //     Updates.set("classesSchedule3l.0.0", "english")
        // );
    }




    //in case DB.json is empty this function init DB
    public void initializer(String school) throws IOException {

        mongoDatabase.createCollection(school);
        collection = mongoDatabase.getCollection(school + "_info");

        List<Document> lista = new ArrayList<>();
        collection.insertOne(new Document("classes", lista));

        Object obid = Backendw.getObjectIdFromInfo("classes", school);

        for (String grade : Backendw.allClasses) {
            Map<String,List<String>> ts = new HashMap<>();
            List<String> em = new ArrayList<>();
            ts.put("teachers", em);
            ts.put("students", em);
            ts.put("homework", em);

            changeData(obid, User.Request.add, "classes.0." + grade, ts, school+"_info");
            collection.insertOne(new Document("classesSchedule" + grade, defaultSchedule()));
        }
    }







    //returns all the data of the current school, maybe we will have to work on this
    public static List<Document> viewAllSchool(String collectionString) {
        collection = mongoDatabase.getCollection(collectionString);
    
        return (List<Document>) collection.find().into(new ArrayList<>());
    }





    public static Object getInfo(String key, String school) {
        collection = mongoDatabase.getCollection(school + "_info");

        return ((Document) collection.find(new BasicDBObject(key, new BasicDBObject("$exists", true))).first()).get(key);
    }


    



    //gets a teacher and prints his students list
    public static Map<String, ArrayList<String>> getStudentsList(ArrayList<String> grades, String school) {

        Map<String, ArrayList<String>> result = new HashMap<>();
        List<Map<String, ArrayList<Map<String,ArrayList<String>>>>> map = (List<Map<String, ArrayList<Map<String,ArrayList<String>>>>> ) getInfo("classes", school);
        
        for (String grade : grades) {
            result.put(grade, map.get(0).get(grade).get(0).get("students"));
        }
        return result;
    }
    
    




    //gets a student and returns his teacher's list
    public static List<String> getTeachersList(Student student){
        List<Map<String, ArrayList<Map<String,ArrayList<String>>>>> map = (List<Map<String, ArrayList<Map<String,ArrayList<String>>>>> ) getInfo("classes", student.getSchool());
        
        return map.get(0).get(student.getGrade()).get(0).get("teachers");
    }








    public static ArrayList<ArrayList<String>> defaultSchedule() {
        ArrayList<ArrayList<String>> schedule = new ArrayList<ArrayList<String>>();
    
        for(int i=0;i<5;i++) {
            ArrayList<String> hour = new ArrayList<>(Arrays.asList("", "", "", "", ""));
            schedule.add(hour);
        }
    
        return schedule;
    }






    //remeber to create to smae one to teacher
    public static ArrayList<ArrayList<String>> getSchedule(String grade, String school) {
        return (ArrayList<ArrayList<String>>) getInfo("classesSchedule" + grade, school);
    }





    


    public static List<String> getHomeworkClass(String grade, String school) {
        List<Map<String, ArrayList<Map<String,ArrayList<String>>>>> map = (List<Map<String, ArrayList<Map<String,ArrayList<String>>>>> ) getInfo("classes", school);
        
        return map.get(0).get(grade).get(0).get("homework");
    }





    //need to work on this as well
    //gets a user, adds the user to the db remeber to add every thing that is connected to tihs user
    public static void addUser(User user) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(user.getSchool());
        Document doc = Document.parse(Backendw.gson.toJson(user));
        collection.insertOne(doc);
    
        if (user instanceof Student) {
            Student student = (Student) user;
            String grade = student.getGrade();
            changeData(getObjectIdFromInfo("classes", user.getSchool()), User.Request.add, 
                "classes.0." + grade + ".0.students", student.getFirstName() + " " + student.getLastName(), user.getSchool() + "_info");
        } else if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            for (String grade : teacher.getClassesList()) {
                changeData(getObjectIdFromInfo("classes", user.getSchool()), User.Request.add, 
                    "classes.0." + grade + ".0.teachers", 
                    teacher.getFirstName() + " " + teacher.getLastName() + " (" + teacher.getSubject() + ")", user.getSchool() + "_info");
            }
        }
    }



    //need to work on this remeber to delete every thing that is connected to tihs user
    public static void deleteUser(Bson filter, String school) throws IOException {
        User user = getUser(filter);
        if (user == null) {
            return;
        }
    
        String collectionString = school + "_info";
        Object objectId = getObjectIdFromInfo("classes", school);
    
        if (user instanceof Student) {
            Student student = (Student) user;
            String grade = student.getGrade();
            String studentName = student.getFirstName() + " " + student.getLastName();
            changeData(objectId, User.Request.removeValue, "classes.0." + grade + ".0.students", studentName, collectionString);
        } else if (user instanceof Teacher) {
            Teacher teacher = (Teacher) user;
            for (String grade : teacher.getClassesList()) {
                String teacherName = teacher.getFirstName() + " " + teacher.getLastName() + " (" + teacher.getSubject() + ")";
                changeData(objectId, User.Request.removeValue, "classes.0." + grade + ".0.teachers", teacherName, collectionString);
            }
        }
    
        collection = mongoDatabase.getCollection(school);
        collection.deleteOne(filter);
    }




    //gets a filter if exists retrun user else return null
    public static User getUser(Bson filter) {

        for (String collectionName : mongoDatabase.listCollectionNames()) {
            if(!collectionName.endsWith("_info")) {

                collection = mongoDatabase.getCollection(collectionName);
                Document userDoc = (Document) collection.find(filter).first();
            
                if (userDoc != null) {
                    User user = null;
            
                    if (userDoc.containsKey("subject")) {
                        user = gson.fromJson(userDoc.toJson(), Teacher.class);
                    } else if (userDoc.containsKey("grade")) {
                        user = gson.fromJson(userDoc.toJson(), Student.class);
                    } else {
                        user = gson.fromJson(userDoc.toJson(), Admin.class);
                    }
            
                    return user;
                }
            }
        }                
        return null;
    }




    //returns all the subjects
    public static List<String> getSubjects() {
        return Backendw.subjects;
    }




    //gets a String subject and checks if the subject exists returns true for exists else returns false
    public static Boolean hasSubject(String subject) {
        return Backendw.subjects.contains(subject);
    }




    //gets a String grade and checks if the class exists returns true for exists else returns false
    public static Boolean hasClass(String grade) {
        return Backendw.allClasses.contains(grade);
    }
}
