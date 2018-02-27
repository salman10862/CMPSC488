// Credit: Solution inspired by the following StackOverflow page:
// https://stackoverflow.com/questions/1415008/storing-names-and-other-information

import java.io.*;
import java.util.ArrayList;
import java.util.List;

//Class to aid in storing and retrieving local user data
public class userStore {
    private File storage; //Default value;

    public userStore(File storage){
        this.storage = storage;
    }

    public void saveUser(userProfile user) throws IOException {
        BufferedWriter wr = new BufferedWriter(new FileWriter(storage, true));
        wr.write(user.getUsername() + "," + user.getPassHash());
        wr.newLine();
        wr.close();
    }

    public List<userProfile> loadUserList() throws IOException {
        List<userProfile> users = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(storage));
        String line;
        while((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            userProfile u = new userProfile(parts[0], parts[1]);
            users.add(u);
        }

        br.close();

        return users;


    }

}
