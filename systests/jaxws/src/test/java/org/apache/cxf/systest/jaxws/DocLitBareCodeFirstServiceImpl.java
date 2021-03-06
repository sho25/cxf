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
name|jaxws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPFault
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPFaultException
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.systest.jaxws.DocLitBareCodeFirstService"
argument_list|,
name|serviceName
operator|=
literal|"DocLitBareCodeFirstService"
argument_list|,
name|portName
operator|=
literal|"DocLitBareCodeFirstServicePort"
argument_list|,
name|targetNamespace
operator|=
literal|"http://cxf.apache.org/systest/jaxws/DocLitBareCodeFirstService"
argument_list|)
specifier|public
class|class
name|DocLitBareCodeFirstServiceImpl
implements|implements
name|DocLitBareCodeFirstService
block|{
specifier|public
name|GreetMeResponse
name|greetMe
parameter_list|(
name|GreetMeRequest
name|gmr
parameter_list|)
block|{
if|if
condition|(
literal|"fault"
operator|.
name|equals
argument_list|(
name|gmr
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|SOAPFactory
name|factory
init|=
name|SOAPFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|SOAPFault
name|fault
init|=
name|factory
operator|.
name|createFault
argument_list|(
literal|"this is a fault string!"
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://foo"
argument_list|,
literal|"FooCode"
argument_list|)
argument_list|)
decl_stmt|;
name|fault
operator|.
name|setFaultActor
argument_list|(
literal|"mr.actor"
argument_list|)
expr_stmt|;
name|fault
operator|.
name|addDetail
argument_list|()
operator|.
name|addChildElement
argument_list|(
literal|"test"
argument_list|)
operator|.
name|addTextNode
argument_list|(
literal|"TestText"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|SOAPFaultException
argument_list|(
name|fault
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|SOAPException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WebServiceException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"emptyfault"
operator|.
name|equals
argument_list|(
name|gmr
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Empty!"
argument_list|)
throw|;
block|}
name|GreetMeResponse
name|resp
init|=
operator|new
name|GreetMeResponse
argument_list|()
decl_stmt|;
name|resp
operator|.
name|setName
argument_list|(
name|gmr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|resp
return|;
block|}
specifier|public
name|BigInteger
index|[]
name|sayTest
parameter_list|(
name|SayTestRequest
name|parameter
parameter_list|)
block|{
return|return
operator|new
name|BigInteger
index|[]
block|{
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
block|,
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
block|,
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|2
argument_list|)
block|,
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|3
argument_list|)
block|,         }
return|;
block|}
specifier|public
name|GMonthTest
name|echoGMonthTest
parameter_list|(
name|GMonthTest
name|input
parameter_list|)
block|{
return|return
name|input
return|;
block|}
block|}
end_class

end_unit

