package Graphs;


import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Candidate{
    int id;
    boolean active;

    Candidate(){
        id = 0;
        active = true;
    }

    public void addCandidate(){
        System.out.println("Enter id of new candidate");
        Scanner sc = new Scanner(System.in);
        this.id = sc.nextInt();
    }
}
public class Election {
    ArrayList<Candidate> lst = new ArrayList<>();
    int currIndex;

    public void getCandidate(){
        Candidate c = new Candidate();
        c.addCandidate();
        lst.add(c);
    }

    public void ringFailure(){
        int max = 0;

        for(int i = 0; i < lst.size(); i++){
            if(lst.get(i).id > max && lst.get(i).active){
                max = lst.get(i).id;
                currIndex = i;
            }
        }

        System.out.println("Current coordinator is " + lst.get(currIndex).id);
        System.out.println("Current coordinator fails");
        System.out.println("Detected by " + lst.get(currIndex - 1).id);
        lst.get(currIndex).active = false;
        System.out.println("Election initialized");
    }

    public void ringElection(){
        int old = currIndex;
        int new1 = (old - 1);
        int cnt = 0;

        while(new1 != old){
            int n = (new1 + 1) % lst.size();

            if(new1 == old - 1 && cnt == 0){
                n = (new1 + 2) % lst.size();
                System.out.println("Message passed from " + lst.get(new1).id + " to " + lst.get(n).id);
                new1 = n;
                cnt++;
                continue;
            }

            if(lst.get(n).active){
                System.out.println("Message passed from " + lst.get(new1).id + " to " + lst.get(n).id);
                new1 = n;
            }
            else{
                new1 = n;
            }
        }

        int max = 0;

        for(int i = 0; i < lst.size(); i++){
            if(lst.get(i).id > max && lst.get(i).active){
                max = lst.get(i).id;
                currIndex = i;
            }
        }

        System.out.println("Election coordinator is " + lst.get(currIndex).id);
    }

    public void bullyFailure(){
        int max = 0;

        for(int i = 0; i < lst.size(); i++){
            if(lst.get(i).id > max && lst.get(i).active){
                max = lst.get(i).id;
                currIndex = i;
            }
        }

        System.out.println("Current coordinator is : " + lst.get(currIndex));
        System.out.println("Current coordinator fails");
    }

    public void bullyElection(){
        Random r = new Random();
        int old = currIndex;
        int temp = r.nextInt(100) % (lst.size() - 1);
        int new1 = old - temp;

        System.out.println("Detected by " + lst.get(new1));
        lst.get(new1).active = false;
        System.out.println("Election initialized");

        while(true){
            for(int i = new1; i < lst.size() - 1; i++){
                if(lst.get(i).active){
                    System.out.println("Election message from " + lst.get(new1).id + " to " + lst.get(i + 1).id);
                }
            }

            for(int i = lst.size() - 1; i > new1; i--){
                if(lst.get(i).active){
                    System.out.println("OK message from " + lst.get(i).id + " to " + lst.get(new1).id);
                }
            }

            if(new1 < lst.size() - 1 && lst.get(new1).active){
                new1 = new1 + 1;
            }
            else{
                break;
            }
        }

        System.out.println("Elected coordinator is " +lst.get(new1 - 1));
    }

    public static void main(String[] args) {
        Election e = new Election();

    }
}
