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
public class PassOne 
{
    LinkedHashMap<String,String> _imperative = new LinkedHashMap();
    LinkedHashMap<String,String> _declarative = new LinkedHashMap();
    LinkedHashMap<String,Symbol> symbolTable = new LinkedHashMap();
    LinkedHashMap<String,Literal> literalTable = new LinkedHashMap();
    ArrayList<Integer> poolTable = new ArrayList();
    public static void main(String[] args) throws Exception
    {
        PassOne assembler = new PassOne ();
        try 
        {
            assembler.passOne();
        } 
        catch (IOException ex) 
        {
            ex.printStackTrace();
        }
    }
    PassOne () 
    {
        init();
    }
    
    private void init() {
        // imperative statements
        _imperative.put("STOP", "00");
        _imperative.put("ADD", "01");
        _imperative.put("SUB", "02");
        _imperative.put("MULT", "03");
        _imperative.put("MOVER", "04");
        _imperative.put("MOVEM", "05");
        _imperative.put("COMP", "06");
        _imperative.put("BC", "07");
        _imperative.put("DIV", "08");
        _imperative.put("READ", "09");
        _imperative.put("PRINT", "10");
        // declarative statements
        _declarative.put("DS", "01");
        _declarative.put("DC", "02");
    }
    public void passOne() throws Exception 
    {
        BufferedReader inputBf = new BufferedReader(new FileReader("input.txt"));
        BufferedWriter outputBf = new BufferedWriter(new FileWriter("ic.txt"));       
        int location = 0;
        int symbolTablePointer = 1;
        int literalTablePointer = 1;
        int poolTablePointer = 1;
        
        String currentLine = inputBf.readLine();
        currentLine = currentLine.toUpperCase();
        String [] line = currentLine.split("\\s+");
        
        if (line[1].equals("START")) 
        {
            location = Integer.parseInt(line[2]);
            outputBf.write("\tAD\t01\tC\t" + location + "\n");
        }
        while((currentLine = inputBf.readLine()) != null)
         {
            boolean isPointerSet = false;
            currentLine = currentLine.toUpperCase();
            line = currentLine.split("[ ,]");
            if (!line[0].isEmpty()) 
            {                
                if (symbolTable.get(line[0]) == null) 
                {
                    symbolTable.put(line[0], new Symbol(String.valueOf(location),
                        symbolTablePointer));
                    symbolTablePointer++;
                }
            }
            // ORIGIN statement
            if (line[1].equals("ORIGIN"))
             {
                Symbol s = symbolTable.get(line[2]);
                int address = Integer.parseInt(s.address);
                
                if (line[3].equals("+")) 
                {
                    address += Integer.parseInt(line[4]);
                } 
                else 
                {
                    address -= Integer.parseInt(line[4]);
                }
                outputBf.write("\t" + "AD\t03\tC\t" + location);
                location = address;
                isPointerSet = true;
            }
            
            // EQU statement
            if (line[1].equals("EQU")) 
            {
                Symbol s = symbolTable.get(line[2]);
                int address = Integer.parseInt(s.address);
                
                if (line[3].equals("+")) 
                {
                    address += Integer.parseInt(line[4]);
                }
               	else 
               	{
                    address -= Integer.parseInt(line[4]);
               	}
                symbolTable.put(line[0], new Symbol(String.valueOf(address),
                s.pointer));
                outputBf.write("\t" + "AD\t04\tC\t" + location);
                isPointerSet = true;
            }
            // LTORG statement
            if (line[1].equals("LTORG")) 
            {
                poolTable.add(poolTablePointer);
                for(Map.Entry<String,Literal> m: literalTable.entrySet()) 
                {
                    Literal l = m.getValue();
                    if (l.address.isEmpty()) 
                    {
                        m.setValue(new Literal(String.valueOf(location),l.pointer));
                        outputBf.write("\t"+ location + "\t" + m.getKey() + "\n");
                        location += 1;
                        isPointerSet = true;
                        poolTablePointer += 1;
                    }
                }
            }
            // declarative statements
            if (_declarative.containsKey(line[1])) 
            {
                if (line[1].equals("DC")) 
                {
                    Symbol s = symbolTable.get(line[0]);
                    symbolTable.put(line[0], new Symbol(String.valueOf(location),
                    s.pointer));
                    outputBf.write(location + "\t" + "DL\t02\tC\t" + line[2]);
                }
                else 
                {
                   Symbol s = symbolTable.get(line[0]);
                   int address = Integer.parseInt(line[2]);
                   symbolTable.put(line[0], new Symbol(String.valueOf(location),s.pointer));
                   outputBf.write(location + "\t" + "DL\t01\tC\t" + address);
                   location += address;
                   isPointerSet = true;
                }
            }
            // imperative statements
            if (_imperative.containsKey(line[1])) 
            	{
                String opcode = _imperative.get(line[1]);
                outputBf.write(location+"\tIS\t" + opcode + "\t");
                // checks for registers
                if (line.length > 2) 
                {
                    switch(line[2]) 
                    {
                        case "AREG" : 
                        	outputBf.write("01\t");
                        	break;
                        case "BREG" : 
                        	outputBf.write("02\t");
                        	break;
                        case "CREG" : 
                        	outputBf.write("03\t");
                        	break;
                        case "DREG" : 
                        	outputBf.write("04\t");
                        	break;
                    }
                }
                // check for symbols or literals
                if (line.length > 3) 
                {
                    if (line[3].contains("=")) 
                    {
                        // literal is present
                        literalTable.put(line[3], new Literal("", 
                        	literalTablePointer));
                        outputBf.write("L\t" + literalTablePointer + "\t");
                        literalTablePointer++;
                    } 
                    else 
                    {
                        // symbole is present
                        if (symbolTable.get(line[3]) == null) 
                        {
                            symbolTable.put(line[3], new Symbol("",symbolTablePointer));
                            outputBf.write("S\t" + symbolTablePointer);
                            symbolTablePointer++;
                        } 
                        else 
                        {
                            // TODO
                            Symbol s = symbolTable.get(line[3]);
                            outputBf.write("S\t" +  s.pointer);
                        }
                    } 
                }
                else if (line.length > 2) 
                {
                        if (symbolTable.get(line[2]) == null) 
                        {
                                symbolTable.put(line[2], new Symbol("",
                                        symbolTablePointer));
                                outputBf.write("S\t" + symbolTablePointer);
                                symbolTablePointer++;
                        } 
                        else 
                        {
                                // TODO
                                Symbol s = symbolTable.get(line[2]);
                                outputBf.write("S\t" +  s.pointer);
                        }
                }
                
            }
            // END statement
            if (line[1].equals("END")) 
            {
                poolTable.add(poolTablePointer);
                for(Map.Entry<String,Literal> m: literalTable.entrySet()) 
                {
                    Literal l = m.getValue();
                    if (l.address.isEmpty()) 
                    {
                        m.setValue(new Literal(String.valueOf(location), 
                                l.pointer));
                        outputBf.write("\t"+ location + "\t" + m.getKey() + "\n");
                        location++;
                        isPointerSet = true;
                    }
                    
                } 
                
                outputBf.write( location+"\tAD\t05\t");
            }
            outputBf.newLine();
            if (isPointerSet == false) 
            {
                location += 1;
            }
        }
        inputBf.close();
        outputBf.close();
        
        FileWriter symbolTableFile = new FileWriter("symbolTable.txt");
        BufferedWriter symbolTableBf = new BufferedWriter(symbolTableFile);
        System.out.println("------------ Symbol Table --------------");
        for(Map.Entry<String,Symbol> m: symbolTable.entrySet()) 
        {
            Symbol s= m.getValue();
            //symbolTableBf.write(s.pointer + "\t" + m.getKey() + "\t" + s.address + "\n");
              symbolTableBf.write(m.getKey() + "\t" + s.address + "\n");
            System.out.println(s.pointer + "\t" + m.getKey() + "\t" + s.address + "\n");
        }
        symbolTableBf.close();
        
        FileWriter literalTableFile = new FileWriter("literalTable.txt");
        BufferedWriter literalTableBf = new BufferedWriter(literalTableFile);
        System.out.println("------------ Literal Table --------------");
        for(Map.Entry<String,Literal> m: literalTable.entrySet()) 
        {
            Literal l= m.getValue();
            literalTableBf.write(m.getKey()+ "\t" + l.address + "\n");
            System.out.println(l.pointer + "\t" + m.getKey()+ "\t" + l.address + "\n");
        }
        literalTableBf.close();
        
        FileWriter poolTableFile = new FileWriter("poolTable.txt");
        BufferedWriter poolTableFileBf = new BufferedWriter(poolTableFile);
        System.out.println("------------ Pool Table --------------");
        for(Integer m: poolTable) 
        {
            poolTableFileBf.write(m + "\n");
            System.out.println(m);
        }
        poolTableFileBf.close();
    }
}