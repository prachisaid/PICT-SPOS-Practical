import java.io.*;
import java.util.*;
class Symbol 
{
    public String address;
    public int pointer;
    Symbol() 
    {
        address = "";
    }
    Symbol(String _address, int _pointer) 
    {
        address = _address;
        pointer = _pointer;
    }
}

class Literal 
{
    public String address;
    public int pointer;
    
    Literal() 
    {
        address = "";   
    }
    
    Literal(String _address, int _pointer) 
    {
        address = _address;
        pointer = _pointer;
    }
}

public class PassTwo 
{
    LinkedHashMap<String,Symbol> symb=new LinkedHashMap<>();
    LinkedHashMap<String,Literal> litb=new LinkedHashMap<>();
    public static void main(String[] args) 
    {
        PassTwo assembler = new PassTwo();
        try 
        {
           assembler.passTwo();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
   public void passTwo() throws FileNotFoundException, IOException 
   {
       System.out.println("---------------- Pass Two ---------------------\n");
       BufferedWriter fileBf = new BufferedWriter(new FileWriter("machineCode.txt"));
       BufferedReader icFileBf = new BufferedReader(new FileReader("ic.txt"));
       BufferedReader sym=new BufferedReader(new FileReader("symbolTable.txt"));
       String str1;
       int symbP=1;
       while((str1=sym.readLine())!=null)
       {
	       	String word[]=str1.split("\\t+");
	       	symb.put(word[0], new Symbol(word[1], symbP));
	        symbP++;
       }
       BufferedReader lit=new BufferedReader(new FileReader("literalTable.txt"));
       String str2;
       symbP=1;
       while((str2=lit.readLine())!=null)
       {
       	String word[]=str2.split("\\t+");
       	litb.put(word[0], new Literal(word[1], symbP));
         symbP++;
       }
       String currentLine;
       String [] line;
       while((currentLine = icFileBf.readLine()) != null) 
       {
           if (currentLine.isEmpty())
                continue;
           line = currentLine.split("\\s+");
           if (line[1].equals("IS")) 
           {
               String address = "";
               if (line[4].equals("S")) 
               	{
                   for(Map.Entry<String,Symbol> m: symb.entrySet()) 
                   {
                       Symbol s = m.getValue();
                       if (s.pointer == Integer.parseInt(line[5])) 
                       {
                           address = s.address;
                           break;
                       }
                   }
               }
               else
               { 
                  for(Map.Entry<String,Literal> m: litb.entrySet()) 
                  {
                       Literal s = m.getValue();
                       if (s.pointer == Integer.parseInt(line[5])) 
                       {
                           address = s.address;
                           break;
                       }
                   }
               }
             fileBf.write(line[2] + "\t" + line[3] + "\t" + address + "\n");
             }   
        }
        fileBf.close();
        icFileBf.close();
    }
}