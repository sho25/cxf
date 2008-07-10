begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|hw
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|PingMeFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|JSDemoService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|types
operator|.
name|FaultDetail
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
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"JSDemoService"
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
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
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
name|URL
name|wsdlURL
decl_stmt|;
name|File
name|wsdlFile
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
if|if
condition|(
name|wsdlFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|wsdlURL
operator|=
name|wsdlFile
operator|.
name|toURL
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|wsdlURL
operator|=
operator|new
name|URL
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|wsdlURL
argument_list|)
expr_stmt|;
name|JSDemoService
name|ss
init|=
operator|new
name|JSDemoService
argument_list|(
name|wsdlURL
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|Greeter
name|port
init|=
name|ss
operator|.
name|getJSDemoPort
argument_list|()
decl_stmt|;
name|String
name|resp
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
name|resp
operator|=
name|port
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server responded with: "
operator|+
name|resp
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
name|resp
operator|=
name|port
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
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server responded with: "
operator|+
name|resp
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
literal|"Invoking greetMe with invalid length string, expecting exception..."
argument_list|)
expr_stmt|;
try|try
block|{
name|resp
operator|=
name|port
operator|.
name|greetMe
argument_list|(
literal|"Invoking greetMe with invalid length string, expecting exception..."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Caught expected WebServiceException:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"    "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|port
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
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking pingMe, expecting exception..."
argument_list|)
expr_stmt|;
name|port
operator|.
name|pingMe
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Expected exception: PingMeFault has occurred: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|FaultDetail
name|detail
init|=
name|ex
operator|.
name|getFaultInfo
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FaultDetail major:"
operator|+
name|detail
operator|.
name|getMajor
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FaultDetail minor:"
operator|+
name|detail
operator|.
name|getMinor
argument_list|()
argument_list|)
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

