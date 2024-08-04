package file;

import java.io.*;

public class WorkFile {
    private String name;

    public WorkFile(String name) {
        this.name = name;
    }

    public String readLog() {
        String result = "";
        try{
            File file = new File(name);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = null; // = reader.readLine();
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveInLog(String msg) {
        try (FileWriter writer = new FileWriter(name, true)){
            writer.write(msg);
            writer.write("\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
