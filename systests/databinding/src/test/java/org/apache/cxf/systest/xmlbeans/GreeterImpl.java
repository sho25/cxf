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
name|systest
operator|.
name|xmlbeans
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|helloWorldSoapHttpXmlbeans
operator|.
name|xmlbeans
operator|.
name|types
operator|.
name|FaultDetailDocument
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|helloWorldSoapHttpXmlbeans
operator|.
name|xmlbeans
operator|.
name|types
operator|.
name|FaultDetailDocument
operator|.
name|FaultDetail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|helloWorldSoapHttpXmlbeans
operator|.
name|xmlbeans
operator|.
name|types
operator|.
name|TestEnum
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http_xmlbeans
operator|.
name|xmlbeans
operator|.
name|GreetMeFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http_xmlbeans
operator|.
name|xmlbeans
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
name|hello_world_soap_http_xmlbeans
operator|.
name|xmlbeans
operator|.
name|PingMeFault
import|;
end_import

begin_class
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|portName
operator|=
literal|"SoapPort"
argument_list|,
name|serviceName
operator|=
literal|"SOAPService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/hello_world_soap_http_xmlbeans/xmlbeans"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.hello_world_soap_http_xmlbeans.xmlbeans.Greeter"
argument_list|)
specifier|public
class|class
name|GreeterImpl
implements|implements
name|Greeter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|GreeterImpl
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/* (non-Javadoc)      * @see org.apache.hello_world_soap_http.Greeter#greetMe(java.lang.String)      */
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|me
parameter_list|)
throws|throws
name|GreetMeFault
block|{
if|if
condition|(
literal|"fault"
operator|.
name|equals
argument_list|(
name|me
argument_list|)
condition|)
block|{
name|org
operator|.
name|apache
operator|.
name|helloWorldSoapHttpXmlbeans
operator|.
name|xmlbeans
operator|.
name|types
operator|.
name|GreetMeFaultDetailDocument
name|detail
init|=
name|org
operator|.
name|apache
operator|.
name|helloWorldSoapHttpXmlbeans
operator|.
name|xmlbeans
operator|.
name|types
operator|.
name|GreetMeFaultDetailDocument
operator|.
name|Factory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setGreetMeFaultDetail
argument_list|(
literal|"Some fault detail"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|GreetMeFault
argument_list|(
literal|"Fault String"
argument_list|,
name|detail
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation greetMe"
argument_list|)
expr_stmt|;
return|return
literal|"Hello "
operator|+
name|me
return|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.hello_world_soap_http.Greeter#greetMeOneWay(java.lang.String)      */
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|me
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation greetMeOneWay"
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.hello_world_soap_http.Greeter#sayHi()      */
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation sayHi"
argument_list|)
expr_stmt|;
return|return
literal|"Bonjour"
return|;
block|}
specifier|public
name|void
name|pingMe
parameter_list|()
throws|throws
name|PingMeFault
block|{
comment|// here we need to put the FaultDetail into the FaultDetailDocument
name|FaultDetailDocument
name|faultDocument
init|=
name|org
operator|.
name|apache
operator|.
name|helloWorldSoapHttpXmlbeans
operator|.
name|xmlbeans
operator|.
name|types
operator|.
name|FaultDetailDocument
operator|.
name|Factory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|FaultDetail
name|faultDetail
init|=
name|faultDocument
operator|.
name|addNewFaultDetail
argument_list|()
decl_stmt|;
name|faultDetail
operator|.
name|setMajor
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|)
expr_stmt|;
name|faultDetail
operator|.
name|setMinor
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Executing operation pingMe, throwing PingMeFault exception"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|PingMeFault
argument_list|(
literal|"PingMeFault raised by server"
argument_list|,
name|faultDocument
argument_list|)
throw|;
block|}
specifier|public
name|String
index|[]
name|sayHiArray
parameter_list|(
name|String
name|requests
index|[]
parameter_list|)
block|{
name|String
name|ret
index|[]
init|=
operator|new
name|String
index|[
name|requests
operator|.
name|length
operator|+
literal|1
index|]
decl_stmt|;
name|ret
index|[
literal|0
index|]
operator|=
literal|"Hello"
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|requests
argument_list|,
literal|0
argument_list|,
name|ret
argument_list|,
literal|1
argument_list|,
name|requests
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
name|ret
return|;
block|}
specifier|public
name|TestEnum
operator|.
name|Enum
name|sayHiEnum
parameter_list|(
name|TestEnum
operator|.
name|Enum
name|request
parameter_list|)
block|{
return|return
name|request
return|;
block|}
block|}
end_class

end_unit

