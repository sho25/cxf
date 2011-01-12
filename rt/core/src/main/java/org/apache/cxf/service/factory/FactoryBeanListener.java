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
name|service
operator|.
name|factory
package|;
end_package

begin_comment
comment|/**  *   */
end_comment

begin_interface
specifier|public
interface|interface
name|FactoryBeanListener
block|{
enum|enum
name|Event
block|{
comment|/**          * Event fired at the very start of processing.  No parameters.  Useful          * for setting up any state the listener may need to maintain.          */
name|START_CREATE
block|,
comment|/**          * Event fired at the very end of processing.   One parameter is passed          * in which is the Service object that was created.          */
name|END_CREATE
block|,
comment|/**          * Called at the start of processing when it detects that the service          * is to be created based on a wsdl contract.   One String parameter          * of the URL of the wsdl.          */
name|CREATE_FROM_WSDL
block|,
comment|/**          * Called at the start of processing when it detects that the service          * is to be created based on a Java class.  One Class<?> parameter          * of the class that is being analyzed.          */
name|CREATE_FROM_CLASS
block|,
comment|/**          * Called after the wsdl is loaded/parsed.   Single parameter of the          * WSS4J Definition of the WSDL.          */
name|WSDL_LOADED
block|,
comment|/**          * Called after the Service is set into the Factory after which the getService()          * call will return a valid value.  One parameter of the Service object.          */
name|SERVICE_SET
block|,
comment|/**          * OperationInfo, Method          */
name|INTERFACE_OPERATION_BOUND
block|,
comment|/**          * OperationInfo, Method, MessageInfo          */
name|OPERATIONINFO_IN_MESSAGE_SET
block|,
name|OPERATIONINFO_OUT_MESSAGE_SET
block|,
comment|/**          * OperationInfo, Class<? extends Throwable>, FaultInfo          */
name|OPERATIONINFO_FAULT
block|,
comment|/**          * InterfaceInfo, Class<?>          */
name|INTERFACE_CREATED
block|,
comment|/**          * DataBinding          */
name|DATABINDING_INITIALIZED
block|,
comment|/**          * EndpointInfo, Endpoint, Class          */
name|ENDPOINT_CREATED
block|,
comment|/**          * Server, targetObject, Class          */
name|SERVER_CREATED
block|,
comment|/**          * BindingInfo, BindingOperationInfo          */
name|BINDING_OPERATION_CREATED
block|,
comment|/**          * BindingInfo          */
name|BINDING_CREATED
block|,
comment|/**          * Endpoint, Client          */
name|CLIENT_CREATED
block|,
comment|/**          * EndpointInfo, Endpoint, SEI Class, Class           */
name|ENDPOINT_SELECTED
block|,
comment|/**          * EndpointInfo          */
name|ENDPOINTINFO_CREATED
block|,
comment|/**          * Class[], InvokationHandler, Proxy          */
name|PROXY_CREATED
block|,     }
empty_stmt|;
name|void
name|handleEvent
parameter_list|(
name|Event
name|ev
parameter_list|,
name|AbstractServiceFactoryBean
name|factory
parameter_list|,
name|Object
modifier|...
name|args
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

