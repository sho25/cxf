begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jms_greeter
operator|.
name|client
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Closeable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|jms_greeter
operator|.
name|JMSGreeterPortType
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
name|jms_greeter
operator|.
name|JMSGreeterService
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|Client
block|{
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"JMSGreeterService"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://cxf.apache.org/jms_greeter"
argument_list|,
literal|"GreeterPort"
argument_list|)
decl_stmt|;
specifier|private
name|Client
parameter_list|()
block|{     }
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|args
operator|=
operator|new
name|String
index|[
literal|1
index|]
expr_stmt|;
name|args
index|[
literal|0
index|]
operator|=
literal|"./wsdl/jms_greeter.wsdl"
expr_stmt|;
if|if
condition|(
name|args
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"please specify wsdl"
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|File
name|wsdl
init|=
operator|new
name|File
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|JMSGreeterService
name|service
init|=
operator|new
name|JMSGreeterService
argument_list|(
name|wsdl
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|JMSGreeterPortType
name|greeter
init|=
operator|(
name|JMSGreeterPortType
operator|)
name|service
operator|.
name|getPort
argument_list|(
name|PORT_NAME
argument_list|,
name|JMSGreeterPortType
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking sayHi..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"server responded with: "
operator|+
name|greeter
operator|.
name|sayHi
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking greetMe..."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"server responded with: "
operator|+
name|greeter
operator|.
name|greetMe
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.name"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking greetMeOneWay..."
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.name"
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No response from server as method is OneWay"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
if|if
condition|(
name|greeter
operator|instanceof
name|Closeable
condition|)
block|{
operator|(
operator|(
name|Closeable
operator|)
name|greeter
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

