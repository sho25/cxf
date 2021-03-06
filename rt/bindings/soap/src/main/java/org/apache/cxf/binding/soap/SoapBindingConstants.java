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
package|;
end_package

begin_class
specifier|public
specifier|final
class|class
name|SoapBindingConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|BINDING_STYLE_RPC
init|=
literal|"rpc"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|BINDING_STYLE_DOC
init|=
literal|"document"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PARAMETER_STYLE_BARE
init|=
literal|"bare"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PARAMETER_STYLE_WRAPPED
init|=
literal|"wrapped"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_ACTION
init|=
literal|"SOAPAction"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP11_BINDING_ID
init|=
literal|"http://schemas.xmlsoap.org/soap/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SOAP12_BINDING_ID
init|=
literal|"http://schemas.xmlsoap.org/wsdl/soap12/"
decl_stmt|;
specifier|private
name|SoapBindingConstants
parameter_list|()
block|{
comment|//utility class
block|}
block|}
end_class

end_unit

