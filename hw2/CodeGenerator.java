
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CodeGenerator{
    public static List<String> SortingList = new ArrayList<>();
    public static List<String> OutputList = new ArrayList<>();
    public static List<String> FileContent = new ArrayList<>();
    public static String OutputClass,content,filename,Filetitle;

    public static void main(String[] args) {
        
        String mermaidCode ="";

        if (args.length == 0) {
            System.err.println("請輸入mermaid檔案名稱");           
        }
        else {
            String fileName = args[0];
           try {
            mermaidCode = Files.readString(Paths.get(fileName));
            //System.out.println(mermaidCode);
       }catch (IOException e) {
            System.err.println("無法讀取文件");
            e.printStackTrace();    
    }
    //pre-work&分離檔案
    String[] look = mermaidCode.trim().split("\n");
    //for(String lookl : look){
//System.out.println(lookl);
    //}
    //Outclass search
    OutputList = OutputlistSearching.SearchingFilename(look);
    //file create and initialize
    FileWritting.filewitter(OutputList);
    //deal with challenge
    for(int i=0;i<look.length;i++){
        look[i] = look[i].replaceAll("\\s+"," ").trim();
        if(look[i].contains("{") == true){
            //System.out.println(look[i]);
            Pattern pattern = Pattern.compile("class\\s+(\\w+)");
            Matcher matcher = pattern.matcher(look[i]);
            if(matcher.find()){
            filename = matcher.group(1);
            }else{
                break;
            }
            for(int j=i;j<look.length;j++){
                StringBuffer str = new StringBuffer();
                str.append(look[j]);
                //System.out.println(look[j]);
                int a = str.indexOf("+");
                while(a != -1){
                    str.insert(a,filename+" : ");
                    a = str.indexOf("+",a+filename.length()+4);
                }
                    a = str.indexOf("-");
                while (a != -1) {
                    str.insert(a,filename+" : ");
                    a = str.indexOf("-",a+filename.length()+4);
                }
                look[j] =str.toString().trim();
                if(look[j].contains("}") == true){
                    i=j;
                    break;
                }
            }
        }
    }
    //deleteuselessvalue
    look = removeuselesslValues(look);
    for(int i =0;i<look.length;i++){
        look[i] = look[i].replaceAll("[{}]"," ").replaceAll("\\s+"," ").replaceAll("\\[\\s*\\]","\\[\\]").trim();
    }
    //classify 
    for(int i=0;i<look.length;i++){
        Pattern pattern = Pattern.compile("(\\S+)\\s*:");
        Matcher matcher = pattern.matcher(look[i]);
        if(matcher.find()==true){
        Filetitle = matcher.group(1);
        }
        if(look[i].contains("(") == false){
            look[i] = definetype.type(look[i]);
        }else{
        pattern = Pattern.compile("get[A-Z]");
        matcher = pattern.matcher(look[i]);
        if(matcher.find()==true){
            look[i] = getFunction.getfunction(look[i]);
        }else{
        pattern = Pattern.compile("set[A-Z]");
        matcher = pattern.matcher(look[i]);
        if(matcher.find()==true){
            look[i] = setFunction.setfunction(look[i]);
        }else{
            look[i] = normalfunction.NFuction(look[i]);
                }
           } 
        }
        //writing
        try {
            FileWriter prewritter =  new FileWriter(Filetitle+".java",true);
            prewritter.write(look[i]);
            prewritter.close();
    } catch(IOException e){
        e.printStackTrace();
    }
    }
}
//關閉文件
int t;
List<String> listWithoutDuplicates = OutputList.stream().distinct().collect(Collectors.toList());
for(t=0;t<listWithoutDuplicates.size();t++){
try {
    String CloseContent = listWithoutDuplicates.get(t);
    FileWriter prewritter =  new FileWriter(CloseContent+".java",true);
    String lastwork = "\n}";
    prewritter.write(lastwork);
    prewritter.close();

} catch(IOException e){
e.printStackTrace();
}
}

}
class OutputlistSearching{
        public static List<String> SearchingFilename(String[] inputarray){
            List<String> Output = new ArrayList<>();
            for(int i=0;i<inputarray.length;i++){
                Pattern pattern = Pattern.compile("class\s+(\\w+)");
                Matcher matcher = pattern.matcher(inputarray[i]);
                if(matcher.find() == true){
                    Output.add(matcher.group(1));
                }
            }
         return Output;   
        }
}

class FileWritting{
    public static void filewitter(List<String> input){
        try {
            for(int i=0;i<input.size();i++){
            String output = input.get(i)+".java";
            File file = new File(output);
            String filecontent;
            filecontent = "public class "+input.get(i)+" {"; 
            if (!file.exists()) {
                file.createNewFile();
            //System.out.println("Java class has been generated: " + output);
            }
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(filecontent);
                bw.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
            }
        }catch (IOException e) {
            e.printStackTrace(); 
    }
}
}

private static String[] removeuselesslValues(String[] array) {
    int Count = 0;
    for(int i = 0;i <array.length;i++){
        if(array[i].contains(":")){
            Count++;
    }
}
String[] newArray = new String[Count];
int newIndex = 0;
for(int i=0;i<array.length;i++){
    if(array[i].contains(":")){
        newArray[newIndex] = array[i];
        newIndex++;
    }
}
return newArray;
    }
}

class definetype{
    public static String Outputdefinetype;
    public static String publicorprivate;
    public static String type;
    public static String name;
    public static String type(String input){
        Map<String, String> map = new HashMap<>();
        map.put("+","public");
        map.put("-","private");
        
        Pattern pattern = Pattern.compile("(\\+|\\-)\\s*(\\S+\\s*\\[*\\s*\\]*)\\s*(\\S+)");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find() == true){
            publicorprivate = matcher.group(1);
            type = matcher.group(2).replace(" ","");
            name = matcher.group(3);
            Outputdefinetype = "\n    "+map.get(publicorprivate)+" "+type+" "+name+ ";";
        }
        return Outputdefinetype;
    }
}
class getFunction{
    public static String Outputdefinetype;
    public static String publicorprivate;
    public static String type;
    public static String name;
    public static String getfunction(String input){
        Map<String, String> map = new HashMap<>();
        map.put("+","public");
        map.put("-","private");
        Pattern pattern = Pattern.compile("(\\+|\\-)\\s*(get([A-Z]\\w+))\\W+(.+)");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find() == true){
        publicorprivate = matcher.group(1);
         
        if(matcher.group(4).contains(")")){
            type = "void";
        }else{
            type = matcher.group(4);
        }
        name = matcher.group(2);
        String namename = matcher.group(3);
        namename = namename.substring(0, 1).toLowerCase() + namename.substring(1);
        Outputdefinetype = "\n    "+map.get(publicorprivate)+" "+type+" "+name+"() {\n        return "+namename+";"+"\n    }";
        //System.out.println(Outputdefinetype);
        }
        return Outputdefinetype;
    }
}
class setFunction{
    public static String Outputdefinetype;
    public static String publicorprivate;
    public static String type;
    public static String name;
    public static String argument;
    public static String givename;
    public static String setfunction(String input){
        Map<String, String> map = new HashMap<>();
        map.put("+","public");
        map.put("-","private");
        Pattern pattern = Pattern.compile("(\\+|\\-)\\s*(set([A-Z]\\w+))\\W+(\\S+\\s*\\[*\\s*\\]*)\\W+(\\w+)\\W+(.+)*");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find() == true){
        publicorprivate = matcher.group(1);
         
        if(matcher.group(6) == null){
            type = "void";
        }else{
            type = matcher.group(6);
        }
        name = matcher.group(2);
        argument = matcher.group(4).replace(" ","");
        givename = matcher.group(5);
        String namename = matcher.group(3);
        namename = namename.substring(0, 1).toLowerCase() + namename.substring(1);
        Outputdefinetype = "\n    "+map.get(publicorprivate)+" "+type+" "+name+"("+argument+" "+givename+") {\n        this."+namename+" = "+givename+";"+"\n    }";
        //System.out.println(Outputdefinetype);
        }
        return Outputdefinetype;
    }
}
class normalfunction{
        public static String Outputdefinetype;
        public static String publicorprivate;
        public static String type;
        public static String name;
        public static String argument;
        public static String givename;
    public static String NFuction(String input){
        Map<String, String> map = new HashMap<>();
        map.put("+","public");
        map.put("-","private");
        map.put("int","{return 0;}");
        map.put("boolean","{return false;}");
        map.put("String","{return \"\";}");
        map.put("void","{;}");
        Pattern pattern = Pattern.compile("(\\+|\\-)\\s*(\\w+)\\s*\\(\\s*(.+)*\\)\\s*(\\w*)");
        Matcher matcher = pattern.matcher(input);
        if(matcher.find() == true){
        publicorprivate = matcher.group(1);
        if(matcher.group(4) == ""){
            type = "void";
        }else{
            type = matcher.group(4);
        }
        name = matcher.group(2);
        if(matcher.group(3) == null){
            argument = "";
        }else{
            argument = matcher.group(3);
            argument = argument.trim();
        }
    
        StringBuffer str = new StringBuffer();
                str.append(argument);
                //System.out.println(argument);
                int a = str.indexOf(",");
                while(a != -1){
                    if(str.charAt(str.indexOf(",",a)-1) == ' '){
                        str = str.replace(a-1,a,"");
                        a = str.indexOf(",",a+2);
                    }else{
                        a = str.indexOf(",",a+3);
                    }
                }
                int b = str.indexOf(",");
                while(b != -1){
                    if(str.charAt(str.indexOf(",",b)+1) != ' '){
                        str.insert(b+1," ");
                        b = str.indexOf(",",b+1);
                    }else{
                        b = str.indexOf(",",b+1);
                    }
                }
                argument = str.toString(); 
        Outputdefinetype = "\n    "+map.get(publicorprivate)+" "+type+" "+name+"("+argument+") "+map.get(type);
        //System.out.println(Outputdefinetype);
        }
        return Outputdefinetype;
    }
}
