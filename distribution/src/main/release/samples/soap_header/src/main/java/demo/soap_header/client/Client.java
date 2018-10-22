begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|soap_header
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
name|Holder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|HeaderService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|HeaderTester
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|InHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|InHeaderResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|InoutHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|InoutHeaderResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|OutHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|OutHeaderResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|headers
operator|.
name|SOAPHeaderData
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
literal|"http://apache.org/headers"
argument_list|,
literal|"HeaderService"
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
name|toURI
argument_list|()
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
name|HeaderService
name|hs
init|=
operator|new
name|HeaderService
argument_list|(
name|wsdlURL
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|HeaderTester
name|proxy
init|=
name|hs
operator|.
name|getSoapPort
argument_list|()
decl_stmt|;
name|invokeInHeader
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|invokeOutHeader
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|invokeInOutHeader
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|invokeInHeader
parameter_list|(
name|HeaderTester
name|proxy
parameter_list|)
block|{
comment|// invoke inHeader operation
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking inHeader operation"
argument_list|)
expr_stmt|;
name|InHeader
name|me
init|=
operator|new
name|InHeader
argument_list|()
decl_stmt|;
name|me
operator|.
name|setRequestType
argument_list|(
literal|"CXF user"
argument_list|)
expr_stmt|;
name|SOAPHeaderData
name|headerInfo
init|=
operator|new
name|SOAPHeaderData
argument_list|()
decl_stmt|;
name|headerInfo
operator|.
name|setOriginator
argument_list|(
literal|"CXF client"
argument_list|)
expr_stmt|;
name|headerInfo
operator|.
name|setMessage
argument_list|(
literal|"Invoking inHeader operation"
argument_list|)
expr_stmt|;
name|InHeaderResponse
name|response
init|=
name|proxy
operator|.
name|inHeader
argument_list|(
name|me
argument_list|,
name|headerInfo
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\tinHeader invocation returned: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\tResult: "
operator|+
name|response
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|invokeOutHeader
parameter_list|(
name|HeaderTester
name|proxy
parameter_list|)
block|{
comment|// invoke outHeaderoperation
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking outHeader operation"
argument_list|)
expr_stmt|;
name|OutHeader
name|me
init|=
operator|new
name|OutHeader
argument_list|()
decl_stmt|;
name|me
operator|.
name|setRequestType
argument_list|(
literal|"CXF user"
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|OutHeaderResponse
argument_list|>
name|theResponse
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|SOAPHeaderData
argument_list|>
name|headerInfo
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|proxy
operator|.
name|outHeader
argument_list|(
name|me
argument_list|,
name|theResponse
argument_list|,
name|headerInfo
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\toutHeader invocation returned: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\tOut parameter: "
operator|+
name|theResponse
operator|.
name|value
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\tHeader content:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\tOriginator: "
operator|+
name|headerInfo
operator|.
name|value
operator|.
name|getOriginator
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\tMessage: "
operator|+
name|headerInfo
operator|.
name|value
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|invokeInOutHeader
parameter_list|(
name|HeaderTester
name|proxy
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking inoutHeader operation"
argument_list|)
expr_stmt|;
name|InoutHeader
name|me
init|=
operator|new
name|InoutHeader
argument_list|()
decl_stmt|;
name|me
operator|.
name|setRequestType
argument_list|(
literal|"CXF user"
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|SOAPHeaderData
argument_list|>
name|headerInfo
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|SOAPHeaderData
name|shd
init|=
operator|new
name|SOAPHeaderData
argument_list|()
decl_stmt|;
name|shd
operator|.
name|setOriginator
argument_list|(
literal|"CXF client"
argument_list|)
expr_stmt|;
name|shd
operator|.
name|setMessage
argument_list|(
literal|"Invoking inoutHeader operation"
argument_list|)
expr_stmt|;
name|headerInfo
operator|.
name|value
operator|=
name|shd
expr_stmt|;
name|InoutHeaderResponse
name|response
init|=
name|proxy
operator|.
name|inoutHeader
argument_list|(
name|me
argument_list|,
name|headerInfo
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\tinoutHeader invocation returned: "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\tResult: "
operator|+
name|response
operator|.
name|getResponseType
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\tHeader content:"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\tOriginator: "
operator|+
name|headerInfo
operator|.
name|value
operator|.
name|getOriginator
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\t\t\tMessage: "
operator|+
name|headerInfo
operator|.
name|value
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
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

