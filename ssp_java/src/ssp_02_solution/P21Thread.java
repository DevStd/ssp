package ssp_02_solution;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Scanner;

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
