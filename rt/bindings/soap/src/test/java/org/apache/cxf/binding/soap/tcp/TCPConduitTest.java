begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|tcp
package|;
end_package

begin_comment
comment|//import java.io.ByteArrayInputStream;
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|//import javax.xml.stream.XMLStreamException;
end_comment

begin_comment
comment|//import javax.xml.stream.XMLStreamReader;
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_comment
comment|//import org.apache.cxf.staxutils.StaxUtils;
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|AttributedURIType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_comment
comment|//import org.junit.Ignore;
end_comment

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_class
annotation|@
name|Ignore
specifier|public
class|class
name|TCPConduitTest
block|{
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Test
specifier|public
name|void
name|testTCPConduit
parameter_list|()
block|{
comment|//TCPConduit tcpConduit = new TCPConduit(null);
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPrepare
parameter_list|()
block|{
comment|//int num1 = 2;
comment|//int num2 = 3;
comment|/*         final String messageData = "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\""             + " xmlns:a=\"http://www.w3.org/2005/08/addressing\"><s:Header><a:Action s:mustUnderstand=\"1\">"             + "http://tempuri.org/ICalculator/add</a:Action>"             + "<a:MessageID>urn:uuid:e2606099-5bef-4db2-b661-19a883bab4e7</a:MessageID><a:ReplyTo>"             + "<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo>"             + "<a:To s:mustUnderstand=\"1\">soap.tcp://localhost:9999/calculator</a:To></s:Header><s:Body>"             + "<add xmlns=\"http://tempuri.org/\">"             + "<i>" + num1 + "</i>"             + "<j>" + num2 + "</j>"             + "</add></s:Body></s:Envelope>";             */
comment|/*final String messageData = "<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">"             + "<S:Body><ns2:add xmlns:ns2=\"http://calculator.me.org/\"><i>"             + num1 + "</i><j>" + num2 + "</j></ns2:add></S:Body></S:Envelope>";*/
name|String
name|name
init|=
operator|new
name|String
argument_list|(
literal|"CXF"
argument_list|)
decl_stmt|;
comment|/*for (int i = 0; i< 6000; i++) {             name += "A";         }*/
specifier|final
name|String
name|messageData
init|=
literal|"<S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<S:Body><sayHi><text>"
operator|+
name|name
operator|+
literal|"</text></sayHi></S:Body></S:Envelope>"
decl_stmt|;
specifier|final
name|AttributedURIType
name|a
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
comment|//a.setValue("soap.tcp://localhost:8080/CalculatorApp/CalculatorWSService");
name|a
operator|.
name|setValue
argument_list|(
literal|"soap.tcp://localhost:9999/HelloWorld"
argument_list|)
expr_stmt|;
specifier|final
name|EndpointReferenceType
name|t
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|t
operator|.
name|setAddress
argument_list|(
name|a
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|TCPConduit
name|tcpConduit
init|=
operator|new
name|TCPConduit
argument_list|(
name|t
argument_list|)
decl_stmt|;
name|tcpConduit
operator|.
name|setMessageObserver
argument_list|(
operator|new
name|TestMessageObserver
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Message
name|msg
init|=
name|getNewMessage
argument_list|()
decl_stmt|;
name|tcpConduit
operator|.
name|prepare
argument_list|(
name|msg
argument_list|)
expr_stmt|;
specifier|final
name|OutputStream
name|out
init|=
name|msg
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|messageData
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|tcpConduit
operator|.
name|close
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|Message
name|getNewMessage
parameter_list|()
block|{
name|Message
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|=
operator|new
name|SoapMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|contentTypes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|contentTypes
operator|.
name|add
argument_list|(
literal|"text/xml"
argument_list|)
expr_stmt|;
name|contentTypes
operator|.
name|add
argument_list|(
literal|"charset=utf8"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"content-type"
argument_list|,
name|contentTypes
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|headers
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
specifier|private
class|class
name|TestMessageObserver
implements|implements
name|MessageObserver
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
block|{
comment|//int correctResult = 5;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|InputStream
name|input
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|byte
name|response
index|[]
init|=
literal|null
decl_stmt|;
try|try
block|{
name|response
operator|=
operator|new
name|byte
index|[
name|input
operator|.
name|available
argument_list|()
index|]
expr_stmt|;
name|input
operator|.
name|read
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|response
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
comment|/*try {                 ByteArrayInputStream bais = new ByteArrayInputStream(response);                  XMLStreamReader xmlReader = StaxUtils.createXMLStreamReader(bais, "UTF-8");                 while (xmlReader.hasNext()) {                     xmlReader.next();                     if (xmlReader.getEventType() == XMLStreamReader.START_ELEMENT&& xmlReader.getLocalName().equals("addResult")) {                         assertEquals(correctResult, Integer.parseInt(xmlReader.getElementText()));                     }                 }             } catch (XMLStreamException e) {                 e.printStackTrace();             }*/
block|}
block|}
block|}
end_class

end_unit

