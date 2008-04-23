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
name|callback
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|callback
operator|.
name|CallbackPortType
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
name|serviceName
operator|=
literal|"CallbackService"
argument_list|,
name|portName
operator|=
literal|"CallbackPort"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.callback.CallbackPortType"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/callback"
argument_list|,
name|wsdlLocation
operator|=
literal|"testutils/basic_callback_test.wsdl"
argument_list|)
specifier|public
class|class
name|CallbackImpl
implements|implements
name|CallbackPortType
block|{
comment|//private static final Logger LOG =
comment|//    Logger.getLogger(CallbackImpl.class.getPackage().getName());
comment|/**      * serverSayHi      * @param: return_message (String)      * @return: String      */
specifier|public
name|String
name|serverSayHi
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
operator|new
name|String
argument_list|(
literal|"Hi "
operator|+
name|message
argument_list|)
return|;
block|}
block|}
end_class

end_unit

