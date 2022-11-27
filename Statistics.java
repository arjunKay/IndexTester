import java.io.*;

public class Statistics {

    private String name;
    private long indexTime;

    public Statistics(String name) {
        this.name = name;
    }

    public void setIndexTime(long indexTime) {
        this.indexTime = indexTime;
    }

    public void writeOut() {
        String path = "stats/" + name + ".txt";

        File f = new File(path);

        try (
                BufferedWriter bw = new BufferedWriter(new FileWriter(f));
                ) {

            bw.write("Index time: " + (indexTime / 1000.0) + "s");

        } catch (IOException e) {
            System.out.println("Error writing out statistics for " + name);
            e.printStackTrace();
        }


    }

}
