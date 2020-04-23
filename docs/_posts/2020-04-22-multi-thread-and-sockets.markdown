---
layout: post
title: 2. Multi-Thread and Sockets
tags: [ssp, java, thread, socket]
image: '/images/posts/4.jpg'
---

멀티스레드 및 소켓통신을 다룹니다.

외부 프로그램 실행 및 복수의 스레드를 사용하는 방법에 대해 소개합니다. 이어서 소켓통신을 위한 원격지와의 데이터 전송방법을 알아보고, 여러개의 클라이언트 요청을 동시에 처리하는 방법에 대해 다룹니다.

## 1. 외부 프로그램 실행

콘솔에서 줄 단위로 문자를 입력받아, 외부 프로그램을 호출하여 입력값에 대한 확인코드를 얻어 화면에 출력합니다.

* 프로그램을 실행하자마자 콘솔로부터 100자 이하의 문자를 줄단위로 입력받는다.
* Q 입력시 프로그램을 종료한다.
* Q 이외의 문자 입력시 'data/ssp_02/bin/exprgm.exe'를 실행한다.
    * 실행시 입력받은 문자열을 파라메터로 전달한다.
    * 외부프로그램은 실행할 경우 입력받은 값의 확인코드를 표준콘솔에 출력한다. 
    * 확인코드는 0~255 사이의 숫자이다.
    * MacOS의 경우 'exprgm.mac'을, Linux(Ubuntu 등)인 경우 'exprgm.linux'를 호출한다.
* 입력값과 외부 프로그램 호출로 구한 확인코드를 #으로 구분하여 화면에 출력한다


#### 입력 예시
```
test
essential
success
atta
```
#### 출력예시
```
test#22
essential#126
success#99
atta#0
```
#### 코드
```java
public class P1ExPrgm {

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        String line;
        
        while((line=sc.nextLine()) != null) {
            if("Q".equals(line)) {
                break;
            } else {
                String execResult = runExprgm(line);
                System.out.print(line+"#"+execResult);
            }
        }
        
        sc.close();
    }

    public static String runExprgm(String param) {
        // 실행할 명령
        List<String> command = new ArrayList<>();
        command.add("data/ssp_02/bin/exprgm.exe");
        command.add(param);
        
        // 프로세스 객체 생성
        ProcessBuilder pBuilder = new ProcessBuilder();
        pBuilder.redirectErrorStream(true);
        pBuilder.command(command);
        
        // 프로세스 실행 결과값
        String result = "";
        
        try {
            // 프로세스 실행
            Process proc = pBuilder.start();
            
            // 프로세스의 출력을 읽어서 값이 Null이 아니면 결과값에 추가
            try(BufferedReader buffReader = new BufferedReader(new InputStreamReader(proc.getInputStream()))){
                String tmpLine = null;              
                while((tmpLine = buffReader.readLine())!=null){
                    result += tmpLine + "\n";
                }
            }
            
            // 프로세스가 끝날때 까지 대기
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
        } 

        return result;
    }
}

```

<br/>

## 2. 스레드

### 2.1 단일 스레드 애플리케이션

콘솔에서 입력받은 문구를 검색하는 애플리케이션을 작성한다.

* 프로그램을 시작하자마자 100자 이하의 문자를 줄단위로 입력받는다.
* Q 입력시 프로그램을 종료한다.
* 2글자 이상 입력시 'data/ssp_02/src' 디렉토리의 txt 파일에서 입력받은 문자열을 검색한다
* 검색 결과는 파일이름:줄번호 의 형식으로 화면에 출력한다.
* 줄번호는 1 부터 시작해야 한다.
* 검색결과는 줄단위로 처리하므로, 같은 파일에서 한줄에 2번이상 발견시에도 한번만 출력한다.

#### 입력 예시
```
enough
```
#### 출력예시
```
egg-bred.txt:16
richbred.txt:16
shuimai.txt:94
x-drinks.txt:1520
```
#### 코드
```java
public class P21Thread {

    private static final String DOC_ROOT = "data/ssp_02/src/";
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line;
        
        while((line=sc.nextLine()) != null) {
            if("Q".equals(line)) {
                break;
            } else if(line.length() >= 2){
                // txt 파일 목록 생성
                String[] txtFiles = getTxtFiles();
                if(txtFiles == null) continue; 
                
                // 정보 검색 및 출력
                for (String f : txtFiles) {
                    searchText(f, line);
                }
            }
        }
        
        sc.close();
    }

    // txt 파일 목록 생성
    private static String[] getTxtFiles() {
        File dir = new File(DOC_ROOT);
        
        String[] list = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        
        return list;
    }

    // 정보 검색 및 출력
    private static void searchText(String file, String text) {
        try(BufferedReader br = new BufferedReader(new FileReader(DOC_ROOT+file))){
            int lineNum = 1;
            String line;
            while((line = br.readLine()) != null){
                if(line.contains(text)) System.out.println(file+"#"+lineNum);
                lineNum++;
            }
        } catch (Exception e) {}
    }
}

```

<br/>

### 2.2 멀티 스레드 애플리케이션

콘솔에서 입력받은 문구를 검색하는 애플리케이션을 작성한다.

* 2.1에서 구현한 애플리케이션의 '파일에서 문자열을 찾는 부분'을 멀티스레드로 작성하여 여러개의 파일을 동시에 탐색할 수 있도록 개선한다.

#### 입력 예시
```
enough
```
#### 출력예시
```
egg-bred.txt:16
richbred.txt:16
shuimai.txt:94
x-drinks.txt:1520
```
#### 코드
```java
public class P22MultiThread {

    private static final String DOC_ROOT = "data/ssp_02/src/";
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line;
        
        while((line=sc.nextLine()) != null) {
            if("Q".equals(line)) {
                break;
            } else if(line.length() >= 2){
                // txt 파일 목록 생성
                String[] txtFiles = getTxtFiles();
                if(txtFiles == null) continue; 
                
                // 정보 검색 및 출력
                ArrayList<Thread> thList = new ArrayList<>();
                for (final String f : txtFiles) {
                    final String text = line;                   
                    Thread th = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            searchText(f, text);
                        }
                    });
                    th.start();
                    thList.add(th);
                }
                
                // 모든 스레드가 종료되기를 기다림
                for(Thread th : thList) {
                    try {
                        th.join();
                    } catch (InterruptedException e) {}
                }
                thList.clear();
            }
        }
        
        sc.close();
    }

    // txt 파일 목록 생성
    private static String[] getTxtFiles() {
        File dir = new File(DOC_ROOT);
        
        String[] list = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        
        return list;
    }

    // 정보 검색 및 출력
    private static void searchText(String file, String text) {
        try(BufferedReader br = new BufferedReader(new FileReader(DOC_ROOT+file))){
            int lineNum = 1;
            String line;
            while((line = br.readLine()) != null){
                if(line.contains(text)) System.out.println(file+"#"+lineNum);
                lineNum++;
            }
        } catch (Exception e) {}
    }

}
```

<br/>


### 2.3 스레드 수 제한

콘솔에서 입력받은 문구를 검색하는 애플리케이션을 작성한다.

* 2.2에서 구현한 애플리케이션이 스레드를 최대 4개까지만 생성하도록 제한한다.

#### 입력 예시
```
enough
```
#### 출력예시
```
egg-bred.txt:16
richbred.txt:16
shuimai.txt:94
x-drinks.txt:1520
```
#### 코드
```java
public class P23ThreadPool {

    private static final String DOC_ROOT = "data/ssp_02/src/";
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line;
        
        while((line=sc.nextLine()) != null) {
            if("Q".equals(line)) {
                break;
            } else if(line.length() >= 2){
                // txt 파일 목록 생성
                String[] txtFiles = getTxtFiles();
                if(txtFiles == null) continue; 
                
                // 정보 검색 및 출력
                ExecutorService executor = Executors.newFixedThreadPool(4);
                for (final String f : txtFiles) {
                    final String text = line;
                    executor.submit(new Runnable() {
                        @Override
                        public void run() {
                            searchText(f, text);
                        }
                    });
                }
                
                // 모든 스레드가 종료되기를 기다림
                executor.shutdown();
            }
        }
        
        sc.close();
    }

    // txt 파일 목록 생성
    private static String[] getTxtFiles() {
        File dir = new File(DOC_ROOT);
        
        String[] list = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".txt");
            }
        });
        
        return list;
    }

    // 정보 검색 및 출력
    private static void searchText(String file, String text) {
        try(BufferedReader br = new BufferedReader(new FileReader(DOC_ROOT+file))){
            int lineNum = 1;
            String line;
            while((line = br.readLine()) != null){
                if(line.contains(text)) System.out.println(file+"#"+lineNum);
                lineNum++;
            }
        } catch (Exception e) {}
    }

}
```

<br/>

## 3. 소켓 통신

### 3.1 소켓 클라이언트

파일 전송을 위한 TCP 소켓 클라이언트 애플리케이션을 작성한다.

* 5000번 포트로 TCP소켓을 연결하는 클라이언트 프로그램을 작성한다.
* 서비스 동작 방법
    1. 전송할 파일 이름을 콘솔로부터 입력받는다.
    2. 해당 파일이 'data/ssp_02/src' 디렉토리 안에 있는 경우 소켓 서버에 연결을 시작한다.
    3. 소켓 클라이언트는 TCP를 이용하여 5000번 포트로 서비스 중인 서버에 접속한다.
    4. 서버에 연결되면 'SEND#파일이름#파일크기'를 전송한다.
    5. 서버에서 'READY#파일이름'을 수신하면 파일 전송을 시작한다.
    6. 파일 전송이 끝나면 'END#파일이름'을 화면에 출력하고 소켓 연결을 종료한다.

#### 입력 예시
```
chicken.jpg
```
#### 출력예시
```
END#chicken.jpg
```
#### 코드
```java
public class P31SocketClient {
    
    private static final String DOC_ROOT = "data/ssp_02/src/";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line;
        
        while((line=sc.nextLine()) != null) {
            if("Q".equals(line)) {
                break;
            } else {
                // 파일 존재여부 체크
                File file = new File(DOC_ROOT+line);
                if(!file.exists() || file.isDirectory()) continue;
                
                // 파일전송
                sendFile(file);
            }
        }
        
        sc.close();
    }

    private static void sendFile(File file) {
        try (Socket sock = new Socket("127.0.0.1", 5000);
            BufferedInputStream bi = new BufferedInputStream(sock.getInputStream());
            BufferedOutputStream bo = new BufferedOutputStream(sock.getOutputStream())) {
            if (!sock.isConnected()) return;
            
            // 파일정보 전송
            String header = "SEND#"+file.getName()+"#"+file.length();
            bo.write(header.getBytes());
            bo.flush();
            
            
            // 전송준비완료 응답수신
            byte[] buffer = new byte[1000];
            int readBytes = 0;
            String aspectRes = "READY#"+file.getName();
            String res = "";
            while((readBytes = bi.read(buffer)) > 0) {
                res += new String(buffer, 0, readBytes);
                if(aspectRes.equals(res)) break;
            }
            
            // 파일전송
            buffer = new byte[1000];
            readBytes = 0;
            try(FileInputStream fi = new FileInputStream(file)){                
                while((readBytes = fi.read(buffer)) > 0) {
                    bo.write(buffer, 0, readBytes);
                    bo.flush();
                }
            }
            
            //전송종료
            System.out.println("END#"+file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```

<br/>

### 3.2 소켓 서버

파일 전송을 위한 TCP 소켓 서버 애플리케이션을 작성한다.

* 5000번 포트로 TCP소켓을 수신하는 서버 프로그램을 작성한다.
* 서비스 동작 방법
    1. 5000번포트로 클라이언트 연결을 대기한다.
    2. 클라이언트 연결 후 클라이언트로부터 'SEND#파일이름#파일크기'를 수신한다.
    3. 파일을 수신한 준비가 완료되면 클라이언트에 'READY#파일이름'을 전송한다.
    4. '/data/ssp_02/target'경로에 전달받은 파일이름으로 클라이언트로부터 수신한 데이터를 저장한다.
    5. 파일 저장이 끝나면 다음 클라이언트 연결을 처리할 수 있도록 대기한다.

#### 코드
```java
public class P32SockerServer {

    private static final String DOC_ROOT = "data/ssp_02/target/";
    
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
            
            while (true) {
                Socket client = serverSocket.accept();
                processSocket(client);
            }
        } catch (Exception e) {
        }       
    }

    private static void processSocket(Socket client) {
        String[] fileInfo = null;
        try(BufferedInputStream bi = new BufferedInputStream(client.getInputStream());
            BufferedOutputStream bo = new BufferedOutputStream(client.getOutputStream())){
            
            // 파일정보 수신
            byte[] buffer = new byte[1000]; 
            int readBytes = bi.read(buffer);
            fileInfo = new String(buffer, 0, readBytes).split("#");
            
            // 파일수신대기 응답
            String ack = "READY#"+fileInfo[1];
            bo.write(ack.getBytes());
            bo.flush();
            
            // 파일데이터 수신
            readBytes = 0;
            buffer = new byte[1000];
            long fileSize = Long.valueOf(fileInfo[2]);
            try(FileOutputStream fo = new FileOutputStream(DOC_ROOT+fileInfo[1])){
                long totalBytes = 0;
                while((readBytes = bi.read(buffer)) > 0) {
                    fo.write(buffer, 0, readBytes);
                    totalBytes += readBytes;
                    if(totalBytes >= fileSize) break;
                }
            } catch (IOException e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

<br/>

### 3.3 다중접속 소켓 서버

파일 전송을 위한 TCP 소켓 서버 애플리케이션을 작성한다.

* 앞서 3.2에서 작성한 소켓 서버에 동시에 여러개의 클라이언트의 접속을 처리 할 수 있도록 개선한다.

#### 코드
```java
public class P33SockerServerMultiConn {

    private static final String DOC_ROOT = "data/ssp_02/target/";
    
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(5000);
            
            while (true) {
                Socket client = serverSocket.accept();
                new Thread(new Runnable() {                 
                    @Override
                    public void run() {
                        processSocket(client);
                    }
                }).start();
            }
        } catch (Exception e) {
        }       
    }

    private static void processSocket(Socket client) {
        String[] fileInfo = null;
        try(BufferedInputStream bi = new BufferedInputStream(client.getInputStream());
            BufferedOutputStream bo = new BufferedOutputStream(client.getOutputStream())){
            
            // 파일정보 수신
            byte[] buffer = new byte[1000]; 
            int readBytes = bi.read(buffer);
            fileInfo = new String(buffer, 0, readBytes).split("#");
            
            // 파일수신대기 응답
            String ack = "READY#"+fileInfo[1];
            bo.write(ack.getBytes());
            bo.flush();
            
            // 파일데이터 수신
            readBytes = 0;
            buffer = new byte[1000];
            long fileSize = Long.valueOf(fileInfo[2]);
            try(FileOutputStream fo = new FileOutputStream(DOC_ROOT+fileInfo[1])){
                long totalBytes = 0;
                while((readBytes = bi.read(buffer)) > 0) {
                    fo.write(buffer, 0, readBytes);
                    totalBytes += readBytes;
                    if(totalBytes >= fileSize) break;
                }
            } catch (IOException e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
```
