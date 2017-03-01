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
name|jms
operator|.
name|testsuite
operator|.
name|util
package|;
end_package

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
name|Iterator
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
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|StreamMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|testsuite
operator|.
name|testcase
operator|.
name|MessagePropertiesType
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
name|testsuite
operator|.
name|testcase
operator|.
name|TestCaseType
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
name|testsuite
operator|.
name|testcase
operator|.
name|TestCasesType
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
name|transport
operator|.
name|jms
operator|.
name|spec
operator|.
name|JMSSpecConstants
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|JMSTestUtil
block|{
specifier|private
specifier|static
name|TestCasesType
name|testcases
decl_stmt|;
specifier|private
name|JMSTestUtil
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|getFullAddress
parameter_list|(
name|String
name|partAddress
parameter_list|,
name|String
name|jndiUrl
parameter_list|)
block|{
name|String
name|separator
init|=
name|partAddress
operator|.
name|contains
argument_list|(
literal|"?"
argument_list|)
condition|?
literal|"&"
else|:
literal|"?"
decl_stmt|;
name|String
name|address
init|=
name|partAddress
operator|+
name|separator
operator|+
literal|"&jndiInitialContextFactory=org.apache.activemq.jndi.ActiveMQInitialContextFactory"
operator|+
literal|"&jndiConnectionFactoryName=ConnectionFactory"
operator|+
literal|"&jndiURL="
operator|+
name|jndiUrl
decl_stmt|;
return|return
name|address
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|TestCaseType
argument_list|>
name|getTestCases
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|testcases
operator|==
literal|null
condition|)
block|{
name|loadTestCases
argument_list|()
expr_stmt|;
block|}
return|return
name|testcases
operator|.
name|getTestCase
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|TestCaseType
name|getTestCase
parameter_list|(
name|String
name|testId
parameter_list|)
block|{
if|if
condition|(
name|testId
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Iterator
argument_list|<
name|TestCaseType
argument_list|>
name|iter
init|=
name|getTestCases
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|TestCaseType
name|testcase
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|testId
operator|.
name|equals
argument_list|(
name|testcase
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|testcase
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
name|void
name|loadTestCases
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXBContext
name|context
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.cxf.testsuite.testcase"
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|e
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|new
name|JMSTestUtil
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/cxf/jms/testsuite/util/testcases.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|testcases
operator|=
operator|(
name|TestCasesType
operator|)
name|e
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
comment|/**      * @param testcase      * @param session      * @param rtd      * @return      * @throws JMSException      */
specifier|public
specifier|static
name|Message
name|buildJMSMessageFromTestCase
parameter_list|(
name|TestCaseType
name|testcase
parameter_list|,
name|Session
name|session
parameter_list|,
name|Destination
name|rtd
parameter_list|)
throws|throws
name|JMSException
block|{
name|MessagePropertiesType
name|messageProperties
init|=
name|testcase
operator|.
name|getRequestMessage
argument_list|()
decl_stmt|;
name|Message
name|jmsMessage
init|=
literal|null
decl_stmt|;
name|String
name|messageType
init|=
name|messageProperties
operator|.
name|getMessageType
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"text"
operator|.
name|equals
argument_list|(
name|messageType
argument_list|)
condition|)
block|{
name|jmsMessage
operator|=
name|session
operator|.
name|createTextMessage
argument_list|()
expr_stmt|;
operator|(
operator|(
name|TextMessage
operator|)
name|jmsMessage
operator|)
operator|.
name|setText
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"byte"
operator|.
name|equals
argument_list|(
name|messageType
argument_list|)
condition|)
block|{
name|jmsMessage
operator|=
name|session
operator|.
name|createBytesMessage
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"stream"
operator|.
name|equals
argument_list|(
name|messageType
argument_list|)
condition|)
block|{
name|jmsMessage
operator|=
name|session
operator|.
name|createStreamMessage
argument_list|()
expr_stmt|;
operator|(
operator|(
name|StreamMessage
operator|)
name|jmsMessage
operator|)
operator|.
name|writeString
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|jmsMessage
operator|=
name|session
operator|.
name|createBytesMessage
argument_list|()
expr_stmt|;
block|}
name|jmsMessage
operator|.
name|setJMSReplyTo
argument_list|(
name|rtd
argument_list|)
expr_stmt|;
if|if
condition|(
name|messageProperties
operator|.
name|isSetDeliveryMode
argument_list|()
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSDeliveryMode
argument_list|(
name|messageProperties
operator|.
name|getDeliveryMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetExpiration
argument_list|()
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSExpiration
argument_list|(
name|messageProperties
operator|.
name|getExpiration
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetPriority
argument_list|()
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSPriority
argument_list|(
name|messageProperties
operator|.
name|getPriority
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetExpiration
argument_list|()
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSPriority
argument_list|(
name|messageProperties
operator|.
name|getExpiration
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetCorrelationID
argument_list|()
condition|)
block|{
name|jmsMessage
operator|.
name|setJMSCorrelationID
argument_list|(
name|messageProperties
operator|.
name|getCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetTargetService
argument_list|()
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|messageProperties
operator|.
name|getTargetService
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|TARGETSERVICE_FIELD
argument_list|,
name|messageProperties
operator|.
name|getTargetService
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetBindingVersion
argument_list|()
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|messageProperties
operator|.
name|getBindingVersion
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|BINDINGVERSION_FIELD
argument_list|,
name|messageProperties
operator|.
name|getBindingVersion
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetContentType
argument_list|()
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|messageProperties
operator|.
name|getContentType
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|CONTENTTYPE_FIELD
argument_list|,
name|messageProperties
operator|.
name|getContentType
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetSoapAction
argument_list|()
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|messageProperties
operator|.
name|getSoapAction
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|SOAPACTION_FIELD
argument_list|,
name|messageProperties
operator|.
name|getSoapAction
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageProperties
operator|.
name|isSetRequestURI
argument_list|()
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|messageProperties
operator|.
name|getRequestURI
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|jmsMessage
operator|.
name|setStringProperty
argument_list|(
name|JMSSpecConstants
operator|.
name|REQUESTURI_FIELD
argument_list|,
name|messageProperties
operator|.
name|getRequestURI
argument_list|()
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|jmsMessage
return|;
block|}
block|}
end_class

end_unit

