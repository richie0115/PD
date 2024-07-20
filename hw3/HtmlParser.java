import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlParser {
    
    public static String url = "https://pd2-hw3.netdb.csie.ncku.edu.tw/";
    public static List<String> checkList = new ArrayList<>();
    public static List<String> newCheckList = new ArrayList<>();
    public static List<String> inputCheckList = new ArrayList<>();
    public static void main(String[] args) {

        File file = new File("data.csv");
    try {
        if (!file.exists()) {
            file.createNewFile();}
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line; 
        while ((line = reader.readLine()) != null) {
        inputCheckList.add(line);
        }
        reader.close();
        } catch (IOException e) {
         e.printStackTrace();
        }

        if(args.length == 1 && args[0].equals("0")){
            try {
                Document doc = Jsoup.connect(url).get();    
                System.out.println(doc.title()+"\n");                
                String StringforStockTitle = doc.select("th").text().toString().replace(" ",",");
                String ValueString = doc.select("td").text().toString().replace(" ",",");
                System.out.println(ValueString);
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = reader.readLine()) != null) {
                    checkList.add(line);
                    }
                    reader.close();
                    } catch (IOException e) {
                     e.printStackTrace();
                    }
                    //writing
                    Pattern pattern = Pattern.compile("day\\d+");
                    Matcher matcher = pattern.matcher(doc.title());
                    if(checkList.contains(doc.title()+"-->"+ValueString+",")==false && matcher.find() == true) {
                        try(FileWriter bw = new FileWriter("data.csv",true)) {
                            bw.write("\n"+doc.title()+"-->");
                            bw.write(ValueString+",");
                            bw.close();
                        }catch (IOException e) {
                        e.printStackTrace();
                        }
                    }
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(file));
                        String line;
                        while ((line = reader.readLine()) != null) {
                        newCheckList.add(line);
                        }
                        reader.close();
                        } catch (IOException e) {
                         e.printStackTrace();
                        }
                    //sorting
                        newCheckList = Sorting(newCheckList);
                    //re-write
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                        bw.write(StringforStockTitle+"\n");
                        for(int i=0;i<newCheckList.size();i++){
                            bw.write(newCheckList.get(i)+"\n");
                        }
                        bw.close();
                    }           
        } catch(IOException e){
            e.printStackTrace();
        }
    }if(args.length == 2 && args[0].equals("1") && args[1].equals("0")){
        mode1OutputMode.outputToCsv(inputCheckList);
    }if(args.length == 5 && args[0].equals("1") && args[1].equals("1")){
        String stockName = args[2];
        int startTime = Integer.parseInt(args[3]);
        int endTime = Integer.parseInt(args[4]);
        Pattern pattern = Pattern.compile("(\\S+,)"+stockName+",");
        Matcher matcher = pattern.matcher(inputCheckList.get(0));
        //find position
        int count = 0;
        if(matcher.find()==true) {
            String father = matcher.group(1);
            while (1 <= father.length()){
                if (matcher.group(1).indexOf(",") != -1) {
                    father = father.substring(father.indexOf(",") + 1, father.length());
                    count++;
                }else{
                    break;
                }
            }
        }
        String output = "";
        pattern = Pattern.compile("(\\d+\\.*\\d+,)");
        double[] smaValue = new double[5];
        for(int i = startTime;i<endTime-3;i++) {
            for(int j = 0;j<5;j++) {
                int time = 0;
                String Value = inputCheckList.get(i+j).replaceAll("\\w++-->","");
                String outputValue;
            while (time <= count){
                if (Value.indexOf(",") != -1) {
                    outputValue = Value.substring(0,Value.indexOf(","));
                    Value = Value.substring(Value.indexOf(",") + 1, Value.length());
                    time++;
                    smaValue[j] = Double.valueOf(outputValue);
                }else{
                    break;
                }
            }
        }
        int ifint;
        if(Mathtime.SimpleMovingAverage(smaValue)%1 == 0){
            ifint = Mathtime.ifInt(Mathtime.SimpleMovingAverage(smaValue));
            output = output+ifint+",";
        }else{
            output = output +Mathtime.SimpleMovingAverage(smaValue)+",";
        }
        }
        pattern = Pattern.compile("(.+),");
        matcher = pattern.matcher(output);
        if(matcher.find()) {
            output = matcher.group(1);
        }
        //output to output.csv
        try {
            if (!file.exists()) {
                file.createNewFile();}
                FileWriter bw = new FileWriter("output.csv",true);
                bw.write(stockName+","+startTime+","+endTime+"\n");
                bw.write(output+"\n");
                bw.close();
                //bw.write();
            } catch(IOException e) {
                e.printStackTrace();
            }

    } if(args.length == 5 && args[0].equals("1") && args[1].equals("2")){
        String stockName = args[2];
        String output = "";
        int startTime = Integer.parseInt(args[3]);
        int endTime = Integer.parseInt(args[4]);
        int days = endTime - startTime + 1;
        double[] sdValue = new double[days];
        //find position
        Pattern pattern = Pattern.compile("(\\S+,)"+stockName+",");
        Matcher matcher = pattern.matcher(inputCheckList.get(0));
        int count = 0;
        if(matcher.find()==true) {
            String father = matcher.group(1);
            while (1 <= father.length()){
                if (matcher.group(1).indexOf(",") != -1) {
                    father = father.substring(father.indexOf(",") + 1, father.length());
                    count++;
                }else{
                    break;
                }
            }
        }
            for(int j = 0;j<endTime-startTime+1;j++) {
                int time = 0;
                String Value = inputCheckList.get(startTime+j).replaceAll("\\w++-->","");
                String outputValue;
            while (time <= count){
                if (Value.indexOf(",") != -1) {
                    outputValue = Value.substring(0,Value.indexOf(","));
                    Value = Value.substring(Value.indexOf(",") + 1, Value.length());
                    time++;
                    sdValue[j] = Double.valueOf(outputValue);
                }else{
                    break;
                }
            }
        }
        int ifint;
        if(Mathtime.standardDeviation(sdValue)%1 == 0){
            ifint = Mathtime.ifInt(Mathtime.standardDeviation(sdValue));
            output = output+ifint;
        }else{
            output = output +Mathtime.standardDeviation(sdValue);
        }
        
        //output to output.csv
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
                FileWriter bw = new FileWriter("output.csv",true);
                bw.write(stockName+","+startTime+","+endTime+"\n");
                bw.write(output+"\n");
                bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
    }if(args.length == 5 && args[0].equals("1") && args[1].equals("3")) {
        String output = "";
        int startTime = Integer.parseInt(args[3]);
        int endTime = Integer.parseInt(args[4]);
        int days = endTime - startTime + 1;
        String stockName = inputCheckList.get(0);
        //create item
        Map<Double,String > sdmap = new HashMap<>();
        //number of Stock
        int count = 0;

        while (1 <= stockName.length()){
            if (stockName.indexOf(",") != -1) {
                stockName = stockName.substring(stockName.indexOf(",") + 1, stockName.length());
                count++;
            }else{
                break;
            }
        }

        double[] finalOutput = new double[count+1]; 
        double[] Top3Value = new double[3];
        String[] Top3Name = new String[3]; 
        double[] sdValue = new double[days];
        String mapStockName = inputCheckList.get(0)+",";
        for(int numberOfStockTime = 0;numberOfStockTime<count+1;numberOfStockTime++){
        for(int i = 0;i<endTime-startTime+1;i++){            
            
            String Value = inputCheckList.get(startTime+i).replaceAll("\\w++-->","");
            for(int j = 0;j<numberOfStockTime;j++){
                Value = Value.substring(Value.indexOf(",")+1,Value.length());
            }
            sdValue[i] = Double.valueOf(Value.substring(0,Value.indexOf(",")+1).replace(",",""));
            }
            //store the top 3
            finalOutput[numberOfStockTime] = Mathtime.standardDeviation(sdValue);
            sdmap.put(finalOutput[numberOfStockTime],mapStockName.substring(0,mapStockName.indexOf(",")).replace(",",""));
            mapStockName = mapStockName.substring(mapStockName.indexOf(",") + 1, mapStockName.length());             
        }
        Arrays.sort(finalOutput);
        for(int i =0;i<3;i++) {
            Top3Value[0+i] = finalOutput[finalOutput.length-1-i];
            int ifint;
            if(Top3Value[0+i]%1==0){
                ifint = Mathtime.ifInt(Top3Value[0+i]);
                output = output+ifint+",";
            }else{
                output = output+Top3Value[i]+",";
            } 
            Top3Name[0+i] = sdmap.get(Top3Value[0+i]);    
            
        }
        Pattern pattern = Pattern.compile("(\\S+),");
        Matcher matcher = pattern.matcher(output);
        if(matcher.find()){
            output = matcher.group(1);
        }
        
        //output to output.csv
        try {
            if (!file.exists()) {
                file.createNewFile();}
                FileWriter bw = new FileWriter("output.csv",true);
                for(int i=0;i<3;i++){
                    bw.write(Top3Name[i]);
                    bw.write(",");
                }
                bw.write(startTime+","+endTime+"\n");
                bw.write(output+"\n");
                bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }

    }
    if(args.length == 5 && args[0].equals("1") && args[1].equals("4")){
        String stockName = args[2];
        String output = "";
        double[] outputArray = new double[2];
        int startTime = Integer.parseInt(args[3]);
        int endTime = Integer.parseInt(args[4]);
        int days = endTime - startTime + 1;
        double[] regressionLineValue = new double[days];
        //find position
        Pattern pattern = Pattern.compile("(\\S+,)"+stockName+",");
        Matcher matcher = pattern.matcher(inputCheckList.get(0));
        int count = 0;
        if(matcher.find()==true) {
            String father = matcher.group(1);
            while (1 <= father.length()){
                if (matcher.group(1).indexOf(",") != -1) {
                    father = father.substring(father.indexOf(",") + 1, father.length());
                    count++;
                }else{
                    break;
                }
            }
        }
            for(int j = 0;j<endTime-startTime+1;j++) {
                int time = 0;
                String Value = inputCheckList.get(startTime+j).replaceAll("\\w++-->","");
                String outputValue;
            while (time <= count){
                if (Value.indexOf(",") != -1) {
                    outputValue = Value.substring(0,Value.indexOf(","));
                    Value = Value.substring(Value.indexOf(",") + 1, Value.length());
                    time++;
                    regressionLineValue[j] = Double.valueOf(outputValue);
                }else{
                    break;
                }
            }
        }
        outputArray = Mathtime.regressionLine(regressionLineValue,startTime,endTime);
        for(int i =0;i<2;i++) {
            int ifint;
            if(outputArray[0+i]%1==0){
                ifint = Mathtime.ifInt(outputArray[0+i]);
                output = output+ifint+",";
            }else{
                output = output+outputArray[i]+",";
            }             
        }
        pattern = Pattern.compile("(\\S+),");
        matcher = pattern.matcher(output);
        if(matcher.find()){
            output = matcher.group(1);
        }
        try {
            if (!file.exists()) {
                file.createNewFile();}
                FileWriter bw = new FileWriter("output.csv",true);
                bw.write(stockName+","+startTime+","+endTime+"\n");
                bw.write(output+"\n");
                bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }


    }else{
        System.out.println("Wrong Value");
    }
 }
public static List<String> Sorting(List<String> inputList){
    List<String> Output = new ArrayList<>();
    int[] days = new int[inputList.size()];
    String[] content = new String[inputList.size()];
    Map<Integer, String> map = new HashMap<>();

    for(int i=0;i<inputList.size();i++){
        Pattern pattern = Pattern.compile("day(\\d+)");
        Matcher matcher = pattern.matcher(inputList.get(i));
        if(matcher.find() == true){
            days[i] = Integer.parseInt(matcher.group(1));
            content[i] = inputList.get(i);
            map.put(days[i],content[i]);
                }
    }
    Arrays.sort(days);
    for(int i=0;i<days.length;i++){
        Output.add(map.get(days[i]));
    }  
    Output.removeAll(Collections.singleton(null));
    return Output;     
 }
}
class mode1OutputMode{
        public static void outputToCsv(List<String> inputlList){
            List<String> Output = new ArrayList<>();
            String[] inputArray = new String[inputlList.size()];
            for(int i=0;i<inputlList.size();i++){
            Pattern pattern = Pattern.compile("day\\d+-->(.+),");
            Matcher matcher = pattern.matcher(inputlList.get(i));
            if(matcher.find()==true){
                inputArray[i] = matcher.group(1);
            }else{
                inputArray[i] = inputlList.get(i);
            }
            }
            for(int i =0;i<inputArray.length;i++){
                Output.add(inputArray[i]);
            }
            //write File
            try{
                File file = new File("output.csv");
                if (!file.exists()) {
                    file.createNewFile();
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                for(int i =0;i<Output.size();i++){
                    bw.write(Output.get(i)+"\n");
                }
                bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        }
}
class Mathtime{
    public static double SimpleMovingAverage(double[] smaValue) {
            double sum = 0;
            double output;
            for(int i=0;i<smaValue.length;i++){
                sum = sum + smaValue[i];
            }
            output = sum/5;
            DecimalFormat frmt = new DecimalFormat("#.##");
            output = Double.parseDouble(frmt.format(output));
        return output;
    }
    public static double standardDeviation(double[] sdValue) {
            double avg = 0;
            double ssd =0;
            double output;
            for(int i=0;i<sdValue.length;i++){
                avg = avg + sdValue[i];
            }
            avg = avg/sdValue.length;
            for(int i=0;i<sdValue.length;i++){
                ssd = ssd + pow(sdValue[i]-avg,2);
            }
                output = sqrt(ssd/(sdValue.length-1));
            DecimalFormat frmt = new DecimalFormat("#.##");
            output = Double.parseDouble(frmt.format(output));
            return output;
    }
    public static int ifInt(double input) {
        int output;
        output = (int)input;
        return output;
    }
    public static double[] regressionLine(double[] input,int startTime,int endTime) {
        double ValueAvg = 0;
        double TimeAvg = 0;
        double[] Time = new double[endTime-startTime+1];
        double strangeSum =0;
        double timeSsd = 0;
        double[] output = new double[2];
        DecimalFormat frmt = new DecimalFormat("#.##");
        for(int i=0;i<input.length;i++){
            ValueAvg = ValueAvg + input[i];
        }
        ValueAvg = ValueAvg/input.length;
        for(int i=0;i<input.length;i++){
            Time[i] = startTime+i;
            TimeAvg = TimeAvg + Time[i];
        }
        TimeAvg = TimeAvg/Time.length;
        for(int i=0;i<input.length;i++) {
            strangeSum = strangeSum + (Time[i]-TimeAvg)*(input[i]-ValueAvg);
            timeSsd = timeSsd + pow(Time[i]-TimeAvg,2);
        }
        output[0] = Double.parseDouble(frmt.format(strangeSum/timeSsd));
        output[1] = Double.parseDouble(frmt.format(ValueAvg - (strangeSum/timeSsd)*TimeAvg));
        return output;
        
    }
    public static double pow (double input, int power) {
        double output = 0;
        if(power == 0){
            output = 1;
        } if(power == 1){
            output = input;
        }else{
            for(int i = 0;i<power-1;i++) {
                output = input * input;
            }
        }
        return output;
    }
    public static double sqrt(double input) {
        double start = 0, end = input;
        double mid;
        double output = 0;
        while (start <= end) {
            mid = (start + end) / 2;
            if (mid * mid == input) {
                output = mid;
                break;
            }

            if (mid * mid < input) {
                start = mid + 1;
                output = mid;
            }
 
            else {
                end = mid - 1;
            }
        } 
        double test = 0.1;
        for (int i = 0; i < 3; i++) {
            while (output * output <= input) {
                output = test + output;
            }
            output = output - test;
            test = test / 10;
        }
        return output;
    }
}