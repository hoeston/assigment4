import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLReader {
	   private  String classname=null;
	    public XMLReader(String URLstr){
	    	classname=getName(URLstr);
	    }
	public String getName(String URLstr) {

		Element element = null;
		// ����ʹ�þ���·��
		File f = new File("web.xml");
		// documentBuilderΪ������ֱ��ʵ����(��XML�ļ�ת��ΪDOM�ļ�)
		DocumentBuilder db = null;
		DocumentBuilderFactory dbf = null;
		String stringtem=null;
		try {
			// ����documentBuilderFactory����
			dbf = DocumentBuilderFactory.newInstance();
			// ����db������documentBuilderFatory�����÷���documentBuildr����
			db = dbf.newDocumentBuilder();
			// �õ�һ��DOM�����ظ�document����
			Document dt = db.parse(f);
			// �õ�һ��elment��Ԫ��
			element = dt.getDocumentElement();
			// ��ø��ڵ�
			NodeList childNodes = element.getChildNodes();
			// ������Щ�ӽڵ�
			for (int i = 0; i < childNodes.getLength(); i++) {
				// ���ÿ����Ӧλ��i�Ľ��
				Node node1 = childNodes.item(i);
				if ("servlet-mapping".equals(node1.getNodeName())) {					
					NodeList nodeDetail = node1.getChildNodes();
					for (int j = 0; j < nodeDetail.getLength(); j++) {
						Node detail = nodeDetail.item(j);
						if ("url-pattern".equals(detail.getNodeName())&& detail.getTextContent().equals(URLstr)){// ���code
							for (int k = 0; k < nodeDetail.getLength(); k++) {
								Node d = nodeDetail.item(k);
								if ("servlet-name".equals(d.getNodeName())){
										stringtem=d.getTextContent();
								break;
								}
							}
						    break;
						}
					}	
					
				}
			}
			if(stringtem!=null){
				
				for (int i = 0; i < childNodes.getLength(); i++) {
					// ���ÿ����Ӧλ��i�Ľ��
					Node node1 = childNodes.item(i);
					if ("servlet".equals(node1.getNodeName())) {					
						NodeList nodeDetail = node1.getChildNodes();
						for (int j = 0; j < nodeDetail.getLength(); j++) {
							Node detail = nodeDetail.item(j);
							if ("servlet-name".equals(detail.getNodeName())&& detail.getTextContent().equals(stringtem)){// ���code
								for (int k = 0; k < nodeDetail.getLength(); k++) {
									Node d = nodeDetail.item(k);
									if ("servlet-class".equals(d.getNodeName())){
											return d.getTextContent();
									}
								}
							    break;
							}
						}	
						
					}
				}
				
			}else{
			return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	public String getClass_Name(){
		return classname;
	}
}
