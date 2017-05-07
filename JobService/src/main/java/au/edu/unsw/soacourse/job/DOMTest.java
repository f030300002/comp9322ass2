package au.edu.unsw.soacourse.job;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class Book {
    private String bookNo;
    private String title;
    private String author;
    private double price;

    public void setBookNo(String bookNo) {
        this.bookNo = bookNo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getBookNo() {
        return bookNo;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookNo='" + bookNo + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }

    public Book() {
    }

    public Book(String bookNo, String title, String author, double price) {
        this.bookNo = bookNo;
        this.title = title;
        this.author = author;
        this.price = price;
    }
}


public class DOMTest {
	
	private static List<Book> parseXml(String fileName) {
		List<Book> list = new ArrayList<>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
        try {
        	builder = factory.newDocumentBuilder();
        	document = builder.parse(new File(fileName));
        } catch (Exception e) {
        	e.printStackTrace();
        }
        Node rootNode = document.getDocumentElement();
        NodeList bookElementList = rootNode.getChildNodes();
        for (int i = 0; i < bookElementList.getLength(); i ++) {
        	Node bookElement = bookElementList.item(i);
        	if (bookElement.getNodeName().equals("book")) {
        		Book book = new Book();
        		NodeList subBookElementList = bookElement.getChildNodes();
        		for (int j = 0; j < subBookElementList.getLength(); j ++) {
        			Node subElementNode =  subBookElementList.item(j);
        			String subElementName = subElementNode.getNodeName();
        			String subElementContent = subElementNode.getTextContent().trim();
        			if (subElementName.equals("title"))
        				book.setTitle(subElementContent);
        			else if (subElementName.equals("author"))
        				book.setAuthor(subElementContent);
        			else if (subElementName.equals("price"))
        				book.setPrice(Double.parseDouble(subElementContent));
        		}
        		list.add(book);
        	}
        }
        return list;
	}
	
	private static void addBookElement(String fileName, Book newBook) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
        try {
        	builder = factory.newDocumentBuilder();
        	document = builder.parse(new File(fileName));
        } catch (Exception e) {
        	e.printStackTrace();
        }
        Node rootNode = document.getDocumentElement();
        
        // build new book element
        Element book = document.createElement("book");
        rootNode.appendChild(book);
        Element title = document.createElement("title");
        Element author = document.createElement("author");
        Element price = document.createElement("price");
        book.appendChild(title);
        book.appendChild(author);
        book.appendChild(price);
        title.appendChild(document.createTextNode(newBook.getTitle()));
        author.appendChild(document.createTextNode(newBook.getAuthor()));
        price.appendChild(document.createTextNode(String.valueOf(newBook.getPrice())));
        
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
			transformer = transformerFactory.newTransformer();
		} catch (Exception e) {
			e.printStackTrace();
		}
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(fileName));
        try {
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		List<Book> list;
		list = parseXml("books.xml");
		for (Book book : list)
			System.out.println(book.toString());
		System.out.println("===========================");
		Book newBook = new Book("bookNo.03", "GL HF", "GG", -999);
		addBookElement("books.xml", newBook);
		list = parseXml("books.xml");
		for (Book book : list)
			System.out.println(book.toString());
		System.out.println("===========================");
	}

}
