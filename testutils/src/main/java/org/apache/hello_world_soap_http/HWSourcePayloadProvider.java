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
name|hello_world_soap_http
package|;
end_package

begin_comment
comment|//import java.util.logging.Logger;
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|Provider
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
name|WebServiceProvider
import|;
end_import

begin_class
annotation|@
name|WebServiceProvider
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
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
name|wsdlLocation
operator|=
literal|"resources/wsdl/hello_world.wsdl"
argument_list|)
specifier|public
class|class
name|HWSourcePayloadProvider
implements|implements
name|Provider
argument_list|<
name|DOMSource
argument_list|>
block|{
comment|//private static final Logger LOG =
comment|//    Logger.getLogger(AnnotatedGreeterImpl.class.getName());
specifier|private
name|int
name|invokeCount
decl_stmt|;
specifier|public
name|HWSourcePayloadProvider
parameter_list|()
block|{
comment|//Complete
block|}
specifier|public
name|DOMSource
name|invoke
parameter_list|(
name|DOMSource
name|source
parameter_list|)
block|{
name|invokeCount
operator|++
expr_stmt|;
return|return
name|source
return|;
block|}
specifier|public
name|int
name|getInvokeCount
parameter_list|()
block|{
return|return
name|invokeCount
return|;
block|}
block|}
end_class

end_unit

