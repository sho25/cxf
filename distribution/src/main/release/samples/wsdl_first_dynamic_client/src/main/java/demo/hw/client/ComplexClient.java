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
name|beans
operator|.
name|PropertyDescriptor
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
name|java
operator|.
name|net
operator|.
name|URL
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
name|endpoint
operator|.
name|Client
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
name|endpoint
operator|.
name|ClientImpl
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
name|endpoint
operator|.
name|Endpoint
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
name|jaxws
operator|.
name|endpoint
operator|.
name|dynamic
operator|.
name|JaxWsDynamicClientFactory
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|service
operator|.
name|model
operator|.
name|BindingMessageInfo
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|service
operator|.
name|model
operator|.
name|MessagePartInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ComplexClient
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
literal|"http://Company.com/Application"
argument_list|,
literal|"Company_ESB_Application_Biztalk_AgentDetails_4405_AgentDetails_Prt"
argument_list|)
decl_stmt|;
specifier|private
name|ComplexClient
parameter_list|()
block|{     }
comment|/**      * @param args      */
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|wsdlURL
argument_list|)
expr_stmt|;
name|JaxWsDynamicClientFactory
name|factory
init|=
name|JaxWsDynamicClientFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|factory
operator|.
name|createClient
argument_list|(
name|wsdlURL
operator|.
name|toExternalForm
argument_list|()
argument_list|,
name|SERVICE_NAME
argument_list|)
decl_stmt|;
name|ClientImpl
name|clientImpl
init|=
operator|(
name|ClientImpl
operator|)
name|client
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|clientImpl
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|ServiceInfo
name|serviceInfo
init|=
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|QName
name|bindingName
init|=
operator|new
name|QName
argument_list|(
literal|"http://Company.com/Application"
argument_list|,
literal|"Company_ESB_Application_Biztalk_AgentDetails_4405_AgentDetails_PrtSoap"
argument_list|)
decl_stmt|;
name|BindingInfo
name|binding
init|=
name|serviceInfo
operator|.
name|getBinding
argument_list|(
name|bindingName
argument_list|)
decl_stmt|;
comment|//{
name|QName
name|opName
init|=
operator|new
name|QName
argument_list|(
literal|"http://Company.com/Application"
argument_list|,
literal|"GetAgentDetails"
argument_list|)
decl_stmt|;
name|BindingOperationInfo
name|boi
init|=
name|binding
operator|.
name|getOperation
argument_list|(
name|opName
argument_list|)
decl_stmt|;
name|BindingMessageInfo
name|inputMessageInfo
init|=
name|boi
operator|.
name|getInput
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|MessagePartInfo
argument_list|>
name|parts
init|=
name|inputMessageInfo
operator|.
name|getMessageParts
argument_list|()
decl_stmt|;
comment|// only one part.
name|MessagePartInfo
name|partInfo
init|=
name|parts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|partClass
init|=
name|partInfo
operator|.
name|getTypeClass
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|partClass
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
comment|// GetAgentDetails
name|Object
name|inputObject
init|=
name|partClass
operator|.
name|newInstance
argument_list|()
decl_stmt|;
comment|// Unfortunately, the slot inside of the part object is also called 'part'.
comment|// this is the descriptor for get/set part inside the GetAgentDetails class.
name|PropertyDescriptor
name|partPropertyDescriptor
init|=
operator|new
name|PropertyDescriptor
argument_list|(
literal|"part"
argument_list|,
name|partClass
argument_list|)
decl_stmt|;
comment|// This is the type of the class which really contains all the parameter information.
name|Class
argument_list|<
name|?
argument_list|>
name|partPropType
init|=
name|partPropertyDescriptor
operator|.
name|getPropertyType
argument_list|()
decl_stmt|;
comment|// AgentWSRequest
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|partPropType
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|inputPartObject
init|=
name|partPropType
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|partPropertyDescriptor
operator|.
name|getWriteMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|inputObject
argument_list|,
name|inputPartObject
argument_list|)
expr_stmt|;
name|PropertyDescriptor
name|numberPropertyDescriptor
init|=
operator|new
name|PropertyDescriptor
argument_list|(
literal|"agentNumber"
argument_list|,
name|partPropType
argument_list|)
decl_stmt|;
name|numberPropertyDescriptor
operator|.
name|getWriteMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|inputPartObject
argument_list|,
operator|new
name|Integer
argument_list|(
literal|314159
argument_list|)
argument_list|)
expr_stmt|;
name|Object
index|[]
name|result
init|=
name|client
operator|.
name|invoke
argument_list|(
name|opName
argument_list|,
name|inputObject
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|resultClass
init|=
name|result
index|[
literal|0
index|]
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|resultClass
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
comment|// GetAgentDetailsResponse
name|PropertyDescriptor
name|resultDescriptor
init|=
operator|new
name|PropertyDescriptor
argument_list|(
literal|"agentWSResponse"
argument_list|,
name|resultClass
argument_list|)
decl_stmt|;
name|Object
name|wsResponse
init|=
name|resultDescriptor
operator|.
name|getReadMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|result
index|[
literal|0
index|]
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|wsResponseClass
init|=
name|wsResponse
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|wsResponseClass
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|PropertyDescriptor
name|agentNameDescriptor
init|=
operator|new
name|PropertyDescriptor
argument_list|(
literal|"agentName"
argument_list|,
name|wsResponseClass
argument_list|)
decl_stmt|;
name|String
name|agentName
init|=
operator|(
name|String
operator|)
name|agentNameDescriptor
operator|.
name|getReadMethod
argument_list|()
operator|.
name|invoke
argument_list|(
name|wsResponse
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Agent name: "
operator|+
name|agentName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

