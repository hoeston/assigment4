import java.io.*;
import javax.servlet.*;
public class test_jsp implements Servlet {  
public void destroy(){}
public ServletConfig getServletConfig() {return null;}
public String getServletInfo(){return null;}
public void init(ServletConfig arg0) throws ServletException {}
public void service(ServletRequest arg0, ServletResponse arg1)throws ServletException, IOException {
PrintWriter out = arg1.getWriter();
out.println("<html>");
out.println("<head>");
out.println("<title>i don't want to do homework!!!!</title>");
out.println("</head>");
out.println("<body>");
out.println("<center>");
out.println("<h1>");
out.println("i don't want to do homework!!!!");
out.println("</h1>");
out.println("</center>");
for(int i=0;i<10;i++) {
out.println("homework is too heavy for me!");
out.println("");
out.println("<br/>");
} 
out.println("</body>");
out.println("</html>");
}
}