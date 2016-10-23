import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class TurnJSPtoServlet {

	public String [] getJars(String temp) {//得到jsp头中的import包
		if(temp.indexOf("import")>0){
		 String []a=temp.split("import=\"");
		 String []s=a[1].split("\"");
		 String []jars=s[0].split(",");
		return jars;
	}else{
		return null;
	}
}
	public void turnjtoj(String jspname){
		
		
		//创建****_jsp.java
		String []t=jspname.split("\\.");
		File file_java=new File("bin/"+t[0]+"_jsp.java");
		String filename=jspname;
		String nameoftemp="";
		FileInputStream fis;
		String s1="import java.io.*;\n"+"import javax.servlet.*;\n";
		String classstring="public class "+t[0]+"_jsp"+" implements Servlet {  \n";
		String s2= "public void destroy(){}\n"+
                  "public ServletConfig getServletConfig() {return null;}\n"+  
                  "public String getServletInfo(){return null;}\n"+
                  "public void init(ServletConfig arg0) throws ServletException {}\n"+  
                  "public void service(ServletRequest arg0, ServletResponse arg1)throws ServletException, IOException {\n"+
                  "PrintWriter out = arg1.getWriter();\n";
		try {
			file_java.createNewFile();
			//写入开头到_jsp.java文件
			FileOutputStream out=new FileOutputStream(file_java);
			fis = new FileInputStream(filename);
			InputStreamReader Inputreader = new InputStreamReader(fis,"GBK");
		    BufferedReader br = new BufferedReader(Inputreader);
		    StringBuffer data=new StringBuffer();
		    String temp=br.readLine();
		    String []a=getJars(temp);
		    if(a!=null){
		    for (int i = 0; i < a.length; i++) {
		    	String tString="import "+a[i]+";\n";
				out.write(tString.getBytes());
			}}
		    out.write(s1.getBytes());
		    out.write(classstring.getBytes());
			out.write(s2.getBytes());
			temp=br.readLine();
		while( temp!=null){   
		      if(temp.indexOf("<%")>=0&&temp.indexOf("%>")==-1&&temp.indexOf("<%!")==-1&&temp.indexOf("<%=")==-1&&temp.indexOf("<%--")==-1){//判断<%
		    	  //||temp.indexOf("<%!")>0||temp.indexOf("<%=")>0||temp.indexOf("<%--")>0){
				String sd[]=temp.split("<%");
				if(sd.length==2){
					nameoftemp=sd[1]+"\n";
					out.write(nameoftemp.getBytes());
				}
				temp=br.readLine();
				while(temp.indexOf("%>")==-1){
					nameoftemp=temp+"\n";
					out.write(nameoftemp.getBytes());
					temp=br.readLine();
				}
				sd=temp.split("%>");
				if (sd.length>0) {
					nameoftemp=sd[0]+"\n";
					out.write(nameoftemp.getBytes());
				}
		      }else if(temp.indexOf("<%!")>=0&&temp.indexOf("%>")==-1){//判断<%!
		    	  //||temp.indexOf("<%!")>0||temp.indexOf("<%=")>0||temp.indexOf("<%--")>0){
				String sd[]=temp.split("<%!");
				if(sd.length==2){
					nameoftemp=sd[1]+"\n";
					out.write(nameoftemp.getBytes());
				}
				temp=br.readLine();
				while(temp.indexOf("%>")==-1){
					nameoftemp=temp+"\n";
					out.write(nameoftemp.getBytes());
					temp=br.readLine();
				}
				sd=temp.split("%>");
				if (sd.length>0) {
					nameoftemp=sd[0]+"\n";
					out.write(nameoftemp.getBytes());
				}
		      }else if(temp.indexOf("<%--")>=0&&temp.indexOf("%>")==-1){//注释不写入
				temp=br.readLine();
				while(temp.indexOf("%>")==-1){
					temp=br.readLine();
				}
		      }else if(temp.indexOf("<%=")>=0&&temp.indexOf("%>")==-1){//注释不写入
					nameoftemp="out.println("+temp.replaceAll("<%=","")+");\n";
					if(temp!=null)
					out.write(nameoftemp.getBytes());
					temp=br.readLine();
					while(temp.indexOf("%>")==-1){
						nameoftemp="out.println("+temp+");\n";
						out.write(nameoftemp.getBytes());
						temp=br.readLine();
					}
					nameoftemp="out.println("+temp.replaceAll("%>","")+");\n";
					if(temp!=null)
					out.write(nameoftemp.getBytes());
			      } else if(temp.indexOf("<%=")>=0&&temp.indexOf("%>")>=0){//
			    	  
			    	  nameoftemp="out.println("+temp.replaceAll("<%=","").replaceAll("%>","")+");\n";
					  out.write(nameoftemp.getBytes());
			      }else if(temp.indexOf("<%--")>=0&&temp.indexOf("--%>")>=0){//
			    	  //
			      }else if(temp.indexOf("<%!")>=0&&temp.indexOf("%>")>=0){//
			    	  nameoftemp=temp.replaceAll("<%!","").replaceAll("%>","")+"\n";
					  out.write(nameoftemp.getBytes());
			      }else if(temp.indexOf("<%")==-1&&temp.indexOf("%>")==-1){
			    	  nameoftemp="out.println(\""+temp+"\");\n";
			    	  out.write(nameoftemp.getBytes());
			      }else {//<%在一行
			    	  nameoftemp=temp.replaceAll("<%","").replaceAll("%>","")+"\n";
					  out.write(nameoftemp.getBytes());
			      } 
		      temp=br.readLine();
		}
		out.write("}\n}".getBytes());
		out.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public void compiler(String temp) {  
		String string="bin/"+temp.replaceAll("\\.", "_")+".java";
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();  
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();  
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);  
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromStrings(Arrays.asList(string));  
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null,  
                                                             compilationUnits);  
        Boolean success = task.call();  
        for (@SuppressWarnings("rawtypes") Diagnostic diagnostic : diagnostics.getDiagnostics()) {  
            System.console().printf("Code: %s%n" + "Kind: %s%n" + "Position: %s%n" + "Start Position: %s%n"  
                                            + "End Position: %s%n" + "Source: %s%n" + "Message:   %s%n",  
                                    diagnostic.getCode(), diagnostic.getKind(), diagnostic.getPosition(),  
                                    diagnostic.getStartPosition(), diagnostic.getEndPosition(), diagnostic.getSource(),  
                                    diagnostic.getMessage(null));  
        }  
        try {  
            fileManager.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        System.out.println("Success: " + success);  
    }  
}
