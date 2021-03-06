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
name|transport
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
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
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
import|;
end_import

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
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|JMSHeaderTypeTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_VALUE
init|=
literal|"test"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CONVERTED_RESPONSE_KEY
init|=
literal|"org__apache__cxf__message__Message__RESPONSE_CODE"
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testConversionIn
parameter_list|()
throws|throws
name|JMSException
block|{
name|Message
name|message
init|=
name|createMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setStringProperty
argument_list|(
name|CONVERTED_RESPONSE_KEY
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|JMSMessageHeadersType
name|messageHeaders
init|=
name|JMSMessageHeadersType
operator|.
name|from
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|messageHeaders
operator|.
name|getPropertyKeys
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|keys
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEST_VALUE
argument_list|,
name|messageHeaders
operator|.
name|getProperty
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConversionOut
parameter_list|()
throws|throws
name|JMSException
block|{
name|Message
name|message
init|=
name|createMessage
argument_list|()
decl_stmt|;
name|JMSMessageHeadersType
name|messageHeaders
init|=
operator|new
name|JMSMessageHeadersType
argument_list|()
decl_stmt|;
name|messageHeaders
operator|.
name|putProperty
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|RESPONSE_CODE
argument_list|,
name|TEST_VALUE
argument_list|)
expr_stmt|;
name|messageHeaders
operator|.
name|writeTo
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONVERTED_RESPONSE_KEY
argument_list|,
name|message
operator|.
name|getPropertyNames
argument_list|()
operator|.
name|nextElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Message
name|createMessage
parameter_list|()
throws|throws
name|JMSException
block|{
name|ActiveMQConnectionFactory
name|cf
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
literal|"vm://test?broker.persistent=false"
argument_list|)
decl_stmt|;
name|Connection
name|connection
init|=
name|cf
operator|.
name|createConnection
argument_list|()
decl_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
name|Session
name|session
init|=
name|connection
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|session
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|connection
operator|.
name|stop
argument_list|()
expr_stmt|;
return|return
name|message
return|;
block|}
block|}
end_class

end_unit

