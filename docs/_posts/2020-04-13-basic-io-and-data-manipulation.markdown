---
layout: post
title: 1. Basic I/O and Data Manipulations
tags: [ssp, java, io, data manipulation]
image: '/images/posts/start.png'
---

데이터 처리를 위한 콘솔, 파일 입출력을 다룹니다. 

콘솔 또는 파일에서 텍스트 입력을 받아서 데이터를 정련하고 가공해서 출력하는 방법을 정리합니다. 또한 데이터의 정렬 및 병합을 알아보고, 파일시스템을 조작하는 방법에 대해서도 간단히 알아봅니다.


## 1. 콘솔입력

콘솔에서 줄 단위로 숫자를 입력 받아 합과 평균을 구하고자 한다.

* 프로그램을 실행하자마자 콘솔로부터 숫자 0~100 사이의 숫자를 입력받는다.
* 입력값은 0~100 사이의 숫자와 문자 Q 이며, 입력받는 숫자의 갯수 제한은 없다.
* Q입력시 앞서 입력한 숫자의 합과 평균을 출력하고 종료한다.

#### 입력 예시
```
20
45
100
85
1
Q
```
#### 출력예시
```
251
50.2
```
#### 코드
```java
public class P1ConsoleIO {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        String line;
        int cnt = 0, sum = 0;
        
        while((line=sc.nextLine()) != null) {
            if("Q".equals(line)) {
                break;
            } else {
                sum += Integer.valueOf(line);
                cnt++;
            }
        }
        
        System.out.println(sum);
        System.out.println(Double.valueOf(sum)/cnt);
    }

}
```

<br/>
## 2. 데이터 처리

### 2.1 자료입력(1)
콘솔에서 줄 단위로 점수를 입력 받아 평균을 구하고자 한다.

* 입력 데이터는 ```#```으로 구분되며 ```M```으로 시작한다.
* 입력 데이터의 수는 1개이상 100000개 미만이다.
* 입력데이터 양식 : ```M#년도(4)#회차(1)#수험번호(5)#실기점수(2)#필기점수(2)```    
    * 년도와 회차는 각각 4자리와 1자리 숫자이며, 수험번호는 5자리 숫자이다.
    * 실기점수는 0~80, 필기점수는 0~20 의 두자리 숫자이다.
    * 종합점수는 실기점수와 필기점수를 더해서 계산한다
* ```Q``` 입력시 앞서 입력한 데이터의 종합점수의 평균을 출력하고 종료한다.

#### 입력 예시
```
M#2020#1#40001#60#5
M#2020#1#40002#50#0
M#2020#1#40003#40#20
M#2020#1#40004#65#10
M#2020#1#40005#70#10
Q
```
#### 출력예시
```
66
```
#### 코드
```java
public class P2DataManipulation1 {

    public static void main(String[] args) {
        ArrayList<Score> scores = new ArrayList<>();
        
        Scanner sc = new Scanner(System.in);
        String line;
        
        while((line=sc.nextLine()) != null) {
            if("Q".equals(line)) {
                break;
            } else if(line.startsWith("M")) {
                scores.add(Score.parseM(line));
            }
        }
        
        double sum = 0;
        for(int i = 0 ; i < scores.size() ; i++) sum += scores.get(i).total;
        System.out.println(sum/scores.size());
    }
}

public class Score{
    String test;    //구분
    String year;        //년도
    String seq;     //회차
    int testNum;    //수험번호
    int score1;     //필기점수
    int score2;     //실기점수
    int total;      //종합점수
    public Score(String test, String year, String seq, int testNum, int score1, int score2) {
        super();
        this.test = test;
        this.year = year;
        this.seq = seq;
        this.testNum = testNum;
        this.score1 = score1;
        this.score2 = score2;
        this.total = score1+score2;
    }
    
    public static Score parseM(String line) {
        String[] item = line.split("#");
        Score s = new Score(
                item[0], 
                item[1], 
                item[2], 
                Integer.parseInt(item[3]), 
                Integer.parseInt(item[4]), 
                Integer.parseInt(item[5]));
        return s;
    }
    
    public static Score parseL(String line) {
        //L 2020 1 40001 80 20
        //0 1234 5 67890 11 13
        Score s = new Score(
                line.substring(0,1),
                line.substring(1,5),
                line.substring(5,6),
                Integer.parseInt(line.substring(6,11)),
                Integer.parseInt(line.substring(11,13)),
                Integer.parseInt(line.substring(13,15)));
        return s;
    }
}
```

<br/>
### 2.2 자료입력(2)
콘솔에서 줄 단위로 점수를 입력 받아 평균을 구하고자 한다.

* 입력데이터 기술(M)과 언어(L점수 이며 각각 ```M```과 ```L```로 시작한다.
* 입력 데이터의 수는 1개이상 100000개 미만이다.
* 기술 입력데이터 양식 : ```M#년도(4)#회차(1)#수험번호(5)#실기점수(2)#필기점수(2)```    
    * 년도화 회차는 각각 4자리와 1자리 숫자이며, 수험번호는 5자리 숫자이다.
    * 실기점수는 0~80, 필기점수는 0~20 의 두자리 숫자이다.
    * 종합점수는 실기점수와 필기점수를 더해서 계산한다
* 언어 입력데이터 양식 : ```L년도(4)회차(1)수험번호(5)실기점수(2)필기점수(2)```    
    * 언어 데이터의 구조는 기술 입력데이터와 동일하나, 구분자 없다.
    * 종합점수는 실기점수와 필기점수를 더해서 계산한다
* ```Q``` 입력시 앞서 입력한 데이터의 종합점수의 평균을 출력하고 종료한다.
* 출력 순서는 기술점수 평균을먼저 출력하고 다음줄에 언어점수 평균을 출력한다

#### 입력 예시
```
M#2020#1#40001#60#5
L20201400018020
M#2020#1#40002#50#0
M#2020#1#40003#40#20
L20201400026010
L20201400037010
M#2020#1#40004#65#10
L20201400045005
L20201400053510
M#2020#1#40005#70#10
Q
```
#### 출력예시
```
66
70
```
#### 코드
```java
public class P2DataManipulation2 {

    public static void main(String[] args) {
        ArrayList<Score> scoresM = new ArrayList<>();
        ArrayList<Score> scoresL = new ArrayList<>();
        
        Scanner sc = new Scanner(System.in);
        String line;
        
        while((line=sc.nextLine()) != null) {
            if("Q".equals(line)) {
                break;
            } else if(line.startsWith("M")) {
                scoresM.add(Score.parseM(line));
            } else if(line.startsWith("L")) {
                scoresL.add(Score.parseL(line));
            }
        }
        
        double sumM = 0, sumL = 0;
        for(Score s : scoresM) sumM += s.total;
        for(Score s : scoresL) sumL += s.total;
        
        System.out.println(sumM/scoresM.size());
        System.out.println(sumL/scoresL.size());
    }

}
```

<br/>
## 3. 파일 입출력
파일에서 점수를 입력 받아 평균을 구하고자 한다.

* 입력파일은 ```data/ssp_01/score.txt```이다.
* 입력데이터의 형식은 **2.2 자료입력(2)**와 같은 형식이다.
* 입력파일을 끝까지 읽은 후 기술점수와 실기점수의 평균을 구해서 ```out/score_average.txt```에 저장한다.

#### 입력 예시(data/ssp_01/score.txt)
```
M#2020#1#40001#60#5
L20201400018020
M#2020#1#40002#50#0
M#2020#1#40003#40#20
L20201400026010
L20201400037010
M#2020#1#40004#65#10
L20201400045005
L20201400053510
M#2020#1#40005#70#10
```
#### 출력예시(out/score_average.txt)
```
66
70
```
#### 코드
```java
public class P3FileIO {

    public static void main(String[] args) {

        String filename = "data/ssp_01/score.txt";
        
        ArrayList<Score> scoresM = new ArrayList<>();
        ArrayList<Score> scoresL = new ArrayList<>();
        
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                if(line.startsWith("M")) {
                    scoresM.add(Score.parseM(line));
                } else if(line.startsWith("L")) {
                    scoresL.add(Score.parseL(line));
                }
            }
        } catch (Exception e) {}
        
        double sumM = 0, sumL = 0;
        for(Score s : scoresM) sumM += s.total;
        for(Score s : scoresL) sumL += s.total;
        
        System.out.println(sumM/scoresM.size());
        System.out.println(sumL/scoresL.size());
    }

}
```


<br/>
## 4. 데이터 정렬 및 병합
### 4.1 데이터 정렬
파일에서 점수를 입력 받아 종합점수가 높은 순서대로 정렬하여 상위 3명의 점수를 구하고자 한다.

* 입력파일은 ```data/ssp_01/score2.txt```이다.
* 입력데이터 양식 : ```M#년도(4)#회차(1)#수험번호(5)#실기점수(2)#필기점수(2)```    
    * 년도와 회차는 각각 4자리와 1자리 숫자이며, 수험번호는 5자리 숫자이다.
    * 실기점수는 0~80, 필기점수는 0~20 의 두자리 숫자이다.
    * 종합점수는 실기점수와 필기점수를 더해서 계산한다
* 순위는 다음 순서대로 계산한다
    1. 종합점수가 높은 순서
    2. 종합점수가 같은 경우 실기점수가 높은 순서
    3. 실기/필기점수가 같은 경우 수험번호가 낮은 순서
* 출력은 순위가 높은 순서대로 한줄씩 3명의 정보를 출력한다.
* 출력형식은 수험번호와 종합점수를 ```,```로 구분하여 출력한다.

#### 입력 예시(data/ssp_01/score2.txt)
```
M#2020#1#40001#60#5
M#2020#1#40002#50#0
M#2020#1#40003#80#15
M#2020#1#40004#65#10
M#2020#1#40005#70#10
M#2020#1#40006#70#20
M#2020#1#40007#80#10
M#2020#1#40008#70#20
M#2020#1#40009#50#10
```
#### 출력 예시
```
40003,95
40007,90
40006,90
```
#### 코드
```java
public class P4SortAggregation1 {

    public static void main(String[] args) {
        String filename = "data/ssp_01/score2.txt";
        
        ArrayList<Score> scoresM = new ArrayList<>();
        
        // File input
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                if(line.startsWith("M")) {
                    scoresM.add(Score.parseM(line));
                } 
            }
        } catch (Exception e) {}
        
        // Sort
        Collections.sort(scoresM, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                if(o1.total != o2.total) return o2.total - o1.total;
                else if(o1.score1 != o2.score1) return o2.score1 - o1.score1;
                else return o1.testNum - o2.testNum;
            }
        });
        
        // Print result
        for(int i = 0 ; i < 3 ; i++) {
            Score s = scoresM.get(i);
            System.out.println(s.testNum+","+s.total);
        }
    }

}
```

<br/>
### 4.2 데이터 병합 및 검색
조직정보와 사원정보 파일일 읽어서 사원정보를 조회하는 프로그램을 작성한다.

* 콘솔에서 5자리 숫자 사번을 입력받아 사원정보를 출력한다.
* Q 입력시 프로그램을 종료한다
* 조직 정보 파일 : ```data/ssp_01/dept.txt```
    * 한 줄에 하나의 조직정보를 표기하며 ```#```으로 구분한다.
    * 조직정보는 ```조직코드#조직이름#상위조직코드```의 양식으로 기록되어 있다.
    * 상위조직코드가 0 인 경우 최상위 조직을 의미한다.
* 사원 정보 파일 : ```data/ssp_01/emp.txt```
    * 한 줄에 하나의 사원정보를 표기하며 ```#```으로 구분한다.
    * 사원정보는 ```사번#이름#직위#조직코드```의 양식으로 기록되어 있다.
* 출력은 ```조직명 이름 직위```순으로 출력한다
* 출력하는 조직명은 최상위 조직부터 출력해야 한다.

#### 입력 파일(data/ssp_01/dept.txt)
```
1#화성정밀#0
10#사장실#1
20#국내사업담당#1
30#해외사업담당#1
40#신소재연구소#1
501#중부영업팀#20
502#남부영업팀#20
```
#### 입력 파일(data/ssp_01/emp.txt)
```
00001#박정석#사장#10
40002#서지훈#책임연구원#40
40009#강민#사원#501
```
#### 입력 예시
```
00001
40009
Q
```
#### 출력 예시
```
화성정밀 사장실 박정석 사장
화설정밀 국내사업담당 중부영업팀 강민 사원
```
#### 코드
```java
public class P4SortAggregation2 {

    public static void main(String[] args) {
        
        String deptFile = "data/ssp_01/dept.txt";
        String empFile = "data/ssp_01/emp.txt";
        
        HashMap<String, Dept> dept = EmpUtil.readDeptFile(deptFile);
        HashMap<String, Emp> emp = EmpUtil.readEmpFile(empFile);
        
        Scanner sc = new Scanner(System.in);
        String line;
        while((line=sc.nextLine())!= null) {
            if("Q".equals(line)) {
                break;
            } else {
                Emp e = emp.get(line);
                if(e == null) continue;
                
                String deptNm = EmpUtil.getEntireDept(dept, e.deptNo);
                
                System.out.println(deptNm + " " + e.name + " " + e.position);
            }
        }
    }
}

public class Dept {
    String no;
    String name;
    String upperNo;
    public Dept(String no, String name, String upperNo) {
        super();
        this.no = no;
        this.name = name;
        this.upperNo = upperNo;
    }
}

public class Emp {
    String no;
    String name;
    String position;
    String deptNo;
    public Emp(String no, String name, String position, String deptNo) {
        super();
        this.no = no;
        this.name = name;
        this.position = position;
        this.deptNo = deptNo;
    }
}

public class EmpUtil{
    public static HashMap<String, Dept> readDeptFile(String filename){
        HashMap<String, Dept> dept = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                String[] d = line.split("#");
                dept.put(d[0], new Dept(d[0], d[1], d[2]));
            }
        } catch (Exception e) {}
        return dept;
    }
    
    public static HashMap<String, Emp> readEmpFile(String filename){
        HashMap<String, Emp> emp = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;
            while((line = br.readLine()) != null){
                String[] e = line.split("#");
                emp.put(e[0], new Emp(e[0], e[1], e[2], e[3]));
            }
        } catch (Exception e) {}
        return emp;
    }
    
    public static String getEntireDept(HashMap<String, Dept> dept, String deptNo) {
        Dept d = dept.get(deptNo);
        if("0".equals(d.upperNo)) return d.name;
        else return getEntireDept(dept, d.upperNo) + " " + d.name;
    }
}
```


<br/>
## 5. 파일 시스템
보안담당자 김푸른씨는 보안양식에 맞게 파일 이름이 작성되었는지 검사하는 프로그램을 만들고 있다.

```data/ssp_01/q05```디렉토리 안의 모든 하위 디랙토리를 검사해서 다음 규칙에 맞지 않는 파일이름을 출력하자.

* 보안점검 대상 파일은 확장자가 xlsx, pptx, pdf 인 파일이다.
* 모든 보안점검 대상 파일이름은 보안규정에 따라 ```S_```또는 ```C_```로 시작해야 한다.
* 단, 각 폴더에 존재하는 ```except_policy.txt```에 기록된 파일은 보안점검대상에서 제외한다.
* 해당 폴더 안에 예외파일이 없는 경우 ```except_policy.txt```가 없을 수도 있다.
* 출력은 화면에 한줄에 파일 1개씩 출력하며, 파일이름순으로 정렬해서 출력한다.

#### 입력폴더 예시
```
  q05
    │  except_policy.txt
    │  E_Requrements.xlsx
    │  HIS_Board_specfications.pdf
    │  Memo.txt
    │  S_System_Management.pptx
    │
    ├─data
    │      CoPromotion_20190102.xlsx
    │      C_Experiments_20200405.pptx
    │      except_policy.txt
    │      Power_Consumptions.xlsx
    │
    └─specs
            UPS_specfications.pdf
```
#### 출력 예시
```
CoPromotion_20190102.xlsx
E_Requrements.xlsx
UPS_specfications.pdf
```
#### 코드
```java
public class P5FileSystem {
    public static void main(String[] args) {
        String checkRoot = "data/ssp_01/q05";

        ArrayList<String> result = checkSecurity(checkRoot);
        Collections.sort(result);
        
        for(int i = 0 ; i < result.size() ; i++) {
            System.out.println(result.get(i));
        }
    }
    
    // 파일 보안 점검
    private static ArrayList<String> checkSecurity(String root){
        ArrayList<String> result = new ArrayList<>();
        
        String[] list = new File(root).list();
        for(String fn : list) {
            HashSet<String> exList = exceptList(root);          
            String fullPath = root + File.separatorChar + fn;
            
            if(new File(fullPath).isDirectory()) {
                // 하위폴더 점검
                ArrayList<String> subDirResult = checkSecurity(fullPath);
                result.addAll(subDirResult);
            } else {
                // 파일 점검
                if((fn.endsWith(".xlsx") || fn.endsWith(".pptx") || fn.endsWith(".pdf"))
                    && !(fn.startsWith("S_") || fn.startsWith("C_"))
                    && !exList.contains(fn)) {
                    // 위반 파일
                    result.add(fn);
                }               
            }
        }
        
        return result;
    }
    
    // 예외파일 목록 읽기
    private static HashSet<String> exceptList(String dirName){
        HashSet<String> exSet = new HashSet<>();
        
        File exFile = new File(dirName + File.separatorChar + "except_policy.txt");
        if(exFile.exists()) {
            try(BufferedReader br = new BufferedReader(new FileReader(exFile))){
                String line;
                while((line=br.readLine())!= null) {
                    exSet.add(line);
                }
            } catch(Exception e) {}
        }
        
        return exSet;
    }
}
```